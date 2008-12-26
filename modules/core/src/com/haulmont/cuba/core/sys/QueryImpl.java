/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 18:15:55
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.global.View;

import javax.persistence.TemporalType;
import javax.persistence.FlushModeType;
import java.util.List;
import java.util.Date;

import org.apache.openjpa.persistence.OpenJPAQuery;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueryImpl implements Query
{
    private Log log = LogFactory.getLog(QueryImpl.class);

    private OpenJPAEntityManager em;
    private OpenJPAQuery query;
    private boolean isNative;
    private String queryString;

    public QueryImpl(OpenJPAEntityManager entityManager, boolean isNative) {
        this.em = entityManager;
        this.isNative = isNative;
    }

    private OpenJPAQuery getQuery() {
        if (query == null) {
            if (isNative) {
                log.trace("Creating SQL query: " + queryString);
                query = em.createNativeQuery(queryString);
                query.setFlushMode(FlushModeType.COMMIT);
            }
            else {
                log.trace("Creating JPQL query: " + queryString);
                query = em.createQuery(queryString);
                query.setFlushMode(FlushModeType.COMMIT);
            }
        }
        return query;
    }

    public List getResultList() {
        return getQuery().getResultList();
    }

    public Object getSingleResult() {
        return getQuery().getSingleResult();
    }

    public int executeUpdate() {
        return getQuery().executeUpdate();
    }

    public Query setMaxResults(int maxResult) {
        getQuery().setMaxResults(maxResult);
        return this;
    }

    public Query setFirstResult(int startPosition) {
        getQuery().setFirstResult(startPosition);
        return this;
    }

    public Query setParameter(String name, Object value) {
        getQuery().setParameter(name, value);
        return this;
    }

    public Query setParameter(String name, Date value, TemporalType temporalType) {
        getQuery().setParameter(name, value, temporalType);
        return this;
    }

    public Query setParameter(int position, Object value) {
        getQuery().setParameter(position, value);
        return this;
    }

    public Query setParameter(int position, Date value, TemporalType temporalType) {
        getQuery().setParameter(position, value, temporalType);
        return this;
    }

    public Query setView(View view) {
        ViewHelper.setView(getQuery().getFetchPlan(), view);
        return this;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        if (query != null)
            throw new IllegalStateException("Unable to set query string: query is already created");
        this.queryString = queryString;
    }
}
