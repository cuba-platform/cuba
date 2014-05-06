/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.NestedDatasource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
* @author krivopustov
* @version $Id$
*/
public class DsTree extends Tree<Datasource> {

    public DsTree(List<Datasource> datasources) {
        List<Datasource> backlog = new ArrayList<>(datasources);
        while (!backlog.isEmpty()) {
            for (Datasource datasource : new ArrayList<>(backlog)) {
                Node<Datasource> node = new Node<>(datasource);
                if (datasource instanceof NestedDatasource) {
                    Node<Datasource> masterNode = find(getRootNodes(), ((NestedDatasource) datasource).getMaster());
                    if (masterNode != null) {
                        masterNode.addChild(node);
                        backlog.remove(datasource);
                    }
                } else {
                    getRootNodes().add(node);
                    backlog.remove(datasource);
                }
            }
        }
    }

    @Nullable
    private Node<Datasource> find(List<Node<Datasource>> nodes, Datasource target) {
        for (Node<Datasource> node : nodes) {
            if (node.getData().equals(target))
                return node;
            Node<Datasource> result = find(node.getChildren(), target);
            if (result != null)
                return result;
        }
        return null;
    }

    public List<Datasource> toDsList() {
        List<Datasource> result = new ArrayList<>();
        for (Node<Datasource> node : toList()) {
            result.add(node.getData());
        }
        return result;
    }
}
