/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.security.app.role;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.app.role.annotation.*;
import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.ScreenComponentPermission;
import com.haulmont.cuba.security.role.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

/**
 * INTERNAL
 * <p>
 * Helps construct permissions for roles defined using annotations.
 */
@Component(AnnotatedPermissionsBuilder.NAME)
public class AnnotatedPermissionsBuilder {
    public static final String NAME = "cuba_AnnotatedPermissionsBuilder";

    private static final String ENTITY_ACCESS_METHOD_NAME = "entityPermissions";
    private static final String ENTITY_ATTR_ACCESS_METHOD_NAME = "entityAttributePermissions";
    private static final String SPECIFIC_ACCESS_METHOD_NAME = "specificPermissions";
    private static final String SCREEN_ACCESS_METHOD_NAME = "screenPermissions";
    private static final String SCREEN_COMPONENT_ACCESS_METHOD_NAME = "screenComponentPermissions";

    @Inject
    private Logger log;

    @Inject
    protected Metadata metadata;

    @Inject
    protected ExtendedEntities extendedEntities;

    public EntityPermissionsContainer buildEntityAccessPermissions(RoleDefinition role) {
        boolean isSuper = getIsSuperFromAnnotation(role);
        if (isSuper) {
            EntityPermissionsContainer entityPermissionsContainer = new EntityPermissionsContainer();
            entityPermissionsContainer.getExplicitPermissions().put("*:create", Access.ALLOW.getId());
            entityPermissionsContainer.getExplicitPermissions().put("*:read", Access.ALLOW.getId());
            entityPermissionsContainer.getExplicitPermissions().put("*:update", Access.ALLOW.getId());
            entityPermissionsContainer.getExplicitPermissions().put("*:delete", Access.ALLOW.getId());
            return entityPermissionsContainer;
        }
        return (EntityPermissionsContainer) processAnnotationsInternal(role,
                EntityAccess.class,
                ENTITY_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processEntityAccessAnnotation((EntityAccess) annotation,
                        (EntityPermissionsContainer) permissions));
    }

    public EntityAttributePermissionsContainer buildEntityAttributeAccessPermissions(RoleDefinition role) {
        boolean isSuper = getIsSuperFromAnnotation(role);
        if (isSuper) {
            EntityAttributePermissionsContainer entityAttributeAccessContainer = new EntityAttributePermissionsContainer();
            entityAttributeAccessContainer.getExplicitPermissions().put("*:*", EntityAttrAccess.MODIFY.getId());
            return entityAttributeAccessContainer;
        }
        return (EntityAttributePermissionsContainer) processAnnotationsInternal(role,
                EntityAttributeAccess.class,
                ENTITY_ATTR_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processEntityAttributeAccessAnnotation((EntityAttributeAccess) annotation,
                        (EntityAttributePermissionsContainer) permissions));
    }

    public SpecificPermissionsContainer buildSpecificPermissions(RoleDefinition role) {
        boolean isSuper = getIsSuperFromAnnotation(role);
        if (isSuper) {
            SpecificPermissionsContainer specificPermissionsContainer = new SpecificPermissionsContainer();
            specificPermissionsContainer.getExplicitPermissions().put("*", Access.ALLOW.getId());
            return specificPermissionsContainer;
        }
        return (SpecificPermissionsContainer) processAnnotationsInternal(role,
                SpecificAccess.class,
                SPECIFIC_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processSpecificAccessAnnotation((SpecificAccess) annotation,
                        (SpecificPermissionsContainer) permissions));
    }

    public ScreenPermissionsContainer buildScreenPermissions(RoleDefinition role) {
        boolean isSuper = getIsSuperFromAnnotation(role);
        if (isSuper) {
            ScreenPermissionsContainer screenPermissionsContainer = new ScreenPermissionsContainer();
            screenPermissionsContainer.getExplicitPermissions().put("*", Access.ALLOW.getId());
            return screenPermissionsContainer;
        }
        return (ScreenPermissionsContainer) processAnnotationsInternal(role,
                ScreenAccess.class,
                SCREEN_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processScreenAccessAnnotation((ScreenAccess) annotation,
                        (ScreenPermissionsContainer) permissions));
    }

