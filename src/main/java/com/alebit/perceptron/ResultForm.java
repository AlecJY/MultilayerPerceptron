package com.alebit.perceptron;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Alec on 2016/10/16.
 */
public class ResultForm {
    private JPanel graphPanel;
    private JPanel resultPanel;
    private JPanel detailsPanel;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTextArea logArea;
    private JLabel testLabel;
    private JTextPane tRatePane;
    private JTextPane testRatePane;
    private JTextPane weightsPane;
    private JFrame frame;

    public ResultForm(JFrame frame) {
        this.frame = frame;
        detailsPanel.setPreferredSize(new Dimension(-1, -1));
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() != 0) {
                    detailsPanel.setPreferredSize(new Dimension(-1, 500));
                } else {
                    detailsPanel.setPreferredSize(new Dimension(-1, -1));
                }
                frame.pack();
                frame.revalidate();
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getGraphPanel() {
        return graphPanel;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JTextArea getLogArea() {
        return logArea;
    }

    public void setFieldValue(double[] threshold, double[][] w, double tRate, double testRate) {
        String weightsStr = "<html>";
        String[] colors = new String[]{"red", "blue", "black", "green", "cyan"};
        for (int i = 0; i < threshold.length; i++) {
            weightsStr = weightsStr.concat("<font color=\"" + colors[i % colors.length] + "\">(" + String.format("%.3f", threshold[i]));
            for (double wValue : w[i]) {
                weightsStr = weightsStr.concat(", " + String.format("%.3f", wValue));
            }
            weightsStr = weightsStr.concat(")</font>");
            if (i != threshold.length - 1) {
                weightsStr = weightsStr.concat("<br/>");
            }
        }
        weightsStr = weightsStr.concat("</html>");
        weightsPane.setEditable(false);
        weightsPane.setContentType("text/html");
        weightsPane.setText(weightsStr);
        weightsPane.setPreferredSize(new Dimension(0, -1));
        tRatePane.setText(String.format("%.2f", tRate * 100) + " %");
        if (testRate == -1) {
            testRatePane.setVisible(false);
            testLabel.setVisible(false);
        } else {
            testRatePane.setText(String.format("%.2f", testRate * 100) + " %");
        }
    }

}
