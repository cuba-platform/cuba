/*
 * Copyright (c) 2008-2017 Haulmont.
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
package com.haulmont.cuba.core.entity;

import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.*;

/**
 * Stores information about:
 * <ul>
 *   <li>data that has been filtered by row level security;
 *   <li>attributes that are hidden, read-only or required for this particular instance.
 * </ul>
 */
public class SecurityState implements Serializable {

    private static final long serialVersionUID = 6613320540189701505L;

    protected transient Multimap<String, Object> filteredData = null;

    protected String[] inaccessibleAttributes;

    protected String[] filteredAttributes;

    protected String[] readonlyAttributes;

    protected String[] requiredAttributes;

    protected String[] hiddenAttributes;

    protected byte[] securityToken;

    public Collection<String> getReadonlyAttributes() {
        return readonlyAttributes != null ? Collections.unmodifiableList(Arrays.asList(readonlyAttributes))
                : Collections.emptyList();
    }

    public Collection<String> getRequiredAttributes() {
        return requiredAttributes != null ? Collections.unmodifiableList(Arrays.asList(requiredAttributes))
                : Collections.emptyList();
    }

    public Collection<String> getHiddenAttributes() {
        return hiddenAttributes != null ? Collections.unmodifiableList(Arrays.asList(hiddenAttributes))
                : Collections.emptyList();
    }
}
