package tsp;

/*public class BitArrayIndividualsPopulation {

}*/

import ga_solver.Individual;
import ga_solver.Population;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import operators.MutationInsert;
import operators.MutationInversion;
import operators.MutationScramble;
import operators.MutationSwap;
import operators.Operator;
import operators.OperatorMutation;
import operators.Operators;
import tsp.AgeComparator;
import utilities.ArrayFunction;

/**
 * Cette classe represente le solveur du l'algorithme genetique et
 * ses composantes
 * @author boka
 *
 */
public class TspPopulation implements Population<Double> {

	private ArrayList<Individual> population;
	private int problemSize;
	private int populationSize;
	private int nbChilds;
	private double crossoverProba;
	private double mutationProba;
	private Individual[] childs;
	private Individual[] parents;
	private int iterMax;
	private ArrayList<Individual> currentPopulation;
	private Double[] performance;
	private Operators mutations;
	private int improvement;
	private  int iteration;

	private double[][] fitnessBeforeEvolution;
	private double[][] fitnessAfterEvolution;
	private ArrayList<Integer> originOfIndividuals; // ensemble d'origne des individus présent
	private int nbPop;
	private double[] averageImprovements;

	double[][] distances; // distances entre les villes

	FintessComparator fitnessComparator= new FintessComparator();
	AgeComparator ageComparator = new AgeComparator();

	private int[] improvements;
	public TspPopulation( int problemSize, int popSize , int childs, double pc, double pm, int iterMax) throws IOException{
		Parser p= new Parser();
		distances = p.getDistancesMatrix("a280.tsp");
		population = new ArrayList<Individual>();
		currentPopulation = new ArrayList<Individual>();
		this.problemSize = distances[0].length;
		populationSize = popSize;
		nbChilds = childs;
		crossoverProba = pc;
		mutationProba = pm;
		this.childs = new Individual[nbChilds];
		this.parents =  new Individual[nbChilds];
		this.iterMax = iterMax;
		performance = new Double[iterMax];
		iteration = 0;
		nbPop = 1;
		improvements = new  int[nbPop];
		averageImprovements = new double[nbPop];
		mutations = new Operators(4,25,this.iterMax);


		/*distances = new double[problemSize][problemSize];
		// les distances
		Random rand= new Random();
		int k;
		for(int i=0; i<problemSize;i++){
			for(int j=0; j<problemSize;j++){
				if(distances[i][j]==0){
					k = rand.nextInt(this.problemSize/2)+1;
					distances[i][j] = k;
					distances[j][i] = k;
				}
			}
		}*/


		// instanciation et ajouts des opérateurs de mutations;
		OperatorMutation mutationInsert = new MutationInsert();
		OperatorMutation mutationInversion = new MutationInversion();
		OperatorMutation mutationScramble = new MutationScramble();
		OperatorMutation mutationSwap = new MutationSwap(this.problemSize);

		mutations.addOperator(new Operator(mutationSwap,this.iterMax));
		mutations.addOperator(new Operator(mutationInsert, this.iterMax));
		mutations.addOperator(new Operator(mutationInversion,this.iterMax));
		mutations.addOperator(new Operator(mutationScramble,this.iterMax));
		//mutations.operatorInitial(3);

	}

	public TspPopulation(int popSize , int childs, double pc, double pm, int iterMax) throws IOException{
		Parser p= new Parser();
		distances = p.getDistancesMatrix("/home/etudiant/workspace/genetic-algorithm/a280.tsp");
		population = new ArrayList<Individual>();
		currentPopulation = new ArrayList<Individual>();
		this.problemSize = distances[0].length;
		populationSize = popSize;
		nbChilds = childs;
		crossoverProba = pc;
		mutationProba = pm;
		this.childs = new Individual[nbChilds];
		this.parents =  new Individual[nbChilds];
		this.iterMax = iterMax;
		performance = new Double[iterMax];
		iteration = 0;
		nbPop = 1;
		improvements = new  int[nbPop];
		averageImprovements = new double[nbPop];
		mutations = new Operators(4,25,this.iterMax);


		/*distances = new double[problemSize][problemSize];
		// les distances
		Random rand= new Random();
		int k;
		for(int i=0; i<problemSize;i++){
			for(int j=0; j<problemSize;j++){
				if(distances[i][j]==0){
					k = rand.nextInt(this.problemSize/2)+1;
					distances[i][j] = k;
					distances[j][i] = k;
				}
			}
		}*/


		// instanciation et ajouts des opérateurs de mutations;
		OperatorMutation mutationInsert = new MutationInsert();
		OperatorMutation mutationInversion = new MutationInversion();
		OperatorMutation mutationScramble = new MutationScramble();
		OperatorMutation mutationSwap = new MutationSwap(problemSize);

		mutations.addOperator(new Operator(mutationSwap,this.iterMax));
		mutations.addOperator(new Operator(mutationInsert, this.iterMax));
		mutations.addOperator(new Operator(mutationInversion,this.iterMax));
		mutations.addOperator(new Operator(mutationScramble,this.iterMax));

		//mutations.operatorInitial(3);

	}


