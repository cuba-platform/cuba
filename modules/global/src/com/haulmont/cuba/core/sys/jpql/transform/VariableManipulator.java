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

import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 */
public class VariableManipulator implements TreeVisitorAction {
    private List<PathNode> variableUses = new ArrayList<>();

    public VariableManipulator() {
    }

    @Override
    public Object pre(Object o) {
        // todo небезопасен, если изменяемое дерево определяет свои собственные переменные
        if (o instanceof PathNode) {
            PathNode pathNode = (PathNode) o;
            variableUses.add(pathNode);
        }
        return o;
    }

    @Override
    public Object post(Object o) {
        return o;
    }

    public Set<String> getUsedVariableNames() {
        Set<String> result = new HashSet<>();
        for (PathNode variableUse : variableUses) {
            result.add(variableUse.getEntityVariableName());
        }
        return result;
    }

    public String getVariableNameInUse(int index) {
        return variableUses.get(index).getEntityVariableName();
    }

    public void renameVariable(String origName, EntityReference newName) {
        if (origName == null)
            throw new NullPointerException("No original name passed");
        if (newName == null)
            throw new NullPointerException("No new name passed");

        for (PathNode variableUse : variableUses) {
            if (origName.equals(variableUse.getEntityVariableName())) {
                newName.renameVariableIn(variableUse);
            }
        }
    }
}