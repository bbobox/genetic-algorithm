package ga_solver;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import operators.MutationBitFlip;
import operators.MutationNFilps;
import operators.Operator;
import operators.OperatorMutation;
import operators.Operators;

/**
 * Cette classe represente le solveur du l'algorithme genetique et
 * ses composantes
 * @author boka
 *
 */
public class Population {

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
	private int[] performance;
	private Operators mutations;
	private int improvement;
	private  int iteration;

	private int[][] fitnessBeforeEvolution;
	private int[][] fitnessAfterEvolution;
	private ArrayList<Integer> originOfIndividuals; // ensemble d'origne des individus présent
	private int nbPop;

	private int[] improvements;
	public Population( int problemSize, int popSize , int childs, double pc, double pm, int iterMax){
		population = new ArrayList<Individual>();
		currentPopulation = new ArrayList<Individual>();
		this.problemSize = problemSize;
		populationSize = popSize;
		nbChilds = childs;
		crossoverProba = pc;
		mutationProba = pm;
		this.childs = new Individual[nbChilds];
		this.parents =  new Individual[nbChilds];
		this.iterMax = iterMax;
		performance = new int[iterMax];
		mutations = new Operators(4,25,this.iterMax);
		iteration = 0;
		nbPop = 1;
		improvements = new  int[nbPop];

		// instanciation et ajout des opérateurs d'operation;
		OperatorMutation mutationbitFilp = new MutationBitFlip(problemSize, mutationProba);
		OperatorMutation mutation1Filp = new MutationNFilps(problemSize, mutationProba,1);
		OperatorMutation mutation3Filp = new MutationNFilps(problemSize, mutationProba,3);
		OperatorMutation mutation5Filp = new MutationNFilps(problemSize, mutationProba,5);

		mutations.addOperator(new Operator(mutationbitFilp, this.iterMax));
		mutations.addOperator(new Operator(mutation1Filp,this.iterMax));
		mutations.addOperator(new Operator(mutation3Filp,this.iterMax));
		mutations.addOperator(new Operator(mutation5Filp,this.iterMax));
		//mutations.operatorInitial(3);

	}

