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
package com.haulmont.cuba.core.sys.listener;

import com.haulmont.cuba.core.listener.*;

public enum EntityListenerType {

    BEFORE_DETACH(BeforeDetachEntityListener.class),
    BEFORE_ATTACH(BeforeAttachEntityListener.class),
    BEFORE_INSERT(BeforeInsertEntityListener.class),
    AFTER_INSERT(AfterInsertEntityListener.class),
    BEFORE_UPDATE(BeforeUpdateEntityListener.class),
    AFTER_UPDATE(AfterUpdateEntityListener.class),
    AFTER_DELETE(AfterDeleteEntityListener.class),
    BEFORE_DELETE(BeforeDeleteEntityListener.class);

    private final Class listenerInterface;

    private EntityListenerType(Class listenerInterface) {
        this.listenerInterface = listenerInterface;
    }

    public Class getListenerInterface() {
        return listenerInterface;
    }
}