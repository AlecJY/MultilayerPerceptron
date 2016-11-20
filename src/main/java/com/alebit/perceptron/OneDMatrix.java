package com.alebit.perceptron;

/**
 * Created by Alec on 2016/10/14.
 */
public class OneDMatrix {
    private double[] matrixData;

    public OneDMatrix(int size) {
        matrixData = new double[size];
    }

    public OneDMatrix(double[] trainingData) {
        matrixData = new double[trainingData.length - 1];
        System.arraycopy(trainingData, 0, matrixData, 0, matrixData.length);
    }

    public void set(int index, double num) {
        matrixData[index] = num;
    }

    public double get(int index) {
        return matrixData[index];
    }

    public int size() {
        return matrixData.length;
    }

    public double[] toArray() {
        return matrixData;
    }

    public double multiply(OneDMatrix matrix) {
        double[] data = matrix.toArray();
        if (data.length != matrixData.length) {
            throw new NumberFormatException();
        }
        double result = 0;
        for (int i = 0; i < data.length; i++) {
            result += matrixData[i] * data[i];
        }
        return result;
    }

    public OneDMatrix multiply(double num) {
        OneDMatrix result = new OneDMatrix(size());
        for (int i = 0; i < size(); i++) {
            result.set(i, get(i)*num);
        }
        return result;
    }

    public OneDMatrix add(OneDMatrix matrix) {
        if (size() != matrix.size()) {
            throw new NumberFormatException();
        }
        OneDMatrix result = new OneDMatrix(size());
        for (int i = 0; i < size(); i++) {
            result.set(i, get(i) + matrix.get(i));
        }
        return result;
    }
}
