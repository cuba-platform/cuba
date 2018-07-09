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

package com.haulmont.cuba.core.sys;

import com.google.common.collect.Multimap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.app.AttributeSecuritySupport;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.jpql.JpqlSyntaxException;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.global.ConstraintData;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiPredicate;

import static java.lang.String.format;

@PerformanceLog
public class PersistenceSecurityImpl extends SecurityImpl implements PersistenceSecurity {
    private final Logger log = LoggerFactory.getLogger(PersistenceSecurityImpl.class);

    @Inject
    protected SecurityTokenManager securityTokenManager;

    @Inject
    protected Configuration configuration;

    @Inject
    protected Persistence persistence;

    @Inject
    protected ReferenceToEntitySupport referenceToEntitySupport;

    @Inject
    protected AttributeSecuritySupport attributeSecuritySupport;

    @Inject
    protected EntityStates entityStates;

    @Inject
    protected GlobalConfig globalConfig;

    @Override
    public boolean applyConstraints(Query query) {
        QueryParser parser = QueryTransformerFactory.createParser(query.getQueryString());
        String entityName = parser.getEntityName();

        List<ConstraintData> constraints = getConstraints(metadata.getClassNN(entityName), constraint ->
                constraint.getCheckType().database()
                        && (constraint.getOperationType() == ConstraintOperationType.READ
                        || constraint.getOperationType() == ConstraintOperationType.ALL));

        if (constraints.isEmpty())
            return false;

        QueryTransformer transformer = QueryTransformerFactory.createTransformer(query.getQueryString());

        for (ConstraintData constraint : constraints) {
            processConstraint(transformer, constraint, entityName);
        }
        query.setQueryString(transformer.getResult());

        for (String paramName : transformer.getAddedParams()) {
            setQueryParam(query, paramName);
        }
        return true;
    }

    @Override
    public void setQueryParam(Query query, String paramName) {
        if (paramName.startsWith(CONSTRAINT_PARAM_SESSION_ATTR)) {
            UserSession userSession = userSessionSource.getUserSession();

            String attrName = paramName.substring(CONSTRAINT_PARAM_SESSION_ATTR.length());

            if (CONSTRAINT_PARAM_USER_LOGIN.equals(attrName)) {
                String userLogin = userSession.getSubstitutedUser() != null ?
                        userSession.getSubstitutedUser().getLogin() :
                        userSession.getUser().getLogin();
                query.setParameter(paramName, userLogin);

            } else if (CONSTRAINT_PARAM_USER_ID.equals(attrName)) {
                UUID userId = userSession.getSubstitutedUser() != null ?
                        userSession.getSubstitutedUser().getId() :
                        userSession.getUser().getId();
                query.setParameter(paramName, userId);

            } else if (CONSTRAINT_PARAM_USER_GROUP_ID.equals(attrName)) {
                Object groupId = userSession.getSubstitutedUser() == null ?
                        userSession.getUser().getGroup().getId() :
                        userSession.getSubstitutedUser().getGroup().getId();
                query.setParameter(paramName, groupId);

            } else {
                Serializable value = userSession.getAttribute(attrName);
                query.setParameter(paramName, value);
            }
        }
    }