	/**
	 * Modification du nombre total de population/iles (population voisines+1)
	 * @param nb
	 */
	public void setNbPop(int nb){
		nbPop = nb;
		improvements = new int[nbPop];
		averageImprovements = new double[nbPop];
	}


	public int getPopulationSize() {
		return currentPopulation.size();
	}

	/**
	 * @param nbParents : le nombre d'individus à selectionnés
	 * à l'initialization
	 */
	public void initialization(){
		for(int i = 0; i<populationSize ;i++){
			Individual  ind = new Solution(problemSize,distances);
			//System.out.println(ind.getFitness());
			ind.setGeneration(0);
			currentPopulation.add(ind);
		}
	}

	/**
	 * Definition de 'ile d'appartenance de l'ensemble des individus de la population
	 */
	public void assignIndividualsToPopulation(int i){
		for(int k =0; k<currentPopulation.size();k++){
			currentPopulation.get(k).setIdPopulation(i);
		}
	}

	/**
	 * Recupere de la population actuelle
	 * @return
	 */
	public ArrayList<Individual> getCurrentPopulation(){
		return currentPopulation;
	}


	/**
	 * Affichage d'un ensemble d 'individus
	 */
	public void printSetOfIndividuals( ArrayList<Individual> set){
		for(int i = 0; i<set.size(); i++){
			set.get(i).print();
		}
		System.out.println();
	}

	/**
	 * Verifie les critrères de l'algortithme et renvoie
	 * 'true" si l'objectif est atteint
	 */
	public boolean hasBestIndividual(){
		boolean isOk = false;
		/*int i = 0;
		while(i < currentPopulation.size() && isOk==false){
			Individual ind = currentPopulation.get(i);
			if( ind.getFitness() == problemSize){
				isOk = true;
			}
			i++;
		}*/
		return isOk;
	}

	/**
	 * Effectue le croissement  de deux individus parents: methode pmx
	 * @param i1
	 * @param i2
	 * @return les deux individus enfants generés
	 */
	public Individual[] order1Crossover(Individual i1, Individual i2){
		Individual[] individuals = new Individual[2];

		Individual child1 = i1.cloned();
		Individual child2 = i2.cloned();
		child1.setRepresentation(this.offspringGenerationOrd1(i1, i2, 3));
		child2.setRepresentation(this.offspringGenerationOrd1(i2, i1, 3));

		individuals[0] = child1;
		individuals[1] = child2;

		return individuals;
	}

	/**
	 *
	 * @param i1
	 * @param i2
	 * @return
	 */
	public Individual[] pmxCrossover(Individual i1, Individual i2){
		Individual[] individuals = new Individual[2];

		Individual child1 = i1.cloned();
		Individual child2 = i2.cloned();
		child1.setRepresentation(this.offspringGenerationPmx(i1, i2, 4));
		child2.setRepresentation(this.offspringGenerationPmx(i2, i1, 4));

		individuals[0] = child1;
		individuals[1] = child2;

		return individuals;
	}

	/**
	 *
	 * @param i1
	 * @param i2
	 * @return
	 */
	public Individual[] cycleCrossover(Individual i1, Individual i2){
		Individual[] individuals = new Individual[2];

		Individual child1 = i1.cloned();
		Individual child2 = i2.cloned();
		child1.setRepresentation(this.offspringGenerationCycle(i1, i2));
		child2.setRepresentation(this.offspringGenerationCycle(i2, i1));

		individuals[0] = child1;
		individuals[1] = child2;

		return individuals;
	}



	/**
	 * croisement pa la methode de PMX:  Partially Mapped Crossover
	 *
	 */
	public int[]  offspringGenerationPmx(Individual parent1,Individual parent2,  int subsetSize){
		int[] representation1 = parent1.getRepresentation();
		int[] representation2 = parent2.getRepresentation();
		int[] childRepresentation = new int[representation1.length];
		Random rand = new Random();
		ArrayFunction af = new ArrayFunction();
		af.fill(childRepresentation, -1);

		int i0 = rand.nextInt(representation1.length);
		while(i0+subsetSize>representation1.length){
			 i0= rand.nextInt(representation1.length);
		}


		// etape 1
		int[] subSet1 = new int[subsetSize];
		int[] subSet2 = new int[subsetSize];
		int k=0;
		for(int i=i0; i<i0+subsetSize;i++){
			childRepresentation[i]=representation1[i];
			subSet1[k]=representation1[i];
			subSet2[k] = representation2[i];
			k++;
		}

		// etape 2
		int value1, value2, id, positionValue;
		for(int i=i0; i<i0+subsetSize;i++){
			value2 = representation2[i];
			if (!af.contains(subSet1, value2)){
				id= i;
				value1= representation1[id];
				while(af.contains(subSet2, value1)){
					value2 = value1;
					id= af.getValuePosition(representation2, value1);
					value1 = representation1[id];
				}
				id= af.getValuePosition(representation2, value1);
				childRepresentation[id] = representation2[i];
			}
		}

		//Etape 3
		int ichild =0, iParents2=0,value;
		while(iParents2<representation1.length && ichild<representation1.length ){
			value =  representation2[iParents2];

			if(af.contains(childRepresentation, value)){
				iParents2++;
			}
			else{
				if(childRepresentation[ichild]!=-1){
					ichild++;
				}else{
					childRepresentation[ichild]=value;
					ichild++;
					iParents2++;
				}
			}

		}


		return childRepresentation;
	}

