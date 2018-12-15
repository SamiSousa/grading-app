package database;


import data.Grade;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetGrade extends SQLQuery<Integer>  {
    private int assignmentId;
    private int studentId;

    public GetGrade(int aId, int sId){
        this.assignmentId = aId;
        this.studentId = sId;
    }
    public RowMapper<Integer> getMapper() {
        return new RowMapper<Integer>() {
            @Override
            public Integer map(ResultSet rs, StatementContext ctx) throws SQLException {
                return new Integer(rs.getInt("LostPoints"));
            }
        };
    }

    @Override
    public String getQueryString() {
        return "SELECT * FROM Grade " +
                "WHERE AssignmentID = "+assignmentId+" AND StudentID = "+studentId;
    }
}