    @Override
    public boolean filterByConstraints(Collection<Entity> entities) {
        boolean filtered = false;
        for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext(); ) {
            Entity entity = iterator.next();
            if (!isPermittedInMemory(entity)) {
                //we ignore situations when the collection is immutable
                iterator.remove();
                filtered = true;
            }
        }
        return filtered;
    }

    @Override
    public boolean filterByConstraints(Entity entity) {
        return !isPermittedInMemory(entity);
    }

    @Override
    public void applyConstraints(Collection<Entity> entities) {
        Set<EntityId> handled = new LinkedHashSet<>();
        entities.forEach(entity -> applyConstraints(entity, handled));
    }

    @Override
    public void applyConstraints(Entity entity) {
        applyConstraints(entity, new HashSet<>());
    }

    @Override
    public void calculateFilteredData(Entity entity) {
        calculateFilteredData(entity, new HashSet<>(), false);
    }

    @Override
    public void calculateFilteredData(Collection<Entity> entities) {
        Set<EntityId> handled = new LinkedHashSet<>();
        entities.forEach(entity -> calculateFilteredData(entity, handled, false));
    }

    @Override
    public void restoreSecurityState(Entity entity) {
        try {
            securityTokenManager.readSecurityToken(entity);
        } catch (SecurityTokenException e) {
            throw new RowLevelSecurityException(
                    format("Could not restore security state for entity [%s] because security token isn't valid.",
                            entity), entity.getMetaClass().getName());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void restoreFilteredData(Entity entity) {
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        String storeName = metadataTools.getStoreName(metaClass);
        EntityManager entityManager = persistence.getEntityManager(storeName);

        Multimap<String, Object> filtered = BaseEntityInternalAccess.getFilteredData(entity);
        if (filtered == null) {
            return;
        }

        for (Map.Entry<String, Collection<Object>> entry : filtered.asMap().entrySet()) {
            MetaProperty property = metaClass.getPropertyNN(entry.getKey());
            Collection filteredIds = entry.getValue();

            if (property.getRange().isClass() && CollectionUtils.isNotEmpty(filteredIds)) {
                Class entityClass = property.getRange().asClass().getJavaClass();
                Class propertyClass = property.getJavaType();
                if (Collection.class.isAssignableFrom(propertyClass)) {
                    Collection currentCollection = entity.getValue(property.getName());
                    if (currentCollection == null) {
                        throw new RowLevelSecurityException(
                                format("Could not restore an object to currentValue because it is null [%s]. Entity [%s].",
                                        property.getName(), metaClass.getName()), metaClass.getName());
                    }

                    for (Object entityId : filteredIds) {
                        Entity reference = entityManager.getReference((Class<Entity>) entityClass, entityId);
                        //we ignore situations when the currentValue is immutable
                        currentCollection.add(reference);
                    }
                } else if (Entity.class.isAssignableFrom(propertyClass)) {
                    Object entityId = filteredIds.iterator().next();
                    Entity reference = entityManager.getReference((Class<Entity>) entityClass, entityId);
                    //we ignore the situation when the field is read-only
                    entity.setValue(property.getName(), reference);
                }
            }
        }
    }

    @Override
    public void assertToken(Entity entity) {
        if (BaseEntityInternalAccess.getSecurityToken(entity) == null) {
            assertSecurityConstraints(entity, (e, metaProperty) -> entityStates.isDetached(entity)
                    && !entityStates.isLoaded(entity, metaProperty.getName()));
            assertTokenForAttributeAccess(entity);
        }
    }

    @Override
    public void assertTokenForREST(Entity entity, View view) {
        if (BaseEntityInternalAccess.getSecurityToken(entity) == null) {
            assertSecurityConstraints(entity,
                    (e, metaProperty) -> view != null && !view.containsProperty(metaProperty.getName()));
            assertTokenForAttributeAccess(entity);
        }
    }

    protected void assertSecurityConstraints(Entity entity, BiPredicate<Entity, MetaProperty> predicate) {
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (metaProperty.getRange().isClass() && metadataTools.isPersistent(metaProperty)) {
                if (predicate.test(entity, metaProperty)) {
                    continue;
                }
                if (hasInMemoryConstraints(metaProperty.getRange().asClass(), ConstraintOperationType.READ,
                        ConstraintOperationType.ALL)) {
                    throw new RowLevelSecurityException(format("Could not read security token from entity %s, " +
                            "even though there are active READ/ALL constraints for the property: %s", entity,
                            metaProperty.getName()),
                            entity.getMetaClass().getName());
                }
            }
        }
    }

    protected void assertTokenForAttributeAccess(Entity entity) {
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        if (attributeSecuritySupport.isAttributeAccessEnabled(metaClass)) {
            throw new RowLevelSecurityException(format("Could not read security token from entity %s, " +
                    "even though there are active attribute access for the entity.", entity),
                    entity.getMetaClass().getName());
        }
    }

    protected void processConstraint(QueryTransformer transformer, ConstraintData constraint, String entityName) {
        String join = constraint.getJoin();
        String where = constraint.getWhereClause();
        try {
            if (StringUtils.isBlank(join)) {
                if (!StringUtils.isBlank(where)) {
                    transformer.addWhere(where);
                }
            } else {
                transformer.addJoinAndWhere(join, where);
            }
        } catch (JpqlSyntaxException e) {
            log.error("Syntax errors found in constraint's JPQL expressions. Entity [{}]. Constraint ID [{}].",
                    entityName, constraint.getId(), e);

            throw new RowLevelSecurityException(
                    "Syntax errors found in constraint's JPQL expressions. Please see the logs.", entityName);
        } catch (Exception e) {
            log.error("An error occurred when applying security constraint. Entity [{}]. Constraint ID [{}].",
                    entityName, constraint.getId(), e);

            throw new RowLevelSecurityException(
                    "An error occurred when applying security constraint. Please see the logs.", entityName);
        }
    }

    @SuppressWarnings("unchecked")
    protected void applyConstraints(Entity entity, Set<EntityId> handled) {
        MetaClass metaClass = entity.getMetaClass();
        EntityId entityId = new EntityId(referenceToEntitySupport.getReferenceId(entity), metaClass.getName());
        if (handled.contains(entityId)) {
            return;
        }
        handled.add(entityId);
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;
            Multimap<String, Object> filteredData = BaseEntityInternalAccess.getFilteredData(baseGenericIdEntity);
            for (MetaProperty property : metaClass.getProperties()) {
                if (metadataTools.isPersistent(property) && PersistenceHelper.isLoaded(entity, property.getName())) {
                    Object value = entity.getValue(property.getName());
                    if (value instanceof Collection) {
                        Collection entities = (Collection) value;
                        for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext(); ) {
                            Entity item = iterator.next();
                            if (filteredData != null && filteredData.containsEntry(property.getName(),
                                    referenceToEntitySupport.getReferenceId(item))) {
                                iterator.remove();
                            } else {
                                applyConstraints(item, handled);
                            }
                        }
                    } else if (value instanceof Entity) {
                        if (filteredData != null && filteredData.containsEntry(property.getName(),
                                referenceToEntitySupport.getReferenceId((Entity) value))) {
                            baseGenericIdEntity.setValue(property.getName(), null);
                        } else {
                            applyConstraints((Entity) value, handled);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected boolean calculateFilteredData(Entity entity, Set<EntityId> handled, boolean checkPermitted) {
        if (referenceToEntitySupport.getReferenceId(entity) == null) {
            return false;
        }
        MetaClass metaClass = entity.getMetaClass();
        if (!isPermittedInMemory(entity) && checkPermitted) {
            return true;
        }
        EntityId entityId = new EntityId(referenceToEntitySupport.getReferenceId(entity), metaClass.getName());
        if (handled.contains(entityId)) {
            return false;
        }
        handled.add(entityId);
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;
            for (MetaProperty property : metaClass.getProperties()) {
                if (metadataTools.isPersistent(property) && PersistenceHelper.isLoaded(entity, property.getName())) {
                    Object value = entity.getValue(property.getName());
                    if (value instanceof Collection) {
                        Set filtered = new LinkedHashSet();
                        for (Entity item : (Collection<Entity>) value) {
                            if (calculateFilteredData(item, handled, true)) {
                                filtered.add(referenceToEntitySupport.getReferenceId(item));
                            }
                        }
                        if (!filtered.isEmpty()) {
                            securityTokenManager.addFiltered(baseGenericIdEntity, property.getName(), filtered);
                        }
                    } else if (value instanceof Entity) {
                        Entity valueEntity = (Entity) value;
                        if (calculateFilteredData(valueEntity, handled, true)) {
                            securityTokenManager.addFiltered(baseGenericIdEntity, property.getName(),
                                    referenceToEntitySupport.getReferenceId(valueEntity));
                        }
                    }
                }
            }
            securityTokenManager.writeSecurityToken(baseGenericIdEntity);
        }
        return false;
    }

    protected boolean isPermittedInMemory(Entity entity) {
        return isPermitted(entity, constraint ->
                constraint.getCheckType().memory()
                        && (constraint.getOperationType() == ConstraintOperationType.READ
                        || constraint.getOperationType() == ConstraintOperationType.ALL));
    }

    protected static class EntityId {
        Object id;
        String metaClassName;

        public EntityId(Object id, String metaClassName) {
            this.id = id;
            this.metaClassName = metaClassName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EntityId entityId = (EntityId) o;

            if (!id.equals(entityId.id)) return false;
            return metaClassName.equals(entityId.metaClassName);
        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + metaClassName.hashCode();
            return result;
        }
    }
}