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

package com.haulmont.cuba.core.sys.jpql.tree;

import com.haulmont.cuba.core.sys.jpql.*;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import java.util.Collections;
import java.util.List;

import static com.haulmont.cuba.core.sys.jpql.JPATreeNodes.createWord;

public class PathNode extends BaseCustomNode {
    private String entityVariableName;

    public PathNode(Token token, String entityVariableName) {
        super(token);
        this.entityVariableName = entityVariableName;
    }

    public PathNode(int type, String entityVariableName) {
        this(new CommonToken(type, ""), entityVariableName);
    }

    public String getEntityVariableName() {
        return entityVariableName;
    }

    @Override
    public PathNode dupNode() {
        PathNode result = new PathNode(token, entityVariableName);
        dupChildren(result);
        return result;
    }

    public Pointer resolvePointer(DomainModel model, QueryVariableContext queryVC) {
        return createEntityPath().resolvePointer(model, queryVC);
    }

    public List<Pointer> resolveTransitionalPointers(DomainModel model, QueryVariableContext queryVC) {
        return createEntityPath().resolveTransitionalPointers(model, queryVC);
    }

    @Override
    public String toString() {
        return (token != null ? token.getText() : "") + "Path entity variable: " + entityVariableName;
    }

    @Override
    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        sb.appendString(asPathString());
        return null;
    }

    public String asPathString() {
        return asPathString('.');
    }

    public String asPathString(char separator) {
        StringBuilder result = new StringBuilder();
        result.append(entityVariableName);
        if (children != null) {
            for (Object child : children) {
                result.append(separator)
                        .append(child.toString());
            }
        }
        return result.toString();
    }

    public void renameVariableTo(String newVariableName) {
        entityVariableName = newVariableName;
    }

    public void addDefaultChildren(String path) {
        String[] fields = path.split("\\.");
        for (String field : fields) {
            addChild(createWord(field));
        }
    }

    public void addDefaultChild(String field) {
        addChild(createWord(field));
    }

    protected EntityPath createEntityPath() {
        List treeItems = getChildren();
        if (treeItems == null) {
            treeItems = Collections.emptyList();
        }
        String[] parts = new String[treeItems.size()];
        for (int i = 0; i < treeItems.size(); i++) {
            CommonTree treeItem = (CommonTree) treeItems.get(i);
            parts[i] = treeItem.getText();
        }

        EntityPath path = new EntityPath();
        path.topEntityVariableName = entityVariableName;
        path.lastEntityFieldPattern = null;
        path.traversedFields = parts;
        return path;
    }
}