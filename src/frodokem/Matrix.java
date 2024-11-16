package frodokem;

import java.security.SecureRandom;

public class Matrix {
    private final int[][] data;
    public final int rows;
    public final int cols;
    private static final SecureRandom secureRandom = new SecureRandom();

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows][cols];
    }
    
    public Matrix(int[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0) {
            throw new IllegalArgumentException("Input data must be a non-empty 2D array.");
        }
        int cols = data[0].length;
        for (int[] row : data) {
            if (row.length != cols) {
                throw new IllegalArgumentException("All rows must have the same number of columns.");
            }
        }
        this.rows = data.length;
        this.cols = cols;
        this.data = new int[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, this.cols);
        }
    }

    public int get(int row, int col) {
        return data[row][col];
    }

    public void set(int row, int col, int value) {
        data[row][col] = value;
    }

    // Adds modular reduction to each element in the matrix
    public Matrix mod(int modulus) {
        if (modulus <= 0) {
            throw new IllegalArgumentException("Modulus must be a positive integer.");
        }
        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.set(i, j, (this.get(i, j) % modulus + modulus) % modulus);
            }
        }
        return result;
    }

    // Matrix addition
    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Matrices must have the same dimensions to be added.");
        }
        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.set(i, j, this.get(i, j) + other.get(i, j));
            }
        }
        return result;
    }

    // Matrix subtraction
    public Matrix subtract(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Matrices must have the same dimensions to be subtracted.");
        }
        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.set(i, j, this.get(i, j) - other.get(i, j));
            }
        }
        return result;
    }

    // Matrix multiplication
    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("The number of columns in the first matrix must equal the number of rows in the second matrix.");
        }
        Matrix result = new Matrix(this.rows, other.cols);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                int sum = 0;
                for (int k = 0; k < this.cols; k++) {
                    sum += this.get(i, k) * other.get(k, j);
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }

    // Matrix transpose
    public Matrix transpose() {
        Matrix result = new Matrix(this.cols, this.rows);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.set(j, i, this.get(i, j));
            }
        }
        return result;
    }

    // Generate a discrete Gaussian noise matrix with the specified rows and columns
    public static Matrix generateDiscreteGaussianMatrix(int rows, int cols) {
        Matrix matrix = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i, j, sampleDiscreteGaussian(Constants.SIGMA));
            }
        }
        return matrix;
    }

    // Generate a random matrix with values mod modulus
    public static Matrix generateRandomMatrix(int rows, int cols, int modulus) {
        Matrix matrix = new Matrix(rows, cols);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i, j, random.nextInt(modulus));
            }
        }
        return matrix;
    }

    // Sample a discrete Gaussian noise value with mean 0 and standard deviation sigma
    private static int sampleDiscreteGaussian(double sigma) {
        double u1 = secureRandom.nextDouble();
        double u2 = secureRandom.nextDouble();
        double gaussian = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2) * Constants.SIGMA;
        return (int) Math.round(gaussian);
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(this.get(i, j) + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Check if this matrix is equal to another matrix
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Matrix other = (Matrix) obj;
        if (this.rows != other.rows || this.cols != other.cols) return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (this.get(i, j) != other.get(i, j)) return false;
            }
        }
        return true;
    }
    // Generate a random matrix with values mod modulus using a specific SecureRandom instance
public static Matrix generateRandomMatrix(int rows, int cols, int modulus, SecureRandom random) {
    Matrix matrix = new Matrix(rows, cols);
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            matrix.set(i, j, random.nextInt(modulus));
        }
    }
    return matrix;
}
}
