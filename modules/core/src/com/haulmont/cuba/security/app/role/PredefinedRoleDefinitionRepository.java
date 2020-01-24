/*
 * Copyright (c) 2008-2020 Haulmont.
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

import com.haulmont.cuba.security.role.BasicRoleDefinition;
import com.haulmont.cuba.security.role.RoleDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The repository of role definitions built from classes annotated with the {@link
 * com.haulmont.cuba.security.app.role.annotation.Role}.
 */
@Component("cuba_PredefinedRoleDefinitionRepository")
public class PredefinedRoleDefinitionRepository {

    @Inject
    protected List<RoleDefinition> predefinedRoleDefinitionBeans;

    protected Map<String, RoleDefinition> predefinedRoleDefinitionsMap = new HashMap<>();

    protected volatile boolean initialized;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Logger log = LoggerFactory.getLogger(PredefinedRoleDefinitionRepository.class);

    public Collection<RoleDefinition> getRoleDefinitions() {
        lock.readLock().lock();
        try {
            checkInitialized();
            return new ArrayList<>(predefinedRoleDefinitionsMap.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Nullable
    public RoleDefinition getRoleDefinitionByName(String name) {
        lock.readLock().lock();
        try {
            checkInitialized();
            RoleDefinition roleDefinition = predefinedRoleDefinitionsMap.get(name);
            return roleDefinition != null ?
                    copyRoleDefinition(roleDefinition) :
                    null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void registerRoleDefinition(RoleDefinition roleDefinition) {
        lock.writeLock().lock();
        try {
            RoleDefinition roleDefCopy = copyRoleDefinition(roleDefinition);
            predefinedRoleDefinitionsMap.put(roleDefCopy.getName(), roleDefCopy);
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected void checkInitialized() {
        if (!initialized) {
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if (!initialized) {
                    log.info("Initializing predefined role definitions");
                    init();
                    initialized = true;
                }
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        }
    }

    protected void init() {
        for (RoleDefinition predefinedRoleDefinitionBean : predefinedRoleDefinitionBeans) {
            RoleDefinition roleDefCopy = copyRoleDefinition(predefinedRoleDefinitionBean);
            predefinedRoleDefinitionsMap.put(roleDefCopy.getName(), roleDefCopy);
        }
    }

    protected RoleDefinition copyRoleDefinition(RoleDefinition sourceRoleDef) {
        return BasicRoleDefinition.builder()
                .withEntityPermissions(sourceRoleDef.entityPermissions())
                .withEntityAttributePermissions(sourceRoleDef.entityAttributePermissions())
                .withScreenPermissions(sourceRoleDef.screenPermissions())
                .withSpecificPermissions(sourceRoleDef.specificPermissions())
                .withScreenElementsPermissions(sourceRoleDef.screenComponentPermissions())
                .withName(sourceRoleDef.getName())
                .withLocName(sourceRoleDef.getLocName())
                .withDescription(sourceRoleDef.getDescription())
                .withIsDefault(sourceRoleDef.isDefault())
                .withSecurityScope(sourceRoleDef.getSecurityScope())
                .build();
    }

    public void reset() {
        lock.writeLock().lock();
        try {
            predefinedRoleDefinitionsMap.clear();
            initialized = false;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
