package operators;

import java.util.Random;

import utilities.ArrayFunction;
import ga_solver.Individual;

public class MutationInversion  implements OperatorMutation{

	public int[] mutationApplication(Individual child) {
		int[] representation = child.getClonedRepresentation();
		Random rand = new Random();
		ArrayFunction af = new ArrayFunction();
		int idCity1, idCity2;
		idCity1 = rand.nextInt(representation.length-1);

		idCity2 = rand.nextInt(representation.length-1-idCity1)+idCity1+1;
		representation = af.inverse(representation, idCity1, idCity2);
		return representation;
	}

}
