package org.example;

import java.util.Scanner;

public class MainEncryptionApp {
    private static final EncryptionProcess processor = new EncryptionProcess();

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
                    processor.readFromFile(fileName);
                    break;
                case 2:
                    System.out.print("Enter the string to encrypt: ");
                    String plainText = scanner.nextLine();
                    System.out.print("Enter the file name to write encrypted data: ");
                    String outputFile = scanner.nextLine();
                    processor.encryptAndWriteToFile(plainText, outputFile);
                    break;
                case 3:
                    System.out.print("Enter the file name to read encrypted data: ");
                    String encryptedFile = scanner.nextLine();
                    processor.decryptAndPrintToFile(encryptedFile);
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
}
