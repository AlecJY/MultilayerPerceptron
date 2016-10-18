package com.alebit.perceptron;

import org.math.plot.canvas.Plot3DCanvas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alec on 2016/10/15.
 */
public class Plot3D extends Plot2D {
    public Plot3D(JPanel graphPanel, JFrame frame) {
        super(graphPanel, frame);
    }

    public void drawPlot(HashMap<Double, ArrayList<double[]>> dotData) {
        Plot3DCanvas plot3DCanvas = new Plot3DCanvas();
        int colorCount = 0;
        for (double key: dotData.keySet()) {
            plot3DCanvas.addScatterPlot("Group " + (int) key, colors.get(colorCount%colors.size()), arrayListToArray(dotData.get(key)));
            colorCount++;
        }

        graphPanel.setLayout(new GridLayout());
        graphPanel.add(plot3DCanvas);
        frame.pack();
        frame.revalidate();
    }
}
