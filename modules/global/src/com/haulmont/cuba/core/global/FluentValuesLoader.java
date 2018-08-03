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

import com.haulmont.cuba.core.entity.KeyValueEntity;

import javax.persistence.TemporalType;
import java.util.*;

public class FluentValuesLoader extends AbstractFluentValueLoader {

    private List<String> properties = new ArrayList<>();

    public FluentValuesLoader(String queryString, DataManager dataManager) {
        super(queryString, dataManager, false);
    }

    public FluentValuesLoader(String queryString, DataManager dataManager, boolean transactional) {
        super(queryString, dataManager, transactional);
    }

    protected ValueLoadContext createLoadContext() {
        ValueLoadContext loadContext = super.createLoadContext();
        loadContext.setProperties(properties);
        return loadContext;
    }

    /**
     * Loads a list of entities.
     */
    public List<KeyValueEntity> list() {
        ValueLoadContext loadContext = createLoadContext();
        return dataManager.loadValues(loadContext);
    }

    /**
     * Loads a single instance and wraps it in Optional.
     */
    public Optional<KeyValueEntity> optional() {
        ValueLoadContext loadContext = createLoadContext();
        loadContext.getQuery().setMaxResults(1);
        List<KeyValueEntity> list = dataManager.loadValues(loadContext);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    /**
     * Loads a single instance.
     *
     * @throws IllegalStateException if nothing was loaded
     */
    public KeyValueEntity one() {
        ValueLoadContext loadContext = createLoadContext();
        loadContext.getQuery().setMaxResults(1);
        List<KeyValueEntity> list = dataManager.loadValues(loadContext);
        if (!list.isEmpty())
            return list.get(0);
        else
            throw new IllegalStateException("No results");
    }

    /**
     * Adds a key of a returned key-value pair. The sequence of adding properties must conform to the sequence of
     * result fields in the query "select" clause.
     * <p>For example, if the query is <code>select e.id, e.name from sample$Customer</code>
     * and you executed <code>property("customerId").property("customerName")</code>, the returned KeyValueEntity
     * will contain customer identifiers in "customerId" property and names in "customerName" property.
     */
    public FluentValuesLoader property(String name) {
        properties.add(name);
        return this;
    }

    /**
     * The same as invoking {@link #property(String)} multiple times.
     */
    public FluentValuesLoader properties(List<String> properties) {
        this.properties.clear();
        this.properties.addAll(properties);
        return this;
    }

    /**
     * The same as invoking {@link #property(String)} multiple times.
     */
    public FluentValuesLoader properties(String... properties) {
        return properties(Arrays.asList(properties));
    }

    @Override
    public FluentValuesLoader store(String store) {
        super.store(store);
        return this;
    }

    @Override
    public FluentValuesLoader softDeletion(boolean softDeletion) {
        super.softDeletion(softDeletion);
        return this;
    }

    @Override
    public FluentValuesLoader parameter(String name, Object value) {
        super.parameter(name, value);
        return this;
    }

    @Override
    public FluentValuesLoader parameter(String name, Date value, TemporalType temporalType) {
        super.parameter(name, value, temporalType);
        return this;
    }

    @Override
    public FluentValuesLoader parameter(String name, Object value, boolean implicitConversion) {
        super.parameter(name, value, implicitConversion);
        return this;
    }

    @Override
    public FluentValuesLoader setParameters(Map<String, Object> parameters) {
        super.setParameters(parameters);
        return this;
    }

    @Override
    public FluentValuesLoader firstResult(int firstResult) {
        super.firstResult(firstResult);
        return this;
    }

    @Override
    public FluentValuesLoader maxResults(int maxResults) {
        super.maxResults(maxResults);
        return this;
    }
}
