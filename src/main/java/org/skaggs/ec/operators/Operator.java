package org.skaggs.ec.operators;

import org.skaggs.ec.properties.HasPropertyRequirements;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface Operator<E> extends HasPropertyRequirements {

    /**
     * This method applied the operation to an array of appropriate size. The appropriate size is determined by any implementors of this.
     *
     * @param objects the objects to be modified
     * @return an array of modified objects
     */
    E[] apply(E... objects);
}
