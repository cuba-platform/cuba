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
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.jpql.*;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2RecognitionException;
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.JpqlEntityModel;
import com.haulmont.cuba.core.sys.jpql.transform.ParameterCounter;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.SimpleConditionNode;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(QueryParser.NAME)
public class QueryParserAstBased implements QueryParser {
    protected DomainModel model;
    protected String query;
    protected QueryTreeAnalyzer queryTreeAnalyzer;

    protected class EntityNameAndPath {

        String entityName;
        String entityPath;
        boolean collectionSelect;

        public EntityNameAndPath(String entityName, String entityPath, boolean collectionSelect) {
            this.entityName = entityName;
            this.entityPath = entityPath;
            this.collectionSelect = collectionSelect;
        }

        public EntityNameAndPath(String entityName, String entityPath) {
            this(entityName, entityPath, false);
        }
    }

    public QueryParserAstBased(DomainModel model, String query) {
        this.model = model;
        this.query = query;
    }

    private QueryTreeAnalyzer getQueryAnalyzer() {
        if (queryTreeAnalyzer == null) {
            queryTreeAnalyzer = new QueryTreeAnalyzer();
            try {
                queryTreeAnalyzer.prepare(model, query);
            } catch (RecognitionException e) {
                throw new RuntimeException("Internal error while init queryTreeAnalyzer", e);
            } catch (JPA2RecognitionException e) {
                throw new JpqlSyntaxException(format("Errors found for input jpql:[%s]\n%s", StringUtils.strip(query), e.getMessage()));
            }
            List<ErrorRec> errors = new ArrayList<>(queryTreeAnalyzer.getInvalidIdVarNodes());
            if (!errors.isEmpty()) {
                throw new JpqlSyntaxException(format("Errors found for input jpql:[%s]", StringUtils.strip(query)), errors);
            }
        }
        return queryTreeAnalyzer;
    }

    @Override
    public Set<String> getParamNames() {
        TreeVisitor visitor = new TreeVisitor();
        ParameterCounter parameterCounter = new ParameterCounter(true);
        visitor.visit(getQueryAnalyzer().getTree(), parameterCounter);
        return parameterCounter.getParameterNames();
    }

    @Override
    public Set<String> getAllEntityNames() {
        TreeVisitor visitor = new TreeVisitor();
        EntitiesFinder finder = new EntitiesFinder();
        visitor.visit(getQueryAnalyzer().getTree(), finder);
        return finder.resolveEntityNames(model, getQueryAnalyzer().getRootQueryVariableContext());
    }

    @Override
    public String getEntityName() {
        IdentificationVariableNode mainEntityIdentification = getQueryAnalyzer().getMainEntityIdentification();
        if (mainEntityIdentification != null) {
            return mainEntityIdentification.getEntityNameFromQuery();
        }
        throw new RuntimeException(format("Unable to find entity name [%s]", StringUtils.strip(query)));
    }

    @Override
    public String getEntityAlias(String targetEntity) {
        return getQueryAnalyzer().getRootEntityVariableName(targetEntity);
    }

    @Override
    public String getEntityAlias() {
        IdentificationVariableNode mainEntityIdentification = getQueryAnalyzer().getMainEntityIdentification();
        if (mainEntityIdentification != null) {
            return mainEntityIdentification.getVariableName();
        }
        throw new RuntimeException(format("Unable to find entity alias [%s]", StringUtils.strip(query)));
    }

    @Override
    public boolean isEntitySelect(String targetEntity) {
        List<PathNode> returnedPathNodes = getQueryAnalyzer().getReturnedPathNodes();
        if (CollectionUtils.isEmpty(returnedPathNodes) || returnedPathNodes.size() > 1) {
            return false;
        }

        PathNode pathNode = returnedPathNodes.get(0);
        String targetEntityAlias = getQueryAnalyzer().getRootEntityVariableName(targetEntity);
        return targetEntityAlias.equals(pathNode.getEntityVariableName())
                && CollectionUtils.isEmpty(pathNode.getChildren());
    }

