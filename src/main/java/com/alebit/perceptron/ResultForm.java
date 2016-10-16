package com.alebit.perceptron;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Alec on 2016/10/16.
 */
public class ResultForm {
    private JPanel graphPanel;
    private JPanel resultPanel;
    private JPanel detailsPanel;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTextField weightsField;
    private JTextField tRatefield;
    private JTextField testRateField;
    private JTextArea logArea;
    private JLabel testLabel;
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

    public void setFieldValue(double threshold, double[] w, double tRate, double testRate) {
        String weightsStr = "(" + String.format("%.3f", threshold);
        for (double wValue: w) {
            weightsStr = weightsStr.concat(", " + String.format("%.3f", wValue));
        }
        weightsStr = weightsStr.concat(")");
        weightsField.setText(weightsStr);
        tRatefield.setText(String.format("%.2f", tRate * 100) + " %");
        if (testRate == -1) {
            testRateField.setVisible(false);
            testLabel.setVisible(false);
        } else {
            testRateField.setText(String.format("%.2f", testRate * 100) + " %");
        }
    }

    private void createUIComponents() {
        logArea = new JTextArea();
    }
}
