package org.jnsgaii.functions;

import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Properties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mitchell on 11/25/2015.
 * <p>
 * This class is a comparator on its own function. It decides which score bests which other score, which affects the algorithm's choices.
 * </p>
 * <p>
 * Functions are near-instant and utilize computation results, while computations are designed to be long-running processes
 * </p>
 * <p>
 * NOTE: An implementor must also implement the <code>equals</code> method.
 * </p>
 *
 * @see java.util.Comparator
 */
public interface OptimizationFunction<E> extends HasComparator<Double>, HasPropertyRequirements, Serializable {

    double[] evaluate(List<Individual<E>> individuals, HashMap<String, Object>[] computationResults, Properties properties);

    /**
     * @param properties the evolutionary algorithm's properties
     * @return the minimum value this function can return
     */
    double min(Properties properties);

    /**
     * @param properties the evolutionary algorithm's properties
     * @return the maximum value this function can return
     */
    double max(Properties properties);

    /**
     * Does this OptimizationFunction's result change for each evaluation?
     *
     * @return the answer
     */
    boolean isDeterministic();


}
