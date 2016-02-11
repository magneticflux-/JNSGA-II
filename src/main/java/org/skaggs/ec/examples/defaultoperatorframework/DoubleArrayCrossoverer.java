package org.skaggs.ec.examples.defaultoperatorframework;

import org.skaggs.ec.operators.Crossoverer;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.util.Utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by skaggsm on 2/11/16.
 */
public class DoubleArrayCrossoverer extends Crossoverer<double[]> {

    private int doubleArrayGenerationLength;

    @Override
    public void updateProperties(Properties properties) {
        super.updateProperties(properties);
        doubleArrayGenerationLength = properties.getInt(Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH);
    }

    @Override
    protected double[] crossover(double[] parent1, double[] parent2, double crossoverStrength, double crossoverProbability) {
        final Random r = ThreadLocalRandom.current();
        double[] offspring = new double[doubleArrayGenerationLength];
        int crossoverPoint = doubleArrayGenerationLength > 2 ? r.nextInt(doubleArrayGenerationLength - 2) + 1 : 1; //Exclude endpoints and 50% split when length is 2
        boolean parent1First = r.nextBoolean();
        for (int i = 0; i < doubleArrayGenerationLength; i++) {
            if (i < crossoverPoint) {
                offspring[i] = parent1First ? parent1[i] : parent2[i];
            } else {
                offspring[i] = parent1First ? parent2[i] : parent1[i];
            }
        }

        return offspring;
    }

    @Override
    public Key[] requestProperties() {
        return Utils.concat(super.requestProperties(), new Key[]{Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH});
    }
}
