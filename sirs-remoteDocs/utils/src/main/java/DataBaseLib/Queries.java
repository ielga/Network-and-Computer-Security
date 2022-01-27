package DataBaseLib;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import static DataBaseLib.Messages.*;

public class Queries {

    public static String registerUser(Connection conn, String username, String password) {
        try {
            System.out.println("RegisterUserInputs: " + username + password);
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = algorithm.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            PreparedStatement stmt =
                    conn.prepareStatement("INSERT INTO `remoteDocsDB`.`users` (username, password, logged) VALUES(?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, sb.toString());
            stmt.setBoolean(3, false);
            stmt.execute();

            return USER_REGISTERED;
        } catch (SQLException | NoSuchAlgorithmException e) {
            return REGISTER_ERROR;
        }
    }

    public static String loginUser(Connection conn, String username, String password) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM `remoteDocsDB`.`users` where username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            PreparedStatement stmt2 =
                    conn.prepareStatement("UPDATE `remoteDocsDB`.`users` SET logged = ? where  username = ? and password = ?");
            stmt2.setBoolean(1, true);
            stmt2.setString(2, username);
            stmt2.setString(3, password);

            if(rs.next()){
                String dbUserPass = rs.getString("password");
                if(password.equals(dbUserPass)){
                    stmt2.executeUpdate();
                    return USER_LOGGED;
                }
                return WRONG_PASSWORD;
            }
            return WRONG_USERNAME;
        } catch (SQLException e) {
            return LOGIN_ERROR;
        }
    }

    public  static String createDocument(Connection conn, String owner, String filename, String content){

        try{
            System.out.println("CreateDocument: " + owner + filename + content);
            PreparedStatement stmt_doc =
                    conn.prepareStatement("INSERT INTO `remoteDocsDB`.`docs`(owner, filename, content) VALUES (?, ?, ?)");
            stmt_doc.setString(1, owner);
            stmt_doc.setString(2, filename);
            stmt_doc.setString(3, content);
            System.out.println("CreateDoc Query Executed!");
            stmt_doc.execute();
            System.out.println("Lets see it!");

            return FILE_CREATED;
        }catch (SQLException e){
                System.out.println("");
            return CREATE_DOCUMENT_ERROR;
        }
    }

    public static String addDocumentContributor(Connection conn, String userOwner, String userContributorName,
                                                String filename, String permission, String loggedInUser){
        try{

            if(userOwner.equals(loggedInUser)){
                PreparedStatement stmt = conn.prepareStatement("SELECT owner FROM `remoteDocsDB`.`docs` where owner = ? and filename = ? ");
                stmt.setString(1, userOwner);
                stmt.setString(2, filename);
                ResultSet rs;

                if(stmt.executeQuery().next() ){
                    stmt = conn.prepareStatement("SELECT username FROM `remoteDocsDB`.`users` where username = ?");
                    stmt.setString(1, userContributorName);

                    if(stmt.executeQuery().next()){

                        stmt = conn.prepareStatement("INSERT INTO `remoteDocsDB`.`usersDocs`(contributor,owner,filename, permission) " +
                                "VALUES(?, ?, ?, ?)");
                        stmt.setString(1, userContributorName);
                        stmt.setString(2, userOwner);
                        stmt.setString(3, filename);
                        stmt.setString(4, permission);
                        stmt.execute();
                        return CONTRIBUTOR_WAS_ADDED;
                    }
                    return CONTRIBUTOR_DOES_NOT_EXIST;
                }
                return FILE_OR_OWNER_DOES_NOT_EXIST;
            }
            return ADD_CONTRIBUTOR_DENIED;

        }catch (SQLException e){
            return ADD_CONTRIBUTOR_ERROR;
        }

    }

    public static String editDocumentContent(Connection conn, String filename, String contributor, String owner, String newContent){
        try{

            //Contributor is the owner of the file??
            PreparedStatement owner_stmt = conn.prepareStatement("SELECT * FROM `remoteDocsDB`.`docs` where filename = ? and owner = ? ");
            owner_stmt.setString(1, filename);
            owner_stmt.setString(2, contributor);

            if(owner_stmt.executeQuery().next()){
                owner_stmt = conn.prepareStatement("UPDATE `remoteDocsDB`.`docs` SET content = ? where  filename = ? and owner = ? ");
                owner_stmt.setString(1, newContent);
                owner_stmt.setString(2, filename);
                owner_stmt.setString(3, contributor);
                owner_stmt.executeUpdate();
                return CONTENT_UPDATED;
            }
            else{
                //contributor is not the owner
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `remoteDocsDB`.`usersDocs` where contributor = ?  and owner = ? and  filename = ?  ");
                stmt.setString(1, contributor);
                stmt.setString(2, owner);
                stmt.setString(3, filename);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    String permission = rs.getString("permission");
                    if(permission.equals("w") || permission.equals("r/w")){
                        stmt =  conn.prepareStatement("UPDATE `remoteDocsDB`.`docs` SET content = ? where  filename = ? and owner = ? ");
                        stmt.setString(1, newContent);
                        stmt.setString(2, filename);
                        stmt.setString(3, owner);
                        stmt.executeUpdate();
                        return CONTENT_UPDATED;
                    }
                    return USER_DOES_NOT_HAVE_PERMISSION;
                }
                return EDIT_CONTENT_DENIED;
            }
        }catch (Exception e){
            return EDIT_CONTENT_ERROR;
        }
    }

    public static ResultSet getContributorDocuments(Connection conn, String contributor) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT owner, filename, permission FROM `remoteDocsDB`.`usersDocs` where contributor = ? ");
            stmt.setString(1, contributor);

            return stmt.executeQuery();

        } catch (Exception e) {
            return null;
        }
    }

    public static ResultSet getOwnerDocuments(Connection conn, String owner) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT filename FROM `remoteDocsDB`.`docs` where owner = ? ");
            stmt.setString(1, owner);

            return stmt.executeQuery();

        } catch (Exception e) {
            return null;
        }
    }

}
