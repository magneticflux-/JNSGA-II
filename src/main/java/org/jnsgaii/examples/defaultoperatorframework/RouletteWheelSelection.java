package org.jnsgaii.examples.defaultoperatorframework;

import org.jnsgaii.multiobjective.population.FrontedIndividual;
import org.jnsgaii.operators.Selector;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Mitchell Skaggs on 4/22/2016.
 */
public abstract class RouletteWheelSelection<E> extends Selector<E> {
    @Override
    protected FrontedIndividual<E> choose(List<FrontedIndividual<E>> frontedIndividuals) {
        final Random r = ThreadLocalRandom.current();
        final double maxWeight = getWeight(linearRank(0, frontedIndividuals.size()));

        boolean accepted = false;
        int index = -1;

        while (!accepted) {
            index = r.nextInt(frontedIndividuals.size());
            double weight = getWeight(linearRank(index, frontedIndividuals.size()));
            double probability = weight / maxWeight;
            accepted = r.nextDouble() < probability;
        }
        return frontedIndividuals.get(index);
    }

    /**
     * @param index the index in the list
     * @param size  the size of the list
     * @return a number from <code>size</code> to 1, with <code>size</code> corresponding to an index of 0
     */
    private int linearRank(int index, int size) {
        return size - index;
    }

    protected abstract double getWeight(int rank);

    @Override
    public Key[] requestProperties() {
        return new Key[0];
    }

    @Override
    public void updateProperties(Properties properties) {
    }

    @Override
    public int requestAspectLocation(int startIndex) {
        return 0;
    }

    @Override
    public void modifyAspects(double[] aspects, Random r) {
    }

    @Override
    public String[] getAspectDescriptions() {
        return new String[]{};
    }
}
