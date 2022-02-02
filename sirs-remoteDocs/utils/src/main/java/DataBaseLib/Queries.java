package DataBaseLib;

import ServerLib.ContentInfo;
import jdk.jfr.ContentType;

import javax.xml.transform.Result;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import static DataBaseLib.Messages.*;

public class Queries {

    public static String registerUser(Connection conn, String username, String password, byte[] publicKey) {
        try {
            System.out.println("Register User");
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = algorithm.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            PreparedStatement stmt =
                    conn.prepareStatement("INSERT INTO users (username, password, publicKey) VALUES(?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, sb.toString());
            stmt.setBytes(3, publicKey);
            stmt.execute();

            return USER_REGISTERED;
        } catch (SQLException | NoSuchAlgorithmException e) {
            return REGISTER_ERROR;
        }
    }

    public static String loginUser(Connection conn, String username, String password) {
        try {
            System.out.println("Log In User!");
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users where username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){

                String dbUserPass = rs.getString("password");
                System.out.println(dbUserPass);
                if(password.equals(dbUserPass)){
                    return USER_LOGGED;
                }
                return WRONG_PASSWORD;
            }
            return WRONG_USERNAME;
        } catch (SQLException e) {
            return LOGIN_ERROR;
        }
    }

    public  static String createDocument(Connection conn, String owner, String filename, String content, byte[] ownerReadKey, byte[] ownerWriteKey){

        try{

            PreparedStatement stmt_doc =
                    conn.prepareStatement("INSERT INTO docs (owner, filename, content, readKey, writeKey) VALUES (?, ?, ?, ?, ?)");
            stmt_doc.setString(1, owner);
            stmt_doc.setString(2, filename);
            stmt_doc.setString(3, content);
            stmt_doc.setBytes(4, ownerReadKey);
            stmt_doc.setBytes(5, ownerWriteKey);
            stmt_doc.execute();


            return FILE_CREATED;
        }catch (SQLException e){
                System.out.println("");
            return CREATE_DOCUMENT_ERROR;
        }
    }

    public static String addDocumentContributor(Connection conn, String userOwner, String userContributorName,
                                                String filename, String permission, String loggedInUser,
                                                byte[] contributorReadKey, byte[] contributorWriteKey){
        try{

            if(userOwner.equals(loggedInUser)){
                PreparedStatement stmt = conn.prepareStatement("SELECT owner FROM docs where owner = ? and filename = ? ");
                stmt.setString(1, userOwner);
                stmt.setString(2, filename);
                ResultSet rs;

                if(stmt.executeQuery().next() ){
                    stmt = conn.prepareStatement("SELECT username FROM users where username = ?");
                    stmt.setString(1, userContributorName);

                    if(stmt.executeQuery().next()){

                        stmt = conn.prepareStatement("INSERT INTO usersDocs (contributor,owner,filename, permission, readKey, writeKey) " +
                                "VALUES(?, ?, ?, ?, ?, ?)");
                        stmt.setString(1, userContributorName);
                        stmt.setString(2, userOwner);
                        stmt.setString(3, filename);
                        stmt.setString(4, permission);
                        stmt.setBytes(5, contributorReadKey);
                        stmt.setBytes(6, contributorWriteKey);
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

    public static ResultSet getOwnerWriteAndReadKey(Connection conn, String owner, String filename) {
        try {

            PreparedStatement stmt = conn.prepareStatement("SELECT readKey, writeKey FROM docs where owner = ? and filename = ? ");
            stmt.setString(1, owner);
            stmt.setString(2, filename);
            return stmt.executeQuery();
        } catch (Exception e) {
            System.out.println(OWNER_READ_WRITE_ERROR);

        }
        return null;
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
                    if(permission.equals("w")){
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


    public static ContentInfo getDocumentContentRequest(Connection conn, String filename, String owner, String username) {
        try {
            if(owner.equals(username)) {
                // the user who is requesting access to content is the owner of the doc
                PreparedStatement stmt_1 = conn.prepareStatement("SELECT content, readKey, writeKey FROM `remoteDocsDB`.`docs` where owner = ? and filename = ? ");
                stmt_1.setString(1, owner);
                stmt_1.setString(2, filename);
                ResultSet rs_1 = stmt_1.executeQuery();

                if (rs_1.next()) {
                    ContentInfo contentInfo_1 = new ContentInfo();
                    contentInfo_1.setContent(rs_1.getString("content"));
                    contentInfo_1.setReadKey(rs_1.getBytes("readKey"));
                    contentInfo_1.setWriteKey(rs_1.getBytes("writeKey"));
                    return contentInfo_1;
                }
                else {
                    ContentInfo errorContent = new ContentInfo();
                    errorContent.setContent(Messages.FILE_OR_OWNER_DOES_NOT_EXIST);
                    return errorContent;
                }
            }
            else {
                // the writer who is requesting access to content is a contributor
                PreparedStatement stmt = conn.prepareStatement("SELECT readKey, writeKey FROM `remoteDocsDB`.`usersDocs` where contributor = ? and owner = ? and filename = ? ");
                stmt.setString(1, username);
                stmt.setString(2, owner);
                stmt.setString(3, filename);

                ResultSet rs = stmt.executeQuery();

                if(rs.next()) {
                    PreparedStatement stmt_2 = conn.prepareStatement("SELECT content FROM docs where filename = ? and owner = ? ");
                    stmt_2.setString(1, filename);
                    stmt_2.setString(2, owner);

                    ResultSet rs_2 = stmt_2.executeQuery();
                    if (rs_2.next()) {
                        ContentInfo contentInfo = new ContentInfo();
                        contentInfo.setContent(rs_2.getString("content"));
                        contentInfo.setReadKey(rs.getBytes("readKey"));
                        contentInfo.setWriteKey(rs.getBytes("writeKey"));
                        return contentInfo;
                    }
                    else {
                        ContentInfo errorContent = new ContentInfo();
                        errorContent.setContent(Messages.FILE_OR_OWNER_DOES_NOT_EXIST);
                        return errorContent;
                    }
                }
            }
        } catch (Exception e) {
            ContentInfo errorContent = new ContentInfo();
            errorContent.setContent(Messages.GET_CONTENT_ERROR);
            return errorContent;
        }
        return null;
    }

    public static byte[] getContributorPublicKey(Connection conn, String contributor) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT publicKey FROM `remoteDocsDB`.`users` where username = ? ");
            stmt.setString(1, contributor);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("publicKey");
            }
            else {
                System.out.println("GetContributorPublicKey" +  CONTRIBUTOR_DOES_NOT_EXIST);
            }

        } catch (Exception e) {
            System.out.println("GetContributorPublicKey: " + CONTRIBUTOR_PUBLIC_KEY_ERROR);
        }
        return null;
    }
}
