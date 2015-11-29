package org.skaggs.ec.multiobjective.population;

import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Front<E> {
    private List<FrontedIndividual<E>> members;
    private int rank;

    public Front(List<FrontedIndividual<E>> members, int rank) {
        this.members = members;
        this.rank = rank;
    }
}
