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

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.sys.persistence.PostgresUUID;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.global.*;

import javax.persistence.TemporalType;
import javax.persistence.FlushModeType;
import java.util.*;
import java.sql.SQLException;

import org.apache.openjpa.persistence.OpenJPAQuery;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueryImpl implements Query
{
    private Log log = LogFactory.getLog(QueryImpl.class);

    private EntityManagerImpl em;
    private OpenJPAEntityManager emDelegate;
    private OpenJPAQuery query;
    private boolean isNative;
    private String queryString;

    private Collection<QueryMacroHandler> macroHandlers;

    public QueryImpl(EntityManagerImpl entityManager, boolean isNative) {
        this.em = entityManager;
        this.emDelegate = entityManager.getDelegate();
        this.isNative = isNative;
        this.macroHandlers = AppContext.getBeansOfType(QueryMacroHandler.class).values();
    }

    private OpenJPAQuery getQuery() {
        if (query == null) {
            if (isNative) {
                log.trace("Creating SQL query: " + queryString);
                query = emDelegate.createNativeQuery(queryString);
                query.setFlushMode(FlushModeType.COMMIT);
            }
            else {
                log.trace("Creating JPQL query: " + queryString);
                String s = transformQueryString();
                log.trace("Transformed JPQL query: " + s);
                query = emDelegate.createQuery(s);
                query.setFlushMode(FlushModeType.COMMIT);
            }
        }
        return query;
    }

    private String transformQueryString() {
        String result = expandMacros();

        if (!em.isSoftDeletion())
            return result;

        OpenJPAQuery tmpQuery = emDelegate.createQuery(result);
        Class cls = tmpQuery.getResultClass();
        if (cls == null
                || !BaseEntity.class.isAssignableFrom(cls)
                || !PersistenceHelper.isSoftDeleted(cls))
        {
            return result;
        } else {
            String entityName = PersistenceHelper.getEntityName(cls);
            QueryTransformer transformer = QueryTransformerFactory.createTransformer(result, entityName);
            transformer.addWhere("e.deleteTs is null");
            return transformer.getResult();
        }
    }

    private String expandMacros() {
        String result = queryString;
        if (macroHandlers != null) {
            for (QueryMacroHandler handler : macroHandlers) {
                result = handler.expandMacro(result);
            }
        }
        return result;
    }

    private void addMacroParams(OpenJPAQuery jpaQuery) {
        if (macroHandlers != null) {
            for (QueryMacroHandler handler : macroHandlers) {
                handler.setQueryParams(getQuery().getNamedParameters());
                for (Map.Entry<String, Object> entry : handler.getParams().entrySet()) {
                    jpaQuery.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public List getResultList() {
        if (!isNative && log.isTraceEnabled())
            log.trace("JPQL query result class: " + getQuery().getResultClass());
        OpenJPAQuery jpaQuery = getQuery();
        addMacroParams(jpaQuery);
        return jpaQuery.getResultList();
    }

    public Object getSingleResult() {
        if (!isNative && log.isTraceEnabled())
            log.trace("JPQL query result class: " + getQuery().getResultClass());

        OpenJPAQuery jpaQuery = getQuery();
        addMacroParams(jpaQuery);
        return jpaQuery.getSingleResult();
    }

    public int executeUpdate() {
        OpenJPAQuery jpaQuery = getQuery();
        addMacroParams(jpaQuery);
        return jpaQuery.executeUpdate();
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
        if (value instanceof BaseEntity)
            value = ((BaseEntity) value).getId();
        getQuery().setParameter(name, value);
        return this;
    }

    public Query setParameter(String name, Date value, TemporalType temporalType) {
        getQuery().setParameter(name, value, temporalType);
        return this;
    }

    public Query setParameter(int position, Object value) {
        if (value instanceof BaseEntity)
            value = ((BaseEntity) value).getId();
        else if (isNative
                && value instanceof UUID
                && PersistenceProvider.getDbDialect() instanceof PostgresDbDialect) {
            try {
                value = new PostgresUUID((UUID) value);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
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

    public Query addView(View view) {
        ViewHelper.addView(getQuery().getFetchPlan(), view);
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
