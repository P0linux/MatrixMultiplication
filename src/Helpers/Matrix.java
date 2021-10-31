package Helpers;

import java.util.Random;

public class Matrix {

    private int[][] _matrix;
    public int size;

    public Matrix(int size)
    {
        this.size = size;
        _matrix = new int[size][size];
    }

    public void transpose()
    {
        int[][] newMatrix = new int[size][size];

        for (int i = 0; i < size; i++){
            for (int j = 0; j< size; j++){
                newMatrix[i][j] = _matrix[j][i];
            }
        }

        _matrix = newMatrix;
    }

    public int getValue(int row, int column)
    {
        return _matrix[row][column];
    }

    public void setValue(int row, int column, int value)
    {
        _matrix[row][column] = value;
    }

    public void display()
    {
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                System.out.printf("%d", _matrix[i][j]);
            }
            System.out.println();
        }
    }

    public int[] getRow(int index){
        return _matrix[index];
    }

    public void plusAssign(int row, int col, int value){
        _matrix[row][col] += value;
    }

    public void seedData(){
        Random rng = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                _matrix[i][j] = rng.nextInt(100);
            }
        }
    }
}
