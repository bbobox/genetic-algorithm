package ga_solver;

import java.util.Comparator;


/**
 * Represente un individu
 * @author boka
 *
 */
public class Individual {
	int[] representation;
	int generation;
	int size;

	public Individual(int n){
		 representation = new int[n];
		 size = n;

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

	public static Comparator<Individual> IndividualAgeComparator = new Comparator<Individual>() {

		public int compare(Individual i1, Individual i2) {
		   int evalOfIndividual1 = i1.getGeneration();
		   int evalOfIndividual2 =  i2.getGeneration();

		   //ascending order
		   //return evalOfIndividual1 - evalOfIndividual2;

		   //descending order
		   return evalOfIndividual2 - evalOfIndividual1;
	    }
	};





	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

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

	public Individual cloned() {
		Individual i = new Individual(size);
		i.setRepresentation(getClonedRepresentation());
		i.setGeneration(this.getGeneration());

		return i;
	}

	/*public static void main(String args[]){

		Individual i = new Individual(11);
		Individual i2 = i; //.clone();
		i.setRepresentation(new int[3]);
		i.print();
		i2.print();
		//System.out.println(i.getFitness());

	}*/

}
