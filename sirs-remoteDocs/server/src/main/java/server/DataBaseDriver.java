package server;

import java.sql.*;

public class DataBaseDriver {
    // JDBC driver name and database URL
    String jdbcDriver;
    String dbUrl;
    // Database credentials
    String dbServerUser;
    String dbServerPass;
    Connection conn = null;
    Statement stmt = null;

    public DataBaseDriver() {
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
            stmt = conn.createStatement();
            String sql = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(sql);

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
