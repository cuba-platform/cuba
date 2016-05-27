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

import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.tree.Tree;

public class EntityNameEntityReference implements EntityReference {
    private String entityName;

    public EntityNameEntityReference(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public String replaceEntries(String queryPart, String replaceablePart) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void renameVariableIn(PathNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tree createNode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isJoinableTo(IdentificationVariableNode node) {
        return entityName.equals(node.getEffectiveEntityName());
    }

    @Override
    public PathEntityReference addFieldPath(String fieldPath) {
        throw new UnsupportedOperationException();
    }
}