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

package com.haulmont.cuba.core.app.events;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.contracts.Id;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class AttributeChanges {

    private Set<Change> changes;
    private Map<String, AttributeChanges> embeddedChanges;

    public AttributeChanges() {
        changes = new HashSet<>();
        embeddedChanges = new HashMap<>();
    }

    public AttributeChanges(Set<Change> changes, Map<String, AttributeChanges> embeddedChanges) {
        this.changes = changes;
        this.embeddedChanges = embeddedChanges;
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
            AttributeChanges nestedChanges = embeddedChanges.get(change.name);
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

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getOldValue(String attributeName) {
        String[] properties = attributeName.split("\\.");
        if (properties.length == 1) {
            for (Change change : changes) {
                if (change.name.equals(attributeName))
                    return (T) change.oldValue;
            }
        } else {
            AttributeChanges nestedChanges = embeddedChanges.get(properties[0]);
            if (nestedChanges != null) {
                return nestedChanges.getOldValue(attributeName.substring(attributeName.indexOf(".") + 1));
            }
        }
        return null;
    }

    /**
     * Type-safe method of getting the old value of a reference attribute.
     *
     * @param attributeName reference attribute name
     * @return Id of the referenced object
     */
    @Nullable
    public <E extends Entity<K>, K> Id<E, K> getOldReferenceId(String attributeName) {
        return getOldValue(attributeName);
    }

    /**
     * Type-safe method of getting the old value of a collection attribute.
     * <p>
     * Usage example:
     * <pre>
     * Collection<Id<OrderLine, UUID>> orderLines = event.getChanges().getOldCollection("orderLines", OrderLine.class);
     * for (Id<OrderLine, UUID> orderLineId : orderLines) {
     *     OrderLine orderLine = dataManager.load(orderLineId).one();
     *     // ...
     * }
     * </pre>
     *
     * @param attributeName collection attribute name
     * @param entityClass   class of the attribute
     * @return collection of Ids
     */
    public <E extends Entity<K>, K> Collection<Id<E, K>> getOldCollection(String attributeName, Class<E> entityClass) {
        return getOldValue(attributeName);
    }

    @Override
    public String toString() {
        return "AttributeChanges{"
                + getAttributes().stream()
                        .map(name -> name + ": " + getOldValue(name))
                        .collect(Collectors.joining(","))
                + '}';
    }

    public static class Change {

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
