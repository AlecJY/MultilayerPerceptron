package com.alebit.perceptron;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;

/**
 * Created by Alec on 2016/10/9.
 */
public class InputFileParser {
    public static double[][] parse(String path) {
        double[][] num;
        ArrayList<double[]> numLists = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            boolean lastLine = false;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] numStrs = line.trim().split("\\s+");
                if (numStrs.length > 1) {
                    if (lastLine) {
                        throw new Exception();
                    }
                    double[] nums = new double[numStrs.length];
                    for (int i = 0; i < numStrs.length; i++) {
                        nums[i] = Double.parseDouble(numStrs[i]);
                    }
                    numLists.add(nums);
                } else {
                    lastLine = true;
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (numLists.isEmpty()) {
            throw new RuntimeException();
        }
        num = new double[numLists.size()][numLists.get(0).length];
        for (int i = 0; i < numLists.size(); i++) {
            if (numLists.get(i).length == numLists.get(0).length) {
                for (int j = 0; j < numLists.get(i).length; j++) {
                    num[i][j] = numLists.get(i)[j];
                }
            } else {
                throw new RuntimeException();
            }
        }
        return num;
    }
}
