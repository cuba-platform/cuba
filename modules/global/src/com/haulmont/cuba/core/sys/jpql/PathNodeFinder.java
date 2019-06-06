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
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.SelectedItemNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.List;

public class PathNodeFinder implements TreeVisitorAction {

    protected List<PathNode> selectedPathNodes = new ArrayList<>();
    protected List<PathNode> otherPathNodes = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked")
    public Object pre(Object t) {
        if (t instanceof PathNode) {
            PathNode pathNode = (PathNode) t;
            if (isSelectedPathNode(pathNode)) {
                selectedPathNodes.add(pathNode);
            } else {
                otherPathNodes.add(pathNode);
            }
        }
        return t;
    }

    @Override
    public Object post(Object t) {
        return null;
    }

    public List<PathNode> getSelectedPathNodes() {
        return selectedPathNodes;
    }

    public List<PathNode> getOtherPathNodes() {
        return otherPathNodes;
    }

    protected boolean isSelectedPathNode(PathNode pathNode) {
        Tree node = pathNode;
        while (node.getParent() != null) {
            if (node.getParent() instanceof SelectedItemNode) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }
}
