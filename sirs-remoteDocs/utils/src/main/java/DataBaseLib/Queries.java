package DataBaseLib;

import java.sql.*;

public class Queries {

    public static ResultSet getAllUsers(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String allUsers = "SELECT * FROM users";
            // return the resultSet with the executed query -  must treat the ouput in DataBaseDriver or here
            return stmt.executeQuery(allUsers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet getUser(Connection conn, String username) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users where username = ?");
            stmt.setString(1, username);
            // return the resultSet with the executed query -  must treat the ouput in DataBaseDriver or here
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet getAllDocs(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String allDocs = "SELECT * FROM docs";
            // return the resultSet with the executed query -  must treat the ouput in DataBaseDriver or here
            return stmt.executeQuery(allDocs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
