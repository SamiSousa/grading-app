package database;


public class UpdateCategory extends SQLUpdate {
    private String partOfQuery;
    private int categoryId;
    public UpdateCategory(String query, int categoryID){
        this.partOfQuery = query;
        this.categoryId = categoryID;
    }


    @Override
    public String getQueryString() {
        String query = "UPDATE AssignmentCategory ";
        query += partOfQuery;
        query += " WHERE CategoryID = "+ categoryId;
        return query;
    }
}


