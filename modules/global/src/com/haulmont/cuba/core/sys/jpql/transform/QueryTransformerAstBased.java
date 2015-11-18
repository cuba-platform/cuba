/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.sys.PerformanceLog;
import com.haulmont.cuba.core.sys.jpql.*;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Chevelev
 * @version $Id$
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(QueryTransformer.NAME)
@PerformanceLog
public class QueryTransformerAstBased implements QueryTransformer {
    private DomainModel model;
    private String query;
    private QueryTreeTransformer queryTreeTransformer;
    private Set<String> addedParams = new HashSet<>();
    private String returnedEntityName;
    private String mainEntityName;

    public QueryTransformerAstBased(DomainModel model, String query) throws RecognitionException {
        this.model = model;
        this.query = query;
        initQueryAnalyzer(model, query);

        String returnedVariableName = queryTreeTransformer.getReturnedVariableName();
        Entity entity = queryTreeTransformer.getRootQueryVariableContext().getEntityByVariableName(returnedVariableName);
        if (entity != null) {
            returnedEntityName = entity.getName();
        }

        IdentificationVariableNode mainEntityIdentification = queryTreeTransformer.getMainEntityIdentification();
        if (mainEntityIdentification != null) {
            try {
                Entity entityByName = model.getEntityByName(mainEntityIdentification.getEntityName());
                mainEntityName = entityByName.getName();
            } catch (UnknownEntityNameException e) {
                throw new RuntimeException("Could not resolve entity for name " + mainEntityIdentification.getEntityName());
            }
        }
    }

    private void initQueryAnalyzer(DomainModel model, String query) throws RecognitionException {
        queryTreeTransformer = new QueryTreeTransformer();
        queryTreeTransformer.prepare(model, query);

        List<CommonErrorNode> errorNodes = new ArrayList<>(queryTreeTransformer.getErrorNodes());
        List<ErrorRec> invalidIdVarNodes = queryTreeTransformer.getInvalidIdVarNodes();
        for (ErrorRec invalidIdVarNode : invalidIdVarNodes) {
            if (errorNodes.contains(invalidIdVarNode.node)) {
                errorNodes.remove(invalidIdVarNode.node);
            }
        }

        List<ErrorRec> errors = new ArrayList<>(queryTreeTransformer.getInvalidIdVarNodes());
        for (CommonErrorNode errorNode : errorNodes) {
            ErrorRec rec = new ErrorRec(errorNode, "CommonErrorNode");
            errors.add(rec);
        }

        if (!errors.isEmpty()) {
            throw new QueryErrorsFoundException("Errors found", errors);
        }
    }

    @Override
    public String getResult() {
        CommonTree tree = queryTreeTransformer.getTree();
        TreeVisitor visitor = new TreeVisitor();

        TreeToQuery treeToQuery = new TreeToQuery();
        visitor.visit(tree, treeToQuery);

        return treeToQuery.getQueryString().trim();
    }

    @Override
    public Set<String> getAddedParams() {
        return addedParams;
    }

    @Override
    public void handleCaseInsensitiveParam(String paramName) {
        queryTreeTransformer.handleCaseInsensitiveParam(paramName);
    }

