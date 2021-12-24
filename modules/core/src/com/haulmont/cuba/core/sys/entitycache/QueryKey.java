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

package com.haulmont.cuba.core.sys.entitycache;

import com.google.common.base.MoreObjects;
import com.haulmont.cuba.core.global.UuidProvider;

import javax.persistence.Parameter;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;

public class QueryKey implements Serializable {

    protected final String queryString;
    protected final int firstRow;
    protected final int maxRows;
    protected final boolean softDeletion;
    protected final boolean singleResult;
    protected final Map<String, Object> namedParameters;
    protected final Map<String, Object> additionalCriteriaParameters;
    protected final Object[] positionalParameters;

    protected final int hashCode;
    protected final transient UUID id;

    public static QueryKey create(String queryString, boolean softDeletion, javax.persistence.Query jpaQuery, Map<String, Object> additionalCriteriaParameters) {
        return new QueryKey(queryString, jpaQuery.getFirstResult(), jpaQuery.getMaxResults(), softDeletion, false, getNamedParameters(jpaQuery), getPositionalParameters(jpaQuery), additionalCriteriaParameters);
    }

    public static QueryKey create(String queryString, boolean softDeletion, boolean singleResult, javax.persistence.Query jpaQuery, Map<String, Object> additionalCriteriaParameters) {
        return new QueryKey(queryString, jpaQuery.getFirstResult(), jpaQuery.getMaxResults(), softDeletion, singleResult, getNamedParameters(jpaQuery), getPositionalParameters(jpaQuery), additionalCriteriaParameters);
    }

    private static Map<String, Object> getNamedParameters(Query jpaQuery) {
        if (jpaQuery.getParameters() == null) return null;

        List<String> names = new ArrayList<>();
        Map<String, Object> namedParameters = new LinkedHashMap<>();

        jpaQuery.getParameters().stream()
                .filter(parameter -> parameter.getName() != null)
                .forEach(parameter -> names.add(parameter.getName()));

        if (names.isEmpty()) return null;

        names.stream()
                .sorted()
                .forEach(name -> namedParameters.put(name, jpaQuery.getParameterValue(name)));
        return namedParameters;
    }

    private static Object[] getPositionalParameters(Query jpaQuery) {
        if (jpaQuery.getParameters() == null) return null;

        int max = 0;
        List<Integer> positions = new ArrayList<>();
        for (Parameter parameter : jpaQuery.getParameters()) {
            if (parameter.getPosition() != null) {
                positions.add(parameter.getPosition());
                if (parameter.getPosition() > max) {
                    max = parameter.getPosition();
                }
            }
        }

        if (positions.isEmpty()) return null;

        Object[] positionalParameters = new Object[max];
        positions.forEach(position -> positionalParameters[position - 1] = jpaQuery.getParameterValue(position));
        return positionalParameters;
    }

    protected QueryKey(String queryString, int firstRow, int maxRows,
                       boolean softDeletion, boolean singleResult,
                       Map<String, Object> namedParameters,
                       Object[] positionalParameters,
                       Map<String, Object> additionalCriteriaParameters) {
        this.id = UuidProvider.createUuid();
        this.queryString = queryString;
        this.firstRow = firstRow;
        this.maxRows = maxRows;
        this.softDeletion = softDeletion;
        this.singleResult = singleResult;
        this.hashCode = generateHashCode();
        this.namedParameters = namedParameters;
        this.positionalParameters = positionalParameters;
        this.additionalCriteriaParameters = additionalCriteriaParameters;
    }

    public UUID getId() {
        return id;
    }

    public String printDescription() {
        return MoreObjects.toStringHelper("Query")
                .addValue("\"" + queryString.trim() + "\"")
                .add("id", id)
                .add("firstRow", firstRow)
                .add("maxRows", maxRows)
                .add("softDeletion", softDeletion)
                .add("positionalParameters", Arrays.deepToString(positionalParameters))
                .add("namedParameters", namedParameters)
                .add("additionalCriteriaParameters", additionalCriteriaParameters)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryKey queryKey = (QueryKey) o;

        return hashCode == queryKey.hashCode && equalsFields(queryKey) && equalsParams(queryKey);
    }

