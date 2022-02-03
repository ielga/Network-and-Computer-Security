package server;


import io.grpc.Server;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.io.File;

public class DatabaseBackupServer {

    public static void main(String[] args) {
        try {

            for (String arg : args) System.out.println("Args are: " + arg);

            String dir = System.getProperty("user.dir");

            // directory from where the program was launched
            // e.g /home/mkyong/projects/core-java/java-io
            System.out.println(dir);

            // TODO: IN MAVEN TERMINAL: "../utils/src/main/resources/CACert.pem"
            File CAsCertFile = new File("../utils/src/main/resources/CACert.pem");   // CAfile
            File dbBackupServerCertFile = new File("../utils/src/main/resources/DatabaseBackupServerCert.pem"); //certChainFile
            File dbBackupServerKeyFile = new File("../utils/src/main/resources/DatabaseBackupServerKey.pem"); //privateKeyFile

            /* MUTUAL TLS AUTHENTICATION */
            SslContextBuilder sslContextBuilder = SslContextBuilder.forServer(dbBackupServerCertFile, dbBackupServerKeyFile)
                                                .clientAuth(ClientAuth.REQUIRE)
                                                .trustManager(CAsCertFile);
            SslContext sslContext = GrpcSslContexts.configure(sslContextBuilder).build();

            Server server =  NettyServerBuilder.forPort((8445))
                            .sslContext(sslContext)
                            .addService(new DatabaseBackupServerImpl()).build();


            server.start();
            server.awaitTermination();

        }
        catch(Exception e) {
            System.out.println("Database Backup Server: " );
            e.printStackTrace();
        }
    }

}
