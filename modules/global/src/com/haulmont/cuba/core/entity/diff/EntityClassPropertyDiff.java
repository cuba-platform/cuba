/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.diff;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.ViewProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
@MetaClass(name = "sys$EntityClassPropertyDiff")
@SystemLevel
public class EntityClassPropertyDiff extends EntityPropertyDiff {
    private static final long serialVersionUID = -6783119655414015181L;

    private Object beforeValue;

    private Object afterValue;

    private ItemState itemState = ItemState.Normal;

    private boolean isLinkChange;

    private List<EntityPropertyDiff> propertyDiffs = new ArrayList<>();

    private String beforeString = "";

    private String afterString = "";

    public EntityClassPropertyDiff(Object beforeValue, Object afterValue,
                                   ViewProperty viewProperty, MetaProperty metaProperty,
                                   boolean linkChange) {
        super(viewProperty, metaProperty);
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
        this.isLinkChange = linkChange;

        if ((afterValue != null) && isLinkChange)
            label = InstanceUtils.getInstanceName((Instance) afterValue);
        else
            label = "";

        if (afterValue != null)
            afterString = InstanceUtils.getInstanceName((Instance) afterValue);

        if (beforeValue != null)
            beforeString = InstanceUtils.getInstanceName((Instance) beforeValue);
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
    public ItemState getItemState() {
        return itemState;
    }

    @Override
    public void setItemState(ItemState itemState) {
        this.itemState = itemState;
    }

    public List<EntityPropertyDiff> getPropertyDiffs() {
        return propertyDiffs;
    }

    public void setPropertyDiffs(List<EntityPropertyDiff> propertyDiffs) {
        this.propertyDiffs = propertyDiffs;
    }

    public boolean isLinkChange() {
        return isLinkChange;
    }

    @Override
    public String getBeforeString() {
        if (itemState != ItemState.Added && isLinkChange)
            return beforeString;
        else
            return "";
    }

    @Override
    public String getAfterString() {
        if (itemState != ItemState.Removed && isLinkChange)
            return afterString;
        else
            return "";
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
    public boolean itemStateVisible() {
        return itemState != ItemState.Normal;
    }

    @Override
    public String getLabel() {
        if (itemState == ItemState.Normal)
            return super.getLabel();
        else
            return "";
    }
}
