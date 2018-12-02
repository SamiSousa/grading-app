package data;

public class SelectRow implements TableReady {
	/* 
	 * Generic for storing a row of data from SQL select query
	 */
	
	private String[] columns;
	private Object[] data;
	
	public SelectRow(String[] columns, Object[] data) {
		this.columns = columns;
		this.data = data;
	}
	
	public void setValue(int column, Object value) {
		this.data[column] = value;
	}
	
	public void setValue(String columnName, Object value) {
		// Find a matching column name
		for (int i=0; i<this.columns.length; i++) {
			if (this.columns[i].equals(columnName)) {
				this.data[i] = value;
				return;
			}
		}
		// If reaches here, something went wrong
	}
	
	@Override
	public Object[] getDataRow() {
		return this.data.clone();
	}

	@Override
	public String[] getDataColumns() {
		return columns.clone();
	}

}
