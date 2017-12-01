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
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;

import java.util.Collection;

@MetaClass(name = "sys$EntityBasicPropertyDiff")
@SystemLevel
public class EntityBasicPropertyDiff extends EntityPropertyDiff {

    private static final long serialVersionUID = -1532265990429557046L;

    private Object beforeValue;

    private Object afterValue;

    public EntityBasicPropertyDiff(Object beforeValue,
                                   Object afterValue,
                                   MetaProperty metaProperty) {
        super(metaProperty);
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
    }

    @Override
    public boolean hasStateValues() {
        return true;
    }

    @Override
    public Object getBeforeValue() {
        return beforeValue;
    }

    @Override
    public Object getAfterValue() {
        return afterValue;
    }

    @Override
    public String getLabel() {
        if (afterValue != null)
            return afterValue.toString();
        else
            return super.getLabel();
    }

    @Override
    public String getBeforeString() {
        if (beforeValue != null) {
            if (beforeValue instanceof EnumClass) {
                return getEnumItemName(beforeValue);
            } else if (beforeValue instanceof Collection) {
                return getCollectionString(beforeValue);
            }
            return String.valueOf(beforeValue);
        }
        return super.getBeforeString();
    }

    @Override
    public String getAfterString() {
        if (afterValue != null) {
            if (afterValue instanceof EnumClass) {
                return getEnumItemName(afterValue);
            } else if (afterValue instanceof Collection) {
                return getCollectionString(afterValue);
            }
            return String.valueOf(afterValue);
        }
        return super.getAfterString();
    }

    private String getEnumItemName(Object enumItem) {
        String nameKey = enumItem.getClass().getSimpleName() + "." + enumItem.toString();
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMessage(enumItem.getClass(), nameKey);
    }

    private String getCollectionString(Object collection) {
        if (DynamicAttributesUtils.isDynamicAttribute(propertyName)) {
            Metadata metadata = AppBeans.get(Metadata.class);
            com.haulmont.chile.core.model.MetaClass metaClass = metadata.getClassNN(metaClassName);
            MetaPropertyPath path = DynamicAttributesUtils.getMetaPropertyPath(metaClass, propertyName);
            return metadata.getTools().format(collection, path.getMetaProperty());
        }
        return String.valueOf(beforeValue);
    }

    @Override
    public String getBeforeCaption() {
        String value = getBeforeString();
        if (value.length() > CAPTION_CHAR_COUNT)
            return value.substring(0, CAPTION_CHAR_COUNT) + "...";
        return super.getBeforeCaption();
    }

    @Override
    public String getAfterCaption() {
        String value = getAfterString();
        if (value.length() > CAPTION_CHAR_COUNT)
            return value.substring(0, CAPTION_CHAR_COUNT) + "...";
        return super.getAfterCaption();
    }

    @Override
    public ItemState getItemState() {
        return ItemState.Modified;
    }
}