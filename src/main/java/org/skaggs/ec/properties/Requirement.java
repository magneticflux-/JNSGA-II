package org.skaggs.ec.properties;

import java.util.function.Predicate;

/**
 * Created by Mitchell on 1/20/2016.
 */
public interface Requirement extends Predicate<Properties> {
    String describe();
}
