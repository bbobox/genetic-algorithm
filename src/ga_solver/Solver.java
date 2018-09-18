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
	private ArrayList<Individual> currentPopulation;
	
	public Solver( int problemSize, int popSize , int childs){
		population = new ArrayList<Individual>();
		currentPopulation = new ArrayList<Individual>();
		this.problemSize = problemSize;
		populationSize = popSize;
		nbChilds = childs;
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
		Individual e = new Individual(problemSize);
		e.changeValue(0, 0);
		currentPopulation.add(e);
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
	 * Lancement de la recherche de solution
	 */
	public void run(){
		int stepCounter = 0;
		boolean isOk = false;
		
		// 1 - Initilisation
		initialization(populationSize);
		
		// 2 - Evalutation
		while(!hasBestIndividual(currentPopulation)){
			//-3 Selection
			//Collections.sort(currentPopulation, Individual.IndividualFintessComparator);
			
			//- 4 Croisement
			
			//- 5 Mutation
			
			//-6 Insertion
			

		}			
		
		System.out.println("end of  algorithm...");

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
		System.out.println(n);
		return n;
	}
	
		
	public static void main(String args[]){
		
		Solver s = new Solver(5,20,2);
		//s.run();
		//s.probableChoice(0.6);
		//s.randomChoice();
		//s.initialization(2);
		//s.population=null;
		//s.printSetOfIndividuals(s.currentPopulation);
		
	}

}
