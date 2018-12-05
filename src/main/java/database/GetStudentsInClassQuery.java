package database;


import data.Student;
import model.ClassModel;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetStudentsInClassQuery extends SQLQuery<Student> {

    private int classID;
    public GetStudentsInClassQuery(int classID) {
        this.classID = classID;
    }

    @Override
    public RowMapper<Student> getMapper() {
        return new GetStudentsInClassMapper();
    }

    @Override
    public String getQueryString() {
        System.out.println("Finding students in classID: "+this.classID);
        return "SELECT s.StudentID, s.FirstName, s.LastName, s.Email, s.ClassYear, s.BUID " +
                "FROM Enrolled e " +
                "JOIN Student s ON s.StudentID = e.StudentID " +
                "WHERE e.ClassID="+this.classID+";";
    }
}


class GetStudentsInClassMapper implements RowMapper<Student> {

    @Override
    public Student map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Student(rs.getInt("s.StudentID"), rs.getString("s.FirstName"), rs.getString("s.LastName"), rs.getString("s.BUID"), rs.getString("s.Email"), rs.getString("s.ClassYear"));
        //TODO make this mapping work
    }
}
