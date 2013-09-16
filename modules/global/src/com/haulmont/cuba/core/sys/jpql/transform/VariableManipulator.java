/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Alexander Chevelev
 * Date: 27.03.2011
 * Time: 0:36:19
 */
public class VariableManipulator implements TreeVisitorAction {
    private List<PathNode> variableUses = new ArrayList<PathNode>();

    public VariableManipulator() {
    }


    public Object pre(Object o) {
        // todo небезопасен, если изменяемое дерево определяет свои собственные переменные
        if (o instanceof PathNode) {
            PathNode pathNode = (PathNode) o;
            variableUses.add(pathNode);
        }
        return o;
    }

    public Object post(Object o) {
        return o;
    }

    public Set<String> getUsedVariableNames() {
        Set<String> result = new HashSet<String>();
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
