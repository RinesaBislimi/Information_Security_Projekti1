import java.security.SecureRandom;

public class MatrixUtils {
    private static final SecureRandom secureRandom = new SecureRandom();

    // Funksioni për krijimin e matricave të rastësishme
    public static int[][] generateRandomMatrix(int rows, int cols, int modulus) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = secureRandom.nextInt(modulus);
            }
        }
        return matrix;
    }

    public static int[][] matrixAdd(int[][] A, int[][] B, int q) {
        int rows = A.length;
        int cols = A[0].length;
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = (A[i][j] + B[i][j]) % q;
            }
        }
        return result;
    }

    public static int[][] matrixMultiply(int[][] A, int[][] B, int q) {
        int rows = A.length;
        int cols = B[0].length;
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < A[0].length; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
                result[i][j] %= q;
            }
        }
        return result;
    }
}