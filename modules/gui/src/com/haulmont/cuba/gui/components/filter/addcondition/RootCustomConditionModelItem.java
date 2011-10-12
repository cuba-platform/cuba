/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractCustomConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractFilterEditor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Root of special conditions branch.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RootCustomConditionModelItem implements ModelItem {

    private List<AbstractConditionDescriptor> propertyDescriptors;

    public RootCustomConditionModelItem(List<AbstractConditionDescriptor> propertyDescriptors) {
        this.propertyDescriptors = propertyDescriptors;
    }

    @Override
    public ModelItem getParent() {
        return null;
    }

    @Nonnull
    @Override
    public List<ModelItem> getChildren() {
        List<ModelItem> list = new ArrayList<ModelItem>();

        for (AbstractConditionDescriptor descriptor : propertyDescriptors) {
            if (descriptor instanceof AbstractCustomConditionDescriptor) {
                list.add(new CustomConditionModelItem(this, (AbstractCustomConditionDescriptor) descriptor));
            }
        }

        Collections.sort(list, new ModelItemComparator());
        return list;
    }

    @Override
    public String getCaption() {
        return MessageProvider.getMessage(AbstractFilterEditor.MESSAGES_PACK, "NewConditionDlg.specialConditions");
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return null;
    }
}
