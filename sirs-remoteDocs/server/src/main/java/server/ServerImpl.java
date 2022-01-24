package server;

import io.grpc.stub.StreamObserver;
import sirs.remoteDocs.*;
import sirs.remoteDocs.RemoteDocsServiceGrpc.RemoteDocsServiceImplBase;

import java.sql.ResultSet;
import static DataBaseLib.Messages.*;


public class ServerImpl extends RemoteDocsServiceImplBase {

    DataBaseServer db;

    public ServerImpl() {
        db = new DataBaseServer();
    }


    @Override
    public void registerUser(registerUserRequest request,
                             StreamObserver<registerUserResponse> responseObserver) {
        String res = db.registerUser(request.getUsername(), request.getPassword());
        registerUserResponse response = registerUserResponse.newBuilder()
                .setRegisterResponse(res).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

   @Override
    public void loginUser(loginUserRequest request,
                          StreamObserver<loginUserResponse> responseObserver){
        String res = db.loginUser(request.getUsername(), request.getPassword());
        loginUserResponse response = loginUserResponse.newBuilder()
                .setLoginResponse(res).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createDocument(createDocumentRequest request,
                               StreamObserver<createDocumentResponse> responseObserver){
        String res = db.createDocument(request.getOwner(), request.getFilename(), request.getContent());
        //createDocumentResponse response = createDocumentResponse.newBuilder()
               // .setCreatedocresponse(res).build();
        //responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addDocumentContributor(addContributorRequest request,
                                       StreamObserver<addContributorResponse> responseObserver){
        String res = db.addDocumentContributor(request.getUsernameOwner(), request.getUsernameContributor(),
                request.getFilename(), request.getPermission());
        addContributorResponse response = addContributorResponse.newBuilder().
                setAddUserContributorResponse(res).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void editDocumentContent(editDocContentRequest request,
                                    StreamObserver<editDocContentResponse> responseObserver){
        String res = db.editDocumentContent(request.getFilename(), request.getContributor(), request.getOwner(),
                request.getNewContent());
        editDocContentResponse response = editDocContentResponse.newBuilder().
                setEditDocumentResponse(res).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getContributorDocuments(getContributorDocumentsRequest request,
                                               StreamObserver<getContributorDocumentsResponse> responseObserver) {
        try {
            ResultSet rs = db.getContributorDocuments(request.getContributor());

            if (rs == null) {
                // Then there is nothing in the database with this contributor
                getContributorDocumentsResponse response = getContributorDocumentsResponse.newBuilder()
                        .setOwner("")
                        .setFilename("")
                        .setPermission("").build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            else {
                while (rs.next()) {
                    getContributorDocumentsResponse response = getContributorDocumentsResponse.newBuilder()
                                            .setOwner(rs.getString("owner"))
                                            .setFilename(rs.getString("filename"))
                                            .setPermission(rs.getString("permission")).build();
                    responseObserver.onNext(response);
                }
                responseObserver.onCompleted();
            }
        } catch (Exception e) {
            System.out.println("ServerImpl: " + RETRIEVING_DOCUMENT_INFO_ERROR);
        }

}

    @Override
    public void getOwnerDocuments(getOwnerDocumentsRequest request,
                                  StreamObserver<getOwnerDocumentsResponse> responseObserver) {
        try {
            ResultSet rs = db.getOwnerDocuments(request.getOwner());

            if (rs == null) {
                // Then there is nothing in the database with this owner
                getOwnerDocumentsResponse response = getOwnerDocumentsResponse.newBuilder()
                                                    .setFilename("")
                                                    .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            else {
                while (rs.next()) {
                    getOwnerDocumentsResponse response = getOwnerDocumentsResponse.newBuilder()
                            .setFilename(rs.getString("filename")).build();
                    responseObserver.onNext(response);
                }
                responseObserver.onCompleted();
            }
        } catch (Exception e) {
            System.out.println("ServerImpl: " + RETRIEVING_DOCUMENT_INFO_ERROR);
        }
    }

}
