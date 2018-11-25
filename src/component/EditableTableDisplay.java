package component;

import adapter.TableAdapter;
import component.collector.TableDataCollector;
import model.EditableTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class EditableTableDisplay implements TableModelListener {

    private TableDataCollector collector;
    private EditableTableModel model;
    private TableAdapter adapter;

    public EditableTableDisplay(){
        collector = new TableDataCollector();
        // todo fetch data using component.collector
        model = new EditableTableModel(collector.getCols(),collector.getData());
        adapter = new TableAdapter(model);
        adapter.getTableModel().getModel().addTableModelListener(this);
    }

    public void setTableModel(EditableTableModel m) {
        model = m;
        adapter.setTableModel(m);
    }

    public void setPanel(JPanel panel){
        adapter.setPanel(panel);
    }

    public EditableTableModel getModel() {
        return model;
    }
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();

        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);

        // todo update to database;
        System.out.println(data);
    }
}
