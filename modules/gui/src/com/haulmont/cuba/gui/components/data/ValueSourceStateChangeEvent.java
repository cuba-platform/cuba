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

package com.haulmont.cuba.gui.components.data;

import java.util.EventObject;

public class ValueSourceStateChangeEvent<V> extends EventObject {
    protected ValueSourceState status;

    public ValueSourceStateChangeEvent(ValueSource<V> source, ValueSourceState status) {
        super(source);
        this.status = status;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ValueSource<V> getSource() {
        return (ValueSource<V>) super.getSource();
    }

    public ValueSourceState getState() {
        return status;
    }
}