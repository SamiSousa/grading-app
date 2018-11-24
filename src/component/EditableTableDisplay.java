package component;

import adapter.TableAdapter;
import collector.TableDataCollector;
import model.EditableTableModel;

import javax.swing.*;

public class EditableTableDisplay {

    private TableDataCollector collector;
    private EditableTableModel model;
    private TableAdapter adapter;

    public EditableTableDisplay(){
        collector = new TableDataCollector();
        // todo fetch data using collector
        model = new EditableTableModel(collector.getCols(),collector.getData());
        adapter = new TableAdapter(model);
    }

    public void setTableModel(EditableTableModel m) {
        model = m;
        adapter.setTableModel(m);
    }

    public void setFrame(JFrame frame){
        adapter.setFrame(frame);
    }

    public EditableTableModel getModel() {
        return model;
    }
}
