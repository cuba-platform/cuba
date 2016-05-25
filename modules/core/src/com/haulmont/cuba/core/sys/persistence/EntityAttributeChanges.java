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

import org.eclipse.persistence.internal.sessions.ObjectChangeSet;
import org.eclipse.persistence.sessions.changesets.ChangeRecord;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * INTERNAL. Accumulates changes in entity attributes.
 */
public class EntityAttributeChanges {

    private Set<Change> changes = new HashSet<>();

    public void addChanges(ObjectChangeSet changeSet) {
        if (changeSet == null)
            return;
        for (ChangeRecord changeRecord : changeSet.getChanges()) {
            changes.add(new Change(changeRecord.getAttribute(), changeRecord.getOldValue()));
        }
    }

    public Set<String> getAttributes() {
        return changes.stream().map(change -> change.name).collect(Collectors.toSet());
    }

    @Nullable
    public Object getOldValue(String attributeName) {
        for (Change change : changes) {
            if (change.name.equals(attributeName))
                return change.oldValue;
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
