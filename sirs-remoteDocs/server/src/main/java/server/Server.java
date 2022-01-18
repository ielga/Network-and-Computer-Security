package server;

import remoteInterface.ServerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server {
    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "localhost"); //127.0.0.1 - localhost
            System.out.println("server.Server is loading...");

            //TODO: Add args to command line to input a different ports
            LocateRegistry.createRegistry(9400);

            // Create the object(s)
            ServerImpl server = new ServerImpl();

            // Export those created Objects using UnicastRemoteObject classremotedocsdb
            //  Exported objects are called stubs
            ServerInterface serverStub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);

            System.out.println("her12");
            Registry registry = LocateRegistry.getRegistry("localhost", 9400);

            System.out.println("here444");
            registry.rebind("serverStub-1", serverStub);

            //Naming.rebind("serverStub-1", serverStub);

        }
        catch(Exception e) {
            System.out.println("Server: " + e);
        }
    }
}
