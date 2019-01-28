package operators;

import ga_solver.Individual;

import java.util.ArrayList;

import utilities.DistributedRandomNumberGenerator;

public class Operators {


	int last_selections_number;
	ArrayList<Operator> operatorsSet;
	ArrayList<int[]> last_improvments;
	int[] utilities;
	int total_utilities;
	double probabilities;
	DistributedRandomNumberGenerator drng;
	double Pmin;
	int nb_operators;
	int iterMax ;

	/**
	 * Constructeur
	 * @param nb_perators : le nombre d'operateurs
	 * @param last_generations : le nombre d'iterations de la fenetre glissante
	 * @param iterMax : nombre d'iterations maximales
	 */
	public Operators(int nb_operators, int last_generations, int iterMax){

		this.nb_operators = nb_operators;
		last_improvments = new ArrayList<int[]>();
		operatorsSet = new ArrayList<Operator>();
		utilities = new int[nb_operators];
		last_selections_number = last_generations;
	}

	/**
	 * Ajout d'un opérateur dans la liste d'operateur
	 * @param op
	 */
	public void addOperator(Operator op){
		operatorsSet.add(op);
		Pmin = (1./operatorsSet.size())/2;
		for(int i = 0; i< operatorsSet.size() ; i++){
			operatorsSet.get(i).setProbability(Pmin);
			operatorsSet.get(i).setProbaAtStep(Pmin, 0);
		}
	}

	/**
	 * Mise à jour des utilités pour chaque opérateurs
	 */
	public void updateUtilities(){
		total_utilities=0;
		utilities=new int[nb_operators];
		for(int i=0 ; i< last_improvments.size(); i++){
			int[] op = last_improvments.get(i);
			utilities[op[0]]+= op[1];
			total_utilities+= op[1];
		}
		for(int i=0 ; i< nb_operators; i++){
			operatorsSet.get(i).setUtility(utilities[i]);
		}

	}


	/**
	 * Calcul Mise à jour des probabilités de chaque opérateurs
	 */
	public void updateProbabilites(int iter){
		int nbOperators = operatorsSet.size();
		for(int i =0; i < nbOperators ; i++){
			double utility = operatorsSet.get(i).getUtility();
			double p;
			if(total_utilities==0){
				p = Pmin;
			}else{
				p= ((((1-nbOperators*Pmin)*utility)/total_utilities)+Pmin);
			}
			operatorsSet.get(i).setProbability(p);
			operatorsSet.get(i).setProbaAtStep(p, iter);
		}

	}

	/**
	 * Ajout d'une amelioration lors d'une iteration
	 * @param idOp : id de l'operateur
	 * @param impvt : ameliorations
	 */
	public void addImprovment(int idOp, int impvt,int iter){
		if(last_improvments.size() >= last_selections_number){
			last_improvments.remove(0);
		}
		int[] improvement= {idOp,impvt};
		last_improvments.add(improvement);
		updateUtilities();
		updateProbabilites(iter);
	}

	/**
	 * Effectue le choix de l'operateur à utiliser
	 * @return : l'identifiant de l'operateur
	 */
	public int operatorChoice(){
		drng = new  DistributedRandomNumberGenerator();
		for (int i = 0; i <operatorsSet.size() ; i++){
			drng.addNumber(i, operatorsSet.get(i).getProbability());
		}

		int selected = drng.getDistributedRandomNumber();
		return selected;
	}

	/**
	 * Application d'operateur sur un individu
	 * @param operatorID : id de l'operateur
	 * @param id : individu sur lequel la mutation est appliquée;
	 */
	public int[] operatorApplication(int operatorID, Individual id){

		return operatorsSet.get(operatorID).execution(id);
	}

	/**
	 * Affichage des dernière Ameliorations
	 */
	public void printLastImprovments(){
		System.out.print("[");
		for(int i=0; i<last_improvments.size(); i++){
			System.out.print("{"+last_improvments.get(i)[0]+","+last_improvments.get(i)[1]+"}");

		}
		System.out.print("]");

		System.out.print(" -> "); printUtilities();
		System.out.print(" -> "); printProbabilities();

	}

	public void printUtilities(){
		System.out.print("[");
		for(int i=0; i<utilities.length; i++){
			System.out.print(utilities[i]+",");

		}
		System.out.print("]");
	}

	public void printProbabilities(){
		System.out.print("[");
		for(int i=0; i<utilities.length; i++){
			System.out.print(operatorsSet.get(i).getProbability()+",");

		}
		System.out.println("]");

	}

	public void operatorInitial(int idOP){
		for(int i = 0; i<utilities.length;i++){
			operatorsSet.get(i).setProbability(0);
		}
		operatorsSet.get(idOP).setProbability(1.);
	}



	public int getNbOperator(){
		return operatorsSet.size();
	}


	/**
	 * Recupère l'opérateur d'indice i
	 * @param i : position de l'operateur dans la liste d'opérator
	 * @return
	 */
	public Operator getOperator(int i){
		return operatorsSet.get(i);

	}

}
