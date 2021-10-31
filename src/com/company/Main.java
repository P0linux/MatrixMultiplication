package com.company;

import FoxAlgorithm.FoxMultiplier;
import Helpers.IMultiplier;
import Helpers.Matrix;
import SerialAlgorithm.SerialMultiplier;
import StripedAlgorithm.StripedMultiplier;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        executeAlgorithms(5000);
        //executeAlgorithmsComparison();
    }

    private static void executeAlgorithms(int size){

        var processors = Runtime.getRuntime().availableProcessors();

        Matrix firstMatrix = new Matrix(size);
        Matrix secondMatrix = new Matrix(size);

        firstMatrix.seedData();
        secondMatrix.seedData();

        IMultiplier serialMultiplier = new SerialMultiplier();
        IMultiplier stripedMultiplier = new StripedMultiplier(processors);
        IMultiplier foxMultiplier = new FoxMultiplier(processors);

        var time = System.nanoTime();
        var resultValue = serialMultiplier.multiple(firstMatrix, secondMatrix);
        time = System.nanoTime() - time;
        System.out.printf("Serial time: %d ms\n", time/1_000_000);

        time = System.nanoTime();
        resultValue = stripedMultiplier.multiple(firstMatrix, secondMatrix);
        time = System.nanoTime() - time;
        System.out.printf("Striped time: %d ms\n", time/1_000_000);

        time = System.nanoTime();
        resultValue = foxMultiplier.multiple(firstMatrix, secondMatrix);
        time = System.nanoTime() - time;
        System.out.printf("Fox time: %d ms\n", time/1_000_000);
    }

    private static void executeAlgorithmsComparison(){
        var sizes = new int[]{500, 1000, 1500, 2000, 2500, 3000};
        var threadCounts = new int[]{2, 4, 8};

        System.out.println("Size\t\t\tSerial time\t\t\tStriped 2 threads\t\tFox 2 threads\t\tStriped 4 threads\t\tFox 4 threads\t\tStriped 8 threads\t\tFox 8 threads");

        for (var size: sizes){
            System.out.printf("%04d\t\t\t", size);

            var firstMatrix = new Matrix(size);
            var secondMatrix = new Matrix(size);
            var serialMultiplier = new SerialMultiplier();

            System.out.printf("%06d ms\t\t\t", getAverageTime(serialMultiplier, firstMatrix, secondMatrix));

            for (var count: threadCounts){
                var stripedMultiplier = new StripedMultiplier(count);
                var foxMultiplier = new FoxMultiplier(count);

                System.out.printf("%06d ms\t\t\t\t", getAverageTime(stripedMultiplier, firstMatrix, secondMatrix));
                System.out.printf("%06d ms\t\t\t", getAverageTime(foxMultiplier, firstMatrix, secondMatrix));
            }
            System.out.println();
        }
    }

    private static long getAverageTime(IMultiplier multiplier, Matrix firstMatrix, Matrix secondMatrix){
        var execTimes = new long[5];
        for (int i = 0; i < 5; i++) {
            var currTime = System.nanoTime();
            multiplier.multiple(firstMatrix, secondMatrix);
            execTimes[i] = System.nanoTime() - currTime;
        }

        return Arrays.stream(execTimes).sum()/5/1_000_000;
    }
}
