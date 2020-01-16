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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.role.EntityPermissionsContainer;
import com.haulmont.cuba.security.role.PermissionsUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("cuba_EffectiveEntityPermissionsBuilder")
public class EffectiveEntityPermissionsBuilder {

    @Inject
    protected Metadata metadata;

    @Inject
    protected ServerConfig serverConfig;

    public EntityPermissionsContainer buildEffectivePermissionContainer(EntityPermissionsContainer srcPermissionsContainer) {
        Map<String, Integer> srcExplicitPermissions = srcPermissionsContainer.getExplicitPermissions();
        List<EntityOpDefault> entityOpDefaults = createEntityOpDefaultsList(srcPermissionsContainer);
        EntityPermissionsContainer effectivePermissionsContainer = new EntityPermissionsContainer();
        Map<String, Integer> effectiveExplicitPermissions = effectivePermissionsContainer.getExplicitPermissions();
        for (MetaClass metaClass : getAllMetaClasses()) {
            for (EntityOpDefault entityOpDefault : entityOpDefaults) {
                String target = PermissionsUtils.getEntityOperationTarget(metaClass, entityOpDefault.getEntityOp());
                Integer value = srcExplicitPermissions.get(target);
                effectiveExplicitPermissions.put(target, value != null ? value : entityOpDefault.getDefaultValue());
            }
        }
        return effectivePermissionsContainer;
    }

    protected List<EntityOpDefault> createEntityOpDefaultsList(EntityPermissionsContainer srcPermissionsContainer) {
        List<EntityOpDefault> entityOpDefaults = new ArrayList<>();

        int undefinedPermissionValue = serverConfig.getPermissionUndefinedAccessPolicy() == Access.ALLOW ? 1 : 0;

        int defaultEntityCreatePermissionValue = srcPermissionsContainer.getDefaultEntityCreateAccess() != null ?
                srcPermissionsContainer.getDefaultEntityCreateAccess().getId() :
                undefinedPermissionValue;

        int defaultEntityReadPermissionValue = srcPermissionsContainer.getDefaultEntityReadAccess() != null ?
                srcPermissionsContainer.getDefaultEntityReadAccess().getId() :
                undefinedPermissionValue;

        int defaultEntityUpdatePermissionValue = srcPermissionsContainer.getDefaultEntityUpdateAccess() != null ?
                srcPermissionsContainer.getDefaultEntityUpdateAccess().getId() :
                undefinedPermissionValue;

        int defaultEntityDeletePermissionValue = srcPermissionsContainer.getDefaultEntityDeleteAccess() != null ?
                srcPermissionsContainer.getDefaultEntityDeleteAccess().getId() :
                undefinedPermissionValue;

        entityOpDefaults.add(new EntityOpDefault(EntityOp.CREATE, defaultEntityCreatePermissionValue));
        entityOpDefaults.add(new EntityOpDefault(EntityOp.READ, defaultEntityReadPermissionValue));
        entityOpDefaults.add(new EntityOpDefault(EntityOp.UPDATE, defaultEntityUpdatePermissionValue));
        entityOpDefaults.add(new EntityOpDefault(EntityOp.DELETE, defaultEntityDeletePermissionValue));

        return entityOpDefaults;
    }

    protected List<MetaClass> getAllMetaClasses() {
        Session session = metadata.getSession();
        return session.getModels().stream()
                .flatMap(metaModel -> metaModel.getClasses().stream())
                .filter(metaClass -> !metaClass.getJavaClass().isAnnotationPresent(MappedSuperclass.class))
                .collect(Collectors.toList());
    }

    protected static class EntityOpDefault {
        protected EntityOp entityOp;
        protected Integer defaultValue;

        public EntityOpDefault(EntityOp entityOp, Integer defaultValue) {
            this.entityOp = entityOp;
            this.defaultValue = defaultValue;
        }

        public EntityOp getEntityOp() {
            return entityOp;
        }

        public Integer getDefaultValue() {
            return defaultValue;
        }
    }
}
