package br.univasf.weverton;

import java.util.Random;

public class Matrix {

	public int linha,coluna;
	public float[][] data;
	
	public Matrix(int linha, int coluna){
		this.linha = linha;
		this.coluna = coluna;
		this.data = new float[this.linha][this.coluna];
		for(int i = 0; i < linha; i++){
				for(int j=0;j<coluna;j++){
					this.data[i][j] = 0;
			}
		}	
	}
	
	public int lenght(){
		return this.linha * this.coluna;
	}
	
	public void randomize(){
		for(int i = 0; i < linha; i++){
				for(int j=0;j<coluna;j++){
					this.data[i][j] = (new Random().nextFloat())*2 - 1;
			}
		}	
	}
	
	public float[] toArray(){
		float[] array = new float[linha*coluna];
		int index = 0;
		for(int i = 0; i < linha; i++)
			for(int j=0;j<coluna;j++)
				array[index++] = this.data[i][j];
		return array;
	}
	public Matrix toCopy(){
		Matrix m = new Matrix(linha,coluna);
		for(int i = 0; i < linha; i++)
			for(int j=0;j<coluna;j++)
				m.data[i][j] = this.data[i][j];
		return m;
	}
	
	public static Matrix transpose(Matrix other){
		Matrix m = new Matrix(other.coluna, other.linha);
		for(int i = 0; i < other.linha; i++)
			for(int j=0;j< other.coluna;j++)
				m.data[j][i] = other.data[i][j];
		return m;
	}
	
	public static Matrix multiply(Matrix m1, Matrix m2){
		if(m1.coluna != m2.linha){
			System.out.println("Dimensões incompativeis");
			return m1;
		}
		Matrix result = new Matrix(m1.linha,m2.coluna);
		for(int i = 0; i < result.linha; i++){
			for(int j=0;j<result.coluna;j++){
				float sum = 0;
				for(int k=0;k<m1.coluna;k++){
					sum += m1.data[i][k]*m2.data[k][j];
				}
				result.data[i][j] = sum;
				}
			}
		return result;
	}
	
	public void mapSigmoid(){
		  for (int i = 0; i < this.linha; i++) {
		    for (int j = 0; j < this.coluna; j++) {
		    		this.data[i][j] = sigmoid(data[i][j]);
		    }
		  }
		}
	
	private float sigmoid(float x){
		return (float)(1/(1 + Math.exp(x)));
	}
	
	public static Matrix inputArray(float[] array){
		  Matrix m = new Matrix(array.length, 1);
		  for (int i = 0; i < array.length; i++) {
		    m.data[i][0] = array[i];
		  }
		  return m;
		}
	
	public void fromArray(float[] array){
		if(array.length != linha*coluna)
			return;
		  
		  for (int i = 0,k = 0; i < this.linha; i++) {
			  for(int j=0;j<this.coluna;j++){
				  this.data[i][j] = array[k++];
			}
		  }
		}
	
	public void add(Matrix other){
		    for (int i = 0; i < this.linha; i++) {
		      for (int j = 0; j < this.coluna; j++) {
		        this.data[i][j] += other.data[i][j];
		    }
		}
	}
	
	public void printMatrix(){
		  for (int i = 0; i < this.linha; i++) {
			    for (int j = 0; j < this.coluna; j++) {
			    	if(j<this.coluna-1)
			      System.out.print("" + this.data[i][j] + " | ");
			    	else System.out.print("" + this.data[i][j]);
			    }
			    System.out.println("");
			  }
	}
	public static Matrix sub(Matrix targets, Matrix outputs) {
		Matrix result = new Matrix(targets.linha, targets.coluna);
		for (int i = 0; i < result.linha; i++) 
		      for (int j = 0; j < result.coluna; j++) 
		        result.data[i][j] = targets.data[i][j] - outputs.data[i][j];
		return result;
	}
	
	public void mutateAux(float taxa) {
		Random rand = new Random();
		for(int i = 0; i < this.linha; i++){
			for(int j = 0; j <this.coluna; j++){
				
				float temp = this.data[i][j];
				float r = rand.nextFloat()*(100/taxa);
				
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
				this.data[i][j] = temp;
			}
		}
	}
}