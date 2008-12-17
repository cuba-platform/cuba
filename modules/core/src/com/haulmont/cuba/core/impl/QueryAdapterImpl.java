/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 18:15:55
 *
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import com.haulmont.cuba.core.QueryAdapter;

import javax.persistence.TemporalType;
import javax.persistence.FlushModeType;
import java.util.List;
import java.util.Date;

import org.apache.openjpa.persistence.OpenJPAQuery;

public class QueryAdapterImpl implements QueryAdapter
{
    private OpenJPAQuery query;

    public QueryAdapterImpl(OpenJPAQuery query) {
        this.query = query;
        this.query.setFlushMode(FlushModeType.COMMIT);
    }

    public List getResultList() {
        return query.getResultList();
    }

    public Object getSingleResult() {
        return query.getSingleResult();
    }

    public int executeUpdate() {
        return query.executeUpdate();
    }

    public QueryAdapter setMaxResults(int maxResult) {
        query.setMaxResults(maxResult);
        return this;
    }

    public QueryAdapter setFirstResult(int startPosition) {
        query.setFirstResult(startPosition);
        return this;
    }

    public QueryAdapter setParameter(String name, Object value) {
        query.setParameter(name, value);
        return this;
    }

    public QueryAdapter setParameter(String name, Date value, TemporalType temporalType) {
        query.setParameter(name, value, temporalType);
        return this;
    }

    public QueryAdapter setParameter(int position, Object value) {
        query.setParameter(position, value);
        return this;
    }

    public QueryAdapter setParameter(int position, Date value, TemporalType temporalType) {
        query.setParameter(position, value, temporalType);
        return this;
    }
}
