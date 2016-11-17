package org.jnsgaii.operators;

import org.jnsgaii.properties.AspectUser;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.LateUpdatingProperties;
import org.jnsgaii.util.Range;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

/**
 * Created by Mitchell Skaggs on 1/22/16.
 */
public abstract class Mutator<E> extends AspectUser implements BiFunction<E, double[], E>, HasPropertyRequirements, LateUpdatingProperties {

    public static double mutate(double d, Random r, double range) {
        return (d + (r.nextDouble() * 2 * range)) - range;
    }

    @Override
    public E apply(E individual, double[] aspects) {
        Random r = ThreadLocalRandom.current();

        if (r.nextDouble() < aspects[startIndex + 1])
            return mutate(individual, aspects[startIndex], aspects[startIndex + 1]);
        else
            return individual;
    }

    @Override
    public void modifyAspects(double[] aspects, Random r) {
        if (r.nextDouble() < aspectModificationArray[startIndex * 2 + 1])
            aspects[startIndex] = Mutator.mutate(aspects[startIndex], r, aspectModificationArray[startIndex * 2]);
        if (r.nextDouble() < aspectModificationArray[startIndex * 2 + 3])
            aspects[startIndex + 1] = Mutator.mutate(aspects[startIndex + 1], r, aspectModificationArray[startIndex * 2 + 2]);

        aspects[startIndex] = Range.clip(0, aspects[startIndex], Double.POSITIVE_INFINITY);
        aspects[startIndex + 1] = Range.clip(0, aspects[startIndex + 1], 1);
    }

    @Override
    public String[] getAspectDescriptions() {
        return new String[]{"Mutation Strength", "Mutation Probability"};
    }

    @Override
    public int requestAspectLocation(int startIndex) {
        super.requestAspectLocation(startIndex);
        return 2;
    }

    protected abstract E mutate(E object, double mutationStrength, double mutationProbability);
}
