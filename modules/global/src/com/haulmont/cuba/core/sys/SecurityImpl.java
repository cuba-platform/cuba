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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.ConstraintData;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.runtime.MethodClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import static com.haulmont.cuba.security.entity.ConstraintOperationType.ALL;
import static com.haulmont.cuba.security.entity.ConstraintOperationType.CUSTOM;
import static java.lang.String.format;

@Component(Security.NAME)
@PerformanceLog
public class SecurityImpl implements Security {
    protected final Logger log = LoggerFactory.getLogger(getClass());

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

    protected boolean isEntityAttrReadPermitted(MetaPropertyPath mpp) {
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

    protected boolean isEntityAttrUpdatePermitted(MetaPropertyPath mpp) {
        MetaClass propertyMetaClass = metadata.getTools().getPropertyEnclosingMetaClass(mpp);

        if (metadata.getTools().isEmbeddable(propertyMetaClass)) {
            return isEntityOpPermitted(propertyMetaClass, EntityOp.UPDATE)
                    && isEntityAttrPermitted(propertyMetaClass, mpp, EntityAttrAccess.MODIFY);
        }

        return (isEntityOpPermitted(propertyMetaClass, EntityOp.CREATE)
                || isEntityOpPermitted(propertyMetaClass, EntityOp.UPDATE))
                && isEntityAttrPermitted(propertyMetaClass, mpp, EntityAttrAccess.MODIFY);
    }

    @Override
    public boolean isPermitted(Entity entity, ConstraintOperationType targetOperationType) {
        return isPermitted(entity,
                constraint -> {
                    ConstraintOperationType operationType = constraint.getOperationType();
                    return constraint.getCheckType().memory()
                            && (
                            (targetOperationType == ALL && operationType != CUSTOM)
                                    || operationType == targetOperationType
                                    || operationType == ALL
                    );
                });
    }

    @Override
    public boolean isPermitted(Entity entity, String customCode) {
        return isPermitted(entity,
                constraint -> customCode.equals(constraint.getCode()) && constraint.getCheckType().memory());
    }

    @Override
    public boolean hasConstraints(MetaClass metaClass) {
        UserSession userSession = userSessionSource.getUserSession();
        String mainMetaClassName = extendedEntities.getOriginalOrThisMetaClass(metaClass).getName();
        return userSession.hasConstraints(mainMetaClassName);
    }

    protected List<ConstraintData> getConstraints(MetaClass metaClass, Predicate<ConstraintData> predicate) {
        UserSession userSession = userSessionSource.getUserSession();
        String mainMetaClassName = extendedEntities.getOriginalOrThisMetaClass(metaClass).getName();
        return userSession.getConstraints(mainMetaClassName, predicate);
    }

    protected boolean isPermitted(Entity entity, Predicate<ConstraintData> predicate) {
        List<ConstraintData> constraints = getConstraints(entity.getMetaClass(), predicate);
        for (ConstraintData constraint : constraints) {
            if (!isPermitted(entity, constraint)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isPermitted(Entity entity, ConstraintData constraint) {
        String metaClassName = entity.getMetaClass().getName();
        String groovyScript = constraint.getGroovyScript();
        if (constraint.getCheckType().memory() && StringUtils.isNotBlank(groovyScript)) {
            Map<String, Object> params = new HashMap<>();
            params.put("theEntity", metadataTools.deepCopy(entity));//copy to avoid implicit modification
            params.put("value", new MethodClosure(this, "getParameterValue"));
            try {
                Object o = scripting.evaluateGroovy(groovyScript.replace("{E}", "theEntity"), params);
                if (Boolean.FALSE.equals(o)) {
                    log.trace(format("Entity does not match security constraint. " +
                            "Entity class [%s]. Entity [%s]. Constraint [%s].", metaClassName, entity.getId(), constraint.getCheckType()));
                    return false;
                }
            } catch (Exception e) {
                log.error(format("An error occurred while applying constraint's groovy script. " +
                        "The entity has been filtered." +
                        "Entity class [%s]. Entity [%s].", metaClassName, entity.getId()), e);
                return false;
            }
        }
        return true;
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
            log.error(format("Could not parse a value from constraint. Class [%s], value [%s].", clazz, parameterValue), e);
            throw new RowLevelSecurityException(format("Could not parse a value from constraint. Class [%s], value [%s]. " +
                    "Please see the logs.", clazz, parameterValue), null);
        }
    }
}