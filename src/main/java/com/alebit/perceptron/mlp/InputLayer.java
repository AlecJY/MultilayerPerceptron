package com.alebit.perceptron.mlp;

import com.alebit.perceptron.OneDMatrix;

import java.lang.reflect.Array;
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

    public double successRate(HiddenLayer hiddenLayer, OutputLayer outputLayer) {
        int error = 0;
        transData = new double[trainingData.length][];
        for (int j = 0; j < trainingData.length; j++) {
            hiddenLayer.setExpOut(trainingData[j][trainingData[j].length - 1]);
            for (HiddenLayer.Neuron neuron: hiddenLayer.getNeurons()) {
                neuron.training(trainingData[j]);
            }
            hiddenLayer.testNextHiddenLayer();
            boolean isError = false;
            for (int i = 0; i < outputLayer.getNeurons().length; i++) {
                OutputLayer.Neuron neuron = (OutputLayer.Neuron) outputLayer.getNeurons()[i];
                if ((neuron.y - 0.5) * (neuron.expOut(i) - 0.5) < 0) {
                    isError = true;
                    break;
                }
            }
            if (isError) {
                error++;
            }
            transData[j] = outputLayer.getNeurons()[0].trainingData;
        }
        return (double) (trainingData.length - error) / trainingData.length;
    }

    public double[][] getTransData() {
        return transData;
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

    public void restoreWCollection(ArrayList<double[][]> wCollection,HiddenLayer hiddenLayer) {
        HiddenLayer firstHiddenLayer = hiddenLayer;
        for (int i = 0; i < wCollection.size(); i++) {
            for (int j = 0; j < hiddenLayer.getNeurons().length; j++) {
                hiddenLayer.getNeurons()[j].setW(wCollection.get(i)[j]);
            }
            if (hiddenLayer.childHiddenLayer != null) {
                hiddenLayer = hiddenLayer.childHiddenLayer;
            }
        }
        successRate(firstHiddenLayer, (OutputLayer) hiddenLayer);
    }

    public double[] test(double[] testDatum, HiddenLayer hiddenLayer, OutputLayer outputLayer) {
        for (HiddenLayer.Neuron neuron: hiddenLayer.getNeurons()) {
            neuron.training(testDatum);
        }
        hiddenLayer.testNextHiddenLayer();
        double[] ys = new double[outputLayer.getNeurons().length];
        for (int i = 0; i < outputLayer.getNeurons().length; i++) {
            ys[i] = outputLayer.getNeurons()[i].y;
        }
        return ys;
    }
}
