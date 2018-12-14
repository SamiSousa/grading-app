package data;

public class Assignment implements TableReady {

	private String name;
	private Integer categoryId;
	private Integer maxPoints;
	private Integer weight;
	private Integer assignmentId;


	public Assignment(String name, Integer categoryID, Integer maxPoints, Integer weight, Integer assignmentId) {
		this.name = name;
		this.categoryId = categoryID;
		this.maxPoints = maxPoints;
		this.weight = weight;
		this.assignmentId = assignmentId;
	}
	
	public String getName() {
		return this.name;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setMaxPoints(Integer newPoints) {
		this.maxPoints = newPoints;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAssignmentId() {
		return assignmentId;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getMaxPoints() {
		return this.maxPoints;
	}
	
	public Object[] getDataRow() {
		Object[] row = new Object[3];
		
		row[0] = name;
		row[1] = maxPoints;
		row[2] = weight;
		return row;
	}
	
	public String[] getDataColumns() {
		String[] cols = {"Assignment Name", "Max Points", "Weight"};
		return cols;
	}
	
	
}
