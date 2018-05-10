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

import com.google.common.base.Strings;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.Entity;

import javax.persistence.TemporalType;
import java.util.*;

public class FluentLoader<E extends Entity<K>, K> {

    private Class<E> entityClass;

    private DataManager dataManager;

    private View view;
    private String viewName;
    private boolean softDeletion = true;

    FluentLoader(Class<E> entityClass, DataManager dataManager) {
        this.entityClass = entityClass;
        this.dataManager = dataManager;
    }

    LoadContext<E> createLoadContext() {
        LoadContext<E> loadContext = LoadContext.create(entityClass);
        initCommonLoadContextParameters(loadContext);

        String entityName = AppBeans.get(Metadata.class).getClassNN(entityClass).getName();
        String queryString = String.format("select e from %s e", entityName);
        loadContext.setQuery(LoadContext.createQuery(queryString));

        return loadContext;
    }

    private void initCommonLoadContextParameters(LoadContext<E> loadContext) {
        if (view != null)
            loadContext.setView(view);
        else if (!Strings.isNullOrEmpty(viewName))
            loadContext.setView(viewName);

        loadContext.setSoftDeletion(softDeletion);
    }

    /**
     * Loads a list of entities.
     */
    public List<E> list() {
        LoadContext<E> loadContext = createLoadContext();
        return dataManager.loadList(loadContext);
    }

    /**
     * Loads a single instance and wraps it in Optional.
     */
    public Optional<E> optional() {
        LoadContext<E> loadContext = createLoadContext();
        loadContext.getQuery().setMaxResults(1);
        return Optional.ofNullable(dataManager.load(loadContext));
    }

    /**
     * Loads a single instance.
     *
     * @throws IllegalStateException if nothing was loaded
     */
    public E one() {
        LoadContext<E> loadContext = createLoadContext();
        loadContext.getQuery().setMaxResults(1);
        E entity = dataManager.load(loadContext);
        if (entity != null)
            return entity;
        else
            throw new IllegalStateException("No results");
    }

    /**
     * Sets a view.
     */
    public FluentLoader<E, K> view(View view) {
        this.view = view;
        return this;
    }

    /**
     * Sets a view by name.
     */
    public FluentLoader<E, K> view(String viewName) {
        this.viewName = viewName;
        return this;
    }

    /**
     * Sets soft deletion. The soft deletion is true by default.
     */
    public FluentLoader<E, K> softDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
        return this;
    }

    /**
     * Sets the entity identifier.
     */
    public ById id(K id) {
        return new ById(id);
    }

    /**
     * Sets the query text.
     */
    public ByQuery query(String queryString) {
        return new ByQuery(queryString);
    }

    public class ById {

        private Object id;

        ById(Object id) {
            this.id = id;
        }

        LoadContext<E> createLoadContext() {
            LoadContext<E> loadContext = LoadContext.create(entityClass).setId(id);
            initCommonLoadContextParameters(loadContext);
            return loadContext;
        }

        /**
         * Loads a single instance and wraps it in Optional.
         */
        public Optional<E> optional() {
            LoadContext<E> loadContext = createLoadContext();
            return Optional.ofNullable(dataManager.load(loadContext));
        }

        /**
         * Loads a single instance.
         *
         * @throws IllegalStateException if nothing was loaded
         */
        public E one() {
            LoadContext<E> loadContext = createLoadContext();
            E entity = dataManager.load(loadContext);
            if (entity != null)
                return entity;
            else
                throw new IllegalStateException("No results");
        }

        /**
         * Sets a view.
         */
        public ById view(View view) {
            FluentLoader.this.view = view;
            return this;
        }

        /**
         * Sets a view by name.
         */
        public ById view(String viewName) {
            FluentLoader.this.viewName = viewName;
            return this;
        }

        /**
         * Sets soft deletion. The soft deletion is true by default.
         */
        public ById softDeletion(boolean softDeletion) {
            FluentLoader.this.softDeletion = softDeletion;
            return this;
        }
    }

    public class ByQuery {

        private String queryString;
        private Map<String, Object> parameters = new HashMap<>();
        private Set<String> noConversionParams = new HashSet<>();
        private int firstResult;
        private int maxResults;
        private boolean cacheable;

        ByQuery(String queryString) {
            Preconditions.checkNotEmptyString(queryString, "queryString is empty");
            this.queryString = queryString;
        }

        LoadContext<E> createLoadContext() {
            LoadContext<E> loadContext = LoadContext.create(entityClass);
            initCommonLoadContextParameters(loadContext);

            LoadContext.Query query = LoadContext.createQuery(queryString);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                if (noConversionParams.contains(entry.getKey()))
                    query.setParameter(entry.getKey(), entry.getValue(), false);
                else
                    query.setParameter(entry.getKey(), entry.getValue());
            }
            loadContext.setQuery(query);

            loadContext.getQuery().setFirstResult(firstResult);
            loadContext.getQuery().setMaxResults(maxResults);
            loadContext.getQuery().setCacheable(cacheable);

            return loadContext;
        }

        /**
         * Loads a list of entities.
         */
        public List<E> list() {
            LoadContext<E> loadContext = createLoadContext();
            return dataManager.loadList(loadContext);
        }

        /**
         * Loads a single instance and wraps it in Optional.
         */
        public Optional<E> optional() {
            LoadContext<E> loadContext = createLoadContext();
            return Optional.ofNullable(dataManager.load(loadContext));
        }

        /**
         * Loads a single instance.
         *
         * @throws IllegalStateException if nothing was loaded
         */
        public E one() {
            LoadContext<E> loadContext = createLoadContext();
            E entity = dataManager.load(loadContext);
            if (entity != null)
                return entity;
            else
                throw new IllegalStateException("No results");
        }

        /**
         * Sets a view.
         */
        public ByQuery view(View view) {
            FluentLoader.this.view = view;
            return this;
        }

        /**
         * Sets a view by name.
         */
        public ByQuery view(String viewName) {
            FluentLoader.this.viewName = viewName;
            return this;
        }

        /**
         * Sets soft deletion. The soft deletion is true by default.
         */
        public ByQuery softDeletion(boolean softDeletion) {
            FluentLoader.this.softDeletion = softDeletion;
            return this;
        }

        /**
         * Sets value for a query parameter.

         * @param name  parameter name
         * @param value parameter value
         */
        public ByQuery parameter(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        /**
         * Sets value for a parameter of {@code java.util.Date} type.

         * @param name  parameter name
         * @param value parameter value
         * @param temporalType  how to interpret the value
         */
        public ByQuery parameter(String name, Date value, TemporalType temporalType) {
            parameters.put(name, new TemporalValue(value, temporalType));
            return this;
        }

        /**
         * Sets value for a query parameter.

         * @param name  parameter name
         * @param value parameter value
         * @param implicitConversion whether to do parameter value conversions, e.g. convert an entity to its ID
         */
        public ByQuery parameter(String name, Object value, boolean implicitConversion) {
            parameters.put(name, value);
            if (!implicitConversion) {
                noConversionParams.add(name);
            }
            return this;
        }

        /**
         * Sets the map of query parameters.
         */
        public ByQuery setParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        /**
         * Sets results offset.
         */
        public ByQuery firstResult(int firstResult) {
            this.firstResult = firstResult;
            return this;
        }

        /**
         * Sets results limit.
         */
        public ByQuery maxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        /**
         * Indicates that the query results should be cached.
         * By default, queries are not cached.
         */
        public ByQuery cacheable(boolean cacheable) {
            this.cacheable = cacheable;
            return this;
        }
    }
}
