package org.skaggs.ec.operators;

import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.*;
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

    private static int setAspectIndices(HasAspectRequirements... hasAspectRequirementses) {
        int currentIndex = 0;
        for (HasAspectRequirements hasAspectRequirements : hasAspectRequirementses) {
            currentIndex += hasAspectRequirements.requestAspectLocation(currentIndex);
        }
        return currentIndex;
    }

    private HasAspectRequirements[] getHasAspectRequirementses() {
        return Utils.concat(new HasAspectRequirements[]{recombiner, selector, speciator}, mutators.toArray(new HasAspectRequirements[mutators.size()]));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Population<E> apply(FrontedPopulation<E> population, Properties properties) {
        mutators.forEach(mutator -> mutator.updateProperties(properties));
        recombiner.updateProperties(properties);
        speciator.updateProperties(properties);
        selector.updateProperties(properties);

        setAspectIndices(getHasAspectRequirementses());

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
                Utils.concat(mutators.stream().map(HasPropertyRequirements::requestProperties).toArray(Key[][]::new)),
                recombiner.requestProperties(),
                selector.requestProperties(),
                speciator.requestProperties(),
                new Key[]{
                        Key.BooleanKey.DefaultBooleanKey.THREADED
        });
    }

    @Override
    public Requirement[] requestDetailedRequirements() {
        return Utils.concat(
                Utils.concat(mutators.stream().map(HasPropertyRequirements::requestDetailedRequirements).toArray(Requirement[][]::new)),
                recombiner.requestDetailedRequirements(),
                selector.requestDetailedRequirements(),
                speciator.requestDetailedRequirements(),
                new Requirement[]{
                        new Requirement() {
                            @Override
                            public String describe() {
                                int size = setAspectIndices(getHasAspectRequirementses());
                                return size + " aspect data slots required.";
                            }

                            @Override
                            public boolean test(Properties properties) {
                                int size = setAspectIndices(getHasAspectRequirementses());
                                return ((double[]) properties.getValue(Key.DoubleKey.DefaultDoubleKey.INITIAL_ASPECT_ARRAY)).length == size;
                            }
                        }
                });
    }
}
