package client;



public class File {
    private String fileName;
    private String owner;
    private String content;



    public File(String fileName, String owner,  String content){
        this.fileName = fileName;
        this.owner    =  owner;
        this.content  = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
