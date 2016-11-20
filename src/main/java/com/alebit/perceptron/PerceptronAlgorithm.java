package com.alebit.perceptron;

import com.alebit.perceptron.mlp.HiddenLayer;
import com.alebit.perceptron.mlp.InputLayer;
import com.alebit.perceptron.mlp.OutputLayer;

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
    private double[] classification = new double[2];
    private boolean trainSucceed = false;
    private boolean test;
    private OneDMatrix w;
    private String name;
    private StringBuffer log = new StringBuffer();
    private boolean enableLog = true;
    private long seed;

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

        classification[0] = rawData[0][rawData[0].length-1];
        for (int i = 0; i < rawData.length; i++) {
            if (rawData[i][rawData[0].length-1] != classification[0]) {
                classification[1] = rawData[i][rawData[0].length-1];
                break;
            }
        }
        if (classification[0] > classification[1]) {
            double tmp = classification[0];
            classification[0] = classification[1];
            classification[1] = tmp;
        }
        if (enableLog) {
            log("====== " + name + " Start Training =====");
            log("Classification 1: " + (int) classification[0] + "\t Classification 2: " + (int) classification[1]);
        }
    }

    public void calculate(int hidLayer, int hidUnit, int outUnit) {
        InputLayer inputLayer = new InputLayer(learningData);
        OutputLayer outputLayer = new OutputLayer(outUnit, hidUnit);
        HiddenLayer[] hiddenLayers = new HiddenLayer[hidLayer];
        if (hiddenLayers.length > 1) {
            hiddenLayers[hiddenLayers.length - 1] = new HiddenLayer(hidUnit, hidUnit, outputLayer);
            outputLayer.setParentHiddenLayer(hiddenLayers[hiddenLayers.length - 1]);
            for (int i = hiddenLayers.length - 2; i > 0; i++) {
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

        for (; iterateTimes > 0; iterateTimes--) {
            inputLayer.learning(hiddenLayers[0]);
        }

        if (enableLog) {
            log("====== " + name + " End Training =====");
        }
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
        return 0;
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
        double count = 0;

        return (testData.length - count) / testData.length;
    }

    private void log(String msg) {
        log.append("<" + name +"> " + msg + "\n");
    }


    public String getLog() {
        return log.toString();
    }
}
