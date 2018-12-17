package database;

import main.Config;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public abstract class SQLConnection {

    protected static Jdbi jdbi;

    public static void initialize(String mySQLUser, String mySQLPassword) {
        jdbi = Jdbi.create("jdbc:mysql://localhost:3306/GradingApp", mySQLUser, mySQLPassword);
        jdbi.installPlugin(new SqlObjectPlugin());
        setUpTables();
    }


    private static void setUpTables() {
        try {
            jdbi.useHandle(handle -> {
                handle.execute("CREATE TABLE Semester (" +
                        "    SemesterID INT AUTO_INCREMENT PRIMARY KEY," +
                        "    Name VARCHAR(64) NOT NULL);");
                handle.execute("CREATE TABLE Class (" +
                        "    ClassID INT AUTO_INCREMENT PRIMARY KEY," +
                        "    CourseNumber VARCHAR(64)," +
                        "    SemesterID INT," +
                        "    FOREIGN KEY (SemesterID) REFERENCES Semester(SemesterID));");
                handle.execute("CREATE TABLE AssignmentCategory (" +
                        "    CategoryID INT AUTO_INCREMENT PRIMARY KEY," +
                        "    ClassID INT NOT NULL," +
                        "    Name VARCHAR(128) NOT NULL," +
                        "    Weight INT);");
                handle.execute("CREATE TABLE Assignment (\n" +
                        "    AssignmentID INT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "    ClassID INT NOT NULL,\n" +
                        "    CategoryID INT NOT NULL,\n" +
                        "    Name VARCHAR(255) NOT NULL,\n" +
                        "    MaxPoints INT NOT NULL,\n" +
                        "    Weight INT,\n" +
                        "    FOREIGN KEY (ClassID) REFERENCES Class(ClassID),\n" +
                        "    FOREIGN KEY (CategoryID) REFERENCES AssignmentCategory(CategoryID));");
                handle.execute("CREATE TABLE Student (\n" +
                        "    StudentID INT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "    FirstName VARCHAR(255) NOT NULL,\n" +
                        "    LastName VARCHAR(255) NOT NULL,\n" +
                        "    Email VARCHAR(255),\n" +
                        "    ClassYear VARCHAR(255),\n" +
                        "    BUID VARCHAR(20));");
                handle.execute("CREATE TABLE Enrolled (\n" +
                        "    StudentID INT,\n" +
                        "    ClassID INT,\n" +
                        "    PRIMARY KEY (StudentID, ClassID),\n" +
                        "    FOREIGN KEY (StudentID) REFERENCES Student(StudentID),\n" +
                        "    FOREIGN KEY (ClassID) REFERENCES Class(ClassID));");
                handle.execute(
                        "CREATE TABLE Grade (\n" +
                                "    AssignmentID INT,\n" +
                                "    StudentID INT,\n" +
                                "    LostPoints INT DEFAULT 0,\n" +
                                "    Comment VARCHAR(1000) DEFAULT '',\n" +
                                "    PRIMARY KEY (AssignmentID, StudentID),\n" +
                                "    FOREIGN KEY (AssignmentID) REFERENCES Assignment(AssignmentID),\n" +
                                "    FOREIGN KEY (StudentID) REFERENCES Student(StudentID));");
            });
        } catch(UnableToExecuteStatementException e) {
            //database already established
        }
    }
}
