package org.jnsgaii.operators;

import org.jnsgaii.multiobjective.population.FrontedIndividual;
import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.population.Population;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.AspectUser;
import org.jnsgaii.properties.HasAspectRequirements;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;
import org.jnsgaii.properties.Requirement;
import org.jnsgaii.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by skaggsm on 1/22/16.
 */
public class DefaultOperator<E> implements Operator<E>, HasAspectRequirements {


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

        setAspectIndices(getHasAspectRequirementses());

        List<Individual<E>> newPopulation = new ArrayList<>(population.getPopulation().size());
        for (int i = 0; i < population.getPopulation().size(); i++) {
            FrontedIndividual<E> individual = selector.apply((List<FrontedIndividual<E>>) population.getPopulation());

            final FrontedIndividual<E> finalIndividual = individual;
            List<FrontedIndividual<E>> compatibleIndividuals = ((Collection<FrontedIndividual<E>>) population.getPopulation()).stream()
                    .filter(eFrontedIndividual -> speciator.apply(finalIndividual, eFrontedIndividual) && !finalIndividual.equals(eFrontedIndividual))
                    .collect(Collectors.toList());
            while (compatibleIndividuals.size() == 0) {
                individual = selector.choose((List<FrontedIndividual<E>>) population.getPopulation());

                final FrontedIndividual<E> finalIndividual1 = individual;
                compatibleIndividuals = ((Collection<FrontedIndividual<E>>) population.getPopulation()).stream()
                        .filter(eFrontedIndividual -> speciator.apply(finalIndividual1, eFrontedIndividual))
                        .collect(Collectors.toList());
            }
            FrontedIndividual<E> otherIndividual = selector.apply(compatibleIndividuals);
            newPopulation.add(recombiner.apply(individual, otherIndividual));
        }

        IntStream.range(0, mutators.size()).forEach(value -> newPopulation.replaceAll(mutators.get(value)::apply));

        for (AspectUser<E> aspectUser : getAspectUsers()) {
            newPopulation.forEach(individual -> aspectUser.modifyAspects(individual, ThreadLocalRandom.current()));
        }

        return new Population<>(newPopulation);
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

    private AspectUser<E>[] getAspectUsers() {
        //noinspection unchecked
        return Utils.concat(new AspectUser[]{recombiner, selector, speciator}, mutators.toArray(new AspectUser[mutators.size()]));
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
                            int size = setAspectIndices(getHasAspectRequirementses());

                            @Override
                            public String describe() {
                                return size + " aspect data slots required.";
                            }

                            @Override
                            public boolean test(Properties properties) {
                                return ((double[]) properties.getValue(Key.DoubleKey.DefaultDoubleKey.INITIAL_ASPECT_ARRAY)).length == size;
                            }
                        }
                });
    }

    @Override
    public int requestAspectLocation(int startIndex) {
        throw new UnsupportedOperationException("This object only supports the descriptions of the aspects used by it.");
    }

    @Override
    public String[] getAspectDescriptions() {
        return Utils.concat(recombiner.getAspectDescriptions(), selector.getAspectDescriptions(), speciator.getAspectDescriptions(), mutators.stream().map(Mutator<E>::getAspectDescriptions).reduce(new String[0], Utils::concat));
    }
}
