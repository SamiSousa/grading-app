package database;

import data.Student;
import org.jdbi.v3.core.mapper.RowMapper;

public class GetStudentByName extends SQLQuery<Student> {
    private String name;
    public GetStudentByName(String name){
        this.name = name;
    }
    @Override
    public RowMapper<Student> getMapper() {
        return new GetStudentsInClassMapper();
    }

    @Override
    public String getQueryString() {
        String firstName = name.split(" ")[0];
        String lastName = name.split(" ")[1];
        return "SELECT s.StudentID, s.FirstName, s.LastName, s.Email, s.ClassYear, s.BUID FROM Student s WHERE s.FirstName = '"+firstName+"' AND s.LastName = '"+lastName+"'";
    }
}