	/**
	 * croisement la me methode de CX: offspringGenerationCyle
	 * @param parent1
	 * @param parent2
	 * @return
	 */
	public int[] offspringGenerationCycle(Individual parent1,Individual parent2){
		int[] representation1 = parent1.getRepresentation();
		int[] representation2 = parent2.getRepresentation();

		// step 1: cyle 1 indentification
		int i1; int i2 = 0;
		ArrayList<Integer> firstCyle= new ArrayList<Integer>();
		ArrayList<Integer> secondCyle = new ArrayList<Integer>();
		ArrayFunction af = new ArrayFunction();
		ArrayList<Integer> cycles[];// = {firstCyle,secondCyle};
		//cycles[0]=firstCyle;
		int[] childRepresentation = representation1;

		int firstPosition = 0;
		int allele2= representation2[firstPosition];
		i1=af.getValuePosition(representation1, allele2);

		while( i1!=firstPosition){
			firstCyle.add(i1);
			allele2 = representation2[i1];
			i1=af.getValuePosition(representation1, allele2);
		}
		firstCyle.add(firstPosition);

		// step 2: cyle 2 indentification
		firstPosition = 0;
		 while(firstCyle.contains(firstPosition)){
			 firstPosition++;
		}
		 if(firstCyle.size()<representation1.length){

			allele2= representation2[firstPosition];
			i1=af.getValuePosition(representation1, allele2);
			while( i1!=firstPosition){
				secondCyle.add(i1);
				allele2 = representation2[i1];
				i1=af.getValuePosition(representation1, allele2);
			}
			secondCyle.add(firstPosition);

			int alleleId;
			for(int i = 0 ; i<firstCyle.size();i++){
				alleleId = firstCyle.get(i);
				childRepresentation[alleleId]= representation1[alleleId];
			}

				for(int i = 0 ; i<secondCyle.size();i++){
					alleleId = secondCyle.get(i);
					childRepresentation[alleleId]= representation2[alleleId];
				}
		}
		return childRepresentation;
	}

	/**
	 * Croissement entre deux parents
	 * @param parent1
	 * @param parent2
	 * @param subsetSize : taille du sous chemin
	 * @return
	 */
	public int[] offspringGenerationOrd1(Individual parent1,Individual parent2, int subsetSize ){
		int[] representation1 = parent1.getRepresentation();
		int[] representation2 = parent2.getRepresentation();
		int[] childRepresentation = new int[representation1.length];
		Random rand = new Random();
		ArrayFunction af = new ArrayFunction();
		af.fill(childRepresentation, -1);
		int i0 = rand.nextInt(representation1.length);
		while(i0+subsetSize>representation1.length){
			 i0= rand.nextInt(representation1.length);
		}

		for(int i=i0; i<i0+subsetSize;i++){
			childRepresentation[i]=representation1[i];
		}

		int ichild =0, iParents2=0,value;
		while(iParents2<representation1.length && ichild<representation1.length ){
			value =  representation2[iParents2];

			if(af.contains(childRepresentation, value)){
				iParents2++;
			}
			else{
				if(childRepresentation[ichild]!=-1){
					ichild++;
				}else{
					childRepresentation[ichild]=value;
					ichild++;
					iParents2++;
				}
			}

		}

		return childRepresentation;
	}


	/**
	 *
	 * @param child
	 * @param N
	 * @return
	 */
	public int[] swapMutation(Individual child){
		int[] representation = child.getClonedRepresentation();
		Random rand = new Random();
		int idCity1, idCity2, swap;
		idCity1 = rand.nextInt(problemSize);

		idCity2 = rand.nextInt(problemSize);
		while(idCity2==idCity1){
			idCity2 = rand.nextInt(problemSize);
		}

		swap = representation[idCity1];
		representation[idCity1] = representation[idCity2];
		representation[idCity2] = swap;
		Solution s = new Solution(this.problemSize,distances);
		s.setRepresentation(representation);

		return representation;
	}

	/**
	 *
	 * @param child
	 * @return
	 */
	public int[] inversionMutation(Individual child){
		int[] representation = child.getClonedRepresentation();
		Random rand = new Random();
		ArrayFunction af = new ArrayFunction();
		int idCity1, idCity2;
		idCity1 = rand.nextInt(representation.length-1);

		idCity2 = rand.nextInt(representation.length-1-idCity1)+idCity1+1;
		representation = af.inverse(representation, idCity1, idCity2);
		return representation;
	}


	/**
	 * Mutution par Inserttion
	 * @param child
	 */
	public int[] insertMutation(Individual  child){
		int[] representation = child.getClonedRepresentation();
		Random rand = new Random();
		int idCity1, idCity2, swap;
		idCity1 = rand.nextInt(representation.length-1);

		idCity2 = rand.nextInt(representation.length-1-idCity1)+idCity1+1;
		swap=representation[idCity2];

		for(int i=idCity2 ; i>idCity1+1 ;i--){
			representation[i]=representation[i-1];
		}
		representation[idCity1+1]=swap;

		return representation;
	}


