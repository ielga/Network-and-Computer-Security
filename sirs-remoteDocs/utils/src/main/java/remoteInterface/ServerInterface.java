package remoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public String getUsername() throws RemoteException;
    public void sendDocument(String username, String doc) throws RemoteException;
}
