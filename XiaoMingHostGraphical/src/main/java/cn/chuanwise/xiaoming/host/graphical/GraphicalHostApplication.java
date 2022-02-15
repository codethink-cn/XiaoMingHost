package cn.chuanwise.xiaoming.host.graphical;

import cn.chuanwise.toolkit.preservable.loader.FileLoader;
import cn.chuanwise.util.Serializers;
import cn.chuanwise.xiaoming.bot.XiaoMingBot;
import cn.chuanwise.xiaoming.host.configuration.HostConfiguration;
import cn.chuanwise.xiaoming.host.graphical.stage.content.ContentController;
import cn.chuanwise.xiaoming.host.graphical.plugin.PluginTableElement;
import cn.chuanwise.xiaoming.host.graphical.plugin.PluginTableView;
import cn.chuanwise.xiaoming.host.graphical.stage.init.InitStage;
import cn.chuanwise.xiaoming.host.graphical.util.AlertUtil;
import cn.chuanwise.xiaoming.host.graphical.util.FxmlUtil;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
public class GraphicalHostApplication extends Application {
    protected final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);

    protected final PrintStream outBeforeStart = System.out;
    protected final PrintStream errBeforeStart = System.err;
    protected XiaoMingBot xiaomingBot;

    @Data
    @SuppressWarnings("all")
    public class TextAreaPrintStream extends PrintStream {
        final TextArea console;

        public TextAreaPrintStream(TextArea console) {
            super(new ByteArrayOutputStream());
            this.console = console;
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            print(new String(buf, off, len));
        }

        @Override
        public void print(String string) {
            console.appendText(string);
        }
    }

    final TextArea outTextArea = new TextArea();
    final TextAreaPrintStream outPrintStream = new TextAreaPrintStream(outTextArea);
    final TextAreaPrintStream errPrintStream = new TextAreaPrintStream(outTextArea);

    final PluginTableView pluginTableView = new PluginTableView();

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.setOut(outPrintStream);
        System.setOut(errPrintStream);
        outTextArea.setEditable(false);

        // check first init
        final File launcherDirectory = new File("launcher");
        HostConfiguration configuration = null;
        if (launcherDirectory.isDirectory()) {
            final File configurationFile = new File(launcherDirectory, "launcher.json");
            if (configurationFile.isFile()) {
                try {
                    configuration = new FileLoader(Serializers.newJacksonSerializer()).load(HostConfiguration.class, configurationFile);
                } catch (IOException exception) {
                    AlertUtil.error("加载配置文件时出现错误", "很可能是配置文件 " + configurationFile.getAbsolutePath() + " 出现语法错误。\n" +
                            "你可以关闭程序，或重新开始配置");
                }
            }
        }
        if (Objects.isNull(configuration)) {
            final InitStage initStage = new InitStage();
            initStage.showAndWait();
            xiaomingBot = initStage.getXiaomingBot();
            if (Objects.isNull(xiaomingBot)) {
                return;
            }
        }

        final FXMLLoader fxmlLoader = FxmlUtil.load("content");
        final BorderPane borderPane = fxmlLoader.getRoot();
        final ContentController controller = fxmlLoader.getController();
        controller.setup(this);

        primaryStage.setScene(new Scene(borderPane));

        primaryStage.getIcons().add(new Image("icons/main.jpg"));

        primaryStage.setTitle("XiaoMingHost");
        primaryStage.show();

        // 轮询更新插件信息
        threadPool.scheduleWithFixedDelay(() -> {
            final ObservableList<PluginTableElement> items = pluginTableView.getItems();
            items.setAll(xiaomingBot.getPluginManager()
                    .getPlugins()
                    .values()
                    .stream()
                    .map(PluginTableElement::new)
                    .collect(Collectors.toList()));
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() throws Exception {
        threadPool.shutdown();

        System.setOut(outBeforeStart);
        System.setErr(errBeforeStart);
    }
}
