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

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.PerformanceLog;
import com.haulmont.cuba.core.sys.jpql.*;
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
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
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(QueryParser.NAME)
@PerformanceLog
public class QueryParserAstBased implements QueryParser {
    protected DomainModel model;
    protected String query;
    protected QueryTreeAnalyzer queryTreeAnalyzer;

    public QueryParserAstBased(DomainModel model, String query) throws RecognitionException {
        this.model = model;
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
            return mainEntityIdentification.getEffectiveEntityName();
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

    @Override
    public String getEntityNameIfSecondaryReturnedInsteadOfMain() {
        List<PathNode> returnedPathNodes = queryTreeAnalyzer.getReturnedPathNodes();
        if (CollectionUtils.isEmpty(returnedPathNodes) || returnedPathNodes.size() > 1) {
            return null;
        }

        QueryVariableContext rootQueryVariableContext = queryTreeAnalyzer.getRootQueryVariableContext();
        PathNode pathNode = returnedPathNodes.get(0);
        if (pathNode.getChildren() == null) {
            Entity entity = rootQueryVariableContext.getEntityByVariableName(pathNode.getEntityVariableName());
            if (!entity.getName().equals(getEntityName())) {
                return entity.getName();
            }

            //fix for scary Eclipselink which consider "select p from sec$GroupHierarchy h join h.parent p"
            //(even if h.parent is also sec$GroupHierarchy)
            //as report query and does not allow to set view
            IdentificationVariableNode mainEntityIdentification = queryTreeAnalyzer.getMainEntityIdentification();
            if (mainEntityIdentification != null
                    && !pathNode.getEntityVariableName().equals(mainEntityIdentification.getVariableName())) {
                return entity.getName();
            }

            return null;
        }

        String entityName = getEntityName();
        Entity entity;
        try {
            entity = model.getEntityByName(entityName);

            for (int i = 0; i < pathNode.getChildCount(); i++) {
                String fieldName = pathNode.getChild(i).toString();
                Attribute entityAttribute = entity.getAttributeByName(fieldName);
                if (entityAttribute != null && entityAttribute.isEntityReferenceAttribute()) {
                    entityName = entityAttribute.getReferencedEntityName();
                    entity = model.getEntityByName(entityName);
                } else {
                    return null;
                }
            }
        } catch (UnknownEntityNameException e) {
            throw new RuntimeException("Could not find entity by name " + entityName, e);
        }

        return entity != null ? entity.getName() : null;
    }
}
