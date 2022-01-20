package client;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.NettyChannelBuilder;
import remoteInterface.ServerInterface;
import sirs.remoteDocs.RemoteDocs;
import sirs.remoteDocs.RemoteDocsServiceGrpc;
import sirs.remoteDocs.registerUserRequest;
import sirs.remoteDocs.registerUserResponse;

public class ClientMain {
    public static void main(String[] args) {

        try {
            // Locate the remote registry.
           // Registry remoteRegistry = LocateRegistry.getRegistry("79.168.75.214", 1099);
            // Do lookup and  reference the remote objects inside the remote RMI registry
            //ServerInterface server = (ServerInterface) remoteRegistry.lookup("serverStub-1");
            // Call their methods, retrieve and store the data in DB or print to the screen.
            // Or display the end user.
            //System.out.println("The username that is registered: " + server.getUsername());
            ManagedChannel managedChannel = NettyChannelBuilder
                                            .forAddress("localhost", 8080)
                                            .usePlaintext().build();

            RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub =
                    RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

            registerUserRequest request = registerUserRequest.newBuilder().build();

            registerUserResponse response = serviceBlockingStub.registerUser(request);

            System.out.println("Received response: " + response.getUserpass());

            managedChannel.shutdown();

        }
        catch(Exception e) {
            System.out.println("client.Client: " + e);
        }

    }
}
