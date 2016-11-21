package com.alebit.perceptron;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

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
    private JCheckBox testCheckBox;
    private JCheckBox detailsCheckBox;
    private JSpinner hidLayerSpinner;
    private JSpinner hidUnitSpinner;
    private JSpinner outUnitSpinner;
    private JPanel outPanel;

    private JFrame frame = this;

    public InputForm() {
        setTitle("Multilayer Perceptron");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getRootPane().setDefaultButton(calculateButton);
        add(mainPanel);
        outPanel.setVisible(false);
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
                    PerceptronAlgorithm perceptron = new PerceptronAlgorithm(num, (double) learningSpinner.getValue(), (int) iterateSpinner.getValue(), "Perceptron", testCheckBox.isSelected(), !detailsCheckBox.isSelected(), System.currentTimeMillis());
                    perceptron.initialize();
                    perceptron.calculate((int) hidLayerSpinner.getValue(), (int) hidUnitSpinner.getValue(), (int) outUnitSpinner.getValue());
                    ResultForm resultForm = new ResultForm(frame);
                    resultForm.setFieldValue(perceptron.getW(), perceptron.validate(), perceptron.testValidate());
                    resultForm.getLogArea().setText(perceptron.getLog());
                    resultPanel.removeAll();
                    resultPanel.setLayout(new GridLayout());
                    /* PlotPainter plotPainter = new PlotPainter(resultForm.getGraphPanel(), frame);
                    plotPainter.setRawData(num);
                    // plotPainter.setPerceptron(perceptron.getPerceptrons());
                    if (!plotPainter.paint()) {
                        resultForm.getTabbedPane().setEnabledAt(2, false);
                    } */
                    if (detailsCheckBox.isSelected()) {
                        resultForm.getTabbedPane().setEnabledAt(1, false);
                    }
                    resultPanel.add(resultForm.getMainPanel());
                    frame.pack();
                    frame.revalidate();
                    Thread revalidateThread = new Thread(new RevalidateForm());
                    revalidateThread.start();
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
        hidLayerSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        hidUnitSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        outUnitSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    }

    private class RevalidateForm implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame.pack();
            frame.revalidate();
        }
    }

}