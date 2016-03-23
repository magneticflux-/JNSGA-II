package org.skaggs.ec.examples.defaultoperatorframework;

import org.apache.commons.math3.util.FastMath;
import org.skaggs.ec.operators.Mutator;
import org.skaggs.ec.operators.Speciator;
import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.util.Range;
import org.skaggs.ec.util.Utils;

import java.util.Random;

/**
 * Created by skaggsm on 2/11/16.
 */
public class DoubleArraySpeciator extends Speciator<double[]> {
    private int doubleArrayGenerationLength;
    //private double maxDistance;

    @Override
    protected double getDistance(Individual<double[]> individual, Individual<double[]> individual2) {
        double distance = 0;
        for (int i = 0; i < doubleArrayGenerationLength; i++) {
            distance += FastMath.abs(individual.getIndividual()[i] - individual2.getIndividual()[i]);
        }
        return distance;
    }

    @Override
    protected double getMaxDistance(Individual<double[]> individual, Individual<double[]> individual2) {
        return (individual.aspects[startIndex] + individual2.aspects[startIndex]) / 2;
    }

    @Override
    public Key[] requestProperties() {
        return Utils.concat(super.requestProperties(),
                new Key[]{
                        Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH
                });
    }

    @Override
    public void updateProperties(Properties properties) {
        super.updateProperties(properties);
        doubleArrayGenerationLength = properties.getInt(Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH);
        //maxDistance = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.DOUBLE_SPECIATION_MAX_DISTANCE);
    }

    @Override
    public int requestAspectLocation(int startIndex) {
        super.requestAspectLocation(startIndex);
        return 1;
    }

    @Override
    public void modifyAspects(Individual<double[]> individual, Random r) {
        double[] aspects = individual.aspects;
        if (r.nextDouble() < aspectModificationArray[startIndex * 2 + 1])
            aspects[startIndex] = Mutator.mutate(aspects[startIndex], r, aspectModificationArray[startIndex * 2]);

        aspects[startIndex] = Range.clip(0, aspects[startIndex], Double.POSITIVE_INFINITY);
    }

    @Override
    public String[] getAspectDescriptions() {
        return Utils.concat(new String[]{"Max Mating Dissimilarity"});
    }
}
