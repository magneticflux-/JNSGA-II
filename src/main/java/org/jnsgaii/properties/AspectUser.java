package org.jnsgaii.properties;

import org.jnsgaii.operators.Mutator;
import org.jnsgaii.util.Range;

import java.util.Random;

/**
 * Created by Mitchell Skaggs on 3/22/2016.
 */
public abstract class AspectUser implements HasAspectRequirements, HasPropertyRequirements {
    protected int startIndex;
    protected double[] aspectModificationArray;

    public static void mutateAspect(double[] aspectModificationArray, double[] aspects, int index, Random r, double min, double max) {
        if (r.nextDouble() < aspectModificationArray[index * 2 + 1])
            aspects[index] = Range.clip(min, Mutator.mutate(aspects[index], r, aspectModificationArray[index * 2]), max);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Subclasses that override this must call super to set the startIndex properly!
     */
    @Override
    public int requestAspectLocation(int startIndex) {
        this.startIndex = startIndex;
        return 0;
    }

    public abstract void modifyAspects(double[] aspects, Random r);

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
