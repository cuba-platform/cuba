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

package com.haulmont.cuba.security.group;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class BasicSetOfAccessConstraints implements SetOfAccessConstraints, Serializable {
    private static final long serialVersionUID = 2265242471010129743L;

    protected Map<String, List<AccessConstraint>> constraints;

    @Override
    public Set<String> getEntityTypes() {
        return constraints ==  null ? Collections.emptySet() : constraints.keySet();
    }

    @Override
    public Stream<AccessConstraint> findConstraintsByEntity(String entityName) {
        if (constraints == null) {
            return Stream.empty();
        }
        List<AccessConstraint> result = constraints.get(entityName);
        return result != null ? result.stream() : Stream.empty();
    }

    public boolean exists() {
        return constraints != null && !constraints.isEmpty();
    }

    public Map<String, List<AccessConstraint>> getConstraints() {
        return constraints;
    }

    public void setConstraints(Map<String, List<AccessConstraint>> constraints) {
        this.constraints = constraints;
    }
}
