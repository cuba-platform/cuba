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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.role.EntityAttributePermissionsContainer;
import com.haulmont.cuba.security.role.PermissionsUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.MappedSuperclass;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("cuba_EffectiveEntityAttributePermissionsBuilder")
public class EffectiveEntityAttributePermissionsBuilder {

    @Inject
    protected Metadata metadata;

    @Inject
    protected ServerConfig serverConfig;

    public EntityAttributePermissionsContainer buildEffectivePermissionContainer(EntityAttributePermissionsContainer container) {
        Integer undefinedPermissionValue = serverConfig.getPermissionUndefinedAccessPolicy() == Access.ALLOW ?
                EntityAttrAccess.MODIFY.getId() :
                EntityAttrAccess.DENY.getId();

        Integer defaultPermissionValue = container.getDefaultEntityAttributeAccess() != null ?
                container.getDefaultEntityAttributeAccess().getId() :
                undefinedPermissionValue;

        Map<String, Integer> srcExplicitPermissions = container.getExplicitPermissions();
        EntityAttributePermissionsContainer effectivePermissionsContainer = new EntityAttributePermissionsContainer();
        Map<String, Integer> effectiveExplicitPermissions = effectivePermissionsContainer.getExplicitPermissions();
        for (MetaClass metaClass : getAllMetaClasses()) {
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                String target = PermissionsUtils.getEntityAttributeTarget(metaClass, metaProperty.getName());
                Integer value = srcExplicitPermissions.get(target);
                effectiveExplicitPermissions.put(target, value != null ? value : defaultPermissionValue);
            }
        }
        return effectivePermissionsContainer;
    }

    protected List<MetaClass> getAllMetaClasses() {
        Session session = metadata.getSession();
        return session.getModels().stream()
                .flatMap(metaModel -> metaModel.getClasses().stream())
                .filter(metaClass -> !metaClass.getJavaClass().isAnnotationPresent(MappedSuperclass.class))
                .collect(Collectors.toList());
    }
}
