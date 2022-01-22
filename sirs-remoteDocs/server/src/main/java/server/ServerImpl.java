package server;

import io.grpc.stub.StreamObserver;
import sirs.remoteDocs.registerUserRequest;
import sirs.remoteDocs.registerUserResponse;
import sirs.remoteDocs.RemoteDocsServiceGrpc.RemoteDocsServiceImplBase;


public class ServerImpl extends RemoteDocsServiceImplBase {

    DataBaseServer db;

    public ServerImpl() {
        db = new DataBaseServer();
    }


    @Override
    public void registerUser(registerUserRequest request,
                             StreamObserver<registerUserResponse> responseObserver) {
        String usernamePass = db.getUsername();

        System.out.println("Request: username" + request.getUsername() + "pass: " + request.getPassword());

        registerUserResponse response = registerUserResponse.newBuilder()
                                        .setUserpass(usernamePass).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
