package tsp;

import ga_solver.Population;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	BufferedReader reader;
	String line;
	double[][] distancesMatrix;
	int[][] vertices;

	public Parser(){
		reader = null;

	}


	/**
	 * Recupreration de la matrice de distance
	 * @throws IOException
	 */
	public double[][] getDistancesMatrix(String filepath) throws IOException{

		try {
			reader = new BufferedReader(new FileReader(filepath));
			int n=0;
			while( ((line = reader.readLine()) !=null)){
				if (line.startsWith("DIMENSION")){
					n=Integer.parseInt((line.split("DIMENSION:")[1]).replaceAll(" ", ""));
				}
			}
			reader = new BufferedReader(new FileReader(filepath));
			distancesMatrix = new double[n][n];
			vertices = new int[n][2];
			Pattern p = Pattern.compile("\\s*\\d+\\s*\\d+\\s+\\d+");
			int i=0, x, y;
			while( ((line = reader.readLine()) !=null)){
				Matcher matcher = p.matcher(line);
				if (matcher.matches()){
					line =line.replaceAll("\\s+",",");
        			String[] cord=line.split(",");

        			if (cord.length>3){
        				i = Integer.parseInt(cord[1]);
            			x = Integer.parseInt(cord[2]);
            			y = Integer.parseInt(cord[3]);

        			}
        			else{
        				i = Integer.parseInt(cord[0]);
            			x = Integer.parseInt(cord[1]);
            			y = Integer.parseInt(cord[2]);

        			}
        			vertices[i-1][0]=x;
        			vertices[i-1][1]=y;
        		}
			}

		for(int j = 0; j<n ; j++){
			for(int k = 0; k<n ; k++){
				distancesMatrix[j][k]=euclidianDistance(vertices[j],vertices[k]);

			}

		}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return distancesMatrix;
	}

	/**
	 * Calcule de la distance euclidienne entre deux points
	 * @param v1 : cord p1
	 * @param v2 : cord p2
	 * @return
	 */
	double euclidianDistance(int[] v1, int v2[]){

		return Math.sqrt( Math.pow(v1[0]-v2[0],2)+ Math.pow(v1[1]-v2[1],2));
	}

	public static void main(String args[]) throws IOException{

		Parser p = new  Parser();
		p.getDistancesMatrix("a280.tsp");


	}

}