	/**
	 * Modification du nombre total de population/iles (population voisines+1)
	 * @param nb
	 */
	public void setNbPop(int nb){
		nbPop = nb;
		improvements = new int[nbPop];
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
			Individual  ind = new Individual(problemSize);
			ind.setGeneration(0);
			population.add(ind);

		}
		currentPopulation = population;
	}

	/**
	 * Definition d el'ile d'appartenance de l'ensemble des individus de la population
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
	 * Affichage d'un sensemble d 'individus
	 */
	public void printSetOfIndividuals( ArrayList<Individual> set){
		for(int i = 0; i<set.size(); i++){
			set.get(i).print();
		}
		System.out.println();
	}

	/**
	 * Verifie les critrère de l'algortithme et renvoie
	 * 'true" si l'objectif est atteint
	 */
	public boolean hasBestIndividual(){
		boolean isOk = false;
		int i = 0;
		while(i < currentPopulation.size() && isOk==false){
			Individual ind = currentPopulation.get(i);
			if( ind.getFitness() == problemSize){
				isOk = true;
			}
			i++;
		}
		return isOk;
	}

	/**
	 * Effectue le croissement monopoint de deux individus parents
	 * @param i1
	 * @param i2
	 * @return les deux individus enfants generés
	 */
	public Individual[] monoPointCrossOver(Individual i1, Individual i2){
		Individual[] individuals = new Individual[2];

		int crossoverPoint = randomChoice();

		int[] representation1 = i1.getClonedRepresentation();
		int[] representation2 = i2.getClonedRepresentation();

		int[] crossover1 = representation1;
		for( int i = crossoverPoint ; i< problemSize; i++){
			crossover1[i] = representation2[i];
		}

		int[] crossover2 = representation2;
		for( int i = crossoverPoint ; i< problemSize; i++){
			crossover2[i] = representation1[i];
		}

		Individual children1 = new Individual(problemSize);
		Individual children2 = new Individual(problemSize);
		children1.setRepresentation(crossover1);
		children2.setRepresentation(crossover2);

		individuals[0] = children1;
		individuals[1] = children2;

		return individuals;
	}

	/**
	 *  Effectue le croisement uniforme de deux individus parents
	 */
	public Individual[] uniformCrossover(Individual i1, Individual i2){
		Random rand = new Random();

		int[] representation1 = i1.getClonedRepresentation();
		int[] representation2 = i2.getClonedRepresentation();

		int[] crossover1 = new int[problemSize];
		int[] crossover2 = new int[problemSize];

		Individual[] individuals = new Individual[2];

		int[] subset = new int[problemSize];
		for(int i =0; i<problemSize;i++){
			subset[i] = rand.nextInt(2)+1;
		}

		for( int i = 0 ; i< problemSize; i++){
			if(subset[i]==1){
				crossover1[i] = representation1[i];
			}
			else{
				crossover1[i] = representation2[i];
			}
		}

		for( int i = 0 ; i< problemSize; i++){
			if(subset[i]==1){
				crossover2[i] = representation2[i];
			}
			else{
				crossover2[i] = representation1[i];
			}
		}

		Individual children1 = new Individual(problemSize);
		Individual children2 = new Individual(problemSize);
		children1.setRepresentation(crossover1);
		children2.setRepresentation(crossover2);

		individuals[0] = children1;
		individuals[1] = children2;

		return individuals;
	}

	/**
	 * Mutations de deux individus :  1-flips
	 * @param child
	 */
	public int[] mutation1Flips(Individual child){
		int[] representation = child.getClonedRepresentation();
		if(probableChoice(mutationProba)){

			int randomIndice = randomChoice();
			if(representation[randomIndice] == 0){
				representation[randomIndice]=1;
			}
			else{
				representation[randomIndice]=0;
			}
		}
		return representation;
	}

	/**
	 * Mutation de l'individu : N-flips
	 * @param child
	 */
	public int[] mutationNFlips(Individual child, int N){
		int[] representation = child.getClonedRepresentation();
		ArrayList<Integer> l = new ArrayList<Integer>();

		if(probableChoice(mutationProba)){

			for(int i = 0; i < N; i++){
				int id = randomChoice();
				while(l.contains(id)){
					id = randomChoice();
				}

				l.add(id);
			}

			for(int i = 0; i<l.size();i++){
				int idBit = l.get(i);
				if(representation[idBit] == 0){
					representation[idBit]=1;
				}
				else{
					representation[idBit]=0;
				}
			}

		}


		return representation;
	}


	/**
	 * Mutation bit-flip d'un individu: Chaque bit à une probabilité d'etre modifié
	 * de ( 1/taille_individus)
	 * @param child
	 */
	public int[] mutationBitFlip( Individual child){
		int[] representation = child.getClonedRepresentation();

		double proba = (1./problemSize);
		if(probableChoice(mutationProba)){

			for(int i = 0; i < problemSize;i++){
				if( probableChoice(proba)){
					if(representation[i]==0){
						representation[i]=1;
					}else{
						representation[i]=0;
					}
				}
			}
		}

		return representation;
	}

	/**
	 * Insertion des deux enfants generés dans la population et
	 *  remplacement des individus les moins bons (fitness)
	 */
	public void childrenInsertion1(){
		Collections.sort(currentPopulation, Individual.IndividualFintessComparator);
		int size = currentPopulation.size();
		if(childs[0].getFitness()>=currentPopulation.get(size-1).getFitness()){
			currentPopulation.remove(size-1);
			currentPopulation.add(childs[0].cloned());
		}
		Collections.sort(currentPopulation, Individual.IndividualFintessComparator);

		size = currentPopulation.size();
		if(childs[1].getFitness()>=currentPopulation.get(size-1).getFitness()){
			currentPopulation.remove(size-1);
			currentPopulation.add(childs[1].cloned());
		}

		/*currentPopulation.remove(populationSize-1);
		currentPopulation.remove(populationSize-2);
		currentPopulation.add(childs[0]);
		currentPopulation.add(childs[1]);*/
	}

	/**
	 * Insertion des deux enfants generés dans la population et
	 * remplacement des individus les plus agés
	 */
	public void childrenInsertion2(){
		/*Collections.sort(currentPopulation, Individual.IndividualAgeComparator);
		currentPopulation.remove(populationSize-1);
		currentPopulation.remove(populationSize-2);
		currentPopulation.add(childs[0].cloned());
		currentPopulation.add(childs[1].cloned());*/

		Collections.sort(currentPopulation, Individual.IndividualAgeComparator);
		int size = currentPopulation.size();
		if(childs[0].getFitness()>=currentPopulation.get(size-1).getFitness()){
			currentPopulation.remove(size-1);
			currentPopulation.add(childs[0].cloned());


		}
		Collections.sort(currentPopulation, Individual.IndividualAgeComparator);
		size = currentPopulation.size();
		if(childs[1].getFitness()>=currentPopulation.get(size-1).getFitness()){
			currentPopulation.remove(size-1);
			currentPopulation.add(childs[1].cloned());

		}

	}

	/**
	 * Selection des deux meilleurs parents
	 */
	public void bestSelection(){
		Collections.sort(currentPopulation, Individual.IndividualFintessComparator);

		ArrayList<Individual> bestIndividuals = new ArrayList<Individual>();

		int bestFitness = currentPopulation.get(0).getFitness();
		int i = 0;
		boolean ok =true;
		while(i<currentPopulation.size() && ok){
			if (currentPopulation.get(i).getFitness()==bestFitness){
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
			parents[0] = currentPopulation.get(0).cloned(); //parents[0].print();
			parents[1] = currentPopulation.get(1).cloned(); //parents[1].print();
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

			Collections.sort(selectionned, Individual.IndividualFintessComparator);

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
	public int bestFitness(){
		int bestValue = 0;
		for(int i=0 ; i <currentPopulation.size() ; i++){
			Individual ind = currentPopulation.get(i);
			int fitness = ind.getFitness();
			if(fitness > bestValue){
				bestValue = fitness;
			}
		}
		return bestValue;
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
	public int[] run(int selection, int crossover, int mutation, int insertion){
		int stepCounter = 0;

		// 1 - Initilisation
		initialization();

		// 2 - Evalutation
		for(int i = 0 ; i< iterMax ; i++) {
			stepCounter = i;
			if(!hasBestIndividual()) {
				performance[stepCounter] = bestFitness();

				//-3 Selection ( selection de 2 parents)
				selectionApplication(selection);

				//- 4 Croisement

				if (probableChoice(crossoverProba)){

					this.crossoverApplication(crossover);
				}
				else{
					int[] representation1 = parents[0].getClonedRepresentation();
					int[] representation2 = parents[1].getClonedRepresentation();
					childs[0] = new Individual(problemSize);
					childs[1] = new Individual(problemSize);
					childs[0].setRepresentation(representation1);
					childs[1].setRepresentation(representation2);
				}
				childs[0].setGeneration(stepCounter+1);
				childs[1].setGeneration(stepCounter+1);

				//- 5 Mutation ( des deux enfants)
				this.mutationApplication(mutation);

				//-6 Insertion
				insertionApplication(insertion);
			}
			else {
				performance[stepCounter] = bestFitness();
			}

		}

		return performance;
	}


	/**
	 * Recherche de solution avec gestion automatique du choix de l'opérateur de mutation: Roulette Adaptative
	 */
	public int[] runByAdaptativeWheel(int selection, int crossover, int insertion){
		int stepCounter = 0;
		//boolean isOk = false;

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
					int[] representation1 = parents[0].getClonedRepresentation();
					int[] representation2 = parents[1].getClonedRepresentation();
					childs[0] = new Individual(problemSize);
					childs[1] = new Individual(problemSize);
					childs[0].setRepresentation(representation1);
					childs[1].setRepresentation(representation2);
				}
				childs[0].setGeneration(stepCounter+1);
				childs[1].setGeneration(stepCounter+1);

				//- 5 Mutation ( des deux enfants)
					// choix de l'operateur  et application de la  mutation
				if (probableChoice(mutationProba)){
					int choice = mutations.operatorChoice();
					improvement = 0;
					int[] afterMutation0 = mutations.operatorApplication(choice, childs[0]);
					int[] afterMutation1 = mutations.operatorApplication(choice, childs[1]);

					improvement += improvement(childs[0].getRepresentation(),afterMutation0);
					if(improvement<0){
						improvement=0;
					}
					childs[0].setRepresentation(afterMutation0);
					improvement += improvement(childs[1].getRepresentation(),afterMutation1);
					childs[1].setRepresentation(afterMutation1);
					// mise à jour des amelioration
					if(improvement>0){
						mutations.addImprovment(choice, improvement,stepCounter);
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
				childs = monoPointCrossOver(parents[0],parents[1]);
				break;
			case 2:
				childs = uniformCrossover(parents[0],parents[1]);
				break;
			default:
				childs = monoPointCrossOver(parents[0],parents[1]);
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
			int[] afterMutation1 = this.mutationBitFlip(childs[0]);
			improvement += improvement(childs[0].getRepresentation(),afterMutation1);
			childs[0].setRepresentation(afterMutation1);

			int[] afterMutation2 = this.mutationBitFlip(childs[1]);
			improvement += improvement(childs[1].getRepresentation(),afterMutation2);
			childs[1].setRepresentation(afterMutation2);

		}
		else{
			int[] afterMutation1 = mutationNFlips(childs[0],type);
			improvement += improvement(childs[0].getRepresentation(),afterMutation1);
			childs[0].setRepresentation(afterMutation1);

			int[] afterMutation2 = mutationNFlips(childs[1],type);
			improvement += improvement(childs[0].getRepresentation(),afterMutation2);
			childs[1].setRepresentation(afterMutation2);
		}
	}

	/**
	 * Application d'une methode de mutation
	 * @param type
	 */
	public void mutationApplication(int type){
		if(type==0){
			childs[0].setRepresentation(this.mutationBitFlip(childs[0]));
			childs[1].setRepresentation(this.mutationBitFlip(childs[1]));
		}
		else{
			childs[0].setRepresentation(mutationNFlips(childs[0],type));
			childs[1].setRepresentation(mutationNFlips(childs[1],type));
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
	int onesCounter(int[] rep){
		int result = 0;
		for(int i = 0; i<rep.length; i++){
			result+=rep[i];
		}
		return result;
	}

	int improvement(int[] rep1, int[] rep2){
		return onesCounter(rep2)-onesCounter(rep1);
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
				childs[0] = new Individual(problemSize);
				childs[1] = new Individual(problemSize);
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
		 Collections.sort(currentPopulation, Individual.IndividualFintessComparator);

			ArrayList<Individual> bestIndividuals = new ArrayList<Individual>();

			int bestFitness = currentPopulation.get(0).getFitness();
			int i = 0;
			boolean ok =true;
			while(i<currentPopulation.size() && ok){
				if (currentPopulation.get(i).getFitness()==bestFitness){
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

	 static void printDataInFile(String outputFile, double[] data) throws IOException{
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
	 static double[] operatorAverageProba(ArrayList<double[][]> choicesProba, int operatorID, int iter){
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


	 static  void outPutAllAverage(ArrayList<double[][]> choicesProba , int iter, int n) throws IOException{

		 for(int i=0 ; i< n; i++){
			 printDataInFile("../results/operator_"+i+".dat", operatorAverageProba(choicesProba,i,iter));
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
		 int[] fitnessBefore= new int[2];
		 int[] fitnessAfter= new int[2];
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
				improvement = 0;
				fitnessBefore[0]= childs[0].getFitness(); fitnessBefore[1]= childs[1].getFitness();
				childs[0].setRepresentation(mutations.operatorApplication(mutation, childs[0]));
				childs[1].setRepresentation(mutations.operatorApplication(mutation, childs[1]));
				fitnessAfter[0]= childs[0].getFitness(); fitnessAfter[1]= childs[1].getFitness();

				improvement +=  fitnessAfter[0] - fitnessBefore[0];
				if(improvement<0){
					improvement=0;
				}
				improvement += improvement +=  fitnessAfter[1] - fitnessBefore[1];

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


	 }


	 /**
	  * Mise à jour et recupération des fitness et des population d'origine
	  * Avavant l'application de l'evolutuion
	  */
	public void updateFitnessBeforeEvolution(){
		 fitnessBeforeEvolution = new int[2][currentPopulation.size()];
		 for(int i =0; i<currentPopulation.size();i++){
			 fitnessBeforeEvolution[0][i]= currentPopulation.get(i).getIdPopulation();
			 fitnessBeforeEvolution[1][i]= currentPopulation.get(i).getFitness();
		 }

	}


	  /**
	  * Mise à jour et recupération des fitness et des population d'origine
	  * Avavant l'application de l'evolutuion
	  */
	 public void updateFitnessAfterEvolution(){
		 fitnessAfterEvolution = new int[2][currentPopulation.size()];
		 for(int i =0; i<currentPopulation.size();i++){
			 fitnessAfterEvolution[0][i]= currentPopulation.get(i).getIdPopulation();
			 fitnessAfterEvolution[1][i]= currentPopulation.get(i).getFitness();
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
		 for(int k=0; k<currentPopulation.size(); k++){
			 int f1= fitnessBeforeEvolution[1][k];
			 int f2= fitnessAfterEvolution[1][k];
			 this.improvements[currentPopulation.get(k).getIdPopulation()]+= f2-f1;
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
		ArrayList<int[]> executions = new  ArrayList<int[]>();
		ArrayList<double[][]> choicesProba = new  ArrayList<double[][]>();
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
			Population s = new Population(size,popupaltionSize,2,pc,pm,max);
			if(mutationType>=0) {
				for (int i = 0; i< tests; i++) {
					 s = new Population(size,popupaltionSize,2,pc,pm,max);
					 executions.add(s.run(selectionType, crossoverType, mutationType, insertionType));
				}
				double[] average = average(executions,max);
				printArray(average);
			}
			else {
				for (int i = 0; i< tests; i++) {
					 s = new Population(size,popupaltionSize,2,pc,pm,max);

					 executions.add(s.runByAdaptativeWheel(selectionType, crossoverType, insertionType));
					 choicesProba.add(s.getChoiceProba());

				}
				double[] average = average(executions,max);
				outPutAllAverage(choicesProba,max,4);
				printArray(average);
			}


		}

	}

}
