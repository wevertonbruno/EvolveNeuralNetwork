package br.univasf.weverton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population {

	public int size;
	public ArrayList<NeuralNetwork> population;
	private ArrayList<NeuralNetwork> pool;
	
	public Population(int size){
		
		this.size = size;
		this.population = new ArrayList<NeuralNetwork>();
		this.pool = new ArrayList<NeuralNetwork>();
		
		for(int i=0; i<this.size; i++)
			population.add(new NeuralNetwork(2,2,1));
	}
	
	public void sortPopulation(){
		Collections.sort(this.population);
	}
	
	public void selection(){
		
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
			
			do{
			index = rand.nextInt((int)size/4);
			for(int j : used)
				if(j == index){
					index = -1;
					break;
				}
			}while(index != -1);
			
			this.pool.add(new NeuralNetwork(this.population.get(index)));
			this.pool.get(size/2 + i).mutate();
		}
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
			
			this.pool.add(Mom.getChild(Dad));
			remaining--;
		}
		
	}
	
}
