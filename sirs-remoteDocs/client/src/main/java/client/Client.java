package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import remoteInterface.ServerInterface;

public class Client {
    public static void main(String[] args) {

        try {
            // Locate the remote registry.
            Registry remoteRegistry = LocateRegistry.getRegistry("127.0.0.1", 9400);
            // Do lookup and  reference the remote objects inside the remote RMI registry
            ServerInterface server = (ServerInterface) remoteRegistry.lookup("serverStub-1");
            // Call their methods, retrieve and store the data in DB or print to the screen.
            // Or display the end user.
            System.out.println("The username that is registered: " + server.getUsername());

        }
        catch(Exception e) {
            System.out.println("client.Client: " + e);
        }

    }
}
