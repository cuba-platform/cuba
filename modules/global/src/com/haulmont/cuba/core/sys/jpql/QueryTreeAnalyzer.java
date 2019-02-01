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
import com.haulmont.cuba.core.sys.jpql.model.JpqlEntityModel;
import com.haulmont.cuba.core.sys.jpql.transform.NodesFinder;
import com.haulmont.cuba.core.sys.jpql.transform.ParameterCounter;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.ParameterNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.SimpleConditionNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class QueryTreeAnalyzer {
    protected QueryTree queryTree;

    public QueryTreeAnalyzer(QueryTree queryTree) {
        this.queryTree = queryTree;
    }

    public String getMainEntityName(IdentificationVariableNode identificationVariable) {
        if (identificationVariable == null) {
            throw new RuntimeException(format("Unable to find entity name [%s]", StringUtils.strip(queryTree.getQueryString())));
        }
        return identificationVariable.getEntityNameFromQuery();
    }

    public String getMainEntityVariable(IdentificationVariableNode identificationVariable) {
        if (identificationVariable == null) {
            throw new RuntimeException(format("Unable to find entity variable [%s]", StringUtils.strip(queryTree.getQueryString())));
        }
        return identificationVariable.getVariableName();
    }

    @Nullable
    public IdentificationVariableNode getMainIdentificationVariableNode() {
        List<IdentificationVariableNode> identificationVariables = queryTree.getAstIdentificationVariableNodes()
                .collect(Collectors.toList());

        Optional<String> variableName = queryTree.getAstSelectedPathNodes().findFirst().map(PathNode::getEntityVariableName);
        if (variableName.isPresent()) {
            for (IdentificationVariableNode node : identificationVariables) {
                if (variableName.get().equalsIgnoreCase(node.getVariableName())) {
                    return node;
                }
            }
        }

        return identificationVariables.stream().findFirst().orElse(null);
    }

    public String getMainSelectedEntityName(PathNode pathNode) {
        if (pathNode == null) {
            throw new RuntimeException(format("Unable to find selected entity name [%s]", StringUtils.strip(queryTree.getQueryString())));
        }
        QueryVariableContext variableContext = queryTree.getQueryVariableContext();
        JpqlEntityModel entity = variableContext.getEntityByVariableName(pathNode.getEntityVariableName());
        if (entity == null) {
            throw new RuntimeException(format("Unable to find selected entity name [%s]", StringUtils.strip(queryTree.getQueryString())));
        }
        return entity.getName();
    }

    public String getMainSelectedEntityVariable(PathNode pathNode) {
        if (pathNode == null) {
            throw new RuntimeException(format("Unable to find selected entity variable [%s]", StringUtils.strip(queryTree.getQueryString())));
        }
        return pathNode.getEntityVariableName();
    }

    @Nullable
    public PathNode getMainSelectedPathNode() {
        List<PathNode> pathNodes = queryTree.getAstSelectedPathNodes()
                .limit(2)
                .collect(Collectors.toList());
        if (pathNodes.size() == 0 || pathNodes.size() > 1) {
            return null;
        }

        return pathNodes.get(0);
    }

    public Set<String> getParamNames() {
        return queryTree.visit(new ParameterCounter(true)).getParameterNames();
    }

    public Set<String> getEntityNames() {
        return queryTree.visit(new EntitiesFinder())
                .resolveEntityNames(queryTree.getModel(), queryTree.getQueryVariableContext());
    }

    public List<SimpleConditionNode> getConditions() {
        return queryTree.visit(NodesFinder.of(SimpleConditionNode.class)).getFoundNodes();
    }

    public boolean isQueryWithJoins() {
        long identificationVariableCount = queryTree.getAstIdentificationVariableNodes().count();
        if (identificationVariableCount > 1) {
            return true;
        }
        long joinCount = queryTree.getAstJoinVariableNodes().count();
        return joinCount > 0;
    }

    public boolean isConditionForEntityProperty(SimpleConditionNode condition, String variableName, String property) {
        return generateChildrenByClass(condition, PathNode.class)
                .anyMatch(pathNode -> Objects.equals(pathNode.asPathString(), variableName + "." + property));
    }

    public boolean isConditionForParameter(SimpleConditionNode condition, String parameterName) {
        ParameterNode parameter = (ParameterNode) condition.getFirstChildWithType(JPA2Lexer.T_PARAMETER);
        if (parameter == null) {
            return false;
        }
        if (StringUtils.contains(parameter.getChild(0).getText(), parameterName)) {
            return true;
        }
        if (parameter.getChildCount() > 1 && Objects.equals(parameter.getChild(1).getText(), parameterName)) {
            return true;
        }
        return false;
    }

    public boolean isConditionIN(SimpleConditionNode condition) {
        return condition.getFirstChildWithType(JPA2Lexer.IN) != null;
    }

    public boolean isConditionISNULL(SimpleConditionNode condition) {
        for (int i = 0; i < condition.getChildCount(); i++) {
            Tree child = condition.getChild(i);
            if (i < condition.getChildCount() - 1) {
                Tree nextChild = condition.getChild(i + 1);
                if ("is".equalsIgnoreCase(child.getText()) && "null".equalsIgnoreCase(nextChild.getText())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isConditionISNOTNULL(SimpleConditionNode condition) {
        for (int i = 0; i < condition.getChildCount(); i++) {
            Tree child = condition.getChild(i);
            if (i < condition.getChildCount() - 2) {
                Tree notChild = condition.getChild(i + 1);
                Tree nullChild = condition.getChild(i + 2);
                if ("is".equalsIgnoreCase(child.getText()) &&
                        "not".equalsIgnoreCase(notChild.getText()) &&
                        "null".equalsIgnoreCase(nullChild.getText())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isVariablePathNode(PathNode pathNode) {
        return pathNode.getChildCount() == 0;
    }

    @SuppressWarnings("unchecked")
    protected <T> Stream<T> generateChildrenByClass(CommonTree commonTree, Class<T> clazz) {
        return commonTree.getChildren().stream()
                .filter(o -> clazz.isAssignableFrom(o.getClass()))
                .map(o -> (T) o);
    }
}