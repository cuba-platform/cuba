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

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.tree.ParameterNode;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 */
public class ParameterCounter implements TreeVisitorAction {
    private boolean differentNamesOnly;
    private int totalParameterCount = 0;
    private Set<String> names = new HashSet<>();

    public ParameterCounter(boolean differentNamesOnly) {
        this.differentNamesOnly = differentNamesOnly;
    }

    @Override
    public Object pre(Object o) {
        if (o instanceof ParameterNode) {
            ParameterNode node = (ParameterNode) o;
            if (node.isNamed()) {
                names.add(node.getParameterReference());
            }
            totalParameterCount++;
        }
        return o;
    }

    @Override
    public Object post(Object o) {
        return o;
    }

    public int getParameterCount() {
        return differentNamesOnly ? names.size() : totalParameterCount;
    }

    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(names);
    }
}