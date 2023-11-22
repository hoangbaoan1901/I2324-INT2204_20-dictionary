package Lib;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteConnect {
    public static void main (String[] args) {
        String jbdcURL = "jdbc:sqlite:src/main/Dictionary.db";
        try{
            Connection connection = DriverManager.getConnection(jbdcURL);
            System.out.println("Connection successful");

        } catch (Exception e) {
            System.out.println("Problem connecting to SQLite database");
            e.printStackTrace();
        }
    }
}
