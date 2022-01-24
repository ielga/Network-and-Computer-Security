package client;

import java.util.ArrayList;

public class File {
    private String fileName;
    private String content;
    private String owner;
    public ArrayList<String> writers;
    public ArrayList<String> readers;

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

    public File(String fileName, String owner, ArrayList<String> writers, ArrayList<String> readers, String content){
        this.fileName = fileName;
        this.owner    =  owner;
        this.writers  =  writers;
        this.readers  =  readers;
        this.content  = content;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setWriters(ArrayList<String> writers) {
        this.writers = writers;
    }

    public void setReaders(ArrayList<String> readers) {
        this.readers = readers;
    }

    public ArrayList<String> getWriters() {
        return writers;
    }

    public ArrayList<String> getReaders() {
        return readers;
    }
}
