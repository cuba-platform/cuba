/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.sys.jpql.*;
import com.haulmont.cuba.core.sys.jpql.antlr.JPALexer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Chevelev
 * @version $Id$
 */
public class QueryTransformerAstBased implements QueryTransformer {
    private DomainModel model;
    private String query;
    private String entityName;
    private QueryTreeTransformer queryAnalyzer;
    private Set<String> addedParams = new HashSet<String>();

    public QueryTransformerAstBased(DomainModel model, String query, String entityName) throws RecognitionException {
        this.model = model;
        this.query = query;
        this.entityName = entityName;
        initQueryAnalyzer(model, query);
    }

    private void initQueryAnalyzer(DomainModel model, String query) throws RecognitionException {
        queryAnalyzer = new QueryTreeTransformer();
        queryAnalyzer.prepare(model, query);

        List<CommonErrorNode> errorNodes = new ArrayList<CommonErrorNode>(queryAnalyzer.getErrorNodes());
        List<ErrorRec> invalidIdVarNodes = queryAnalyzer.getInvalidIdVarNodes();
        for (ErrorRec invalidIdVarNode : invalidIdVarNodes) {
            if (errorNodes.contains(invalidIdVarNode.node)) {
                errorNodes.remove(invalidIdVarNode.node);
            }
        }

        List<ErrorRec> errors = new ArrayList<ErrorRec>(queryAnalyzer.getInvalidIdVarNodes());
        for (CommonErrorNode errorNode : errorNodes) {
            ErrorRec rec = new ErrorRec(errorNode, "CommonErrorNode");
            errors.add(rec);
        }

        if (!errors.isEmpty()) {
            throw new ErrorsFoundException("Errors found", errors);
        }
    }

    public String getResult() {
        CommonTree tree = queryAnalyzer.getTree();
        TreeVisitor visitor = new TreeVisitor();

        TreeToQuery treeToQuery = new TreeToQuery();
        visitor.visit(tree, treeToQuery);

        return treeToQuery.getQueryString().trim();
    }

    public Set<String> getAddedParams() {
        return addedParams;
    }

    @Override
    public void handleCaseInsensitiveParam(String paramName) {

    }

    /**
     * @param where - "{E}" may be used as a replaceable entity placeholder. No such value
     *              should be used as a string constant
     */
    public void addWhere(String where) {
        EntityReferenceInferer inferrer = new EntityReferenceInferer(entityName);
        EntityReference ref = inferrer.infer(queryAnalyzer);
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
    public void mergeWhere(String statement) {
        EntityReferenceInferer inferer = new EntityReferenceInferer(entityName);
        EntityReference ref = inferer.infer(queryAnalyzer);
        try {
            CommonTree statementTree = Parser.parse(statement);
            CommonTree whereClause = (CommonTree) statementTree.getFirstChildWithType(JPALexer.T_CONDITION);
            addWhere(whereClause, ref, true);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public void addJoinAndWhere(String join, String where) {
        EntityReferenceInferer inferer = new EntityReferenceInferer(entityName);
        EntityReference ref = inferer.infer(queryAnalyzer);
        if (where.contains("{E}")) {
            where = ref.replaceEntries(where, "\\{E\\}");
        }
        if (join.contains("{E}")) {
            join = ref.replaceEntries(join, "\\{E\\}");
        }
        String[] strings = join.split(",");
        join = strings[0];
        try {
            CommonTree joinClause = Parser.parseJoinClause(join);
            queryAnalyzer.mixinJoinIntoTree(joinClause, ref, true);
            for (int i = 1; i < strings.length; i++) {
                CommonTree selectionSource = Parser.parseSelectionSource(strings[i]);
                queryAnalyzer.addSelectionSource(selectionSource);
            }
            CommonTree whereTree = Parser.parseWhereClause("where " + where);
            addWhere(whereTree, ref, false);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public void addJoinAsIs(String join) {
        String[] strings = join.split(",");
        join = strings[0];
        try {
            CommonTree joinClause = Parser.parseJoinClause(join);
            queryAnalyzer.mixinJoinIntoTree(joinClause, new EntityNameEntityReference(entityName), false);
            for (int i = 1; i < strings.length; i++) {
                CommonTree selectionSource = Parser.parseSelectionSource(strings[i]);
                queryAnalyzer.addSelectionSource(selectionSource);
            }
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public void replaceWithCount() {
        EntityReferenceInferer inferer = new EntityReferenceInferer(entityName);
        EntityReference ref = inferer.infer(queryAnalyzer);
        queryAnalyzer.replaceWithCount(ref);
    }

    @Override
    public void replaceWithSelectId() {
    }

    @Override
    public boolean removeDistinct() {
        // TODO
        return false;
    }

    @Deprecated
    @Override
    public void replaceOrderBy(String newOrderingFieldPath, boolean desc) {
        replaceOrderBy(desc, newOrderingFieldPath);
    }

    @Override
    public void replaceOrderBy(boolean desc, String... properties) {
        EntityReferenceInferer inferer = new EntityReferenceInferer(entityName);
        EntityReference ref = inferer.infer(queryAnalyzer);
        queryAnalyzer.replaceOrderBy(ref.addFieldPath(properties[0]), desc);
    }

    @Override
    public void removeOrderBy() {
    }

    @Override
    public void replaceEntityName(String newName) {
        // TODO
        throw new UnsupportedOperationException();
    }

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

        queryAnalyzer.mixinWhereConditionsIntoTree(whereTree);
    }

}
