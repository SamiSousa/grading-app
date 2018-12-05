package database;


import data.Student;
import org.jdbi.v3.core.result.ResultBearing;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public class InsertStudentIntoClass {

    public static Student insert(String[] studentData, int classID) {
        return SQLConnection.jdbi.withExtension(InsertStudentDAO.class, dao -> {
            int studentID = dao.insertStudent(studentData[0], studentData[1], studentData[2], studentData[3], studentData[4]);
            dao.enroll(studentID, classID);
            System.out.println("Added student ");
            return new Student(studentID, studentData[0], studentData[1], studentData[2], studentData[3], studentData[4]);
        });
    }
}

interface InsertStudentDAO {
    @SqlUpdate("INSERT INTO Student (FirstName, LastName, Email, ClassYear, BUID) VALUES (?, ?, ?, ?, ?)")
    @GetGeneratedKeys("StudentID")
    int insertStudent(String FirstName, String LastName, String Email, String ClassYear, String BUID);

    @SqlUpdate("INSERT INTO Enrolled VALUES (?, ?)")
    void enroll(int studentID, int classID);
}