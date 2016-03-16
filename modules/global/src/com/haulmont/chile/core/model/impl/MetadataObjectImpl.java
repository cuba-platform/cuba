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

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.model.MetadataObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 */
@SuppressWarnings({"TransientFieldNotInitialized"})
public abstract class MetadataObjectImpl implements MetadataObject, Serializable {

    private static final long serialVersionUID = 5179324236413815312L;

    protected String name;

    private transient Map<String, Object> annotations = new HashMap<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getAnnotations() {
        return annotations;
    }

    public void setName(String name) {
        this.name = name;
    }
}