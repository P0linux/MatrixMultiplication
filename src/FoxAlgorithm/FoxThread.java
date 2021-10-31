package FoxAlgorithm;

import Helpers.Matrix;

public class FoxThread extends Thread{
    private Matrix firstMatrix;
    private Matrix secondMatrix;
    private Matrix newMatrix;

    private int stepI;
    private int stepJ;

    public FoxThread(
            Matrix firstMatrix,
            Matrix secondMatrix,
            Matrix newMatrix,
            int stepI,
            int stepJ) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.newMatrix = newMatrix;
        this.stepI = stepI;
        this.stepJ = stepJ;
    }

    @Override
    public void run() {
        Matrix blockRes = multiplyBlock();

        for (int i = 0; i < blockRes.size; i++) {
            for (int j = 0; j < blockRes.size; j++) {
                newMatrix.plusAssign(i + stepI, j + stepJ, blockRes.getValue(i, j));
            }
        }
    }

    private Matrix multiplyBlock() {
        Matrix blockRes = new Matrix(firstMatrix.size);
        for (int i = 0; i < firstMatrix.size; i++) {
            for (int j = 0; j < secondMatrix.size; j++) {
                for (int k = 0; k < firstMatrix.size; k++) {
                    blockRes.plusAssign(i, j, firstMatrix.getValue(i, k) * secondMatrix.getValue(k, j));
                }
            }
        }
        return blockRes;
    }
}
