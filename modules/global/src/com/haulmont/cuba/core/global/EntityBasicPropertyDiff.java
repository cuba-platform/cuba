/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@MetaClass(name = "core$EntityBasicPropertyDiff")
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
        if (beforeValue != null)
            return String.valueOf(beforeValue);
        return super.getBeforeString();
    }

    @Override
    public String getAfterString() {
        if (afterValue != null)
            return String.valueOf(afterValue);
        return super.getAfterString();
    }

    @Override
    public ItemState getItemState() {
        return ItemState.Modified;
    }
}
