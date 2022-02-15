package cn.chuanwise.xiaoming.host.graphical.stage.content;

import cn.chuanwise.xiaoming.host.graphical.GraphicalHostApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class ContentController {
    @FXML
    private BorderPane contentBorderPane;

    @FXML
    private TextField commandTextField;

    @FXML
    private Button stopButton;

    @FXML
    private Text xiaoMingVersionText;

    @FXML
    private Text accountCodeText;

    @FXML
    private TitledPane receiptionistTitlePane;

    @FXML
    private TitledPane pluginTitlePane;

    @FXML
    private TextArea contentTextArea;

    @FXML
    private Menu botMenu;

    @FXML
    private Menu helpMenu;

    @FXML
    private SplitPane contentSplitPane;

    public void setup(GraphicalHostApplication application) {
//        application.get

        pluginTitlePane.setContent(application.getPluginTableView());

        contentSplitPane.getItems().add(application.getOutTextArea());
    }

    @FXML
    void onExecute(ActionEvent event) {
        System.out.println("exe");
    }

    @FXML
    void onStop(ActionEvent event) {
        System.out.println("stop");
    }
}