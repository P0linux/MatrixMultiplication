package StripedAlgorithm;

import Helpers.IMultiplier;
import Helpers.Matrix;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StripedMultiplier implements IMultiplier {

    private final int threadCount;

    public StripedMultiplier(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public Matrix multiple(Matrix firstMatrix, Matrix secondMatrix) {

        if (firstMatrix.size != secondMatrix.size)
            throw new IllegalArgumentException("Matrices size must be the same");

        var resultMatrix = new Matrix(firstMatrix.size);

        secondMatrix.transpose();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Map<String, Number>>> futureList = new ArrayList<>();

        for (int i = 0; i < secondMatrix.size; i++){
            for (int j = 0; j < firstMatrix.size; j++){
                var callable = new StripedCallable(firstMatrix.getRow(j), secondMatrix.getRow(i), j, i);
                Future<Map<String, Number>> future = executorService.submit(callable);
                futureList.add(future);
            }
        }

        for (var future : futureList){
            try {
                var resultValue = future.get();
                var row = (int) resultValue.get("rowIndex");
                var column = (int) resultValue.get("columnIndex");
                var cellValue = (int) resultValue.get("cellValue");
                resultMatrix.setValue(row, column, cellValue);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        secondMatrix.transpose();
        return resultMatrix;
    }
}
