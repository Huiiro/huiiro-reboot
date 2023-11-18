package com.huii.auth.factory;

import com.huii.auth.core.entity.KeyPair;
import com.huii.common.utils.FileReaderUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecurityKeyPairFactory {
    private static KeyPair keyPair;

    private SecurityKeyPairFactory() {
    }

    public static KeyPair getKeyPair() {
        if (keyPair == null) {
            keyPair = initKeyPair();
        }
        return keyPair;
    }

    public static String getPublicKeyPem() {
        return getKeyPair().getPrivateKeyPem();
    }

    public static String getPrivateKeyPem() {
        return getKeyPair().getPrivateKeyPem();
    }

    public static String getPublicKey() {
        return getKeyPair().getPublicKey();
    }

    public static String getPrivateKey() {
        return getKeyPair().getPrivateKey();
    }

    public static byte[] getPublicKeyByte() {
        return getKeyPair().getPublicKeyByte();
    }

    public static byte[] getPrivateKeyByte() {
        return getKeyPair().getPrivateKeyByte();
    }

    public static String encryptRSA(String rawStr) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(getPrivateKeyByte()));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipher.doFinal(rawStr.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptRSA(String encryptStr) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptStr);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(getPrivateKeyByte());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyPair initKeyPair() {
        String publicKeyPem = FileReaderUtils.readResourcesFiles("key/publicKey.pem");
        String privateKeyPem = FileReaderUtils.readResourcesFiles("key/privateKey.pem");
        String publicKey = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        String privateKey = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        return new KeyPair(publicKeyPem, privateKeyPem, publicKey, privateKey, publicKeyBytes, privateKeyBytes);
    }
}
