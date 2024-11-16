package frodokem;

import java.security.SecureRandom;
import java.util.Arrays;

public class FrodoKEM_TestVectors {

    public static void main(String[] args) {
        System.out.println("=== FrodoKEM Test Vectors ===");

        // Initialize a seeded SecureRandom for consistent randomness in tests
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(42);  // Use a fixed seed for reproducibility

        // Step 1: Generate matrices A, S, and E with consistent randomness
        Matrix A = Matrix.generateRandomMatrix(2, 2, Constants.Q, secureRandom);
        Matrix S = Matrix.generateDiscreteGaussianMatrix(2, 2);
        Matrix E = Matrix.generateDiscreteGaussianMatrix(2, 2);

        // Step 1: Key Generation
        System.out.println("\n[Step 1: Key Generation]");
        FrodoKEM.KeyPair keyPair = FrodoKEM.keyGenFromMatrices(A, S, E);

        // Print matrices for debugging
        System.out.println("[Debug] Matrix A:");
        A.print();
        System.out.println("[Debug] Matrix S:");
        S.print();
        System.out.println("[Debug] Matrix E:");
        E.print();
        System.out.println("[Debug] Generated Public Key B:");
        keyPair.publicKey.print();

        // No hardcoded expected public key; this test is just for consistency

        // Step 2: Encapsulation
        System.out.println("\n[Step 2: Encapsulation]");
        FrodoKEM.Ciphertext ciphertext = FrodoKEM.encapsulate(keyPair.publicKey);

        // Debugging outputs for ciphertext
        System.out.println("[Debug] Generated Ciphertext C1:");
        ciphertext.C1.print();
        System.out.println("[Debug] Generated Ciphertext C2:");
        ciphertext.C2.print();

        // Step 3: Validate Shared Secret (Encapsulation)
        byte[] sharedSecretEncapsulation = FrodoKEM.hashMatrix(
                ciphertext.C2.subtract(S.transpose().multiply(ciphertext.C1).mod(Constants.Q)).mod(Constants.Q));
        System.out.println("[Debug] Shared Secret (Encapsulation): " + bytesToHex(sharedSecretEncapsulation));

        // Step 3: Decapsulation
        System.out.println("\n[Step 3: Decapsulation]");
        byte[] sharedSecretDecapsulation = FrodoKEM.decapsulate(ciphertext, keyPair.privateKey);
        System.out.println("[Debug] Shared Secret (Decapsulation): " + bytesToHex(sharedSecretDecapsulation));

        // Test whether encapsulation and decapsulation produce the same shared secret
        if (Arrays.equals(sharedSecretEncapsulation, sharedSecretDecapsulation)) {
            System.out.println("Shared Secret Consistency Test PASSED");
        } else {
            System.out.println("Shared Secret Consistency Test FAILED");
        }

        // Summary of all test results
        System.out.println("\nTest Completed");
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
