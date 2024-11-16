package frodokem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FrodoKEM {

    public static class KeyPair {
        public final Matrix publicKey;
        public final Matrix privateKey;

        public KeyPair(Matrix publicKey, Matrix privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    // Standard key generation using random matrices
    public static KeyPair keyGen() {
        // Generate the random matrix A, and discrete Gaussian noise matrices S and E
        Matrix A = Matrix.generateRandomMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, Constants.Q);
        Matrix S = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE);
        Matrix E = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE);

        // Calculate public key B = (A * S + E) mod Q
        Matrix B = A.multiply(S).add(E).mod(Constants.Q);
        return new KeyPair(B, S);
    }

    // Key generation with predefined matrices for testing purposes
    public static KeyPair keyGenFromMatrices(Matrix A, Matrix S, Matrix E) {
        // Calculate public key B = (A * S + E) mod Q
        Matrix B = A.multiply(S).add(E).mod(Constants.Q);
        return new KeyPair(B, S);
    }

    public static class Ciphertext {
        public final Matrix C1;
        public final Matrix C2;

        public Ciphertext(Matrix C1, Matrix C2) {
            this.C1 = C1;
            this.C2 = C2;
        }
    }

    // Encapsulation function to generate ciphertext
    public static Ciphertext encapsulate(Matrix publicKey) {
        // Generate random matrix A and discrete Gaussian noise matrices r, e1, e2
        Matrix A = Matrix.generateRandomMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, Constants.Q);
        Matrix r = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE);
        Matrix e1 = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE);
        Matrix e2 = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE);
    
        // Calculate C1 = (A * r + e1) mod Q
        Matrix C1 = A.multiply(r).add(e1).mod(Constants.Q);
    
        // Calculate C2 = (publicKey * r + e2) mod Q
        Matrix C2 = publicKey.multiply(r).add(e2).mod(Constants.Q);
    
        return new Ciphertext(C1, C2);
    }

    // Decapsulation function to derive the shared secret
    public static byte[] decapsulate(Ciphertext ct, Matrix privateKey) {
        Matrix C1 = ct.C1;
        Matrix C2 = ct.C2;

        // Derive shared secret matrix by calculating C2 - (S^T * C1) mod Q
        Matrix derivedSecret = privateKey.transpose().multiply(C1).mod(Constants.Q);
        Matrix sharedSecretMatrix = C2.subtract(derivedSecret).mod(Constants.Q);

        // Hash the shared secret matrix to derive the shared secret
        return hashMatrix(sharedSecretMatrix);
    }

    // Hash the shared secret matrix to produce a byte array (shared secret)
    public static byte[] hashMatrix(Matrix matrix) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            for (int i = 0; i < matrix.rows; i++) {
                for (int j = 0; j < matrix.cols; j++) {
                    digest.update((byte) matrix.get(i, j));
                }
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found.");
        }
    }
}
