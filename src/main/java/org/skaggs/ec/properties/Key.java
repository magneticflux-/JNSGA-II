package org.skaggs.ec.properties;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface Key {

    enum IntKey implements Key {
        INT_POPULATION
    }

    enum BooleanKey implements Key {
        BOOLEAN_THREADED
    }

    enum DoubleKey implements Key {
        DOUBLE_ELITE_FRACTION,

        CROSSOVER_DISTRIBUTION_INDEX,
        MUTATION_DISTRIBUTION_INDEX,

        CROSSOVER_PROBABILITY,
        MUTATION_PROBABILITY
    }
}
