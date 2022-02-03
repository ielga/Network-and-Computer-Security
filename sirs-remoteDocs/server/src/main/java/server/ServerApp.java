package server;

import com.google.protobuf.ByteString;
import io.grpc.*;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.NettyServerBuilder;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import sirs.remoteDocs.DatabaseBackupServiceGrpc;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import sirs.remoteDocs.DatabaseBackupServiceGrpc;

import sirs.remoteDocs.loginUserRequest;
import sirs.remoteDocs.loginUserResponse;

import sirs.remoteDocs.backupDatabaseRequest;
import sirs.remoteDocs.backupDatabaseReply;




public class ServerApp {

    public static ManagedChannel databaseChannel;

    public static void main(String[] args) {
        try {

            for (String arg : args) System.out.println("Args are: " + arg);

            File CAsCertFile = new File("../utils/src/main/resources/CACert.pem");   // CAfile
            File serverCertFile = new File("../utils/src/main/resources/ServerCert.pem"); //certChainFile
            File serverKeyFile = new File("../utils/src/main/resources/ServerKey.pem"); //privateKeyFile

            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String dbHost = args[2];
            int dbPort = Integer.parseInt(args[3]);


            /* MUTUAL TLS AUTHENTICATION */
            SslContextBuilder sslContextBuilder = SslContextBuilder.forServer(serverCertFile, serverKeyFile)
                    .clientAuth(ClientAuth.REQUIRE)
                    .trustManager(CAsCertFile);
            SslContext sslContext = GrpcSslContexts.configure(sslContextBuilder).build();

            Server server =  NettyServerBuilder.forPort((port))
                            .sslContext(sslContext)
                            .addService(new ServerImpl()).build();

            connectToDatabaseBackupServer(dbHost, dbPort);
            ServerApp.startAutomaticDatabaseBackup();

            server.start();
            server.awaitTermination();

        }
        catch(Exception e) {
            System.out.println("Server: " + e);
        }
    }

    public static void connectToDatabaseBackupServer(String dbHost, int dbPort) {

        File CAsCertFile = new File("../utils/src/main/resources/CACert.pem");
        File serverCertFile = new File("../utils/src/main/resources/UserCert.pem");
        File serverKeyFile = new File("../utils/src/main/resources/UserKey.pem");

        try {

            SslContext sslContext = GrpcSslContexts.forClient()
                    .keyManager(serverCertFile, serverKeyFile)
                    .trustManager(CAsCertFile)
                    .build();

            databaseChannel = NettyChannelBuilder
                    .forAddress(dbHost, dbPort)
                    .sslContext(sslContext)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startAutomaticDatabaseBackup() {
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                doDatabaseBackup();
                sendSQLbackup();
            }
        }, 0, 45, TimeUnit.MINUTES);
    }

    public static void doDatabaseBackup() {
        try {

            // String dir = System.getProperty("user.dir");
            // System.out.println("DIR: "+ dir);

            String filePath = "src/main/resources/DataBaseBackup.sh";
            File file = new File(filePath);


            if(!file.isFile()){
                throw new IllegalArgumentException("The file " + filePath + " does not exist");
            }
            else {
                Runtime runtime = Runtime.getRuntime();
                // Process process = runtime.exec(new String[]{"/bin/sh", "-c", filePath}, null);
                Process process = runtime.exec(new String[]{"bash", filePath}, null);

                BufferedReader lineReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                lineReader.lines().forEach(System.out::println);

                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                errorReader.lines().forEach(System.out::println);

                System.out.println("Backing up database files...");
            }

        } catch (IOException e) {
            System.out.println("Database Backup Server: ");
            e.printStackTrace();
        }
    }

    public static void sendSQLbackup() {
        try {
            File dir = new File("src/main/DataBaseBackups");
            File[] files = dir.listFiles();
            String filePath = null;

            if (files != null) {
                Arrays.sort(files, Comparator.comparingLong(File::lastModified));
                filePath = "src/main/DataBaseBackups/" + files[files.length-1].getName();
            }

            DatabaseBackupServiceGrpc.DatabaseBackupServiceBlockingStub serviceBlockingStub = DatabaseBackupServiceGrpc.newBlockingStub(databaseChannel);

            if (filePath == null) return;

            FileInputStream fileInputStream = new FileInputStream(filePath);

            byte[] buffer = new byte[1024*1024];
            int total = 0;
            int n_read = 0;
            while (true) {
                n_read = fileInputStream.read(buffer);
                if (n_read <= 0) {
                    break;
                }
                else total += n_read;
            }

            backupDatabaseRequest request = backupDatabaseRequest.newBuilder().setSqlBackupFile(ByteString.copyFrom(buffer, 0, total)).build();

            backupDatabaseReply reply = serviceBlockingStub.sendDatabaseBackup(request);

            boolean responseMessage = reply.getBackupResponse();

            if(!responseMessage) {
                // try to send back up again ?
            }
            return;

        } catch (Exception e)  {
            e.printStackTrace();
        }
        databaseChannel.shutdown();
    }

}
