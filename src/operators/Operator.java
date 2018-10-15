package operators;

import ga_solver.Individual;

import java.util.ArrayList;


public class Operator {
	

	
	int utility;
	double probability;
	ArrayList<Integer> last_improvments;
	int last_selections_number;
	
	OperatorMutation op;
	
	/**
	 * 
	 * @param nb : le nombre d'operateur
	 */
	public Operator(OperatorMutation type){
		//this.last_selections_number = n;
		op = type;
	}
	
	public int getLast_selections_number() {
		return last_selections_number;
	}

	public void setLast_selections_number(int last_selections_number) {
		this.last_selections_number = last_selections_number;
	}

	public int getUtility() {
		return utility;
	}
	
	public void setUtility(int utility) {
		this.utility = utility;
	}
	
	public double getProbability() {
		return probability;
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	public ArrayList<Integer> getLast_improvments() {
		return last_improvments;
	}
	
	public void setLast_improvments(ArrayList<Integer> last_improvments) {
		this.last_improvments = last_improvments;
	}
	
	/**
	 * Application de l'operation de variation sur l'individu
	 * @param id
	 * @return
	 */
	public int[] execution(Individual id){
		
		return op.mutationApplication(id);
	}
	
	/**
	 * Affichage de l'utilite
	 */
	public void printUtility(){
		System.out.print(utility);
	}
	
	/**
	 * Affichage de la probabilité
	 */
	public void printProbabiliy(){
		System.out.print(probability);
	}
	

	
	

}
