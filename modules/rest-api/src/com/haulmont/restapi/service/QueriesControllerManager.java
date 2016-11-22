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

package com.haulmont.restapi.service;

import com.google.common.base.Strings;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationOption;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.restapi.common.RestControllerUtils;
import com.haulmont.restapi.exception.RestAPIException;
import com.haulmont.restapi.config.RestQueriesConfiguration;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Component("cuba_QueriesControllerManager")
public class QueriesControllerManager {

    @Inject
    protected RestQueriesConfiguration restQueriesConfiguration;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected Metadata metadata;

    @Inject
    protected EntitySerializationAPI entitySerializationAPI;

    @Inject
    protected Security security;

    @Inject
    protected RestControllerUtils restControllerUtils;

    public String executeQuery(String entityName,
                               String queryName,
                               @Nullable Integer limit,
                               @Nullable Integer offset,
                               @Nullable String view,
                               @Nullable Boolean returnNulls,
                               @Nullable Boolean dynamicAttributes,
                               Map<String, String> params) throws ClassNotFoundException, ParseException {
        LoadContext<Entity> ctx = createQueryLoadContext(entityName, queryName, limit, offset, params);
        ctx.setLoadDynamicAttributes(BooleanUtils.isTrue(dynamicAttributes));

        //override default view defined in queries config
        if (!Strings.isNullOrEmpty(view)) {
            ctx.setView(view);
        }
        List<Entity> entities = dataManager.loadList(ctx);

        List<EntitySerializationOption> serializationOptions = new ArrayList<>();
        serializationOptions.add(EntitySerializationOption.SERIALIZE_INSTANCE_NAME);
        if (BooleanUtils.isTrue(returnNulls)) serializationOptions.add(EntitySerializationOption.SERIALIZE_NULLS);

        return entitySerializationAPI.toJson(entities, ctx.getView(), serializationOptions.toArray(new EntitySerializationOption[0]));
    }

    public String getCount(String entityName,
                           String queryName,
                           Map<String, String> params) throws ClassNotFoundException, ParseException {
        LoadContext<Entity> ctx = createQueryLoadContext(entityName, queryName, null, null, params);
        long count = dataManager.getCount(ctx);
        return String.valueOf(count);
    }

    public List<RestQueriesConfiguration.QueryInfo> loadQueriesList(String entityName) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanReadEntity(metaClass);
        return restQueriesConfiguration.getQueries(entityName);
    }

    protected LoadContext<Entity> createQueryLoadContext(String entityName,
                                                         String queryName,
                                                         @Nullable Integer limit,
                                                         @Nullable Integer offset,
                                                         Map<String, String> params) throws ClassNotFoundException, ParseException {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanReadEntity(metaClass);

        RestQueriesConfiguration.QueryInfo queryInfo = restQueriesConfiguration.getQuery(entityName, queryName);
        if (queryInfo == null) {
            throw new RestAPIException("Query not found",
                    String.format("Query with name %s for entity %s not found", queryName, entityName),
                    HttpStatus.NOT_FOUND);
        }

        LoadContext<Entity> ctx = new LoadContext<>(metaClass);
        LoadContext.Query query = new LoadContext.Query(queryInfo.getJpql());

        if (limit != null) {
            query.setMaxResults(limit);
        }
        if (offset != null) {
            query.setFirstResult(offset);
        }

        for (RestQueriesConfiguration.QueryParamInfo paramInfo : queryInfo.getParams()) {
            String paramName = paramInfo.getName();
            String requestParamValue = params.get(paramName);
            if (requestParamValue == null) {
                throw new RestAPIException("Query parameter not found",
                        String.format("Query parameter %s not found", paramName),
                        HttpStatus.BAD_REQUEST);
            }

            Class<?> clazz = ClassUtils.forName(paramInfo.getType(), getClass().getClassLoader());
            Object objectParamValue = toObject(clazz, requestParamValue);
            query.setParameter(paramName, objectParamValue);
        }

        ctx.setQuery(query);
        ctx.setView(queryInfo.getViewName());
        return ctx;
    }

    protected void checkCanReadEntity(MetaClass metaClass) {
        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            throw new RestAPIException("Reading forbidden",
                    String.format("Reading of the %s is forbidden", metaClass.getName()),
                    HttpStatus.FORBIDDEN);
        }
    }

    protected Object toObject(Class clazz, String value) throws ParseException {
        if (String.class == clazz) return value;
        if (Integer.class == clazz || Integer.TYPE == clazz
                || Byte.class == clazz || Byte.TYPE == clazz
                || Short.class == clazz || Short.TYPE == clazz) return Datatypes.get(IntegerDatatype.NAME).parse(value);
        if (Date.class == clazz) {
            try {
                return Datatypes.get(DateTimeDatatype.NAME).parse(value);
            } catch (ParseException e) {
                try {
                    return Datatypes.get(DateDatatype.NAME).parse(value);
                } catch (ParseException e1) {
                    return Datatypes.get(TimeDatatype.NAME).parse(value);
                }
            }
        }
        if (BigDecimal.class == clazz) return Datatypes.get(BigDecimalDatatype.NAME).parse(value);
        if (Boolean.class == clazz || Boolean.TYPE == clazz) return Datatypes.get(BooleanDatatype.NAME).parse(value);
        if (Long.class == clazz || Long.TYPE == clazz) return Datatypes.get(LongDatatype.NAME).parse(value);
        if (Double.class == clazz || Double.TYPE == clazz
                || Float.class == clazz || Float.TYPE == clazz) return Datatypes.get(DoubleDatatype.NAME).parse(value);
        if (UUID.class == clazz) return UUID.fromString(value);
        throw new IllegalArgumentException("Parameters of type " + clazz.getName() + " are not supported");
    }

}
