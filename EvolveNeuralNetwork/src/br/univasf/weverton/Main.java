package br.univasf.weverton;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		long initTime = System.currentTimeMillis();
		
		for(int j=0;j<2000;j++) {
		
		Random r = new Random();
		float[] in = {3,5};
		int[] setup = {2,2,1};
		Population population = new Population(4, setup);
		
		for(int i=0; i<4; i++) {
			float[] saida = population.getBrain(i).feedFoward(in);
			float s = saida[0];
			if(s > 0.5)
			System.out.println("Pule!");
			else
				System.out.println("Não pule!");
		}
		System.out.println(" ");
		
		for(int i=0; i<4; i++) {
			population.getBrain(i).addFitness(r.nextInt(100));
		}
		
		population.selection();
		population.breed();
		
		
		}
		long finalTime = System.currentTimeMillis() - initTime;
		System.out.println("Tempo de execução: " + finalTime);
		System.out.println(" ");
	}
}