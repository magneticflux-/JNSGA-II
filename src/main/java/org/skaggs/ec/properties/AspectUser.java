package org.skaggs.ec.properties;

import org.skaggs.ec.population.individual.Individual;

import java.util.Random;

/**
 * Created by Mitchell on 3/22/2016.
 */
public abstract class AspectUser<E> implements HasAspectRequirements, HasPropertyRequirements {
    protected int startIndex;
    protected double[] aspectModificationArray;

    @Override
    public int requestAspectLocation(int startIndex) {
        this.startIndex = startIndex;
        return 0;
    }

    public abstract void modifyAspects(Individual<E> individual, Random r);

    public void updateProperties(Properties properties) {
        aspectModificationArray = (double[]) properties.getValue(Key.DoubleKey.DefaultDoubleKey.ASPECT_MODIFICATION_ARRAY);
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.DefaultDoubleKey.ASPECT_MODIFICATION_ARRAY
        };
    }

    @Override
    public Requirement[] requestDetailedRequirements() {
        return new Requirement[]{
        };
    }
}
