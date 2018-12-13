package model;

import javax.swing.table.AbstractTableModel;
import java.util.HashSet;
import java.util.Set;

public class EditableTableModel extends AbstractTableModel{
    // private data
    private String[] columnNames;
    private Object[][] tableData;
    private Set<Integer> editableCol;

    public EditableTableModel(String[] cName, Object[][] data) {
        super();
        columnNames = cName;
        tableData = data;
        editableCol = new HashSet<>();
    }
    public EditableTableModel(){
        editableCol = new HashSet<>();
    }
    public void setEditableColumn(Set<Integer> set) {
        editableCol = set;
    }
    public void addEditableCol(int col){
        editableCol.add(col);
    }
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public String getColumnName(int col) {
        if(col < 0 || col >= columnNames.length)
            return "Read data error";

        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return tableData.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if(editableCol.contains(col))
            return true;
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        tableData[row][col] = value;

        // todo fire update to database here
        fireTableCellUpdated(row,col);
    }
}
