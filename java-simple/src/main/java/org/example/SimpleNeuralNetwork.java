package org.example;


public class SimpleNeuralNetwork {

    // MODEL: parameters (weights + bias)
    static double[] weights = {0.5, -1.2, 0.8};
    static double bias = 0.1;

    static double learningRate = 0.1;

    // MODEL (forward pass)
    static double neuralNetwork(double[] input) {
        double sum = bias;
        for (int i = 0; i < input.length; i++) {
            sum += input[i] * weights[i];
        }
        return sigmoid(sum);
    }

    // ACTIVATION
    static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    // ALGORITHM: training (gradient descent)
    static void train(double[] input, double target) {

        double prediction = neuralNetwork(input);
        double error = prediction - target;

        // Update weights
        for (int i = 0; i < weights.length; i++) {
            weights[i] -= learningRate * error * input[i];
        }

        // Update bias
        bias -= learningRate * error;
    }
   //TODO (copilot): write unit test using junit 5 and mockito to test the train method
    public static void main(String[] args) {

        // Training data
        double[][] inputs = {
                {0.9, 0.1, 0.4},
                {0.1, 0.8, 0.2}
        };

        double[] targets = {1.0, 0.0}; // correct answers

        // TRAINING LOOP (algorithm running)
        for (int epoch = 0; epoch < 1000; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                train(inputs[i], targets[i]);
            }
        }

        // Prediction after training
        double[] testInput = {0.9, 0.1, 0.4};
        double prediction = neuralNetwork(testInput);

        System.out.println("Prediction after training: " + prediction);
    }
}

