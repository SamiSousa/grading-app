package database;

import model.ClassModel;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public class InsertNewClass {
    public static ClassModel insertNewClass(String courseNumber, int semesterID) {
        return SQLConnection.jdbi.withExtension(InsertNewClassDAO.class, dao -> {
            int newClassID = dao.insertNewClass(courseNumber, semesterID);
            return new ClassModel(newClassID, courseNumber, semesterID);
        });
    }
}

interface InsertNewClassDAO {
    @SqlUpdate("INSERT INTO Class(CourseNumber, SemesterID) VALUES (?, ?)")
    @GetGeneratedKeys("ClassID")
    int insertNewClass(String courseNumber, int SemesterID);
}