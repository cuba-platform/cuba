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

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.restapi.transform.JsonTransformationDirection;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

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

    public Object toObject(Type type, String value, @Nullable String modelVersion) throws ParseException {
        if (value == null) return null;
        Class clazz;
        Class argumentTypeClass = null;
        if (type instanceof Class) {
            clazz = (Class) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                if (actualTypeArguments[0] instanceof Class) {
                    argumentTypeClass = (Class) actualTypeArguments[0];
                } else if (actualTypeArguments[0] instanceof ParameterizedType) {
                    argumentTypeClass = (Class) ((ParameterizedType) actualTypeArguments[0]).getRawType();
                }
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

    public String serialize(Object instance) {
        return entitySerializationAPI.objectToJson(instance);
    }

    public Map<String, String> parseParamsJson(String paramsJson) {
        Map<String, String> result = new LinkedHashMap<>();
        if (Strings.isNullOrEmpty(paramsJson)) return result;

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(paramsJson).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String paramName = entry.getKey();
            JsonElement paramValue = entry.getValue();
            if (paramValue.isJsonNull()) {
                result.put(paramName, null);
            } else if (paramValue.isJsonPrimitive()) {
                result.put(paramName, paramValue.getAsString());
            } else {
                result.put(paramName, paramValue.toString());
            }
        }

        return result;
    }
}
