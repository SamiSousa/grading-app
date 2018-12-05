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
        return "SELECT s.StudentID, s.FirstName, s.LastName, S.Email, s.ClassYear, s.BUID " +
                "FROM Enrolled e " +
                "JOIN Student s ON s.Email = e.StudentID " +
                "WHERE e.ClassID="+this.classID+";";
    }
}


class GetStudentsInClassMapper implements RowMapper<Student> {

    @Override
    public Student map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Student(rs.getInt(0), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
    }
}
