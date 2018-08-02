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

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.tree.TreeToQueryCapable;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TreeToQuery implements TreeVisitorAction {
    private QueryBuilder sb = new QueryBuilder();
    private List<ErrorRec> invalidNodes = new ArrayList<>();

    @Override
    public Object pre(Object t) {
        if (!(t instanceof CommonTree)) {
            return t;
        }

        if (t instanceof CommonErrorNode) {
            invalidNodes.add(new ErrorRec((CommonErrorNode) t, "Error node"));
            return t;
        }

        CommonTree node = (CommonTree) t;

        if (node.token == null)
            return t;

        if (node.getType() == JPA2Lexer.HAVING ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_SIMPLE_CONDITION
                        && !parentNodeHasPreviousLparen(node) && !isDecimal(node) ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_GROUP_BY && !isExtractDatePartNode(node) ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_ORDER_BY && node.getType() != JPA2Lexer.T_ORDER_BY_FIELD ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_CONDITION && node.getType() == JPA2Lexer.LPAREN && (node.childIndex == 0 || node.parent.getChild(node.childIndex - 1).getType() != JPA2Lexer.LPAREN) ||
                node.getType() == JPA2Lexer.AND ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_ORDER_BY_FIELD && !isExtractDatePartNode(node) ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_SELECTED_ITEM && node.getType() == JPA2Lexer.AS ||
                node.getType() == JPA2Lexer.OR ||
                node.getType() == JPA2Lexer.NOT ||
                node.getType() == JPA2Lexer.DISTINCT && node.childIndex == 0 ||
                node.getType() == JPA2Lexer.JOIN ||
                node.getType() == JPA2Lexer.LEFT ||
                node.getType() == JPA2Lexer.OUTER ||
                node.getType() == JPA2Lexer.INNER ||
                node.getType() == JPA2Lexer.FETCH ||
                node.getType() == JPA2Lexer.CASE ||
                node.getType() == JPA2Lexer.WHEN ||
                node.getType() == JPA2Lexer.THEN ||
                node.getType() == JPA2Lexer.ELSE ||
                node.getType() == JPA2Lexer.END ||
                isExtractFromNode(node)
                ) {
            sb.appendSpace();
        }

        if (node.getType() == JPA2Lexer.T_ORDER_BY_FIELD && node.childIndex > 0 && node.parent.getChild(node.childIndex - 1).getType() == JPA2Lexer.T_ORDER_BY_FIELD) {
            sb.appendString(", ");
        }

        if (isGroupByItem(node)) {
            if (node.childIndex > 0 && isGroupByItem((CommonTree) node.parent.getChild(node.childIndex - 1))) {
                if (!isExtractFromNode(node) && !isExtractDatePartNode(node)
                        && !isExtractArgNode(node) && !isExtractEnd(node)) {
                    sb.appendString(", ");
                }
            }
        }

        if (node instanceof TreeToQueryCapable) {
            return ((TreeToQueryCapable) t).treeToQueryPre(sb, invalidNodes);
        }

        if (node.getType() == JPA2Lexer.T_SELECTED_ITEMS) {
            return t;
        }

        if (node.getType() == JPA2Lexer.T_SOURCES) {
            sb.appendString("from ");
            return t;
        }

        sb.appendString(node.toString());
        return t;
    }

    private boolean parentNodeHasPreviousLparen(CommonTree node) {
        return (node.childIndex == 0 && node.parent.childIndex > 0 && node.parent.parent.getChild(node.parent.childIndex - 1).getType() == JPA2Lexer.LPAREN);
    }

    private boolean isDecimal(CommonTree node) {
        if (node instanceof CommonTree) {
            if (Objects.equals(".", node.getText())) {
                return true;
            }
            if (node.getType() == JPA2Lexer.INT_NUMERAL && node.childIndex > 0) {
                Tree prevNode = node.parent.getChild(node.childIndex - 1);
                return prevNode != null && Objects.equals(".", prevNode.getText());
            }
        }
        return false;
    }

    private boolean isGroupByItem(CommonTree node) {
        if (node.parent != null && node.parent.getType() == JPA2Lexer.T_GROUP_BY) {
            if (node.getType() != JPA2Lexer.BY && node.getType() != JPA2Lexer.GROUP) {
                return true;
            }
        }
        return false;
    }

    private boolean isExtractFromNode(CommonTree node) {
        if (node.parent != null && "from".equalsIgnoreCase(node.getText())) {
            //third part of EXTRACT expression
            if (node.childIndex >= 2) {
                Tree extractNode = node.parent.getChild(node.childIndex - 2);
                if ("extract(".equalsIgnoreCase(extractNode.getText())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isExtractDatePartNode(CommonTree node) {
        if (node.parent != null) {
            //date part of EXTRACT expression
            if (node.childIndex >= 1) {
                Tree extractNode = node.parent.getChild(node.childIndex - 1);
                if ("extract(".equalsIgnoreCase(extractNode.getText())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isExtractArgNode(CommonTree node) {
        if (node.parent != null) {
            //argument of EXTRACT expression
            if (node.childIndex >= 3) {
                Tree extractNode = node.parent.getChild(node.childIndex - 3);
                if ("extract(".equalsIgnoreCase(extractNode.getText())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isExtractEnd(CommonTree node) {
        if (node.parent != null && ")".equalsIgnoreCase(node.getText())) {
            //third part of EXTRACT expression
            if (node.childIndex >= 4) {
                Tree extractNode = node.parent.getChild(node.childIndex - 4);
                if ("extract(".equalsIgnoreCase(extractNode.getText())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object post(Object t) {
        if (!(t instanceof CommonTree))
            return t;

        if (t instanceof CommonErrorNode) {
            return t;
        }

        CommonTree node = (CommonTree) t;

        if (node.token == null)
            return t;

        if (node.getType() == JPA2Lexer.DISTINCT ||
                node.getType() == JPA2Lexer.FETCH ||
                node.getType() == JPA2Lexer.THEN ||
                node.getType() == JPA2Lexer.ELSE ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_SELECTED_ITEM && node.getType() == JPA2Lexer.AS ||
                isExtractFromNode(node)) {
            sb.appendSpace();
        }


        if (node instanceof TreeToQueryCapable) {
            return ((TreeToQueryCapable) t).treeToQueryPost(sb, invalidNodes);
        }

        return t;
    }

    public List<ErrorRec> getInvalidNodes() {
        return Collections.unmodifiableList(invalidNodes);
    }

    public String getQueryString() {
        return sb.toString();
    }
}