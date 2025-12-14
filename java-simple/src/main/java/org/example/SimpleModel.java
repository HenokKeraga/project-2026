package org.example;

public class SimpleModel {

    // These numbers ARE the model (learned weights)
    static double[] weights = {1.5, -2.0, 0.7};
    static double bias = -0.4;

    // Predict method
    public static double predict(double[] input) {
        double sum = bias;
        for (int i = 0; i < input.length; i++) {
            sum += input[i] * weights[i];
        }
        return sigmoid(sum);
    }

    static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public static void main(String[] args) {
        // Vectorized input (e.g., "free money")
        double[] messageVector = {0.8, 0.9, 0.1};

        double output = predict(messageVector);

        if (output > 0.5) {
            System.out.println("Spam");
        } else {
            System.out.println("Not Spam");
        }
    }
}

