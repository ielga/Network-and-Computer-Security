package ServerLib;

public class ContentInfo {
    public String content;
    public byte[] readKey;
    public byte[] writeKey;

    public void setContent(String content) {
        this.content = content;
    }
    public void setReadKey(byte[] readKey) {
        this.readKey = readKey;
    }
    public void setWriteKey(byte[] writeKey) {
        this.writeKey = writeKey;
    }

    public String getContent() {
        return this.content;
    }
    public byte[] getReadKey() {
        return this.readKey;
    }
    public byte[] getWriteKey() {
        return this.writeKey;
    }

}