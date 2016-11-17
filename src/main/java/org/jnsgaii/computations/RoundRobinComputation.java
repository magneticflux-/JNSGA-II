package org.jnsgaii.computations;

import org.apache.commons.lang3.tuple.Pair;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Mitchell Skaggs on 10/2/2016.
 */
public class RoundRobinComputation<E> implements Computation<E, List<Pair<Integer, Integer>>> {
    @Override
    public List<Pair<Integer, Integer>>[] compute(List<Individual<E>> individuals, Properties properties) {
        List<Pair<Integer, Integer>>[] results = individuals.parallelStream()
                .map(new Function<Individual<E>, List<Pair<Integer, Integer>>>() {
                    @Override
                    public List<Pair<Integer, Integer>> apply(Individual<E> firstIndividual) {
                        List<Pair<Integer, Integer>> results = new ArrayList<>(individuals.size());
                        for (int i = 0; i < individuals.size(); i++) {
                            //TODO Finish this class. It probably won't be needed though
                        }
                        return results;
                    }
                }).toArray(value -> {
                    //noinspection unchecked
                    return (List<Pair<Integer, Integer>>[]) new List[0];
                });
        //noinspection unchecked
        return results;
    }

    @Override
    public String getComputationID() {
        return null;
    }

    @Override
    public boolean isDeterministic() {
        return false;
    }
}
