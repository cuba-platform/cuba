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

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.NestedDatasource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DsTree extends Tree<Datasource> {

    public DsTree(List<Datasource> datasources) {
        List<Datasource> backlog = new ArrayList<>(datasources);
        while (!backlog.isEmpty()) {
            for (Datasource datasource : new ArrayList<>(backlog)) {
                Node<Datasource> node = new Node<>(datasource);
                if (!(datasource instanceof NestedDatasource)
                        || !backlog.contains(((NestedDatasource) datasource).getMaster())) {
                    getRootNodes().add(node);
                    backlog.remove(datasource);
                } else {
                    Node<Datasource> masterNode = find(getRootNodes(), ((NestedDatasource) datasource).getMaster());
                    if (masterNode != null) {
                        masterNode.addChild(node);
                        backlog.remove(datasource);
                    }
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