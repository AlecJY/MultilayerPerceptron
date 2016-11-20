package com.alebit.perceptron.mlp;

import com.alebit.perceptron.OneDMatrix;

/**
 * Created by Alec on 2016/11/17.
 */
public class InputLayer {
    private double[][] trainingData;

    public InputLayer(double[][] trainingData) {
        this.trainingData = trainingData;
    }

    public void learning(HiddenLayer hiddenLayer) {
        for (double[] trainingDatum: trainingData) {
            hiddenLayer.setExpOut(trainingDatum[trainingDatum.length - 1]);
            for (HiddenLayer.Neuron neuron: hiddenLayer.getNeurons()) {
                neuron.training(trainingDatum);
            }
            hiddenLayer.nextHiddenLayer();
        }
    }

    public void testing(HiddenLayer hiddenLayer) {
        for (double[] trainingDatum: trainingData) {
            hiddenLayer.setExpOut(trainingDatum[trainingDatum.length - 1]);
            for (HiddenLayer.Neuron neuron: hiddenLayer.getNeurons()) {
                neuron.training(trainingDatum);
            }
            hiddenLayer.testNextHiddenLayer();
        }
    }

    public boolean testSuccessful(HiddenLayer hiddenLayer, OutputLayer outputLayer, double threshold) {
        for (double[] trainingDatum: trainingData) {
            hiddenLayer.setExpOut(trainingDatum[trainingDatum.length - 1]);
            for (HiddenLayer.Neuron neuron: hiddenLayer.getNeurons()) {
                neuron.training(trainingDatum);
            }
            hiddenLayer.testNextHiddenLayer();
            for (int i = 0; i < outputLayer.getYs().length - 1; i++) {
                // System.out.println(outputLayer.getYs()[i] + ", " + outputLayer.getYs()[outputLayer.getYs().length - 1]);
                if ((outputLayer.getYs()[i] - threshold) * (outputLayer.getYs()[outputLayer.getYs().length - 1] - threshold) < 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
