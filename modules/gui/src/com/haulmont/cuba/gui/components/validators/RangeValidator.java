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

package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RangeValidator implements Field.Validator {

    protected String message;
    protected String messagesPack;
    protected Messages messages = AppBeans.get(Messages.NAME);

    protected Class<? extends Comparable> type = String.class;
    protected Comparable minValue = null;
    protected boolean includeMinValue = true;
    protected Comparable maxValue = null;
    protected boolean includeMaxValue = true;
    protected boolean ignoreCase = false;

    public RangeValidator(Element element, String messagesPack) {
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;

        includeMinValue = getBooleanFromString(element.attributeValue("includeMinValue"), includeMinValue);
        includeMinValue = getBooleanFromString(element.attributeValue("includeMaxValue"), includeMaxValue);

        ignoreCase = getBooleanFromString(element.attributeValue("ignoreCase"), ignoreCase);

        String typeStr = element.attributeValue("valueType");
        if (StringUtils.isNotEmpty(typeStr)) {
            type = getTypeFromString(typeStr);
        }

        String minValueStr = element.attributeValue("minValue");
        String maxValueStr = element.attributeValue("maxValue");
        if (StringUtils.isEmpty(minValueStr) || StringUtils.isEmpty(maxValueStr)) {
            throw new RuntimeException("'min' or 'max' values cannot be empty");
        }

        setMinValue(getValueFromString(minValueStr));
        setMaxValue(getValueFromString(maxValueStr));
    }

    public RangeValidator(String message, String messagesPack, Class<? extends Comparable> type, Comparable minValue, Comparable maxValue) {
        this.message = message;
        this.messagesPack = messagesPack;
        this.type = type;
        setMinValue(minValue);
        setMaxValue(maxValue);
    }

    public RangeValidator(Class<? extends Comparable> type, Comparable minValue, Comparable maxValue) {
        this(null, null, type, minValue, maxValue);
    }

    @Override
    public void validate(Object value) throws ValidationException {
        boolean result = false;

        if (type.isAssignableFrom(value.getClass())) {
            int compare = getCompareValue(minValue, (Comparable) value);
            if (compare < 0 || (compare == 0 && isIncludeMinValue())) {
                compare = getCompareValue(maxValue, (Comparable) value);
                if (compare > 0 || (compare == 0 && isIncludeMaxValue())) {
                    result = true;
                }
            }
        }

        if (!result) {
            String msg = message != null
                    ? messages.getTools().loadString(messagesPack, message)
                    : "Value '%s' is not included in the range from '%s' to '%s'";
            throw new ValidationException(String.format(msg, value, minValue, maxValue));
        }
    }

    public boolean isIncludeMinValue() {
        return includeMinValue;
    }

    public void setIncludeMinValue(boolean includeMinValue) {
        this.includeMinValue = includeMinValue;
    }

    public boolean isIncludeMaxValue() {
        return includeMaxValue;
    }

    public void setIncludeMaxValue(boolean includeMaxValue) {
        this.includeMaxValue = includeMaxValue;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public Comparable getMinValue() {
        return minValue;
    }

    public void setMinValue(Comparable minValue) {
        checkValueType(minValue);
        this.minValue = minValue;
    }

    public Comparable getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Comparable maxValue) {
        checkValueType(maxValue);
        this.maxValue = maxValue;
    }

    protected Class<? extends Comparable> getTypeFromString(String typeStr) {
        Class type;
        try {
            type = Class.forName(typeStr);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Type '%s' not found", typeStr));
        }

        if (!Arrays.asList(type.getInterfaces()).contains(Comparable.class)) {
            throw new RuntimeException(String.format("Type '%s' is not comparable", typeStr));
        }

        return type;
    }

    protected Comparable getValueFromString(String valueStr) {
        if (String.class.equals(type)) {
            return valueStr;
        } else {
            Method method;
            try {
                method = type.getMethod("valueOf", String.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(
                        String.format("Type '%s' does not contain method 'valueOf(String)'", type.getName()));
            }
            try {
                return (Comparable) method.invoke(type, valueStr);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(
                        String.format("Value '%s' conversion failed", valueStr));
            }
        }
    }

    protected boolean getBooleanFromString(@Nullable String stringValue, boolean defaultValue) {
        if (stringValue == null) {
            return defaultValue;
        } else {
            return Boolean.valueOf(stringValue);
        }
    }

    protected int getCompareValue(Comparable src, Comparable to) {
        if (String.class.isAssignableFrom(type) && src instanceof String && isIgnoreCase()) {
            return ((String) src).compareToIgnoreCase((String) to);
        }
        return src.compareTo(to);
    }

    protected void checkValueType(Comparable value) {
        if (!type.isAssignableFrom(value.getClass())) {
            throw new RuntimeException(String.format("Wrong type of value '%s'", value));
        }
    }
}