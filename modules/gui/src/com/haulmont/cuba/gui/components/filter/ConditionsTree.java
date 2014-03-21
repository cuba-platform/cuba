/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a data structure to store conditions inside generic filter.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ConditionsTree extends Tree<AbstractCondition> {

    public ConditionsTree() {
        super(new ArrayList<Node<AbstractCondition>>());
    }

    /**
     * Get all conditions as a plain list.
     * @return  conditions list
     */
    public List<AbstractCondition> toConditionsList() {
        List<AbstractCondition> list = new ArrayList<AbstractCondition>();
        for (Node<AbstractCondition> node : toList()) {
            list.add(node.getData());
        }
        return list;
    }

    /**
     * Get root conditions.
     * @return  root conditions list
     */
    public List<AbstractCondition> getRoots() {
        List<AbstractCondition> list = new ArrayList<AbstractCondition>();
        for (Node<AbstractCondition> node : getRootNodes()) {
            list.add(node.getData());
        }
        return list;
    }

    /**
     * Get node corresponding to the condition.
     * @param condition condition
     * @return  node or null if not found
     */
    @Nullable
    public Node<AbstractCondition> getNode(AbstractCondition condition) {
        for (Node<AbstractCondition> node : toList()) {
            if (condition.equals(node.getData()))
                return node;
        }
        return null;
    }
}