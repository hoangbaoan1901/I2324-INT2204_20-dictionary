package Lib;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteConnect {
    public static Connection getSQLiteConnection(String path) {
        String jbdcURL = String.format("jdbc:sqlite:%s", path);
        try {
            Connection connection = DriverManager.getConnection(jbdcURL);
            System.out.println("Connection success!");
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
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
