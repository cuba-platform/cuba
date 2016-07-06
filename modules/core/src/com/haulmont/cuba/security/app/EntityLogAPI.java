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
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.sys.persistence.EntityAttributeChanges;

import javax.annotation.Nullable;

/**
 * Allows to log entity lifecycle events: create, modify, delete.
 * <p/>
 * Configured by {@link com.haulmont.cuba.security.entity.LoggedEntity} and
 * {@link com.haulmont.cuba.security.entity.LoggedAttribute} entities.
 * See also {@link com.haulmont.cuba.security.app.EntityLogConfig} configuration interface.
 *
 */
public interface EntityLogAPI {

    String NAME = "cuba_EntityLog";

    boolean isEnabled();
    void setEnabled(boolean enabled);

    /**
     * Logs creation of an entity which is configured for manual logging (LoggedEntity.auto == false).
     */
    void registerCreate(Entity entity);

    /**
     * Logs creation of an entity which is configured for auto or manual logging
     * (depending on the {@code auto} parameter).
     */
    void registerCreate(Entity entity, boolean auto);

    /**
     * Logs modification of an entity which is configured for manual logging (LoggedEntity.auto == false).
     */
    void registerModify(Entity entity);

    /**
     * Logs modification of an entity which is configured for auto or manual logging
     * (depending on the {@code auto} parameter).
     */
    void registerModify(Entity entity, boolean auto);


    /**
     * Logs modification of an entity which is configured for auto or manual logging
     * (depending on the {@code auto} parameter).
     * @param changes attribute changes provided by caller
     */
    void registerModify(Entity entity, boolean auto, @Nullable EntityAttributeChanges changes);

    /**
     * Logs deletion of an entity which is configured for manual logging (LoggedEntity.auto == false).
     */
    void registerDelete(Entity entity);

    /**
     * Logs deletion of an entity which is configured for auto or manual logging
     * (depending on the {@code auto} parameter).
     */
    void registerDelete(Entity entity, boolean auto);

    /**
     * Invalidates configuration cache.
     * The configuration will be recreated from the database on next lifecycle event.
     */
    void invalidateCache();

    /**
     * Disables/enables entity logging for current thread.
     * Enabled by default.
     *
     * @param enabled entity logging disabled if false, enabled otherwise.
     */
    void processLoggingForCurrentThread(boolean enabled);
}
