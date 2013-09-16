/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Type-safe extension of Query interface.
 *
 * @author Alexander Budarov
 * @version $Id$
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

    /**
     * Set View for this Query instance.
     * <p/> All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * @param viewName      view name - must not be null
     * @return the same query instance
     */
    Query setViewName(String viewName);

    /**
     * Adds View for this Query instance.
     * <p/> All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * @param viewName      view name - must not be null
     * @return the same query instance
     */
    Query addViewName(String viewName);
    /**
     * Execute a SELECT query.<br/>
     * Returns null if there is no result. <br/>
     * Returns first result if more than one result.
     *
     * @return the result
     * @throws IllegalStateException if called for a Java Persistence query language UPDATE or DELETE statement
     */
    @Nullable
    T getFirstResult();
}