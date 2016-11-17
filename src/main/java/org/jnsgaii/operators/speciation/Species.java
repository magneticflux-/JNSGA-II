package org.jnsgaii.operators.speciation;

import java.util.Collections;
import java.util.Set;

/**
 * Created by skaggsm on 11/14/16.
 */
public class Species {
    private final long id;
    private final Set<Long> individualIDs;

    public Species(Set<Long> individualIDs, long id) {
        this.individualIDs = Collections.unmodifiableSet(individualIDs);
        this.id = id;
    }

    public Set<Long> getIndividualIDs() {
        return individualIDs;
    }

    public long getId() {
        return id;
    }
}
