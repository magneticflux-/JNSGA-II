package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.population.EvaluatedIndividual;
import org.skaggs.ec.population.EvaluatedPopulation;

import java.util.*;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class FrontedPopulation<E> extends EvaluatedPopulation<E> {

    protected List<Front<E>> fronts;

    public FrontedPopulation(List<FrontedIndividual<E>> population, List<Front<E>> fronts) {
        this.fronts = fronts;
        this.population = population;
    }

    public FrontedPopulation(EvaluatedPopulation<E> population, List<OptimizationFunction<E>> optimizationFunctions) {
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

            frontedIndividual.rank = -1; // Unset rank
            frontedIndividual.dominationCount = 0;
            frontedIndividual.dominatedIndividuals = new ArrayList<>();
            frontedIndividual.crowdingScore = 0;

            castPopulationView.add(frontedIndividual);
        }

        Front<E> firstFront = new Front<>(new TreeSet<>(), 0);
        this.fronts.add(0, firstFront);

        // Start computing the crowding distance

        for (OptimizationFunction<E> optimizationFunction : optimizationFunctions) {
            // Sorts the population according to the comparator
            Collections.sort(castPopulationView, (o1, o2) -> -optimizationFunction.compare(o1.getScores().get(optimizationFunction), o2.getScores().get(optimizationFunction))); // Lowest first
            // First and last have priority with the crowding score
            castPopulationView.get(0).crowdingScore = Double.POSITIVE_INFINITY;
            castPopulationView.get(castPopulationView.size() - 1).crowdingScore = Double.POSITIVE_INFINITY;

            for (int i = 1; i < castPopulationView.size() - 1; i++) { // Don't check the outside ones
                double toAdd = (castPopulationView.get(i + 1).getScore(optimizationFunction) - castPopulationView.get(i - 1).getScore(optimizationFunction)) / (optimizationFunction.max() - optimizationFunction.min());
                if (toAdd < 0)
                    System.out.println("Negative score with " + castPopulationView.get(i) + " ");
                castPopulationView.get(i).crowdingScore += toAdd;
            }
        }

        // Start ranking individuals

        for (FrontedIndividual<E> individual : castPopulationView) {
            for (FrontedIndividual<E> other : castPopulationView) {
                if (other == individual) continue;
                if (individual.dominates(other)) {
                    individual.dominatedIndividuals.add(other);
                } else if (other.dominates(individual)) {
                    individual.dominationCount++;
                }
            }
            if (individual.dominationCount == 0) { // Add it to the first front (Front 0). That front has RANK 0, is at POSITION 0, and the individual has RANK 0
                individual.rank = 0;
                this.fronts.get(0).members.add(individual);
            }
        }

        // Start establishing Fronts from ranked individuals

        int currentFrontRank = 0;
        while (!this.fronts.get(currentFrontRank).members.isEmpty()) {
            TreeSet<FrontedIndividual<E>> nextFront = new TreeSet<>();

            for (FrontedIndividual<E> individual : this.fronts.get(currentFrontRank).members) {
                for (FrontedIndividual<E> dominated : individual.dominatedIndividuals) {
                    dominated.dominationCount--;
                    if (dominated.dominationCount == 0) {
                        dominated.rank = currentFrontRank + 1; // Part of the next front
                        nextFront.add(dominated);
                    }
                }
            }
            this.fronts.add(currentFrontRank + 1, new Front<>(nextFront, currentFrontRank + 1));
            currentFrontRank++;
        }
    }

    public List<Front<E>> getFronts() {
        return Collections.unmodifiableList(fronts);
    }

    public void sort() {
        //noinspection unchecked
        Collections.sort(((List<FrontedIndividual<E>>) this.population));
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
}
