package org.jnsgaii.operators;

import org.apache.commons.lang3.time.StopWatch;
import org.jnsgaii.multiobjective.population.FrontedIndividual;
import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.speciation.Speciator;
import org.jnsgaii.operators.speciation.Species;
import org.jnsgaii.population.Population;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.*;
import org.jnsgaii.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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

    private static int setAspectIndices(HasAspectRequirements... hasAspectRequirementses) {
        int currentIndex = 0;
        for (HasAspectRequirements hasAspectRequirements : hasAspectRequirementses) {
            currentIndex += hasAspectRequirements.requestAspectLocation(currentIndex);
        }
        return currentIndex;
    }

    @Override
    public Population<E> apply(FrontedPopulation<E> population, Properties properties) {
        StopWatch stopWatch = new StopWatch();
        @SuppressWarnings("unchecked")
        List<FrontedIndividual<E>> castPopulation = (List<FrontedIndividual<E>>) population.getPopulation();

        stopWatch.start();
        mutators.forEach(mutator -> mutator.updateProperties(properties));
        recombiner.updateProperties(properties);
        speciator.updateProperties(properties);
        selector.updateProperties(properties);

        setAspectIndices(getHasAspectRequirementses());
        stopWatch.stop();
        System.out.println("Property/Aspect Update Time: " + stopWatch.getTime() + "ms");
        stopWatch.reset();

        stopWatch.start();
        List<E> newPopulation = new ArrayList<>(population.getPopulation().size());
        List<double[]> newAspects = new ArrayList<>(population.getPopulation().size());

        for (int i = 0; i < population.getPopulation().size(); i++) {
            FrontedIndividual<E> individual = selector.apply(castPopulation);

            final FrontedIndividual<E> finalIndividual = individual;
            List<FrontedIndividual<E>> compatibleIndividuals = (castPopulation).stream()
                    .filter(eFrontedIndividual -> speciator.apply(finalIndividual, eFrontedIndividual) && !finalIndividual.equals(eFrontedIndividual))
                    .collect(Collectors.toList());
            while (compatibleIndividuals.size() == 0) {
                individual = selector.choose(castPopulation);

                final FrontedIndividual<E> finalIndividual1 = individual;
                compatibleIndividuals = (castPopulation).stream()
                        .filter(eFrontedIndividual -> speciator.apply(finalIndividual1, eFrontedIndividual))
                        .collect(Collectors.toList());
            }
            FrontedIndividual<E> otherIndividual = selector.apply(compatibleIndividuals);

            newAspects.add(recombiner.apply(individual.aspects, otherIndividual.aspects));
            newPopulation.add(recombiner.apply(individual.getIndividual(), otherIndividual.getIndividual(), newAspects.get(i)));
        }

        stopWatch.stop();
        System.out.println("Mating Time: " + stopWatch.getTime() + "ms");
        stopWatch.reset();

        stopWatch.start();
        for (Mutator<E> mutator : mutators)
            for (int i = 0; i < newPopulation.size(); i++)
                newPopulation.set(i, mutator.apply(newPopulation.get(i), newAspects.get(i)));
        stopWatch.stop();
        System.out.println("Mutation Time: " + stopWatch.getTime() + "ms");
        stopWatch.reset();

        stopWatch.start();
        for (AspectUser aspectUser : getAspectUsers())
            for (int i = 0; i < newPopulation.size(); i++)
                aspectUser.modifyAspects(newAspects.get(i), ThreadLocalRandom.current());
        stopWatch.stop();
        System.out.println("Aspect Modification Time: " + stopWatch.getTime());
        stopWatch.reset();

        List<Individual<E>> newIndividuals = new ArrayList<>();
        long currentIndividualID = population.getCurrentIndividualID();
        long currentSpeciesID = population.getCurrentSpeciesID();

        for (int i = 0; i < newPopulation.size(); i++) {
            newIndividuals.add(new Individual<>(newPopulation.get(i), newAspects.get(i), currentIndividualID++));
        }

        Set<Species> species = new HashSet<>();

        return new Population<>(newIndividuals, species, currentSpeciesID, currentIndividualID);
    }

    private HasAspectRequirements[] getHasAspectRequirementses() {
        return Utils.concat(new HasAspectRequirements[]{recombiner, selector, speciator}, mutators.toArray(new HasAspectRequirements[mutators.size()]));
    }

    private AspectUser[] getAspectUsers() {
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
