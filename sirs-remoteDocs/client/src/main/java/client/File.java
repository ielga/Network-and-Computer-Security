package client;

import java.util.ArrayList;

public class File {
    private String owner;
    public ArrayList<String> writers;
    public ArrayList<String> readers;

    public File(String owner,ArrayList<String> writers,ArrayList<String> readers){
        this.owner   =  owner;
        this.writers =  writers;
        this.readers =  readers;
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
