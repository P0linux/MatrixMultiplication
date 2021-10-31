package SerialAlgorithm;

import Helpers.IMultiplier;
import Helpers.Matrix;

public class SerialMultiplier implements IMultiplier {

    @Override
    public Matrix multiple(Matrix firstMatrix, Matrix secondMatrix) {

        if (firstMatrix.size != secondMatrix.size)
            throw new IllegalArgumentException("Matrices size must be the same");

        secondMatrix.transpose();

        var newMatrix = new Matrix(firstMatrix.size);
        for (int row = 0; row < firstMatrix.size; row++){
            for (int column = 0; column < secondMatrix.size; column++){
                var value = getCellValue(firstMatrix, secondMatrix, row, column);
                newMatrix.setValue(row, column, value);
            }
        }

        secondMatrix.transpose();
        return newMatrix;
    }

    public int getCellValue(Matrix firstMatrix, Matrix secondMatrix, int row, int column)
    {
        int cellValue = 0;

        for (int i = 0; i < secondMatrix.size; i++)
        {
            cellValue += firstMatrix.getValue(row, i) * secondMatrix.getValue(column, i);
        }

        return cellValue;
    }
}
