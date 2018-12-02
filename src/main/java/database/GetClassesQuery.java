package database;


import model.ClassModel;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetClassesQuery extends SQLQuery<ClassModel> {

    @Override
    public RowMapper<ClassModel> getMapper() {
        return new GetClassesMapper();
    }

    @Override
    public String getQueryString() {
        return "SELECT * FROM Class";
    }
}


class GetClassesMapper implements RowMapper<ClassModel> {

    @Override
    public ClassModel map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new ClassModel(rs.getInt("ClassID"), rs.getString("CourseNumber"), rs.getString("Semester"));
    }
}

/*
    EXAMPLE USAGE
        GetClassesQuery query = new GetClassesQuery();
        List<ClassModel> list = query.execute();
        list.forEach(x -> System.out.println(x.CourseNumber));

        When creating our view for
 */