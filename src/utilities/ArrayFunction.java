package utilities;

import ga_solver.Individual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import tsp.Parser;
import tsp.Solution;
import tsp.TspPopulation;

public class ArrayFunction<T> {


	public ArrayFunction(){}


	public <T> boolean  contains(int[] childRepresentation, int j){

		for(int i=0; i< childRepresentation.length; i++){
			if( childRepresentation[i]==j){
				return true;
			}
		}
		return false;
	}


	/**
	 * Affiche la representation de l'individu
	 */
	public <T> void print(int[] rep1){
		System.out.print('[');
		for(int i = 0; i<rep1.length; i++){
			System.out.print(rep1[i]+",");
		}
		System.out.print(']');
	}

	public void fill(int[] rep1, int value){
		for(int i=0; i<rep1.length;i++){
			rep1[i]=value;
		}
	}

	/**
	 * Recupère la position d'une value dans un tableau
	 * @param rep1
	 * @return : renvoi la première position trouvé si la valeur exite dans le tableau, sinon la valeur -1 est renvoyée
	 */
	public int getValuePosition(int[] arr, int value){
		for( int i=0; i<arr.length; i++){
			if( arr[i]== value){
				return i;
			}
		}
		return -1;

	}


	public static void main(String args[]) throws IOException{
		Parser p = new  Parser();
		p.getDistancesMatrix("a280.tsp");
		Individual i = new Solution(280,p.getDistancesMatrix("a280.tsp"));
		int[] t = {128,26,71,152,231,37,70,257,229,62,29,27,38,53,252,85,131,195,52,69,31,9,194,130,11,134,28,55,92,192,147,3,211,246,205,19,260,108,273,215,186,102,199,88,212,17,78,87,81,40,143,173,36,137,237,120,119,30,261,153,144,104,68,63,263,279,23,107,159,18,253,178,105,98,139,216,145,164,97,83,39,35,206,258,163,8,99,278,125,241,90,148,58,95,72,225,110,174,262,75,96,169,116,46,220,136,20,271,233,132,22,118,197,14,256,189,135,218,54,91,175,171,156,185,214,76,217,142,248,168,166,10,42,89,103,5,150,250,204,196,50,177,244,4,67,187,188,16,203,106,184,57,111,202,210,133,165,122,226,1,124,269,146,74,149,180,232,73,127,227,66,201,113,190,138,277,236,6,160,79,86,223,179,266,238,154,12,182,64,157,270,34,200,254,198,219,56,193,32,259,222,245,230,158,191,162,129,121,183,123,170,221,274,33,239,213,251,167,267,45,80,101,208,59,84,43,112,181,176,117,49,44,249,228,47,61,247,224,240,243,109,161,93,140,172,100,155,25,272,41,268,234,24,264,51,7,141,13,82,276,115,65,255,242,235,209,2,77,151,60,15,126,21,114,48,94,275,0,265,207};
		i.setRepresentation(t);

		ArrayFunction af = new ArrayFunction();
		//System.out.print(af.getValuePosition(t,152));
		//System.out.print(i.getFitness());

		int[] t1 = {1,2,3,4,5,6,7,8,9};
		int[] t3 = {9,8,7,6,1,5,4,3,2};

		int[] t2 = {9,3,7,8,2,6,5,1,4};
		//af.offspringGenerationCyle(t1, t2);

	}




}
