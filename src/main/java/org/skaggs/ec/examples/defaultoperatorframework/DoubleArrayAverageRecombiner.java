package org.skaggs.ec.examples.defaultoperatorframework;

import org.skaggs.ec.operators.Recombiner;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.util.Utils;

/**
 * Created by Mitchell on 2/11/2016.
 */
public class DoubleArrayAverageRecombiner extends Recombiner<double[]> {

    private int doubleArrayGenerationLength;

    @Override
    public void updateProperties(Properties properties) {
        super.updateProperties(properties);
        doubleArrayGenerationLength = properties.getInt(Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected double[] crossover(double[] parent1, double[] parent2, double crossoverStrength, double crossoverProbability) {
        double[] offspring = new double[doubleArrayGenerationLength];
        double parent1Weight = crossoverStrength;
        double parent2Weight = 1 - crossoverStrength;
        for (int i = 0; i < doubleArrayGenerationLength; i++) {
            offspring[i] = (parent1[i] * parent1Weight + parent2[i] * parent2Weight) / (parent1Weight + parent2Weight);
        }

        return offspring;
    }

    @Override
    public Key[] requestProperties() {
        return Utils.concat(super.requestProperties(), new Key[]{Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH});
    }
}
