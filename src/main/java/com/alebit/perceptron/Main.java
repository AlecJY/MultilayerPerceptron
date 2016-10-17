package com.alebit.perceptron;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

import javax.swing.*;
import java.awt.*;

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
            UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        new Main();
    }
}
