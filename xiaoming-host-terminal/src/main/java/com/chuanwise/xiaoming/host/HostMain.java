package com.chuanwise.xiaoming.host;

import com.chuanwise.xiaoming.api.launcher.XiaomingLauncher;
import com.chuanwise.xiaoming.api.util.PathUtils;
import com.chuanwise.xiaoming.host.launcher.HostXiaomingLauncher;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 小明启动器
 * https://github.com/Chuanwise/xiaoming-bot
 * @author Chuanwise
 */
@Slf4j
public class HostMain {
    public static void main(String[] args) {
        final File directory = PathUtils.LAUNCHER;
        if (!directory.isDirectory() && !directory.mkdirs()) {
            log.error("无法创建启动器配置文件夹：" + directory.getAbsolutePath());
            return;
        }

        final XiaomingLauncher launcher = new HostXiaomingLauncher();
        if (launcher.launch()) {
            launcher.start();
        }
    }
}
