package operators;

import ga_solver.Individual;

import java.util.Random;


public class MutationBitFlip implements OperatorMutation {
	
	private int problemSize;
	private double mutationProba;
	
	/**
	 * 
	 * @param problemSize : taille de la structure d'un individu
	 * @param mutationProba : probabilité de mutation
	 */
	public MutationBitFlip(int problemSize, double mutationProba){
		this.problemSize = problemSize;
		this.mutationProba  = mutationProba;
		
	}

	public int[] mutationApplication(Individual child) {
		
		int[] representation = child.getClonedRepresentation();
		
		double proba = (1./problemSize);
		//if(probableChoice(mutationProba)){
		
			for(int i = 0; i < problemSize;i++){
				if( probableChoice(proba)){
					if(representation[i]==0){
						representation[i]=1;
					}else{
						representation[i]=0;
					}
				}
			}
		//}
	
		return representation;
	}
	
	/**
	 * Effectue un choix aleatoire en fonction d'une proabilite 
	 * donnée
	 * @param probalility : probabilite du choix
	 * @return 
	 */
	boolean probableChoice(double probalility){
		Random rand = new Random();
		int  n = rand.nextInt(1000) + 1;
		return n <= probalility*1000;
	}

	
	
	


}
