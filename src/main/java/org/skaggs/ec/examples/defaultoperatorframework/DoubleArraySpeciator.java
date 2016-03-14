package org.skaggs.ec.examples.defaultoperatorframework;

import org.apache.commons.math3.util.FastMath;
import org.skaggs.ec.operators.Speciator;
import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

/**
 * Created by skaggsm on 2/11/16.
 */
public class DoubleArraySpeciator extends Speciator<double[]> {
    private int doubleArrayGenerationLength;
    private double maxDistance;

    @Override
    protected double getDistance(Individual<double[]> individual, Individual<double[]> individual2) {
        double distance = 0;
        for (int i = 0; i < doubleArrayGenerationLength; i++) {
            distance += FastMath.abs(individual.getIndividual()[i] - individual2.getIndividual()[i]);
        }
        return distance;
    }

    @Override
    protected double getMaxDistance() {
        return maxDistance;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH, Key.DoubleKey.DefaultDoubleKey.DOUBLE_SPECIATION_MAX_DISTANCE};
    }

    @Override
    public void updateProperties(Properties properties) {
        doubleArrayGenerationLength = properties.getInt(Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH);
        maxDistance = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.DOUBLE_SPECIATION_MAX_DISTANCE);
    }

    @Override
    public int requestAspectLocation(int startIndex) {
        return 0;
    }
}
