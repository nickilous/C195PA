package SchedulingApp.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DATABASENAME = "U05YFc";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/" + DATABASENAME;
    private static final String USERNAME = "U05YFc";
    private static final String PASSWORD = "53688640783";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection conn;

    public static void makeConnection() {
        try {
            Class.forName(DRIVER);
            conn = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //Lambda expression that prints to the console when user logins
            new Thread(() -> System.out.println("Connection sucessful!")).start();
        } catch (ClassNotFoundException | SQLException exception){
            System.out.println("Error creating DB connection.");
        }
    }

    public static void closeConnection() throws SQLException {
        conn.close();
        System.out.println("DB closed");
    }
    public static Connection getConnection () {
        return conn;
    }
}
