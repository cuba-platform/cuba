/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.core.app.dynamicattributes;


import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(AttributeOptionsLoader.NAME)
public class AttributeOptionsLoaderImpl implements AttributeOptionsLoader {

    protected final Map<String, OptionsLoaderStrategy> loaderStrategies = new HashMap<>();

    @Inject
    protected Scripting scripting;
    @Inject
    protected Persistence persistence;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Metadata metadata;

    protected static final String ENTITY_QUERY_PARAM = "entity";
    protected static final String ENTITY_FIELD_QUERY_PARAM = "entity.";
    protected static final Pattern COMMON_PARAM_PATTERN = Pattern.compile("\\$\\{(.+?)}");

    public interface OptionsLoaderStrategy {
        List loadOptions(BaseGenericIdEntity entity, CategoryAttribute attribute, String script);
    }

    @PostConstruct
    public void init() {
        loaderStrategies.put(CategoryAttributeOptionsLoaderType.GROOVY.getId(), this::executeGroovyScript);
        loaderStrategies.put(CategoryAttributeOptionsLoaderType.SQL.getId(), this::executeSql);
        loaderStrategies.put(CategoryAttributeOptionsLoaderType.JPQL.getId(), this::executeJpql);
    }

    @Override
    public List loadOptions(BaseGenericIdEntity entity, CategoryAttribute attribute) {
        CategoryAttributeConfiguration configuration = attribute.getConfiguration();
        String loaderScript = configuration.getOptionsLoaderScript();

        OptionsLoaderStrategy loaderStrategy = resolveLoaderStrategy(configuration.getOptionsLoaderType());
        List result = loaderStrategy.loadOptions(entity, attribute, loaderScript);

        return result == null ? Collections.emptyList() : result;
    }

    protected OptionsLoaderStrategy resolveLoaderStrategy(CategoryAttributeOptionsLoaderType loaderType) {
        OptionsLoaderStrategy loaderStrategy = loaderStrategies.get(loaderType.getId());
        if (loaderStrategy == null) {
            throw new IllegalStateException(String.format("Unsupported options loader type: %s", loaderType.getId()));
        }
        return loaderStrategy;
    }

    protected List executeSql(BaseGenericIdEntity entity, CategoryAttribute attribute, String script) {
        List result = null;
        if (!Strings.isNullOrEmpty(script)) {
            Transaction tx = persistence.createTransaction();
            try {
                EntityManager em = persistence.getEntityManager();
                SqlQuery sqlQuery = buildSqlQuery(script, Collections.singletonMap("entity", entity));

                Query query = em.createNativeQuery(sqlQuery.query);

                if (sqlQuery.params != null) {
                    int i = 1;
                    for (Object param : sqlQuery.params) {
                        query.setParameter(i++, param);
                    }
                }

                result = query.getResultList();
                tx.commit();
            } finally {
                tx.end();
            }
        }
        return result;
    }

    protected static class SqlQuery {
        protected String query;
        protected List<Object> params;

        public SqlQuery(String query, List<Object> params) {
            this.query = query;
            this.params = params;
        }
    }

    protected SqlQuery buildSqlQuery(String script, Map<String, Object> params) {
        Matcher matcher = COMMON_PARAM_PATTERN.matcher(script);
        boolean result = matcher.find();
        if (result) {
            List<Object> queryParams = new ArrayList<>();
            StringBuffer query = new StringBuffer();
            do {
                String parameterName = matcher.group(1);
                queryParams.add(getQueryParameterValue(parameterName, params));
                matcher.appendReplacement(query, "?");
                result = matcher.find();
            } while (result);
            matcher.appendTail(query);
            return new SqlQuery(query.toString(), queryParams);
        }
        return new SqlQuery(script, null);
    }

    protected Object getQueryParameterValue(String name, Map<String, Object> params) {
        if (ENTITY_QUERY_PARAM.equals(name)) {
            Entity entity = (Entity) params.get("entity");
            if (entity != null) {
                return entity.getId();
            }
        } else if (name != null && name.startsWith(ENTITY_FIELD_QUERY_PARAM)) {
            Entity entity = (Entity) params.get("entity");
            if (entity != null) {
                String attributePath = name.substring(ENTITY_FIELD_QUERY_PARAM.length());
                Object value = entity.getValueEx(attributePath);
                return value instanceof Entity ? ((Entity) value).getId() : value;
            }
        }
        return null;
    }

    protected List executeJpql(BaseGenericIdEntity entity, CategoryAttribute attribute, String script) {
        MetaClass metaClass = metadata.getClassNN(attribute.getJavaClassForEntity());

        StringBuilder queryString = new StringBuilder(String.format("select e from %s e", metaClass.getName()));

        if (!Strings.isNullOrEmpty(attribute.getJoinClause())) {
            queryString.append(" ").append(attribute.getJoinClause());
        }

        if (!Strings.isNullOrEmpty(attribute.getWhereClause())) {
            queryString.append(" where ").append(attribute.getWhereClause().replaceAll("\\{E}", "e"));
        }

        LoadContext.Query query = buildJpqlQuery(queryString.toString(), Collections.singletonMap("entity", entity));

        LoadContext<?> loadContext = new LoadContext<>(metaClass).setView(View.MINIMAL);
        loadContext.setQuery(query);

        return dataManager.secure().loadList(loadContext);
    }

    protected LoadContext.Query buildJpqlQuery(String script, Map<String, Object> params) {
        Matcher matcher = COMMON_PARAM_PATTERN.matcher(script);
        boolean result = matcher.find();
        if (result) {
            Map<String, Object> queryParams = new HashMap<>();
            StringBuffer queryString = new StringBuffer();
            int i = 1;
            do {
                String paramKey = String.format("param_%s", i);
                queryParams.put(paramKey, getQueryParameterValue(matcher.group(1), params));
                matcher.appendReplacement(queryString, ":" + paramKey);
                result = matcher.find();
            } while (result);
            matcher.appendTail(queryString);
            LoadContext.Query query = new LoadContext.Query(queryString.toString());
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            return query;
        } else {
            return new LoadContext.Query(script);
        }
    }

    protected List executeGroovyScript(BaseGenericIdEntity entity, CategoryAttribute attribute, String script) {
        if (!Strings.isNullOrEmpty(script)) {
            return scripting.evaluateGroovy(script, Collections.singletonMap("entity", entity));
        }
        return null;
    }
}
