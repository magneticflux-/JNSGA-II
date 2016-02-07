package org.skaggs.ec.operators;

import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.properties.Requirement;
import org.skaggs.ec.util.Utils;

import java.util.stream.Stream;

/**
 * Created by skaggsm on 1/22/16.
 */
public class DefaultOperator<E> implements Operator<E> {


    private final Mutator<E> mutator;
    private final Crossoverer<E> crossoverer;
    private final Selector<E> selector;
    private final Speciator<E> speciator;

    public DefaultOperator(Mutator<E> mutator, Crossoverer<E> crossoverer, Selector<E> selector, Speciator<E> speciator) {
        this.mutator = mutator;
        this.crossoverer = crossoverer;
        this.selector = selector;
        this.speciator = speciator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Population<E> apply(FrontedPopulation<E> population, Properties properties) {
        mutator.updateProperties(properties);
        crossoverer.updateProperties(properties);
        boolean threaded = properties.getBoolean(Key.BooleanKey.THREADED);

        Stream<FrontedIndividual<E>> individualStream;

        if (threaded)
            individualStream = (Stream<FrontedIndividual<E>>) population.getPopulation().parallelStream();
        else
            individualStream = (Stream<FrontedIndividual<E>>) population.getPopulation().stream();

        return null;
    }

    @Override
    public Key[] requestProperties() {
        return Utils.concat(mutator.requestProperties(), crossoverer.requestProperties(), selector.requestProperties(), speciator.requestProperties(), new Key[]{
                Key.BooleanKey.THREADED
        });
    }

    @Override
    public Requirement[] requestDetailedRequirements() {
        return Utils.concat(mutator.requestDetailedRequirements(), crossoverer.requestDetailedRequirements(), selector.requestDetailedRequirements(), speciator.requestDetailedRequirements(), new Requirement[]{
        });
    }
}
