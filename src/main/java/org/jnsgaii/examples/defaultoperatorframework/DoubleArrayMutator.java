package org.jnsgaii.examples.defaultoperatorframework;

import org.jnsgaii.operators.Mutator;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;
import org.jnsgaii.util.Utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Mitchell Skaggs on 2/11/16.
 */
public class DoubleArrayMutator extends Mutator<double[]> {
    private int doubleArrayGenerationLength;

    @Override
    public void updateProperties(Properties properties) {
        super.updateProperties(properties);
        doubleArrayGenerationLength = properties.getInt(Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH);
    }

    @Override
    protected double[] mutate(double[] object, double mutationStrength, double mutationProbability) {
        Random r = ThreadLocalRandom.current();
        double[] newObject = new double[doubleArrayGenerationLength];
        for (int i = 0; i < newObject.length; i++) {
            newObject[i] = Mutator.mutate(object[i], r, mutationStrength);
        }
        return newObject;
    }

    @Override
    public Key[] requestProperties() {
        return Utils.concat(super.requestProperties(), new Key[]{Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH});
    }
}
