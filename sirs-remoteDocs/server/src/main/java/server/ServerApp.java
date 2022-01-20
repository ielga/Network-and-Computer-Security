package server;

import io.grpc.Server;

import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;

import java.net.InetSocketAddress;

public class ServerApp {
    public static void main(String[] args) {
        try {
          //  System.out.println("server.Server is loading...");

          //  Server server = ServerBuilder.forPort(8080)
           //                 .addService(new ServerImpl()).build();
            Server server = NettyServerBuilder.forAddress(new InetSocketAddress("localhost", 8080))
                            .addService(new ServerImpl())

                           // .addListenAddress(new InetSocketAddress("localhost", 8080))
                            .build();

            //TODO: Add args to command line to input a different ports

            server.start();
            server.awaitTermination();


        }
        catch(Exception e) {
            System.out.println("Server: " + e);
        }
    }
}
