package ru.javaops.masterjava.matrix;

public class MatrixMultiply implements Runnable {
    private int rowA;
    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] matrixC;

    public MatrixMultiply(int rowA, int[][] matrixA, int[][] matrixB, int[][] matrixC) {
        this.rowA = rowA;
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.matrixC = matrixC;
    }

    @Override
    public void run() {
        final int matrixSize = matrixA.length;
        int[] rowMatrixA = matrixA[rowA];

        for (int rowB = 0; rowB < matrixSize; rowB++) {
            int sum = 0;
            int[] rowMatrixB = matrixB[rowB];
            for (int i = 0; i < matrixSize; i++) {
                sum += rowMatrixA[i] * rowMatrixB[i];
            }
            matrixC[rowA][rowB] = sum;
        }
    }
}
