package com.alebit.perceptron;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

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

    public void setFieldValue(ArrayList<double[][]> w, double tRate, double testRate) {
        String weightsStr = "<html>";
        String[] colors = new String[]{"red", "blue", "black", "green", "cyan"};
        for (int i = 0; i < w.size() - 1; i++) {
            weightsStr = weightsStr.concat("<font color=\"" + colors[i % colors.length] + "\">" + "&lt;HiddenLayer " + (i + 1) + "&gt;");
            for (double[] wn : w.get(i)) {
                weightsStr = weightsStr.concat("<br/>(" + wn[wn.length - 1]);
                for (int j = 0; j < wn.length - 1; j++) {
                    weightsStr = weightsStr.concat(", " + wn[j]);
                }
                weightsStr = weightsStr.concat(")");
            }
            weightsStr = weightsStr.concat("</font><br/>");
        }
        weightsStr = weightsStr.concat("<font color=\"" + colors[w.size() - 1 % colors.length] + "\">" + "&lt;OutputLayer&gt;");
        for (double[] wn : w.get(w.size() - 1)) {
            weightsStr = weightsStr.concat("<br/>(" + wn[wn.length - 1]);
            for (int j = 0; j < wn.length - 1; j++) {
                weightsStr = weightsStr.concat(", " + wn[j]);
            }
            weightsStr = weightsStr.concat(")</font>");
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayoutManager(3, 2, new Insets(20, 20, 20, 20), -1, -1));
        tabbedPane.addTab("   Result   ", resultPanel);
        final JLabel label1 = new JLabel();
        label1.setText("Training Recognition Rate");
        resultPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        testLabel = new JLabel();
        testLabel.setText("Test Recognition Rate");
        resultPanel.add(testLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Weights");
        resultPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tRatePane = new JTextPane();
        tRatePane.setEditable(false);
        resultPanel.add(tRatePane, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        testRatePane = new JTextPane();
        testRatePane.setEditable(false);
        resultPanel.add(testRatePane, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        scrollPane1.setVerticalScrollBarPolicy(22);
        resultPanel.add(scrollPane1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 150), new Dimension(-1, 150), new Dimension(-1, 150), 0, false));
        weightsPane = new JTextPane();
        weightsPane.setText("");
        scrollPane1.setViewportView(weightsPane);
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("   Details   ", detailsPanel);
        final JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setHorizontalScrollBarPolicy(30);
        scrollPane2.setVerticalScrollBarPolicy(22);
        detailsPanel.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setEnabled(false);
        scrollPane2.setViewportView(logArea);
        graphPanel = new JPanel();
        graphPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("   Graph   ", graphPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
