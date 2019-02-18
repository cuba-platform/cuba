/*
 * Copyright (c) 2008-2019 Haulmont.
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

import com.google.common.base.MoreObjects;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;

public class EntityVariable {
    private String entityName;
    private String variableName;

    public EntityVariable(String entityName, String variableName) {
        this.entityName = entityName;
        this.variableName = variableName;
    }

    public boolean supportsJoinTo(IdentificationVariableNode node) {
        return entityName.equals(node.getEffectiveEntityName()) || entityName.equals(node.getEntityNameFromQuery());
    }

    public String getEntityName() {
        return entityName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("entityName", entityName)
                .add("variableName", variableName)
                .toString();
    }
}