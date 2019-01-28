package ga_solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import tsp.Solution;

public class BitArrayGenotype implements Individual<Integer> {
	int[] representation;
	int generation;
	int size;
	int idPopulation;

	public  BitArrayGenotype(int n){
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
	public Integer getFitness(){
		int result = 0;
		for(int i = 0; i<representation.length; i++){
			result+=representation[i];
		}
		return result;
	}





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

	public Individual<Integer> cloned() {
		Individual i = new BitArrayGenotype(size);
		i.setRepresentation(getClonedRepresentation());
		i.setGeneration(this.getGeneration());
		i.setIdPopulation(this.getIdPopulation());

		return i;
	}



	public int getIdPopulation() {
		return idPopulation;
	}

	public void setIdPopulation(int idPopulation) {
		this.idPopulation = idPopulation;
	}

	public int compareFiteness(Individual i1, Individual i2) {
		// TODO Auto-generated method stub
		return 0;
	}


	public static void main(String args[]){
		int[] r1 = {1,1,1,1,0}, r2 = {1,0,0,0,0}, r3 = {1,1,1,1,1}, r4 = {1,0,1,0,1}, r5 = {0,0,0,0,1};

		Individual i1 = new BitArrayGenotype(5); i1.setRepresentation(r1);
		Individual i2 = new BitArrayGenotype(5); i2.setRepresentation(r2);
		Individual i3 = new BitArrayGenotype(5); i3.setRepresentation(r3);
		i1.setGeneration(12);
		i2.setGeneration(1);
		i3.setGeneration(0);
		ArrayList<Individual> tempList = new ArrayList<Individual>();
		tempList.add(i1);
		tempList.add(i2);
		tempList.add(i3);

		/*AgeComparator cp  = new AgeComparator();
		Collections.sort(tempList, cp);
		for(int i=0; i<tempList.size(); i++){
			tempList.get(i).print();
			System.out.println(tempList.get(i).getGeneration());

		}*/

		double aa = 0.01826923076923077;
		int rounded = (int) aa ;

		System.out.println(rounded);

		/*Individual i4 = new BitArrayGenotype(5); i4.setRepresentation(r4);
		Individual i5 = new BitArrayGenotype(5); i5.setRepresentation(r5);

		ArrayList<Individual> list = new ArrayList<Individual>();

		list.add(i1); list.add(i2); list.add(i3);
		for(int i=0; i<list.size(); i++){
			list.get(i).print();
		}
		OneMaxFintessComparator fitnessComparator  = new OneMaxFintessComparator();
		Collections.sort(list, fitnessComparator);
		System.out.println();
		//list.add(i1); list.add(i2); list.add(i3);
		for(int i=0; i<list.size(); i++){
			list.get(i).print();
		}

		int n =  list.size();
		Individual cpyI1 = list.get(n-1).cloned();
		Individual cpyI2 = list.get(n-2).cloned();

		list.remove(n-1);
		list.remove(n-2);

		ArrayList<Individual> tempList = new ArrayList<Individual>();
		tempList.add(i4);
		tempList.add(i5);
		tempList.add(cpyI1);
		tempList.add(cpyI2);
		Collections.sort(tempList, fitnessComparator);


		System.out.println("---------------");
		for(int i=0; i<list.size(); i++){
			list.get(i).print();
		}

		System.out.println("--------temps LIST-------");
		for(int i=0; i<tempList.size(); i++){
			tempList.get(i).print();
		}

		list.add(tempList.get(0)); tempList.remove(0);
		list.add(tempList.get(0)); tempList.remove(0);
		System.out.println("--------final LIST-------");

		System.out.println("---------------");
		for(int i=0; i<list.size(); i++){
			list.get(i).print();
		}*/
	}


}


class OneMaxFintessComparator implements Comparator<Individual> {
	public int compare(Individual i1, Individual i2) {
		   int evalOfIndividual1 = (Integer) i1.getFitness();
		   int evalOfIndividual2 = (Integer) i2.getFitness();

		   //ascending order
		   //return evalOfIndividual1 - evalOfIndividual2;

		   //descending order
		   return evalOfIndividual2 - evalOfIndividual1;
	    }

}

class AgeComparator implements Comparator<Individual> {
	public int compare(Individual i1, Individual i2) {
		   int evalOfIndividual1 = i1.getGeneration();
		   int evalOfIndividual2 = i2.getGeneration();

		   //ascending order
		   //return evalOfIndividual1 - evalOfIndividual2;

		   //descending order
		   return evalOfIndividual2 - evalOfIndividual1;
	    }

}