package org.skaggs.ec.operators;

import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.properties.Requirement;
import org.skaggs.ec.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        speciator.updateProperties(properties);
        selector.updateProperties(properties);

        List<Individual<E>> newPopulation = new ArrayList<>(population.getPopulation().size());

        for (int i = 0; i < population.getPopulation().size(); i++) {
            FrontedIndividual<E> individual = selector.apply((List<FrontedIndividual<E>>) population.getPopulation());

            final FrontedIndividual<E> finalIndividual = individual;
            List<FrontedIndividual<E>> compatibleIndividuals = ((Collection<FrontedIndividual<E>>) population.getPopulation()).stream().filter(eFrontedIndividual -> speciator.apply(finalIndividual, eFrontedIndividual)).collect(Collectors.toList());
            while (compatibleIndividuals.size() == 0) {
                individual = selector.choose((List<FrontedIndividual<E>>) population.getPopulation());

                final FrontedIndividual<E> finalIndividual1 = individual;
                compatibleIndividuals = ((Collection<FrontedIndividual<E>>) population.getPopulation()).stream().filter(eFrontedIndividual -> speciator.apply(finalIndividual1, eFrontedIndividual)).collect(Collectors.toList());
            }
            FrontedIndividual<E> otherIndividual = selector.apply(compatibleIndividuals);
            newPopulation.add(crossoverer.apply(individual, otherIndividual));
        }

        newPopulation.replaceAll(mutator::apply);

        return new Population<>(newPopulation);
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
