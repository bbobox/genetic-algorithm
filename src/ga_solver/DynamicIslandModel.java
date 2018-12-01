package ga_solver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
	int[][] islandSize;


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
		rewards = new double[n][n];
		islandSize = new int[n][this.iterMax];

		//intialiation de la matrice de migration
		for(int i =0; i<this.n; i++){
			for(int j = 0 ; j<this.n ; j++){
				if(i==j){
					migrationM[i][j]=0.75;
				}else{
					migrationM[i][j] = (1-0.75)/(this.n-1);
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



	public int[][] getIslandSize() {
		return islandSize;
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
			Individual individual = islands.get(i).getCurrentPopulation().get(0).cloned();
			individual.setIdPopulation(i);
			islands.get(i).removeIndividual(0);
			islands.get(islandChoosen).addIndividual(individual);
		}

	}

	public double getMaxValue(double[] t){
		int iMax = 0;
		double maxVal=0;
		for(int i = 0 ; i< t.length; i++){
			if(t[i]>t[iMax]){
				iMax=i;
				maxVal = t[iMax];
			}
		}

		return maxVal;
	}


	/**
	 * Mise à jour de la politique de migration de l'ile i
	 * @param i
	 */
	public void updateMigrationPolicy(int i){
		double[] feedback= new double[n];
		rewards[i]=new double[n];

		//feedbacks
		int improvements_cpt=0;
		for (int k =0; k<n; k++){
			feedback[k]=islands.get(k).getAverageImprovements()[i];
		}

		double maxValue = getMaxValue(feedback);

		for (int k =0; k<n; k++){
			if(feedback[k]>0 && feedback[k]==maxValue){
				improvements_cpt++;
			}
		}

		// Calcul de recompenses
		if(improvements_cpt>0){
			for (int k =0; k<n; k++){
				if (feedback[k]>0 && feedback[k]==maxValue){
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




	public int[] run(int selection, int crossover, int insertion){
		//Initialisation des  iles;
		for (int i = 0; i<n ; i++ ){
			islands.add(new Population(problemSize,popSize/n, 2, crossoverProba, mutationProba , iterMax));
			islands.get(i).initialization();
			islands.get(i).setNbPop(n);
			islands.get(i).assignIndividualsToPopulation(i);
		}

		// Evolution des populations
		for(int i = 0 ; i< iterMax ; i++){
			//System.out.println("iteration =========== "+i+" ===============");
			stepCounter = i;
			// critère d'arret
			if(!hasBestIndividual()){
				performance[stepCounter] = bestFitness();

				for(int k =0; k<islands.size() ; k++){
					this.islandSize[k][stepCounter]=islands.get(k).getPopulationSize();
				}

				for(int k = 0 ; k<islands.size() ; k++){
					//Evolution
					//islands.get(k).evolution(selection, crossover, mutation, insertion);  // evolution à revoir
					islands.get(k).overallMutationApplication(k); //TO DO k à revoir
					//islands.get(k).islandEvolution(selection, crossover, k, insertion, stepCounter);

					// Mise à jour de la politque de la politique de migration
					updateMigrationPolicy(k);

					// migration des individus de l'iles
					migration(k);

				}

			}
			else{
				performance[stepCounter] = bestFitness();
				for(int k =0; k<islands.size() ; k++){
					this.islandSize[k][stepCounter]=islands.get(k).getPopulationSize();
				}
			}
		}



		return performance;
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

	public void testPrintTable(double [] t){
		System.out.print("[");
		for(int i =0 ; i< t.length ; i++){
			System.out.print(t[i]+",");
		}
		System.out.println("]");
	}

	 /**
	  * Calcul de la moyenne des tailles de populations
	  * @param choicesProba : Ensemble des données recoltés sur l'ensemble des exécutions
	  * @param operatorID :  Id de l'opérateur/ile
	  * @param iter : nombre max d'itérations
	  * @return
	  */
	 static double[] islandSizeAverage(ArrayList<int[][]> islandSizes, int operatorID, int iter){
		 int n = islandSizes.size();
		 double[] average = new double[iter];
		 int[][] islandAverage = new int[n][iter];
		 for(int i=0; i< n ; i++){
			 islandAverage[i] = islandSizes.get(i)[operatorID];
		 }
		 for( int i=0; i< iter ; i++){
			 double cpt = 0 ;
			 for( int k=0; k< n ; k++){

				 cpt+=  islandAverage[k][i];
			 }
			 average[i] = (1.)*cpt/n;
		 }

		 return average;
	 }

	 static  void outPutAllAverage(ArrayList<int[][]> islandSizes , int iter, int n) throws IOException{

		 for(int i=0 ; i< n; i++){
			 printDataInFile("../results/operator_"+i+".dat", islandSizeAverage(islandSizes,i,iter));
		 }
	 }

	 static void printDataInFile(String outputFile, double[] data) throws IOException{
		 BufferedWriter fSortie;
		 fSortie = new BufferedWriter(new FileWriter(outputFile));

		 fSortie.write("#iterations tailles_populations");fSortie.newLine();
		 for(int i = 0; i<data.length ; i++){
			 fSortie.write(i+" "+data[i]); fSortie.newLine();

		 }
		 fSortie.close();
	 }


	public static void main(String args[]) throws IOException{
		ArrayList<int[]> executions = new  ArrayList<int[]>();
		ArrayList<int[][]> islandsSizes = new  ArrayList<int[][]>();
			int selectionType = Integer.parseInt(args[0]);
			int crossoverType = Integer.parseInt(args[1]);
			int insertionType = Integer.parseInt(args[2]);
			double pc = Double.parseDouble(args[3]);
			double pm = Double.parseDouble(args[4]);
			int size = Integer.parseInt(args[5]);
			int max = Integer.parseInt(args[6]);
			int tests = Integer.parseInt(args[7]);
			int popupaltionSize = Integer.parseInt(args[8]);
			DynamicIslandModel islands = new  DynamicIslandModel(size,popupaltionSize,max,4,pc,pm,0.8,0.1);
			for (int i = 0; i< tests; i++) {
				 islands = new  DynamicIslandModel(size,popupaltionSize,max,4,pc,pm,0.8,0.1);
				// executions.add(
						 islands.run(selectionType, crossoverType, insertionType);//);
				 islandsSizes.add(islands.getIslandSize());
			}

			double[] average = average(executions,max);
			outPutAllAverage(islandsSizes,max,4);
			printArray(average);

}




}
