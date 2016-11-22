package com.alebit.perceptron.mlp;

import com.alebit.perceptron.OneDMatrix;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alec on 2016/11/17.
 */
public class InputLayer {
    private double[][] trainingData;
    private double[][] transData;

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

    public double successRate(HiddenLayer hiddenLayer, OutputLayer outputLayer, HashMap<Double,Integer> classMap) {
        int error = 0;
        transData = new double[trainingData.length][];
        for (int j = 0; j < trainingData.length; j++) {
            hiddenLayer.setExpOut(trainingData[j][trainingData[j].length - 1]);
            for (HiddenLayer.Neuron neuron: hiddenLayer.getNeurons()) {
                neuron.training(trainingData[j]);
            }
            hiddenLayer.testNextHiddenLayer();
            double orgClass = Math.round(outputLayer.getYs()[outputLayer.getYs().length - 1] * (classMap.size() - 1));
            for (int i = 0; i < outputLayer.getYs().length - 1; i++) {
                if (outputLayer.getYs()[i] < orgClass / classMap.size() || outputLayer.getYs()[i] >= (orgClass + 1) /classMap.size()) {
                    error++;
                }
            }
            transData[j] = outputLayer.getNeurons()[0].trainingData;
        }
        return (double) (trainingData.length - error) / trainingData.length;
    }

    public ArrayList<double[][]> getWCollection(HiddenLayer hiddenLayer) {
        ArrayList<double[][]> wCollection = new ArrayList<>();
        while (hiddenLayer != null) {
            double[][] ws = new double[hiddenLayer.getNeurons().length][];
            for (int i = 0; i < ws.length; i++) {
                ws[i] = hiddenLayer.getNeurons()[i].getW();
            }
            wCollection.add(ws);
            hiddenLayer = hiddenLayer.childHiddenLayer;
        }
        return wCollection;
    }

    public void restoreWCollection(ArrayList<double[][]> wCollection,HiddenLayer hiddenLayer, HashMap<Double, Integer> classMap) {
        HiddenLayer firstHiddenLayer = hiddenLayer;
        for (int i = 0; i < wCollection.size(); i++) {
            for (int j = 0; j < hiddenLayer.getNeurons().length; j++) {
                hiddenLayer.getNeurons()[j].setW(wCollection.get(i)[j]);
            }
            if (hiddenLayer.childHiddenLayer != null) {
                hiddenLayer = hiddenLayer.childHiddenLayer;
            }
        }
        successRate(firstHiddenLayer, (OutputLayer) hiddenLayer, classMap);
    }

    public double[][] getTransData() {
        return transData;
    }
}
