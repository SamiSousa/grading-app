package database;

import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface InsertStudentDAO {
    @SqlUpdate("INSERT INTO Student (FirstName, LastName, Email, ClassYear, BUID) VALUES (?, ?, ?, ?, ?)")
    @GetGeneratedKeys("StudentID")
    int insertStudent(String FirstName, String LastName, String Email, String ClassYear, String BUID);

    @SqlUpdate("INSERT INTO Enrolled VALUES (?, ?)")
    void enroll(int studentID, int classID);
}