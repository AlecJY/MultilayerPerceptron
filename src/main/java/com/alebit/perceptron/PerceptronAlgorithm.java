package com.alebit.perceptron;

import com.sun.org.apache.regexp.internal.RE;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

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
    private OneDMatrix w;
    private String name;
    private StringBuffer log = new StringBuffer();
    private static final int GREEN = 1;
    private static final int RED = 0;
    private boolean enableLog = true;
    private boolean enableHighlight = false;
    private JTextPane logPane;

    public PerceptronAlgorithm(double[][] rawData, double learningRate, int iterateTimes, String name) {
        this.rawData = rawData;
        this.learningRate = learningRate;
        this.iterateTimes = iterateTimes;
        this.name = name;
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        logPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(logPane);
        frame.setLayout(new GridLayout());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logPane.setText("");
        frame.setPreferredSize(new Dimension(200, 500));
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }

    public void initialize() {
        /* int num = rawData.length;
        int learningNum = (int) Math.ceil(num * 2 / 3);
        int testNum = num - learningNum;
        learningData = new double[learningNum][];
        testData = new double[testNum][];
        System.arraycopy(rawData, 0, learningData, 0, learningNum);
        System.arraycopy(rawData, learningNum, testData, 0, testNum); */
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
            log("====== " + name + " Start Calculate =====");
            log("Classification 1: " + (int) classification[0] + "\t Classification 2: " + (int) classification[1]);
        }
    }

    public double[] calculate() {
        OneDMatrix w = new OneDMatrix(rawData[0].length - 1);
        for (int i = 0; i < w.size(); i++) {
            w.set(i, 0);
        }
        w = training(w);
        this.w = w;
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
                    msg.append("Dot: (" + String.format("%.3f", data[0]));
                    for (int j = 1; j < data.length - 1; j++) {
                        msg.append(", " + String.format("%.3f", data[j]));
                    }
                    msg.append("), w: (" + String.format("%.3f", threshold));
                    for (int j = 0; j < w.size(); j++) {
                        msg.append( ", " + String.format("%.3f", w.toArray()[j]));
                    }
                    msg.append("), predict: " + (long) data[data.length - 1] + ", actual: " + (long) out(vector, w));
                    if (e == 0) {
                        log(msg.toString(), GREEN);
                    } else {
                        log(msg.toString(), RED);
                    }
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

    private void log(String msg) {
        try {
            logPane.getStyledDocument().insertString(logPane.getStyledDocument().getLength(), msg + "\n", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // log.append("&lt; " + name +" &gt; " + msg + "<br/>");
    }
    private void log(String msg, int colorID) {
        Color color;
        if (colorID == RED) {
            color = Color.RED;
        } else if (colorID == GREEN) {
            color = Color.RED;
        }else {
            log(msg);
            return;
        }
        // log.append("&lt; " + name +" &gt; "  + "<font color=\"" + colorStr + "\">" + msg + "</font>" + "<br/>");
        try {
            SimpleAttributeSet textAttr = new SimpleAttributeSet();
            StyleConstants.setForeground(textAttr, color);
            logPane.getStyledDocument().insertString(logPane.getStyledDocument().getLength(), msg + "\n", textAttr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLog() {
        System.out.println("Logged");
        return log.toString();
    }
}
