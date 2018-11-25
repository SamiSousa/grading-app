package component.collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/***
 * collector responsible for fetching data from database
 */
public class TableDataCollector {
    // todo fetch data from database
    private String[] cols = {"First Name",
            "Last Name",
            "Item",
            "Negtive Score",
            "Label"};
    private Object[][] data = {
            {"Kathy", "Smith",
                    "Midterm", new Integer(5), new Boolean(false)},
            {"John", "Doe",
                    "Final", new Integer(3), new Boolean(true)},
            {"Sue", "Black",
                    "Midterm", new Integer(2), new Boolean(false)},
            {"Jane", "White",
                    "Project 1", new Integer(10), new Boolean(true)},
            {"Joe", "Brown",
                    "Project 2", new Integer(5), new Boolean(false)}
    };
    public TableDataCollector() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/grading_system", "root", "htys123");
            Statement stmt=con.createStatement();
            String tname = "attemp";
            ResultSet rs=stmt.executeQuery("select * from "+tname);

            while(rs.next())
                System.out.println(rs.getString(1));
            con.close();


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[] getCols() {
        return cols;
    }

    public Object[][] getData() {
        return data;
    }
}
