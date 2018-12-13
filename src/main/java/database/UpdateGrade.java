package database;

public class UpdateGrade extends SQLUpdate  {
    private int lostPoints;
    private int assignmentId;
    private int studentId;
    public UpdateGrade(int lostPoints, int aId, int sId){
        this.lostPoints = lostPoints;
        this.assignmentId = aId;
        this.studentId = sId;
    }
    @Override
    public String getQueryString() {
        return "UPDATE Grade SET LostPoints = "+lostPoints + " WHERE AssignmentID = "+assignmentId+" AND StudentID = "+studentId;
    }
}
