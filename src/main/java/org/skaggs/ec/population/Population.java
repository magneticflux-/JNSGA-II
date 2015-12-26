package org.skaggs.ec.population;

import org.skaggs.ec.properties.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Population<E> {

    public List<? extends Individual<E>> population;

    protected Population() {
    }

    public Population(int size, PopulationGenerator<E> populationGenerator, Properties properties) {
        this.population = populationGenerator.generatePopulation(size, properties);
    }

    public Population(Population<E> population) {
        this.population = new ArrayList<>(population.population);
    }

    public Population(List<? extends Individual<E>> individuals) {
        this.population = new ArrayList<>(individuals);
    }

    public static <E> Population<E> merge(Population<E> population1, Population<E> population2) {
        return new Population<>((ArrayList<Individual<E>>) Stream.concat(population1.population.stream(), population2.population.stream()).collect(Collectors.toCollection(ArrayList::new)));
    }

    public List<? extends Individual<E>> getPopulation() {
        return Collections.unmodifiableList(this.population);
    }
}
