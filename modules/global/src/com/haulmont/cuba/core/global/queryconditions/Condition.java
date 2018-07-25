package com.haulmont.cuba.core.global.queryconditions;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * The tree of {@code Condition}s represents an optional part of a query that is added if the corresponding parameters
 * are present.
 */
public interface Condition extends Serializable, Cloneable {

    /**
     * Returns parameters specified in the condition.
     */
    Collection<String> getParameters();

    /**
     * Returns the condition if the argument contains all parameters specified in the condition.
     */
    @Nullable
    Condition actualize(Set<String> actualParameters);
}
