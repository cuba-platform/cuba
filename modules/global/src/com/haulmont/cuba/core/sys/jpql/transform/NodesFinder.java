/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class NodesFinder<T> implements TreeVisitorAction {
    protected Class<T> nodeClassToFind;
    protected List<T> foundNodes = new ArrayList<>();

    public NodesFinder(Class<T> nodeClassToFind) {
        this.nodeClassToFind = nodeClassToFind;
    }

    public List<T> getFoundNodes() {
        return foundNodes;
    }

    @Override
    public Object pre(Object node) {
        if (nodeClassToFind.isAssignableFrom(node.getClass())) {
            foundNodes.add((T) node);
        }
        return node;
    }

    @Override
    public Object post(Object node) {
        return node;
    }
}
