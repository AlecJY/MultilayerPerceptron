package com.alebit.perceptron.mlp;

/**
 * Created by Alec on 2016/11/20.
 */
public class OutputLayer extends HiddenLayer {

    public OutputLayer(int unitNum, int dim) {
        super(unitNum, dim, null);
    }

    protected void initializeNeurons(int unitNum, int dim) {
        neurons = new Neuron[unitNum];
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(dim);
        }
    }

    public void nextHiddenLayer() {
        backPropagation();
    }

    public void testNextHiddenLayer() {
        ys = new double[neurons.length + 1];
        ys[ys.length - 1] = expOut;

        for (int i = 0; i < neurons.length; i++) {
            ys[i] = neurons[i].getY();
        }
    }

    public boolean isOutputLayer() {
        return true;
    }

    public class Neuron extends HiddenLayer.Neuron {
        public Neuron(int dim) {
            super(dim);
        }

        public void getDelta(int index) {
            delta = (expOut - y) * y * (1 - y);
            // System.out.println(y + "," + expOut + ", " + delta);
            setPy();
        }
    }
}
