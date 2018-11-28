package adapter;

import model.EditableTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
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
        header.setFont(new Font("SansSerif", Font.ITALIC, 13));
        header.setReorderingAllowed(false);
        tableModel.setAutoCreateRowSorter(true);

        tableModel.setFont(new Font("Serif", Font.PLAIN, 14));
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
        // todo customize table style

        cur.add(tablePanel);

    }

    public JTable getTableModel() {
        return tableModel;
    }
    public void setCellColorRender(DefaultTableCellRenderer render) {

        for (int i =0; i<tableModel.getColumnCount();i++) {
            if(tableModel.getColumnName(i).equals("Lost points")){
                tableModel.getColumnModel().getColumn(i).setCellRenderer(render);
                break;
            }
        }
    }
}
