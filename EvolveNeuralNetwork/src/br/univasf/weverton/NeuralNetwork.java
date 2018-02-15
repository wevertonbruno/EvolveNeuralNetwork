package br.univasf.weverton;

import java.util.Random;
import java.lang.Comparable;

public class NeuralNetwork implements Comparable<NeuralNetwork>{
	private int input_nodes;
	private int hidden_nodes;
	private int output_nodes;
	
	private int fitness;
	
	public Matrix weights_input_hidden;
	public Matrix weights_hidden_output;
	public Matrix hidden_bias;
	public Matrix output_bias;
	
	public NeuralNetwork(int in, int hid, int out){
		this.input_nodes = in;
		this.output_nodes = out;
		this.hidden_nodes = hid;
		setFitness(0);
		 // inicializar matriz de pesos
		
		initWeights();
		randomizeWeights();
	}
	
	//metodo que realiza uma deep copy desta rede
	public NeuralNetwork(NeuralNetwork copyNN){
		this.input_nodes = copyNN.input_nodes;
		this.output_nodes = copyNN.output_nodes;
		this.hidden_nodes = copyNN.hidden_nodes;
		this.setFitness(copyNN.getFitness());
		
		this.weights_input_hidden = copyNN.weights_input_hidden.toCopy();
		this.weights_hidden_output = copyNN.weights_hidden_output.toCopy();
		this.hidden_bias = copyNN.hidden_bias.toCopy();
		this.output_bias = copyNN.output_bias.toCopy();
	}
	
	private void initWeights(){
		//input_hidden possui numero de colunas da input e linhas da hidden
		this.weights_input_hidden = new Matrix(hidden_nodes,input_nodes);
		this.weights_hidden_output = new Matrix(output_nodes,hidden_nodes);
		this.hidden_bias = new Matrix(hidden_nodes,1);
		this.output_bias = new Matrix(output_nodes, 1);
	}
	private void randomizeWeights(){
		this.weights_input_hidden.randomize();
		this.weights_hidden_output.randomize();
		this.hidden_bias.randomize();
		this.output_bias.randomize();
	}
	
	public float[] feedFoward(float input[]){
		
		//geração das saidas das camadas escondidas
		Matrix in = Matrix.inputArray(input);
		Matrix hidden = Matrix.multiply(weights_input_hidden, in);
		hidden.add(hidden_bias);
		hidden.mapSigmoid();
		
		//geração das saidas
		Matrix out = Matrix.multiply(weights_hidden_output, hidden);
		out.add(output_bias);
		out.mapSigmoid();
		
		//retorna um vetor das saidas
		return out.toArray();
		
	}
	
	public void setFitness(int v){
		this.fitness = v;
	}
	public void addFitness(int v) {
		this.fitness += v;
	}
	public void incFitness(){
		this.fitness++;
	}
	public int getFitness(){
		return this.fitness;
	}
	
	public void mutate(){
		Random rand = new Random();
		for(int i = 0; i < weights_input_hidden.linha; i++){
			for(int j = 0; j < weights_input_hidden.coluna; j++){
				float temp = weights_input_hidden.data[i][j];
				float r = rand.nextFloat()*100;
				
				if (r <= 2)
                {
                    temp *= -1;
                }
                else if (r <= 4)
                { 
                    temp = rand.nextFloat()*2-1;
                }
                else if (r <= 6f)
                { //if 3
                  //randomly increase by 0% to 100%
                    float factor = rand.nextFloat() + 1f;
                    temp *= factor;
                }
                else if (r <= 8f)
                { //if 4
                  //randomly decrease by 0% to 100%
                    float factor = rand.nextFloat();
                    temp *= factor;
                }
				weights_input_hidden.data[i][j] = temp;
			}
		}
		
		for(int i = 0; i < weights_hidden_output.linha; i++){
			for(int j = 0; j < weights_hidden_output.coluna; j++){
				float temp = weights_hidden_output.data[i][j];
				float r = rand.nextFloat()*100;
				
				if (r <= 2)
                {
                    temp *= -1;
                }
                else if (r <= 4)
                { 
                    temp = rand.nextFloat()*2-1;
                }
                else if (r <= 6f)
                { //if 3
                  //randomly increase by 0% to 100%
                    float factor = rand.nextFloat() + 1f;
                    temp *= factor;
                }
                else if (r <= 8f)
                { //if 4
                  //randomly decrease by 0% to 100%
                    float factor = rand.nextFloat();
                    temp *= factor;
                }
				
				weights_hidden_output.data[i][j] = temp;
			}
		}
	}
	
	//backpropagation
	public void backpropagation(float[] inputs,float[] target){
		
		float[] out = feedFoward(inputs);
		
		Matrix outputs = Matrix.inputArray(out);
		Matrix targets = Matrix.inputArray(target);
		
		//calculo do erro
		//Erro = target - output
		Matrix output_erro = Matrix.sub(targets,outputs);
		
		//calcular o erro da hidden
		Matrix weight_ho = Matrix.transpose(this.weights_hidden_output);
		Matrix hidden_error = Matrix.multiply(weight_ho, output_erro);
		
		hidden_error.printMatrix();
		//coding back Propagation
	}

	//metodo auxiliar do Sort()
	public int compareTo(NeuralNetwork other) {
		if (this.getFitness() < other.getFitness()) {
            return -1;
        }
        if (this.getFitness() > other.getFitness()) {
            return 1;
        }
        return 0;
	}
	
	public NeuralNetwork getChild(NeuralNetwork other){
		
		NeuralNetwork child = new NeuralNetwork(this.input_nodes,this.hidden_nodes,this.output_nodes);
		
		child.weights_input_hidden.fromArray(crossOver(this.weights_input_hidden,other.weights_input_hidden));
		child.hidden_bias.fromArray(crossOver(this.hidden_bias,other.hidden_bias));
		child.weights_hidden_output.fromArray(crossOver(this.weights_hidden_output,other.weights_hidden_output));
		child.output_bias.fromArray(crossOver(this.output_bias,other.output_bias));
		
		
		return child;
	}
	
	private float[] crossOver(Matrix pai1, Matrix pai2) {
		float[] parent1, parent2, child;
		parent1 = pai1.toArray();
		parent2 = pai2.toArray();
		child = new float[parent1.length];
		Random rand = new Random();
		
		if(rand.nextFloat() > 0.4){
			int midpoint = rand.nextInt(parent1.length);

			for (int i = 0; i < parent1.length; i++) {
				if (i > midpoint) 
					child[i] = parent1[i];
				else              
					child[i] = parent2[i];
			}
			return child;

		}else{
			return parent1.clone();		
		}
		
	}
	

}