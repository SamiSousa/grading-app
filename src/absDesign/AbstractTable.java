package absDesign;

abstract public class AbstractTable {

    // abstract method
    abstract public String getColumnName(int col);
    abstract public int getRowCount();
    abstract public int getColumnCount();
    abstract public Object getValueAt(int row, int col);
    abstract public void setValueAt(Object value, int row, int col);
}
