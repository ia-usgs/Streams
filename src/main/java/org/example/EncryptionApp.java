package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Scanner;
import java.util.Base64;
import jakarta.xml.bind.annotation.*;

@XmlRootElement
class EncryptedData {
    private String data;
    private boolean encrypted;

    public EncryptedData() {
    }

    public EncryptedData(String data, boolean encrypted) {
        this.data = data;
        this.encrypted = encrypted;
    }

    @XmlElement
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @XmlElement
    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}

public class EncryptionApp {
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String SECRET_KEY = "UCwatk24HeV3KtjFpYjGDq0PRY2ulyGy3nSPqt8VDBA="; //I used a keygenerator to make a key

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n----- Top-Level Menu -----");
            System.out.println("1. Read a file given a name by the user.");
            System.out.println("2. Encode a string to encrypted form and write it out to a file.");
            System.out.println("3. Decrypt a coded string back to the original string given a file containing the encrypted data.");
            System.out.println("4. Exit the program.");
            System.out.print("Enter your choice (1/2/3/4): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter the file name to read: ");
                    String fileName = scanner.nextLine();
                    readFromFile(fileName);
                    break;
                case 2:
                    System.out.print("Enter the string to encrypt: ");
                    String plainText = scanner.nextLine();
                    System.out.print("Enter the file name to write encrypted data: ");
                    String outputFile = scanner.nextLine();
                    encryptAndWriteToFile(plainText, outputFile);
                    break;
                case 3:
                    System.out.print("Enter the file name to read encrypted data: ");
                    String encryptedFile = scanner.nextLine();
                    decryptAndPrintToFile(encryptedFile);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static SecretKeySpec generateSecretKey() throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(SECRET_KEY.getBytes());
        return new SecretKeySpec(keyBytes, ENCRYPTION_ALGORITHM);
    }

    private static void readFromFile(String fileName) {
        try {
            String data = new String(Files.readAllBytes(Paths.get(fileName)));
            System.out.println("Content of " + fileName + ":\n" + data);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    private static void encryptAndWriteToFile(String plainText, String fileName) {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey());

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            String encryptedData = Base64.getEncoder().encodeToString(encryptedBytes);

            EncryptedData encryptedObject = new EncryptedData(encryptedData, true);

            File xmlFile = new File(fileName);
            jakarta.xml.bind.JAXBContext jaxbCtx = jakarta.xml.bind.JAXBContext.newInstance(EncryptedData.class);
            jakarta.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(encryptedObject, xmlFile);

            System.out.println("String encrypted and written to " + fileName + ".");
        } catch (Exception e) {
            System.out.println("Error encrypting and writing the data: " + e.getMessage());
        }
    }

    private static void decryptAndPrintToFile(String encryptedFile) {
        try {
            jakarta.xml.bind.JAXBContext jaxbCtx = jakarta.xml.bind.JAXBContext.newInstance(EncryptedData.class);
            jakarta.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            EncryptedData encryptedObject = (EncryptedData) unmarshaller.unmarshal(new File(encryptedFile));

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
