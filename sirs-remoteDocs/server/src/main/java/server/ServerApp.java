package server;

import io.grpc.*;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.File;
import java.net.InetSocketAddress;


public class ServerApp {
    public static void main(String[] args) {
        try {
           /*  System.out.println("server.Server is loading...");

              Server server = ServerBuilder.forPort(8080)
                            .addService(new ServerImpl()).build(); */


            File CAsCertFile = new File("sirs-remoteDocs/utils/src/main/resources/CACert.pem");   // CAfile
            File serverCertFile = new File("sirs-remoteDocs/utils/src/main/resources/ServerCert.pem"); //certChainFile
            File serverKeyFile = new File("sirs-remoteDocs/utils/src/main/resources/ServerKey.pem"); //privateKeyFile

            /* MUTUAL TLS AUTHENTICATION */
            SslContextBuilder sslContextBuilder = SslContextBuilder.forServer(serverCertFile, serverKeyFile)
                    .clientAuth(ClientAuth.REQUIRE)
                    .trustManager(CAsCertFile);
            SslContext sslContext = GrpcSslContexts.configure(sslContextBuilder).build();

            Server server =  NettyServerBuilder.forPort((8443))
                    .sslContext(sslContext)
                    .addService(new ServerImpl()).build();


            server.start();
            server.awaitTermination();

        }
        catch(Exception e) {
            System.out.println("Server: " + e);
        }
    }
}
