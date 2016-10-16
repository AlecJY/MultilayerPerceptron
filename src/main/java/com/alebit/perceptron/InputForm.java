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
    private JPanel resultPanel;
    private JTextField pathField;
    private JButton openButton;
    private JSpinner iterateSpinner;
    private JPanel mainPanel;
    private JSpinner learningSpinner;

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
                    PerceptronAlgorithm perceptron = new PerceptronAlgorithm(num, (double) learningSpinner.getValue(), (int) iterateSpinner.getValue(), "Perceptron 1");
                    perceptron.initialize();
                    double[] w = perceptron.calculate();
                    ResultForm resultForm = new ResultForm(frame);
                    resultForm.setFieldValue(perceptron.getThreshold(), w, perceptron.validate(), 0);
                    resultForm.getLogArea().setText("<html>" + perceptron.getLog() + "</html>");
                    resultPanel.removeAll();
                    resultPanel.setLayout(new GridLayout());
                    resultPanel.add(resultForm.getMainPanel());
                    PlotPainter plotPainter = new PlotPainter(resultForm.getGraphPanel(), frame);
                    plotPainter.setRawData(num);
                    plotPainter.setPerceptron(perceptron);
                    if (!plotPainter.paint()) {
                        resultForm.getTabbedPane().setEnabledAt(2, false);
                    }
                    frame.pack();
                    frame.revalidate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void createUIComponents() {
        learningSpinner = new JSpinner(new SpinnerNumberModel(0, -1, 1, 0.01));
        iterateSpinner = new JSpinner();
        resultPanel = new JPanel();
    }

}