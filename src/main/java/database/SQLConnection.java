package database;

import main.Config;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public abstract class SQLConnection {

    protected static Jdbi jdbi;

    public static void initialize(String mySQLUser, String mySQLPassword) {
        System.out.println("Attempting to connect to DB with username "+mySQLUser+ " and password "+mySQLPassword);
        jdbi = Jdbi.create("jdbc:mysql://localhost:3306/GradingApp", mySQLUser, mySQLPassword);
        jdbi.installPlugin(new SqlObjectPlugin());
    }
}
