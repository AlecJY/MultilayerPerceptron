package com.alebit.perceptron;

import com.alebit.perceptron.mlp.HiddenLayer;
import com.alebit.perceptron.mlp.InputLayer;
import com.alebit.perceptron.mlp.OutputLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Alec on 2016/10/14.
 */
public class PerceptronAlgorithm {
    private double[][] rawData;
    private double[][] learningData;
    private double[][] testData;
    private double learningRate;
    private double iterateTimes;
    private double threshold = 1;
    private double oTh = 1;
    private boolean lastTraining = false;
    private boolean test;
    private OneDMatrix w;
    private String name;
    private StringBuffer log = new StringBuffer();
    private boolean enableLog = true;
    private long seed;
    private InputLayer inputLayer;
    private OutputLayer outputLayer;
    private HiddenLayer[] hiddenLayers;
    private double maxSuccessRate;
    private ArrayList<double[][]> maxWCollection;

    public PerceptronAlgorithm(double[][] rawData, double learningRate, int iterateTimes, String name, boolean test, boolean enableLog, long seed) {
        this.rawData = rawData;
        this.learningRate = learningRate;
        this.iterateTimes = iterateTimes;
        this.name = name;
        this.enableLog = enableLog;
        this.test = test;
        this.seed = seed;
    }

    public void initialize() {
        if (test) {
            int num = rawData.length;
            int learningNum = (int) Math.ceil((double) num * 2 / 3);
            int testNum = num - learningNum;
            learningData = new double[learningNum][];
            testData = new double[testNum][];
            Random random = new Random(seed);
            for (double[] data: rawData) {
                if (random.nextInt(3) < 2) {
                    if (learningNum != 0) {
                        learningData[learningNum-1] = data;
                        learningNum--;
                    } else {
                        testData[testNum-1] = data;
                        testNum--;
                    }
                } else {
                    if (testNum != 0) {
                        testData[testNum-1] = data;
                        testNum--;
                    } else {
                        learningData[learningNum-1] = data;
                        learningNum--;
                    }
                }
            }
        } else {
            learningData = rawData;
        }

        if (enableLog) {
            log("====== " + name + " Start Training =====");
        }
    }

    public void calculate(int hidLayer, int hidUnit, int outUnit) {
        inputLayer = new InputLayer(learningData);
        outputLayer = new OutputLayer(outUnit, hidUnit);
        hiddenLayers = new HiddenLayer[hidLayer];
        if (hiddenLayers.length > 1) {
            hiddenLayers[hiddenLayers.length - 1] = new HiddenLayer(hidUnit, hidUnit, outputLayer);
            outputLayer.setParentHiddenLayer(hiddenLayers[hiddenLayers.length - 1]);
            for (int i = hiddenLayers.length - 2; i > 0; i--) {
                hiddenLayers[i] = new HiddenLayer(hidUnit, hidUnit, hiddenLayers[i + 1]);
                hiddenLayers[i + 1].setParentHiddenLayer(hiddenLayers[i]);
            }
            hiddenLayers[0] = new HiddenLayer(hidUnit, learningData[0].length, hiddenLayers[1]);
            hiddenLayers[1].setParentHiddenLayer(hiddenLayers[0]);
        } else {
            hiddenLayers[0] = new HiddenLayer(hidUnit, learningData[0].length, outputLayer);
            outputLayer.setParentHiddenLayer(hiddenLayers[0]);
        }
        hiddenLayers[0].setParentHiddenLayer(null);
        hiddenLayers[0].setLearningRate(learningRate);

        maxSuccessRate = -1;
        maxWCollection = new ArrayList<>();
        for (; iterateTimes > 0; iterateTimes--) {
            inputLayer.learning(hiddenLayers[0]);
            double successRate = inputLayer.successRate(hiddenLayers[0], outputLayer);
            log("Training recognition rate: " + String.format("%.2f", successRate * 100));
            if (maxSuccessRate == -1) {
                maxSuccessRate = successRate;
                maxWCollection = inputLayer.getWCollection(hiddenLayers[0]);
                lastTraining = true;
            } else if (successRate > maxSuccessRate) {
                maxSuccessRate = successRate;
                maxWCollection = inputLayer.getWCollection(hiddenLayers[0]);
                lastTraining = true;
            } else {
                lastTraining = false;
            }
            if (successRate == 1) {
                break;
            }
        }

        if (enableLog) {
            log("====== " + name + " End Training =====");
        }

        if (!lastTraining) {
            inputLayer.restoreWCollection(maxWCollection, hiddenLayers[0]);
        }
    }

    public double[][] getTransData() {
        return inputLayer.getTransData();
    }

    public ArrayList<double[][]> getW() {
        return maxWCollection;
    }

    public double getX(double y) {
        double[] wArray = w.toArray();
        return (-1 * threshold * oTh - y*wArray[1])/wArray[0];
    }

    public double getY(double x) {
        double[] wArray = w.toArray();
        return (-1 * threshold * oTh -x*wArray[0])/wArray[1];
    }

    public double getThreshold() {
        return threshold;
    }

    public double validate() {
        return maxSuccessRate;
    }

    public boolean validateOne(int index) {
        return true;
    }

    public boolean testValidateOne(int index) {
        return true;
    }

    public double[] getTestDatum(int index) {
        return testData[index];
    }

    public int rawSize() {
        return rawData.length;
    }

    public int testSize() {
        return testData.length;
    }

    public double testValidate() {
        if (!test) {
            return -1;
        }
        if (enableLog) {
            log("===== " + name + " Start Testing=====");
        }

        InputLayer testInputLayer = new InputLayer(testData);
        double testRate =  testInputLayer.successRate(hiddenLayers[0], outputLayer);

        if (enableLog) {
            log("===== " + name + " Finish Testing=====");
        }
        return testRate;
    }

    private void log(String msg) {
        log.append("<" + name +"> " + msg + "\n");
    }


    public String getLog() {
        return log.toString();
    }

    public double[] test(double[] testDatum) {
        double[] realTestDatum = new double[testDatum.length + 1];
        System.arraycopy(testDatum, 0, realTestDatum, 0, testDatum.length);
        realTestDatum[realTestDatum.length - 1] = 0;
        return inputLayer.test(realTestDatum, hiddenLayers[0], outputLayer);
    }
}
