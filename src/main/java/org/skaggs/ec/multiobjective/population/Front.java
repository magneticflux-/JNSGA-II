package org.skaggs.ec.multiobjective.population;

import java.util.Collections;
import java.util.SortedSet;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Front<E> implements Comparable<Front<E>> {
    final SortedSet<FrontedIndividual<E>> members;
    private final int rank;

    public Front(SortedSet<FrontedIndividual<E>> members, int rank) {
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

    public SortedSet<FrontedIndividual<E>> getMembers() {
        return Collections.unmodifiableSortedSet(members);
    }
}
