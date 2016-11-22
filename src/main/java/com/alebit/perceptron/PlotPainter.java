package com.alebit.perceptron;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alec on 2016/10/15.
 */
public class PlotPainter {
    private JPanel graphPanel;
    private JFrame frame;
    private HashMap<Double, ArrayList<double[]>> dotData = new HashMap();
    private int dimension;
    private PerceptronAlgorithm perceptron;
    private double[][] rawData;

    public PlotPainter(JPanel graphPanel, JFrame frame) {
        this.graphPanel = graphPanel;
        this.frame = frame;
    }

    public void setRawData(double[][] data) {
        rawData = data;
        dimension = data[0].length-1;
        for (int i = 0; i < data.length; i++) {
            double dataType = data[i][dimension];
            double[] dots = new double[dimension];
            for (int j = 0; j < dots.length; j++) {
                dots[j] = data[i][j];
            }
            if (dotData.containsKey(dataType)) {
                dotData.get(dataType).add(dots);
            } else {
                ArrayList<double[]> dotsArray = new ArrayList<>();
                dotsArray.add(dots);
                dotData.put(dataType, dotsArray);
            }
        }

    }

    public void setPerceptron(PerceptronAlgorithm perceptron) {
        this.perceptron = perceptron;
    }

    public boolean paint() {
        graphPanel.removeAll();
        if (dimension == 2) {
            Plot2D plot2D = new Plot2D(graphPanel, frame);

            // plot2D.addLine(perceptron, rawData);

            plot2D.drawPlot(dotData);
        } else if (dimension == 3) {
            Plot3D plot3D = new Plot3D(graphPanel, frame);
            plot3D.drawPlot(dotData);
        } else {
            return false;
        }
        return true;
    }
}
