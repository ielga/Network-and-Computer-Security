package client;

import java.io.File;
import io.grpc.*;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.grpc.netty.GrpcSslContexts;
import sirs.remoteDocs.RemoteDocsServiceGrpc;
import sirs.remoteDocs.registerUserRequest;
import sirs.remoteDocs.registerUserResponse;


public class ClientService {

    public ManagedChannel managedChannel;

    public ClientService() {
        File CAsCertFile = new File("utils/src/main/resources/CACert.pem");
        File clientCertFile = new File("utils/src/main/resources/UserCert.pem");
        File clientKeyFile = new File("utils/src/main/resources/UserKey.pem");

        try {

            SslContext sslContext = GrpcSslContexts.forClient()
                    .keyManager(clientCertFile, clientKeyFile)
                    .trustManager(CAsCertFile)
                    .build();

            managedChannel = NettyChannelBuilder
                    // This 0.0.0.0 address is the one that matches the one put into utils../resources/"server-ext.cnf"
                    .forAddress("0.0.0.0", 8443)
                    .sslContext(sslContext)
                    .build();

            /* OTHERS:
            ChannelCredentials creds = TlsChannelCredentials.newBuilder()
                    .trustManager(CAsCertFile)
                    .build();
            managedChannel = Grpc.newChannelBuilder("192.168.1.164:8443", creds)
                    .build(); */


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void registerUser(String username, String password) {
        System.out.println(username);
         try {
            RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

            registerUserRequest request = registerUserRequest.newBuilder().build();


            registerUserResponse response = serviceBlockingStub.registerUser(request);
            System.out.println("Received response: " + response.getUserpass());
        } catch (Exception e)  {
            e.printStackTrace();
        }


        managedChannel.shutdown();
    }

    public void shutDown() {
        managedChannel.shutdown();
    }


}
