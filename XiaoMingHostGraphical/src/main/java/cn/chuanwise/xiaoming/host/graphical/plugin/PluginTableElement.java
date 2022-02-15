package cn.chuanwise.xiaoming.host.graphical.plugin;

import cn.chuanwise.util.Preconditions;
import cn.chuanwise.xiaoming.plugin.Plugin;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

@Data
public class PluginTableElement {
    protected final StringProperty nameProperty;
    protected final StringProperty versionProperty;
    protected final StringProperty stateProperty;

    public PluginTableElement(Plugin plugin) {
        Preconditions.nonNull(plugin, "plugin");
        this.nameProperty = new SimpleStringProperty(plugin.getName());
        this.versionProperty = new SimpleStringProperty(plugin.getVersion());
        this.stateProperty = new SimpleStringProperty(plugin.getStatus().toChinese());
    }
}
