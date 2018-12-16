package database;

import data.Student;
import org.jdbi.v3.core.mapper.RowMapper;

public class GetStudentByName extends SQLQuery<Student> {
    private String name;
    private int classId;
    public GetStudentByName(String name,int classId){
        this.name = name;
        this.classId = classId;
    }
    @Override
    public RowMapper<Student> getMapper() {
        return new GetStudentsInClassMapper();
    }

    @Override
    public String getQueryString() {
        String firstName = name.split(" ")[0];
        String lastName = name.split(" ")[1];
        return "SELECT s.StudentID, s.FirstName, s.LastName, s.Email, s.ClassYear, s.BUID FROM Student s NATURAL JOIN Enrolled e WHERE s.FirstName = '"+firstName+"' AND s.LastName = '"+lastName+"'"
                +" AND ClassID = "+classId;
    }
}

