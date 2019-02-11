package operators;

import java.util.Random;

import tsp.Solution;
import ga_solver.Individual;

public class MutationSwap implements OperatorMutation {

	int problemSize;

	public MutationSwap(int problemSize){
		this.problemSize= problemSize;
	}

	public int[] mutationApplication(Individual child) {
		int[] representation = child.getClonedRepresentation();
		Random rand = new Random();
		int idCity1, idCity2, swap;
		idCity1 = rand.nextInt(problemSize);

		idCity2 = rand.nextInt(problemSize);
		while(idCity2==idCity1){
			idCity2 = rand.nextInt(problemSize);
		}

		swap = representation[idCity1];
		representation[idCity1] = representation[idCity2];
		representation[idCity2] = swap;

		return representation;
	}

}
