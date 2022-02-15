package cn.chuanwise.xiaoming.host.graphical.plugin;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Data;

@Data
public class PluginTableView extends TableView<PluginTableElement> {
    protected final TableColumn<PluginTableElement, String> nameColumn = new TableColumn<>("插件");
    protected final TableColumn<PluginTableElement, String> versionColumn = new TableColumn<>("版本");
    protected final TableColumn<PluginTableElement, String> stateColumn = new TableColumn<>("状态");

    @SuppressWarnings("all")
    public PluginTableView() {
        getColumns().addAll(nameColumn, versionColumn, stateColumn);

        nameColumn.setCellValueFactory(x -> x.getValue().getNameProperty());
        versionColumn.setCellValueFactory(x -> x.getValue().getVersionProperty());
        stateColumn.setCellValueFactory(x -> x.getValue().getStateProperty());
    }
}
