/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.PerformanceLog;
import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.ErrorRec;
import com.haulmont.cuba.core.sys.jpql.QueryErrorsFoundException;
import com.haulmont.cuba.core.sys.jpql.QueryTreeAnalyzer;
import com.haulmont.cuba.core.sys.jpql.transform.ParameterCounter;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.SimpleConditionNode;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.TreeVisitor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(QueryParser.NAME)
@PerformanceLog
public class QueryParserAstBased implements QueryParser {
    private String query;
    private QueryTreeAnalyzer queryTreeAnalyzer;

    public QueryParserAstBased(DomainModel model, String query) throws RecognitionException {
        this.query = query;
        initQueryAnalyzer(model, query);
    }

    private void initQueryAnalyzer(DomainModel model, String query) throws RecognitionException {
        queryTreeAnalyzer = new QueryTreeAnalyzer();
        queryTreeAnalyzer.prepare(model, query);
        List<ErrorRec> errors = new ArrayList<>(queryTreeAnalyzer.getInvalidIdVarNodes());
        if (!errors.isEmpty()) {
            throw new QueryErrorsFoundException("Errors found", errors);
        }
    }

    @Override
    public Set<String> getParamNames() {
        TreeVisitor visitor = new TreeVisitor();
        ParameterCounter parameterCounter = new ParameterCounter(true);
        visitor.visit(queryTreeAnalyzer.getTree(), parameterCounter);
        return parameterCounter.getParameterNames();
    }

    @Override
    public String getEntityName() {
        IdentificationVariableNode mainEntityIdentification = queryTreeAnalyzer.getMainEntityIdentification();
        if (mainEntityIdentification != null) {
            return mainEntityIdentification.getEntityName();
        }
        throw new RuntimeException("Unable to find entity name [" + query + "]");
    }

    @Override
    public String getEntityAlias(String targetEntity) {
        return queryTreeAnalyzer.getRootEntityVariableName(targetEntity);
    }

    @Override
    public String getEntityAlias() {
        IdentificationVariableNode mainEntityIdentification = queryTreeAnalyzer.getMainEntityIdentification();
        if (mainEntityIdentification != null) {
            return mainEntityIdentification.getVariableName();
        }
        throw new RuntimeException("Unable to find entity alias [" + query + "]");
    }

    @Override
    public boolean isEntitySelect(String targetEntity) {
        List<PathNode> returnedPathNodes = queryTreeAnalyzer.getReturnedPathNodes();
        if (CollectionUtils.isEmpty(returnedPathNodes) || returnedPathNodes.size() > 1) {
            return false;
        }

        PathNode pathNode = returnedPathNodes.get(0);
        String targetEntityAlias = queryTreeAnalyzer.getRootEntityVariableName(targetEntity);
        return targetEntityAlias.equals(pathNode.getEntityVariableName())
                && CollectionUtils.isEmpty(pathNode.getChildren());
    }

    @Override
    public boolean hasIsNullCondition(String attribute) {
        List<SimpleConditionNode> allConditions = queryTreeAnalyzer.findAllConditionsForMainEntityAttribute(attribute);
        for (SimpleConditionNode allCondition : allConditions) {
            List<?> children = allCondition.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Object child = children.get(i);
                if (i < children.size() - 1
                        && child.toString().equalsIgnoreCase("is")
                        && children.get(i + 1).toString().equalsIgnoreCase("null")) {
                    return true;
                }


            }
        }
        return false;
    }
}
