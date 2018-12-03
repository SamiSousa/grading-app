package data;

public class Assignment implements TableReady {

	private String name;
	private String category;
	private Integer maxPoints;
	
	public Assignment(String name, Integer maxPoints) {
		this.name = name;
		this.category = "Assignment";
		this.maxPoints = maxPoints;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setMaxPoints(Integer newPoints) {
		this.maxPoints = newPoints;
	}
	
	public Integer getMaxPoints() {
		return this.maxPoints;
	}
	
	public Object[] getDataRow() {
		Object[] row = new Object[3];
		
		row[0] = name;
		row[1] = category;
		row[2] = maxPoints;
		return row;
	}
	
	public String[] getDataColumns() {
		String[] cols = {"Assignment Name", "Category", "Max Points"};
		return cols;
	}
	
	
}
