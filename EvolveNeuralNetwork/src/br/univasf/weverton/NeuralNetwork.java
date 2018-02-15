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
	
	public NeuralNetwork(int in, int hid, int out, float[] pesos){
		this.input_nodes = in;
		this.output_nodes = out;
		this.hidden_nodes = hid;
		setFitness(0);
		 // inicializar matriz de pesos
		
		initWeightsFromArray(pesos);
	}
	
	public void initWeightsFromArray(float[] pesos){
		float[] temp;
		int t1,t2;
		
		//primeira camada de pesos
		this.weights_input_hidden = new Matrix(hidden_nodes,input_nodes);
			t1 = this.weights_input_hidden.lenght();
			temp = new float[t1];
				for(int i=0; i<t1; i++)
					temp[i]=pesos[i];
		this.weights_input_hidden.fromArray(temp);
		
		//segunda camada de pesos
		this.weights_hidden_output = new Matrix(output_nodes,hidden_nodes);
		t2 = this.weights_hidden_output.lenght();
		temp = new float[t2];
			for(int i=0; i<t2; i++)
				temp[i]=pesos[i+t1];
		this.weights_hidden_output.fromArray(temp);
		t1 += t2;
		
		
		this.hidden_bias = new Matrix(hidden_nodes,1);
		t2 = this.hidden_bias.lenght();
		temp = new float[t2];
			for(int i=0; i<t2; i++)
				temp[i]=pesos[i+t1];
		this.hidden_bias.fromArray(temp);
		t1 += t2;
		
		this.output_bias = new Matrix(output_nodes, 1);
		t2 = this.output_bias.lenght();
		temp = new float[t2];
			for(int i=0; i<t2; i++)
				temp[i]=pesos[i+t1];
		this.hidden_bias.fromArray(temp);
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
	
	public void train(float[] inputs,float[] target){
		
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
		float[] arrayPai1, arrayPai2;
		
		//array vai ter o tamanho de todos os parametros da rede
		//necessario criar metodo que cria rede passando um vetor
		arrayPai1 = new float[this.weights_input_hidden.lenght() + this.weights_hidden_output.lenght() + 
		                      this.hidden_bias.lenght() + this.output_bias.lenght()];
		
		arrayPai2 = new float[other.weights_input_hidden.lenght() + other.weights_hidden_output.lenght() + 
		                      other.hidden_bias.lenght() + other.output_bias.lenght()];
		
		//criar algoritmo de procriacao aqui
		
		return new NeuralNetwork(2,2,1);
	}
	

}