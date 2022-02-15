package cn.chuanwise.xiaoming.host.graphical.stage.content;

import cn.chuanwise.xiaoming.bot.XiaoMingBot;
import javafx.stage.Stage;

public class ContentStage extends Stage {
    private final XiaoMingBot xiaomingBot;

    public ContentStage(XiaoMingBot xiaomingBot) {
        this.xiaomingBot = xiaomingBot;
    }
}
