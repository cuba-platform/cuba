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

import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.sys.jpql.*;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.JoinVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.SimpleConditionNode;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(QueryTransformer.NAME)
public class QueryTransformerAstBased implements QueryTransformer {
    protected DomainModel model;
    protected String query;
    protected QueryTree queryTree;
    protected QueryTreeTransformer queryTransformer;
    protected QueryTreeAnalyzer queryAnalyzer;
    protected Set<String> addedParams = new HashSet<>();

    public QueryTransformerAstBased(DomainModel model, String query) {
        this.model = model;
        this.query = query;
    }

    protected QueryTreeTransformer getTransformer() {
        if (queryTransformer == null) {
            queryTransformer = new QueryTreeTransformer(getTree());
        }
        return queryTransformer;
    }

    protected QueryTreeAnalyzer getAnalyzer() {
        if (queryAnalyzer == null) {
            queryAnalyzer = new QueryTreeAnalyzer(getTree());
        }
        return queryAnalyzer;
    }

    protected QueryTree getTree() {
        if (queryTree == null) {
            try {
                queryTree = new QueryTree(model, query);
            } catch (JPA2RecognitionException e) {
                throw new JpqlSyntaxException(format("Errors found for input JPQL:[%s]\n%s", StringUtils.strip(query), e.getMessage()));
            }
            List<ErrorRec> errors = new ArrayList<>(queryTree.getInvalidIdVarNodes());
            if (!errors.isEmpty()) {
                throw new JpqlSyntaxException(format("Errors found for input JPQL:[%s]", StringUtils.strip(query)), errors);
            }
        }
        return queryTree;
    }

    @Override
    public String getResult() {
        return getTree().visit(new TreeToQuery()).getQueryString().trim();
    }

    @Override
    public Set<String> getAddedParams() {
        return addedParams;
    }

    @Override
    public void handleCaseInsensitiveParam(String parameterName) {
        List<SimpleConditionNode> conditions = getAnalyzer().getConditions().stream()
                .filter(condition -> getAnalyzer().isConditionForParameter(condition, parameterName)).
                        collect(Collectors.toList());
        getTransformer().applyLowerCaseForConditions(conditions);
    }

    /**
     * @param where - "{E}" may be used as a replaceable entity placeholder. No such value
     *              should be used as a string constant
     */
    @Override
    public void addWhere(String where) {
        EntityReference entityReference = createMainIdentificationVariableReference();
        if (where.contains("{E}")) {
            where = entityReference.replaceEntries(where, "\\{E\\}");
        }
        addWhere(parseWhereCondition(where), entityReference, false);
    }

    @Override
    public void addWhereAsIs(String where) {
        addWhere(parseWhereCondition(where), null, false);
    }

