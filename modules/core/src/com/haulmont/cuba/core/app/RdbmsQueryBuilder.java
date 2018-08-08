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

import com.google.common.base.Joiner;
import com.haulmont.bali.util.StringHelper;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.queryconditions.Condition;
import com.haulmont.cuba.core.global.queryconditions.ConditionJpqlGenerator;
import com.haulmont.cuba.core.sys.QueryMacroHandler;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Builds {@link Query} instance to use in DataService.
 */
@Component(RdbmsQueryBuilder.NAME)
@Scope("prototype")
public class RdbmsQueryBuilder {

    public static final String NAME = "cuba_RdbmsQueryBuilder";

    private static final Logger log = LoggerFactory.getLogger(RdbmsQueryBuilder.class);

    protected String queryString;
    protected Map<String, Object> queryParams;
    protected String[] noConversionParams;
    protected String entityName;
    protected boolean singleResult;

    @Inject
    protected Metadata metadata;

    @Inject
    private PersistenceSecurity security;

    @Inject
    private ConditionJpqlGenerator conditionJpqlGenerator;

    public void init(@Nullable String queryString, Condition condition, Sort sort,
                     Map<String, Object> queryParams, String[] noConversionParams,
                     @Nullable Object id, String entityName)
    {
        this.entityName = entityName;
        String qs;
        if (queryString == null && id == null) {
            qs = "select e from " + entityName + " e";
            this.queryParams = Collections.emptyMap();

        } else if (!StringUtils.isBlank(queryString)) {
            qs = queryString;
            this.queryParams = queryParams;
            this.noConversionParams = noConversionParams;

        } else {
            MetaClass metaClass = metadata.getClassNN(entityName);
            String pkName = metadata.getTools().getPrimaryKeyName(metaClass);
            if (pkName == null)
                throw new IllegalStateException(String.format("Entity %s has no primary key", entityName));
            qs = "select e from " + entityName + " e where e." + pkName + " = :entityId";
            this.queryParams = new HashMap<>();
            this.queryParams.put("entityId", id);
        }
        if (condition != null) {
            Condition actualized = condition.actualize(queryParams.keySet());
            qs = conditionJpqlGenerator.processQuery(qs, actualized);
        }
        if (sort != null) {
            qs = processSort(qs, sort);
        }
        this.queryString = qs;
    }

    public void setSingleResult(boolean singleResult) {
        this.singleResult = singleResult;
    }

    public void restrictByPreviousResults(UUID sessionId, int queryKey) {
        QueryTransformer transformer = QueryTransformerFactory.createTransformer(queryString);
        MetaClass metaClass = metadata.getClassNN(entityName);
        MetaProperty primaryKey = metadata.getTools().getPrimaryKeyProperty(metaClass);
        if (primaryKey == null)
            throw new IllegalStateException(String.format("Entity %s has no primary key", entityName));
        Class type = primaryKey.getJavaType();
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
        transformer.addJoinAndWhere(
                ", sys$QueryResult _qr",
                String.format("_qr.%s = {E}.%s and _qr.sessionId = :_qr_sessionId and _qr.queryKey = %s",
                        entityIdField, primaryKey.getName(), queryKey)
        );
        queryString = transformer.getResult();
        this.queryParams.put("_qr_sessionId", sessionId);
    }

    public String getQueryString() {
        return queryString;
    }

    public Query getQuery(EntityManager em) {
        Query query = em.createQuery(queryString);

        //we have to replace parameter names in macros because for {@link com.haulmont.cuba.core.sys.querymacro.TimeBetweenQueryMacroHandler}
        //we need to replace a parameter with number of days with its value before macros is expanded to JPQL expression
        replaceParamsInMacros(query);

        applyConstraints(query);

        QueryParser parser = QueryTransformerFactory.createParser(queryString);
        Set<String> paramNames = parser.getParamNames();

        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            String name = entry.getKey();
            if (paramNames.contains(name)) {
                Object value = entry.getValue();

                boolean convert = noConversionParams == null
                        || Arrays.stream(noConversionParams).noneMatch(s -> s.equals(name));
                if (convert) {
                    if (value instanceof Entity) {
                        value = ((Entity) value).getId();

                    } else if (value instanceof EnumClass) {
                        value = ((EnumClass) value).getId();

                    } else if (value instanceof Collection) {
                        List<Object> list = new ArrayList<>(((Collection) value).size());
                        for (Object item : (Collection) value) {
                            if (item instanceof Entity)
                                list.add(((Entity) item).getId());
                            else if (item instanceof EnumClass)
                                list.add(((EnumClass) item).getId());
                            else
                                list.add(item);
                        }
                        value = list;
                    }
                }

                if (value instanceof TemporalValue) {
                    TemporalValue temporalValue = (TemporalValue) value;
                    query.setParameter(name, temporalValue.date, temporalValue.type);
                } else {
                    if (!convert) {
                        query.setParameter(name, value, false);
                    } else {
                        query.setParameter(name, value);
                    }
                }
            } else
                throw new DevelopmentException("Parameter '" + name + "' is not used in the query");
        }

