package database;

import data.Assignment;
import data.ClassCategory;
import model.AssignmentEntry;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetAssignmentFromCategory extends SQLQuery<Assignment> {
    private int assignmentId;
    public GetAssignmentFromCategory(int assignmentId) {
        this.assignmentId = assignmentId;
    }
    @Override
    public RowMapper<Assignment> getMapper() {
        return new GetAssigmentMapper();
    }

    @Override
    public String getQueryString() {

        return "SELECT * FROM Assignment c WHERE c.AssignmentID = "+assignmentId;
    }
}


class GetAssigmentMapper implements RowMapper<Assignment> {

    @Override
    public Assignment map(ResultSet rs, StatementContext ctx) throws SQLException {

        return new Assignment(rs.getString("Name"), rs.getInt("CategoryID"),rs.getInt("MaxPoints"),
                rs.getInt("Weight"),rs.getInt("AssignmentID"));
    }

}
