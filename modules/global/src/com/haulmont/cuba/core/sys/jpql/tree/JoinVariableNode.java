/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.tree;

import com.haulmont.cuba.core.sys.jpql.ErrorRec;
import com.haulmont.cuba.core.sys.jpql.QueryBuilder;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author chevelev
 * @version $Id$
 */
public class JoinVariableNode extends BaseJoinNode {
    private String joinSpec;
    private String joinCondition;

    public JoinVariableNode(Token token, String joinSpec, String variableName, String joinCondition) {
        super(token, variableName);
        this.joinSpec = joinSpec;
        this.joinCondition = joinCondition;
    }

    public JoinVariableNode(int type, String joinSpec, String variableName, String joinCondition) {
        this(new CommonToken(type, ""), joinSpec, variableName, joinCondition);
    }

    @Override
    public String toString() {
        return (token != null ? token.getText() : "") + "Join variable: " + variableName;
    }

    @Override
    public Tree dupNode() {
        JoinVariableNode result = new JoinVariableNode(token, joinSpec, variableName, joinCondition);
        dupChildren(result);
        return result;
    }

    @Override
    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        sb.appendSpace();
        sb.appendString(joinSpec);
        sb.appendSpace();
        return this;
    }

    @Override
    public CommonTree treeToQueryPost(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        // должно появится после определения сущности, из которой выбирают, поэтому в post
        sb.appendSpace();
        sb.appendString(variableName);
        if (StringUtils.isNotBlank(joinCondition)) {
            sb.appendSpace();
            sb.appendString("on");
            sb.appendSpace();
            sb.appendString(joinCondition);
        }

        return this;
    }

    @Nullable
    public PathNode findPathNode() {
        for (Object child : getChildren()) {
            if (child instanceof PathNode) {
                return (PathNode) child;
            }
        }

        return null;
    }

    public String getJoinSpec() {
        return joinSpec;
    }

    public String getJoinCondition() {
        return joinCondition;
    }
}