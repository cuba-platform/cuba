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

import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.*;

public class ValueLoadContext implements DataLoadContext, Serializable {

    protected String storeName = Stores.MAIN;
    protected Query query;
    protected boolean softDeletion = true;
    protected String idName;
    protected List<String> properties = new ArrayList<>();

    public static ValueLoadContext create() {
        return new ValueLoadContext();
    }

    public static Query createQuery(String queryString) {
        return new Query(queryString);
    }

    @Override
    public Query setQueryString(String queryString) {
        query = new Query(queryString);
        return query;
    }

    public String getStoreName() {
        return storeName;
    }

    public ValueLoadContext setStoreName(String storeName) {
        this.storeName = storeName;
        return this;
    }

    public ValueLoadContext setQuery(Query query) {
        this.query = query;
        return this;
    }

    public Query getQuery() {
        return query;
    }

    public ValueLoadContext setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
        return this;
    }

    public boolean isSoftDeletion() {
        return softDeletion;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public ValueLoadContext addProperty(String name) {
        properties.add(name);
        return this;
    }

    public ValueLoadContext setProperties(List<String> properties) {
        this.properties.clear();
        this.properties.addAll(properties);
        return this;
    }

    public List<String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return String.format("ValuesContext{query=%s, softDeletion=%s, keys=%s}", query, softDeletion, properties);
    }

    public static class Query implements DataLoadContextQuery, Serializable {

        private String queryString;
        private int firstResult;
        private int maxResults;
        private Map<String, Object> parameters = new HashMap<>();

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


        @Override
        public String toString() {
            return "Query{" +
                    "queryString='" + queryString + '\'' +
                    ", firstResult=" + firstResult +
                    ", maxResults=" + maxResults +
                    '}';
        }
    }
}
