package cn.chuanwise.xiaoming.host.graphical.util;

import cn.chuanwise.util.Preconditions;
import cn.chuanwise.util.StaticUtil;
import javafx.fxml.FXMLLoader;

import java.net.URL;
import java.util.Objects;

public class FxmlUtil extends StaticUtil {
    public static FXMLLoader load(String path) throws Exception {
        final URL url = Objects.requireNonNull(FxmlUtil.class
                        .getClassLoader()
                        .getResource("fxml\\" + path + ".fxml"))
                .toURI()
                .toURL();
        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.load();
        return fxmlLoader;
    }
}