	/**
	 *
	 * @param child
	 * @return
	 */
	public int[] scrambleMutation(Individual child){
		int[] representation = child.getClonedRepresentation();
		Random rand = new Random();
		ArrayFunction af = new ArrayFunction();
		int idCity1, idCity2;
		idCity1 = rand.nextInt(representation.length-1);

		idCity2 = rand.nextInt(representation.length-1-idCity1)+idCity1+1;
		af.suffle(representation, idCity1, idCity2);
		return representation;
	}






	/**
	 * Insertion des deux enfants generés dans la population et
	 *  remplacement des individus les moins bons (fitness)
	 */
	public void childrenInsertion1(){
		Collections.sort(currentPopulation, fitnessComparator);

		int n =  currentPopulation.size();
		Individual cpyI1 = currentPopulation.get(n-1).cloned();
		Individual cpyI2 = currentPopulation.get(n-2).cloned();
		currentPopulation.remove(n-1);
		currentPopulation.remove(n-2);
		ArrayList<Individual> tempList = new ArrayList<Individual>();

		tempList.add(childs[1].cloned());
		tempList.add(childs[0].cloned());
		tempList.add(cpyI1);
		tempList.add(cpyI2);
		Collections.sort(tempList, fitnessComparator);
		currentPopulation.add(tempList.get(0)); tempList.remove(0);
		currentPopulation.add(tempList.get(0)); tempList.remove(0);
	}


	/**
	 * Insertion des deux enfants generés dans la population et
	 * remplacement des individus les plus agés
	 */
	public void childrenInsertion2(){
		Collections.sort(currentPopulation, ageComparator);

		int n =  currentPopulation.size();
		Individual cpyI1 = currentPopulation.get(n-1).cloned();
		Individual cpyI2 = currentPopulation.get(n-2).cloned();
		currentPopulation.remove(n-1);
		currentPopulation.remove(n-2);
		ArrayList<Individual> tempList = new ArrayList<Individual>();

		tempList.add(childs[1].cloned());
		tempList.add(childs[0].cloned());
		tempList.add(cpyI1);
		tempList.add(cpyI2);
		Collections.sort(tempList, fitnessComparator);
		currentPopulation.add(tempList.get(0)); tempList.remove(0);
		currentPopulation.add(tempList.get(0)); tempList.remove(0);

	}

	/**
	 * Selection des deux meilleurs parents
	 */
	public void bestSelection(){
		Collections.sort(currentPopulation,  this.fitnessComparator);

		ArrayList<Individual> bestIndividuals = new ArrayList<Individual>();

		double bestFitness = (Double)currentPopulation.get(0).getFitness();
		int i = 0;
		boolean ok =true;
		while(i<currentPopulation.size() && ok){
			if ((Double) currentPopulation.get(i).getFitness()==bestFitness){
				bestIndividuals.add(currentPopulation.get(i));
			}
			else{
				ok= false;
			}

			i++;
		}


		int counter = bestIndividuals.size();
		if(counter > 2){
			Random rand = new Random();
			ArrayList<Integer> l = new ArrayList<Integer>();
			for(int j = 0; j<2; j++){
				int  id = rand.nextInt(counter-1);
				while(l.contains(id)){
					id = rand.nextInt(counter-1);
				}
				l.add(id);
				parents[j] = bestIndividuals.get(id).cloned();
			}

		}else{
			parents[0] = currentPopulation.get(0).cloned();
			parents[1] = currentPopulation.get(1).cloned();
		}


	}

	/**
	 * Selection des individus par tournoi
	 * @param T :  nombre d 'individus selectionnés T € [2,populationSize]
	 */
	public void tournamentSelection(int T){
		Random rand = new Random();
		if( currentPopulation.size() < T ){
			bestSelection();
		}
			else{
			ArrayList<Individual> selectionned = new ArrayList<Individual>();
			ArrayList<Integer> l = new ArrayList<Integer>();

			for(int i = 0; i <T; i++){
				int  id = rand.nextInt(currentPopulation.size());
				while(l.contains(id)){
					id = rand.nextInt(currentPopulation.size());
				}
				l.add(id);
				selectionned.add(currentPopulation.get(id).cloned());
			}

			Collections.sort(selectionned, this.fitnessComparator);

			parents[0] = selectionned.get(0).cloned();
			parents[1] = selectionned.get(1).cloned();
		}

	}


	/**
	 * Selection aleatoire de deux parents
	 */
	public void randomSelection(){
		Random rand = new Random();
		int  i1 = rand.nextInt(currentPopulation.size()-1);
		int i2 = rand.nextInt(currentPopulation.size()-1);
		while(i1==i2){
			i2 = rand.nextInt(currentPopulation.size()-1);
		}
		parents[0] = currentPopulation.get(i1).cloned();
		parents[1] = currentPopulation.get(i2).cloned();
	}

