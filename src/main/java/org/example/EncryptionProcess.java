package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import jakarta.xml.bind.*;

public class EncryptionProcess {
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String SECRET_KEY = "UCwatk24HeV3KtjFpYjGDq0PRY2ulyGy3nSPqt8VDBA="; //I used a keygenerator to make a key

    public SecretKeySpec generateSecretKey() throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(SECRET_KEY.getBytes());
        return new SecretKeySpec(keyBytes, ENCRYPTION_ALGORITHM);
    }

    public void readFromFile(String fileName) {
        try {
            String data = new String(Files.readAllBytes(Paths.get(fileName)));
            System.out.println("Content of " + fileName + ":\n" + data);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public void encryptAndWriteToFile(String plainText, String fileName) {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey());

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            String encryptedData = Base64.getEncoder().encodeToString(encryptedBytes);

            EncryptionData encryptedObject = new EncryptionData(encryptedData, true);

            File xmlFile = new File(fileName);
            jakarta.xml.bind.JAXBContext jaxbCtx = jakarta.xml.bind.JAXBContext.newInstance(EncryptionData.class);
            jakarta.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(encryptedObject, xmlFile);

            System.out.println("String encrypted and written to " + fileName + ".");
        } catch (Exception e) {
            System.out.println("Error encrypting and writing the data: " + e.getMessage());
        }
    }

    public void decryptAndPrintToFile(String encryptedFile) {
        try {
            jakarta.xml.bind.JAXBContext jaxbCtx = jakarta.xml.bind.JAXBContext.newInstance(EncryptionData.class);
            jakarta.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            EncryptionData encryptedObject = (EncryptionData) unmarshaller.unmarshal(new File(encryptedFile));

            String encryptedData = encryptedObject.getData();

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateSecretKey());

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            String decryptedData = new String(decryptedBytes);

            System.out.println("Decrypted data:\n" + decryptedData);
        } catch (Exception e) {
            System.out.println("Error decrypting and printing the data: " + e.getMessage());
        }
    }
}
