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

import com.haulmont.cuba.core.entity.Entity;

import static java.lang.String.format;

public class SecurityJpqlGenerator extends AbstractJpqlGenerator {
    @Override
    protected String generateClauseText(Clause condition) {
        ParameterInfo parameterInfo = condition.getParameters().iterator().next();
        Class javaClass = parameterInfo.getJavaClass();
        if (javaClass == null) {
            throw new UnsupportedOperationException();
        }

        Op operator = condition.getOperator();
        String jpqlOperator = operator.forJpql();
        String valueToString = valueToString(javaClass, parameterInfo.getValue(), operator);
        if (operator == Op.IN || operator == Op.NOT_IN) {
            valueToString = valueToString.replace("[", "(").replace("]", ")");
        }

        if (operator.isUnary()) {
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
        } else if (Number.class.isAssignableFrom(javaClass)
                || Boolean.class.isAssignableFrom(javaClass)
                || operator == Op.IN || operator == Op.NOT_IN) {
            return value;
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