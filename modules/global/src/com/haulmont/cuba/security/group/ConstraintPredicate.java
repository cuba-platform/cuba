package com.haulmont.cuba.security.group;

import com.haulmont.cuba.core.entity.Entity;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * Represents access constraint in-memory predicate
 */
@FunctionalInterface
public interface ConstraintPredicate<T extends Entity> extends Predicate<T>, Serializable {
}
