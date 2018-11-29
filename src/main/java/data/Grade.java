package data;

public class Grade implements TableReady {

	private Student student;
	private Assignment assignment;
	private Integer lostPoints;
	private Boolean label;
	
	public Grade(Student student, Assignment assignment) {
		this.student = student;
		this.assignment = assignment;
		this.lostPoints = 0;
	}
	
	public void setLabel(boolean label) {
		this.label = label;
	}
	
	
	public Boolean getLabel() {
		return this.label;
	}
	
	public void setPoints(Integer points) {
		this.lostPoints = points;
	}
	
	public Integer getPoints() {
		return this.lostPoints;
	}
	
	public Object[] getDataRow() {
		Object[] row = new Object[5];
		
		row[0] = student.getFirstName();
		row[1] = student.getLastName();
		row[2] = assignment.getName();
		row[3] = this.lostPoints;
		row[4] = this.label;
		return row;
	}
	
	public String[] getDataColumns() {
		String[] cols = {"First Name", "Last Name", "Assignment", "Negative Score", "Label"};
		return cols;
	}
}
