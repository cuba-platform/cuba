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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstancePropertyContainer;

import javax.annotation.Nullable;

public class InstancePropertyContainerImpl<E extends Entity>
        extends InstanceContainerImpl<E> implements InstancePropertyContainer<E> {

    protected InstanceContainer master;
    protected String property;

    public InstancePropertyContainerImpl(MetaClass metaClass, InstanceContainer master, String property) {
        super(metaClass);
        this.master = master;
        this.property = property;
    }

    @Override
    public InstanceContainer getMaster() {
        return master;
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public void setItem(@Nullable E item) {
        super.setItem(item);
        Entity masterItem = master.getItemOrNull();
        if (masterItem != null) {
            MetaProperty masterProperty = getMasterProperty();
            Entity masterValue = masterItem.getValue(masterProperty.getName());
            if (masterValue != item) {
                mute();
                try {
                    masterItem.setValue(masterProperty.getName(), item);
                } finally {
                    unmute();
                }
            }
        }
    }

    protected MetaProperty getMasterProperty() {
        MetaClass masterMetaClass = master.getEntityMetaClass();
        MetaProperty masterProperty = masterMetaClass.getPropertyNN(property);
        if (!masterProperty.getRange().isClass() || masterProperty.getRange().getCardinality().isMany()) {
            throw new IllegalStateException(String.format("Property '%s' is not a single-value reference", property));
        }
        return masterProperty;
    }
}
