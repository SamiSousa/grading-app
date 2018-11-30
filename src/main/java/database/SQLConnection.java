package database;

import org.jdbi.v3.core.Jdbi;

public abstract class SQLConnection {

    protected static Jdbi jdbi = Jdbi.create("jdbc:mysql://localhost:3306/GradingApp", "root", "password");

}
