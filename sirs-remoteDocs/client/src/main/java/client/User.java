package client;


import java.util.ArrayList;
import java.util.List;

public class User {
    public String username; // we have to save it encrypted
    // public String password;  // maybe we dont even need to save the password
    public boolean loggedIn;
    public List<String> userOwnerFiles = new ArrayList<>();
    public List<FileAsContributor> filesAsContributor = new ArrayList<>();


    public User(String username) {
        this.username = username;
      //  this.password = password;
        this.loggedIn = false;
    }
    public  User(){
    }


    public void logIn() {
        this.loggedIn = true;
    }

    public void addFileAsOwner(String filename) {
        this.userOwnerFiles.add(filename);
    }

    public void addFileAsContributor(String filename, String owner, String permission) {
        this.filesAsContributor.add(new FileAsContributor(filename, owner, permission));
    }

    public void cleanFilesAsContributorList() {
        this.filesAsContributor.clear();
    }

    /* Class to represent the files that the current user is a Contributor in */
    public static class FileAsContributor {
        public String filename;
        public String owner;
        public String userPermission; // Current user's permission to read/write on someone else's (the owner's) files

        public FileAsContributor(String filename, String owner, String permission) {
            this.filename = filename;
            this.owner = owner;
            this.userPermission = permission;
        }
    }

}
