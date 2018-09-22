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
	}
	
	/**
	 * @param nbParents : le nombre d'individus à selectionnés
	 * à l'initialization
	 */
	public void initialization( int nbParents){
		for(int i = 0; i<nbParents ;i++){
			Individual  ind = new Individual(problemSize);
			population.add(ind);
			
		}
		currentPopulation = population;
		/*Individual e = new Individual(problemSize);
		e.changeValue(0, 0);
		currentPopulation.add(e);*/
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
		//System.out.println("--Point de croisement:"+crossoverPoint);
		
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
	 * Mutations de deux individus
	 * @param childs
	 */
	public int[] mutation1Flips(Individual child){
		int[] representation = child.getClonedRepresentation();
		if(probableChoice(mutationProba)){
			
			int randomIndice = randomChoice();
			//System.out.print("Il y mutation sur le le bt:"+randomIndice);
			//System.out.print("(");
			//System.out.print('[');
			/*for(int i = 0; i<representation.length; i++){
				System.out.print(representation[i]);
			}
			System.out.println(']');*/
			if(representation[randomIndice] == 0){
				representation[randomIndice]=1;
			}
			else{
				representation[randomIndice]=0;
			}
		}
		//System.out.print("mutation performed");
		return representation;
	}
	
	/**
	 * Insertion des deux enfants generés dans la population
	 * 
	 */
	public void childrenInsertion(){
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
		
		parents[0] = currentPopulation.get(0); //parents[0].print();
		parents[1] = currentPopulation.get(1); //parents[1].print();
	}
	
	/**
	 * Retourne le meilleure fitness de la population
	 */
	public int bestFitness(){
		int bestValue = 0;
		for(int i=0 ; i <currentPopulation.size(); i++){
			Individual ind = currentPopulation.get(i);
			int fitness=ind.getFitness();
			if(fitness>bestValue){
				bestValue = fitness;
			}
		}
		return bestValue;
	}
	
	
	
	
	/**
	 * Lancement de la recherche de solution
	 */
	public void run(){
		int stepCounter = 0;
		boolean isOk = false;
		
		// 1 - Initilisation
		initialization(populationSize);
		//printSetOfIndividuals(currentPopulation);
		
		
		
		// 2 - Evalutation
		//printSetOfIndividuals(currentPopulation);
		while(!hasBestIndividual(currentPopulation) && stepCounter < iterMax ){
			
			//-3 Selection ( Tri et selection des 2 meilleurs parents)
			bestSelection();
			
			/*System.out.println(stepCounter+"======================================");
			System.out.print("-----Population actuelle : ");
			printSetOfIndividuals(currentPopulation);
			
			System.out.println("parents selectionnés");*/
			//Collections.sort(currentPopulation, Individual.IndividualFintessComparator);
			
			//parents[0] = currentPopulation.get(0); //parents[0].print();
			//parents[1] = currentPopulation.get(1); //parents[1].print();
			//System.out.println();
			//- 4 Croisement
			
			if (probableChoice(crossoverProba)){
				
				childs = monoPointCrossOver(parents[0],parents[1]);

			}
			else{
				int[] representation1 = parents[0].getClonedRepresentation();
				int[] representation2 = parents[1].getClonedRepresentation();
				childs[0] = new Individual(problemSize);
				childs[1] = new Individual(problemSize);
				childs[0].setRepresentation(representation1);
				childs[0].setRepresentation(representation2);
			}
			/*System.out.println("Enfants après croissement:");
			childs[0].print();
			childs[1].print();
			System.out.println();*/
			
			//- 5 Mutation ( des deux enfants)
			childs[0].setRepresentation(mutation1Flips(childs[0]));
			childs[1].setRepresentation(mutation1Flips(childs[1]));
			
			/*System.out.println("Mutations des enfants:");
			childs[0].print();
			childs[1].print();
			System.out.println();*/
			
			
			//System.out.println("-----Population arrivée");
			//printSetOfIndividuals(currentPopulation);
			
			//-6 Insertion ( Fiteness: remplacement des deux moins bons)
			childrenInsertion();
			/*System.out.println("-----Population après insertion: ");
			printSetOfIndividuals(currentPopulation);*/
			stepCounter++;
			//System.out.println(stepCounter);
		}			
		
		System.out.println("end of  algorithm...:"+bestFitness());

		

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
		//System.out.println(n);
		return n;
	}
	
		
	public static void main(String args[]){
		
		Solver s = new Solver(100,20,2,0.6,0.1,400);
		s.run();
		//s.probableChoice(0.6);
		//s.randomChoice();
		//s.initialization(2);
		//s.population=null;
		//s.printSetOfIndividuals(s.currentPopulation);
		
	}

}
