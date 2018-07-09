/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.core.global;

import javax.persistence.TemporalType;
import java.util.*;

class AbstractFluentValueLoader {

    protected DataManager dataManager;
    private boolean transactional;

    private String store;
    private String queryString;
    private boolean softDeletion = true;
    private Map<String, Object> parameters = new HashMap<>();
    private Set<String> noConversionParams = new HashSet<>();
    private int firstResult;
    private int maxResults;

    AbstractFluentValueLoader(String queryString, DataManager dataManager, boolean transactional) {
        this.queryString = queryString;
        this.dataManager = dataManager;
        this.transactional = transactional;
    }

    protected ValueLoadContext createLoadContext() {
        ValueLoadContext loadContext = ValueLoadContext.create();
        if (store != null)
            loadContext.setStoreName(store);
        loadContext.setSoftDeletion(softDeletion);

        ValueLoadContext.Query query = ValueLoadContext.createQuery(queryString);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (noConversionParams.contains(entry.getKey()))
                query.setParameter(entry.getKey(), entry.getValue(), false);
            else
                query.setParameter(entry.getKey(), entry.getValue());
        }
        loadContext.setQuery(query);

        loadContext.getQuery().setFirstResult(firstResult);
        loadContext.getQuery().setMaxResults(maxResults);

        loadContext.setJoinTransaction(transactional);

        return loadContext;
    }

    /**
     * Sets DataStore name.
     */
    public AbstractFluentValueLoader store(String store) {
        this.store = store;
        return this;
    }

    /**
     * Sets soft deletion. The soft deletion is true by default.
     */
    public AbstractFluentValueLoader softDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
        return this;
    }

    /**
     * Sets value for a query parameter.

     * @param name  parameter name
     * @param value parameter value
     */
    public AbstractFluentValueLoader parameter(String name, Object value) {
        parameters.put(name, value);
        return this;
    }

    /**
     * Sets value for a parameter of {@code java.util.Date} type.

     * @param name  parameter name
     * @param value parameter value
     * @param temporalType  how to interpret the value
     */
    public AbstractFluentValueLoader parameter(String name, Date value, TemporalType temporalType) {
        parameters.put(name, new TemporalValue(value, temporalType));
        return this;
    }

    /**
     * Sets value for a query parameter.

     * @param name  parameter name
     * @param value parameter value
     * @param implicitConversion whether to do parameter value conversions, e.g. convert an entity to its ID
     */
    public AbstractFluentValueLoader parameter(String name, Object value, boolean implicitConversion) {
        parameters.put(name, value);
        if (!implicitConversion) {
            noConversionParams.add(name);
        }
        return this;
    }

    /**
     * Sets the map of query parameters.
     */
    public AbstractFluentValueLoader setParameters(Map<String, Object> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    /**
     * Sets results offset.
     */
    public AbstractFluentValueLoader firstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets results limit.
     */
    public AbstractFluentValueLoader maxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }
}
