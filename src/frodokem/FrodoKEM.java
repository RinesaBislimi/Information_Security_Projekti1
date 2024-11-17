package frodokem;

import java.security.SecureRandom;

import org.bouncycastle.crypto.digests.SHAKEDigest;

public class FrodoKEM {

    public static class KeyPair {
        public final Matrix publicKey;
        public final Matrix privateKey;

        public KeyPair(Matrix publicKey, Matrix privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    // Key generation using random matrices
    public static KeyPair keyGen() {
        // Use the CSPRNG instance for deterministic random generation
        SecureRandom random = CSPRNG.getInstance();

        // Generate random matrix A and discrete Gaussian noise matrices S and E
        Matrix A = Matrix.generateRandomMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, Constants.Q, random);
        Matrix S = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, random);
        Matrix E = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, random);

        // Compute public key B = (A * S + E) mod Q
        Matrix B = A.multiply(S).add(E).mod(Constants.Q);

        return new KeyPair(B, S); // S is used as the private key
    }

    // Key generation using predefined matrices (for testing purposes)
    public static KeyPair keyGenFromMatrices(Matrix A, Matrix S, Matrix E) {
        // Compute public key B = (A * S + E) mod Q
        Matrix B = A.multiply(S).add(E).mod(Constants.Q);

        return new KeyPair(B, S); // S is used as the private key
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
        // Use the CSPRNG instance for deterministic random generation
        SecureRandom random = CSPRNG.getInstance();

        // Generate random matrix A and discrete Gaussian noise matrices r, e1, e2 for FrodoKEM_TestVectors purpose
        Matrix A = Matrix.generateRandomMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, Constants.Q, random);
        Matrix r = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, random);
        Matrix e1 = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, random);
        Matrix e2 = Matrix.generateDiscreteGaussianMatrix(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, random);
         // Save randomness to output folder
        
         FrodoKEM_Test.writeMatrixToFile(A, "random_A.txt");
         FrodoKEM_Test.writeMatrixToFile(r, "random_r.txt");
         FrodoKEM_Test.writeMatrixToFile(e1, "random_e1.txt");
         FrodoKEM_Test.writeMatrixToFile(e2, "random_e2.txt");
 
        // Compute C1 = (A * r + e1) mod Q
        Matrix C1 = A.multiply(r).add(e1).mod(Constants.Q);

        // Compute C2 = (publicKey * r + e2) mod Q
        Matrix C2 = publicKey.multiply(r).add(e2).mod(Constants.Q);

        return new Ciphertext(C1, C2);
    }

    // Decapsulation function to derive the shared secret
    public static byte[] decapsulate(Ciphertext ct, Matrix privateKey) {
        Matrix C1 = ct.C1;
        Matrix C2 = ct.C2;

        // Compute derived secret matrix
        Matrix derivedSecret = privateKey.transpose().multiply(C1).mod(Constants.Q);
        Matrix sharedSecretMatrix = C2.subtract(derivedSecret).mod(Constants.Q);

        // Hash the shared secret matrix to derive the shared secret
        return hashMatrix(sharedSecretMatrix);
    }

    // Hash the shared secret matrix to produce a byte array
    public static byte[] hashMatrix(Matrix matrix) {
        // Use SHAKE-256 for hashing the shared secret matrix
        SHAKEDigest shake256 = new SHAKEDigest(256);
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                byte[] elementBytes = Integer.toString(matrix.get(i, j)).getBytes();
                shake256.update(elementBytes, 0, elementBytes.length);
            }
        }
        byte[] output = new byte[32]; // 256 bits = 32 bytes
        shake256.doFinal(output, 0, output.length);
        return output;
    }
}
