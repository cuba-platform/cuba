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

import javax.annotation.Nullable;

/**
 * Data-aware component that supports data binding with {@link ValueSource}.
 *
 * @param <V> value type
 */
public interface HasValueSource<V> {
    /**
     * Sets value source for component.
     *
     * @param valueSource value source
     */
    void setValueSource(@Nullable ValueSource<V> valueSource);

    /**
     * @return currently bound value source
     */
    @Nullable
    ValueSource<V> getValueSource();
}