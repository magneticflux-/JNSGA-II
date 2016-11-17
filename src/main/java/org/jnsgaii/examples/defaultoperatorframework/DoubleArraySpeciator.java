package org.jnsgaii.examples.defaultoperatorframework;

import org.apache.commons.math3.util.FastMath;
import org.jnsgaii.operators.speciation.Speciator;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.AspectUser;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;
import org.jnsgaii.util.Utils;

import java.util.Random;

/**
 * Created by Mitchell Skaggs on 2/11/16.
 */
public class DoubleArraySpeciator extends Speciator<double[]> {
    private int doubleArrayGenerationLength;
    //private double maxDistance;

    @Override
    public double getDistance(Individual<double[]> individual, Individual<double[]> individual2) {
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
    public void modifyAspects(double[] aspects, Random r) {
        AspectUser.mutateAspect(aspectModificationArray, aspects, startIndex, r, 0, Double.POSITIVE_INFINITY);
    }

    @Override
    public String[] getAspectDescriptions() {
        return Utils.concat(new String[]{"Max Mating Dissimilarity"});
    }
}
