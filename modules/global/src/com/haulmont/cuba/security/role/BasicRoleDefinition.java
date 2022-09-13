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

package com.haulmont.cuba.security.role;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.*;

import java.io.Serializable;
import java.util.Collection;

/**
 * {@link RoleDefinition} implementation. To create an instance of the {@code BasicRoleDefinition} use the
 * {@link BasicRoleDefinitionBuilder}:
 *
 * <pre>
 * BasicRoleDefinition role = BasicRoleDefinition.builder()
 *     .withName("name")
 *     .withDescription("description")
 *     ...
 *     .build();
 * </pre>
 */
public class BasicRoleDefinition implements RoleDefinition, Serializable {

    private EntityPermissionsContainer entityPermissions;
    private EntityAttributePermissionsContainer entityAttributePermissions;
    private SpecificPermissionsContainer specificPermissions;
    private ScreenPermissionsContainer screenPermissions;
    private ScreenComponentPermissionsContainer screenElementsPermissions;
    private String name;
    private String locName;
    private String description;
    private String securityScope;
    private boolean isDefault;
    private boolean isSuper;

    private BasicRoleDefinition() {
        entityPermissions = new EntityPermissionsContainer();
        entityAttributePermissions = new EntityAttributePermissionsContainer();
        specificPermissions = new SpecificPermissionsContainer();
        screenPermissions = new ScreenPermissionsContainer();
        screenElementsPermissions = new ScreenComponentPermissionsContainer();
    }

    private BasicRoleDefinition(BasicRoleDefinitionBuilder builder) {
        this.name = builder.name;
        this.locName = builder.locName;
        this.description = builder.description;
        this.isDefault = builder.isDefault;
        this.isSuper = builder.isSuper;
        this.screenPermissions = builder.screenPermissions;
        this.entityPermissions = builder.entityPermissions;
        this.entityAttributePermissions = builder.entityAttributePermissions;
        this.specificPermissions = builder.specificPermissions;
        this.screenElementsPermissions = builder.screenComponentPermissions;
        this.securityScope = builder.securityScope;

        if (this.isSuper) {
            this.entityPermissions.getExplicitPermissions().put("*:create", Access.ALLOW.getId());
            this.entityPermissions.getExplicitPermissions().put("*:read", Access.ALLOW.getId());
            this.entityPermissions.getExplicitPermissions().put("*:update", Access.ALLOW.getId());
            this.entityPermissions.getExplicitPermissions().put("*:delete", Access.ALLOW.getId());
            this.entityAttributePermissions.getExplicitPermissions().put("*:*", EntityAttrAccess.MODIFY.getId());
            this.specificPermissions.getExplicitPermissions().put("*", Access.ALLOW.getId());
            this.screenPermissions.getExplicitPermissions().put("*", Access.ALLOW.getId());
        }
    }

    @Override
    public EntityPermissionsContainer entityPermissions() {
        return entityPermissions;
    }

