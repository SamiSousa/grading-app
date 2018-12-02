package component;

import adapter.TableAdapter;
import component.collector.TableDataCollector;
import model.EditableTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;

public class EditableTableDisplay implements TableModelListener {

    private TableDataCollector collector;
    private EditableTableModel model;
    private TableAdapter adapter;
    private JPanel currentPanel;

    public EditableTableDisplay(JPanel cur){
        collector = new TableDataCollector();
        // todo fetch data using component.collector
        model = new EditableTableModel(collector.getCols(),collector.getData());
        adapter = new TableAdapter(model);
        adapter.getTableModel().getModel().addTableModelListener(this);
        currentPanel = cur;
    }

    public void setTableModel(EditableTableModel m) {
        model = m;
        adapter.setTableModel(model);
        adapter.getTableModel().getModel().removeTableModelListener(this);
        adapter.getTableModel().getModel().addTableModelListener(this);
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
        if(columnName.equals("Label") && data.toString().equals("true")){
            System.out.println("label");
            NoteDialog note = new NoteDialog((JFrame) SwingUtilities.getWindowAncestor(currentPanel), (String) model.getValueAt(row, 0),(String) model.getValueAt(row, 2));
            note.setVisible(true);
            if (note.isSucceed()) {
//                // todo add note


            }
        } else if(columnName.equals("Label")) {
            // todo delete note here
        }
    }

    public TableAdapter getAdapter() {
        return adapter;
    }
}
