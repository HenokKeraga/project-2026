package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleNeuralNetworkTest {

    @BeforeEach
    void resetModel() {
        SimpleNeuralNetwork.weights = new double[]{0.5, -1.2, 0.8};
        SimpleNeuralNetwork.bias = 0.1;
        SimpleNeuralNetwork.learningRate = 0.1;
    }

    @Test
    void forwardPassReturnsExpectedValueForKnownParameters() {
        double[] input = {0.9, 0.1, 0.4};
        double sum = SimpleNeuralNetwork.bias;
        for (int i = 0; i < input.length; i++) {
            sum += input[i] * SimpleNeuralNetwork.weights[i];
        }
        double expected = 1.0 / (1.0 + Math.exp(-sum));
        double actual = SimpleNeuralNetwork.neuralNetwork(input);
        assertEquals(expected, actual, 1e-12);
    }

    @Test
    void sigmoidProducesValuesInZeroOneRangeAndIsMonotonic() {
        double low = SimpleNeuralNetwork.sigmoid(-10);
        double mid = SimpleNeuralNetwork.sigmoid(0);
        double high = SimpleNeuralNetwork.sigmoid(10);

        assertTrue(low > 0.0 && low < 1.0);
        assertTrue(mid > 0.0 && mid < 1.0);
        assertTrue(high > 0.0 && high < 1.0);

        assertTrue(low < mid && mid < high);
    }

    @Test
    void singleTrainStepAdjustsWeightsAndBiasInDirectionOfReducingError() {
        double[] input = {0.9, 0.1, 0.4};
        double target = 1.0;

        double beforePred = SimpleNeuralNetwork.neuralNetwork(input);
        double[] beforeWeights = SimpleNeuralNetwork.weights.clone();
        double beforeBias = SimpleNeuralNetwork.bias;

        SimpleNeuralNetwork.train(input, target);

        double afterPred = SimpleNeuralNetwork.neuralNetwork(input);
        double[] afterWeights = SimpleNeuralNetwork.weights;
        double afterBias = SimpleNeuralNetwork.bias;

        // Prediction should move closer to target
        assertTrue(Math.abs(afterPred - target) < Math.abs(beforePred - target));

        // Bias should have changed by learningRate * (prediction - target) in opposite sign
        assertNotEquals(beforeBias, afterBias);

        // Weight updates should not be NaN and should reflect change
        for (int i = 0; i < beforeWeights.length; i++) {
            assertFalse(Double.isNaN(afterWeights[i]));
            assertNotEquals(beforeWeights[i], afterWeights[i]);
        }
    }

    @Test
    void trainingLoopReducesErrorAcrossMultipleEpochs() {
        double[][] inputs = {
                {0.9, 0.1, 0.4},
                {0.1, 0.8, 0.2}
        };
        double[] targets = {1.0, 0.0};

        double initialError = 0.0;
        for (int i = 0; i < inputs.length; i++) {
            double p = SimpleNeuralNetwork.neuralNetwork(inputs[i]);
            initialError += Math.pow(p - targets[i], 2);
        }

        for (int epoch = 0; epoch < 1000; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                SimpleNeuralNetwork.train(inputs[i], targets[i]);
            }
        }

        double finalError = 0.0;
        for (int i = 0; i < inputs.length; i++) {
            double p = SimpleNeuralNetwork.neuralNetwork(inputs[i]);
            finalError += Math.pow(p - targets[i], 2);
        }

        assertTrue(finalError < initialError);
    }

    @Test
    void neuralNetworkHandlesLargeInputsApproachingOne() {
        double[] largePositive = {100.0, 100.0, 100.0};
        double pred = SimpleNeuralNetwork.neuralNetwork(largePositive);
        assertTrue(pred > 0.999);
    }

}

