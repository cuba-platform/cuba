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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstancePropertyContainer;

public class InstancePropertyContainerImpl<E extends Entity>
        extends InstanceContainerImpl<E> implements InstancePropertyContainer<E> {

    protected InstanceContainer parent;
    protected String property;

    public InstancePropertyContainerImpl(MetaClass metaClass, InstanceContainer parent, String property) {
        super(metaClass);
        this.parent = parent;
        this.property = property;
    }

    @Override
    public InstanceContainer getParent() {
        return parent;
    }

    @Override
    public String getProperty() {
        return property;
    }
}
