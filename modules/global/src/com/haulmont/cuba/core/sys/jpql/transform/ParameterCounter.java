/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.tree.ParameterNode;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author chevelev
 * @version $Id$
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