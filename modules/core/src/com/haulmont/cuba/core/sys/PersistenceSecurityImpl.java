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
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.app.AttributeSecuritySupport;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.jpql.JpqlSyntaxException;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.group.ConstraintValidationResult;
import com.haulmont.cuba.security.group.JpqlAccessConstraint;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.MethodClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    @Override
    public boolean applyConstraints(Query query) {
        QueryParser parser = QueryTransformerFactory.createParser(query.getQueryString());
        String entityName = parser.getEntityName();

        List<JpqlAccessConstraint> constraints = getConstraints(metadata.getClassNN(entityName))
                .filter(c -> c instanceof JpqlAccessConstraint && c.getOperation() == EntityOp.READ)
                .map(JpqlAccessConstraint.class::cast)
                .collect(Collectors.toList());

        if (constraints.isEmpty())
            return false;

        QueryTransformer transformer = QueryTransformerFactory.createTransformer(query.getQueryString());

        for (JpqlAccessConstraint constraint : constraints) {
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
            if (isNotPermittedInMemory(entity)) {
                //we ignore situations when the collection is immutable
                iterator.remove();
                filtered = true;
            }
        }
        return filtered;
    }

    @Override
    public boolean filterByConstraints(Entity entity) {
        return isNotPermittedInMemory(entity);
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

    @Override
    public boolean hasInMemoryReadConstraints(MetaClass metaClass) {
        return getConstraints(metaClass)
                .anyMatch(c -> c.isInMemory() && EntityOp.READ == c.getOperation());
    }

    protected void assertSecurityConstraints(Entity entity, BiPredicate<Entity, MetaProperty> predicate) {
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (metaProperty.getRange().isClass() && metadataTools.isPersistent(metaProperty)) {
                if (predicate.test(entity, metaProperty)) {
                    continue;
                }
                if (hasInMemoryReadConstraints(metaProperty.getRange().asClass())) {
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

    protected void processConstraint(QueryTransformer transformer, JpqlAccessConstraint constraint, String entityName) {
        String join = constraint.getJoin();
        String where = constraint.getWhere();
        try {
            if (StringUtils.isBlank(join)) {
                if (!StringUtils.isBlank(where)) {
                    transformer.addWhere(where);
                }
            } else {
                transformer.addJoinAndWhere(join, where);
            }
        } catch (JpqlSyntaxException e) {
            log.error("Syntax errors found in constraint's JPQL expressions. Entity [{}]. Constraint [where = {}, join = {}].",
                    entityName, constraint.getWhere(), constraint.getJoin(), e);

            throw new RowLevelSecurityException(
                    "Syntax errors found in constraint's JPQL expressions. Please see the logs.", entityName);
        } catch (Exception e) {
            log.error("An error occurred when applying security constraint. Entity [{}]. Constraint [where = {}, join = {}].",
                    entityName, constraint.getWhere(), constraint.getJoin(), e);

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
        if (isNotPermittedInMemory(entity) && checkPermitted) {
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


    @Override
    public boolean isPermitted(Entity entity, EntityOp operation) {
        return getConstraints(entity.getMetaClass())
                .filter(c -> c.isInMemory() && c.getOperation() == operation)
                .allMatch(c -> ((Predicate<Entity>) c.getPredicate()).test(entity));
    }

    @Override
    public boolean isPermitted(Entity entity, ConstraintOperationType operationType) {
        for (EntityOp entityOp : operationType.toEntityOps()) {
            if (!isPermitted(entity, entityOp)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isPermitted(Entity entity, String customCode) {
        //noinspection unchecked
        return getConstraints(entity.getMetaClass())
                .filter(c -> c.isInMemory() && Objects.equals(c.getCode(), customCode))
                .allMatch(c -> ((Predicate<Entity>) c.getPredicate()).test(entity));
    }

    protected boolean isNotPermittedInMemory(Entity entity) {
        //noinspection unchecked
        return !getConstraints(entity.getMetaClass())
                .filter(c -> c.isInMemory() && c.getOperation() == EntityOp.READ)
                .allMatch(c -> ((Predicate<Entity>) c.getPredicate()).test(entity));
    }

    @Override
    public Object evaluateConstraintScript(Entity entity, String groovyScript) {
        String metaClassName = entity.getMetaClass().getName();
        if (StringUtils.isNotBlank(groovyScript)) {
            try {
                Object result = runGroovyScript(entity, groovyScript);
                if (Boolean.FALSE.equals(result)) {
                    log.trace("Entity does not match security constraint. Entity class [{}]. Entity [{}].",
                            metaClassName, entity.getId());
                    return false;
                }
            } catch (Exception e) {
                log.error("An error occurred while applying constraint's Groovy script. The entity has been filtered out." +
                        "Entity class [{}]. Entity [{}].", metaClassName, entity.getId(), e);
                return false;
            }
        }
        return true;
    }

    @Override
    public ConstraintValidationResult validateConstraintScript(String entityType, String groovyScript) {
        ConstraintValidationResult result = new ConstraintValidationResult();
        try {
            runGroovyScript(metadata.create(entityType), groovyScript);
        } catch (CompilationFailedException e) {
            result.setCompilationFailedException(true);
            result.setStacktrace(ExceptionUtils.getStackTrace(e));
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            // ignore
        }
        return result;
    }

    protected Object runGroovyScript(Entity entity, String groovyScript) {
        Map<String, Object> context = new HashMap<>();
        context.put("__entity__", entity);
        context.put("parse", new MethodClosure(this, "parseValue"));
        context.put("userSession", userSessionSource.getUserSession());
        fillGroovyConstraintsContext(context);
        return scripting.evaluateGroovy(groovyScript.replace("{E}", "__entity__"), context);
    }

    /**
     * Override if you need specific context variables in Groovy constraints.
     *
     * @param context passed to Groovy evaluator
     */
    protected void fillGroovyConstraintsContext(Map<String, Object> context) {
    }

    @SuppressWarnings("unused")
    protected Object parseValue(Class<?> clazz, String string) {
        try {
            if (Entity.class.isAssignableFrom(clazz)) {
                Object entity = metadata.create((Class<Entity>) clazz);
                if (entity instanceof BaseIntegerIdEntity) {
                    ((BaseIntegerIdEntity) entity).setId(Integer.valueOf(string));
                } else if (entity instanceof BaseLongIdEntity) {
                    ((BaseLongIdEntity) entity).setId(Long.valueOf(string));
                } else if (entity instanceof BaseStringIdEntity) {
                    ((BaseStringIdEntity) entity).setId(string);
                } else if (entity instanceof BaseIdentityIdEntity) {
                    ((BaseIdentityIdEntity) entity).setId(IdProxy.of(Long.valueOf(string)));
                } else if (entity instanceof BaseIntIdentityIdEntity) {
                    ((BaseIntIdentityIdEntity) entity).setId(IdProxy.of(Integer.valueOf(string)));
                } else if (entity instanceof HasUuid) {
                    ((HasUuid) entity).setUuid(UUID.fromString(string));
                }
                return entity;
            } else if (EnumClass.class.isAssignableFrom(clazz)) {
                //noinspection unchecked
                return Enum.valueOf((Class<Enum>) clazz, string);
            } else {
                Datatype datatype = Datatypes.get(clazz);
                return datatype != null ? datatype.parse(string) : string;
            }
        } catch (ParseException | IllegalArgumentException e) {
            log.error("Could not parse a value in constraint. Class [{}], value [{}].", clazz, string, e);
            throw new RowLevelSecurityException(format("Could not parse a value in constraint. Class [%s], value [%s]. " +
                    "See the log for details.", clazz, string), null);
        }
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