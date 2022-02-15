package cn.chuanwise.xiaoming.host.graphical.stage.init;

import cn.chuanwise.xiaoming.bot.XiaoMingBot;
import cn.chuanwise.xiaoming.host.graphical.util.FxmlUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class InitStage extends Stage {
    XiaoMingBot xiaomingBot;

    final Object condition = new Object();

    public InitStage() throws Exception {
        final FXMLLoader loader = FxmlUtil.load("init");
        final BorderPane borderPane = loader.getRoot();
        final InitController controller = loader.getController();
        controller.init(this);
        setResizable(false);
        setScene(new Scene(borderPane));
    }
}
