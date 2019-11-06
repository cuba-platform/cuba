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
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.app.role.annotation.*;
import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.function.BiFunction;

/**
 * INTERNAL
 *
 * Helps construct permissions for roles defined using annotations.
 */
@Component(AnnotationPermissionsBuilder.NAME)
public class AnnotationPermissionsBuilder {
    public static final String NAME = "cuba_AnnotationPermissionsBuilder";

    private static final String ENTITY_ACCESS_METHOD_NAME = "entityPermissions";
    private static final String ENTITY_ATTR_ACCESS_METHOD_NAME = "entityAttributePermissions";
    private static final String SPECIFIC_ACCESS_METHOD_NAME = "specificPermissions";
    private static final String SCREEN_ACCESS_METHOD_NAME = "screenPermissions";
    private static final String SCREEN_ELEMENTS_ACCESS_METHOD_NAME = "screenElementsPermissions";

    @Inject
    protected Metadata metadata;

    public EntityPermissions buildEntityAccessPermissions(ApplicationRole role) {
        return (EntityPermissions) processAnnotationsInternal(role,
                EntityAccess.class,
                ENTITY_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processEntityAccessAnnotation((EntityAccess) annotation,
                        (EntityPermissions) permissions));
    }

    public EntityAttributePermissions buildEntityAttributeAccessPermissions(ApplicationRole role) {

        return (EntityAttributePermissions) processAnnotationsInternal(role,
                EntityAttributeAccess.class,
                ENTITY_ATTR_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processEntityAttributeAccessAnnotation((EntityAttributeAccess) annotation,
                        (EntityAttributePermissions) permissions));
    }

    public SpecificPermissions buildSpecificPermissions(ApplicationRole role) {
        return (SpecificPermissions) processAnnotationsInternal(role,
                SpecificAccess.class,
                SPECIFIC_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processSpecificPermissionAnnotation((SpecificAccess) annotation,
                        (SpecificPermissions) permissions));
    }

    public ScreenPermissions buildScreenPermissions(GenericUiRole role) {
        return (ScreenPermissions) processAnnotationsInternal(role,
                ScreenAccess.class,
                SCREEN_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processScreenAccessAnnotation((ScreenAccess) annotation,
                        (ScreenPermissions) permissions));
    }

    public ScreenElementsPermissions buildScreenElementsPermissions(GenericUiRole role) {
        return (ScreenElementsPermissions) processAnnotationsInternal(role,
                ScreenElementAccess.class,
                SCREEN_ELEMENTS_ACCESS_METHOD_NAME,
                (annotation, permissions) -> processScreenElementAccessAnnotation((ScreenElementAccess) annotation,
                        (ScreenElementsPermissions) permissions));
    }

    public String getNameFromAnnotation(RoleDefinition role) {
        Role annotation = getPredefinedRoleAnnotationNN(role);

        return annotation.name();
    }

    public String getDescriptionFromAnnotation(RoleDefinition role) {
        Role annotation = getPredefinedRoleAnnotationNN(role);

        return annotation.description();
    }

    public RoleType getTypeFromAnnotation(RoleDefinition role) {
        Role annotation = getPredefinedRoleAnnotationNN(role);

        return annotation.type();
    }

    public boolean getIsDefaultFromAnnotation(RoleDefinition role) {
        Role annotation = getPredefinedRoleAnnotationNN(role);

        return annotation.isDefault();
    }

    protected Role getPredefinedRoleAnnotationNN(RoleDefinition role) {
        Role annotation = role.getClass().getAnnotation(Role.class);
        if (annotation == null) {
            throw new IllegalArgumentException("The class must have Role annotation.");
        }
        return annotation;
    }

    protected EntityAttributePermissions processEntityAttributeAccessAnnotation(EntityAttributeAccess annotation,
                                                                                EntityAttributePermissions permissions) {
        Class entityClass = annotation.target();
        MetaClass metaClass = metadata.getClassNN(entityClass);
        String[] deny = annotation.deny();
        String[] allow = annotation.allow();
        String[] readOnly = annotation.readOnly();

        for (String property : deny) {
            String target = PermissionsUtils.getEntityAttributeTarget(metaClass, property);
            PermissionsUtils.addPermission(permissions, target, null, EntityAttrAccess.DENY.getId());
        }

        for (String property : allow) {
            String target = PermissionsUtils.getEntityAttributeTarget(metaClass, property);
            PermissionsUtils.addPermission(permissions, target, null, EntityAttrAccess.MODIFY.getId());
        }

        for (String property : readOnly) {
            String target = PermissionsUtils.getEntityAttributeTarget(metaClass, property);
            PermissionsUtils.addPermission(permissions, target, null, EntityAttrAccess.VIEW.getId());
        }

        return permissions;
    }

