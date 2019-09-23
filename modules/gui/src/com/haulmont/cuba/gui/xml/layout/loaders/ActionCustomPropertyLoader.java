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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.google.common.base.Splitter;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.components.Action;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component(ActionCustomPropertyLoader.NAME)
public class ActionCustomPropertyLoader {

    public static final String NAME = "cuba_ActionCustomPropertyLoader";

    private static final Logger log = LoggerFactory.getLogger(ActionCustomPropertyLoader.class);

    public void load(Action instance, String propertyName, String stringValue) {
        String setterName = "set" + StringUtils.capitalize(propertyName);
        try {
            Method method = Arrays.stream(instance.getClass().getMethods())
                    .filter(m -> m.getName().equals(setterName) && m.getParameterCount() == 1)
                    .findAny()
                    .orElseThrow(() -> new DevelopmentException(
                            "Unable to set action property '" + propertyName + "': cannot find setter method with single parameter"));

            Class<?> parameterType = method.getParameterTypes()[0];
            Type genericParameterType = method.getGenericParameterTypes()[0];
            Object value = parseStringValue(stringValue, parameterType, genericParameterType);
            method.invoke(instance, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DevelopmentException("Unable to set action property '" + propertyName + "': " + e);
        }
    }

    protected Object parseStringValue(String stringValue, Type propType, Type genericParameterType) {
        Object value = null;

        if (String.class == propType) {
            value = stringValue;

        } else if (propType instanceof Class && ((Class) propType).isEnum() && !StringUtils.isBlank(stringValue)) {
            for (Object enumConst : ((Class) propType).getEnumConstants()) {
                Enum en = (Enum) enumConst;
                if (en.name().equals(stringValue)) {
                    value = en;
                    break;
                }
            }

        } else if (Class.class == propType) {
            value = ReflectionHelper.getClass(stringValue);

        } else if (Boolean.class == propType || Boolean.TYPE == propType) {
            value = Boolean.valueOf(stringValue);

        } else if (Byte.class == propType || Byte.TYPE == propType) {
            value = parseNumber(stringValue, Byte.class);

        } else if (Short.class == propType || Short.TYPE == propType) {
            value = parseNumber(stringValue, Short.class);

        } else if (Integer.class == propType || Integer.TYPE == propType) {
            value = parseNumber(stringValue, Integer.class);

        } else if (Long.class == propType || Long.TYPE == propType) {
            value = parseNumber(stringValue, Long.class);

        } else if (Float.class == propType || Float.TYPE == propType) {
            value = parseNumber(stringValue, Float.class);

        } else if (Double.class == propType || Double.TYPE == propType) {
            value = parseNumber(stringValue, Double.class);

        } else if (List.class == propType) {
            value = parseList(stringValue, genericParameterType);
        }

        if (value == null) {
            log.warn("Unable to set value {} for property of type {}", stringValue, propType);
        }
        return value;
    }

    protected Object parseNumber(String stringValue, Class<? extends Number> numberType) {
        if (!NumberUtils.isParsable(stringValue)) {
            throw new DevelopmentException(String.format("Unable to parse '%s' as '%s'", stringValue, numberType));
        }
        return org.springframework.util.NumberUtils.parseNumber(stringValue, numberType);
    }

    private Object parseList(String stringValue, Type genericParameterType) {
        if (genericParameterType instanceof ParameterizedType) {
            Type itemType = ((ParameterizedType) genericParameterType).getActualTypeArguments()[0];
            List<String> strings = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(stringValue);
            return strings.stream()
                    .map(s -> parseStringValue(s, itemType, null))
                    .collect(Collectors.toList());
        }
        return null;
    }

}
