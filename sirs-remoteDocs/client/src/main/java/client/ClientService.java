package client;

import java.io.File;

import com.google.protobuf.ByteString;
import io.grpc.*;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.grpc.netty.GrpcSslContexts;
import sirs.remoteDocs.RemoteDocsServiceGrpc;
import sirs.remoteDocs.*;


import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static DataBaseLib.Messages.*;


public class ClientService {
    public ManagedChannel managedChannel;

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$"; /* (?=.*[!@#&()–[{}]:;',?/*~$^+=<>]) - Special characters */
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);


    private User loggedInUser;
    public String user_owner;

    //private PrivateKey privKey;
    //public PublicKey pubKey;


    public ClientService(String serverHost, int serverPort) {
        File CAsCertFile = new File("../utils/src/main/resources/CACert.pem");
        File clientCertFile = new File("../utils/src/main/resources/UserCert.pem");
        File clientKeyFile = new File("../utils/src/main/resources/UserKey.pem");

        try {

            SslContext sslContext = GrpcSslContexts.forClient()
                    .keyManager(clientCertFile, clientKeyFile)
                    .trustManager(CAsCertFile)
                    .build();

            managedChannel = NettyChannelBuilder
                    .forAddress(serverHost, serverPort)
                    .sslContext(sslContext)
                    .build();

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

                     // Generating
                     CryptoGenerator cg = new CryptoGenerator();
                     KeyPair pair = cg.GenerateKeys(4096);
                     PrivateKey privKey = pair.getPrivate();
                     PublicKey pubKey = pair.getPublic();


                     //String pubKeyEncoded = Base64.getEncoder().encodeToString( this.pubKey.getEncoded() );


                     registerUserRequest request = registerUserRequest.newBuilder()
                                                        .setUsername(username).setPassword(password)
                                                        .setPublicKey(ByteString.copyFrom(pubKey.getEncoded()))
                                                        .build();
                     registerUserResponse response = serviceBlockingStub.registerUser(request);

                     String responseMessage = response.getRegisterResponse();

                     if(responseMessage.equals(USER_REGISTERED)) {
                         Log("ClientService: Register - ", responseMessage);
                         // If the register was successful, then we save it in the file
                         String publicKeyPath = "src/main/resources/PublicKey_" + username;
                         String privateKeyPath = "src/main/resources/PrivateKey_" + username;
                         cg.createKeyFile(publicKeyPath, pubKey.getEncoded());
                         cg.createKeyFile(privateKeyPath, privKey.getEncoded());
                     }

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

            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = algorithm.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }

            loginUserRequest request = loginUserRequest.newBuilder()
                                        .setUsername(username).setPassword(sb.toString())
                                        .build();
            loginUserResponse response = serviceBlockingStub.loginUser(request);

            String responseMessage = response.getLoginResponse();

            Log("ClientService: Login - ", responseMessage);


            if(responseMessage.equals(USER_LOGGED)) {
                this.loggedInUser = new User(username);
                user_owner = username;
            }
            return responseMessage;

        } catch (Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    public String createDocument(String owner, String filename, String content) {

        try {
            if (!owner.isEmpty() && !filename.isEmpty()) {
                RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

                /* Creating document keys */
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(512);
                KeyPair keyPair = kpg.generateKeyPair();

                PublicKey docPuKey = keyPair.getPublic();
                PrivateKey docPrKey = keyPair.getPrivate();
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, docPuKey);
                byte[] cipherContent  = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
                String newContent = Base64.getEncoder().encodeToString(cipherContent);

                /* Getting public key of owner */
                String ownerPubKeyPath = "/src/main/resources/PublicKey_" + owner;
                CryptoGenerator cg = new CryptoGenerator();
                PublicKey pubKey = cg.loadPublicKey(ownerPubKeyPath);

                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                byte[] ownerReadKey  = cipher.doFinal(docPrKey.getEncoded());
                byte[] ownerWriteKey = cipher.doFinal(docPuKey.getEncoded());

                createDocumentRequest request = createDocumentRequest.newBuilder()
                        .setOwner(owner).setFilename(filename).setContent(newContent)
                        .setOwnerReadKey(ByteString.copyFrom(ownerReadKey)).setOwnerWriteKey(ByteString.copyFrom(ownerWriteKey))
                        .build();
                createDocumentResponse response = serviceBlockingStub.createDocument(request);


                String responseMessage = response.getCreateDocResponse();

                Log("ClientService: CreateDoc - ", responseMessage);

                if (responseMessage.equals(FILE_CREATED)) {
                    this.loggedInUser.addFileAsOwner(filename);

                }
                System.out.println("Return ClientService; " + responseMessage);
                return responseMessage;
            }
            else {
                return EMPTY_USERNAME_OR_FILENAME;
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }
        return "NOK";
    }

   public String addDocumentContributor(String owner, String contributor, String filename, String permission) {
        try {
            if (!owner.isEmpty() && !filename.isEmpty() && !contributor.isEmpty() ) {
                // Owner retrieves their read/write keys for the file
                RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

                String currentPermission;
                if (permission.equals("read")) {
                    currentPermission = "r";
                }
                else if (permission.equals("write")) {
                    currentPermission = "w";
                }
                else {
                    return INVALID_PERMISSION;
                }

                getOwnerReadAndWriteKeyRequest request1 = getOwnerReadAndWriteKeyRequest.newBuilder()
                                                          .setOwner(owner).setFilename(filename).build();
                getOwnerReadAndWriteKeyResponse response1 = serviceBlockingStub.getOwnerReadAndWriteKey(request1);

                byte[] ownerReadKey = response1.getOwnerReadKey().toByteArray();
                byte[] ownerWriteKey = response1.getOwnerWriteKey().toByteArray();


                String ownerPubKeyPath = "src/main/resources/PublicKey_" + owner;
                String ownerPrvKeyPath = "src/main/resources/PrivateKey_" + owner;

                CryptoGenerator cg = new CryptoGenerator();
                PublicKey pubKey = cg.loadPublicKey(ownerPubKeyPath);
                PrivateKey prvKey = cg.loadPrivateKey(ownerPrvKeyPath);


                byte[] ownerPublicKeyByte = pubKey.getEncoded();
                byte[] ownerPrivateKeyByte = prvKey.getEncoded();
                byte[] contributorPublicKeyByte = getContributorPublicKey(contributor);

                PublicKey ownerPubKey = cg.convertBytesToPublicKey(ownerPublicKeyByte);
                PublicKey contributorPubKey = cg.convertBytesToPublicKey(contributorPublicKeyByte);
                PrivateKey ownerPrvKey = cg.convertBytesToPrivateKey(ownerPrivateKeyByte);


                byte[] contributorReadKey = new byte[512];
                byte[] contributorWriteKey = new byte[512];
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

                if(permission.equals("write")){
                    cipher.init(Cipher.DECRYPT_MODE, ownerPrvKey);
                    byte[] docPubKeyByte = cipher.doFinal(ownerWriteKey);
                    byte[] docPrvKeyByte = cipher.doFinal(ownerReadKey);
                    cipher.init(Cipher.ENCRYPT_MODE, contributorPubKey);
                    contributorReadKey = cipher.doFinal(docPrvKeyByte);
                    contributorWriteKey = cipher.doFinal(docPubKeyByte);
                }
                else if(permission.equals("read")){
                     cipher.init(Cipher.DECRYPT_MODE, ownerPrvKey);
                     byte[] docPrvKeyByte = cipher.doFinal(ownerReadKey);
                     cipher.init(Cipher.ENCRYPT_MODE, contributorPubKey);
                     contributorReadKey = cipher.doFinal(docPrvKeyByte);
                     contributorWriteKey = new byte[512];
                }

                addContributorRequest request = addContributorRequest.newBuilder()
                                                .setUsernameOwner(owner).setUsernameContributor(contributor)
                                                .setFilename(filename).setPermission(currentPermission).setLoggedInUserName(user_owner)
                                                .setContributorReadKey(ByteString.copyFrom(contributorReadKey))
                                                .setContributorWriteKey(ByteString.copyFrom(contributorWriteKey))
                                                .build();
                addContributorResponse response = serviceBlockingStub.addDocumentContributor(request);

                String responseMessage = response.getAddUserContributorResponse();

                Log("ClientService: Add Document Contributor - ", responseMessage);

                System.out.println(responseMessage);
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

    public String getDocumentContent(String filename, String owner, String username) {
        try {
            RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);
            getDocumentContentRequest request = getDocumentContentRequest.newBuilder()
                                            .setFilename(filename).setOwner(owner)
                                            .setUsername(username).build();

            getDocumentContentResponse response = serviceBlockingStub.getDocumentContent(request);

            /* Receive document content and the respective read or write keys of the "username"(owner or contributor) */
            String content = response.getContent();
            byte[] userReadKey = response.getReadKey().toByteArray();
            byte[] userWriteKey = response.getWriteKey().toByteArray();
            byte[] userPublicKey;
            byte[] userPrivateKey;
            CryptoGenerator cg = new CryptoGenerator();
            PublicKey pubKey;
            PrivateKey privKey;

            if(owner.equals(username)) {
                // we are the owners - load our public keys
                String ownerPubKeyPath = "src/main/resources/PublicKey_" + owner;
                String ownerPrivateKeyPath = "client/src/main/resources/PrivateKey_" + owner;
                pubKey = cg.loadPublicKey(ownerPubKeyPath);
                privKey = cg.loadPrivateKey(ownerPrivateKeyPath);
            }
            else {
                String contributorPubKeyPath = "src/main/resources/PublicKey_" + username;
                String contributorPrivateKeyPath = "src/main/resources/PrivateKey_" + username;
                pubKey = cg.loadPublicKey(contributorPubKeyPath);
                privKey = cg.loadPrivateKey(contributorPrivateKeyPath);

            }

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //TODO: FIX FOR WRITE OR READ- HERE ONLY FOR WRITING
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] privDocKeyBytes = cipher.doFinal(userReadKey);
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] pubDocKeyBytes = cipher.doFinal(userWriteKey);

            // Document
            PrivateKey privDocKey = cg.convertBytesToPrivateKey(privDocKeyBytes);
            cipher.init(Cipher.DECRYPT_MODE, privDocKey);

            //byte[] contentBytes = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
            //byte[] originalContent = cipher.doFinal(contentBytes);
            //String originalContentText = Arrays.toString(originalContent);

            String originalContent = new String(cipher.doFinal(Base64.getDecoder().decode(content)), StandardCharsets.UTF_8);

            System.out.println("ContentDecrypted: " + content);

            return originalContent;

        } catch (Exception e) {
            System.out.println("ClientService: Get Doc Content: " + e.getMessage());
        }

        return null;
    }


    public String editDocumentContent(String filename, String contributor, String owner, String newContent) {
        try {

            if (!filename.isEmpty() && !owner.isEmpty() && !contributor.isEmpty()) {
                RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

                PublicKey pubKey;
                PrivateKey privKey;
                CryptoGenerator cg = new CryptoGenerator();
                byte[] writeKey;

                if(owner.equals(contributor)) {
                    getOwnerReadAndWriteKeyRequest request1 = getOwnerReadAndWriteKeyRequest.newBuilder()
                            .setOwner(owner).setFilename(filename).build();
                    getOwnerReadAndWriteKeyResponse response1 = serviceBlockingStub.getOwnerReadAndWriteKey(request1);

                    writeKey = response1.getOwnerWriteKey().toByteArray(); //(PubDocument)PublicOwner

                    String ownerPrivateKeyPath = "src/main/resources/PrivateKey_" + owner;

                    privKey = cg.loadPrivateKey(ownerPrivateKeyPath);


                } else { // the contributor is editing
                    getContributorReadAndWriteKeyRequest request1 = getContributorReadAndWriteKeyRequest.newBuilder()
                            .setContributor(contributor).setFilename(filename).setOwner(owner).build();
                    getContributorReadAndWriteKeyResponse response1 = serviceBlockingStub.getContributorReadAndWriteKey(request1);

                    writeKey = response1.getContributorWriteKey().toByteArray(); //(PubDocument)PublicOwner


                    String contributorPrivateKeyPath = "src/main/resources/PrivateKey_" + contributor;

                    privKey = cg.loadPrivateKey(contributorPrivateKeyPath);

                }

                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

                cipher.init(Cipher.DECRYPT_MODE, privKey);
                byte[] publicDocKeyBytes = cipher.doFinal(writeKey);

                PublicKey docPubKey = cg.convertBytesToPublicKey(publicDocKeyBytes);

                cipher.init(Cipher.ENCRYPT_MODE, docPubKey);
                byte[] cipherContent  = cipher.doFinal(newContent.getBytes(StandardCharsets.UTF_8));
                String editedContent = Base64.getEncoder().encodeToString(cipherContent);


                editDocContentRequest request = editDocContentRequest.newBuilder()
                                            .setFilename(filename).setContributor(contributor)
                                            .setOwner(owner).setNewContent(editedContent)
                                            .build();
                editDocContentResponse response = serviceBlockingStub.editDocumentContent(request);

                String responseMessage = response.getEditDocumentResponse();
                switch (responseMessage) {
                    case CONTENT_UPDATED:
                    case USER_DOES_NOT_HAVE_PERMISSION:
                    case EDIT_CONTENT_DENIED:
                        Log("ClientService: Edit Document Content - ", responseMessage);
                        return responseMessage;
                    default:
                        Log("ClientService: Edit Document Content -", "An internal error has occured!");
                        return responseMessage;
                }
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


    public void getContributorDocuments() {
        try {
            String contributor = user_owner;
            if (!contributor.isEmpty()) {
                RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

                getContributorDocumentsRequest request = getContributorDocumentsRequest.newBuilder()
                                                        .setContributor(contributor)
                                                        .build();

                Iterator <getContributorDocumentsResponse> responseIterator = serviceBlockingStub.getContributorDocuments(request);
                loggedInUser.cleanFilesAsContributorList();
                while(responseIterator.hasNext()){

                    getContributorDocumentsResponse response = responseIterator.next();
                    String owner = response.getOwner() ;
                    String filename = response.getFilename();
                    String permission = response.getPermission();
                    loggedInUser.addFileAsContributor(filename, owner, permission);
                }

                Log("ClientService: Get Documents as Contributor - ", OK);

            }
            else {
                Log( "ClientService: Get Documents as Contributor - ", EMPTY_USERNAME_OR_FILENAME);
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }

    }

    public void getOwnerDocuments() {

        try {
            String owner = this.loggedInUser.username;
            if (!owner.isEmpty()) {
                RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

                getOwnerDocumentsRequest request = getOwnerDocumentsRequest.newBuilder()
                        .setOwner(this.loggedInUser.username)
                        .build();

                Iterator <getOwnerDocumentsResponse> responseIterator = serviceBlockingStub.getOwnerDocuments(request);
                loggedInUser.cleanFilesAsOwnerList();
                while(responseIterator.hasNext()){

                    getOwnerDocumentsResponse response = responseIterator.next();
                    String filename = response.getFilename();
                    loggedInUser.addFileAsOwner(filename);
                }

                Log("ClientService: Get Documents as Owner - ", OK);

            }
            else {
                Log( "ClientService: Get Documents as Owner - ", EMPTY_USERNAME_OR_FILENAME);
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }

    }

    public byte[] getContributorPublicKey(String contributor) {
        try {
            RemoteDocsServiceGrpc.RemoteDocsServiceBlockingStub serviceBlockingStub = RemoteDocsServiceGrpc.newBlockingStub(managedChannel);

            getContributorPublicKeyRequest request = getContributorPublicKeyRequest.newBuilder()
                                                    .setContributor(contributor)
                                                    .build();

            getContributorPublicKeyResponse response = serviceBlockingStub.getContributorPublicKey(request);

            byte[] contributorPublicKeyBytes = response.getContributorPublicKey().toByteArray();


            return contributorPublicKeyBytes;

        } catch (Exception e) {
            System.out.println("getContributorPublicKey: " + e.getMessage());
        }
        return null;
    }

    public User getLoggedInUser() {
        return this.loggedInUser;
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

    public String getLoggedInUsername() {
        return user_owner;
    }

}
