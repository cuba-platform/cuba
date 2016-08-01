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

import com.haulmont.cuba.core.sys.jpql.ErrorRec;
import com.haulmont.cuba.core.sys.jpql.QueryBuilder;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import java.util.List;

public class ParameterNode extends BaseCustomNode {

    private ParameterNode(Token token) {
        super(token);
    }

    public ParameterNode(int type) {
        this(new CommonToken(type, ""));
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public Tree dupNode() {
        ParameterNode result = new ParameterNode(token);
        dupChildren(result);
        return result;
    }

    @Override
    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        if (getChildren().size() == 2) {
            String child0Text = getChild(0).getText();
            String child1Text = getChild(1).getText();
            if (":".equals(child0Text) || "?".equals(child0Text)) {
                sb.appendString(child0Text);
                sb.appendString(child1Text);
                return null;
            }
        }

        return super.treeToQueryPre(sb, invalidNodes);
    }

    public boolean isNamed() {
        return getChild(0).getText().charAt(0) == ':';
    }

    public boolean isIndexed() {
        return getChild(0).getText().equals("?");
    }

    public String getParameterReference() {
        return isIndexed() ? getChild(1).getText() : getChild(0).getText().substring(1);
    }
}