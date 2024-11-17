package frodokem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FrodoKEM_TestVectors {

    private static final String OUTPUT_FOLDER = "output";

    public static void main(String[] args) {
        System.out.println("=== FrodoKEM Test Vectors ===");

        try {
            // Step 1: Load saved data from the output folder
            System.out.println("\n[Step 1: Load Saved Data]");
            Matrix publicKey = readMatrixFromFile(OUTPUT_FOLDER + "/public_key.txt");
            System.out.println("Loaded Public Key:");
            publicKey.print();

            Matrix privateKey = readMatrixFromFile(OUTPUT_FOLDER + "/private_key.txt");
            System.out.println("Loaded Private Key:");
            privateKey.print();

            Matrix ciphertextC1 = readMatrixFromFile(OUTPUT_FOLDER + "/ciphertext_C1.txt");
            System.out.println("Loaded Ciphertext C1:");
            ciphertextC1.print();

            Matrix ciphertextC2 = readMatrixFromFile(OUTPUT_FOLDER + "/ciphertext_C2.txt");
            System.out.println("Loaded Ciphertext C2:");
            ciphertextC2.print();

            // Load saved randomness
            Matrix A = readMatrixFromFile(OUTPUT_FOLDER + "/random_A.txt");
            Matrix r = readMatrixFromFile(OUTPUT_FOLDER + "/random_r.txt");
            Matrix e1 = readMatrixFromFile(OUTPUT_FOLDER + "/random_e1.txt");
            Matrix e2 = readMatrixFromFile(OUTPUT_FOLDER + "/random_e2.txt");

            // Recompute C1 = (A * r + e1) mod Q
            Matrix recomputedCiphertext_C1 = A.multiply(r).add(e1).mod(Constants.Q);
            System.out.println("Recomputed Ciphertext C1:");
            recomputedCiphertext_C1.print();

            // Recompute C2 = (publicKey * r + e2) mod Q
            Matrix recomputedCiphertext_C2 = publicKey.multiply(r).add(e2).mod(Constants.Q);
            System.out.println("Recomputed Ciphertext C2:");
            recomputedCiphertext_C2.print();

            // Validate C1 and C2
            System.out.println("\n[Step 2: Validate Encapsulation]");
            boolean c1Matches = compareMatrices(ciphertextC1, recomputedCiphertext_C1);
            System.out.println("Ciphertext C1 Matches: " + c1Matches);

            boolean c2Matches = compareMatrices(ciphertextC2, recomputedCiphertext_C2);
            System.out.println("Ciphertext C2 Matches: " + c2Matches);

            // Step 3: Validate Shared Secrets
            System.out.println("\n[Step 3: Validate Shared Secrets]");
            byte[] sharedSecretEncapsulation = readSharedSecretFromFile(OUTPUT_FOLDER + "/shared_secret_encapsulation.txt");
            byte[] sharedSecretDecapsulation = readSharedSecretFromFile(OUTPUT_FOLDER + "/shared_secret_decapsulation.txt");

            byte[] recomputedSharedSecret = FrodoKEM.decapsulate(new FrodoKEM.Ciphertext(ciphertextC1, ciphertextC2), privateKey);

            System.out.println("Recomputed Shared Secret (Encapsulation): " + bytesToHex(sharedSecretEncapsulation));
            System.out.println("Recomputed Shared Secret (Decapsulation): " + bytesToHex(recomputedSharedSecret));

            boolean encapsulationMatches = Arrays.equals(sharedSecretEncapsulation, recomputedSharedSecret);
            boolean decapsulationMatches = Arrays.equals(sharedSecretDecapsulation, recomputedSharedSecret);

            System.out.println("Shared Secret Encapsulation Matches: " + encapsulationMatches);
            System.out.println("Shared Secret Decapsulation Matches: " + decapsulationMatches);

            // Final result
            boolean success = c1Matches && c2Matches && encapsulationMatches && decapsulationMatches;
            System.out.println("\nTest Vectors Validation " + (success ? "PASSED" : "FAILED"));

        } catch (IOException e) {
            System.err.println("Error validating test vectors: " + e.getMessage());
        }
    }

    // Helper to read a matrix from a file
    private static Matrix readMatrixFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int[][] tempData = new int[Constants.MATRIX_SIZE][Constants.MATRIX_SIZE];
            int row = 0;

            while ((line = reader.readLine()) != null) {
                String[] values = line.trim().split("\t");
                for (int col = 0; col < values.length; col++) {
                    tempData[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }
            return new Matrix(tempData);
        }
    }

    // Helper to read shared secret from a file
    private static byte[] readSharedSecretFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String hexString = reader.readLine().trim().split(":")[1].trim(); // Extract hex part
            return hexStringToByteArray(hexString);
        }
    }

    // Convert hex string to byte array
    private static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    // Convert byte array to hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Compares two matrices element by element
    public static boolean compareMatrices(Matrix m1, Matrix m2) {
        if (m1.rows != m2.rows || m1.cols != m2.cols) {
            System.out.println("Matrix dimensions do not match!");
            return false; // Dimensions mismatch
        }

        // Compare each element
        for (int i = 0; i < m1.rows; i++) {
            for (int j = 0; j < m1.cols; j++) {
                if (m1.get(i, j) != m2.get(i, j)) {
                    System.out.printf("Mismatch at (%d, %d): m1=%d, m2=%d%n", i, j, m1.get(i, j), m2.get(i, j));
                    return false; // Element mismatch
                }
            }
        }
        return true; // All elements match
    }
}
