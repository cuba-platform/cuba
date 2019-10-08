/*
 * Copyright (c) 2008-2016 Haulmont.
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

import com.haulmont.bali.util.StringHelper;
import com.haulmont.cuba.core.global.queryconditions.Condition;

import javax.annotation.Nullable;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.*;

/**
 * Class that defines parameters for loading values from the database via {@link DataManager#loadValues(ValueLoadContext)}.
 * <p>Typical usage:
 * <pre>
 * ValueLoadContext context = ValueLoadContext.create()
 *      .setQuery(ValueLoadContext.createQuery("select e.id, e.name from sample$Customer e where e.grade = :grade")
 *          .setParameter("grade", 1))
 *      .addProperty("id")
 *      .addProperty("name");
 * </pre>
 */
public class ValueLoadContext implements DataLoadContext, Serializable {

    protected String storeName = Stores.MAIN;
    protected Query query;
    protected boolean softDeletion = true;
    protected String idName;
    protected List<String> properties = new ArrayList<>();
    protected boolean authorizationRequired;
    protected boolean joinTransaction;

    /**
     * Creates an instance of ValueLoadContext
     */
    public static ValueLoadContext create() {
        return new ValueLoadContext();
    }

    /**
     * Creates an instance of ValueLoadContext query
     */
    public static Query createQuery(String queryString) {
        return new Query(queryString);
    }

    /**
     * @param queryString JPQL query string. Only named parameters are supported.
     * @return  query definition object
     */
    @Override
    public Query setQueryString(String queryString) {
        query = new Query(queryString);
        return query;
    }

    /**
     * @return data store name if set by {@link #setStoreName(String)}
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * Sets a data store name if it is different from the main database.
     * @return this instance for chaining
     */
    public ValueLoadContext setStoreName(String storeName) {
        this.storeName = storeName;
        return this;
    }

    /**
     * Sets query instance
     * @return this instance for chaining
     */
    public ValueLoadContext setQuery(Query query) {
        this.query = query;
        return this;
    }

    /**
     * @return query instance
     */
    public Query getQuery() {
        return query;
    }

    /**
     * @param softDeletion whether to use soft deletion when loading entities
     */
    public ValueLoadContext setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
        return this;
    }

    /**
     * @return whether to use soft deletion when loading entities
     */
    public boolean isSoftDeletion() {
        return softDeletion;
    }

    /**
     * @return name of property that represents an identifier of the returned KeyValueEntity, if set by {@link #setIdName(String)}
     */
    public String getIdName() {
        return idName;
    }

    /**
     * Sets name of the property that represents an identifier of the returned KeyValueEntity.
     */
    public void setIdName(String idName) {
        this.idName = idName;
    }

    /**
     * Adds a key of a returned key-value pair. The sequence of adding properties must conform to the sequence of
     * result fields in the query "select" clause.
     * <p>For example, if the query is <code>select e.id, e.name from sample$Customer</code>
     * and you executed <code>context.addProperty("customerId").addProperty("customerName")</code>, the returned KeyValueEntity
     * will contain customer identifiers in "customerId" property and names in "customerName" property.
     * @return this instance for chaining
     */
    public ValueLoadContext addProperty(String name) {
        properties.add(name);
        return this;
    }

    /**
     * The same as invoking {@link #addProperty(String)} multiple times.
     * @return this instance for chaining
     */
    public ValueLoadContext setProperties(List<String> properties) {
        this.properties.clear();
        this.properties.addAll(properties);
        return this;
    }

    /**
     * @return  the list of properties added by {@link #addProperty(String)}
     */
    public List<String> getProperties() {
        return properties;
    }

    public boolean isAuthorizationRequired() {
        return authorizationRequired;
    }

    public ValueLoadContext setAuthorizationRequired(boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
        return this;
    }

    public boolean isJoinTransaction() {
        return joinTransaction;
    }

    public ValueLoadContext setJoinTransaction(boolean joinTransaction) {
        this.joinTransaction = joinTransaction;
        return this;
    }

    @Override
    public String toString() {
        return String.format("ValuesContext{query=%s, softDeletion=%s, keys=%s}", query, softDeletion, properties);
    }

    /**
     * Class that defines a query to be executed for loading values.
     */
    public static class Query implements DataLoadContextQuery, Serializable {

        private String queryString;
        private int firstResult;
        private int maxResults;
        private Map<String, Object> parameters = new HashMap<>();
        private String[] noConversionParams;
        private Condition condition;
        private Sort sort;

        /**
         * @param queryString JPQL query string. Only named parameters are supported.
         */
        public Query(String queryString) {
            this.queryString = queryString;
        }

        /**
         * @return JPQL query string
         */
        public String getQueryString() {
            return queryString;
        }

        /**
         * @param queryString JPQL query string. Only named parameters are supported.
         */
        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        /**
         * Set value for a query parameter.
         * @param name  parameter name
         * @param value parameter value
         * @return  this query instance for chaining
         */
        public Query setParameter(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        /**
         * Set value for a query parameter.
         * @param name  parameter name
         * @param value parameter value
         * @param implicitConversions whether to do parameter value conversions, e.g. convert an entity to its ID
         * @return  this query instance for chaining
         */
        public Query setParameter(String name, Object value, boolean implicitConversions) {
            parameters.put(name, value);
            if (!implicitConversions) {
                // this is a rare case, so let's save some memory by using an array instead of a list
                if (noConversionParams == null)
                    noConversionParams = new String[0];
                noConversionParams = new String[noConversionParams.length + 1];
                noConversionParams[noConversionParams.length - 1] = name;
            }
            return this;
        }

        /**
         * Set value for a parameter of java.util.Date type.
         * @param name          parameter name
         * @param value         date value
         * @param temporalType  temporal type
         * @return  this query instance for chaining
         */
        public Query setParameter(String name, Date value, TemporalType temporalType) {
            parameters.put(name, new TemporalValue(value, temporalType));
            return this;
        }

        /**
         * @return editable map of the query parameters
         */
        public Map<String, Object> getParameters() {
            return parameters;
        }

        /**
         * @param parameters map of the query parameters
         */
        public Query setParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        /**
         * @param firstResult results offset
         * @return this query instance for chaining
         */
        public Query setFirstResult(int firstResult) {
            this.firstResult = firstResult;
            return this;
        }

        /**
         * @param maxResults results limit
         * @return this query instance for chaining
         */
        public Query setMaxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        /**
         * @return root query condition
         */
        public Condition getCondition() {
            return condition;
        }

        /**
         * @param condition root query condition
         * @return this query instance for chaining
         */
        public Query setCondition(Condition condition) {
            this.condition = condition;
            return this;
        }

        /**
         * @return query sort
         */
        public Sort getSort() {
            return sort;
        }

        /**
         * @param sort query sort
         * @return this query instance for chaining
         */
        public Query setSort(Sort sort) {
            this.sort = sort;
            return this;
        }

        /**
         * @return results offset
         */
        public int getFirstResult() {
            return firstResult;
        }

        /**
         * @return results limit
         */
        public int getMaxResults() {
            return maxResults;
        }

        @Nullable
        public String[] getNoConversionParams() {
            return noConversionParams;
        }

        @Override
        public String toString() {
            String stringResult = "Query{" +
                    "queryString='" + queryString + '\'' +
                    ", condition=" + condition +
                    ", sort=" + sort +
                    ", firstResult=" + firstResult +
                    ", maxResults=" + maxResults +
                    "}";
            return StringHelper.removeExtraSpaces(stringResult.replace('\n', ' '));
        }
    }
}