	/**
	 * Retourne le meilleure fitness de la population
	 */
	public Double bestFitness(){
		Collections.sort(currentPopulation,this.fitnessComparator );
		/*double bestValue = (Double) currentPopulation.get(0).getFitness();
		for(int i=0 ; i <currentPopulation.size() ; i++){
			Individual ind = currentPopulation.get(i);
			Double fitness = (Double) ind.getFitness();
			if(fitness < bestValue){
				bestValue = fitness;
			}
		}*/
		return (Double) currentPopulation.get(0).getFitness();
	}


	/**
	 * Affiche du meilleur fitness en fonction du nombre d'iterations
	 */
	public void printPerformance(){
		System.out.println("#iterations fitness");
		for(int i =0; i<performance.length; i++){
			System.out.println(i+" "+performance[i]);
		}
	}




	/**
	 * Lancement de la recherche de solution (Steady State)
	 */
	public Double[] run(int selection, int crossover, int mutation, int insertion){
		int stepCounter = 0;

		// 1 - Initilisation
		initialization();

		// 2 - Evalutation
		for(int i = 0 ; i< iterMax ; i++) {
			stepCounter = i;
				performance[stepCounter] = bestFitness();

				//-3 Selection ( selection de 2 parents)
				selectionApplication(selection);

				//- 4 Croisement

				if (probableChoice(crossoverProba)){

					this.crossoverApplication(crossover);
				}
				else{
					childs[0]=  parents[0].cloned();
					childs[1]= parents[1].cloned();
				}
				childs[0].setGeneration(stepCounter+1);
				childs[1].setGeneration(stepCounter+1);

				//- 5 Mutation ( des deux enfants)
				this.mutationApplication(mutation);

				//-6 Insertion
				insertionApplication(insertion);
		}


		return performance;
	}


	/**
	 * Recherche de solution avec gestion automatique du choix de l'opérateur de mutation: Roulette Adaptative
	 */
	public Double[] runByAdaptativeWheel(int selection, int crossover, int insertion){
		int stepCounter = 0;

		// 1 - Initilisation
		initialization();

		// 2 - Evalutation
		for(int i = 0 ; i< iterMax ; i++) {
			stepCounter = i;
			if(!hasBestIndividual()) {
				performance[stepCounter] = bestFitness();

				//-3 Selection ( Tri et selection des 2 meilleurs parents)
				selectionApplication(selection);

				//- 4 Croisement

				if (probableChoice(crossoverProba)){

					this.crossoverApplication(crossover);
				}
				else{

					childs[0] = parents[0].cloned();  new Solution(problemSize,distances);
					childs[1] = parents[1].cloned(); new Solution(problemSize,distances);
				}
				childs[0].setGeneration(stepCounter+1);
				childs[1].setGeneration(stepCounter+1);

				//- 5 Mutation ( des deux enfants)
				if (probableChoice(mutationProba)){
					int choice = mutations.operatorChoice();// choix de l'operateur  et application de la  mutation
					double improvement = 0;

					// Before mutation
					Double fitnessBeforeMutatation0 = (Double) childs[0].getFitness();
					Double fitnessBeforeMutatation1 = (Double) childs[1].getFitness();
					// After Mutation
					int[] afterMutation0 = mutations.operatorApplication(choice, childs[0]);
					int[] afterMutation1 = mutations.operatorApplication(choice, childs[1]);
					childs[0].setRepresentation(afterMutation0);
					childs[1].setRepresentation(afterMutation1);
					Double fitnessAfterMutatation0 = (Double) childs[0].getFitness();
					Double fitnessAfterMutatation1 = (Double) childs[1].getFitness();

					// mise à jour des ameliorations
					improvement += computeImprovement(fitnessBeforeMutatation0,fitnessAfterMutatation0);

					if(improvement<0){
						improvement=0;
					}

					improvement += computeImprovement(fitnessBeforeMutatation1,fitnessAfterMutatation1);
					if(improvement>0){
						mutations.addImprovment(choice, (int) improvement,stepCounter);
					}
					else{
						mutations.addImprovment(choice, 0,stepCounter);
					}

				}

				//-6 Insertion

				insertionApplication(insertion);
			}else {
				performance[stepCounter] = bestFitness();
				mutations.updateUtilities();
				mutations.updateProbabilites(stepCounter);
			}
		}
		return performance;

	}


	/**
	 * Effectue un choix aleatoire en fonction d'une proabilite
	 * donnée
	 * @param probalility : probabilite du choix
	 * @return
	 */
	public boolean probableChoice(double probalility){
		Random rand = new Random();
		int  n = rand.nextInt(1000) + 1;
		return n <= probalility*1000;
	}


	/**
	 * Choix aleatoire d'un entier correspondant à l'indice
	 * d'un bit dans la representation
	 * @return
	 */
	public int randomChoice(){
		Random rand = new Random();
		int  n = rand.nextInt(problemSize-1) + 1;
		return n;
	}

	/**
	 * Application d'une methode de selection
	 * @param type
	 */
	public void selectionApplication(int type){
		switch(type){
			case 1 :
				bestSelection();
				break;
			case 2:
				randomSelection();
				break;
			case 3:
				tournamentSelection(5);
				break;
			default:
				bestSelection();
				break;
			}
	}

