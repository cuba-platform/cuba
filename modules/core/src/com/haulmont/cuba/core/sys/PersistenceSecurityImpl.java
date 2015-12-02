/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.google.common.collect.Multimap;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.global.ConstraintData;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

import static java.lang.String.format;

/**
 * @author krivopustov
 * @version $Id$
 */
@PerformanceLog
public class PersistenceSecurityImpl extends SecurityImpl implements PersistenceSecurity {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected SecurityTokenManager securityTokenManager;

    @Inject
    protected Configuration configuration;

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
            String join = constraint.getJoin();
            String where = constraint.getWhereClause();
            processConstraint(transformer, join, where);
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
    public void applyConstraints(Collection<Entity> entities) {
        internalApplyConstraints(entities, new HashSet<>());
    }

    @Override
    public boolean applyConstraints(Entity entity) {
        return internalApplyConstraints(entity, new HashSet<>());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void restoreFilteredData(BaseGenericIdEntity<?> resultEntity) {
        Persistence persistence = AppBeans.get(Persistence.NAME);
        EntityManager entityManager = persistence.getEntityManager();

        securityTokenManager.readSecurityToken(resultEntity);

        if (resultEntity.__securityToken() == null) {
            List<ConstraintData> existingConstraints = getConstraints(resultEntity.getMetaClass(),
                    constraint -> constraint.getCheckType().memory());
            if (CollectionUtils.isNotEmpty(existingConstraints)) {
                throw new RowLevelSecurityException(format("Could not read security token from entity %s, " +
                        "even though there are active constraints for the entity.", resultEntity),
                        resultEntity.getMetaClass().getName());
            }
        }

        Multimap<String, UUID> filtered = resultEntity.__filteredData();
        if (filtered == null) {
            return;
        }

        MetaClass metaClass = resultEntity.getMetaClass();
        for (Map.Entry<String, Collection<UUID>> entry : filtered.asMap().entrySet()) {
            MetaProperty property = metaClass.getPropertyNN(entry.getKey());
            Collection<UUID> filteredIds = entry.getValue();

            if (property.getRange().isClass() && CollectionUtils.isNotEmpty(filteredIds)) {
                Class entityClass = property.getRange().asClass().getJavaClass();
                Class propertyClass = property.getJavaType();
                if (Collection.class.isAssignableFrom(propertyClass)) {
                    Collection currentCollection = resultEntity.getValue(property.getName());
                    if (currentCollection == null) {
                        throw new RowLevelSecurityException(
                                format("Could not restore an object to currentValue because it is null [%s]. Entity [%s].",
                                        property.getName(), metaClass.getName()), metaClass.getName());
                    }

                    for (UUID entityId : filteredIds) {
                        Entity<UUID> reference = entityManager.getReference(entityClass, entityId);
                        //we ignore situations when the currentValue is immutable
                        currentCollection.add(reference);
                    }
                } else if (Entity.class.isAssignableFrom(propertyClass)) {
                    UUID entityId = filteredIds.iterator().next();
                    Entity<UUID> reference = entityManager.getReference(entityClass, entityId);
                    //we ignore the situation when the field is read-only
                    resultEntity.setValue(property.getName(), reference);
                }
            }
        }
    }

    protected void processConstraint(QueryTransformer transformer, String join, String where) {
        if (StringUtils.isBlank(join)) {
            if (!StringUtils.isBlank(where))
                transformer.addWhere(where);
        } else
            transformer.addJoinAndWhere(join, where);
    }

    protected Set<UUID> internalApplyConstraints(Collection<Entity> entities, Set<UUID> handled) {
        Set<UUID> filtered = new LinkedHashSet<>();
        for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext(); ) {
            Entity next = iterator.next();
            if (internalApplyConstraints(next, handled)) {
                filtered.add(next.getUuid());
                //we ignore situations when the collection is immutable
                iterator.remove();
            }
        }

        return filtered;
    }

    @SuppressWarnings("unchecked")
    protected boolean internalApplyConstraints(Entity entity, Set<UUID> handled) {
        MetaClass metaClass = entity.getMetaClass();

        if (!isPermitted(entity, constraint ->
                constraint.getCheckType().memory()
                        && (constraint.getOperationType() == ConstraintOperationType.READ
                        || constraint.getOperationType() == ConstraintOperationType.ALL))) {
            return true;
        }

        if (handled.contains(entity.getUuid())) return false;
        handled.add(entity.getUuid());

        for (MetaProperty property : metaClass.getProperties()) {
            if (metadataTools.isPersistent(property) && PersistenceHelper.isLoaded(entity, property.getName())) {
                Object value = entity.getValue(property.getName());
                if (value instanceof Collection) {
                    Set<UUID> filtered = internalApplyConstraints((Collection<Entity>) value, handled);
                    if (entity instanceof BaseGenericIdEntity) {
                        securityTokenManager.addFiltered((BaseGenericIdEntity) entity, property.getName(), filtered);
                    }
                } else if (value instanceof Entity) {
                    Entity valueEntity = (Entity) value;
                    if (internalApplyConstraints(valueEntity, handled)) {
                        //we ignore the situation when the field is read-only
                        entity.setValue(property.getName(), null);
                        if (entity instanceof BaseGenericIdEntity) {
                            securityTokenManager.addFiltered((BaseGenericIdEntity) entity, property.getName(), valueEntity.getUuid());
                        }
                    }
                }
            }
        }

        if (entity instanceof BaseGenericIdEntity) {
            securityTokenManager.writeSecurityToken((BaseGenericIdEntity<?>) entity);
        }

        return false;
    }

    @SuppressWarnings("unused")
    protected Object getParameterValue(Class clazz, String parameterValue) {
        try {
            if (String.class.isAssignableFrom(clazz)) {
                return parameterValue;
            } else if (Entity.class.isAssignableFrom(clazz)) {
                UUID uuid = UUID.fromString(parameterValue);
                Object entity = metadata.create(clazz);
                if (entity instanceof BaseUuidEntity) {
                    ((BaseUuidEntity) entity).setId(uuid);
                } else {
                    ((BaseGenericIdEntity) entity).setValue("uuid", uuid);
                }

                return entity;
            }

            Datatype datatype = Datatypes.get(clazz);
            return datatype != null ? datatype.parse(parameterValue) : parameterValue;
        } catch (ParseException e) {
            throw new RowLevelSecurityException(e, format("Could not parse a value from constraint. Class [%s], value [%s].",
                    clazz, parameterValue), null);
        }
    }
}
