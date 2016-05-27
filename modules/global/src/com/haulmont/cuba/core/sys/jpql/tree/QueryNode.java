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
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import java.util.List;

public class QueryNode extends BaseCustomNode {
    private Token lastToken;

    private QueryNode(Token token, Token lastToken) {
        super(token);
        this.lastToken = lastToken;
    }

    public QueryNode(int type, Token token) {
        this(token, null);
    }

    public QueryNode(int type, Token token, Token lastToken) {
        this(token, lastToken);
    }

    public boolean contains(int caret) {
        return lastToken == null ||
                caret >= token.getCharPositionInLine() && (caret - lastToken.getCharPositionInLine()) <= lastToken.getText().length();


    }

    @Override
    public Tree dupNode() {
        QueryNode result = new QueryNode(token, lastToken);
        dupChildren(result);
        return result;
    }

    @Override
    public String toStringTree() {
        return super.toStringTree();
    }

    @Override
    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        sb.appendString(getText());
        return this;
    }

    @Override
    public CommonTree treeToQueryPost(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        if (parent != null) {
            if (' ' == sb.getLast())
                sb.deleteLast();
            sb.appendString(") ");
        }
        return this;
    }
}