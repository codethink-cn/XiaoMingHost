package cn.chuanwise.xiaoming.host.configuration;

import cn.chuanwise.toolkit.preservable.AbstractPreservable;
import lombok.*;
import net.mamoe.mirai.utils.BotConfiguration;

import java.beans.Transient;

/**
 * 机器人账号密码配置文件
 * @author Chuanwise
 */
@Data
public class HostConfiguration extends AbstractPreservable {
    /**
     * 机器人账号密码
     */
    BotAccount account = new BotAccount();

    BotConfiguration.MiraiProtocol protocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE;

    boolean autoReconnectOnForceOffline = false;

    boolean enableDeviceInfo = true;

    @Deprecated
    transient String logFileNamePattern = "yyyy-mm-dd hh:mm:ss";

    @Deprecated
    @Transient
    public String getLogFileNamePattern() {
        return logFileNamePattern;
    }
}