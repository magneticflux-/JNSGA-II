package org.skaggs.ec.properties;

/**
 * Created by Mitchell on 3/13/2016.
 */
public interface HasAspectRequirements {

    /**
     * Gives information to this object about where to access the aspect array for an individual to retrieve and store data. Also used to determine how large the aspect array should be.
     *
     * @param startIndex the location in the aspect array that is used for this object
     * @return the size of the area requested.
     */
    int requestAspectLocation(int startIndex);
}
