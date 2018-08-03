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
 */

package com.haulmont.cuba.core.sys.persistence;

import org.eclipse.persistence.sessions.changesets.AggregateChangeRecord;
import org.eclipse.persistence.sessions.changesets.ChangeRecord;
import org.eclipse.persistence.sessions.changesets.ObjectChangeSet;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * INTERNAL. Accumulates changes in entity attributes.
 */
public class EntityAttributeChanges {

    private Set<Change> changes = new HashSet<>();
    private Map<String, EntityAttributeChanges> embeddedChanges = new HashMap<>();

    public void addChanges(ObjectChangeSet changeSet) {
        if (changeSet == null)
            return;
        for (ChangeRecord changeRecord : changeSet.getChanges()) {
            changes.add(new Change(changeRecord.getAttribute(), changeRecord.getOldValue()));
            if (changeRecord instanceof AggregateChangeRecord) {
                embeddedChanges.computeIfAbsent(changeRecord.getAttribute(), s -> {
                    EntityAttributeChanges embeddedChanges = new EntityAttributeChanges();
                    embeddedChanges.addChanges(((AggregateChangeRecord) changeRecord).getChangedObject());
                    return embeddedChanges;
                });
            }
        }
    }

    /**
     * @return changed attributes names for current entity
     */
    public Set<String> getOwnAttributes() {
        return changes.stream().map(change -> change.name).collect(Collectors.toSet());
    }

    /**
     * @return changed attributes names for current entity and all embedded entities
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

    public boolean isChanged(String attributeName) {
        for (Change change : changes) {
            if (change.name.equals(attributeName))
                return true;
        }
        return false;
    }

    @Nullable
    public <T> T getOldValue(String attributeName) {
        for (Change change : changes) {
            if (change.name.equals(attributeName))
                return (T) change.oldValue;
        }
        return null;
    }

    @Nullable
    public <T> T getOldValueEx(String attributePath) {
        String[] properties = attributePath.split("[.]");
        if (properties.length == 1) {
            for (Change change : changes) {
                if (change.name.equals(attributePath))
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
