package com.chuanwise.xiaoming.host.launcher;

import com.chuanwise.toolkit.preservable.file.FileLoader;
import com.chuanwise.toolkit.preservable.file.loader.JsonFileLoader;
import com.chuanwise.utility.MessageDigestUtility;
import com.chuanwise.xiaoming.api.bot.XiaomingBot;
import com.chuanwise.xiaoming.api.launcher.XiaomingLauncher;
import com.chuanwise.xiaoming.api.util.PathUtils;
import com.chuanwise.xiaoming.core.bot.XiaomingBotImpl;
import com.chuanwise.xiaoming.host.configuration.BotAccount;
import com.chuanwise.xiaoming.host.configuration.LauncherConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * 小明机器人启动器
 */
@Slf4j
@Getter
public class HostXiaomingLauncher implements XiaomingLauncher {
    final XiaomingBot xiaomingBot = new XiaomingBotImpl();

    /** 文件数据载入器 */
    final FileLoader fileLoader = new JsonFileLoader();

    final File directory = PathUtils.LAUNCHER;

    /** 启动器设置 */
    final LauncherConfiguration configuration = fileLoader.loadOrSupplie(LauncherConfiguration.class, new File(directory, "launcher.json"), LauncherConfiguration::new);

    /** 读取机器人账号密码并准备登录 */
    boolean loadBotAccount() {
        final BotAccount account = configuration.getAccount();
        final File medium = configuration.getMedium();

        if (!medium.isFile() || Objects.isNull(account)) {
            log.error("请在 " + medium.getAbsolutePath() + " 文件中写入机器人的账号密码");
            configuration.saveOrFail();
            return false;
        }

        String password = account.getPassword();
        byte[] md5 = account.getMd5();

//        Scanner scanner = new Scanner(System.in);
        if (Objects.isNull(password) && Objects.isNull(md5)) {
            log.error("请检查位于 launcher/launcher.json 中的账号信息是否正确。password 和 md5 属性至少要有一个");
            return false;
        }
        if (Objects.nonNull(password)) {
            md5 = MessageDigestUtility.MD5.digest(password.getBytes());
            account.setPassword(null);
            account.setMd5(md5);
            configuration.saveOrFail();
        }
        if (Arrays.equals(md5, MessageDigestUtility.MD5.digest("password".getBytes()))) {
            log.error("launcher/launcher.json 中记录的机器人的密码是 \"password\"（至少在哈希后是这个值），你是没有修改该文件中的账号密码吗？");
            return false;
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
                xiaomingBot.setMiraiBot(BotFactory.INSTANCE.newBot(account.getQq(), md5, botConfiguration));
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
    public Logger getLog() {
        return log;
    }

    @Override
    public void stop() {
        xiaomingBot.stop();
    }
}