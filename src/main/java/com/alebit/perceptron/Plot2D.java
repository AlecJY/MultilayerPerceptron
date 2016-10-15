package com.alebit.perceptron;

import org.math.plot.canvas.Plot2DCanvas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Alec on 2016/10/14.
 */
public class Plot2D {
    protected JPanel graphPanel;
    protected JFrame frame;
    protected ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLACK, Color.GREEN, Color.CYAN));

    public Plot2D(JPanel graphPanel, JFrame frame) {
        this.graphPanel = graphPanel;
        this.frame = frame;
    }

    public void drawPlot(HashMap<Double, ArrayList<double[]>> dotData, PerceptronAlgorithm perceptron) {
        Plot2DCanvas plot2DCanvas = new Plot2DCanvas();
        plot2DCanvas.addLinePlot("w", colors.get(0), new double[][]{new double[]{-1,perceptron.getY(-1)}, new double[]{1, perceptron.getY(1)}});
        int colorCount = 0;
        for (double key: dotData.keySet()) {
            plot2DCanvas.addScatterPlot("Group " + (int) key, colors.get(colorCount%colors.size()), arrayListToArray(dotData.get(key)));
            colorCount++;
        }

        graphPanel.setLayout(new GridLayout());
        graphPanel.add(plot2DCanvas);
        graphPanel.setPreferredSize(new Dimension(frame.getWidth() - 80, 500));
        frame.pack();
        frame.revalidate();
    }

    protected double[][] arrayListToArray(ArrayList<double[]> arrayList) {
        double[][] array = new double[arrayList.size()][];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    protected void addLine(PerceptronAlgorithm perceptron) {

    }
}