    protected boolean equalsFields(QueryKey queryKey) {
        return Objects.equals(queryString, queryKey.queryString)
                && firstRow == queryKey.firstRow
                && maxRows == queryKey.maxRows
                && softDeletion == queryKey.softDeletion
                && singleResult == queryKey.singleResult;
    }

    protected boolean equalsParams(QueryKey queryKey) {
        return Arrays.deepEquals(positionalParameters, queryKey.positionalParameters)
                && mapEquals(namedParameters, queryKey.namedParameters)
                && mapEquals(additionalCriteriaParameters, queryKey.additionalCriteriaParameters);
    }


    @Override
    public int hashCode() {
        return this.hashCode;
    }

    protected int generateHashCode() {
        int result = 1;
        result = 31 * result + queryString.hashCode();
        result = 31 * result + Integer.hashCode(firstRow);
        result = 31 * result + Integer.hashCode(maxRows);
        result = 31 * result + Boolean.hashCode(softDeletion);
        result = 31 * result + Boolean.hashCode(singleResult);
        //generates hashCode for value in same way as org.eclipse.persistence.internal.identitymaps.CacheId.computeArrayHashCode()
        result = 31 * result + (positionalParameters == null ? 0 : Arrays.deepHashCode(positionalParameters));

        result = 31 * result + (namedParameters == null ? 0 : generateMapHashCode(namedParameters));
        result = 31 * result + (additionalCriteriaParameters == null ? 0 : generateMapHashCode(additionalCriteriaParameters));
        return result;
    }

    protected int generateMapHashCode(Map<String, Object> map) {
        int result = 0;

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value != null && value.getClass().isArray()) {
                //generates hashCode for value in same way as org.eclipse.persistence.internal.identitymaps.CacheId.computeArrayHashCode()
                result += Objects.hashCode(entry.getKey()) ^ generateArrayHashCode(value);
            } else {
                result += Objects.hashCode(entry.getKey()) ^ Objects.hashCode(entry.getValue());
            }
        }

        return result;
    }

    protected boolean mapEquals(Map<String, Object> a, Map<String, Object> b) {
        if (a == b) return true;

        if (a == null || b == null) return false;

        for (Map.Entry<String, Object> entry : a.entrySet()) {
            Object aValue = entry.getValue();
            Object bValue = b.get(entry.getKey());

            if (aValue == bValue) continue;

            if (aValue == null || bValue == null) return false;

            if (aValue.getClass() != bValue.getClass()) return false;

            if (aValue.getClass().isArray()) {
                if (!Arrays.deepEquals((Object[])aValue, (Object[])bValue)) return false;
            } else {
                if (!aValue.equals(bValue)) return false;
            }
        }
        return true;
    }

    protected int generateArrayHashCode(Object array) {
        if (array instanceof Object[])
            return Arrays.deepHashCode((Object[]) array);
        else if (array instanceof byte[])
            return Arrays.hashCode((byte[]) array);
        else if (array instanceof short[])
            return Arrays.hashCode((short[]) array);
        else if (array instanceof int[])
            return Arrays.hashCode((int[]) array);
        else if (array instanceof long[])
            return Arrays.hashCode((long[]) array);
        else if (array instanceof char[])
            return Arrays.hashCode((char[]) array);
        else if (array instanceof float[])
            return Arrays.hashCode((float[]) array);
        else if (array instanceof double[])
            return Arrays.hashCode((double[]) array);
        else if (array instanceof boolean[])
            return Arrays.hashCode((boolean[]) array);
        else {
            return Objects.hashCode(array);
        }
    }
}
