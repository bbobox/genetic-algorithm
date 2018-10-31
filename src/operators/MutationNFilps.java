package operators;

import ga_solver.Individual;

import java.util.ArrayList;
import java.util.Random;


public class MutationNFilps implements OperatorMutation{
	
	int problemSize;
	double mutationProba;
	int N;
	
	public MutationNFilps(int problemSize, double mutationProba, int N){
		this.problemSize = problemSize;
		this.mutationProba  = mutationProba;
		this.N = N;
		
	}

	public int[] mutationApplication(Individual child) {
		int[] representation = child.getClonedRepresentation();
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		//if(probableChoice(mutationProba)){
			
			for(int i = 0; i < N; i++){
				int id = randomChoice();
				while(l.contains(id)){
					id = randomChoice();
				}
				
				l.add(id);
			}
			
			for(int i = 0; i < l.size();i++){
				int idBit = l.get(i);
				if(representation[idBit] == 0){
					representation[idBit]=1;
				}
				else{
					representation[idBit]=0;
				}
			}
			
		//}
		
				
		return representation;
	}
	
	
	/**
	 * Effectue un choix aleatoire en fonction d'une proabilite donnée
	 * @param probalility : probabilite du choix
	 * @return 
	 */
	boolean probableChoice(double probalility){
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
	
	

}
