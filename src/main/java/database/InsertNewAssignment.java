package database;

import data.Assignment;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public class InsertNewAssignment {


    public static Assignment insert(int classID, int categoryId, String name, int MaxPoints, int weight) {
        return SQLConnection.jdbi.withExtension(database.InsertAssignmentDAO.class, dao -> {
            int assignmentId = dao.insertAssignment(classID, categoryId, name, MaxPoints, weight);
            return new Assignment(name, categoryId, MaxPoints , weight,assignmentId);

        });
    }
}
    interface InsertAssignmentDAO {
        @SqlUpdate("INSERT INTO Assignment (ClassID, CategoryID, Name, MaxPoints, Weight) VALUES (?, ?, ?, ?, ?)")
        @GetGeneratedKeys("AssignmentID")
        int insertAssignment(int ClassID, int CategoryID, String Name, int MaxPoints,int Weight);

    }

