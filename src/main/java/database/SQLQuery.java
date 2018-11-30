package database;


import org.jdbi.v3.core.mapper.RowMapper;

import java.util.List;

public abstract class SQLQuery<T> extends SQLConnection {

    public List<T> execute() {
        return SQLConnection.jdbi.withHandle(handle ->
            handle.createQuery(getQueryString())
                    .map(getMapper())
                    .list()
        );
    }

    public abstract RowMapper<T> getMapper();
    public abstract String getQueryString();
}
