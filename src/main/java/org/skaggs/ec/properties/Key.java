package org.skaggs.ec.properties;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface Key {

    interface IntKey extends Key {
        enum DefaultIntKey implements Key {
            POPULATION_SIZE,
            ASPECT_COUNT,
            DOUBLE_ARRAY_GENERATION_LENGTH
        }
    }

    interface DoubleKey extends Key {
        enum DefaultDoubleKey implements Key {
            INITIAL_ASPECT_ARRAY, // Stored as an array, needs to be cast. Hack-y, but works.

            INITIAL_MUTATION_STRENGTH,
            MUTATION_STRENGTH_MUTATION_PROBABILITY,
            MUTATION_STRENGTH_MUTATION_STRENGTH,

            INITIAL_MUTATION_PROBABILITY,
            MUTATION_PROBABILITY_MUTATION_PROBABILITY,
            MUTATION_PROBABILITY_MUTATION_STRENGTH,

            INITIAL_CROSSOVER_PROBABILITY,
            CROSSOVER_PROBABILITY_MUTATION_PROBABILITY,
            CROSSOVER_PROBABILITY_MUTATION_STRENGTH,

            INITIAL_CROSSOVER_STRENGTH,
            CROSSOVER_STRENGTH_MUTATION_PROBABILITY,
            CROSSOVER_STRENGTH_MUTATION_STRENGTH,

            INITIAL_DOUBLE_CROSSOVER_DISTRIBUTION_INDEX,
            INITIAL_DOUBLE_MUTATION_DISTRIBUTION_INDEX,

            RANDOM_DOUBLE_GENERATION_MINIMUM,
            RANDOM_DOUBLE_GENERATION_MAXIMUM,

            DOUBLE_SPECIATION_MAX_DISTANCE
        }
    }

    interface BooleanKey extends Key {
        enum DefaultBooleanKey implements Key {
            THREADED
        }
    }
}