    public ScreenComponentPermissionsContainer buildScreenElementsPermissions(RoleDefinition role) {
        return (ScreenComponentPermissionsContainer) processAnnotationsInternal(role,
                ScreenComponentAccess.class,
                SCREEN_COMPONENT_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processScreenElementAccessAnnotation((ScreenComponentAccess) annotation,
                        (ScreenComponentPermissionsContainer) permissions));
    }

    public String getNameFromAnnotation(RoleDefinition role) {
        Role annotation = getPredefinedRoleAnnotationNN(role);
        return annotation.name();
    }

    public String getSecurityScopeFromAnnotation(RoleDefinition role) {
        Role annotation = getPredefinedRoleAnnotationNN(role);
        return annotation.securityScope();
    }

    public String getDescriptionFromAnnotation(RoleDefinition role) {
        Role annotation = getPredefinedRoleAnnotationNN(role);
        return annotation.description();
    }

    public boolean getIsDefaultFromAnnotation(RoleDefinition role) {
        Role annotation = getPredefinedRoleAnnotationNN(role);
        return annotation.isDefault();
    }

    public boolean getIsSuperFromAnnotation(RoleDefinition role) {
        Role annotation = getPredefinedRoleAnnotationNN(role);
        return annotation.isSuper();
    }

    protected Role getPredefinedRoleAnnotationNN(RoleDefinition role) {
        Role annotation = role.getClass().getAnnotation(Role.class);
        if (annotation == null) {
            throw new IllegalArgumentException("The class must have Role annotation.");
        }
        return annotation;
    }

    protected EntityAttributePermissionsContainer processEntityAttributeAccessAnnotation(
            EntityAttributeAccess annotation,
            EntityAttributePermissionsContainer permissions) {
        Class<? extends Entity> entityClass = annotation.entityClass();
        String entityName = annotation.entityName();
        if (entityClass != NullEntity.class) {
            MetaClass metaClass;
            metaClass = metadata.getClassNN(entityClass);
            entityName = metaClass.getName();
        } else if (Strings.isNullOrEmpty(entityName)) {
            log.warn("Neither entityClass, not entityName is defined for the EntityAttributeAccess annotation.");
            return permissions;
        }
        String[] modify = annotation.modify();
        String[] view = annotation.view();

        String originalEntityName = evaluateOriginalEntityName(entityName);

        for (String property : modify) {
            addEntityAttributeTarget(permissions, originalEntityName, property, EntityAttrAccess.MODIFY);
        }

        for (String property : view) {
            addEntityAttributeTarget(permissions, originalEntityName, property, EntityAttrAccess.VIEW);
        }
        return permissions;
    }

    protected void addEntityAttributeTarget(PermissionsContainer permissions, String entityName, String property,
                                            EntityAttrAccess access) {
        String target = PermissionsUtils.getEntityAttributeTarget(entityName, property);
        Integer permissionValue = access.getId();
        permissions.getExplicitPermissions().put(target, permissionValue);
        String extendedTarget = PermissionsUtils.evaluateExtendedEntityTarget(target);
        if (!Strings.isNullOrEmpty(extendedTarget)) {
            permissions.getExplicitPermissions().put(extendedTarget, permissionValue);
        }

    }

    protected void processEntityAccessAnnotation(EntityAccess annotation, EntityPermissionsContainer permissions) {
        Class<? extends Entity> entityClass = annotation.entityClass();
        String entityName = annotation.entityName();
        if (entityClass != NullEntity.class) {
            MetaClass metaClass;
            metaClass = metadata.getClassNN(entityClass);
            entityName = metaClass.getName();
        } else if (Strings.isNullOrEmpty(entityName)) {
            log.warn("Neither entityClass, not entityName is defined for the EntityAccess annotation.");
            return;
        }

        String originalEntityName = evaluateOriginalEntityName(entityName);

        EntityOp[] operations = annotation.operations();
        for (EntityOp entityOp : operations) {
            String target = PermissionsUtils.getEntityOperationTarget(originalEntityName, entityOp);
            Integer permissionValue = Access.ALLOW.getId();
            permissions.getExplicitPermissions().put(target, permissionValue);
            String extendedTarget = PermissionsUtils.evaluateExtendedEntityTarget(target);
            if (!Strings.isNullOrEmpty(extendedTarget)) {
                permissions.getExplicitPermissions().put(extendedTarget, permissionValue);
            }
        }
    }

