package org.skaggs.ec.operators;

import org.skaggs.ec.properties.Key;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface Operator<E> {
    /**
     * This method applied the operation to an array of appropriate size. The appropriate size is determined by any implementors of this.
     *
     * @param objects the objects to be modified
     * @return an array of modified objects
     */
    E[] apply(E... objects);

    /**
     * This method requests certain properties by providing an array of {@link Key}s. If any keys are not supplied, an exception will be thrown.
     *
     * @return an array of requests
     */
    Key[] requestProperties();
}
