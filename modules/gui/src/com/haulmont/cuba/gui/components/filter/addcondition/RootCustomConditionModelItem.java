/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
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
 * @author krivopustov
 * @version $Id$
 */
public class RootCustomConditionModelItem implements ModelItem {

    private List<AbstractConditionDescriptor> propertyDescriptors;
    private List<ModelItem> modelItems;

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
        if (modelItems == null) {
            modelItems = new ArrayList<>();

            for (AbstractConditionDescriptor descriptor : propertyDescriptors) {
                if (descriptor instanceof AbstractCustomConditionDescriptor) {
                    modelItems.add(new CustomConditionModelItem(this, (AbstractCustomConditionDescriptor) descriptor));
                }
            }

            Collections.sort(modelItems, new ModelItemComparator());
        }
        return modelItems;
    }

    @Override
    public String getCaption() {
        return AppBeans.get(Messages.class).getMessage(AbstractFilterEditor.MESSAGES_PACK, "NewConditionDlg.specialConditions");
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return null;
    }
}