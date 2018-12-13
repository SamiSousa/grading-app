package database;

public class DeleteAssignment extends SQLUpdate  {

    private int assignmentId;
    private int categoryId;
    private int classId;
    public DeleteAssignment(int assignmentId){
        this.assignmentId = assignmentId;
    }
    public DeleteAssignment(int categoryId, int classId){
        this.categoryId = categoryId;
        this.classId = classId;
        this.assignmentId = Integer.MIN_VALUE;
    }
    @Override
    public String getQueryString() {
        if(assignmentId == Integer.MIN_VALUE){
            return "DELETE FROM Assignment WHERE CategoryID = "+categoryId+" AND ClassID = "+classId;
        }
        return "DELETE FROM Assignment WHERE AssignmentID = "+assignmentId;
    }
}
