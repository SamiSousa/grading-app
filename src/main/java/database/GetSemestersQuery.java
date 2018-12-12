package database;


import model.Semester;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSemestersQuery extends SQLQuery<Semester> {

    @Override
    public RowMapper<Semester> getMapper() {
        return new GetSemesterMapper();
    }

    @Override
    public String getQueryString() {
        return "SELECT s.SemesterID, s.Name FROM Semester s";
    }
}


class GetSemesterMapper implements RowMapper<Semester> {

    @Override
    public Semester map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Semester(rs.getString("s.Name"), rs.getInt("s.SemesterID"));
    }
}
