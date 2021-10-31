package FoxAlgorithm;

import Helpers.IMultiplier;
import Helpers.Matrix;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FoxMultiplier implements IMultiplier {

    private int threadCount;

    public FoxMultiplier(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public Matrix multiple(Matrix firstMatrix, Matrix secondMatrix)
    {
        if (firstMatrix.size != secondMatrix.size)
            throw new IllegalArgumentException("Matrices size must be the same");

        var newMatrix = new Matrix(firstMatrix.size);

        threadCount = Math.min(threadCount, firstMatrix.size);
        threadCount = findNearestDivider(threadCount, firstMatrix.size);
        int step = firstMatrix.size / threadCount;
        ExecutorService exec = Executors.newFixedThreadPool(threadCount);
        ArrayList<Future> threads = new ArrayList<>();

        int[][] matrixOfSizesI = new int[threadCount][threadCount];
        int[][] matrixOfSizesJ = new int[threadCount][threadCount];

        int stepI = 0;
        for (int i = 0; i < threadCount; i++) {
            int stepJ = 0;
            for (int j = 0; j < threadCount; j++) {
                matrixOfSizesI[i][j] = stepI;
                matrixOfSizesJ[i][j] = stepJ;
                stepJ += step;
            }
            stepI += step;
        }

        for (int l = 0; l < threadCount; l++) {
            for (int i = 0; i < threadCount; i++) {
                for (int j = 0; j < threadCount; j++) {
                    int stepI0 = matrixOfSizesI[i][j];
                    int stepJ0 = matrixOfSizesJ[i][j];

                    int stepI1 = matrixOfSizesI[i][(i + l) % threadCount];
                    int stepJ1 = matrixOfSizesJ[i][(i + l) % threadCount];

                    int stepI2 = matrixOfSizesI[(i + l) % threadCount][j];
                    int stepJ2 = matrixOfSizesJ[(i + l) % threadCount][j];

                    FoxThread t = new FoxThread(
                            copyIntBlock(firstMatrix, stepI1, stepJ1, step),
                            copyIntBlock(secondMatrix, stepI2, stepJ2, step),
                            newMatrix,
                            stepI0,
                            stepJ0);
                    threads.add(exec.submit(t));
                }
            }
        }

        for (Future mapFuture : threads) {
            try {
                mapFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        exec.shutdown();
        return newMatrix;
    }

    private Matrix copyIntBlock(Matrix matrix, int i, int j, int size) {
        Matrix block = new Matrix(size);
        for (int k = 0; k < size; k++) {
            System.arraycopy(matrix.getRow(k + i), j, block.getRow(k), 0, size);
        }
        return block;
    }

    private int findNearestDivider(int s, int p) {
        int i = s;
        while (i > 1) {
            if (p % i == 0) break;
            if (i >= s) {
                i++;
            } else {
                i--;
            }
            if (i > Math.sqrt(p)) i = Math.min(s, p / s) - 1;
        }

        return i >= s ? i : i != 0 ? p / i : p;
    }
}
