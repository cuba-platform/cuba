/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global.filter;

import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LogicalCondition extends Condition {
    private final LogicalOp operation;

    private List<Condition> conditions = new ArrayList<>();

    public LogicalCondition(String name, LogicalOp operation) {
        super(name);
        this.operation = operation;
    }

    public LogicalOp getOperation() {
        return operation;
    }

    @Override
    public List<Condition> getConditions() {
        return conditions;
    }

    @Override
    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public Set<ParameterInfo> getParameters() {
        Set<ParameterInfo> set = new HashSet<>();
        for (Condition condition : conditions) {
            set.addAll(condition.getParameters());
        }
        return set;
    }

    @Override
    public Set<String> getJoins() {
        Set<String> set = new LinkedHashSet<>();
        for (Condition condition : conditions) {
            set.addAll(condition.getJoins());
        }
        return set;
    }
}