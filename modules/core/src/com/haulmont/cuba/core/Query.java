/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 18:02:40
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.View;

import javax.persistence.LockModeType;
import javax.persistence.TemporalType;
import java.util.List;
import java.util.Date;

/**
 * Interface to control query execution.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Query {

    /**
     * Get the query string.
     * @return  query string
     */
    String getQueryString();

    /**
     * Set the query string.
     * @param queryString   query string
     */
    void setQueryString(String queryString);

    /**
     * Execute a SELECT query and return the query results as a List.
     * @return a list of the results
     * @throws IllegalStateException if called for a Java Persistence query language UPDATE or DELETE statement
     */
    List getResultList();

    /**
     * Execute a SELECT query that returns a single result.
     * @return the result
     * @throws javax.persistence.NoResultException if there is no result
     * @throws javax.persistence.NonUniqueResultException if more than one result
     * @throws IllegalStateException if called for a Java Persistence query language UPDATE or DELETE statement
     */
    Object getSingleResult();

    /**
     * Execute an update or delete statement.
     * @return the number of entities updated or deleted
     * @throws IllegalStateException if called for a Java Persistence query language SELECT statement
     * @throws javax.persistence.TransactionRequiredException if there is no transaction
     */
    int executeUpdate();

    /**
     * Set the maximum number of results to retrieve.
     * @param maxResult
     * @return the same query instance
     * @throws IllegalArgumentException if argument is negative
     */
    Query setMaxResults(int maxResult);

    /**
     * Set the position of the first result to retrieve.
     * @param startPosition position of the first result, numbered from 0
     * @return the same query instance
     * @throws IllegalArgumentException if argument is negative
     */
    Query setFirstResult(int startPosition);

    /**
     * Bind an argument to a named parameter.<br>
     * Native Query doesn't support named parameters.
     * @param name                      parameter name
     * @param value                     parameter value. Entity instance replaced with its ID.
     * @return the same query instance
     * @throws IllegalArgumentException if parameter name does not correspond to parameter in query string
     * or argument is of incorrect type
     */
    Query setParameter(String name, Object value);

    /**
     * Bind an argument to a named parameter.<br>
     * Native Query doesn't support named parameters.
     * @param name                      parameter name
     * @param value                     parameter value
     * @param implicitConversions       whether to make parameter value conversions, e.g. convert an entity to its ID
     * @return the same query instance
     * @throws IllegalArgumentException if parameter name does not correspond to parameter in query string
     * or argument is of incorrect type
     */
    Query setParameter(String name, Object value, boolean implicitConversions);

    /**
     * Bind an instance of java.util.Date to a named parameter.<br>
     * Native Query doesn't support named parameters.
     * @param name
     * @param value
     * @param temporalType
     * @return the same query instance
     * @throws IllegalArgumentException if parameter name does not correspond to parameter in query string
     */
    Query setParameter(String name, Date value, TemporalType temporalType);

    /**
     * Bind an argument to a positional parameter.
     * @param position                  parameter position, starting with 1
     * @param value                     parameter value. Entity instance replaced with its ID.
     * @return the same query instance
     * @throws IllegalArgumentException if position does not correspond to positional parameter of query
     * or argument is of incorrect type
     */
    Query setParameter(int position, Object value);

    /**
     * Bind an argument to a positional parameter.
     * @param position                  parameter position, starting with 1
     * @param value                     parameter value
     * @param implicitConversions       whether to make parameter value conversions, e.g. convert an entity to its ID
     * @return the same query instance
     * @throws IllegalArgumentException if position does not correspond to positional parameter of query
     * or argument is of incorrect type
     */
    Query setParameter(int position, Object value, boolean implicitConversions);

    /**
     * Bind an instance of java.util.Date to a positional parameter.
     * @param position
     * @param value
     * @param temporalType
     * @return the same query instance
     * @throws IllegalArgumentException if position does not correspond to positional parameter of query
     */
    Query setParameter(int position, Date value, TemporalType temporalType);

    /**
     * Set the lock mode type to be used for the query execution.
     * @param lockMode  lock mode
     * @return          the same query instance
     */
    Query setLockMode(LockModeType lockMode);

    /**
     * Set View for this Query instance.
     * @param view view instance. May be null, in this case eager fetching will be performed according to JPA mappings.
     * @return the same query instance
     */
    Query setView(View view);

    /**
     * Adds View for this Query instance.<br/>
     * Eager fetching will be performed for fields specified in all added views.
     * @param view view instance - must not be null
     * @return the same query instance
     */
    Query addView(View view);

    /**
     * @return  underlying implementation provided by ORM
     */
    javax.persistence.Query getDelegate();
}
