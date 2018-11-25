package adapter;

import model.EditableTableModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;


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
        tableModel.setAutoCreateRowSorter(true);
    }
    public void setTableModel(AbstractTableModel model){
        if(tableModel != null)
            tableModel.setModel(model);
        else
            tableModel = new JTable(model);
    }
    public void setPanel(JPanel cur){
        //add the table to the frame
        JScrollPane tablePanel = new JScrollPane(tableModel);
        tablePanel.setBorder(null);
        tablePanel.setBounds(200,200,400,200);
        // todo customize table style

        cur.add(tablePanel);

//        cur.setTitle("Table Example");
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        cur.pack();
//        cur.setVisible(true);
    }

    public JTable getTableModel() {
        return tableModel;
    }
}
