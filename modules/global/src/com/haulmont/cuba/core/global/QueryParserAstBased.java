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
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.JpqlEntityModel;
import com.haulmont.cuba.core.sys.jpql.transform.NodesFinder;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.SimpleConditionNode;
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
    protected QueryTree queryTree;
    protected QueryTreeAnalyzer queryAnalyzer;

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

    protected QueryTreeAnalyzer getAnalyzer() {
        if (queryAnalyzer == null) {
            queryAnalyzer = new QueryTreeAnalyzer(getTree());
        }
        return queryAnalyzer;
    }

    @Override
    public Set<String> getParamNames() {
        return getAnalyzer().getParamNames();
    }

    @Override
    public Set<String> getAllEntityNames() {
        return getAnalyzer().getEntityNames();
    }

    @Override
    public String getEntityName() {
        IdentificationVariableNode identificationVariable = getAnalyzer().getMainIdentificationVariableNode();
        return getAnalyzer().getMainEntityName(identificationVariable);
    }

    @Override
    public String getEntityAlias(String entityType) {
        return getTree().getVariableNameByEntity(entityType);
    }

    @Override
    public String getEntityAlias() {
        IdentificationVariableNode identificationVariable = getAnalyzer().getMainIdentificationVariableNode();
        return getAnalyzer().getMainEntityVariable(identificationVariable);
    }

    @Override
    public boolean isEntitySelect(String targetEntity) {
        PathNode pathNode = getAnalyzer().getMainSelectedPathNode();
        IdentificationVariableNode identificationVariable = getAnalyzer().getMainIdentificationVariableNode();
        if (pathNode != null && identificationVariable != null) {
            return queryAnalyzer.isVariablePathNode(pathNode)
                    && StringUtils.equalsIgnoreCase(identificationVariable.getVariableName(), pathNode.getEntityVariableName())
                    && Objects.equals(identificationVariable.getEntityNameFromQuery(), targetEntity);
        }
        return false;
    }

    @Override
    public boolean hasIsNullCondition(String attribute) {
        IdentificationVariableNode identificationVariable = getAnalyzer().getMainIdentificationVariableNode();
        if (identificationVariable != null) {
            String variableName = identificationVariable.getVariableName();
            return queryTree.visit(NodesFinder.of(SimpleConditionNode.class)).getFoundNodes().stream()
                    .filter(condition -> getAnalyzer().isConditionForEntityProperty(condition, variableName, attribute))
                    .anyMatch(condition -> getAnalyzer().isConditionISNULL(condition));
        }
        return false;
    }

    @Override
    public boolean hasIsNotNullCondition(String attribute) {
        IdentificationVariableNode identificationVariable = getAnalyzer().getMainIdentificationVariableNode();
        if (identificationVariable != null) {
            String variableName = identificationVariable.getVariableName();
            return queryTree.visit(NodesFinder.of(SimpleConditionNode.class)).getFoundNodes().stream()
                    .filter(condition -> getAnalyzer().isConditionForEntityProperty(condition, variableName, attribute))
                    .anyMatch(condition -> getAnalyzer().isConditionISNOTNULL(condition));
        }
        return false;
    }

    @Override
    public boolean isQueryWithJoins() {
        return getAnalyzer().isQueryWithJoins();
    }

    @Override
    public String getOriginalEntityName() {
        EntityNameAndPath entityNameAndAlias = getOriginEntityNameAndPath();
        return entityNameAndAlias != null ? entityNameAndAlias.entityName : null;
    }

    @Override
    public String getOriginalEntityPath() {
        EntityNameAndPath entityNameAndAlias = getOriginEntityNameAndPath();
        return entityNameAndAlias != null ? entityNameAndAlias.entityPath : null;
    }

    @Override
    public boolean isParameterInCondition(String parameterName) {
        return getAnalyzer().getConditions().stream()
                .filter(condition -> getAnalyzer().isConditionForParameter(condition, parameterName))
                .anyMatch(condition -> getAnalyzer().isConditionIN(condition));
    }

    @Override
    public boolean isCollectionOriginalEntitySelect() {
        EntityNameAndPath entityNameAndAlias = getOriginEntityNameAndPath();
        return entityNameAndAlias != null && entityNameAndAlias.collectionSelect;
    }

    @Override
    public List<QueryPath> getQueryPaths() {
        List<QueryPath> queryPaths = new ArrayList<>();
        QueryVariableContext variableContext = getTree().getQueryVariableContext();
        PathNodeFinder finder = getTree().visit(new PathNodeFinder());
        for (PathNode node : finder.getSelectedPathNodes()) {
            JpqlEntityModel model = variableContext.getEntityByVariableNameHierarchically(node.getEntityVariableName());
            QueryPath queryPath = new QueryPath(model.getName(), node.getEntityVariableName(), node.asPathString(), true);
            queryPaths.add(queryPath);
        }
        for (PathNode node : finder.getOtherPathNodes()) {
            JpqlEntityModel model = variableContext.getEntityByVariableNameHierarchically(node.getEntityVariableName());
            QueryPath queryPath = new QueryPath(model.getName(), node.getEntityVariableName(), node.asPathString(), false);
            queryPaths.add(queryPath);
        }
        return queryPaths;
    }

    protected EntityNameAndPath getOriginEntityNameAndPath() {
        PathNode pathNode = getAnalyzer().getMainSelectedPathNode();
        IdentificationVariableNode identificationVariable = getAnalyzer().getMainIdentificationVariableNode();

        if (pathNode == null) {
            return null;
        }

        QueryVariableContext variableContext = getTree().getQueryVariableContext();
        if (queryAnalyzer.isVariablePathNode(pathNode)) {
            JpqlEntityModel entity = variableContext.getEntityByVariableName(pathNode.getEntityVariableName());
            if (entity != null && entity.getName() != null && identificationVariable != null) {
                if (!StringUtils.equalsIgnoreCase(pathNode.getEntityVariableName(), identificationVariable.getVariableName())) {
                    return new EntityNameAndPath(entity.getName(), pathNode.getEntityVariableName());
                }
            }
            return null;
        }

        JpqlEntityModel entity;
        String entityPath;
        boolean collectionSelect = false;
        try {
            entity = variableContext.getEntityByVariableName(pathNode.getEntityVariableName());
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
