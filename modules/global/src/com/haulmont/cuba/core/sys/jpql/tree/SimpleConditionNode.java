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

/**
 * Author: Alexander Chevelev
 * Date: 30.10.2010
 * Time: 4:15:07
 */
public class SimpleConditionNode extends BaseCustomNode {
    private SimpleConditionNode(Token token) {
        super(token);
    }

    public SimpleConditionNode(int type) {
        this(new CommonToken(type, ""));
    }

    @Override
    public String toString() {
        return "SIMPLE_CONDITION";
    }

    @Override
    public Tree dupNode() {
        SimpleConditionNode result = new SimpleConditionNode(token);
        dupChildren(result);
        return result;
    }

    @Override
    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        return super.treeToQueryPre(sb, invalidNodes);
    }

    @Override
    public CommonTree treeToQueryPost(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        return this;
    }
}