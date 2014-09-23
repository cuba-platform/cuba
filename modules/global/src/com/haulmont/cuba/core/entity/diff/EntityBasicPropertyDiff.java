/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.diff;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.ViewProperty;

/**
 * @author artamonov
 * @version $Id$
 */
@MetaClass(name = "sys$EntityBasicPropertyDiff")
@SystemLevel
public class EntityBasicPropertyDiff extends EntityPropertyDiff {

    private static final long serialVersionUID = -1532265990429557046L;

    private Object beforeValue;

    private Object afterValue;

    public EntityBasicPropertyDiff(ViewProperty viewProperty, MetaProperty metaProperty,
                                   Object beforeValue, Object afterValue) {
        super(viewProperty, metaProperty);
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
            if (beforeValue instanceof EnumClass)
                return getEnumItemName(beforeValue);
            return String.valueOf(beforeValue);
        }
        return super.getBeforeString();
    }

    @Override
    public String getAfterString() {
        if (afterValue != null) {
            if (afterValue instanceof EnumClass)
                return getEnumItemName(afterValue);
            return String.valueOf(afterValue);
        }
        return super.getAfterString();
    }

    private String getEnumItemName(Object enumItem) {
        String nameKey = enumItem.getClass().getSimpleName() + "." + enumItem.toString();
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMessage(enumItem.getClass(), nameKey);
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
