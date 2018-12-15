package database;

import data.ClassCategory;
import model.ClassModel;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

public class GetCategoriesInClass extends SQLQuery<Map.Entry<Integer, Integer>> {

    private int classId;
    public GetCategoriesInClass(int classId){
        this.classId = classId;
    }
    @Override
    public RowMapper<Map.Entry<Integer, Integer>> getMapper() {
        return new GetCategoriesInClassMapper();
    }

    @Override
    public String getQueryString() {
        return "SELECT CategoryID, Weight FROM AssignmentCategory WHERE ClassID = "+classId;
    }
}


class GetCategoriesInClassMapper implements RowMapper<Map.Entry<Integer, Integer>> {

    @Override
    public Map.Entry<Integer, Integer> map(ResultSet rs, StatementContext ctx) throws SQLException {
       return new AbstractMap.SimpleEntry<>(rs.getInt("CategoryID"), rs.getInt("Weight"));
    }
}
