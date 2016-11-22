package com.alebit.perceptron;

import com.intellij.uiDesigner.core.GridConstraints;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Alec on 2016/10/16.
 */
public class ResultForm {
    private JPanel resultPanel;
    private JPanel detailsPanel;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTextArea logArea;
    private JButton recognizeButton;
    private JPanel numberButtonPane;
    private JTextPane tRatePane;
    private JFrame frame;
    private JButton[][] numberButtons = new JButton[5][5];

    public ResultForm(JFrame frame, PerceptronAlgorithm perceptron) {
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
        numberButtonPane.setLayout(new GridBagLayout());
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                numberButtons[i][j] = new JButton();
                numberButtons[i][j].setMinimumSize(new Dimension(80, 80));
                numberButtons[i][j].setPreferredSize(new Dimension(80, 80));
                numberButtons[i][j].setMaximumSize(new Dimension(80, 80));
                numberButtons[i][j].setBackground(Color.WHITE);
                numberButtons[i][j].setOpaque(true);
                numberButtons[i][j].setBorderPainted(false);
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = i;
                c.gridy = j;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.weightx = 1;
                c.weighty = 1;
                c.fill = GridConstraints.FILL_BOTH;
                c.ipadx = 0;
                c.ipady = 0;
                numberButtonPane.add(numberButtons[i][j], c);
                numberButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton button = (JButton) e.getSource();
                        if (!button.isSelected()) {
                            button.setBackground(Color.BLACK);
                            button.setSelected(true);
                        } else {
                            button.setBackground(Color.WHITE);
                            button.setSelected(false);
                        }
                    }
                });
            }
        }
        recognizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double[] testData = new double[25];
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (numberButtons[i][j].isSelected()) {
                            testData[i + j * 5] = 1;
                        } else {
                            testData[i + j * 5] = 0;
                        }
                    }
                }
                double[] ys = perceptron.test(testData);
                String msgStr = "<html>0: " + String.format("%.2f", ys[0] * 100) + " %";
                for (int i = 1; i < 10; i++) {
                    msgStr = msgStr.concat("<br/>" + i + ": " + String.format("%.2f", ys[i] * 100) + " %");
                }
                msgStr = msgStr.concat("</html>");
                JOptionPane.showMessageDialog(null, msgStr);
            }
        });
    }

    public void setFieldValue(double tRate) {
        tRatePane.setText(String.format("%.2f", tRate * 100) + " %");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JTextArea getLogArea() {
        return logArea;
    }

}