    protected EntityPermissions processEntityAccessAnnotation(EntityAccess annotation, EntityPermissions permissions) {
        Class entityClass = annotation.target();
        MetaClass metaClass = metadata.getClassNN(entityClass);
        EntityOp[] deny = annotation.deny();
        EntityOp[] allow = annotation.allow();

        for (EntityOp entityOp : deny) {
            String target = PermissionsUtils.getEntityOperationTarget(metaClass, entityOp);
            PermissionsUtils.addPermission(permissions, target, null, Access.DENY.getId());
        }

        for (EntityOp entityOp : allow) {
            String target = PermissionsUtils.getEntityOperationTarget(metaClass, entityOp);
            PermissionsUtils.addPermission(permissions, target, null, Access.ALLOW.getId());
        }

        return permissions;
    }

    protected SpecificPermissions processSpecificPermissionAnnotation(SpecificAccess annotation, SpecificPermissions permissions) {
        String target = annotation.target();
        Access access = annotation.access();

        if (Strings.isNullOrEmpty(target)) {
            return permissions;
        }

        PermissionsUtils.addPermission(permissions, target, null, access.getId());

        return permissions;
    }

    protected ScreenPermissions processScreenAccessAnnotation(ScreenAccess annotation, ScreenPermissions permissions) {
        String[] deny = annotation.deny();
        String[] allow = annotation.allow();

        for (String screen : deny) {
            PermissionsUtils.addPermission(permissions, screen, null, Access.DENY.getId());
        }

        for (String screen : allow) {
            PermissionsUtils.addPermission(permissions, screen, null, Access.ALLOW.getId());
        }

        return permissions;
    }

    protected ScreenElementsPermissions processScreenElementAccessAnnotation(ScreenElementAccess annotation,
                                                                             ScreenElementsPermissions permissions) {
        String screen = annotation.screen();
        String[] deny = annotation.deny();
        String[] allow = annotation.allow();

        if (Strings.isNullOrEmpty(screen)) {
            return permissions;
        }

        for (String component : deny) {
            String target = PermissionsUtils.getScreenElementTarget(screen, component);
            PermissionsUtils.addPermission(permissions, target, null, Access.DENY.getId());
        }

        for (String component : allow) {
            String target = PermissionsUtils.getScreenElementTarget(screen, component);
            PermissionsUtils.addPermission(permissions, target, null, Access.ALLOW.getId());
        }

        return permissions;

    }

    protected Permissions processAnnotationsInternal(Object role, Class<? extends Annotation> annotationClass,
                                                     String methodName, BiFunction<Object, Permissions, Permissions> biFunction) {
        if (role == null) {
            return null;
        }

        try {
            Object[] annotations = role.getClass()
                    .getMethod(methodName)
                    .getAnnotationsByType(annotationClass);
            Permissions permissions = createPermissionsByMethodName(methodName);

            if (annotations.length == 0) {
                return permissions;
            }

            for (Object annotation : annotations) {
                permissions = biFunction.apply(annotation, permissions);
            }

            return permissions;

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No such method: " + methodName);
        }
    }

    protected Permissions createPermissionsByMethodName(String methodName) {
        switch (methodName) {
            case ENTITY_ACCESS_METHOD_NAME:
                return new EntityPermissions();
            case ENTITY_ATTR_ACCESS_METHOD_NAME:
                return new EntityAttributePermissions();
            case SPECIFIC_ACCESS_METHOD_NAME:
                return new SpecificPermissions();
            case SCREEN_ACCESS_METHOD_NAME:
                return new ScreenPermissions();
            case SCREEN_ELEMENTS_ACCESS_METHOD_NAME:
                return new ScreenElementsPermissions();
            default:
                throw new IllegalArgumentException("No such method: " + methodName);

        }
    }

}
