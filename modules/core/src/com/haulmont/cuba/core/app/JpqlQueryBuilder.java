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
 *
 */

package com.haulmont.cuba.core.app;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.haulmont.bali.util.StringHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.queryconditions.Condition;
import com.haulmont.cuba.core.global.queryconditions.ConditionJpqlGenerator;
import com.haulmont.cuba.core.sys.QueryMacroHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Builds {@link Query} instance to use in DataService.
 */
@Component(JpqlQueryBuilder.NAME)
@Scope("prototype")
public class JpqlQueryBuilder {

    public static final String NAME = "cuba_JpqlQueryBuilder";

    private static final Logger log = LoggerFactory.getLogger(JpqlQueryBuilder.class);

    protected Object id;
    protected List<?> ids;

    protected String queryString;
    protected Map<String, Object> queryParameters;
    protected String[] noConversionParams;
    protected Condition condition;
    protected Sort sort;

    protected String entityName;
    protected List<String> valueProperties;

    protected boolean singleResult;

    protected boolean previousResults;
    protected UUID sessionId;
    protected int queryKey;

    protected String resultQuery;
    protected Map<String, Object> resultParameters;

    @Inject
    protected Metadata metadata;

    @Inject
    protected PersistenceSecurity security;

    @Inject
    protected ConditionJpqlGenerator conditionJpqlGenerator;

    @Inject
    protected SortJpqlGenerator sortJpqlGenerator;

    @Inject
    protected QueryTransformerFactory queryTransformerFactory;

    public JpqlQueryBuilder setId(Object id) {
        this.id = id;
        return this;
    }

    public JpqlQueryBuilder setIds(List<?> ids) {
        this.ids = ids;
        return this;
    }

    public JpqlQueryBuilder setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public JpqlQueryBuilder setValueProperties(List<String> valueProperties) {
        this.valueProperties = valueProperties;
        return this;
    }

    public JpqlQueryBuilder setSingleResult(boolean singleResult) {
        this.singleResult = singleResult;
        return this;
    }

    public JpqlQueryBuilder setQueryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    public JpqlQueryBuilder setQueryParameters(Map<String, Object> queryParams) {
        this.queryParameters = queryParams;
        return this;
    }

    public JpqlQueryBuilder setNoConversionParams(String[] noConversionParams) {
        this.noConversionParams = noConversionParams;
        return this;
    }

    public JpqlQueryBuilder setCondition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public JpqlQueryBuilder setSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public JpqlQueryBuilder setPreviousResults(UUID sessionId, int queryKey) {
        this.previousResults = true;
        this.sessionId = sessionId;
        this.queryKey = queryKey;
        return this;
    }

    public String getResultQueryString() {
        if (resultQuery == null) {
            buildResultQuery();
        }
        return resultQuery;
    }

    public Map<String, Object> getResultParameters() {
        if (resultQuery == null) {
            buildResultQuery();
        }
        return resultParameters;
    }

    public Query getQuery(EntityManager em) {
        Query query = em.createQuery(getResultQueryString());

        //we have to replace parameter names in macros because for {@link com.haulmont.cuba.core.sys.querymacro.TimeBetweenQueryMacroHandler}
        //we need to replace a parameter with number of days with its value before macros is expanded to JPQL expression
        replaceParamsInMacros(query);

        applyConstraints(query);

        Set<String> paramNames = queryTransformerFactory.parser(getResultQueryString()).getParamNames();

        for (Map.Entry<String, Object> entry : getResultParameters().entrySet()) {
            String name = entry.getKey();
            if (paramNames.contains(name)) {
                Object value = entry.getValue();

                if (value instanceof TemporalValue) {
                    TemporalValue temporalValue = (TemporalValue) value;
                    query.setParameter(name, temporalValue.date, temporalValue.type);
                } else {
                    if (noConversionParams != null && Arrays.asList(noConversionParams).contains(name)) {
                        query.setParameter(name, value, false);
                    } else {
                        query.setParameter(name, value);
                    }
                }
            } else {
                if (entry.getValue() != null)
                    throw new DevelopmentException(String.format("Parameter '%s' is not used in the query", name));
            }
        }

        return query;
    }

