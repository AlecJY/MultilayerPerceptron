package com.alebit.perceptron.mlp;

import java.util.Random;

/**
 * Created by Alec on 2016/11/19.
 */
public class HiddenLayer {
    public Neuron[] neurons;
    private HiddenLayer childHiddenLayer;

    public HiddenLayer(int unitNum, int dim) {
        neurons = new Neuron[unitNum];
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(dim);
        }
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public void nextHiddenLayer() {
        if (childHiddenLayer != null) {
            double[][] ys = new double[1][neurons.length + 1];
            ys[0][0] = -1;

            // TODO finish ys

            InputLayer inputLayer = new InputLayer(ys);
            inputLayer.learning(childHiddenLayer);
        }
    }

    public class Neuron {
        double[] w;
        double y;
        public Neuron(int dim) {
            w = new double[dim];
            Random random = new Random();
            for (int i = 0; i < w.length; i++) {
                w[i] = random.nextDouble() + random.nextInt(11) + 0.00001;
            }
        }

        public void training(double[] trainingData) {
            double expt = w[0] * -1;
            for (int i = 1; i < w.length; i++) {
                expt += w[i]*trainingData[i-1];
            }
            y = 1 / (1 + Math.exp(expt*-1));
        }
    }
}
