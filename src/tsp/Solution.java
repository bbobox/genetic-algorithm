package tsp;

import ga_solver.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import java.util.stream.IntStream;

public class Solution implements Individual<Double> {

	int[] representation;
	int generation;
	int size;
	int idPopulation;
	double[][] distances; //
	double fitness;
	int n ;

    ArrayList<Individual> parents;

	/**
	 * Réprésente
	 * @param n : nombre de ville
	 */
	public Solution(int n, double[][] dist){
		representation = new int[n];
		Random rand = new Random();
		ArrayList<Integer> l = new ArrayList<Integer>();
		this.n = n;
		distances = dist;
		int  id;
		for(int i = 0; i<n; i++){
			id = rand.nextInt(n);
			while(l.contains(id)){
				id = rand.nextInt(n);
			}
			l.add(id);
			representation[i]=id;
		}
	}

	public double[][] getDistances() {
		return distances;
	}

	public void setDistances(double[][] distances) {
		this.distances = distances;
	}

	/**
	 * Affiche la representation de l'individu
	 */
	public void print(){
		System.out.print('[');
		for(int i = 0; i<representation.length; i++){
			System.out.print(representation[i]+",");
		}
		System.out.print(']');
	}


	/**
	 * Evaluation de fitness fitness (la somme des distance entre les ville à parcourir)
	 * @return
	 */
	public  Double getFitness(){
		Double result = (double) 0;
		for(int i = 0; i<n-1; i++){
			result+=distances[representation[i]][representation[i+1]];
		}
		result+=distances[representation[representation.length-1]][representation[0]];
		fitness= result;
		return fitness;
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

	public Solution cloned() {
		Solution i = new Solution(n,distances);
		i.setRepresentation(this.getClonedRepresentation());
		i.setGeneration(this.getGeneration());
		i.setIdPopulation(this.getIdPopulation());
		return i;
	}



	public void setFiteness(double f){
		fitness = f;
	}

	public int getIdPopulation() {
		return idPopulation;
	}

	public void setIdPopulation(int idPopulation) {
		this.idPopulation = idPopulation;
	}


	public static void main(String args[]){

		ArrayList<Solution> list = new ArrayList<Solution>();

		Solution s1 = new Solution(9,new double[1][1]);
		s1.setFiteness(2);


		Solution s2 = new Solution(9,new double[1][1]);
		s2.setFiteness(2);

		Solution s3 = new Solution(9,new double[1][1]);
		s3.setFiteness(3);

		list.add(s1);
		list.add(s2);
		list.add(s3);
		Collections.sort(list,new FintessComparator());

		for(int i = 0; i<list.size() ; i++){
			System.out.println(list.get(i).getFitness());
		}

	}



}

	class FintessComparator implements Comparator<Individual> {
    public int compare(Individual a, Individual b) {
        if ( (Double) a.getFitness() < (Double) b.getFitness() ){ return -1;}
        else if ( a.getFitness() == b.getFitness() ) return 0;
        else return 1;
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


