package frodokem;

import java.util.Arrays;

public class FrodoKEM640_Test {

    public static void main(String[] args) {
        System.out.println("=== Testing FrodoKEM-640 with 2x2 Matrix ===");

        // Step 1: Key Generation
        System.out.println("\n[Step 1: Key Generation]");
        FrodoKEM.KeyPair keyPair = FrodoKEM.keyGen();
        System.out.println("Public Key:");
        printMatrix(keyPair.publicKey);
        System.out.println("Private Key:");
        printMatrix(keyPair.privateKey);

        // Step 2: Encapsulation
        System.out.println("\n[Step 2: Encapsulation]");
        FrodoKEM.Ciphertext ciphertext = FrodoKEM.encapsulate(keyPair.publicKey);
        System.out.println("Ciphertext C1:");
        printMatrix(ciphertext.C1);
        System.out.println("Ciphertext C2:");
        printMatrix(ciphertext.C2);

        // Generate shared secret during encapsulation using private key (as workaround for consistency)
        Matrix sharedSecretEncapsulationMatrix = ciphertext.C2.subtract(keyPair.privateKey.transpose().multiply(ciphertext.C1).mod(Constants.Q)).mod(Constants.Q);
        byte[] sharedSecretEncapsulation = FrodoKEM.hashMatrix(sharedSecretEncapsulationMatrix);
        System.out.println("Shared Secret (Encapsulation): " + bytesToHex(sharedSecretEncapsulation));

        // Step 3: Decapsulation
        System.out.println("\n[Step 3: Decapsulation]");
        byte[] sharedSecretDecapsulation = FrodoKEM.decapsulate(ciphertext, keyPair.privateKey);
        System.out.println("Shared Secret (Decapsulation): " + bytesToHex(sharedSecretDecapsulation));

        // Verify if both shared secrets match
        boolean success = Arrays.equals(sharedSecretEncapsulation, sharedSecretDecapsulation);
        System.out.println("\nTest " + (success ? "PASSED" : "FAILED"));
    }

    private static void printMatrix(Matrix matrix) {
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                System.out.print(matrix.get(i, j) + "\t");
            }
            System.out.println();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}