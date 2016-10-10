package com.alebit.perceptron;

import javax.swing.*;

/**
 * Created by Alec on 2016/10/9.
 */
public class Main {
    public Main() {
        JFrame inputFrame = new InputForm();
        inputFrame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Main();
    }
}
