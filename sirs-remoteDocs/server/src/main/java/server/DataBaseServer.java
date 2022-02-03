package server;

import DataBaseLib.Queries;
import ServerLib.ContentInfo;
import sirs.remoteDocs.getContributorDocumentsResponse;

import javax.xml.transform.Result;
import java.sql.*;

import static DataBaseLib.Messages.*;
import static ServerLib.ContentInfo.*;

public class DataBaseServer {
    // JDBC driver name and database URL
    String jdbcDriver;
    String dbUrl;
    // Database credentials
    String dbServerUser;
    String dbServerPass;
    Connection conn = null;


    public  DataBaseServer() {
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
            System.out.println(DATABASE_INIT_ERROR);
        }
    }

    public String registerUser(String username, String password, byte[] publicKey) {
        System.out.println("Creating registerUser Statement!");

        try{
            return Queries.registerUser(conn, username, password,publicKey);
        }catch (Exception e){
            return REGISTER_ERROR;
        }
    }

    public String loginUser(String username, String password){
        System.out.println("Creating loginUser Statement!");
        try{
            return Queries.loginUser(conn, username, password);
        }catch (Exception e){
           return LOGIN_ERROR;
        }
    }

    public String createDocument(String owner, String filename, String content, byte[] ownerReadKey, byte[] ownerWriteKey){
        try{
            return Queries.createDocument(conn, owner, filename, content,ownerReadKey, ownerWriteKey);
        }catch (Exception e){
            return CREATE_DOCUMENT_ERROR;
        }
    }

    public ResultSet getOwnerWriteAndReadKey(String owner, String filename){
            return Queries.getOwnerWriteAndReadKey(conn, owner, filename);

    }

    public ResultSet getContributorWriteAndReadKey(String contributor, String filename, String owner){
        return Queries.getContributorWriteAndReadKey(conn, contributor, filename, owner);

    }

    public String addDocumentContributor(String owner, String contributor, String filename, String permission,
                                         String loggedInUser, byte[] contributorReadKey, byte[] contributorWriteKey){
        try{
            return Queries.addDocumentContributor(conn, owner, contributor, filename, permission,
                                                    loggedInUser, contributorReadKey, contributorWriteKey);
        }catch (Exception e){
            return ADD_CONTRIBUTOR_ERROR;
        }
    }

    public String editDocumentContent(String filename, String contributor, String owner, String newContent){
        try {
            return Queries.editDocumentContent(conn, filename, contributor, owner, newContent);
        }catch (Exception e){
            return EDIT_CONTENT_ERROR;
        }
    }


    public ResultSet getContributorDocuments(String contributor) {
        return Queries.getContributorDocuments(conn, contributor);
    }

    public ResultSet getOwnerDocuments(String owner) {
        return Queries.getOwnerDocuments(conn, owner);
    }


    public ContentInfo getDocumentContentRequest(String filename, String owner, String username) {
        return Queries.getDocumentContentRequest(conn, filename, owner, username);
    }

    public byte[] getContributorPublicKey(String contributor) {
        return Queries.getContributorPublicKey(conn, contributor);
    }


}
