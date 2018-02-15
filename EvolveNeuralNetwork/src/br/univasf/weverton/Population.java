package br.univasf.weverton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population {
	
	public int[] settings;
	public int size;
	public ArrayList<NeuralNetwork> population;
	private ArrayList<NeuralNetwork> pool;
	
	public Population(int size, int[] setup){
		
		this.settings = setup.clone();
		this.size = size;
		this.population = new ArrayList<NeuralNetwork>();
		this.pool = new ArrayList<NeuralNetwork>();
		
		for(int i=0; i<this.size; i++)
			population.add(new NeuralNetwork(this.settings[0],this.settings[1],this.settings[2]));
	}
	
	public NeuralNetwork getBrain(int index) {
		return this.population.get(index);
	}
	
	public void sortPopulation(){
		Collections.sort(this.population);
	}
	
	public void selection(){ //consertar essa função
		
		Random rand = new Random();
		int[] used = {-1,-1,-1,-1};
		int index;
		
		//limpa o array de nets selecionadas anteriormente
		this.pool.clear();
		//ordena a populacao em ordem crescente com bse no fitness
		sortPopulation();
		//salvando 50% das melhores redes
		for(int i = 0; i < size/2; i++){
			this.pool.add(new NeuralNetwork(this.population.get(i+size/2)));
			this.pool.get(i).mutate();
			}
		//seleciona randomicamente 25% das piores redes
		for(int i=0; i < size/4; i++){
			this.pool.add(new NeuralNetwork(this.population.get(i)));
			this.pool.get(size/2 + i).mutate();
		}
		System.out.println("Selection succefull!");
	}
	
	public void breed(){
		
		//calcula quantidade de filhos para preencher a pool
		int popSize = this.population.size();
		int poolSize = this.pool.size();
		int remaining = popSize - poolSize;
		Random rand = new Random();
		int iMom;
		int iDad;
		
		while(remaining > 0){
			
			do{
				iMom = rand.nextInt(poolSize);
				iDad = rand.nextInt(poolSize);
			}while(iMom != iDad);
			
			NeuralNetwork Mom = this.pool.get(iMom);
			NeuralNetwork Dad = this.pool.get(iDad);
			NeuralNetwork Child = Mom.getChild(Dad);
			//Child.mutate();
			
			this.pool.add(Child);
			remaining--;
		}
		
		this.population.clear();
		for(int i=0; i < this.size; i++) {
			this.population.add(this.pool.get(i));
		}
		System.out.println("Breed succefull!");
	}
	
}
