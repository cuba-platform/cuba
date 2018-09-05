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

package com.haulmont.cuba.core.app;

/**
 * Represents sequence with name, store, start value and increment.
 * Default data store for sequence is MAIN data store.
 * Default start value 1, and default increment 1.
 */
public class Sequence {
    protected String name;
    protected String dataStore;
    protected long startValue = 1;
    protected long increment = 1;

    private Sequence(String sequenceName) {
        this.name = sequenceName;
    }

    public static Sequence withName(String sequenceName) {
        return new Sequence(sequenceName);
    }

    public Sequence setStore(String store) {
        this.dataStore = store;
        return this;
    }

    public Sequence setStartValue(long startValue) {
        this.startValue = startValue;
        return this;
    }

    public Sequence setIncrement(long increment) {
        this.increment = increment;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDataStore() {
        return dataStore;
    }

    public long getStartValue() {
        return startValue;
    }

    public long getIncrement() {
        return increment;
    }
}
