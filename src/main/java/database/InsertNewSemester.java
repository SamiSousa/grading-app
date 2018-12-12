package database;


import model.Semester;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public class InsertNewSemester {
    public static Semester insertNewSemester(String semesterName) {
        return SQLConnection.jdbi.withExtension(InsertNewSemesterDAO.class, dao -> {
            int semesterID = dao.insertNewClass(semesterName);
            return new Semester(semesterName, semesterID);
        });
    }
}

interface InsertNewSemesterDAO {
    @SqlUpdate("INSERT INTO Semester(Name) VALUES (?)")
    @GetGeneratedKeys("SemesterID")
    int insertNewClass(String name);
}