    /**
     * @param where - "{E}" may be used as a replaceable entity placeholder. No such value
     *              should be used as a string constant
     */
    @Override
    public void addWhere(String where) {
        EntityReferenceInferer inferrer = new EntityReferenceInferer(mainEntityName);
        EntityReference ref = inferrer.infer(queryTreeTransformer);
        boolean doReplaceVariableName = true;
        if (where.contains("{E}")) {
            doReplaceVariableName = false;
            where = ref.replaceEntries(where, "\\{E\\}");
        }
        try {
            CommonTree whereTree = Parser.parseWhereClause("where " + where);
            addWhere(whereTree, ref, doReplaceVariableName);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addWhereAsIs(String where) {
        try {
            CommonTree whereTree = Parser.parseWhereClause("where " + where);
            addWhere(whereTree, null, false);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * в реальности это копирование из другого запроса
     *
     * @param statement выражение, из которого скопировать where часть
     */
    @Override
    public void mergeWhere(String statement) {
        EntityReferenceInferer inferer = new EntityReferenceInferer(mainEntityName);
        EntityReference ref = inferer.infer(queryTreeTransformer);
        try {
            CommonTree statementTree = Parser.parse(statement);
            CommonTree whereClause = (CommonTree) statementTree.getFirstChildWithType(JPA2Lexer.T_CONDITION);
            addWhere(whereClause, ref, true);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addJoinAndWhere(String join, String where) {
        EntityReferenceInferer inferer = new EntityReferenceInferer(mainEntityName);
        EntityReference ref = inferer.infer(queryTreeTransformer);
        if (where.contains("{E}")) {
            where = ref.replaceEntries(where, "\\{E\\}");
        }
        if (join.contains("{E}")) {
            join = ref.replaceEntries(join, "\\{E\\}");
        }
        String[] strings = join.split(",");
        join = strings[0];
        try {
            if (StringUtils.isNotBlank(join)) {
                CommonTree joinClause = Parser.parseJoinClause(join);
                queryTreeTransformer.mixinJoinIntoTree(joinClause, ref, true);
            }
            for (int i = 1; i < strings.length; i++) {
                CommonTree selectionSource = Parser.parseSelectionSource(strings[i]);
                queryTreeTransformer.addSelectionSource(selectionSource);
            }
            CommonTree whereTree = Parser.parseWhereClause("where " + where);
            addWhere(whereTree, ref, false);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addJoinAsIs(String join) {
        String[] strings = join.split(",");
        join = strings[0];
        try {
            CommonTree joinClause = Parser.parseJoinClause(join);
            queryTreeTransformer.mixinJoinIntoTree(joinClause, new EntityNameEntityReference(mainEntityName), false);
            for (int i = 1; i < strings.length; i++) {
                CommonTree selectionSource = Parser.parseSelectionSource(strings[i]);
                queryTreeTransformer.addSelectionSource(selectionSource);
            }
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void replaceWithCount() {
        EntityReferenceInferer inferer = new EntityReferenceInferer(returnedEntityName);
        EntityReference ref = inferer.infer(queryTreeTransformer);
        queryTreeTransformer.replaceWithCount(ref);
    }

    @Override
    public void replaceWithSelectId() {
        queryTreeTransformer.replaceWithSelectId();
    }

    @Override
    public boolean removeDistinct() {
        return queryTreeTransformer.removeDistinct();
    }

    @Override
    public void replaceOrderBy(boolean desc, String... properties) {
        EntityReferenceInferer inferer = new EntityReferenceInferer(returnedEntityName);
        EntityReference ref = inferer.infer(queryTreeTransformer);
        queryTreeTransformer.replaceOrderBy(ref.addFieldPath(properties[0]), desc);
    }

    @Override
    public void removeOrderBy() {
        queryTreeTransformer.removeOrderBy();
    }

    @Override
    public void replaceEntityName(String newName) {
        queryTreeTransformer.replaceEntityName(newName);
    }

    @Override
    public void reset() {
        try {
            initQueryAnalyzer(model, query);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
        addedParams.clear();
    }

    private void addWhere(CommonTree whereTree, EntityReference ref, boolean replaceVariableName) {
        TreeVisitor visitor = new TreeVisitor();
        VariableManipulator variableManip = new VariableManipulator();
        visitor.visit(whereTree, variableManip);
        if (replaceVariableName) {
            Set<String> variables = variableManip.getUsedVariableNames();
            if (variables.size() > 1) {
                // предположение, что добавляемый where  использует только одну переменную и не определяет собственных
                throw new IllegalStateException("Multiple variables used in condition");
            }
            String assumedEntityVariableInWhere = variableManip.getVariableNameInUse(0);
            variableManip.renameVariable(assumedEntityVariableInWhere, ref);
        }

        ParameterCounter parameterCounter = new ParameterCounter(true);
        visitor.visit(whereTree, parameterCounter);
        addedParams.addAll(parameterCounter.getParameterNames());

        queryTreeTransformer.mixinWhereConditionsIntoTree(whereTree);
    }
}