	/**
	 * Application d'un methode de croisement
	 * @param type
	 */
	public void crossoverApplication(int type){
		switch(type){
			case 1:
				childs = this.order1Crossover(parents[0],parents[1]);
				break;
			case 2:
				childs = this.pmxCrossover(parents[0],parents[1]);
				break;
			case 3:
				childs = this.cycleCrossover(parents[0],parents[1]);
				break;
			default:
				childs = this.order1Crossover(parents[0],parents[1]);
				break;
		}
	}

	/**
	 * Application d'une methode par selection adaptative
	 * @param type
	 */
	public void adaptiveWheelMutationApplication(int type){
		improvement = 0;
		if(type==0){
			int[] afterMutation1 = this.swapMutation(childs[0]);
			//improvement += computeImprovement(childs[0].getRepresentation(),afterMutation1);
			childs[0].setRepresentation(afterMutation1);

			int[] afterMutation2 = this.swapMutation(childs[1]);
			//improvement += computeImprovement(childs[1].getRepresentation(),afterMutation2);
			childs[1].setRepresentation(afterMutation2);

		}
		else{
			int[] afterMutation1 = this.swapMutation(childs[0]);
			//improvement += computeImprovement(childs[0].getRepresentation(),afterMutation1);
			childs[0].setRepresentation(afterMutation1);

			int[] afterMutation2 = swapMutation(childs[1]);
			//improvement += computeImprovement(childs[0].getRepresentation(),afterMutation2);
			childs[1].setRepresentation(afterMutation2);
		}
	}

	/**
	 * Application d'une methode de mutation
	 * @param type
	 */
	public void mutationApplication(int type){
		switch(type){
			case 1:
				childs[0].setRepresentation(this.swapMutation(childs[0]));
				childs[1].setRepresentation(this.swapMutation(childs[1]));
				break;
			case 2:
				childs[0].setRepresentation(this.insertMutation(childs[0]));
				childs[1].setRepresentation(this.insertMutation(childs[1]));
				break;
			case 3:
				childs[0].setRepresentation(this.inversionMutation(childs[0]));
				childs[1].setRepresentation(this.inversionMutation(childs[1]));
			case 4:
				childs[0].setRepresentation(this.scrambleMutation(childs[0]));
				childs[1].setRepresentation(this.scrambleMutation(childs[1]));

			default:
				childs[0].setRepresentation(this.swapMutation(childs[0]));
				childs[1].setRepresentation(this.swapMutation(childs[1]));
				break;
		}

	}

	/**
	 * Application d'une methode d'insertion
	 * @param type
	 */
	public void insertionApplication(int type){
		switch(type){
			case 1:
				childrenInsertion1(); // ( Fiteness: remplacement des deux moins bons)
				break;
			case 2:
				childrenInsertion2(); // ( generation: remplacement des deux plus agés)
				break;
			default:
				childrenInsertion1();
				break;

		}
	}


	/*
	 * Compte le nombre de bit à 1
	 */
	public int onesCounter(int[] rep){
		int result = 0;
		for(int i = 0; i<rep.length; i++){
			result+=rep[i];
		}
		return result;
	}

	Double computeImprovement(double fitessValueBeforeMutation, double fitessValueAfterMutation){
		return fitessValueBeforeMutation-fitessValueAfterMutation;
	}

