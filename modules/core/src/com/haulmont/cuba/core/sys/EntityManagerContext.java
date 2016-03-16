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
package com.haulmont.cuba.core.sys;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class EntityManagerContext {

    private boolean softDeletion = true;

    private Map<String, Object> dbHints = new HashMap<>();

    private Map<Object, Object> attributes = new HashMap<>();

    public boolean isSoftDeletion() {
        return softDeletion;
    }

    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }

    public Map<String, Object> getDbHints() {
        return dbHints;
    }

    public void setDbHints(Map<String, Object> dbHints) {
        this.dbHints = dbHints;
    }

    public void setAttribute(Object key, Object value) {
        attributes.put(key, value);
    }

    @Nullable
    public <T> T getAttribute(Object key) {
        return (T) attributes.get(key);
    }
}
