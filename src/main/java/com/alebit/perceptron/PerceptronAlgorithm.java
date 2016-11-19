package com.alebit.perceptron;

import com.alebit.perceptron.mlp.InputLayer;

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

    public double[] calculate() {
        InputLayer inputLayer = new InputLayer(learningData);

        if (enableLog) {
            log("====== " + name + " End Training =====");
        }
        return w.toArray();
    }

    private OneDMatrix training(OneDMatrix w) {
        for (int i = 0; i < iterateTimes; i++) {
            if (enableLog) {
                log("===== " + (i + 1) + "th Training =====");
            }
            double total = 0;
            for (double[] data : rawData) {
                OneDMatrix vector = new OneDMatrix(data);
                double e = data[data.length - 1] - out(vector, w);

                // log
                if (enableLog) {
                    StringBuffer msg = new StringBuffer();
                    msg.append("dot: (" + String.format("%.3f", data[0]));
                    for (int j = 1; j < data.length - 1; j++) {
                        msg.append(", " + String.format("%.3f", data[j]));
                    }
                    msg.append("), w: (" + String.format("%.3f", threshold));
                    for (int j = 0; j < w.size(); j++) {
                        msg.append( ", " + String.format("%.3f", w.toArray()[j]));
                    }
                    msg.append("), predict: " + (long) data[data.length - 1] + ", actual: " + (long) out(vector, w));
                    if (e == 0) {
                        msg.append(", status: true");
                    } else {
                        msg.append(", status: false");
                    }
                    log(msg.toString());
                }

                total += Math.abs(e);
                OneDMatrix dw = new OneDMatrix(w.size());
                threshold += oTh * learningRate * e;
                for (int j = 0; j < dw.size(); j++) {
                    dw.set(j, vector.get(j) * learningRate * e);
                }
                w = w.add(dw);
            }
            if (total == 0) {
                trainSucceed = true;
                break;
            }
        }
        return w;
    }

    private double out(OneDMatrix vector, OneDMatrix w) {
        double result = vector.multiply(w) + threshold * oTh;
         if (result >= 0) {
            return classification[1];
        } else {
            return classification[0];
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
        double count = 0;
        for (double[] data: rawData) {
            OneDMatrix vector = new OneDMatrix(data);
            if (out(vector, w) != data[data.length-1]) {
                count++;
            }
        }
        return (rawData.length - count) / rawData.length;
    }

    public boolean validateOne(int index) {
        OneDMatrix vector = new OneDMatrix(rawData[index]);
        if (out(vector, w) != rawData[index][rawData[0].length-1]) {
            return false;
        }
        return true;
    }

    public boolean testValidateOne(int index) {
        OneDMatrix vector = new OneDMatrix(testData[index]);
        if (out(vector, w) != testData[index][testData[0].length-1]) {
            return false;
        }
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
        for (double[] data: testData) {
            OneDMatrix vector = new OneDMatrix(data);
            if (enableLog) {
                StringBuffer msg = new StringBuffer();
                msg.append("dot: (" + String.format("%.3f", data[0]));
                for (int j = 1; j < data.length - 1; j++) {
                    msg.append(", " + String.format("%.3f", data[j]));
                }
                msg.append("), predict: " + (long) data[data.length - 1] + ", actual: " + (long) out(vector, w));
                if (out(vector, w) == data[data.length-1]) {
                    msg.append(", status: true");
                } else {
                    msg.append(", status: false");
                }
                log(msg.toString());
            }
            if (out(vector, w) != data[data.length-1]) {
                count++;
            }
        }
        if (enableLog) {
            log("===== " + name + " End Testing=====");
        }
        return (testData.length - count) / testData.length;
    }

    private void log(String msg) {
        log.append("<" + name +"> " + msg + "\n");
    }


    public String getLog() {
        return log.toString();
    }
}
