/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global.filter;


import com.haulmont.cuba.core.entity.Entity;

import java.util.Iterator;
import java.util.List;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class JpqlGenerator {
    public String generateJpql(Condition condition) {
        if (condition instanceof LogicalCondition) {
            LogicalOp operation = ((LogicalCondition) condition).getOperation();
            List<Condition> conditions = condition.getConditions();
            if (conditions.isEmpty())
                return "";
            else {
                StringBuilder sb = new StringBuilder();

                if (conditions.size() > 1)
                    sb.append("(");

                for (Iterator<Condition> it = conditions.iterator(); it.hasNext(); ) {
                    Condition currentCondition= it.next();
                    sb.append(generateJpql(currentCondition));
                    if (it.hasNext())
                        sb.append(" ").append(operation.forJpql()).append(" ");
                }

                if (conditions.size() > 1)
                    sb.append(")");

                return sb.toString();
            }
        } else if (condition instanceof Clause) {
            ParameterInfo parameterInfo = condition.getParameters().iterator().next();
            Class javaClass = parameterInfo.getJavaClass();
            if (javaClass == null) {
                throw new UnsupportedOperationException();
            } else if (Number.class.isAssignableFrom(javaClass) || Boolean.class.isAssignableFrom(javaClass)) {
                return String.format("{E}.%s %s %s",
                        condition.getName(), ((Clause) condition).getOperator().forJpql(), parameterInfo.getValue());
            } else if (Entity.class.isAssignableFrom(javaClass)) {
                return String.format("{E}.%s.id %s '%s'",
                        condition.getName(), ((Clause) condition).getOperator().forJpql(), parameterInfo.getValue());
            } else if (String.class.isAssignableFrom(javaClass)) {
                return String.format("{E}.%s %s '%s'",
                        condition.getName(), ((Clause) condition).getOperator().forJpql(), parameterInfo.getValue());
            } else {
                return String.format("{E}.%s %s '%s'",
                        condition.getName(), ((Clause) condition).getOperator().forJpql(), parameterInfo.getValue());
            }
        }
        throw new UnsupportedOperationException();
    }
}
