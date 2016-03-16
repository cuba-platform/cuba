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

package com.haulmont.cuba.core.entity.diff;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.ViewProperty;

import java.io.Serializable;

/**
 * Diff between properties in entity snapshots
 *
 */
@MetaClass(name = "sys$EntityPropertyDiff")
@SystemLevel
public abstract class EntityPropertyDiff extends AbstractNotPersistentEntity implements Serializable {

    protected static final int CAPTION_CHAR_COUNT = 30;

    public enum ItemState {
        Normal,
        Modified,
        Added,
        Removed
    }

    private static final long serialVersionUID = -6467322033937742101L;

    private ViewProperty viewProperty;
    protected String propertyCaption;
    protected String label = "";
    protected String metaClassName = "";

    protected EntityPropertyDiff(ViewProperty viewProperty, com.haulmont.chile.core.model.MetaProperty metaProperty) {
        this.viewProperty = viewProperty;
        MessageTools messageTools = AppBeans.get(MessageTools.NAME);
        this.propertyCaption = messageTools.getPropertyCaption(metaProperty);
        this.metaClassName = metaProperty.getDomain().getName();
    }

    public ViewProperty getViewProperty() {
        return viewProperty;
    }

    public String getMetaClassName() {
        return metaClassName;
    }

    @MetaProperty
    public String getName() {
        return propertyCaption;
    }

    @MetaProperty
    public void setName(String name) {
        propertyCaption = name;
    }

    @MetaProperty
    public String getLabel() {
        return label;
    }

    @MetaProperty
    public void setLabel(String label) {
        this.label = label;
    }

    public boolean hasStateValues() {
        return false;
    }

    public Object getBeforeValue() {
        return null;
    }

    public Object getAfterValue() {
        return null;
    }

    @MetaProperty
    public String getBeforeString() {
        return "";
    }

    @MetaProperty
    public String getAfterString() {
        return "";
    }

    @MetaProperty
    public String getBeforeCaption() {
        return getBeforeString();
    }

    @MetaProperty
    public String getAfterCaption() {
        return getAfterString();
    }

    @MetaProperty
    public ItemState getItemState() {
        return ItemState.Normal;
    }

    @MetaProperty
    public void setItemState(ItemState itemState) {

    }

    public boolean itemStateVisible() {
        return false;
    }
}