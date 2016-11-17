package org.jnsgaii.observation;

import org.jnsgaii.population.PopulationData;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Mitchell Skaggs on 6/18/2016.
 */
public class DummyNSGAII<E> implements EvolutionObservable<E> {
    private final List<EvolutionObserver<E>> observers;
    private Iterator<PopulationData<E>> populationDataIterator;
    private int currentGeneration;

    public DummyNSGAII(Stream<PopulationData<E>> populationDataStream) {
        this.observers = new LinkedList<>();
        this.populationDataIterator = populationDataStream.iterator();
        this.currentGeneration = 0;
    }

    public int getCurrentGeneration() {
        return this.currentGeneration;
    }

    public boolean loadGeneration() {
        PopulationData<E> populationData = populationDataIterator.next();
        for (EvolutionObserver<E> observer : this.observers) {
            observer.update(populationData);
        }
        this.currentGeneration++;

        return populationDataIterator.hasNext();
    }

    @Override
    public boolean addObserver(EvolutionObserver<E> observer) {
        return observers.add(observer);
    }

    @Override
    public boolean removeObserver(EvolutionObserver<E> observer) {
        return observers.remove(observer);
    }
}
