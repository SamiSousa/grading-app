package database;

import data.ClassCategory;
import model.ClassModel;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetClassCategoriesMap extends SQLQuery<ClassCategory> {

    private int classId;
    public GetClassCategoriesMap(int classId){
        this.classId = classId;
    }
    @Override
    public RowMapper<ClassCategory> getMapper() {
        return new GetCategoryMapper();
    }

    @Override
    public String getQueryString() {
        return "SELECT c.CategoryID, c.ClassID, c.Name, c.Weight, a.AssignmentID FROM AssignmentCategory c JOIN Assignment a WHERE c.CategoryID = a.CategoryID AND c.ClassID = a.ClassID AND c.ClassID = "+classId;
    }
}


class GetCategoryMapper implements RowMapper<ClassCategory> {

    @Override
    public ClassCategory map(ResultSet rs, StatementContext ctx) throws SQLException {

        return new ClassCategory(rs.getInt("c.CategoryID"),rs.getInt("c.ClassID"), rs.getString("c.Name"),
                rs.getInt("c.Weight"), rs.getInt("a.AssignmentID"));
    }
}

/*
    EXAMPLE USAGE
        GetClassesQuery query = new GetClassesQuery();
        List<ClassModel> list = query.execute();
        list.forEach(x -> System.out.println(x.CourseNumber));

        When creating our view for
 */