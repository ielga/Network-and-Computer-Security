package server;

import remoteInterface.ServerInterface;
import java.rmi.RemoteException;
import java.sql.*;

public class ServerImpl implements ServerInterface {

    DataBaseDriver db = new DataBaseDriver();

    public ServerImpl() {
        db = new DataBaseDriver();
    }

    @Override
    public String getUsername() throws RemoteException {
        return db.getUsername();
    }

    @Override
    public void sendDocument(String username, String doc) throws RemoteException {

    }
}
