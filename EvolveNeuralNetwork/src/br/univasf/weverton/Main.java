package br.univasf.weverton;

public class Main {

	public static void main(String[] args) {
	NeuralNetwork brain = new NeuralNetwork(2, 2, 1);
	float[] input = {1,0};
	float[] target = {1};
	brain.train(input, target);
	}
}