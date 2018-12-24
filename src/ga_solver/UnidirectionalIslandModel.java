package ga_solver;

import java.util.ArrayList;

import operators.Operators;

public class UnidirectionalIslandModel {

	ArrayList <Population> islands; // represente l'ensemble des iles
	private int m;
	private int popSize;
	private int iterMax;
	private int problemSize;
	private double crossoverProba;
	private double mutationProba;
	private int nbChilds;
	private int[] performance;

	public UnidirectionalIslandModel(int problemSize, int m , int childs, double pc, double pm, int popSize, int iter){
		islands = new ArrayList <Population> ();
		this.m = m;
		this.popSize  = popSize;
		iterMax = iter;
		this.problemSize = problemSize;
		crossoverProba = pc;
		mutationProba = pm;
		this.iterMax = iter;
		performance = new int[iterMax];
		nbChilds = childs;
	}



	/**
	 *  Verifie si le critère d'arret est atteint (fitness maximal)
	 * @return
	 */
	public boolean hasBestIndividual(){
		boolean ok=false;
		int i = 0;
		while(i<m && ok==false){
			if(islands.get(i).hasBestIndividual()){
				ok = true;
			}
			i++;
		}

		return ok;
	}

	/**
	 * Calcule la meilleur valeur de fitness de toute les Iles
	 * @return
	 */
	public int bestFitness(){
		int res = 0;
		for (int i=0; i<islands.size() ; i++){
			if ((Integer) islands.get(i).bestFitness()>res){
				res = (Integer) islands.get(i).bestFitness();
			}
		}
		return res;
	}


	/**
	 * Execution de l'agloritthme évolutionnaire utilisant le concept en Ile
	 * @param selection : type de selection
	 * @param crossover : type de croissement
	 * @param mutation	: type de mutation
	 * @param insertion	 : type d'insertion
	 * @return
	 */
	public int[] run(int selection, int crossover, int mutation, int insertion){
		int stepCounter = 0;
		//Initialisation des  iles;
		for (int i = 0; i<m ; i++ ){
			islands.add(new BitArrayIndividualsPopulation(problemSize,popSize/m, nbChilds, crossoverProba, mutationProba , iterMax));
			islands.get(i).initialization();
		}

		for(int i = 0 ; i< iterMax ; i++){
			stepCounter = i;
			// critère d'arret

			if(!hasBestIndividual()){
				performance[i] = bestFitness();
				ArrayList<Individual> islandsBestIndividuals = new ArrayList<Individual>();
				//Evolution des populations
				for(int k = 0 ; k<islands.size() ; k++){
					islands.get(k).evolution(selection, crossover, mutation, insertion);
					Individual best = islands.get(k).getBestIndividual();
					islandsBestIndividuals.add(best);
					islands.get(k).removeIndividual(best);
				}
				// Migration
				for(int k = 1 ; k<islands.size() ; k++){
					islands.get(k).addIndividual(islandsBestIndividuals.get(k-1).cloned());
				}
				islands.get(0).addIndividual(islandsBestIndividuals.get(islandsBestIndividuals.size()-1).cloned());

			}
			else{
				performance[stepCounter] = bestFitness();
			}

		}

		return performance;

	}

	/**
	 * Calcule de  la moyenne des resultats obtenus lors des exécutions
	 * @param executions : ensemble des resultats d'executions
	 * @param iterMax : nombre maximum d'iterations
	 * @return
	 */
	 static double[] average(ArrayList<int[]> executions, int iterMax) {
		 int nbExecutions = executions.size();
		 double[] res = new double[iterMax];
		 for( int i = 0 ; i< iterMax; i++) {
			 int cpt=0;
			 for(int k = 0; k < nbExecutions; k++){
				 cpt+=executions.get(k)[i];
			 }
			 res[i] = (cpt*(1.))/nbExecutions;
		 }

		return res;
	}

	 /**
	  * Affichage des valeurs d'un tableau répresentatif des exécutions
	  * @param array
	  */
	 static void printArray(double[] array){
		 System.out.println("#iterations fitness");
		 for (int i=0; i< array.length; i++) {
			 System.out.println(i+" "+array[i]);
		 }

	 }

	public static void main(String args[]){
			ArrayList<int[]> executions = new  ArrayList<int[]>();
			if(args.length==10){
				int selectionType = Integer.parseInt(args[0]);
				int crossoverType = Integer.parseInt(args[1]);
				int mutationType = Integer.parseInt(args[2]);
				int insertionType = Integer.parseInt(args[3]);
				double pc = Double.parseDouble(args[4]);
				double pm = Double.parseDouble(args[5]);
				int size = Integer.parseInt(args[6]);
				int max = Integer.parseInt(args[7]);
				int tests = Integer.parseInt(args[8]);
				int popupaltionSize = Integer.parseInt(args[9]);
				UnidirectionalIslandModel islands = new UnidirectionalIslandModel(size, 4, 2,pc, pm,popupaltionSize,max);
				if(mutationType>=0) {
					for (int i = 0; i< tests; i++) {
						 islands = new UnidirectionalIslandModel(size, 4, 2,pc, pm,popupaltionSize,max);
						 executions.add(islands.run(selectionType, crossoverType, mutationType, insertionType));
					}
					double[] average = average(executions,max);
					printArray(average);
				}

			}

	}

}



