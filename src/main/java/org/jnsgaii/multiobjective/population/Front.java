package org.jnsgaii.multiobjective.population;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mitchell Skaggs on 11/28/2015.
 */
public class Front<E> implements Comparable<Front<E>> {
    final SortedArrayList<FrontedIndividual<E>> members;
    private final int rank;

    @SuppressWarnings("unused")
    private Front() {
        this(null, -1);
    }

    public Front(SortedArrayList<FrontedIndividual<E>> members, int rank) {
        this.members = members;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Rank " + this.rank;
    }

    @Override
    public int compareTo(Front<E> o) {
        return Integer.compare(this.rank, o.rank);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Front<?> front = (Front<?>) o;

        if (rank != front.rank) return false;
        return members != null ? members.equals(front.members) : front.members == null;

    }

    @Override
    public int hashCode() {
        int result = members != null ? members.hashCode() : 0;
        result = 31 * result + rank;
        return result;
    }

    public List<FrontedIndividual<E>> getMembers() {
        return Collections.unmodifiableList(this.members);
    }
}
