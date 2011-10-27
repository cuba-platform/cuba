/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core;

import java.util.List;

/**
 * Type-safe extension of Query interface.
 * <p/>
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public interface TypedQuery<T> extends Query {

    /**
     * Execute a SELECT query and return the query results as a List.
     *
     * @return a list of the results
     * @throws IllegalStateException if called for a Java Persistence query language UPDATE or DELETE statement
     */
    List<T> getResultList();

    /**
     * Execute a SELECT query that returns a single result.
     *
     * @return the result
     * @throws javax.persistence.NoResultException
     *                               if there is no result
     * @throws javax.persistence.NonUniqueResultException
     *                               if more than one result
     * @throws IllegalStateException if called for a Java Persistence query language UPDATE or DELETE statement
     */
    T getSingleResult();

}
