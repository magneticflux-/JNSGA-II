package org.jnsgaii.operators;

import org.apache.commons.lang3.time.StopWatch;
import org.jnsgaii.multiobjective.population.FrontedIndividual;
import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.speciation.SpeciatorEx;
import org.jnsgaii.operators.speciation.Species;
import org.jnsgaii.population.Population;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.*;
import org.jnsgaii.properties.Properties;
import org.jnsgaii.util.Utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Mitchell Skaggs on 1/22/16.
 */
public class DefaultOperator<E> implements Operator<E>, HasAspectRequirements {


    private final List<Mutator<E>> mutators;
    private final Recombiner<E> recombiner;
    private final Selector<E> selector;
    private final SpeciatorEx<E> speciatorEx;

    public DefaultOperator(List<Mutator<E>> mutators, Recombiner<E> recombiner, Selector<E> selector, SpeciatorEx<E> speciator) {
        this.mutators = new ArrayList<>(mutators);
        this.recombiner = recombiner;
        this.selector = selector;
        this.speciatorEx = speciator;
    }

    @Override
    public Population<E> apply(FrontedPopulation<E> population, Properties properties) {
        StopWatch stopWatch = new StopWatch();
        @SuppressWarnings("unchecked")
        List<FrontedIndividual<E>> castPopulation = (List<FrontedIndividual<E>>) population.getPopulation();

        stopWatch.start();
        mutators.forEach(mutator -> mutator.updateProperties(properties));
        recombiner.updateProperties(properties);
        speciatorEx.updateProperties(properties);
        selector.updateProperties(properties);

        setAspectIndices(getHasAspectRequirementses());
        stopWatch.stop();
        System.out.println("Property/Aspect Update Time: " + stopWatch.getTime() + "ms");
        stopWatch.reset();

        stopWatch.start();
        List<E> newPopulation = new ArrayList<>(population.getPopulation().size());
        List<double[]> newAspects = new ArrayList<>(population.getPopulation().size());

        Set<Species> species = population.getSpecies();
        Map<Long, Double> speciesIDToAverageRank = new HashMap<>(species.size(), 1f);
        for (Species s : species) {
            speciesIDToAverageRank.put(s.getId(),
                    s.getIndividualIDs().stream()
                            .mapToDouble(individualID -> population.size() - population.getIdToPopulationIndexMap().get(individualID)).average().orElseThrow(Error::new));
        }
        double totalAverageSpeciesFitness = speciesIDToAverageRank.entrySet().stream().mapToDouble(Map.Entry::getValue).sum();

        Map<Long, Integer> speciesToAllocatedIndividuals = new HashMap<>(species.size(), 1f);
        for (Map.Entry<Long, Double> e : speciesIDToAverageRank.entrySet()) {
            speciesToAllocatedIndividuals.put(e.getKey(), (int) (population.size() * e.getValue() / totalAverageSpeciesFitness));
        }
        int allocatedIndividualsSoFar = speciesToAllocatedIndividuals.entrySet().stream().mapToInt(Map.Entry::getValue).sum();
        int individualsToBeAllocatedStill = population.size() - allocatedIndividualsSoFar;
        if (individualsToBeAllocatedStill > 0) {
            Map.Entry<Long, Integer> smallestSpecies = speciesToAllocatedIndividuals.entrySet().stream().sorted((o1, o2) -> -Integer.compare(o1.getValue(), o2.getValue())).findFirst().orElseThrow(Error::new);
            smallestSpecies.setValue(smallestSpecies.getValue() + individualsToBeAllocatedStill);
        }

        for (Species s : species) {
            int numRequestedIndividuals = speciesToAllocatedIndividuals.get(s.getId());
            List<FrontedIndividual<E>> speciesMembers = s.getIndividualIDs().stream().map(population::getIndividualByID).sorted().collect(Collectors.toList()); //Sorted for proper selection
            for (int i = 0; i < numRequestedIndividuals; i++) {
                FrontedIndividual<E> firstIndividual = selector.apply(speciesMembers);
                FrontedIndividual<E> secondIndividual = selector.apply(speciesMembers);
                double[] offspringAspects = recombiner.apply(firstIndividual.aspects, secondIndividual.aspects);
                E offspring = recombiner.apply(firstIndividual.getIndividual(), secondIndividual.getIndividual(), offspringAspects);
                newAspects.add(offspringAspects);
                newPopulation.add(offspring);
            }
        }

        System.out.println("Mating produced " + newPopulation.size() + " individuals from " + species.size() + " species");

        /*
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
        */

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

        List<Individual<E>> newIndividuals = new ArrayList<>(population.getPopulation().size() * 2);
        long currentIndividualID = population.getCurrentIndividualID();

        for (int i = 0; i < newPopulation.size(); i++) {
            newIndividuals.add(new Individual<>(newPopulation.get(i), newAspects.get(i), currentIndividualID++));
        }

        Set<Species> newSpecies = speciatorEx.getSpecies(population, newIndividuals, population.getCurrentSpeciesID());
        long currentSpeciesID = newSpecies.stream().mapToLong(Species::getId).max().orElseThrow(Error::new) + 1;

        newIndividuals.addAll(castPopulation);

        return new Population<>(newIndividuals, newSpecies, currentSpeciesID, currentIndividualID);
    }

    private static int setAspectIndices(HasAspectRequirements... hasAspectRequirementses) {
        int currentIndex = 0;
        for (HasAspectRequirements hasAspectRequirements : hasAspectRequirementses) {
            currentIndex += hasAspectRequirements.requestAspectLocation(currentIndex);
        }
        return currentIndex;
    }

    private HasAspectRequirements[] getHasAspectRequirementses() {
        return Utils.concat(new HasAspectRequirements[]{recombiner, selector, speciatorEx}, mutators.toArray(new HasAspectRequirements[mutators.size()]));
    }

    private AspectUser[] getAspectUsers() {
        //noinspection unchecked
        return Utils.concat(new AspectUser[]{recombiner, selector, speciatorEx}, mutators.toArray(new AspectUser[mutators.size()]));
    }

    @Override
    public Key[] requestProperties() {
        return Utils.concat(
                Utils.concat(mutators.stream().map(HasPropertyRequirements::requestProperties).toArray(Key[][]::new)),
                recombiner.requestProperties(),
                selector.requestProperties(),
                speciatorEx.requestProperties(),
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
                speciatorEx.requestDetailedRequirements(),
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
        return Utils.concat(recombiner.getAspectDescriptions(), selector.getAspectDescriptions(), speciatorEx.getAspectDescriptions(), mutators.stream().map(Mutator<E>::getAspectDescriptions).reduce(new String[0], Utils::concat));
    }
}
