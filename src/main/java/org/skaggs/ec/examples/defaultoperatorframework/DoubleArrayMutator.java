package org.skaggs.ec.examples.defaultoperatorframework;

import org.skaggs.ec.operators.Mutator;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.util.Utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by skaggsm on 2/11/16.
 */
public class DoubleArrayMutator extends Mutator<double[]> {
    private int doubleArrayGenerationLength;

    @Override
    public void updateProperties(Properties properties) {
        super.updateProperties(properties);
        doubleArrayGenerationLength = properties.getInt(Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH);
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
        return Utils.concat(super.requestProperties(), new Key[]{Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH});
    }
}
