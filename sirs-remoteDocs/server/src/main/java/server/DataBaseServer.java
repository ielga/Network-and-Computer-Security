package server;

import DataBaseLib.Queries;

import java.sql.*;

public class DataBaseServer {
    // JDBC driver name and database URL
    String jdbcDriver;
    String dbUrl;
    // Database credentials
    String dbServerUser;
    String dbServerPass;
    Connection conn = null;


    public DataBaseServer() {
        //Register JDBC driver
        //Open a connection
        jdbcDriver = "com.mysql.cj.jdbc.Driver";
        dbServerUser = "root";
        dbUrl = "jdbc:mysql://localhost:3306/remoteDocsDB";
        dbServerPass = "sirsRDpasswd47dif";

        System.out.println("Connecting to a selected database...");
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(dbUrl, dbServerUser, dbServerPass);
            System.out.println("Connected database successfully...");
        } catch (
        SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        //Execute a query
        String username = "";
        String password = "";
        System.out.println("Creating statement...");
        try {
           // Statement stmt = conn.createStatement();
           // String sql = "SELECT * FROM users";
           //  ResultSet rs = stmt.executeQuery(sql);

            ResultSet rs = Queries.getAllUsers(conn);

            //Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                username  = rs.getString("username");
                password = rs.getString("password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username + ": " + password;
    }

}
