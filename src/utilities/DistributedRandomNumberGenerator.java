package utilities;

import java.util.HashMap;
import java.util.Map;

public class DistributedRandomNumberGenerator {


    private Map<Integer, Double> distribution;
    private double distSum;

    public DistributedRandomNumberGenerator() {
        distribution = new HashMap<Integer, Double>();
    }

    public void addNumber(int value, double distribution) {
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public int getDistributedRandomNumber() {
        double rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Integer i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return 0;
    }


    public static void main(String args[]){
    	DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
        drng.addNumber(1, 0.1);
        drng.addNumber(2, 0.05);
        drng.addNumber(0, 1.);

        int random ; //= drng.getDistributedRandomNumber();

        for(int i=0 ; i<10 ; i++){
        	random = drng.getDistributedRandomNumber();


        System.out.print(random);}
    }

}
