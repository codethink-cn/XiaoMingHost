package cn.chuanwise.xiaoming.host.launcher;

import cn.chuanwise.toolkit.serialize.json.JacksonSerializer;
import cn.chuanwise.util.MessageDigests;
import cn.chuanwise.xiaoming.bot.XiaoMingBot;
import cn.chuanwise.xiaoming.host.configuration.HostConfiguration;
import cn.chuanwise.toolkit.preservable.loader.FileLoader;
import cn.chuanwise.xiaoming.launcher.XiaoMingLauncher;
import cn.chuanwise.xiaoming.bot.XiaoMingBotImpl;
import cn.chuanwise.xiaoming.host.configuration.BotAccount;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.slf4j.Logger;

import java.io.Console;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/** 小明机器人启动器 */
@Slf4j
@Getter
public class HostXiaoMingLauncher implements XiaoMingLauncher {
    XiaoMingBot xiaoMingBot;

    /** 文件数据载入器 */
    final FileLoader fileLoader = new FileLoader(new JacksonSerializer());
    {
        fileLoader.setDecoding(StandardCharsets.UTF_8.name());
    }

    final File directory = new File("launcher");

    /** 启动器设置 */
    final HostConfiguration configuration = fileLoader.loadOrSupply(HostConfiguration.class, new File(directory, "launcher.json"), HostConfiguration::new);

    void onFirstInitialize() {
        Console console = System.console();

        log.warn("这是你初次启动小明吗？小明找不到或无法读取配置文件，我们现在就开始配置吧！");
        log.info("请输入机器人的 QQ 号");

        long qq;
        do {
            final String input = console.readLine();
            if (input.matches("\\d+")) {
                qq = Long.parseLong(input);
                break;
            } else {
                log.error("QQ 号的格式有误，请重新输入");
            }
        } while (true);

        log.info("请输入机器人的密码");
        log.info("接下来你输入的密码并不会显示在屏幕上（这是一种保护机制），看起来像是无法输入密码。不用担心，正常输入后回车即可。");
        log.info("如果密码输入错误，可删除 launcher/launcher.json 后再次输入。不建议直接修改该文件的内容");

        final char[] passwordValue = console.readPassword();
        final byte[] passwordMd5 = MessageDigests.MD5.digest(new String(passwordValue).getBytes());
        Arrays.fill(passwordValue, (char) 0);

        log.info("默认的小明登录方式是安卓手机登录。如果需要修改账号、密码和登陆方式等设置，请修改 " + configuration.getFile().getAbsolutePath() + " 后重启小明。");
        log.info("如有疑问，参阅小明用户手册：https://github.com/Chuanwise/XiaoMingBot/tree/main/docs/Manual.md");

        configuration.setAccount(new BotAccount(qq, passwordMd5));
        configuration.saveOrFail();
    }

    void onBothAccountPasswordAndMD5AreNull() {
        Console console = System.console();

        log.error(configuration.getFile().getAbsolutePath() + " 中 Bot 账号的 password 和 md5 属性都是空的，现在设置一下密码吧");

        log.info("请输入机器人的密码");
        final char[] passwordValue = console.readPassword();
        final byte[] passwordMD5 = MessageDigests.MD5.digest(new String(passwordValue).getBytes());
        Arrays.fill(passwordValue, (char) 0);

        configuration.getAccount().setMd5(passwordMD5);
        configuration.saveOrFail();
    }

    /** 读取机器人账号密码并准备登录 */
    boolean loadBotAccount() {
        final File file = configuration.getFile();

        if (!file.isFile() || Objects.isNull(configuration.getAccount())) {
            onFirstInitialize();
        }

        final BotAccount account = configuration.getAccount();
        String password = account.getPassword();
        byte[] md5 = account.getMd5();

        if (Objects.isNull(password) && Objects.isNull(md5)) {
            onBothAccountPasswordAndMD5AreNull();
            password = account.getPassword();
            md5 = account.getMd5();
        }

        if (Objects.nonNull(password)) {
            md5 = MessageDigests.MD5.digest(password.getBytes());
            account.setPassword(null);
            account.setMd5(md5);
            configuration.saveOrFail();
        }

        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setWorkingDir(directory);

        if (configuration.isEnableDeviceInfo()) {
            botConfiguration.fileBasedDeviceInfo();
        }
        botConfiguration.setProtocol(configuration.getProtocol());
        botConfiguration.setAutoReconnectOnForceOffline(configuration.isAutoReconnectOnForceOffline());

        if (Objects.nonNull(md5)) {
            try {
                xiaoMingBot = new XiaoMingBotImpl(BotFactory.INSTANCE.newBot(account.getQq(), md5, botConfiguration));
                return true;
            } catch (Exception exception) {
                log.error("请检查位于 launcher/launcher.json 中的账号信息是否正确");
                exception.printStackTrace();
                return false;
            }
        } else {
            log.error("小明非常惊讶，因为密码的 md5 加密值居然为空");
            return false;
        }
    }

    @Override
    public boolean launch() {
        try {
            return loadBotAccount();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return false;
        }
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public void stop() {
        xiaoMingBot.stop();
    }
}