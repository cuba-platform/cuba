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

import java.util.List;
import java.util.Set;

/**
 */
public abstract class Condition implements Cloneable {
    protected String name;

    public Condition(String name) {
        this.name = name;
    }

    public Condition copy() {
        try {
            return (Condition) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract List<Condition> getConditions();

    public abstract void setConditions(List<Condition> conditions);

    public abstract Set<ParameterInfo> getParameters();

    public abstract Set<String> getJoins();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}