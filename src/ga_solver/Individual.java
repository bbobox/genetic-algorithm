package ga_solver;

import java.util.Comparator;


/**
 * Represente un individu
 * @author boka
 *
 */
public class Individual {
	int[] representation;
	
	public Individual(int n){
		 representation = new int[n];
		 
		 for(int i = 0; i<representation.length; i++){
				representation[i]=1;
			}
	}
	
	/**
	 * Affiche la representation de l'individu
	 */
	public void print(){
		System.out.print('[');
		for(int i = 0; i<representation.length; i++){
			System.out.print(representation[i]);
		}
		System.out.print(']');
	}
	
	/**
	 * Calcule le fitness
	 */
	public int getFitness(){
		int result = 0;
		for(int i = 0; i<representation.length; i++){
			result+=representation[i];
		}
		return result;
	}
	
	/**
	 * Compateur de deux  individus en fonction du resultat de leur evalution
	 */
	public static Comparator<Individual> IndividualFintessComparator = new Comparator<Individual>() {

		public int compare(Individual i1, Individual i2) {
		   int evalOfIndividual1 = i1.getFitness();
		   int evalOfIndividual2 =  i2.getFitness();

		   //ascending order
		   //return evalOfIndividual1 - evalOfIndividual2;

		   //descending order
		   return evalOfIndividual2 - evalOfIndividual1;
	    }
	};
	
	/** 
	 * @param id : indice element à modifier dans la representation
	 * @param value : nouvelle value
	 */
	public void changeValue(int id, int value){
		representation[id] = value;
	}
	
	
	public int[] getRepresentation() {
		return representation;
	}

	public void setRepresentation(int[] representation) {
		this.representation = representation;
	}

	
	
	public int[] getClonedRepresentation(){
		
		int[] clone = representation.clone();
		
		return clone;
	}
	
	public static void main(String args[]){
		
		Individual i = new Individual(11);
		System.out.print(i.getFitness());
		
	}

}
