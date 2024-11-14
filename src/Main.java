public class Main {
    public static void main(String[] args) {
        // Krijojmë një instancë të FrodoKEM për të verifikuar parametrat
        FrodoKEM frodoKEM = new FrodoKEM();

        // Printojmë vlerën e modës q dhe dimensionet për ta verifikuar
        System.out.println("Modulo q: " + frodoKEM.getQ());
        System.out.println("Dimensioni i matricës n: " + frodoKEM.getN());
        System.out.println("Dimensioni i matricës nbar: " + frodoKEM.getNbar());
        System.out.println("Dimensioni i matricës mbar: " + frodoKEM.getMbar());

        // Krijojmë dy matrica të rastësishme për të testuar operacionet
        int[][] matrixA = MatrixUtils.generateRandomMatrix(2, 2, frodoKEM.getQ());
        int[][] matrixB = MatrixUtils.generateRandomMatrix(2, 2, frodoKEM.getQ());

        // Printojmë matricat e krijuara
        System.out.println("\nMatrica A:");
        printMatrix(matrixA);

        System.out.println("\nMatrica B:");
        printMatrix(matrixB);

        // Testimi i funksionit të mbledhjes së matricave
        int[][] sumMatrix = MatrixUtils.matrixAdd(matrixA, matrixB, frodoKEM.getQ());
        System.out.println("\nShuma e matricave A dhe B:");
        printMatrix(sumMatrix);

        // Testimi i funksionit të shumëzimit të matricave
        int[][] productMatrix = MatrixUtils.matrixMultiply(matrixA, matrixB, frodoKEM.getQ());
        System.out.println("\nProdukti i matricave A dhe B:");
        printMatrix(productMatrix);
    }

    // Funksion ndihmës për të printuar matricat
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int elem : row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }
}