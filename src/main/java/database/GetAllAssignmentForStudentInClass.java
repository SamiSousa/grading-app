package database;

import data.Assignment;
import data.Grade;
import data.Student;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetAllAssignmentForStudentInClass extends SQLQuery<Grade> {

    private int classId;
    public GetAllAssignmentForStudentInClass(int classId){
        this.classId = classId;
    }
    public RowMapper<Grade> getMapper() {
        return new GetGradeMapper();
    }

    @Override
    public String getQueryString() {

        return "SELECT * FROM Assignment NATURAL JOIN Enrolled NATURAL JOIN Student WHERE ClassID = "+classId;
    }
}


class GetGradeMapper implements RowMapper<Grade> {

    @Override
    public Grade map(ResultSet rs, StatementContext ctx) throws SQLException {

        Student curStudent = new Student(rs.getInt("StudentID"),rs.getString("FirstName"),rs.getString("LastName"),
                rs.getString("BUID"),rs.getString("Email"),rs.getString("ClassYear"));
        Assignment curAssignment = new Assignment(rs.getString("Name"),rs.getInt("CategoryID"),rs.getInt("MaxPoints"),
                rs.getInt("Weight"),rs.getInt("AssignmentID"));
        return new Grade(curStudent, curAssignment);
    }

}