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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FluentValueLoader<T> extends AbstractFluentValueLoader {

    private final Class<T> valueClass;

    private final static String PROP_NAME = "p1";

    public FluentValueLoader(String queryString, Class<T> valueClass, DataManager dataManager) {
        super(queryString, dataManager, false);
        this.valueClass = valueClass;
    }

    public FluentValueLoader(String queryString, Class<T> valueClass, DataManager dataManager, boolean transactional) {
        super(queryString, dataManager, transactional);
        this.valueClass = valueClass;
    }

    protected ValueLoadContext createLoadContext() {
        ValueLoadContext loadContext = super.createLoadContext();
        loadContext.addProperty(PROP_NAME);
        return loadContext;
    }

    @SuppressWarnings("unchecked")
    T castValue(Object value) {
        if (value != null && !value.getClass().equals(valueClass) && Number.class.isAssignableFrom(valueClass)) {
            if (valueClass.equals(Integer.class)) {
                return (T) Integer.valueOf(((Number) value).intValue());
            }
            if (valueClass.equals(Long.class)) {
                return (T) Long.valueOf(((Number) value).longValue());
            }
            if (valueClass.equals(Double.class)) {
                return (T) Double.valueOf(((Number) value).doubleValue());
            }
            if (valueClass.equals(Float.class)) {
                return (T) Float.valueOf(((Number) value).floatValue());
            }
            if (valueClass.equals(Short.class)) {
                return (T) Short.valueOf(((Number) value).shortValue());
            }
            if (valueClass.equals(BigDecimal.class)) {
                return (T) BigDecimal.valueOf(((Number) value).doubleValue());
            }
            if (valueClass.equals(BigInteger.class)) {
                return (T) BigInteger.valueOf(((Number) value).longValue());
            }
        }
        return (T) value;
    }

    /**
     * Loads a list of entities.
     */
    public List<T> list() {
        ValueLoadContext loadContext = createLoadContext();
        return dataManager.loadValues(loadContext).stream()
                .map(e -> castValue(e.getValue(PROP_NAME)))
                .collect(Collectors.toList());
    }

    /**
     * Loads a single instance and wraps it in Optional.
     */
    public Optional<T> optional() {
        ValueLoadContext loadContext = createLoadContext();
        loadContext.getQuery().setMaxResults(1);
        List<KeyValueEntity> list = dataManager.loadValues(loadContext);
        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(castValue(list.get(0).getValue(PROP_NAME)));
    }

    /**
     * Loads a single instance.
     *
     * @throws IllegalStateException if nothing was loaded
     */
    public T one() {
        ValueLoadContext loadContext = createLoadContext();
        loadContext.getQuery().setMaxResults(1);
        List<KeyValueEntity> list = dataManager.loadValues(loadContext);
        if (!list.isEmpty())
            return castValue(list.get(0).getValue(PROP_NAME));
        else
            throw new IllegalStateException("No results");
    }

    @Override
    public FluentValueLoader store(String store) {
        super.store(store);
        return this;
    }

    @Override
    public FluentValueLoader softDeletion(boolean softDeletion) {
        super.softDeletion(softDeletion);
        return this;
    }

    @Override
    public FluentValueLoader parameter(String name, Object value) {
        super.parameter(name, value);
        return this;
    }

    @Override
    public FluentValueLoader parameter(String name, Date value, TemporalType temporalType) {
        super.parameter(name, value, temporalType);
        return this;
    }

    @Override
    public FluentValueLoader parameter(String name, Object value, boolean implicitConversion) {
        super.parameter(name, value, implicitConversion);
        return this;
    }

    @Override
    public FluentValueLoader setParameters(Map<String, Object> parameters) {
        super.setParameters(parameters);
        return this;
    }

    @Override
    public FluentValueLoader firstResult(int firstResult) {
        super.firstResult(firstResult);
        return this;
    }

    @Override
    public FluentValueLoader maxResults(int maxResults) {
        super.maxResults(maxResults);
        return this;
    }
}
