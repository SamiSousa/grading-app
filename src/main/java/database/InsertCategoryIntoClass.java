package database;

import data.ClassCategory;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public class InsertCategoryIntoClass {

        public static ClassCategory insert(String categoryName, int weight, int classID) {
            return SQLConnection.jdbi.withExtension(database.InsertCategoryDAO.class, dao -> {
                int categoryID = dao.insertCategory(classID,categoryName,weight);
                return new ClassCategory(categoryID, classID, categoryName, weight);

            });
        }
    }
    interface InsertCategoryDAO {
        @SqlUpdate("INSERT INTO AssignmentCategory (ClassID, Name, Weight) VALUES (?, ?, ?)")
        @GetGeneratedKeys("CategoryID")
        int insertCategory(int ClassID, String Name, int Weight);

}
