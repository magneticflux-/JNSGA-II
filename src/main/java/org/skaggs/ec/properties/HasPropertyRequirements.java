package org.skaggs.ec.properties;

/**
 * Created by skaggsm on 11/26/15.
 */
@FunctionalInterface
public interface HasPropertyRequirements {

    /**
     * This method requests certain properties by providing an array of {@link Key}s. If any keys are not supplied, an exception will be thrown.
     *
     * @return an array of requests
     */
    Key[] requestProperties();

    default Requirement[] requestDetailedRequirements() {
        return new Requirement[0];
    }
}
