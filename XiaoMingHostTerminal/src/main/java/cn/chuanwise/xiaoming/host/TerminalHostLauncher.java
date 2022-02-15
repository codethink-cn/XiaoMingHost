package cn.chuanwise.xiaoming.host;

import cn.chuanwise.xiaoming.launcher.XiaoMingLauncher;
import cn.chuanwise.xiaoming.host.launcher.HostXiaoMingLauncher;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.network.WrongPasswordException;
import org.apache.log4j.PropertyConfigurator;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * 小明启动器
 * https://github.com/Chuanwise/XiaoMingBot
 * @author Chuanwise
 */
@Slf4j
public class TerminalHostLauncher {
    public static void main(String[] args) {
        // 加载配置文件
        final Charset defaultCharset = Charset.defaultCharset();
        final InputStream loggerPropertyStream = TerminalHostLauncher.class
                .getClassLoader()
                .getResourceAsStream("logger/" + defaultCharset.name().toLowerCase() + ".properties");
        if (Objects.isNull(loggerPropertyStream)) {
            System.err.println("当前系统编码为 " + defaultCharset.name() + "，未找到该编码的日志配置。");
            System.err.println("解决方案见 http://chuanwise.cn:10074/#/question");
            System.err.println("current default charset is " + defaultCharset.name() + ", " +
                    "can not find the matchable slf4j configuration file.");
            return;
        } else {
            PropertyConfigurator.configure(loggerPropertyStream);
            System.out.println("当前系统编码为 " + defaultCharset.name() + "，已加载此编码配置文件");
            System.out.println("current default charset is " + defaultCharset.name() + ", " +
                    "slf4j logger configured.");
        }

        final File directory = new File("launcher");
        if (!directory.isDirectory() && !directory.mkdirs()) {
            log.error("无法创建启动器配置文件夹：" + directory.getAbsolutePath());
            return;
        }

        AnsiConsole.systemInstall();

        final XiaoMingLauncher launcher = new HostXiaoMingLauncher();
        try {
            if (launcher.launch()) {
                launcher.start();
            }
        } catch (WrongPasswordException exception) {
            log.error("无法登录机器人账号，可能是密码错误，或网络出现问题。\n" +
                    "信息：" + exception + "\n" +
                    "如果是密码错误，请删除 launcher/launcher.json 后再试");
        } catch (Exception exception) {
            log.error("无法启动小明", exception);
        }
    }
}
