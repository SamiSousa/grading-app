package database;


public abstract class SQLUpdate extends SQLConnection {
    public void execute() {
        SQLConnection.jdbi.withHandle(handle ->
                handle.createCall(getQueryString())
                    .invoke()
        );
    }
    public abstract String getQueryString();
}
