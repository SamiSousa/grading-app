package database;

public class InsertGrade extends SQLUpdate  {
    private int assignmentId;
    private int studentId;

    public InsertGrade(int aId, int sId){
        this.assignmentId = aId;
        this.studentId = sId;
    }
    @Override
    public String getQueryString() {
        return "INSERT INTO Grade(AssignmentID, StudentID) VALUES("+assignmentId+","+studentId+")";
    }
}
