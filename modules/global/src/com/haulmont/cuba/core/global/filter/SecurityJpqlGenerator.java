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

package com.haulmont.cuba.core.global.filter;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.entity.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class SecurityJpqlGenerator extends AbstractJpqlGenerator {
    @Override
    protected String generateClauseText(Clause condition) {
        ParameterInfo parameterInfo = condition.getCompiledParameters().iterator().next();
        Class javaClass = parameterInfo.getJavaClass();
        if (javaClass == null) {
            throw new UnsupportedOperationException();
        }

        Op operator = condition.getOperator();
        String jpqlOperator = operator.forJpql();
        String parameterInfoValue = parameterInfo.getValue();

        String valueToString = valueToString(javaClass, parameterInfoValue, operator);

        if (condition.getType() == ConditionType.RUNTIME_PROPERTY) {
            return condition.getContent().replace(":" + parameterInfo.getName(), valueToString);
        } else if (operator.isUnary()) {
            return format("{E}.%s %s", condition.getName(), jpqlOperator);
        } else if (Entity.class.isAssignableFrom(javaClass)) {
            return String.format("{E}.%s.id %s %s", condition.getName(), jpqlOperator, valueToString);
        } else {
            return String.format("{E}.%s %s %s", condition.getName(), jpqlOperator, valueToString);
        }
    }

    protected String valueToString(Class javaClass, String value, Op operator) {
        if (value == null) {
            return "null";
        } else if (operator == Op.IN || operator == Op.NOT_IN) {
            // IN operator has its own logic, because we have to convert string with default values to the right form
            if (value.startsWith("[") || value.startsWith("(")) {
                value = value.replaceAll("[\\[\\]()]", "");
            }

            String[] splittedValues = value.split(",");
            String convertedValue = Arrays.stream(splittedValues)
                    .map(String::trim)
                    .map(v -> valueToString(javaClass, v, Op.EQUAL))
                    .collect(Collectors.joining(", ", "(", ")"));
           return convertedValue;
        } else  if (Number.class.isAssignableFrom(javaClass)
                || Boolean.class.isAssignableFrom(javaClass)) {
            return value;
        } else if (EnumClass.class.isAssignableFrom(javaClass)) {
            //noinspection unchecked
            Enum enumValue = Enum.valueOf(javaClass, value);
            Object enumId = ((EnumClass) enumValue).getId();
            return (enumId instanceof Number) ? enumId.toString() : "'" + enumId + "'";
        } else if (Entity.class.isAssignableFrom(javaClass)) {
            if (BaseIntegerIdEntity.class.isAssignableFrom(javaClass)
                    || BaseLongIdEntity.class.isAssignableFrom(javaClass)
                    || BaseDbGeneratedIdEntity.class.isAssignableFrom(javaClass)) {
                return value;
            } else {
                return "'" + value + "'";
            }
        } else {
            if (operator == Op.CONTAINS || operator == Op.DOES_NOT_CONTAIN) {
                return "'%" + value + "%'";
            } else if (operator == Op.STARTS_WITH) {
                return "'" + value + "%'";
            } else if (operator == Op.ENDS_WITH) {
                return "'%" + value + "'";
            }
            return "'" + value + "'";
        }
    }
}