    /**
     * @param entityName there may be an entity name of wildcard here
     * @return an original entity name if the passed {@code entityName} extends other entity, or the {@code entityName}
     * itself otherwise
     */
    protected String evaluateOriginalEntityName(String entityName) {
        //we'll store permissions for original meta class
        MetaClass metaClass = metadata.getClass(entityName);
        if (metaClass != null) {
            MetaClass originalMetaClass = extendedEntities.getOriginalMetaClass(metaClass);
            if (originalMetaClass != null) {
                entityName = originalMetaClass.getName();
            }
        }
        return entityName;
    }

    protected void processSpecificAccessAnnotation(SpecificAccess annotation,
                                                   SpecificPermissionsContainer permissions) {
        String[] specificPermissions = annotation.permissions();
        for (String specificPermission : specificPermissions) {
            permissions.getExplicitPermissions().put(specificPermission, Access.ALLOW.getId());
        }
    }

    protected void processScreenAccessAnnotation(ScreenAccess annotation,
                                                 ScreenPermissionsContainer permissions) {
        String[] screenIds = annotation.screenIds();
        for (String screenId : screenIds) {
            permissions.getExplicitPermissions().put(screenId, Access.ALLOW.getId());
        }
    }

    protected void processScreenElementAccessAnnotation(ScreenComponentAccess annotation,
                                                        ScreenComponentPermissionsContainer permissions) {
        String screen = annotation.screenId();
        String[] deny = annotation.deny();
        String[] view = annotation.view();
        String[] modify = annotation.modify();

        if (Strings.isNullOrEmpty(screen)) {
            return;
        }

        for (String component : deny) {
            String target = PermissionsUtils.getScreenComponentTarget(screen, component);
            permissions.getExplicitPermissions().put(target, ScreenComponentPermission.DENY.getId());
        }

        for (String component : view) {
            String target = PermissionsUtils.getScreenComponentTarget(screen, component);
            permissions.getExplicitPermissions().put(target, ScreenComponentPermission.VIEW.getId());
        }

        for (String component : modify) {
            String target = PermissionsUtils.getScreenComponentTarget(screen, component);
            permissions.getExplicitPermissions().put(target, ScreenComponentPermission.MODIFY.getId());
        }
    }

    protected PermissionsContainer processAnnotationsInternal(
            Object role,
            Class<? extends Annotation> explicitAccessAnnotationClass,
            String methodName,
            BiConsumer<Object, PermissionsContainer> explicitAccessBiConsumer) {
        try {
            Method method = role.getClass().getMethod(methodName);
            Object[] explicitAccessAnnotations = method.getAnnotationsByType(explicitAccessAnnotationClass);
            PermissionsContainer permissionsContainer = createPermissionsByMethodName(methodName);
            for (Object annotation : explicitAccessAnnotations) {
                explicitAccessBiConsumer.accept(annotation, permissionsContainer);
            }
            return permissionsContainer;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No such method: " + methodName);
        }
    }

    protected PermissionsContainer createPermissionsByMethodName(String methodName) {
        switch (methodName) {
            case ENTITY_ACCESS_METHOD_NAME:
                return new EntityPermissionsContainer();
            case ENTITY_ATTR_ACCESS_METHOD_NAME:
                return new EntityAttributePermissionsContainer();
            case SPECIFIC_ACCESS_METHOD_NAME:
                return new SpecificPermissionsContainer();
            case SCREEN_ACCESS_METHOD_NAME:
                return new ScreenPermissionsContainer();
            case SCREEN_COMPONENT_ACCESS_METHOD_NAME:
                return new ScreenComponentPermissionsContainer();
            default:
                throw new IllegalArgumentException("No such method: " + methodName);
        }
    }
}
