package ru.javaops.masterjava.matrix;

public class MatrixMultiply implements Runnable {
    private int rowA;
    private int rowB;
    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] matrixC;

    public MatrixMultiply(int rowA, int rowB, int[][] matrixA, int[][] matrixB, int[][] matrixC) {
        this.rowA = rowA;
        this.rowB = rowB;
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.matrixC = matrixC;
    }

    @Override
    public void run() {
        final int matrixSize = matrixA.length;
        int[] rowMatrixA = matrixA[rowA];
        int[] rowMatrixB = matrixB[rowB];
        int sum = 0;
        for (int i = 0; i < matrixSize; i++) {
            sum += rowMatrixA[i] * rowMatrixB[i];
        }

        matrixC[rowA][rowB] = sum;
    }
}
