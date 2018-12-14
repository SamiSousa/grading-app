package database;

public class DeleteCategory extends SQLUpdate  {
    private int categoryId;
    private int classId;
    public DeleteCategory(int categoryId, int classId){
        this.categoryId = categoryId;
        this.classId = classId;
    }
    @Override
    public String getQueryString() {
        String query =  "DELETE FROM AssignmentCategory WHERE CategoryID = "+categoryId+" AND ClassID = "+classId;
        return query;
    }
}
