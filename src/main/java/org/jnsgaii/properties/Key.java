package org.jnsgaii.properties;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface Key {

    interface IntKey extends Key {
        enum DefaultIntKey implements IntKey {
            POPULATION_SIZE,
            ASPECT_COUNT,
            DOUBLE_ARRAY_GENERATION_LENGTH,
            OBSERVER_UPDATE_SKIP_NUM,
            ROUND_ROBIN_BEST_OF
        }
    }

    interface DoubleKey extends Key {
        enum DefaultDoubleKey implements DoubleKey {
            INITIAL_ASPECT_ARRAY, // Stored as an array, needs to be cast. Hack-y, but works.
            ASPECT_MODIFICATION_ARRAY, // Aspect1Str, Aspect1Prob, Aspect2Str, Aspect2Prob...

            @Deprecated
            MUTATION_STRENGTH_MUTATION_PROBABILITY,
            @Deprecated
            MUTATION_STRENGTH_MUTATION_STRENGTH,

            @Deprecated
            MUTATION_PROBABILITY_MUTATION_PROBABILITY,
            @Deprecated
            MUTATION_PROBABILITY_MUTATION_STRENGTH,

            @Deprecated
            CROSSOVER_PROBABILITY_MUTATION_PROBABILITY,
            @Deprecated
            CROSSOVER_PROBABILITY_MUTATION_STRENGTH,

            @Deprecated
            CROSSOVER_STRENGTH_MUTATION_PROBABILITY,
            @Deprecated
            CROSSOVER_STRENGTH_MUTATION_STRENGTH,

            RANDOM_DOUBLE_GENERATION_MINIMUM,
            RANDOM_DOUBLE_GENERATION_MAXIMUM,

            DOUBLE_SPECIATION_MAX_DISTANCE,

            @Deprecated
            INITIAL_MUTATION_STRENGTH,
            @Deprecated
            INITIAL_MUTATION_PROBABILITY,
            @Deprecated
            INITIAL_CROSSOVER_STRENGTH,
            @Deprecated
            INITIAL_CROSSOVER_PROBABILITY,

            @Deprecated
            INITIAL_DOUBLE_CROSSOVER_DISTRIBUTION_INDEX,
            @Deprecated
            INITIAL_DOUBLE_MUTATION_DISTRIBUTION_INDEX,
        }
    }

    interface BooleanKey extends Key {
        enum DefaultBooleanKey implements BooleanKey {
            THREADED
        }
    }
}
