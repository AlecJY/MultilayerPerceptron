package com.alebit.perceptron.mlp;

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
}
