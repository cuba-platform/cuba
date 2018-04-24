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

package com.haulmont.cuba.core.sys;

import org.eclipse.persistence.config.QueryHints;

import java.util.*;
import java.util.function.Predicate;

public class FetchGroupDescription {
    protected Set<String> fetchGroupAttributes = new TreeSet<>();
    protected Map<String, String> fetchHints = new TreeMap<>(); //sort hints by attribute path
    protected boolean batches;

    public Set<String> getAttributes() {
        return Collections.unmodifiableSet(fetchGroupAttributes);
    }

    public void addAttribute(String attributePath) {
        fetchGroupAttributes.add(attributePath);
    }

    public void addAttributes(List<String> attributePaths) {
        fetchGroupAttributes.addAll(attributePaths);
    }

    public void removeAttributeIf(Predicate<String> filter) {
        fetchGroupAttributes.removeIf(filter);
    }

    public Map<String, String> getHints() {
        return Collections.unmodifiableMap(fetchHints);
    }

    public void addHint(String attributePath, String hint) {
        fetchHints.put(attributePath, hint);
        if (QueryHints.BATCH.equals(hint)) {
            batches = true;
        }
    }

    public boolean hasBatches() {
        return batches;
    }
}
