package operators;

import java.util.Random;

import ga_solver.Individual;

public class MutationInsert implements OperatorMutation{

	public int[] mutationApplication(Individual child) {
		int[] representation = child.getClonedRepresentation();
		Random rand = new Random();
		int idCity1, idCity2, swap;
		idCity1 = rand.nextInt(representation.length-1);

		idCity2 = rand.nextInt(representation.length-1-idCity1)+idCity1+1;
		swap=representation[idCity2];

		for(int i=idCity2 ; i>idCity1+1 ;i--){
			representation[i]=representation[i-1];
		}
		representation[idCity1+1]=swap;

		return representation;
	}

}