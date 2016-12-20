package org.jnsgaii.operators.speciation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mitchell Skaggs on 11/14/16.
 */
public class Species {
    private long id;
    private Set<Long> individualIDs, unmodifiableIndividualIDs;
    private boolean frozen;

    private Species() {
    }

    public Species(Set<Long> individualIDs, long id) {
        this(individualIDs, id, true);
    }

    public Species(Set<Long> individualIDs, long id, boolean frozen) {
        this.individualIDs = new HashSet<>(individualIDs);
        this.unmodifiableIndividualIDs = Collections.unmodifiableSet(this.individualIDs);
        this.id = id;
        this.frozen = frozen;
    }

    public Species(Species s) {
        this(s.individualIDs, s.id, s.frozen);
    }

    public Set<Long> getIndividualIDs() {
        return frozen ? unmodifiableIndividualIDs : individualIDs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (frozen)
            throw new UnsupportedOperationException();
        else
            this.id = id;
    }

    public Species thaw() {
        this.frozen = false;
        return this;
    }

    public Species freeze() {
        this.frozen = true;
        return this;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + individualIDs.hashCode();
        result = 31 * result + unmodifiableIndividualIDs.hashCode();
        result = 31 * result + (frozen ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Species species = (Species) o;

        if (id != species.id) return false;
        if (frozen != species.frozen) return false;
        if (!individualIDs.equals(species.individualIDs)) return false;
        return unmodifiableIndividualIDs.equals(species.unmodifiableIndividualIDs);

    }

    @Override
    public String toString() {
        return "Species{" +
                "id=" + id +
                ", individualIDs=" + individualIDs +
                ", frozen=" + frozen +
                '}';
    }
}