	/**
	 * Calcule de  la moyenne des resultats obtenus lors des exécutions
	 * @param executions : ensemble des resultats d'executions
	 * @param iterMax : nombre maximum d'iterations
	 * @return
	 */
	 public static double[] average(ArrayList<Double[]> executions, int iterMax) {
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
	 public static void printArray(double[] array){
		 System.out.println("#iterations fitness");
		 for (int i=0; i< array.length; i++) {
			 System.out.println(i+" "+array[i]);
		 }

	 }

	 /**
	  * Evolutation de la population lors en seul itération
	  */
	 public void evolution(int selection, int crossover, int mutation, int insertion){
		//-3 Selection ( Tri et selection des 2 meilleurs parents)
			selectionApplication(selection);

			//- 4 Croisement
			if (probableChoice(crossoverProba)){

				this.crossoverApplication(crossover);
			}
			else{
				int[] representation1 = parents[0].getClonedRepresentation();
				int[] representation2 = parents[1].getClonedRepresentation();
				childs[0] = new Solution(problemSize,distances);
				childs[1] = new Solution(problemSize,distances);
				childs[0].setRepresentation(representation1);
				childs[1].setRepresentation(representation2);
			}
			iteration+=1;
			childs[0].setGeneration(iteration);
			childs[1].setGeneration(iteration);

			//- 5 Mutation ( des deux enfants)
			this.mutationApplication(mutation);

			//-6 Insertion

			insertionApplication(insertion);

	 }

	 /**
	  * Recherche d'un individu de meilleur qualité
	  */
	 public Individual getBestIndividual(){
		 Collections.sort(currentPopulation, fitnessComparator);

			ArrayList<Individual> bestIndividuals = new ArrayList<Individual>();

			double bestFitness = (Double) currentPopulation.get(0).getFitness();
			int i = 0;
			boolean ok =true;
			while(i<currentPopulation.size() && ok){
				if ((Double)currentPopulation.get(i).getFitness()==bestFitness){
					bestIndividuals.add(currentPopulation.get(i));
				}
				else{
					ok= false;
				}

				i++;
			}

			int counter = bestIndividuals.size();

			if(counter > 1){
				Random rand = new Random();
				ArrayList<Integer> l = new ArrayList<Integer>();
				int  id = rand.nextInt(counter-1);
				return bestIndividuals.get(id);

			}else{
				return currentPopulation.get(0);
			}

	 }

	 /**
	  * Ajout d'un individu dans la population
	  * @param id
	  */
	 public void addIndividual(Individual id){
		 currentPopulation.add(id);
	 }

	 /**
	  * Suppression d'un individu de la population
	  * @param ind : indice/position de l'individu
	  */
	 public  void removeIndividual(int position){
		 currentPopulation.remove(position);
	 }


	 /**
	  * Suppression d'un individu de la population
	  * @param id : Individu
	  */
	 public  void removeIndividual(Individual id){
		 currentPopulation.remove(id);
	 }



	 public  double[][] getChoiceProba(){
		 double[][] res = new double[mutations.getNbOperator()][this.iterMax];
		 for(int i=0; i< mutations.getNbOperator(); i++){
			 res[i] = mutations.getOperator(i).getProbabilites();
		 }

		 return res;
	 }

	 public static void printDataInFile(String outputFile, double[] data) throws IOException{
		 BufferedWriter fSortie;
		 fSortie = new BufferedWriter(new FileWriter(outputFile));

		 fSortie.write("#iterations probabilites");fSortie.newLine();
		 for(int i = 0; i<data.length ; i++){
			 fSortie.write(i+" "+data[i]); fSortie.newLine();

		 }
		 fSortie.close();
	 }

	 /**
	  * Calcul de la moyenne des probas d'un opérateurs sur l'ensemble des exécutions
	  * @param choicesProba : Ensemble des données recoltés sur l'ensemble des exécutions
	  * @param operatorID :  Id de l'opérateur
	  * @param iter : nombre max d'itérations
	  * @return
	  */
	 public static double[] operatorAverageProba(ArrayList<double[][]> choicesProba, int operatorID, int iter){
		 int n = choicesProba.size();
		 double[] average = new double[iter];
		 double[][] operatorAverage = new double[n][iter];
		 for(int i=0; i< n ; i++){
			 operatorAverage[i] = choicesProba.get(i)[operatorID];
		 }
		 for( int i=0; i< iter ; i++){
			 double cpt = 0 ;
			 for( int k=0; k< n ; k++){

				 cpt+=  operatorAverage[k][i];
			 }
			 average[i] = (1.)*cpt/n;
		 }

		 return average;
	 }


	 public static void outPutAllAverage(ArrayList<double[][]> choicesProba , int iter, int n) throws IOException{

		 for(int i=0 ; i< n; i++){
			 printDataInFile("../tsp_results/operator_"+i+".dat", operatorAverageProba(choicesProba,i,iter));
		 }
	 }

	 /**
	  * Application de l'operateur de mutation sur l'ensemble des individus
	  * @param type: opératteur de mutation à utiliser
	  */
	 public void overallMutationApplication(int type){
		 updateFitnessBeforeEvolution();

		 for(int i =0; i<currentPopulation.size();i++){
			 currentPopulation.get(i).setRepresentation(mutations.operatorApplication(type, currentPopulation.get(i)));
		 }
		 updateFitnessAfterEvolution();

		 // mise à jour des améliorations
		 computeImprovements();
	 }


	 public void islandEvolution(int selection, int crossover, int mutation, int insertion, int generation){

		 int[] popChoosen = new int[nbPop];
		 this.improvements = new int[nbPop];
		 double[] fitnessBefore= new double[2];
		 double[] fitnessAfter= new double[2];
		 improvement = 0;
		 //Mutation

		 if(this.currentPopulation.size()>=2){
			//-3 Selection ( Tri et selection des 2 meilleurs parents)
			selectionApplication(selection);
			popChoosen[parents[0].getIdPopulation()]=1;
			popChoosen[parents[1].getIdPopulation()]=1;

			//- 4 Croisement
			if (probableChoice(crossoverProba)){

				this.crossoverApplication(crossover);
			}
			else{
				childs[0] = parents[0].cloned();
				childs[1] = parents[1].cloned();
			}
			childs[0].setGeneration(generation);
			childs[1].setGeneration(generation);

			//- 5 Mutation ( des deux enfants)
			// choix de l'operateur  et application de la  mutation
			if (probableChoice(mutationProba)){
				fitnessBefore[0]= (Double)childs[0].getFitness(); fitnessBefore[1]= (Double)childs[1].getFitness();
				childs[0].setRepresentation(mutations.operatorApplication(mutation, childs[0]));
				childs[1].setRepresentation(mutations.operatorApplication(mutation, childs[1]));
				fitnessAfter[0]= (Double) childs[0].getFitness(); fitnessAfter[1]= (Double) childs[1].getFitness();

				improvement +=  (fitnessAfter[0] - fitnessBefore[0]);
				if(improvement<0){
					improvement=0;
				}
				improvement +=  (fitnessAfter[1] - fitnessBefore[1]);

				// mise à jour des amelioration
				if(improvement>0){
					for(int i=0; i<this.nbPop; i++){
						 if(popChoosen[i]==1){
							 this.improvements[i] = improvement;
						 }
					}
				}

			}

			//-6 Insertion
			insertionApplication(insertion);
		 }
		// System.out.println("amelioration op="+mutation);
		// this.testPrintTable(improvements);

	 }


	 /**
	  * Mise à jour et recupération des fitness et des population d'origine
	  * Avavant l'application de l'evolutuion
	  */
	public void updateFitnessBeforeEvolution(){
		 fitnessBeforeEvolution = new double[2][currentPopulation.size()];
		 for(int i =0; i<currentPopulation.size();i++){
			 fitnessBeforeEvolution[0][i]= currentPopulation.get(i).getIdPopulation();
			 fitnessBeforeEvolution[1][i]= (Double) currentPopulation.get(i).getFitness();
		 }

	}


	  /**
	  * Mise à jour et recupération des fitness et des population d'origine
	  * Avavant l'application de l'evolutuion
	  */
	 public void updateFitnessAfterEvolution(){
		 fitnessAfterEvolution = new double[2][currentPopulation.size()];
		 for(int i =0; i<currentPopulation.size();i++){
			 fitnessAfterEvolution[0][i]= currentPopulation.get(i).getIdPopulation();
			 fitnessAfterEvolution[1][i]= (Double) currentPopulation.get(i).getFitness();
		 }

	 }



	 /**
	  * Met à jour l'etat d'amelioration des individus
	  */
	 public  void improvedIndividual(){


	 }

	 /**
	  * Mise à jour de l'ensemble des populations présente
	  */
	 public void updateOriginOfIndividuals(){
		 originOfIndividuals = new ArrayList<Integer>();
		 for(int i =0; i<currentPopulation.size();i++){
			 if(!this.originOfIndividuals.contains(currentPopulation.get(i).getIdPopulation())){
				 this.originOfIndividuals.add(currentPopulation.get(i).getIdPopulation());
			}
		 }

	 }


	 /**
	  * Calcul de l'ensemble des amélorations des individus provenants de chaque iles
	  */
	 public void computeImprovements(){
		 this.improvements = new int[nbPop];
		 this.averageImprovements = new double[nbPop];
		 int[] originIslandRepartition = new int[nbPop];
		 int originIsland;
		 for(int k=0; k<currentPopulation.size(); k++){
			 double f1 = fitnessBeforeEvolution[1][k];
			 double f2 = fitnessAfterEvolution[1][k];
			 originIsland = currentPopulation.get(k).getIdPopulation();
			 originIslandRepartition[originIsland]+=1;
			 this.improvements[originIsland]+= f2-f1;
		 }

		 for(int k=0; k<nbPop; k++){
			 if(originIslandRepartition[k]>0){
				this.averageImprovements[k]=(this.improvements[k]*(1.))/originIslandRepartition[k];
			 }
		 }
	 }

	 /**
	  * Recuperation de la liste des
	  * @return
	  */
	 public double[] getAverageImprovements(){
		 return this.averageImprovements;
	 }


	public void initArray(double[] t){
		for(int k=0; k<t.length; k++){
			t[k]=0*(1.);
		}
	}

	public int[] getImprovements() {
		return improvements;
	}

	public void testPrintTable(int [] t){
		System.out.print("[");
		for(int i =0 ; i< t.length ; i++){
			System.out.print(t[i]+",");
		}
		System.out.println("]");
	}


	public static void main(String args[]) throws IOException{
		ArrayList<Double[]> executions = new  ArrayList<Double[]>();
		ArrayList<double[][]> choicesProba = new  ArrayList<double[][]>();
		if(args.length==9){
			int selectionType = Integer.parseInt(args[0]);
			int crossoverType = Integer.parseInt(args[1]);
			int mutationType = Integer.parseInt(args[2]);
			int insertionType = Integer.parseInt(args[3]);
			double pc = Double.parseDouble(args[4]);
			double pm = Double.parseDouble(args[5]);
			int max = Integer.parseInt(args[6]);
			int tests = Integer.parseInt(args[7]);
			int popupaltionSize = Integer.parseInt(args[8]);
			Population s = new TspPopulation(0,popupaltionSize,2,pc,pm,max);
			if(mutationType>=0) {
				for (int i = 0; i< tests; i++) {
					 s = new TspPopulation(popupaltionSize,2,pc,pm,max);
					 executions.add((Double[]) s.run(selectionType, crossoverType, mutationType, insertionType));
				}
				double[] average = average(executions,max);
				printArray(average);
			}
			else {
				for (int i = 0; i< tests; i++) {
					 s = new TspPopulation(popupaltionSize,2,pc,pm,max);

					 executions.add((Double[]) s.runByAdaptativeWheel(selectionType, crossoverType, insertionType));
					 choicesProba.add(s.getChoiceProba());

				}
				double[] average = average(executions,max);
				outPutAllAverage(choicesProba,max,4);
				printArray(average);
			}

		}

	}

}
