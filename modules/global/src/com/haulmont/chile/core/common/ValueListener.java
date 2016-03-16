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

package com.haulmont.chile.core.common;

/**
 * Interface to track changes in data model objects.
 *
 * @deprecated Use {@link com.haulmont.chile.core.model.Instance.PropertyChangeListener}
 */
@Deprecated
public interface ValueListener {

    /**
     * Called by a data model object when an attribute changes.
     *
     * @param item      data model object instance
     * @param property  changed attribute name
     * @param prevValue previous value
     * @param value     current value
     */
    void propertyChanged(Object item, String property, Object prevValue, Object value);
}