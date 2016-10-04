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

import com.haulmont.bali.util.StringHelper;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.QueryMacroHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;

/**
 * Builds {@link Query} instance to use in DataService.
 */
@Component(DataServiceQueryBuilder.NAME)
@Scope("prototype")
public class DataServiceQueryBuilder {

    public static final String NAME = "cuba_DataServiceQueryBuilder";

    private Logger log = LoggerFactory.getLogger(getClass());

    protected String queryString;
    protected Map<String, Object> queryParams;
    protected String entityName;

    @Inject
    protected Metadata metadata;

    @Inject
    private PersistenceSecurity security;

    public void init(String queryString, Map<String, Object> queryParams,
                     Object id, String entityName)
    {
        this.entityName = entityName;
        if (!StringUtils.isBlank(queryString)) {
            this.queryString = queryString;
            this.queryParams = queryParams;
        } else {
            MetaClass metaClass = metadata.getClassNN(entityName);
            String pkName = metadata.getTools().getPrimaryKeyName(metaClass);
            if (pkName == null)
                throw new IllegalStateException(String.format("Entity %s has no primary key", entityName));
            this.queryString = "select e from " + entityName + " e where e." + pkName + " = :entityId";
            this.queryParams = new HashMap<>();
            this.queryParams.put("entityId", id);
        }
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

                if (value instanceof LoadContext.Query.TemporalValue) {
                    LoadContext.Query.TemporalValue temporalValue = (LoadContext.Query.TemporalValue) value;
                    query.setParameter(name, temporalValue.date, temporalValue.type);
                } else {
                    query.setParameter(name, value);
                }
            } else
                throw new DevelopmentException("Parameter '" + name + "' is not used in the query");
        }

        return query;
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
        if (constraintsApplied && log.isDebugEnabled())
            log.debug("Constraints applyed: " + printQuery(query.getQueryString()));
    }

    public static String printQuery(String query) {
        if (query == null)
            return null;
        else
            return StringHelper.removeExtraSpaces(query.replace("\n", " "));
    }
}