        return query;
    }

    protected String processSort(String queryString, Sort sort) {
        if (sort.getOrders().isEmpty()) {
            return queryString;
        }
        Map<Sort.Direction, List<Sort.Order>> directions = sort.getOrders().stream()
                .collect(Collectors.groupingBy(Sort.Order::getDirection));
        if (directions.size() > 1) {
            throw new UnsupportedOperationException("Sorting by multiple properties in different directions is not supported");
        }
        boolean asc = directions.keySet().iterator().next() == Sort.Direction.ASC;

        List<String> allSortProperties = new ArrayList<>();

        for (Sort.Order order : sort.getOrders()) {
            MetaPropertyPath propertyPath = metadata.getClassNN(entityName).getPropertyPath(order.getProperty());
            if (propertyPath == null) {
                throw new IllegalArgumentException("Property " + order.getProperty() + " is invalid");
            }

            if (metadata.getTools().isPersistent(propertyPath)) {
                allSortProperties.addAll(getSortPropertiesForPersistentAttribute(propertyPath));
            } else {
                // a non-persistent attribute
                List<String> relProperties = metadata.getTools().getRelatedProperties(propertyPath.getMetaProperty());
                if (!relProperties.isEmpty()) {
                    List<String> sortPropertiesList = new ArrayList<>(relProperties.size());
                    for (String relProp : relProperties) {
                        String[] ppCopy = Arrays.copyOf(propertyPath.getPath(), propertyPath.getPath().length);
                        ppCopy[ppCopy.length - 1] = relProp;

                        MetaPropertyPath relPropertyPath = propertyPath.getMetaProperties()[0].getDomain().getPropertyPath(Joiner.on(".").join(ppCopy));
                        List<String> sortPropertiesForRelProperty = getSortPropertiesForPersistentAttribute(relPropertyPath);
                        if (sortPropertiesForRelProperty != null)
                            sortPropertiesList.addAll(sortPropertiesForRelProperty);
                    }
                    if (!sortPropertiesList.isEmpty())
                        allSortProperties.addAll(sortPropertiesList);
                }
            }
        }

        if (!allSortProperties.isEmpty()) {
            QueryTransformer transformer = QueryTransformerFactory.createTransformer(queryString);
            transformer.replaceOrderBy(!asc, allSortProperties.toArray(new String[0]));
            return transformer.getResult();
        } else {
            return queryString;
        }
    }

    protected List<String> getSortPropertiesForPersistentAttribute(MetaPropertyPath propertyPath) {
        List<String> sortProperties = new ArrayList<>(1);
        MetaProperty metaProperty = propertyPath.getMetaProperty();
        Range range = metaProperty.getRange();

        if (!range.isClass()) {
            // a scalar persistent attribute
            MetaClass propertyMetaClass = metadata.getTools().getPropertyEnclosingMetaClass(propertyPath);
            String storeName = metadata.getTools().getStoreName(propertyMetaClass);
            if (!metadata.getTools().isLob(metaProperty) || supportsLobSortingAndFiltering(storeName)) {
                sortProperties.add(propertyPath.toString());
            }
        } else {
            // a reference attribute
            if (!range.getCardinality().isMany()) {
                Collection<MetaProperty> properties = metadata.getTools().getNamePatternProperties(range.asClass());
                if (!properties.isEmpty()) {
                    sortProperties.addAll(properties.stream()
                            .filter(prop -> {
                                if (metadata.getTools().isPersistent(prop)) {
                                    String storeName = metadata.getTools().getStoreName(range.asClass());
                                    return !metadata.getTools().isLob(prop) || supportsLobSortingAndFiltering(storeName);
                                }
                                return false;
                            })
                            .map(MetadataObject::getName)
                            .map(propName -> propertyPath.toString().concat(".").concat(propName))
                            .collect(Collectors.toList()));
                } else {
                    sortProperties.add(propertyPath.toString());
                }
            }
        }
        return sortProperties;
    }

    protected boolean supportsLobSortingAndFiltering(String storeName) {
        return storeName == null || DbmsSpecificFactory.getDbmsFeatures(storeName).supportsLobSortingAndFiltering();
    }

    protected void replaceParamsInMacros(Query query) {
        Collection<QueryMacroHandler> handlers = AppBeans.getAll(QueryMacroHandler.class).values();
        String modifiedQuery = query.getQueryString();
        for (QueryMacroHandler handler : handlers) {
            modifiedQuery = handler.replaceQueryParams(modifiedQuery, queryParams);
        }
        query.setQueryString(modifiedQuery);
    }

    protected void applyConstraints(Query query) {
        boolean constraintsApplied = security.applyConstraints(query);
        if (constraintsApplied && singleResult) {
            QueryParser parser = QueryTransformerFactory.createParser(query.getQueryString());
            if (parser.hasJoins()) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(query.getQueryString());
                transformer.addDistinct();
                query.setQueryString(transformer.getResult());
            }
        }
        if (constraintsApplied && log.isDebugEnabled())
            log.debug("Constraints applied: {}", printQuery(query.getQueryString()));
    }

    public static String printQuery(String query) {
        if (query == null)
            return null;
        else
            return StringHelper.removeExtraSpaces(query.replace('\n', ' '));
    }
}
