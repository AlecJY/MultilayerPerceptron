package com.alebit.perceptron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Alec on 2016/10/9.
 */
public class InputForm extends JFrame {
    private JButton calculateButton;
    private JPanel graphicPanel;
    private JTextField pathField;
    private JButton openButton;
    private JSpinner iterateSpinner;
    private JPanel mainPanel;
    private JSpinner learningSpinner;
    private JSpinner thresholdSpinner;

    private JFrame frame = this;

    public InputForm() {
        setTitle("Perceptron Calculator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getRootPane().setDefaultButton(calculateButton);
        add(mainPanel);
        pack();


        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(".");
                int status = fileChooser.showOpenDialog(null);
                if (status == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    pathField.setText(file.getPath());
                }
            }
        });
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double[][] num = InputFileParser.parse(pathField.getText());
                    PerceptronAlgorithm perceptron = new PerceptronAlgorithm(num, (double) learningSpinner.getValue(), (int) iterateSpinner.getValue(), (double) thresholdSpinner.getValue());
                    perceptron.initialize();
                    perceptron.calculate();
                    PlotPainter plotPainter = new PlotPainter(graphicPanel, frame);
                    plotPainter.setRawData(num);
                    plotPainter.paint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void createUIComponents() {
        learningSpinner = new JSpinner(new SpinnerNumberModel(0, -1, 1, 0.01));
        thresholdSpinner = new JSpinner(new SpinnerNumberModel(0, -10000, 10000, 0.01));
        iterateSpinner = new JSpinner();
        graphicPanel = new JPanel();
    }

}