package ga_solver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Cette classe represente le solveur du l'algorithme genetique et
 * ses composantes
 * @author boka
 *
 */
public class Solver {
	
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
	private ArrayList<int[]> performance;
	
	public Solver( int problemSize, int popSize , int childs, double pc, double pm, int iterMax){
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
		performance = new ArrayList<int[]>();
	}
	
	/**
	 * @param nbParents : le nombre d'individus à selectionnés
	 * à l'initialization
	 */
	public void initialization( int nbParents){
		for(int i = 0; i<nbParents ;i++){
			Individual  ind = new Individual(problemSize);
			ind.setGeneration(0);
			population.add(ind);
			
		}
		currentPopulation = population;
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
	public boolean hasBestIndividual(ArrayList<Individual> set){
		boolean isOk = false;
		int i = 0;
		while(i < set.size() && isOk==false){
			Individual ind = set.get(i);
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
		Individual[] childs = new Individual[2];
		
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
		
		childs[0] = children1;
		childs[1] = children2;
		
		return childs;
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
		
		Individual[] childs = new Individual[2];
		
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
		
		childs[0] = children1;
		childs[1] = children2;
		
		return childs;
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
		if(childs[0].getFitness()>currentPopulation.get(size-1).getFitness()){
			currentPopulation.remove(size-1);
			currentPopulation.add(childs[0]);
		}
		Collections.sort(currentPopulation, Individual.IndividualFintessComparator);
		
		size = currentPopulation.size();
		if(childs[1].getFitness()>currentPopulation.get(size-1).getFitness()){
			currentPopulation.remove(size-1);
			currentPopulation.add(childs[1]);
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
		Collections.sort(currentPopulation, Individual.IndividualAgeComparator);
		currentPopulation.remove(populationSize-1);
		currentPopulation.remove(populationSize-2);
		currentPopulation.add(childs[0]);
		currentPopulation.add(childs[1]);
		
	}
	
	/**
	 * Selection des deux meilleur parents
	 */
	public void bestSelection(){
		Collections.sort(currentPopulation, Individual.IndividualFintessComparator);
		
		
		ArrayList<Individual> bestIndividuals = new ArrayList<Individual>();
		
		int bestFitness = currentPopulation.get(0).getFitness();
		int i =0;
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
				parents[j] = bestIndividuals.get(id);
			}
			
		}else{
			parents[0] = currentPopulation.get(0); //parents[0].print();
			parents[1] = currentPopulation.get(1); //parents[1].print();
		}
		
		
	}
	
	/**
	 * Selection des individus par tournoi
	 * @param T :  nombre d 'individus selectionnés T € [2,populationSize]
	 */
	public void tournamentSelection(int T){
		Random rand = new Random();
		ArrayList<Individual> selectionned = new ArrayList<Individual>();
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		for(int i = 0; i <T; i++){
			int  id = rand.nextInt(populationSize-1);
			while(l.contains(id)){
				id = rand.nextInt(populationSize-1);
			}
			l.add(id);
			selectionned.add(currentPopulation.get(id));
		}
		
		Collections.sort(selectionned, Individual.IndividualFintessComparator);
		
		parents[0] = selectionned.get(0);
		parents[1] = selectionned.get(1);
		
	}
	
	
	/**
	 * Selection aleatoire de deux parents
	 */
	public void randomSelection(){
		Random rand = new Random();
		int  i1 = rand.nextInt(populationSize-1);
		int i2 = rand.nextInt(populationSize-1);
		while(i1==i2){
			i2 = rand.nextInt(populationSize-1);
		}
		parents[0] = currentPopulation.get(i1);
		parents[1] = currentPopulation.get(i2);
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
		for(int i =0; i<performance.size(); i++){
			System.out.println(performance.get(i)[0]+" "+performance.get(i)[1]);
		}
	}
	
	
	/**
	 * Lancement de la recherche de solution
	 */
	public void run(){
		int stepCounter = 0;
		boolean isOk = false;
		
		// 1 - Initilisation
		initialization(populationSize);

		// 2 - Evalutation
		while(!hasBestIndividual(currentPopulation) && stepCounter < iterMax ){
			int[] perf = new int[2];
			perf[0] = stepCounter;
			perf[1] = bestFitness();
			performance.add(perf);
			
			//-3 Selection ( Tri et selection des 2 meilleurs parents)
			bestSelection();
			//tournamentSelection(5);
			
			//- 4 Croisement
			
			if (probableChoice(crossoverProba)){
				
				//childs = monoPointCrossOver(parents[0],parents[1]);
				childs = uniformCrossover(parents[0],parents[1]);


			}
			else{
				int[] representation1 = parents[0].getClonedRepresentation();
				int[] representation2 = parents[1].getClonedRepresentation();
				childs[0] = new Individual(problemSize);
				childs[1] = new Individual(problemSize);
				childs[0].setRepresentation(representation1);
				childs[1].setRepresentation(representation2);
			}
			childs[0].setGeneration(stepCounter);
			childs[1].setGeneration(stepCounter);
			
			//- 5 Mutation ( des deux enfants)
			childs[0].setRepresentation(mutationBitFlip(childs[0]));
			childs[1].setRepresentation(mutationBitFlip(childs[1]));
			
			//-6 Insertion 
			//childrenInsertion1(); // ( Fiteness: remplacement des deux moins bons)
			childrenInsertion2();  // ( generation: remplacement des deux plus agés)
			stepCounter++;
		}			
		
		printPerformance();
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
	
	
	/*public double nRunAverage(int n){
		int total =0;
		for( int i= 0; i< n; i++){
			total = total+run();
		}
		
		return ((total*(1.)/n));
	}*/
		
	public static void main(String args[]){
		
		Solver s = new Solver(100,20,2,0.6,0.5,1000);
		s.run();
	}

}
