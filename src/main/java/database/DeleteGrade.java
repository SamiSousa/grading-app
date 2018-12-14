package database;

public class DeleteGrade extends SQLUpdate  {
    private int categoryId;
    private int classId;
    private int assignmentId;
    public DeleteGrade(int categoryId, int classId){
        this.categoryId = categoryId;
        this.classId = classId;
        this.assignmentId = Integer.MIN_VALUE;
    }
    public DeleteGrade(int assignmentId){
        this.assignmentId = assignmentId;
    }
    @Override
    public String getQueryString() {
        if(this.assignmentId != Integer.MIN_VALUE){
            return "DELETE FROM Grade WHERE AssignmentID = "+assignmentId;
        }
        return "DELETE FROM Grade WHERE Grade.AssignmentID IN (SELECT DISTINCT B.AssignmentID FROM (SELECT * FROM Grade Natural JOIN Assignment) AS B WHERE B.ClassID = "+classId+" AND B.CategoryID = "+categoryId+")";
    }
}