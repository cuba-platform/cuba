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
 *
 */

package com.haulmont.cuba.gui.components.autocomplete.impl;

import com.haulmont.cuba.core.sys.jpql.InferredType;

import java.util.Set;

public class HintRequest {
    private int position;
    private String query;
    private Set<InferredType> expectedTypes;

    public HintRequest() {
    }

    public int getPosition() {
        return position;
    }

    public String getQuery() {
        return query;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Set<InferredType> getExpectedTypes() {
        return expectedTypes;
    }

    public void setExpectedTypes(Set<InferredType> expectedTypes) {
        this.expectedTypes = expectedTypes;
    }
}