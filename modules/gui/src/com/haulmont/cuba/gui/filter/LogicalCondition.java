/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.filter;

import com.haulmont.cuba.gui.xml.ParameterInfo;

import java.util.*;

public class LogicalCondition extends Condition {

    private final LogicalOp operation;

    private List<Condition> conditions = new ArrayList<Condition>();

    public LogicalCondition(LogicalOp operation) {
        this.operation = operation;
    }

    public LogicalOp getOperation() {
        return operation;
    }

    @Override
    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String getContent() {
        if (conditions.isEmpty())
            return "";
        else {
            StringBuilder sb = new StringBuilder();

            if (conditions.size() > 1)
                sb.append("(");

            for (Iterator<Condition> it = conditions.iterator(); it.hasNext();) {
                Condition condition = it.next();
                sb.append(condition.getContent());
                if (it.hasNext())
                    sb.append(" ").append(operation.getText()).append(" ");
            }

            if (conditions.size() > 1)
                sb.append(")");

            return sb.toString();
        }
    }

    @Override
    public Set<ParameterInfo> getParameters() {
        Set<ParameterInfo> set = new HashSet<ParameterInfo>();
        for (Condition condition : conditions) {
            set.addAll(condition.getParameters());
        }
        return set;
    }

    @Override
    public Set<String> getJoins() {
        Set<String> set = new LinkedHashSet<String>();
        for (Condition condition : conditions) {
            set.addAll(condition.getJoins());
        }
        return set;
    }
}
