package com.alebit.perceptron;

import java.util.HashMap;

/**
 * Created by Alec on 2016/10/16.
 */
public class MultiPerceptron {
    private double[][] rawData;
    private double[][][] rawDataCollection;
    private double learningRate;
    private int iterateTimes;
    private boolean test;
    private boolean enableLog;
    private HashMap<Double, int[]> classMap = new HashMap<>();
    private int[][] classToPerceptronsMap;
    private PerceptronAlgorithm[] perceptrons;
    private StringBuffer log = new StringBuffer();



    public MultiPerceptron(double[][] rawData, double learningRate, int iterateTimes, boolean test, boolean enableLog) {
        this.rawData = rawData;
        this.learningRate = learningRate;
        this.iterateTimes = iterateTimes;
        this.enableLog = enableLog;
        this.test = test;

        getClassification();
        int perceptronNum;
        for (perceptronNum = 1; Math.round(Math.pow(2, perceptronNum)) < classMap.size(); perceptronNum++);
        perceptrons = new PerceptronAlgorithm[perceptronNum];
        int count = 0;
        buildMap();
        buildPerceptrons();
    }

    private void getClassification() {
        int count = 0;
        for (double[] data: rawData) {
            if (classMap.containsKey(data[data.length-1])) {
                int[] classCount = classMap.get(data[data.length-1]);
                classCount[1]++;
                classMap.replace(data[data.length-1], classCount);
            } else {
                int[] classCount = new int[] {count, 1};
                classMap.put(data[data.length-1], classCount);
                count++;
            }
        }
    }

    private void buildMap() {
        classToPerceptronsMap = new int[classMap.size()][perceptrons.length];
        for (int i = 0; i < classToPerceptronsMap.length; i++) {
            int j = i;
            for (int k = 0; k < perceptrons.length; k++) {
                classToPerceptronsMap[i][k] = j % 2;
                j /= 2;
            }

        }
    }

    private void buildPerceptrons() {
        rawDataCollection = new double[perceptrons.length][rawData.length][rawData[0].length];
        long seed = System.currentTimeMillis();
        for (int i = 0; i < perceptrons.length; i++) {
            for (int j = 0; j < rawData.length; j++) {
                for (int k = 0; k < rawData[0].length-1; k++) {
                    rawDataCollection[i][j][k] = rawData[j][k];
                }
                rawDataCollection[i][j][rawData[0].length-1] = classToPerceptronsMap[classMap.get(rawData[j][rawData[0].length-1])[0]][i];
            }
            perceptrons[i] = new PerceptronAlgorithm(rawDataCollection[i], learningRate, iterateTimes, "Perceptron " + (i+1), test, enableLog, seed);
        }
    }

    public void initialize() {
        for (int i = 0; i < perceptrons.length; i++) {
            perceptrons[i].initialize();
        }
    }

    public double[][] calculate() {
        double[][] ws = new double[perceptrons.length][];
        for (int i = 0; i < perceptrons.length; i++) {
            ws[i] = perceptrons[i].calculate();
        }
        return ws;
    }

    public PerceptronAlgorithm getPerceptron(int index) {
        return perceptrons[index];
    }

    public PerceptronAlgorithm[] getPerceptrons() {
        return perceptrons;
    }

    public double[] getThreshold() {
        double[] thresholds = new double[perceptrons.length];
        for (int i = 0; i < perceptrons.length; i++) {
            thresholds[i] = perceptrons[i].getThreshold();
        }
        return thresholds;
    }

    public double validate() {
        int count = 0;
        for (int i = 0; i < perceptrons[0].rawSize(); i++) {
            boolean success = true;
            for (int j = 0; j < perceptrons.length; j++) {
                if (!perceptrons[j].validateOne(i)) {
                    success = false;
                    break;
                }
            }
            if (success) {
                count++;
            }
        }
        return (double) count / (double) perceptrons[0].rawSize();
    }

    public double testValidate() {
        if (enableLog) {
            log("=====  Start Testing=====");
        }
        if (!test) {
            return -1;
        }
        int count = 0;
        for (int i = 0; i < perceptrons[0].testSize(); i++) {
            boolean success = true;
            StringBuffer msg = new StringBuffer();
            if (enableLog) {
                double[] datum = perceptrons[0].getTestDatum(i);
                msg.append("dots: (" + String.format("%.3f", datum[0]));
                for (int j = 1; j < datum.length-1; j++) {
                    msg.append(", " + String.format("%.3f", datum[1]));
                }
                msg.append("), status: ");
            }
            for (int j = 0; j < perceptrons.length; j++) {
                if (!perceptrons[j].testValidateOne(i)) {
                    success = false;
                    break;
                }
            }
            if (success) {
                if (enableLog) {
                    msg.append("true");
                }
                count++;
            } else if (enableLog){
                msg.append("false");
            }
            if (enableLog) {
                log(msg.toString());
            }
        }
        if (enableLog) {
            log("=====  End Testing=====");
        }
        return (double) count / (double) perceptrons[0].testSize();
    }

    public String getLog() {
        String logs = "";
        for (PerceptronAlgorithm perceptron: perceptrons) {
            logs += perceptron.getLog() + "\n";
        }
        return logs + log;
    }

    private void log(String msg) {
        log.append("<Perceptrons> " + msg + "\n");
    }
}
