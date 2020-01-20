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

import com.google.common.collect.Streams;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.group.AccessConstraint;
import com.haulmont.cuba.security.group.PersistenceSecurityService;
import com.haulmont.cuba.security.group.ConstraintsContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component(Security.NAME)
public class SecurityImpl implements Security {
    private final Logger log = LoggerFactory.getLogger(SecurityImpl.class);

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected ExtendedEntities extendedEntities;

    @Inject
    protected Scripting scripting;

    @Inject
    protected PersistenceSecurityService persistenceSecurityService;

    @Override
    public boolean isScreenPermitted(String windowAlias) {
        return userSessionSource.getUserSession().isScreenPermitted(windowAlias);
    }

    @Override
    public boolean isEntityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        MetaClass originalMetaClass = extendedEntities.getOriginalMetaClass(metaClass);
        if (originalMetaClass != null) {
            metaClass = originalMetaClass;
        }

        return userSessionSource.getUserSession().isEntityOpPermitted(metaClass, entityOp);
    }

    @Override
    public boolean isEntityOpPermitted(Class<?> entityClass, EntityOp entityOp) {
        MetaClass metaClass = metadata.getSession().getClassNN(entityClass);
        return isEntityOpPermitted(metaClass, entityOp);
    }

    @Override
    public boolean isEntityAttrPermitted(MetaClass metaClass, String property, EntityAttrAccess access) {
        MetaPropertyPath mpp = metadataTools.resolveMetaPropertyPath(metaClass, property);
        return mpp != null && isEntityAttrPermitted(metaClass, mpp, access);
    }

    @Override
    public boolean isEntityAttrPermitted(Class<?> entityClass, String property, EntityAttrAccess access) {
        MetaClass metaClass = metadata.getSession().getClassNN(entityClass);
        return isEntityAttrPermitted(metaClass, property, access);
    }

    @Override
    public boolean isEntityAttrReadPermitted(MetaClass metaClass, String propertyPath) {
        MetaPropertyPath mpp = metadataTools.resolveMetaPropertyPath(metaClass, propertyPath);
        return mpp != null && isEntityAttrReadPermitted(mpp);
    }

    @Override
    public boolean isEntityAttrUpdatePermitted(MetaClass metaClass, String propertyPath) {
        MetaPropertyPath mpp = metadataTools.resolveMetaPropertyPath(metaClass, propertyPath);
        return mpp != null && isEntityAttrUpdatePermitted(mpp);
    }

    @Override
    public boolean isSpecificPermitted(String name) {
        return userSessionSource.getUserSession().isSpecificPermitted(name);
    }

    @Override
    public void checkSpecificPermission(String name) {
        if (!isSpecificPermitted(name))
            throw new AccessDeniedException(PermissionType.SPECIFIC, name);
    }

    @Override
    public boolean isEntityAttrReadPermitted(MetaPropertyPath mpp) {
        MetaClass propertyMetaClass = metadata.getTools().getPropertyEnclosingMetaClass(mpp);
        return isEntityOpPermitted(propertyMetaClass, EntityOp.READ)
                && isEntityAttrPermitted(propertyMetaClass, mpp, EntityAttrAccess.VIEW);
    }

    protected boolean isEntityAttrPermitted(MetaClass metaClass, MetaPropertyPath propertyPath, EntityAttrAccess access) {
        MetaClass originalMetaClass = extendedEntities.getOriginalMetaClass(metaClass);
        if (originalMetaClass != null) {
            metaClass = originalMetaClass;
        }

        return userSessionSource.getUserSession()
                .isEntityAttrPermitted(metaClass, propertyPath.getMetaProperty().getName(), access);
    }

    @Override
    public boolean isEntityAttrUpdatePermitted(MetaPropertyPath mpp) {
        MetaClass propertyMetaClass = metadata.getTools().getPropertyEnclosingMetaClass(mpp);

        if (metadata.getTools().isEmbeddable(propertyMetaClass)) {
            return isEntityOpPermitted(propertyMetaClass, EntityOp.UPDATE)
                    && isEntityAttrPermitted(propertyMetaClass, mpp, EntityAttrAccess.MODIFY)
                    && isEntityOpPermitted(mpp.getMetaClass(), EntityOp.UPDATE);
        }

        return (isEntityOpPermitted(propertyMetaClass, EntityOp.CREATE)
                || isEntityOpPermitted(propertyMetaClass, EntityOp.UPDATE))
                && isEntityAttrPermitted(propertyMetaClass, mpp, EntityAttrAccess.MODIFY);
    }

    @Override
    public boolean isPermitted(Entity entity, EntityOp operation) {
        return persistenceSecurityService.isPermitted(entity, operation);
    }

    @Override
    public boolean isPermitted(Entity entity, ConstraintOperationType operationType) {
        for (EntityOp entityOp : operationType.toEntityOps()) {
            if (!persistenceSecurityService.isPermitted(entity, entityOp)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isPermitted(Entity entity, String customCode) {
        return persistenceSecurityService.isPermitted(entity, customCode);
    }

    @Override
    public boolean hasConstraints(MetaClass metaClass) {
        return getConstraints(metaClass).findAny().isPresent();
    }

    @Override
    public boolean hasInMemoryConstraints(MetaClass metaClass, ConstraintOperationType... operationTypes) {
        final Set<EntityOp> entityOperations = Stream.of(operationTypes)
                .flatMap(o -> o.toEntityOps().stream())
                .collect(Collectors.toSet());

        return getConstraints(metaClass)
                .anyMatch(c -> c.isInMemory() && entityOperations.contains(c.getOperation()));
    }

    @Override
    public Object evaluateConstraintScript(Entity entity, String groovyScript) {
        return persistenceSecurityService.evaluateConstraintScript(entity, groovyScript);
    }

    protected Stream<AccessConstraint> getConstraints(MetaClass metaClass) {
        UserSession userSession = userSessionSource.getUserSession();
        MetaClass mainMetaClass = extendedEntities.getOriginalOrThisMetaClass(metaClass);

        ConstraintsContainer setOfConstraints = userSession.getConstraints();

        Stream<AccessConstraint> constraints = setOfConstraints.findConstraintsByEntity(mainMetaClass.getName());
        for (MetaClass parent : mainMetaClass.getAncestors()) {
            constraints = Streams.concat(constraints, setOfConstraints.findConstraintsByEntity(parent.getName()));
        }
        return constraints;
    }
}