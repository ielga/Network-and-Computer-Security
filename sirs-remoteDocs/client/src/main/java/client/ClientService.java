package client;

import java.io.File;
import io.grpc.*;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.grpc.netty.GrpcSslContexts;
import sirs.remoteDocs.RemoteDocsServiceGrpc;
import sirs.remoteDocs.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static DataBaseLib.Messages.*;


public class ClientService {
    public ManagedChannel managedChannel;

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$"; /* (?=.*[!@#&()–[{}]:;',?/*~$^+=<>]) - Special characters */
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    private User loggedInUser;


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


    public String registerUser(String username, String password, String confirmPassword) {
         try {
             if (verifyEqualPasswords(password, confirmPassword)) {
                 // Verifies the password syntax requirements are correct
                 if (verifyPasswordSyntax(password)) {

                     RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

                         registerUserRequest request = registerUserRequest.newBuilder()
                                                        .setUsername(username).setPassword(password)
                                                        .build();
                         registerUserResponse response = serviceBlockingStub.registerUser(request);

                         String responseMessage = response.getRegisterResponse();

                         Log("ClientService: Register - ", responseMessage);

                         return responseMessage;
                 }
                 else
                     return WRONG_PASSWORD_SYNTAX;
             }
              else {
                  return MISMATCHED_PASSWORD;
             }
         } catch (Exception e)  {
            e.printStackTrace();
        }
         shutDown();
         return null;
    }

    public String loginUser(String username, String password) {
        try {
            RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

            loginUserRequest request = loginUserRequest.newBuilder()
                                        .setUsername(username).setPassword(password)
                                        .build();
            loginUserResponse response = serviceBlockingStub.loginUser(request);

            String responseMessage = response.getLoginResponse();

            Log("ClientService: Login - ", responseMessage);

            if(responseMessage.equals(USER_LOGGED)) {
                this.loggedInUser = new User(username);
            }

            return responseMessage;

        } catch (Exception e)  {
            e.printStackTrace();
        }
        shutDown();
        return null;
    }


    public String createDocument(String owner, String filename, String content) {
        try {
            if (!owner.isEmpty() && !filename.isEmpty()) {
                RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

                createDocumentRequest request = createDocumentRequest.newBuilder()
                                                .setOwner(owner).setFilename(filename).setContent(content)
                                                .build();
                createDocumentResponse response = serviceBlockingStub.createDocument(request);

                String responseMessage = response.getCreateDocResponse();

                Log("ClientService: CreateDoc - ", responseMessage);

                if (responseMessage.equals(FILE_CREATED)) {
                    this.loggedInUser.addFileAsOwner(filename);
                }

                return responseMessage;
            }
            else {
                return EMPTY_USERNAME_OR_FILENAME;
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }
        shutDown();
        return null;
    }

    public String addDocumentContributor(String owner, String contributor, String filename, String permission) {
        try {
            if (!owner.isEmpty() && !filename.isEmpty() && !contributor.isEmpty()) {
                RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

                addContributorRequest request = addContributorRequest.newBuilder()
                                                .setUsernameOwner(owner).setUsernameContributor(contributor)
                                                .setFilename(filename).setPermission(permission)
                                                .build();
                addContributorResponse response = serviceBlockingStub.addDocumentContributor(request);

                String responseMessage = response.getAddUserContributorResponse();

                Log("ClientService: Add Document Contributor - ", responseMessage);

                return responseMessage;
            }
            else {
                return EMPTY_USERNAME_OR_FILENAME;
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }
        shutDown();
        return null;
    }


    public String editDocumentContent(String filename, String contributor, String owner, String newContent){
        try {
            if (!filename.isEmpty() && !owner.isEmpty() && !contributor.isEmpty()) {
                RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

                editDocContentRequest request = editDocContentRequest.newBuilder()
                                            .setFilename(filename).setContributor(contributor)
                                            .setOwner(owner).setNewContent(newContent)
                                            .build();
                editDocContentResponse response = serviceBlockingStub.editDocumentContent(request);

                String responseMessage = response.getEditDocumentResponse();

                Log("ClientService: Edit Document Content - ", responseMessage);

                return responseMessage;
            }
            else {
                return EMPTY_USERNAME_OR_FILENAME;
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }
        shutDown();
        return null;
    }

    public void shutDown() {
        managedChannel.shutdown();
    }

    public void Log(String outputInterface, String message) {
        System.out.println(outputInterface + "Received Response from Server: " +  message);
    }

    public boolean verifyEqualPasswords(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean verifyPasswordSyntax(String password) {
        /*  Password must contain:
            - one numeric value
            - one lowercase letter
            - one uppercase letter
            - length of at least 8 characters
        */
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