    protected void buildResultQuery() {
        resultQuery = queryString;
        resultParameters = queryParameters;
        if (entityName != null) {
            if (Strings.isNullOrEmpty(queryString)) {
                if (id != null) {
                    resultQuery = String.format("select e from %s e where e.%s = :entityId", entityName, getPrimaryKeyProperty().getName());
                    resultParameters = Maps.newHashMap(ImmutableMap.of("entityId", id));
                } else if (ids != null && !ids.isEmpty()) {
                    resultQuery = String.format("select e from %s e where e.%s in :entityIds", entityName, getPrimaryKeyProperty().getName());
                    resultParameters = Maps.newHashMap(ImmutableMap.of("entityIds", ids));
                } else {
                    resultQuery = String.format("select e from %s e", entityName);
                    resultParameters = Collections.emptyMap();
                }
            }
        }
        applyFiltering();
        applySorting();
        restrictByPreviousResults();
    }

    protected void applySorting() {
        if (sort != null) {
            resultQuery = sortJpqlGenerator.processQuery(entityName, valueProperties, resultQuery, sort);
        }
    }

    protected void applyFiltering() {
        if (condition != null) {
            Set<String> nonNullParamNames = queryParameters.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            Condition actualized = condition.actualize(nonNullParamNames);
            resultQuery = conditionJpqlGenerator.processQuery(resultQuery, actualized);
        }
    }

    protected void restrictByPreviousResults() {
        if (previousResults) {
            Class type = getPrimaryKeyProperty().getJavaType();
            String entityIdField;
            if (UUID.class.equals(type)) {
                entityIdField = "entityId";
            } else if (Long.class.equals(type)) {
                entityIdField = "longEntityId";
            } else if (Integer.class.equals(type)) {
                entityIdField = "intEntityId";
            } else if (String.class.equals(type)) {
                entityIdField = "stringEntityId";
            } else {
                throw new IllegalStateException(
                        String.format("Unsupported primary key type: %s for %s", type.getSimpleName(), entityName));
            }

            QueryTransformer transformer = queryTransformerFactory.transformer(resultQuery);
            transformer.addJoinAndWhere(
                    ", sys$QueryResult _qr",
                    String.format("_qr.%s = {E}.%s and _qr.sessionId = :_qr_sessionId and _qr.queryKey = %s",
                            entityIdField, getPrimaryKeyProperty().getName(), queryKey)
            );

            this.resultQuery = transformer.getResult();
            this.resultParameters.put("_qr_sessionId", sessionId);
        }
    }

    protected void replaceParamsInMacros(Query query) {
        Collection<QueryMacroHandler> handlers = AppBeans.getAll(QueryMacroHandler.class).values();
        String modifiedQuery = query.getQueryString();
        for (QueryMacroHandler handler : handlers) {
            modifiedQuery = handler.replaceQueryParams(modifiedQuery, queryParameters);
        }
        query.setQueryString(modifiedQuery);
    }

    protected void applyConstraints(Query query) {
        boolean constraintsApplied = security.applyConstraints(query);
        if (constraintsApplied && singleResult) {
            QueryParser parser = queryTransformerFactory.parser(query.getQueryString());
            if (parser.isQueryWithJoins()) {
                QueryTransformer transformer = queryTransformerFactory.transformer(query.getQueryString());
                transformer.addDistinct();
                query.setQueryString(transformer.getResult());
            }
        }
        if (constraintsApplied && log.isDebugEnabled()) {
            log.debug("Constraints applied: {}", printQuery(query.getQueryString()));
        }
    }

    protected MetaProperty getPrimaryKeyProperty() {
        MetaClass metaClass = metadata.getClassNN(entityName);
        MetaProperty property = metadata.getTools().getPrimaryKeyProperty(metaClass);
        if (property == null) {
            throw new IllegalStateException(String.format("Entity %s has no primary key", entityName));
        }
        return property;
    }

    public static String printQuery(String query) {
        return query == null ? null : StringHelper.removeExtraSpaces(query.replace('\n', ' '));
    }
}
