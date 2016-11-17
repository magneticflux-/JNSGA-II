package org.jnsgaii.observation;

/**
 * Created by Mitchell Skaggs on 6/18/2016.
 */
public interface EvolutionObservable<E> {
    boolean addObserver(EvolutionObserver<E> observer);

    boolean removeObserver(EvolutionObserver<E> observer);
}
