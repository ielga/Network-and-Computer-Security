package client;



public class File {
    private String fileName;
    private String owner;
    private String permission;



    public File(String fileName, String owner,  String permission){
        this.fileName = fileName;
        this.owner    =  owner;
        this.permission  = permission;
    }

    public File(String filepath) {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPermission() {
        return permission;
    }

    public void setContent(String permission) {
        this.permission = permission;
    }


}
