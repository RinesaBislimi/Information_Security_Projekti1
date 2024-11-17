package frodokem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class FrodoKEM_Test {

    private static final String OUTPUT_FOLDER = "output";

    public static void main(String[] args) {
        System.out.println("=== Testing FrodoKEM ===");

        // Ensure output folder exists
        createOutputFolder();

        // Step 1: Key Generation
        System.out.println("[Step 1: Key Generation]");
        FrodoKEM.KeyPair keyPair = FrodoKEM.keyGen();

        // Save Public and Private Keys
        System.out.println("Public Key:");
        keyPair.publicKey.print();
        writeMatrixToFile(keyPair.publicKey, "public_key.txt");

        System.out.println("Private Key:");
        keyPair.privateKey.print();
        writeMatrixToFile(keyPair.privateKey, "private_key.txt");

        // Step 2: Encapsulation
        System.out.println("[Step 2: Encapsulation]");
        FrodoKEM.Ciphertext ciphertext = FrodoKEM.encapsulate(keyPair.publicKey);

        // Save Ciphertext Matrices
        System.out.println("Ciphertext C1:");
        ciphertext.C1.print();
        writeMatrixToFile(ciphertext.C1, "ciphertext_C1.txt");

        System.out.println("Ciphertext C2:");
        ciphertext.C2.print();
        writeMatrixToFile(ciphertext.C2, "ciphertext_C2.txt");
        // Save randomness
        

        // Generate shared secret during encapsulation
        byte[] sharedSecretEncapsulation = FrodoKEM.hashMatrix(
                ciphertext.C2.subtract(keyPair.privateKey.transpose().multiply(ciphertext.C1).mod(Constants.Q)).mod(Constants.Q)
        );

        System.out.println("Shared Secret (Encapsulation): " + bytesToHex(sharedSecretEncapsulation));
        writeToFile("Shared Secret (Encapsulation): " + bytesToHex(sharedSecretEncapsulation), "shared_secret_encapsulation.txt");

        // Step 3: Decapsulation
        System.out.println("[Step 3: Decapsulation]");
        byte[] sharedSecretDecapsulation = FrodoKEM.decapsulate(ciphertext, keyPair.privateKey);

        System.out.println("Shared Secret (Decapsulation): " + bytesToHex(sharedSecretDecapsulation));
        writeToFile("Shared Secret (Decapsulation): " + bytesToHex(sharedSecretDecapsulation), "shared_secret_decapsulation.txt");

        // Verify if both shared secrets match
        boolean success = Arrays.equals(sharedSecretEncapsulation, sharedSecretDecapsulation);
        System.out.println("\nTest " + (success ? "PASSED" : "FAILED"));
        writeToFile("Test " + (success ? "PASSED" : "FAILED"), "test_result.txt");
    } 

    // Ensure the output folder exists
    private static void createOutputFolder() {
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Output folder created: " + OUTPUT_FOLDER);
            } else {
                System.err.println("Failed to create output folder.");
            }
        }
    }


    // Write matrix data to a text file
    public static void writeMatrixToFile(Matrix matrix, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FOLDER + "/" + fileName))) {
            for (int i = 0; i < matrix.rows; i++) {
                for (int j = 0; j < matrix.cols; j++) {
                    writer.write(matrix.get(i, j) + "\t");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing matrix to file " + fileName + ": " + e.getMessage());
        }
    }

    // Write textual content to a text file
    private static void writeToFile(String content, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FOLDER + "/" + fileName))) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to file " + fileName + ": " + e.getMessage());
        }
    }

    // Convert byte array to hexadecimal representation
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
