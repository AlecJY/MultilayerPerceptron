package com.alebit.perceptron.mlp;

import com.alebit.perceptron.OneDMatrix;

import java.util.Random;

/**
 * Created by Alec on 2016/11/19.
 */
public class HiddenLayer {
    protected Neuron[] neurons;
    protected HiddenLayer childHiddenLayer;
    protected HiddenLayer parentHiddenLayer;
    protected double expOut;
    protected double learningRate;
    protected double[] ys;

    public HiddenLayer(int unitNum, int dim, HiddenLayer childHiddenLayerLayer) {
        initializeNeurons(unitNum, dim);
        this.childHiddenLayer = childHiddenLayerLayer;
    }

    protected void initializeNeurons(int unitNum, int dim) {
        neurons = new Neuron[unitNum];
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(dim);
        }
    }

    public void setParentHiddenLayer(HiddenLayer parentHiddenLayer) {
        this.parentHiddenLayer = parentHiddenLayer;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public void nextHiddenLayer() {
        double[][] ysi = new double[1][];
        ys = new double[neurons.length + 1];
        ys[ys.length - 1] = expOut;

        for (int i = 0; i < neurons.length; i++) {
            ys[i] = neurons[i].getY();
        }
        ysi[0] = ys;
        childHiddenLayer.setLearningRate(learningRate);
        InputLayer inputLayer = new InputLayer(ysi);
        inputLayer.learning(childHiddenLayer);
    }

    public void backPropagation() {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].getDelta(i);
        }
        if (parentHiddenLayer != null) {
            parentHiddenLayer.backPropagation();
        }
        for (Neuron neuron: neurons) {
            neuron.reviseW();
        }
    }

    public void setExpOut(double expOut) {
        this.expOut = expOut;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public class Neuron {
        protected OneDMatrix w;
        protected double y;
        protected double delta;
        public Neuron(int dim) {
            w = new OneDMatrix(dim);
            Random random = new Random();
            for (int i = 0; i < w.size(); i++) {
                w.set(i ,random.nextDouble() + random.nextInt(11) + 0.00001);
            }
        }

        public void setW(OneDMatrix w) {
            this.w = w;
        }

        public void training(double[] trainingData) {
            double expt = w.get(w.size() - 1) * -1;
            for (int i = 0; i < w.size() - 1; i++) {
                expt += w.get(i)*trainingData[i];
            }
            y = 1 / (1 + Math.exp(expt*-1));
        }

        public void getDelta(int index) {
            delta = y * (1 - y);
            double sum = 0;
            for (int i = 0; i < childHiddenLayer.neurons.length; i++) {
                sum += childHiddenLayer.neurons[i].delta * childHiddenLayer.neurons[i].w.get(index);
            }
            delta *= sum;
        }

        public void reviseW() {
            OneDMatrix py;
            py = new OneDMatrix(w.size());
            if (parentHiddenLayer == null) {
                for (int i = 0; i < py.size() - 1; i++) {
                    py.set(i, 1);
                }
            } else {
                double[] pys = parentHiddenLayer.ys;
                for (int i = 0; i < pys.length - 1; i++) {
                    py.set(i, pys[i]);
                }
            }
            py.set(py.size() - 1, -1);
            w = w.add(py.multiply(learningRate *delta));
        }

        public double getY() {
            return y;
        }
    }
}
