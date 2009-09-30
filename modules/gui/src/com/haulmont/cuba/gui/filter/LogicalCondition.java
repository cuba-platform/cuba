/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.09.2009 11:40:35
 *
 * $Id$
 */
package com.haulmont.cuba.gui.filter;

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
    public Set<String> getParameters() {
        Set<String> set = new HashSet<String>();
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
