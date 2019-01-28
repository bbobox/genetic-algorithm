package ga_solver;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import operators.MutationBitFlip;
import operators.MutationNFilps;
import operators.Operator;
import operators.OperatorMutation;
import operators.Operators;

/**
 * Cette classe represente le solveur du l'algorithme genetique et
 * ses composantes
 * @author boka
 *
 */
public interface Population<T> {

	/**
	 * Modification du nombre total de population/iles (population voisines+1)
	 * @param nb
	 */
	public void setNbPop(int nb);

	public int getPopulationSize();

	/**
	 * @param nbParents : le nombre d'individus à selectionnés
	 * à l'initialization
	 */
	public void initialization();
	/**
	 * Definition d el'ile d'appartenance de l'ensemble des individus de la population
	 */
	public void assignIndividualsToPopulation(int i);

	/**
	 * Recupere de la population actuelle
	 * @return
	 */
	public ArrayList<Individual> getCurrentPopulation();

	/**
	 * Affichage d'un sensemble d 'individus
	 */
	public void printSetOfIndividuals( ArrayList<Individual> set);


	/**
	 * Verifie les critrère de l'algortithme et renvoie
	 * 'true" si l'objectif est atteint
	 */
	public boolean hasBestIndividual();

	/**
	 * Selection des deux meilleurs parents
	 */
	public void bestSelection();

	/**
	 * Selection des individus par tournoi
	 * @param T :  nombre d 'individus selectionnés T € [2,populationSize]
	 */
	public void tournamentSelection(int T);

	/**
	 * Selection aleatoire de deux parents
	 */
	public void randomSelection();

	/**
	 * Retourne le meilleure fitness de la population
	 */
	public T bestFitness();

	/**
	 * Affiche du meilleur fitness en fonction du nombre d'iterations
	 */
	public void printPerformance();

	/**
	 * Lancement de la recherche de solution (Steady State)
	 */
	public T[] run(int selection, int crossover, int mutation, int insertion);


	/**
	 * Recherche de solution avec gestion automatique du choix de l'opérateur de mutation: Roulette Adaptative
	 */
	public T[] runByAdaptativeWheel(int selection, int crossover, int insertion);

	/**
	 * Effectue un choix aleatoire en fonction d'une proabilite
	 * donnée
	 * @param probalility : probabilite du choix
	 * @return
	 */
	public boolean probableChoice(double probalility);


	/**
	 * Choix aleatoire d'un entier correspondant à l'indice
	 * d'un bit dans la representation
	 * @return
	 */
	public int randomChoice();
	/**
	 * Application d'une methode de selection
	 * @param type
	 */
	public void selectionApplication(int type);

	/**
	 * Application d'un methode de croisement
	 * @param type
	 */
	public void crossoverApplication(int type);

	/**
	 * Application d'une methode par selection adaptative
	 * @param type
	 */
	public void adaptiveWheelMutationApplication(int type);
	/**
	 * Application d'une methode de mutation
	 * @param type
	 */
	public void mutationApplication(int type);

	/**
	 * Application d'une methode d'insertion
	 * @param type
	 */
	public void insertionApplication(int type);


	/*
	 * Compte le nombre de bit à 1
	 */
	int onesCounter(int[] rep);

	/**
	 * Calcule de  la moyenne des resultats obtenus lors des exécutions
	 * @param executions : ensemble des resultats d'executions
	 * @param iterMax : nombre maximum d'iterations
	 * @return
	 */
	 //double[] average(ArrayList<int[]> executions, int iterMax);

	 /**
	  * Affichage des valeurs d'un tableau répresentatif des exécutions
	  * @param array
	  */
	 // printArray(double[] array);

	 /**
	  * Evolutation de la population lors en seul itération
	  */
	 public void evolution(int selection, int crossover, int mutation, int insertion);

	 /**
	  * Recherche d'un individu de meilleur qualité
	  */
	 public Individual getBestIndividual();
	 /**
	  * Ajout d'un individu dans la population
	  * @param id
	  */
	 public void addIndividual(Individual id);

	 /**
	  * Suppression d'un individu de la population
	  * @param ind : indice/position de l'individu
	  */
	 public  void removeIndividual(int position);


	 /**
	  * Suppression d'un individu de la population
	  * @param id : Individu
	  */
	 public  void removeIndividual(Individual id);


	 public  double[][] getChoiceProba();
	 //void printDataInFile(String outputFile, double[] data) throws IOException;

	 /**
	  * Calcul de la moyenne des probas d'un opérateurs sur l'ensemble des exécutions
	  * @param choicesProba : Ensemble des données recoltés sur l'ensemble des exécutions
	  * @param operatorID :  Id de l'opérateur
	  * @param iter : nombre max d'itérations
	  * @return
	  */
	// double[] operatorAverageProba(ArrayList<double[][]> choicesProba, int operatorID, int iter);


	 //void outPutAllAverage(ArrayList<double[][]> choicesProba , int iter, int n) throws IOException;

	 /**
	  * Application de l'operateur de mutation sur l'ensemble des individus
	  * @param type: opératteur de mutation à utiliser
	  */
	 public void overallMutationApplication(int type);

	 public void islandEvolution(int selection, int crossover, int mutation, int insertion, int generation);

	 /**
	  * Mise à jour et recupération des fitness et des population d'origine
	  * Avavant l'application de l'evolutuion
	  */
	public void updateFitnessBeforeEvolution();

	  /**
	  * Mise à jour et recupération des fitness et des population d'origine
	  * Avavant l'application de l'evolutuion
	  */
	 public void updateFitnessAfterEvolution();

	 /**
	  * Met à jour l'etat d'amelioration des individus
	  */
	 public  void improvedIndividual();/*{


	 }*/

	 /**
	  * Mise à jour de l'ensemble des populations présente
	  */
	 public void updateOriginOfIndividuals();

	 /**
	  * Calcul de l'ensemble des amélorations des individus provenants de chaque iles
	  */
	 public void computeImprovements();

	 /**
	  * Recuperation de la liste des
	  * @return
	  */
	 public double[] getAverageImprovements();

	public void initArray(double[] t);



}
