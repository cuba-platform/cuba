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
import java.util.Map;

public class BasicAccessGroupDefinition implements AccessGroupDefinition {
    protected String name;
    protected String parent;
    protected SetOfAccessConstraints entityConstraints;
    protected Map<String, Serializable> sessionAttributes;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public SetOfAccessConstraints accessConstraints() {
        return entityConstraints;
    }

    public void setEntityConstraints(SetOfAccessConstraints entityConstraints) {
        this.entityConstraints = entityConstraints;
    }

    @Override
    public Map<String, Serializable> sessionAttributes() {
        return sessionAttributes == null ? Collections.emptyMap() : Collections.unmodifiableMap(sessionAttributes);
    }

    public void setSessionAttributes(Map<String, Serializable> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }
}