    /**
     * copy from another statement
     *
     * @param statement from we copy where clause
     */
    @Override
    @Deprecated
    public void mergeWhere(String statement) {
        try {
            EntityReference entityReference = createMainIdentificationVariableReference();
            CommonTree whereTree = Parser.parse(statement, true);
            CommonTree whereClause = (CommonTree) whereTree.getFirstChildWithType(JPA2Lexer.T_CONDITION);
            addWhere(whereClause, entityReference, true);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addJoinAndWhere(String join, String where) {
        EntityReference entityReference = createMainIdentificationVariableReference();
        if (where.contains("{E}")) {
            where = entityReference.replaceEntries(where, "\\{E\\}");
        }
        if (join.contains("{E}")) {
            join = entityReference.replaceEntries(join, "\\{E\\}");
        }
        String[] strings = join.split(",");
        join = strings[0];
        if (StringUtils.isNotBlank(join)) {
            List<JoinVariableNode> joinVariableNodes = parseJoinCondition(join);
            boolean firstJoin = true;
            for (JoinVariableNode joinVariableNode : joinVariableNodes) {
                getTransformer().mixinJoinIntoTree(joinVariableNode, entityReference, firstJoin);
                firstJoin = false;
            }
        }
        for (int i = 1; i < strings.length; i++) {
            CommonTree selectionSource = parseSelectionSource(strings[i]);
            getTransformer().addSelectionSource(selectionSource);
        }
        addWhere(parseWhereCondition(where), entityReference, false);
    }

    @Override
    public void addJoinAsIs(String join) {
        String[] strings = join.split(",");
        join = strings[0];
        List<JoinVariableNode> joinVariableNodes = parseJoinCondition(join);
        IdentificationVariableNode identificationVariable = getAnalyzer().getMainIdentificationVariableNode();
        String entityName = getAnalyzer().getMainEntityName(identificationVariable);
        for (JoinVariableNode joinVariableNode : joinVariableNodes) {
            getTransformer().mixinJoinIntoTree(joinVariableNode, new EntityNameEntityReference(entityName), false);
        }
        for (int i = 1; i < strings.length; i++) {
            CommonTree selectionSource = parseSelectionSource(strings[i]);
            getTransformer().addSelectionSource(selectionSource);
        }
    }

    @Override
    public void addFirstSelectionSource(String selection) {
        try {
            getTransformer().addFirstSelectionSource(Parser.parseSelectionSource(selection));
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void replaceWithCount() {
        EntityReference entityReference = createMainSelectedPathNodeReference();
        getTransformer().replaceWithCount(entityReference.createNode());
    }

    @Override
    public void replaceWithSelectId() {
        getTransformer().replaceWithSelectId("id", getAnalyzer().getMainSelectedPathNode());
    }

    @Override
    public void replaceWithSelectId(String pkName) {
        getTransformer().replaceWithSelectId(pkName, getAnalyzer().getMainSelectedPathNode());
    }

    @Override
    public void replaceWithSelectEntityVariable(String selectEntityVariable) {
        getTransformer().replaceSelectedEntityVariable(selectEntityVariable, getAnalyzer().getMainSelectedPathNode());
    }

    @Override
    public boolean removeDistinct() {
        return getTransformer().removeDistinct();
    }

    @Override
    public void addDistinct() {
        getTransformer().addDistinct();
    }

    @Override
    public void replaceOrderBy(boolean desc, String... properties) {
        EntityReference entityReference = createMainSelectedPathNodeReference();
        PathEntityReference[] paths = Arrays.stream(properties)
                .map(entityReference::addFieldPath)
                .toArray(PathEntityReference[]::new);
        getTransformer().replaceOrderBy(desc, paths);
    }

    @Override
    public void addOrderByIdIfNotExists(String idProperty) {
        EntityReference entityReference = createMainSelectedPathNodeReference();
        PathEntityReference idReference = entityReference.addFieldPath(idProperty);
        getTransformer().addOrderByIdIfNotExists(idReference);
    }

    @Override
    public void addEntityInGroupBy(String entityAlias) {
        getTransformer().addEntityInGroupBy(entityAlias);
    }

    @Override
    public void removeOrderBy() {
        getTransformer().removeOrderBy();
    }

    @Override
    public void replaceEntityName(String newName) {
        getTransformer().replaceEntityName(newName, getAnalyzer().getMainIdentificationVariableNode());
    }

    @Override
    public void reset() {
        queryTree = null;
        queryTransformer = null;
        queryAnalyzer = null;
        addedParams.clear();
    }

    @Override
    public void replaceInCondition(String parameterName) {
        List<SimpleConditionNode> conditions = getAnalyzer().getConditions().stream()
                .filter(condition -> getAnalyzer().isConditionForParameter(condition, parameterName))
                .filter(condition -> getAnalyzer().isConditionIN(condition))
                .collect(Collectors.toList());
        getTransformer().clearInConditions(conditions);
    }

    @Override
    public boolean replaceIsNullStatements(String parameterName, boolean isNullValue) {
        List<SimpleConditionNode> conditions = getAnalyzer().getConditions().stream()
                .filter(condition -> getAnalyzer().isConditionForParameter(condition, parameterName))
                .filter(condition -> getAnalyzer().isConditionISNULL(condition)
                        || getAnalyzer().isConditionISNOTNULL(condition))
                .collect(Collectors.toList());
        if (conditions.isEmpty()) {
            return false;
        }
        getTransformer().replaceIsNullStatements(conditions, isNullValue);
        return true;
    }

    protected CommonTree parseWhereCondition(String whereCondition) {
        try {
            return Parser.parseWhereClause("where " + whereCondition);
        } catch (RecognitionException | JPA2RecognitionException e) {
            throw new JpqlSyntaxException(format("Errors found while parsing where condition:[%s] for query:[%s]\n%s",
                    StringUtils.strip(whereCondition), StringUtils.strip(query), e.getMessage()));
        }
    }

    protected List<JoinVariableNode> parseJoinCondition(String joinCondition) {
        try {
            return Parser.parseJoinClause(joinCondition);
        } catch (RecognitionException | JPA2RecognitionException e) {
            throw new JpqlSyntaxException(format("Errors found while parsing join condition:[%s] for query:[%s]\n%s",
                    StringUtils.strip(joinCondition), StringUtils.strip(query), e.getMessage()));
        }
    }

    protected CommonTree parseSelectionSource(String selectionSource) {
        try {
            return Parser.parseSelectionSource(selectionSource);
        } catch (RecognitionException | JPA2RecognitionException e) {
            throw new JpqlSyntaxException(format("Errors found while parsing selection source:[%s] for query:[%s]\n%s",
                    StringUtils.strip(selectionSource), StringUtils.strip(query), e.getMessage()));
        }
    }

    protected void addWhere(CommonTree whereTree, EntityReference ref, boolean replaceVariableName) {
        TreeVisitor visitor = new TreeVisitor();
        VariableManipulator variableManipulator = new VariableManipulator();
        visitor.visit(whereTree, variableManipulator);
        if (replaceVariableName) {
            Set<String> variables = variableManipulator.getUsedVariableNames();
            if (variables.size() > 1) {
                // we assume that adding where that use only one variable and does not add its own variables
                throw new IllegalStateException("Multiple variables used in condition");
            }
            String assumedEntityVariableInWhere = variableManipulator.getVariableNameInUse(0);
            variableManipulator.renameVariable(assumedEntityVariableInWhere, ref);
        }

        ParameterCounter parameterCounter = new ParameterCounter(true);
        visitor.visit(whereTree, parameterCounter);
        addedParams.addAll(parameterCounter.getParameterNames());

        getTransformer().mixinWhereConditionsIntoTree(whereTree);
    }

    protected EntityReference createMainIdentificationVariableReference() {
        IdentificationVariableNode identificationVariable = getAnalyzer().getMainIdentificationVariableNode();
        String entityName = getAnalyzer().getMainEntityName(identificationVariable);
        String variableName = getAnalyzer().getMainEntityVariable(identificationVariable);
        return new VariableEntityReference(entityName, variableName);
    }

    protected EntityReference createMainSelectedPathNodeReference() {
        PathNode pathNode = getAnalyzer().getMainSelectedPathNode();
        String entityName = getAnalyzer().getMainSelectedEntityName(pathNode);
        String variableName = getAnalyzer().getMainSelectedEntityVariable(pathNode);
        return new VariableEntityReference(entityName, variableName);
    }
}