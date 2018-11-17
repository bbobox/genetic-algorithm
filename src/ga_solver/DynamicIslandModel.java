package ga_solver;

import java.util.ArrayList;


public class DynamicIslandModel {

	ArrayList <Population> islands; // represente l'ensemble des iles
	double[][] migrationM; // Matrice de transition
	double[] R; // Vecteur de Récompense
	double B; //  beta - quantité de bruit
	double Ni; //vecteur stochastique aléatoire.
	double[][] rewards;  // Matrice contenant des vecteur de recompense
	double a ;// alpha -- l'importance de la connaissance
	int n;     // nombre d'iles
	int iterMax; //nombre max d'iterations
	int popSize;
	int problemSize;
	double crossoverProba;
	double mutationProba ;
	int stepCounter;
	int[][] feedBacks; // Ensemble des feedback renvoyé

	double[][] feedbacK; // Matrice de feedBack
	double alpha;
	double beta;
	double[][] N;


	private int[] performance;

	/**
	 *
	 * @param n : nombre d'iles
	 */
	public DynamicIslandModel(int problemSize, int popSize, int iter, int n, double pc, double pm, double alpha, double beta){

		this.n= n;
		this.iterMax= iter;
		this.popSize = popSize;
		migrationM = new double[n][n];
		islands = new ArrayList <Population>();
		this.problemSize = problemSize;
		crossoverProba = pc;
		mutationProba = pm;
		this.beta = beta;
		this.alpha = alpha;
		N = new double[n][n];

		//intialiation de la matrice de migration
		for(int i =0; i<this.n; i++){
			for(int j = 0 ; j<this.n ; j++){
				if(i==j){
					migrationM[i][j]=0.75;
				}else{
					migrationM[i][j] = (1-0.75)/n-1;
				}
			}
		}

	    //initalisation de la matrice de recompenses
		for(int i =0; i<this.n; i++){
			for(int j = 0 ; j<this.n ; j++){
				rewards[i][j]=0;
			}
		}

		// generations des vecteur stochastique aléatoire
		for(int i =0; i<n; i++){
			N[i]= createStochasticVector(100,this.n);
		}

		performance = new int[iterMax];

	}

	/**
	 * Creation  d'un vecteur stochastique aléatoire:
	 * 		-l'idée est de génerer un certains nombre de fois un nombre compris entre [0,...., n-1]
	 * 		-et pour chaque nombre i compris entre [0,...., n-1], V[i] est est égal au pourcentage d'obbtention et l'enmble de la generation
	 * @param time : nombre d'essais
	 * @param n	: taille du vecteur
	 * @return
	 */
	public double[] createStochasticVector(int time, int n){
		double[] vector = new double[n];
		DistributedRandomNumberGenerator drng = new  DistributedRandomNumberGenerator();
		for (int j = 0; j <n ; j++){
			drng.addNumber(j, 1./n);
		}

		int[] S= new int[n];
		int l;
		for(int i=0; i<time ;i++){
			l = drng.getDistributedRandomNumber();
			S[l]+=1;
		}

		for (int j = 0; j <n ; j++){
			vector[j]=S[j]*(1.)/time;
		}

		return vector;
	}

	/**
	 *  Verifie si le critère d'arret est atteint (fitness maximal)
	 * @return
	 */
	public boolean hasBestIndividual(){
		boolean ok=false;
		int i = 0;
		while(i<n && ok==false){
			if(islands.get(i).hasBestIndividual()){
				ok = true;
			}
			i++;
		}

		return ok;
	}

	/**
	 * Migration des individu d'une population
	 * @param j:  Population j
	 */
	public void migration(int i){
		int islandChoosen ;
		DistributedRandomNumberGenerator drng = new  DistributedRandomNumberGenerator();
		for (int j = 0; j <n ; j++){
			drng.addNumber(j, migrationM[i][j]);
		}

		int pSize = islands.get(i).getPopulationSize();
		for(int k = 0; k<pSize;k++){
			islandChoosen= drng.getDistributedRandomNumber();
			Individual individual = islands.get(i).getCurrentPopulation().get(k).cloned();
			individual.setIdPopulation(i);
			islands.get(i).removeIndividual(0);
			islands.get(islandChoosen).addIndividual(individual);
		}

	}

	public void feedBack(){
		// determination des amélioration et renvoi de feedback
	}

	/**
	 * Mise à jour de la politique de migration de l'ile i
	 * @param i
	 */
	public void updateMigrationPolicy(int i){
		int[] feedback= new int[n];
		rewards[i]=new double[n];

		//feedbacks
		int improvements_cpt=0;
		for (int k =0; k<n; k++){
			feedback[k]=islands.get(k).getImprovements()[i];
			if(feedback[k]>0){
				improvements_cpt++;
			}
		}

		// Calcul de recompenses
		if(improvements_cpt>0){
			for (int k =0; k<n; k++){
				if (feedback[k]>0){
					rewards[i][k]=1./improvements_cpt;
				}
			}
		}

		// probaliités de migrations
		for (int k =0; k<n; k++){
			this.migrationM[i][k]= (1-beta) * ((alpha*migrationM[i][k]) + ((1-alpha)*rewards[i][k]))+ (beta*N[i][k]);
		}

	}


	/**
	 * Calcule la meilleur valeur de fitness de toute les Iles
	 * @return
	 */
	public int bestFitness(){
		int res = 0;
		for (int i=0; i<islands.size() ; i++){
			if (islands.get(i).bestFitness()>res){
				res = islands.get(i).bestFitness();
			}
		}
		return res;
	}




	public int[] run(int selection, int crossover, int mutation, int insertion){
		//Initialisation des  iles;
		for (int i = 0; i<n ; i++ ){
			islands.add(new Population(problemSize,popSize/n, 2, crossoverProba, mutationProba , iterMax));
			islands.get(i).initialization();
			islands.get(i).assignIndividualsToPopulation(i);
		}
		// Evolution des populations

		for(int i = 0 ; i< iterMax ; i++){
			stepCounter = i;
			// critère d'arret
			if(!hasBestIndividual()){
				for(int k = 0 ; k<islands.size() ; k++){
					//Evolution
					//islands.get(k).evolution(selection, crossover, mutation, insertion);  // evolution à revoir
					islands.get(k).overallMutationApplication(k); //TO DO k à revoir

					// Mise à jour de la politque de la politique de migration
					updateMigrationPolicy(k);

					// migration des individus de l'iles
					migration(k);

				}
			}
			else{
				performance[stepCounter] = bestFitness();
			}
		}



		return performance;
	}




}
