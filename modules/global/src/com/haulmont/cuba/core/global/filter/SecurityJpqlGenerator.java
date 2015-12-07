/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global.filter;


import com.haulmont.cuba.core.entity.Entity;

import static java.lang.String.format;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class SecurityJpqlGenerator extends AbstractJpqlGenerator {
    @Override
    protected String generateClauseText(Clause condition) {
        ParameterInfo parameterInfo = condition.getParameters().iterator().next();
        Class javaClass = parameterInfo.getJavaClass();
        if (javaClass == null) {
            throw new UnsupportedOperationException();
        } else {
            String jpqlOperator = condition.getOperator().forJpql();
            if (condition.getOperator().isUnary()) {
                return format("{E}.%s %s", condition.getName(), jpqlOperator);
            } else if (Number.class.isAssignableFrom(javaClass) || Boolean.class.isAssignableFrom(javaClass)) {
                return String.format("{E}.%s %s %s",
                        condition.getName(), jpqlOperator, parameterInfo.getValue());
            } else if (Entity.class.isAssignableFrom(javaClass)) {
                return String.format("{E}.%s.id %s '%s'",
                        condition.getName(), jpqlOperator, parameterInfo.getValue());
            } else if (String.class.isAssignableFrom(javaClass)) {
                return String.format("{E}.%s %s '%s'",
                        condition.getName(), jpqlOperator, parameterInfo.getValue());
            } else {
                return String.format("{E}.%s %s '%s'",
                        condition.getName(), jpqlOperator, parameterInfo.getValue());
            }
        }
    }
}