    @Override
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return entityAttributePermissions;
    }

    @Override
    public SpecificPermissionsContainer specificPermissions() {
        return specificPermissions;
    }

    @Override
    public ScreenPermissionsContainer screenPermissions() {
        return screenPermissions;
    }

    @Override
    public ScreenComponentPermissionsContainer screenComponentPermissions() {
        return screenElementsPermissions;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getSecurityScope() {
        return securityScope;
    }

    public void setSecurityScope(String securityScope) {
        this.securityScope = securityScope;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }

    public static BasicRoleDefinitionBuilder builder() {
        return new BasicRoleDefinitionBuilder();
    }

    public static class BasicRoleDefinitionBuilder {

        private String name;
        private String locName;
        private String description;
        private String securityScope;
        private boolean isDefault;
        private boolean isSuper;
        private EntityPermissionsContainer entityPermissions = new EntityPermissionsContainer();
        private EntityAttributePermissionsContainer entityAttributePermissions = new EntityAttributePermissionsContainer();
        private SpecificPermissionsContainer specificPermissions = new SpecificPermissionsContainer();
        private ScreenPermissionsContainer screenPermissions = new ScreenPermissionsContainer();
        private ScreenComponentPermissionsContainer screenComponentPermissions = new ScreenComponentPermissionsContainer();

        private BasicRoleDefinitionBuilder() {
        }

        public BasicRoleDefinitionBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public BasicRoleDefinitionBuilder withLocName(String locName) {
            this.locName = locName;
            return this;
        }

        public BasicRoleDefinitionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public BasicRoleDefinitionBuilder withIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public BasicRoleDefinitionBuilder withIsSuper(boolean isSuper) {
            this.isSuper = isSuper;
            return this;
        }

        public BasicRoleDefinitionBuilder withSecurityScope(String securityScope) {
            this.securityScope = securityScope;
            return this;
        }

        public BasicRoleDefinitionBuilder withEntityPermissionsContainer(EntityPermissionsContainer entityPermissions) {
            this.entityPermissions = entityPermissions;
            return this;
        }

        public BasicRoleDefinitionBuilder withEntityAttributePermissionsContainer(EntityAttributePermissionsContainer entityAttributePermissions) {
            this.entityAttributePermissions = entityAttributePermissions;
            return this;
        }

        public BasicRoleDefinitionBuilder withSpecificPermissionsContainer(SpecificPermissionsContainer specificPermissions) {
            this.specificPermissions = specificPermissions;
            return this;
        }

        public BasicRoleDefinitionBuilder withScreenPermissionsContainer(ScreenPermissionsContainer screenPermissions) {
            this.screenPermissions = screenPermissions;
            return this;
        }

        public BasicRoleDefinitionBuilder withScreenComponentPermissionsContainer(ScreenComponentPermissionsContainer screenElementsPermissions) {
            this.screenComponentPermissions = screenElementsPermissions;
            return this;
        }

        public BasicRoleDefinitionBuilder withScreenPermission(String screenId, Access access) {
            addPermission(PermissionType.SCREEN, screenId, access.getId());
            return this;
        }

        public BasicRoleDefinitionBuilder withEntityPermission(String entityName, EntityOp entityOp, Access access) {
            String originalEntityName = evaluateOriginalEntityName(entityName);
            addPermission(PermissionType.ENTITY_OP,
                    PermissionsUtils.getEntityOperationTarget(originalEntityName, entityOp),
                    access.getId());
            return this;
        }

        public BasicRoleDefinitionBuilder withEntityPermission(Class<? extends Entity> entityClass, EntityOp entityOp, Access access) {
            String entityName = AppBeans.get(Metadata.class).getClassNN(entityClass).getName();
            return withEntityPermission(entityName, entityOp, access);
        }

        public BasicRoleDefinitionBuilder withEntityAttributePermission(String entityName, String attributeName,
                                                                        EntityAttrAccess entityAttrAccess) {
            String originalEntityName = evaluateOriginalEntityName(entityName);
            addPermission(PermissionType.ENTITY_ATTR,
                    PermissionsUtils.getEntityAttributeTarget(originalEntityName, attributeName),
                    entityAttrAccess.getId());
            return this;
        }

        public BasicRoleDefinitionBuilder withEntityAttributePermission(Class<? extends Entity> entityClass, String attributeName,
                                                                        EntityAttrAccess entityAttrAccess) {
            String entityName = AppBeans.get(Metadata.class).getClassNN(entityClass).getName();
            return withEntityAttributePermission(entityName, attributeName, entityAttrAccess);
        }

        public BasicRoleDefinitionBuilder withSpecificPermission(String target, Access access) {
            addPermission(PermissionType.SPECIFIC,
                    target,
                    access.getId());
            return this;
        }

        public BasicRoleDefinitionBuilder withScreenComponentPermission(String screenId, String componentId,
                                                                        Access access) {
            addPermission(PermissionType.UI,
                    PermissionsUtils.getScreenComponentTarget(screenId, componentId),
                    access.getId());
            return this;
        }

        protected BasicRoleDefinitionBuilder withPermission(Permission permission) {
            if (permission.getValue() != null) {
                addPermission(permission.getType(), permission.getTarget(), permission.getValue());
            }
            return this;
        }

        public BasicRoleDefinitionBuilder withPermissions(Collection<Permission> permissions) {
            for (Permission permission : permissions) {
                if (permission.getValue() != null) {
                    addPermission(permission.getType(), permission.getTarget(), permission.getValue());
                }
            }
            return this;
        }

        public BasicRoleDefinitionBuilder withPermission(PermissionType permissionType, String target, int access) {
            addPermission(permissionType, target, access);
            return this;
        }

        /**
         * @param entityName there may be an entity name of wildcard here
         * @return an original entity name if the passed {@code entityName} extends other entity, or the {@code entityName}
         * itself otherwise
         */
        protected String evaluateOriginalEntityName(String entityName) {
            //we'll store permissions for original meta class
            MetaClass metaClass = AppBeans.get(Metadata.class).getClass(entityName);
            if (metaClass != null) {
                MetaClass originalMetaClass = AppBeans.get(ExtendedEntities.class).getOriginalMetaClass(metaClass);
                if (originalMetaClass != null) {
                    entityName = originalMetaClass.getName();
                }
            }
            return entityName;
        }

        protected void addPermission(PermissionType permissionType, String target, int access) {
            switch (permissionType) {
                case ENTITY_OP:
                    entityPermissions.getExplicitPermissions().put(target, access);
                    break;
                case ENTITY_ATTR:
                    entityAttributePermissions.getExplicitPermissions().put(target, access);
                    break;
                case SPECIFIC:
                    specificPermissions.getExplicitPermissions().put(target, access);
                    break;
                case SCREEN:
                    screenPermissions.getExplicitPermissions().put(target, access);
                    break;
                case UI:
                    screenComponentPermissions.getExplicitPermissions().put(target, access);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported permission type.");
            }
        }

        public BasicRoleDefinition build() {
            return new BasicRoleDefinition(this);
        }
    }
}