    @Override
    public boolean hasIsNullCondition(String attribute) {
        List<SimpleConditionNode> allConditions = getQueryAnalyzer().findAllConditionsForMainEntityAttribute(attribute);
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
    public boolean hasJoins() {
        return getQueryAnalyzer().hasJoins();
    }

    @Override
    public String getEntityNameIfSecondaryReturnedInsteadOfMain() {
        EntityNameAndPath entityNameAndAlias = getEntityNameAndPathIfSecondaryReturnedInsteadOfMain();
        return entityNameAndAlias != null ? entityNameAndAlias.entityName : null;
    }

    @Override
    public String getEntityPathIfSecondaryReturnedInsteadOfMain() {
        EntityNameAndPath entityNameAndAlias = getEntityNameAndPathIfSecondaryReturnedInsteadOfMain();
        return entityNameAndAlias != null ? entityNameAndAlias.entityPath : null;
    }

    @Override
    public boolean isParameterInCondition(String parameterName) {
        List<SimpleConditionNode> conditions = getQueryAnalyzer().findConditionsForParameter(parameterName);
        for (SimpleConditionNode condition : conditions) {
            Tree inToken = condition.getFirstChildWithType(JPA2Lexer.IN);
            if (inToken != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCollectionSecondaryEntitySelect() {
        EntityNameAndPath entityNameAndAlias = getEntityNameAndPathIfSecondaryReturnedInsteadOfMain();
        return entityNameAndAlias != null && entityNameAndAlias.collectionSelect;
    }

    @Override
    public List<QueryPath> getQueryPaths() {
        List<QueryPath> queryPaths = new ArrayList<>();
        QueryVariableContext context = getQueryAnalyzer().getRootQueryVariableContext();
        TreeVisitor visitor = new TreeVisitor();
        PathNodeFinder finder = new PathNodeFinder();
        visitor.visit(getQueryAnalyzer().getTree(), finder);
        for (PathNode node : finder.getSelectedPathNodes()) {
            JpqlEntityModel model = context.getEntityByVariableNameHierarchically(node.getEntityVariableName());
            QueryPath queryPath = new QueryPath(model.getName(), node.getEntityVariableName(), node.asPathString(), true);
            queryPaths.add(queryPath);
        }
        for (PathNode node : finder.getOtherPathNodes()) {
            JpqlEntityModel model = context.getEntityByVariableNameHierarchically(node.getEntityVariableName());
            QueryPath queryPath = new QueryPath(model.getName(), node.getEntityVariableName(), node.asPathString(), false);
            queryPaths.add(queryPath);
        }
        return queryPaths;
    }

    protected EntityNameAndPath getEntityNameAndPathIfSecondaryReturnedInsteadOfMain() {
        List<PathNode> returnedPathNodes = getQueryAnalyzer().getReturnedPathNodes();
        if (CollectionUtils.isEmpty(returnedPathNodes) || returnedPathNodes.size() > 1) {
            return null;
        }

        QueryVariableContext rootQueryVariableContext = getQueryAnalyzer().getRootQueryVariableContext();
        PathNode pathNode = returnedPathNodes.get(0);
        if (pathNode.getChildren() == null) {
            JpqlEntityModel entity = rootQueryVariableContext.getEntityByVariableName(pathNode.getEntityVariableName());
            if (entity != null) {
                if (!Objects.equals(entity.getName(), getEntityName())) {
                    return new EntityNameAndPath(entity.getName(), pathNode.getEntityVariableName());
                }

                //fix for scary Eclipselink which consider "select p from sec$GroupHierarchy h join h.parent p"
                //(even if h.parent is also sec$GroupHierarchy)
                //as report query and does not allow to set view
                IdentificationVariableNode mainEntityIdentification = getQueryAnalyzer().getMainEntityIdentification();
                if (mainEntityIdentification != null
                        && !pathNode.getEntityVariableName().equals(mainEntityIdentification.getVariableName())) {
                    return entity.getName() != null ? new EntityNameAndPath(entity.getName(), pathNode.getEntityVariableName()) : null;
                }
            }
            return null;
        }

        JpqlEntityModel entity;
        String entityPath;
        boolean collectionSelect = false;
        try {
            entity = rootQueryVariableContext.getEntityByVariableName(pathNode.getEntityVariableName());
            if (entity != null) {
                entityPath = pathNode.asPathString();

                for (int i = 0; i < pathNode.getChildCount(); i++) {
                    String fieldName = pathNode.getChild(i).toString();
                    Attribute entityAttribute = entity.getAttributeByName(fieldName);
                    if (entityAttribute != null && entityAttribute.isEntityReferenceAttribute()) {
                        entity = model.getEntityByName(entityAttribute.getReferencedEntityName());
                        if (!collectionSelect) {
                            collectionSelect = entityAttribute.isCollection();
                        }
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } catch (UnknownEntityNameException e) {
            throw new RuntimeException(format("Unable to find entity by name %s", e.getEntityName()), e);
        }

        return entity != null && entity.getName() != null ? new EntityNameAndPath(entity.getName(), entityPath, collectionSelect) : null;
    }
}
