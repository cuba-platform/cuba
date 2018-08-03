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

/**
 * An object describing changes in entity attributes.
 *
 * @see EntityChangedEvent#getChanges()
 */
public class AttributeChanges {

    private Set<Change> changes;
    private Map<String, AttributeChanges> embeddedChanges;

    /**
     * INTERNAL.
     */
    public AttributeChanges(Set<Change> changes, Map<String, AttributeChanges> embeddedChanges) {
        this.changes = changes;
        this.embeddedChanges = embeddedChanges;
    }

    /**
     * Returns names of changed attributes for the root entity.
     */
    public Set<String> getOwnAttributes() {
        return changes.stream().map(change -> change.name).collect(Collectors.toSet());
    }

    /**
     * Returns names of changed attributes for the root entity and all its embedded entities (if any).
     * Embedded attributes are represented by dot-separated paths.
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

    /**
     * Returns true if an attribute with the given name is changed.
     * If the attribute is not changed or does not exist at all, returns false.
     */
    public boolean isChanged(String attributeName) {
        for (Change change : changes) {
            if (change.name.equals(attributeName))
                return true;
        }
        return false;
    }

    /**
     * Returns old value of a changed attribute with the given name. Old value can be null.
     * If the attribute is not changed or does not exist at all, returns null.
     * <p>
     * If the attribute is a reference to an entity, its old value is of type {@link Id}. If the attribute is a
     * collection of references, its old value is a collection of {@link Id}s.
     */
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
     * Collection&lt;Id&lt;OrderLine, UUID&gt;&gt; orderLines = event.getChanges().getOldCollection("orderLines", OrderLine.class);
     * for (Id&lt;OrderLine, UUID&gt; orderLineId : orderLines) {
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

    /**
     * INTERNAL.
     * Contains name and old value of a changed attribute.
     */
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
