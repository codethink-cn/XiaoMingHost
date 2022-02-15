package cn.chuanwise.xiaoming.host.graphical.stage.init;

import cn.chuanwise.util.NumberUtil;
import cn.chuanwise.util.StringUtil;
import cn.chuanwise.xiaoming.bot.XiaoMingBot;
import cn.chuanwise.xiaoming.bot.XiaoMingBotImpl;
import cn.chuanwise.xiaoming.host.graphical.util.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.network.WrongPasswordException;

import java.util.Optional;

public class InitController {
    @FXML
    private TextField codeField;

    @FXML
    private CheckBox showHelpCheckBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    protected InitStage initStage;

    void init(InitStage initStage) {
        this.initStage = initStage;
    }

    @FXML
    void onLogIn(ActionEvent event) {
        final String codeText = codeField.getText();
        final String passwordText = passwordField.getText();

        final Optional<Long> optionalCode = NumberUtil.parseLong(codeText);
        if (!optionalCode.isPresent()) {
            AlertUtil.error("QQ 号错误", "QQ 号应该为整数");
            return;
        }
        final long code = optionalCode.get();

        if (StringUtil.isEmpty(passwordText)) {
            AlertUtil.error("密码错误", "密码不能为空！");
            return;
        }

        final boolean showHelp = showHelpCheckBox.isSelected();
        final XiaoMingBot xiaoMingBot = new XiaoMingBotImpl(BotFactory.INSTANCE.newBot(code, passwordText));
        try {
            xiaoMingBot.start();

            initStage.setXiaomingBot(xiaoMingBot);
            final Object condition = initStage.getCondition();
            synchronized (condition) {
                condition.notifyAll();
            }
        } catch (WrongPasswordException exception) {
            AlertUtil.error("登录失败", exception.getMessage());
        }
    }
}
