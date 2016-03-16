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

import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.List;

/**
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
