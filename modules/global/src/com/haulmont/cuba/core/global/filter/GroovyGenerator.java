/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global.filter;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class GroovyGenerator {
    protected final EnumSet METHOD_OPS = EnumSet.of(Op.CONTAINS, Op.STARTS_WITH, Op.ENDS_WITH, Op.DOES_NOT_CONTAIN);
    protected final EnumSet NEGATIVE_OPS = EnumSet.of(Op.DOES_NOT_CONTAIN, Op.NOT_IN);

    public String generateGroovy(Condition condition) {
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
                    Condition child = it.next();
                    sb.append(generateGroovy(child));
                    if (it.hasNext())
                        sb.append(" ").append(operation.forGroovy()).append(" ");
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
            }

            Op operator = ((Clause) condition).getOperator();
            String resultingClause = null;
            String groovyOperator = operator.forGroovy();

            if (operator.isUnary()) {
                resultingClause = format("{E}.%s %s", condition.getName(), groovyOperator);
            } else if (!METHOD_OPS.contains(operator)) {
                resultingClause = format("{E}.%s %s %s",
                        condition.getName(), groovyOperator, valueToString(javaClass, parameterInfo.getValue()));
            } else if (METHOD_OPS.contains(operator)) {
                resultingClause = format("{E}.%s.%s(%s)",
                        condition.getName(), groovyOperator, valueToString(javaClass, parameterInfo.getValue()));
            }

            if (NEGATIVE_OPS.contains(operator)) {
                resultingClause = "!(" + resultingClause + ")";
            }

            return resultingClause;
        }

        throw new UnsupportedOperationException();
    }

    protected String valueToString(Class javaClass, String value) {
        if (value == null) {
            return "null";
        } else if (Number.class.isAssignableFrom(javaClass)
                || Boolean.class.isAssignableFrom(javaClass)
                || Collection.class.isAssignableFrom(javaClass)) {
            return value;
        } else if (String.class.isAssignableFrom(javaClass)) {
            return "'" + value + "'";
        } else {
            return format("value(%s.class, '%s')", javaClass.getCanonicalName(), value);
        }
    }

}
