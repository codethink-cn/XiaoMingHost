package cn.chuanwise.xiaoming.host.graphical.util;

import cn.chuanwise.util.StaticUtil;
import javafx.scene.control.Alert;

public class AlertUtil extends StaticUtil {
    public static void info(String head, String content) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("消息");

        alert.setHeaderText(head);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void error(String head, String content) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("错误");

        alert.setHeaderText(head);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
