package ga_solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;


/**
 * Represente un individu
 * @author boka
 *
 */
public interface Individual<T> {

	Comparator<? super Individual> IndividualAgeComparator = null;
	Comparator<? super Individual> IndividualFintessComparator = null;

	/**
	 * Affiche la representation de l'individu
	 */
	public void print();
	/**
	 * Calcule le fitness
	 */

	public T getFitness();



	public int getGeneration();

	public void setGeneration(int generation) ;

	/**
	 * @param id : indice element à modifier dans la representation
	 * @param value : nouvelle value
	 */
	public void changeValue(int id, int value);


	public int[] getRepresentation() ;
	public void setRepresentation(int[] representation) ;


	public int[] getClonedRepresentation();

	public Individual<T> cloned();



	public int getIdPopulation() ;

	public void setIdPopulation(int idPopulation);


}
