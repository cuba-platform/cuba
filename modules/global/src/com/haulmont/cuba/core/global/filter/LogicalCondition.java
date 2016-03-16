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

import java.util.*;

/**
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