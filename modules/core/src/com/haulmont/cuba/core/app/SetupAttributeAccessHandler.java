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

import com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent;
import com.haulmont.cuba.core.entity.Entity;

/**
 * Interface to be implemented by beans that setup access to attributes of a particular entity instance.
 * The handler should write appropriate attribute names into the event object using
 * {@link com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent#addHidden(String)},
 * {@link com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent#addReadOnly(String)} and
 * {@link com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent#addRequired(String)} methods.
 */
public interface SetupAttributeAccessHandler<T extends Entity> {

    void setupAccess(SetupAttributeAccessEvent<T> event);

    /**
     *
     * @param clazz - entity class
     * @return true if handler supports attribute access for the current entity class
     */
    boolean supports(Class clazz);
}
