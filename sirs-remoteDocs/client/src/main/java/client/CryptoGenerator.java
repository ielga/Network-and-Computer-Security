package client;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class CryptoGenerator {
    //private KeyPairGenerator keyGenerator;
    //private KeyPair keyPair;
    private PrivateKey privateKey;
    private PublicKey publicKey;



    public  KeyPair GenerateKeys(int keySize) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            SecureRandom sr = new SecureRandom();
            kpg.initialize(keySize, sr);

            return kpg.generateKeyPair();

        } catch( NoSuchAlgorithmException e ) {
            System.out.println("Crypto Generator1: " + e.getMessage());
        }

        return null;
    }

    public byte[] createReadKey(PrivateKey docPrivKey, PublicKey userPubKey) {
        return getBytes(userPubKey, docPrivKey.getEncoded());
    }

    public byte[] createWriteKey(PublicKey docPubKey, PublicKey userPubKey) {
        return getBytes(userPubKey, docPubKey.getEncoded());
    }

    private byte[] getBytes(PublicKey userPubKey, byte[] encoded) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, userPubKey);
            return cipher.doFinal(encoded);
        } catch (Exception e) {
            System.out.println("Crypto Generator-CipherService2: " + e.getMessage());
        }
        return null;
    }




    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }


    public byte[] getPublicKeyEncoded() {
        return this.publicKey.getEncoded();
    }

    public byte[] getPrivateKeyEncoded() {
        return this.privateKey.getEncoded();
    }

    public void createKeyFile(String filepath, byte[] key) {
    System.out.println("Create key file enter");
        try {
            java.io.File keyFile = new File(filepath);
            // keyFile.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(keyFile);
            fos.write(key);
            fos.close();
        }
        catch (IOException e) {
            System.out.println("Crypto Generator3: " + e.getMessage());
        }
    }


    public PrivateKey loadPrivateKey(String privateKeyPath) {
        try {
           FileInputStream privateKeyFile = new FileInputStream(privateKeyPath);
           byte[] privateEncoded = new byte[privateKeyFile.available()];
           privateKeyFile.read(privateEncoded);
           privateKeyFile.close();

           PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateEncoded); //####
           KeyFactory kfPrivate = KeyFactory.getInstance("RSA");
           return kfPrivate.generatePrivate(privateSpec);

        } catch (Exception e) {
            System.out.println("Crypto Generator4: " + e.getMessage());
        }
        return null;
    }

    public PublicKey convertBytesToPublicKey(byte[] pubKeyBytes) {
        try {
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(pubKeyBytes);
            KeyFactory kfPublic = KeyFactory.getInstance("RSA");

            return kfPublic.generatePublic(publicSpec);
        } catch (Exception e) {
            System.out.println("Crypto Generator: Convert bytes to Public Key" + e.getMessage());
        }
        return null;
    }

    public PrivateKey convertBytesToPrivateKey(byte[] privateKeyByte) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
            System.out.println("PrivateKeyBytes: " + privateKey);
            return privateKey;
        } catch (Exception e) {
            System.out.println("Crypto Generator: convert bytes to Private Key" + e.getMessage());
        }
        return null;
    }


    public PublicKey loadPublicKey(String publicKeyPath) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(publicKeyPath);
            if (in == null) {
                return null;
            }
            File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            FileInputStream publicKeyFile = new FileInputStream(tempFile);
            byte[] publicEncoded = new byte[publicKeyFile.available()];
            publicKeyFile.read(publicEncoded);;
            publicKeyFile.close();

            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicEncoded);
            KeyFactory kfPublic = KeyFactory.getInstance("RSA");

            return kfPublic.generatePublic(publicSpec);
        } catch (Exception e) {
            System.out.println("Crypto Generator: Load Public Key " + e.getMessage());
        }
        return null;
    }

    public String getBase64String(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] getBytesFromBase64String(String message) {
        return Base64.getDecoder().decode(message);
    }


    public class CipherService {
        public Cipher cipher;

        public CipherService() {
            super();
            createCipher();
        }

        public String encryptText(String message, PublicKey docPubKey) {
            System.out.println("publickey do doc when encrypt text: " + docPubKey);
            try {

                this.cipher.init(Cipher.ENCRYPT_MODE, docPubKey);

                return getBase64String(message);

            } catch (Exception e) {
                System.out.println("Crypto Generator-CipherService5: " + e.getMessage());
            }
            return "Unknown";
        }

        public void createCipher() {
            try {
                this.cipher = Cipher.getInstance("RSA");
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                System.out.println("Crypto Generator-CipherService6: " + e.getMessage());
            }
        }

        public byte[] createReadKey_second(PrivateKey docPrivKey, PublicKey userPubKey) {
            System.out.println("CreateReadKEY1"+docPrivKey + userPubKey);
            try {
               // Cipher cipher = Cipher.getInstance("RSA");
                this.cipher.init(Cipher.ENCRYPT_MODE, userPubKey);

                return cipher.doFinal(docPrivKey.getEncoded());

            } catch (Exception e) {
                System.out.println("Crypto Generator-CipherService7: " + e.getMessage());
            }
            return null;
        }

        public String decryptText(String message, PrivateKey privKey) {
            try {

                this.cipher.init(Cipher.DECRYPT_MODE, privKey);

                return new String(cipher.doFinal(Base64.getDecoder().decode(message)), StandardCharsets.UTF_8);

            } catch (Exception e) {
                System.out.println("Crypto Generator-CipherService8: " + e.getMessage());
            }
            return "Unknown";
        }

    }

}
