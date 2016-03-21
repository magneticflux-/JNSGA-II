package org.skaggs.ec.examples.defaultoperatorframework;

import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.operators.Selector;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Mitchell on 2/10/2016.
 */
public class RouletteWheelLinearSelection<E> extends Selector<E> {
    @Override
    protected FrontedIndividual<E> choose(List<FrontedIndividual<E>> frontedIndividuals) {
        final Random r = ThreadLocalRandom.current();
        final double maxWeight = frontedIndividuals.size();

        boolean accepted = false;
        int index = -1;

        while (!accepted) {
            index = r.nextInt(frontedIndividuals.size());
            double weight = frontedIndividuals.size() - index;
            double probability = weight / maxWeight;
            accepted = r.nextDouble() < probability;
        }
        return frontedIndividuals.get(index);
    }

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
    public String[] getAspectDescriptions() {
        return new String[]{};
    }
}
