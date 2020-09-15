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

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.ResourceHolderSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Holds entities being saved by {@code RdbmsStore} during a transaction.
 * <p>
 * It ensures that {@code commit()} returns last saved entities regardless of how many saves where performed in
 * listeners.
 */
public class SavedEntitiesHolder extends ResourceHolderSupport {

    private static final Logger log = LoggerFactory.getLogger(SavedEntitiesHolder.class);

    public static final String RESOURCE_KEY = "cuba-SavedEntitiesHolder";

    private Set<Entity> entities = new HashSet<>();

    public static SavedEntitiesHolder setEntities(Set<Entity> saved) {
        SavedEntitiesHolder holder = (SavedEntitiesHolder) TransactionSynchronizationManager.getResource(RESOURCE_KEY);

        if (holder == null) {
            holder = new SavedEntitiesHolder();
            TransactionSynchronizationManager.bindResource(RESOURCE_KEY, holder);

            holder.setSynchronizedWithTransaction(true);
            TransactionSynchronizationManager.registerSynchronization(new Synchronization(holder, RESOURCE_KEY));
            log.trace("Created {}", holder);
        }

        log.trace("{} updates entities: {}", holder, saved);
        for (Entity entity : saved) {
            holder.entities.remove(entity);
            holder.entities.add(entity);
        }

        return holder;
    }

    public Set<Entity> getEntities(Set<Entity> saved) {
        Set<Entity> result = new HashSet<>(entities);
        result.retainAll(saved);
        return result;
    }

    private void release() {
        log.trace("Released {}", this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }

    private static class Synchronization extends ResourceHolderSynchronization<SavedEntitiesHolder, String> {

        public Synchronization(SavedEntitiesHolder resourceHolder, String resourceKey) {
            super(resourceHolder, resourceKey);
        }

        @Override
        protected boolean shouldReleaseBeforeCompletion() {
            return false;
        }

        @Override
        protected void releaseResource(SavedEntitiesHolder resourceHolder, String resourceKey) {
            resourceHolder.release();
        }
    }

}
