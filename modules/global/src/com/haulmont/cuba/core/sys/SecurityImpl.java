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
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.ConstraintData;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.runtime.MethodClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.haulmont.cuba.security.entity.ConstraintOperationType.ALL;
import static com.haulmont.cuba.security.entity.ConstraintOperationType.CUSTOM;
import static java.lang.String.format;

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
        List<ConstraintData> constraints = getConstraints(metaClass);
        return !constraints.isEmpty();
    }

    @Override
    public boolean hasInMemoryConstraints(MetaClass metaClass, ConstraintOperationType... operationTypes) {
        List<ConstraintData> constraints = getConstraints(metaClass, constraint ->
                constraint.getCheckType().memory() && constraint.getOperationType() != null
                        && Arrays.asList(operationTypes).contains(constraint.getOperationType())
        );
        return !constraints.isEmpty();
    }

    protected List<ConstraintData> getConstraints(MetaClass metaClass, Predicate<ConstraintData> predicate) {
        return getConstraints(metaClass).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    protected List<ConstraintData> getConstraints(MetaClass metaClass) {
        UserSession userSession = userSessionSource.getUserSession();
        MetaClass mainMetaClass = extendedEntities.getOriginalOrThisMetaClass(metaClass);

        List<ConstraintData> constraints = new ArrayList<>();
        constraints.addAll(userSession.getConstraints(mainMetaClass.getName()));
        for (MetaClass parent : mainMetaClass.getAncestors()) {
            constraints.addAll(userSession.getConstraints(parent.getName()));
        }
        return constraints;
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
            try {
                Object o = evaluateConstraintScript(entity, groovyScript);
                if (Boolean.FALSE.equals(o)) {
                    log.trace("Entity does not match security constraint. Entity class [{}]. Entity [{}]. Constraint [{}].",
                            metaClassName, entity.getId(), constraint.getCheckType());
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
    public Object evaluateConstraintScript(Entity entity, String groovyScript) {
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
                Object entity = metadata.create((Class<Entity>)clazz);
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
                Enum parsedEnum = Enum.valueOf((Class<Enum>) clazz, string);
                return parsedEnum;
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
}