/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.entity.Entity;
import org.eclipse.persistence.descriptors.changetracking.ChangeTracker;
import org.eclipse.persistence.internal.descriptors.changetracking.AttributeChangeListener;
import org.eclipse.persistence.sessions.changesets.AggregateChangeRecord;
import org.eclipse.persistence.sessions.changesets.ChangeRecord;
import org.eclipse.persistence.sessions.changesets.ObjectChangeSet;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Accumulates changes in entity attributes.
 * The {@code #addChanges(Entity entity)} method is used by EntityLog when registering
 * changes for the whole entity automatically.
 * You can add changes for some attributes by calling
 * {@link EntityAttributeChanges#addChange(String, Object)} and calling EntityLog programmatically.
 */
public class EntityAttributeChanges {

    protected Set<Change> changes = new HashSet<>();
    protected Map<String, EntityAttributeChanges> embeddedChanges = new HashMap<>();

    /**
     * Add change for the given entity attribute.
     *
     * @param attributeName - attribute name
     * @param oldValue      - old attribute value
     */
    public void addChange(String attributeName, Object oldValue) {
        changes.add(new Change(attributeName, oldValue));
    }

    /**
     * Adds changes for the embedded entity attribute.
     *
     * @param attributeName - attribute name
     * @param changes       - {@link EntityAttributeChanges} changes for embedded object
     */
    public void addEmbeddedChanges(String attributeName, EntityAttributeChanges changes) {
        embeddedChanges.put(attributeName, changes);
    }

    /**
     * Accumulates changes for the entity. Stores changed attribute names and old values.
     */
    public void addChanges(Entity entity) {
        if (!(entity instanceof ChangeTracker))
            return;

        AttributeChangeListener changeListener =
                (AttributeChangeListener) ((ChangeTracker) entity)._persistence_getPropertyChangeListener();

        if (changeListener == null)
            return;

        addChanges(changeListener.getObjectChangeSet());
    }

    /**
     * @return changed attributes names for current entity.
     */
    public Set<String> getOwnAttributes() {
        return changes.stream().map(change -> change.name).collect(Collectors.toSet());
    }

    /**
     * @return changed attributes names for current entity and all embedded entities.
     */
    public Set<String> getAttributes() {
        Set<String> attributes = new HashSet<>();
        for (Change change : changes) {
            EntityAttributeChanges nestedChanges = embeddedChanges.get(change.name);
            if (nestedChanges == null) {
                attributes.add(change.name);
            } else {
                for (String attribute : nestedChanges.getAttributes()) {
                    attributes.add(String.format("%s.%s", change.name, attribute));
                }
            }
        }
        return attributes;
    }

    /**
     * @return returns {@link EntityAttributeChanges} for an embedded attribute.
     */
    public EntityAttributeChanges getEmbeddedChanges(String attributeName) {
        return embeddedChanges.get(attributeName);
    }

    /**
     * @return true if attribute is changed.
     */
    public boolean isChanged(String attributeName) {
        for (Change change : changes) {
            if (change.name.equals(attributeName))
                return true;
        }
        return false;
    }

    /**
     * @return old value for changed own attribute.
     */
    @Nullable
    public <T> T getOldValue(String attributeName) {
        for (Change change : changes) {
            if (change.name.equals(attributeName))
                //noinspection unchecked
                return (T) change.oldValue;
        }
        return null;
    }

    /**
     * @return old value for changed attribute.
     * Includes changed embedded attributes.
     */
    @Nullable
    public <T> T getOldValueEx(String attributePath) {
        String[] properties = attributePath.split("[.]");
        if (properties.length == 1) {
            for (Change change : changes) {
                if (change.name.equals(attributePath))
                    //noinspection unchecked
                    return (T) change.oldValue;
            }
        } else {
            EntityAttributeChanges nestedChanges = embeddedChanges.get(properties[0]);
            if (nestedChanges != null) {
                return nestedChanges.getOldValueEx(attributePath.substring(attributePath.indexOf(".") + 1));
            }
        }
        return null;
    }

    protected void addChanges(ObjectChangeSet changeSet) {
        if (changeSet == null)
            return;

        for (ChangeRecord changeRecord : changeSet.getChanges()) {
            addChange(changeRecord.getAttribute(), changeRecord.getOldValue());
            if (changeRecord instanceof AggregateChangeRecord) {
                embeddedChanges.computeIfAbsent(changeRecord.getAttribute(), s -> {
                    EntityAttributeChanges embeddedChanges = new EntityAttributeChanges();
                    embeddedChanges.addChanges(((AggregateChangeRecord) changeRecord).getChangedObject());
                    return embeddedChanges;
                });
            }
        }
    }

    private static class Change {

        public final String name;
        public final Object oldValue;

        public Change(String name, Object oldValue) {
            this.name = name;
            this.oldValue = oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Change that = (Change) o;

            return name.equals(that.name);

        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
