/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.restapi.service.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class RestFilterGroupCondition implements RestFilterCondition {

    public enum Type {
        AND,
        OR
    }

    @Override
    public String toJpql() {
        String childConditionsJpql = conditions.stream()
                .map(RestFilterCondition::toJpql)
                .collect(Collectors.joining(" " + type.name().toLowerCase() + " "));
        return "(" + childConditionsJpql + ")";
    }

    private Type type;

    private List<RestFilterCondition> conditions = new ArrayList<>();

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<RestFilterCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<RestFilterCondition> conditions) {
        this.conditions = conditions;
    }
}
