/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.core.sys.jpql.tree;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.ErrorRec;
import com.haulmont.cuba.core.sys.jpql.QueryBuilder;
import com.haulmont.cuba.core.sys.jpql.QueryVariableContext;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import java.util.List;

public class TreatPathNode extends PathNode {
    private String subtype;

    public TreatPathNode(Token token, String entityVariableName, String subtype) {
        super(token, entityVariableName);
        this.subtype = subtype;
    }

    public TreatPathNode(int type, String entityVariableName, String subtype) {
        super(type, entityVariableName);
        this.subtype = subtype;
    }

    @Override
    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        sb.appendString("treat(");
        super.treeToQueryPre(sb, invalidNodes);
        sb.appendString(" as ");
        sb.appendString(subtype);
        sb.appendString(")");
        return null;
    }

    @Override
    protected PathNode createDuplicate() {
        return new TreatPathNode(token, entityVariableName, subtype);
    }

    @Override
    public Pointer resolvePointer(DomainModel model, QueryVariableContext queryVC) {
        return super.resolvePointer(model, queryVC);
    }

    public String getSubtype() {
        return subtype;
    }

    ;

}
