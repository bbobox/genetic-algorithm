package operators;

import ga_solver.Individual;

import java.util.ArrayList;


public class Operator {



	int utility;
	double probability;
	ArrayList<Integer> last_improvments;
	int last_selections_number;

	OperatorMutation op;

	double[] probabilites;

	/**
	 * @param nb : le nombre d'operateur
	 */
	public Operator(OperatorMutation type, int itermax){
		op = type;
		probabilites = new double[itermax];
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


	/**
	 * Ajout de la probalité utiliser cet opérateur à l'iteration i
	 * @param proba
	 * @param id
	 */
	public void setProbaAtStep(double proba, int i){
		probabilites[i] = proba;
	}

	public double[] getProbabilites(){
		 return probabilites;
	}

	public void printProbabilities(){
		System.out.print("[");
		for(int i=0; i<probabilites.length; i++){
			System.out.print(probabilites[i]+",");

		}
		System.out.println("]");

	}



}
