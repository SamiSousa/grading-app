package database;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public abstract class SQLConnection {

    protected static Jdbi jdbi = Jdbi.create("jdbc:mysql://localhost:3306/GradingApp", "root", "password");

    static {
        jdbi.installPlugin(new SqlObjectPlugin());
    }

}
