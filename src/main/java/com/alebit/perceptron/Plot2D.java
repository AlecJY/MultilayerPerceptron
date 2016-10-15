package com.alebit.perceptron;

import org.math.plot.FrameView;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Alec on 2016/10/14.
 */
public class Plot2D {
    private JPanel graphPanel;
    private JFrame frame;
    private ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLACK, Color.GREEN, Color.CYAN));

    public Plot2D(JPanel graphPanel, JFrame frame) {
        this.graphPanel = graphPanel;
        this.frame = frame;
    }

    public void drawPlot(HashMap<Double, ArrayList<double[]>> dotData) {
        Plot2DPanel plot2DPanel = new Plot2DPanel();
        int colorCount = 0;
        for (double key: dotData.keySet()) {
            plot2DPanel.addScatterPlot("Group " + (int) key, colors.get(colorCount%colors.size()), arrayListToArray(dotData.get(key)));
            colorCount++;
        }

        graphPanel.setLayout(new GridLayout());
        graphPanel.add(plot2DPanel);
        graphPanel.setPreferredSize(new Dimension(frame.getWidth() - 80, 500));
        frame.pack();
        frame.revalidate();
    }

    private double[][] arrayListToArray(ArrayList<double[]> arrayList) {
        double[][] array = new double[arrayList.size()][];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }
}
