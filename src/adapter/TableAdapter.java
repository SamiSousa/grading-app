package adapter;

import model.EditableTableModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;


public class TableAdapter {

    private JTable tableModel;
    public TableAdapter(String[] col, Object[][] data){
        tableModel = new JTable(new EditableTableModel(col,data));
    }
    public TableAdapter(EditableTableModel model) {
        if(tableModel != null)
            tableModel.setModel(model);
        else
            tableModel = new JTable(model);
        JTableHeader header = tableModel.getTableHeader();
        header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
    }
    public void setTableModel(AbstractTableModel model){
        if(tableModel != null)
            tableModel.setModel(model);
        else
            tableModel = new JTable(model);
    }
    public void setFrame(JFrame cur){
        //add the table to the frame
        cur.add(new JScrollPane(tableModel));
//        cur.setTitle("Table Example");
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cur.pack();
        cur.setVisible(true);
    }
}
