package org.skaggs.ec.operators;

import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.HasPropertyRequirements;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.properties.Requirement;
import org.skaggs.ec.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by skaggsm on 1/22/16.
 */
public class DefaultOperator<E> implements Operator<E> {


    private final List<Mutator<E>> mutators;
    private final Recombiner<E> recombiner;
    private final Selector<E> selector;
    private final Speciator<E> speciator;

    public DefaultOperator(List<Mutator<E>> mutators, Recombiner<E> recombiner, Selector<E> selector, Speciator<E> speciator) {
        this.mutators = new ArrayList<>(mutators);
        this.recombiner = recombiner;
        this.selector = selector;
        this.speciator = speciator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Population<E> apply(FrontedPopulation<E> population, Properties properties) {
        mutators.forEach(mutator -> mutator.updateProperties(properties));
        recombiner.updateProperties(properties);
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
            newPopulation.add(recombiner.apply(individual, otherIndividual));
        }

        IntStream.range(0, mutators.size()).forEach(value -> newPopulation.replaceAll(mutators.get(value)::apply));

        return new Population<>(newPopulation);
    }

    @Override
    public Key[] requestProperties() {
        return Utils.concat(
                Utils.concat((Key[]) mutators.stream().map(HasPropertyRequirements::requestProperties).toArray()),
                recombiner.requestProperties(),
                selector.requestProperties(),
                speciator.requestProperties(),
                new Key[]{
                Key.BooleanKey.THREADED
        });
    }

    @Override
    public Requirement[] requestDetailedRequirements() {
        return Utils.concat(
                Utils.concat((Requirement[]) mutators.stream().map(HasPropertyRequirements::requestDetailedRequirements).toArray()),
                recombiner.requestDetailedRequirements(),
                selector.requestDetailedRequirements(),
                speciator.requestDetailedRequirements(),
                new Requirement[]{
        });
    }
}
