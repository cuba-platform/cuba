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

package com.haulmont.restapi.common;

import com.google.gson.Gson;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.restapi.transform.JsonTransformationDirection;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 */
@Component("cuba_RestParseUtils")
public class RestParseUtils {

    @Inject
    protected EntitySerializationAPI entitySerializationAPI;

    @Inject
    protected RestControllerUtils restControllerUtils;

    @Inject
    protected Metadata metadata;

    public Object toObject(Type type, String value, String modelVersion) throws ParseException {
        Class clazz;
        Class argumentTypeClass = null;
        if (type instanceof Class) {
            clazz = (Class) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                argumentTypeClass = (Class) actualTypeArguments[0];
            }
            clazz = (Class) parameterizedType.getRawType();
        } else {
            throw new RuntimeException("Cannot handle the method argument with type " + type.getTypeName());
        }

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
        if (Entity.class.isAssignableFrom(clazz)) {
            return entitySerializationAPI.entityFromJson(value, metadata.getClassNN(clazz));
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            //if type argument for the collection is defined and is not entity, then do the basic deserialization
            if (argumentTypeClass != null) {
                if (!Entity.class.isAssignableFrom(argumentTypeClass)) {
                    return deserialize(value, clazz);
                }
            }
            //if type argument for the collection is defined and is entity or if there is no type argument then try to
            //deserialize entities collection
            MetaClass metaClass = null;
            if (argumentTypeClass != null) {
                metaClass = metadata.getClassNN(argumentTypeClass);
                String entityName = restControllerUtils.transformEntityNameIfRequired(metaClass.getName(), modelVersion, JsonTransformationDirection.TO_VERSION);
                value = restControllerUtils.transformJsonIfRequired(entityName, modelVersion, JsonTransformationDirection.FROM_VERSION, value);
            }
            return entitySerializationAPI.entitiesCollectionFromJson(value, metaClass);
        }
        return deserializePOJO(value, clazz);
    }

    public Object deserializePOJO(String json, Class clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public Object deserialize(String json, Class clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public String serializePOJO(Object pojoInstance, Class clazz) {
        Gson gson = new Gson();
        return gson.toJson(pojoInstance, clazz);
    }

    public String serialize(Object instance) {
        Gson gson = new Gson();
        return gson.toJson(instance);
    }
}
