package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.population.EvaluatedPopulation;
import org.skaggs.ec.population.individual.EvaluatedIndividual;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Mitchell on 11/28/2015.
 */
@SuppressWarnings("AssignmentToSuperclassField")
public class FrontedPopulation<E> extends EvaluatedPopulation<E> {

    protected List<Front<E>> fronts;

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    protected FrontedPopulation(List<FrontedIndividual<E>> population, List<Front<E>> fronts) {
        this.fronts = fronts;
        this.population = population;
    }

    public FrontedPopulation(EvaluatedPopulation<E> population, Iterable<OptimizationFunction<E>> optimizationFunctions, Properties properties) {
        super();

        this.fronts = new ArrayList<>();
        this.population = new ArrayList<FrontedIndividual<E>>();

        /*
        A view of the population that has been cast to a list of FrontedIndividuals
        Generics just can't handle me right now. I can't override a variable, so I have to cast the List<Individual<>> to a more precise Individual type
        */
        @SuppressWarnings("unchecked") List<FrontedIndividual<E>> castPopulationView = (List<FrontedIndividual<E>>) this.population;

        for (EvaluatedIndividual<E> individual : population.getPopulation()) {
            FrontedIndividual<E> frontedIndividual = new FrontedIndividual<>(individual);
            castPopulationView.add(frontedIndividual);
        }

        boolean threaded = properties.getBoolean(Key.BooleanKey.THREADED);

        Front<E> firstFront = new Front<>(new TreeSet<>(), 0);
        this.fronts.add(0, firstFront);

        // Start computing the crowding distance

        for (OptimizationFunction<E> optimizationFunction : optimizationFunctions) {
            // Sorts the population according to the comparator
            Collections.sort(castPopulationView, (o1, o2) -> -optimizationFunction.compare(o1.getScores().get(optimizationFunction), o2.getScores().get(optimizationFunction))); // Lowest first
            // First and last have priority with the crowding score
            castPopulationView.get(0).crowdingScore = Double.POSITIVE_INFINITY;
            castPopulationView.get(castPopulationView.size() - 1).crowdingScore = Double.POSITIVE_INFINITY;

            for (int i = 1; i < (castPopulationView.size() - 1); i++) { // Don't check the outside ones
                if (Double.isFinite(castPopulationView.get(i).crowdingScore)) // Only add to it if it isn't an outlier on another function
                    castPopulationView.get(i).crowdingScore += (castPopulationView.get(i + 1).getScore(optimizationFunction) - castPopulationView.get(i - 1).getScore(optimizationFunction)) / (optimizationFunction.max(properties) - optimizationFunction.min(properties));
            }
        }

        // Start ranking individuals

        for (FrontedIndividual<E> individual : castPopulationView) { //TODO This takes 95% of the CPU time for each generation; optimize like crazy
            Stream<FrontedIndividual<E>> populationStream;

            if (threaded)
                populationStream = castPopulationView.parallelStream();
            else
                populationStream = castPopulationView.stream();

            populationStream.forEach(otherIndividual -> {
                if (otherIndividual == individual) return;
                int domination = individual.dominates(otherIndividual);
                switch (domination) {
                    case -1:
                        individual.dominationCount++;
                        break;
                    case 1:
                        synchronized (individual.dominatedIndividuals) {
                            individual.dominatedIndividuals.add(otherIndividual);
                        }
                        break;
                }
            });
            if (individual.dominationCount == 0) { // Add it to the first front (Front 0). That front has RANK 0, is at POSITION 0, and the individual has RANK 0
                individual.rank = 0;
                this.fronts.get(0).members.add(individual);
            }
        }

        // Start establishing Fronts from ranked individuals

        int currentFrontRank = 0;
        while (!this.fronts.get(currentFrontRank).members.isEmpty()) {
            TreeSet<FrontedIndividual<E>> nextFront = new TreeSet<>();
            final int finalCurrentFrontRank = currentFrontRank;

            Stream<FrontedIndividual<E>> frontStream;
            if (threaded)
                frontStream = this.fronts.get(currentFrontRank).members.parallelStream();
            else
                frontStream = this.fronts.get(currentFrontRank).members.stream();

            frontStream.forEach(individual -> {
                Stream<FrontedIndividual<E>> dominatedStream;
                if (threaded)
                    dominatedStream = individual.dominatedIndividuals.parallelStream();
                else
                    dominatedStream = individual.dominatedIndividuals.stream();

                dominatedStream.forEach(dominatedIndividual -> {
                    //noinspection SynchronizationOnLocalVariableOrMethodParameter
                    synchronized (dominatedIndividual) {
                        dominatedIndividual.dominationCount--;
                        if (dominatedIndividual.dominationCount == 0) {
                            dominatedIndividual.rank = finalCurrentFrontRank + 1; // Part of the next front
                            synchronized (nextFront) {
                                nextFront.add(dominatedIndividual);
                            }
                        }
                    }
                });
            });
            this.fronts.add(currentFrontRank + 1, new Front<>(nextFront, currentFrontRank + 1));
            currentFrontRank++;
        }
    }

    public List<Front<E>> getFronts() {
        return Collections.unmodifiableList(fronts);
    }

    @Override
    public String toString() {
        return this.population.toString();
    }

    @Override
    public List<? extends FrontedIndividual<E>> getPopulation() {
        // This SHOULD work, since the only constructor fills the List<> with FrontedIndividual<>s
        //noinspection unchecked
        return Collections.unmodifiableList((List<FrontedIndividual<E>>) this.population);
    }

    public FrontedPopulation<E> truncate(int limit) {
        this.sort();
        List<Front<E>> newFronts = new ArrayList<>();
        List<FrontedIndividual<E>> newPopulation = new ArrayList<>();

        int currentFront = 0;
        int numIndividuals = 0;

        while (currentFront < this.fronts.size() && numIndividuals + this.fronts.get(currentFront).members.size() <= limit) {
            newPopulation.addAll(this.fronts.get(currentFront).members);
            newFronts.add(currentFront, this.fronts.get(currentFront));
            numIndividuals += this.fronts.get(currentFront).members.size();
            currentFront++;
        }
        if (currentFront < this.fronts.size() && limit - numIndividuals > 0) {
            TreeSet<FrontedIndividual<E>> individuals = new TreeSet<>();
            Iterator<FrontedIndividual<E>> iterator = this.fronts.get(currentFront).members.iterator();
            for (int i = 0; i < limit - numIndividuals; i++) {
                FrontedIndividual<E> individual = iterator.next();
                //System.out.println("Current iterator return: " + individual);
                individuals.add(individual);
                newPopulation.add(individual);
            }
            newFronts.add(currentFront, new Front<>(individuals, currentFront));
        }
        return new FrontedPopulation<>(newPopulation, newFronts);
    }

    public void sort() {
        //noinspection unchecked
        Collections.sort(((List<FrontedIndividual<E>>) this.population));
    }
}
