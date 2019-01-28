package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[][] matrixBT = transpose(matrixB);

        List<Future> futures = new ArrayList<>();
        for (int rowA = 0; rowA < matrixSize; rowA++) {
            for (int rowB = 0; rowB < matrixSize; rowB++) {
                Future element = executor.submit(new MatrixMultiply(rowA, rowB, matrixA, matrixBT, matrixC));
                futures.add(element);
            }
        }
        for (Future future : futures) {
            future.get();
        }

        return matrixC;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[][] matrixBT = transpose(matrixB);
        for (int rowA = 0; rowA < matrixSize; rowA++) {
            for (int rowB = 0; rowB < matrixSize; rowB++) {
                matrixC[rowA][rowB] = multiply(matrixA[rowA], matrixBT[rowB]);
            }
        }
        return matrixC;
    }

    private static int[][] transpose(int[][] matrix) {
        final int matrixSize = matrix.length;
        int[][] transposedMatrix = new int[matrixSize][matrixSize];
        for (int row = 0; row < matrixSize; row++) {
            for (int col = 0; col < matrixSize; col++) {
                transposedMatrix[col][row] = matrix[row][col];
            }
        }

        return transposedMatrix;
    }

    private static int multiply(int[] rowMatrixA, int[] rowMatrixBT) {
        final int matrixSize = rowMatrixA.length;
        int sum = 0;
        for (int i = 0; i < matrixSize; i++) {
            sum += rowMatrixA[i] * rowMatrixBT[i];
        }

        return sum;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
