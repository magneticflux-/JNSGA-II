package org.jnsgaii.operators.speciation;

import org.jnsgaii.population.individual.Individual;

import java.util.Random;

/**
 * Created by Mitchell on 11/26/2016.
 */
public class SpeciatorExWrappedSpeciator<E> extends DistanceSpeciatorEx<E> {
    private final Speciator<E> wrappedSpeciator;

    public SpeciatorExWrappedSpeciator(Speciator<E> wrappedSpeciator) {
        this.wrappedSpeciator = wrappedSpeciator;
    }

    @Override
    protected double getDistance(Individual<E> first, Individual<E> second) {
        return wrappedSpeciator.getDistance(first, second);
    }

    @Override
    protected double getMaxDistance(Individual<E> first, Individual<E> second) {
        return wrappedSpeciator.getMaxDistance(first, second);
    }

    @Override
    public String[] getAspectDescriptions() {
        return wrappedSpeciator.getAspectDescriptions();
    }

    @Override
    public void modifyAspects(double[] aspects, Random r) {
        wrappedSpeciator.modifyAspects(aspects, r);
    }
}
