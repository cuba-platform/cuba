/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.annotation.Nullable;
import javax.persistence.LockModeType;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Type-safe extension of Query interface.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
public interface TypedQuery<T> extends Query {

    @Override
    List<T> getResultList();

    @Override
    T getSingleResult();

    @Override
    @Nullable
    T getFirstResult();

    @Override
    TypedQuery<T> setMaxResults(int maxResult);

    @Override
    TypedQuery<T> setFirstResult(int startPosition);

    @Override
    TypedQuery<T> setParameter(String name, Object value);

    @Override
    TypedQuery<T> setParameter(String name, Object value, boolean implicitConversions);

    @Override
    TypedQuery<T> setParameter(String name, Date value, TemporalType temporalType);

    @Override
    TypedQuery<T> setParameter(int position, Object value);

    @Override
    TypedQuery<T> setParameter(int position, Object value, boolean implicitConversions);

    @Override
    TypedQuery<T> setParameter(int position, Date value, TemporalType temporalType);

    @Override
    TypedQuery<T> setLockMode(LockModeType lockMode);

    @Override
    TypedQuery<T> setView(@Nullable View view);

    @Override
    TypedQuery<T> setView(Class<? extends Entity> entityClass, String viewName);

    @Override
    TypedQuery<T> addView(View view);

    @Override
    TypedQuery<T> addView(Class<? extends Entity> entityClass, String viewName);

    /**
     * Set View for this Query instance.
     * <p/> All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * @param viewName      view name - must not be null
     * @return the same query instance
     */
    TypedQuery<T> setViewName(String viewName);

    /**
     * Adds View for this Query instance.
     * <p/> All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * @param viewName      view name - must not be null
     * @return the same query instance
     */
    TypedQuery<T> addViewName(String viewName);
}