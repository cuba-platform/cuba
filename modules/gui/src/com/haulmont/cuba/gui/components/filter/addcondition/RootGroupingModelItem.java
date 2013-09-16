/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractFilterEditor;
import com.haulmont.cuba.gui.components.filter.GroupType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Root of grouping conditions branch.
 *
 * @author krivopustov
 * @version $Id$
 */
public class RootGroupingModelItem implements ModelItem {

    private AbstractDescriptorBuilder descriptorBuilder;
    private List<ModelItem> modelItems;

    public RootGroupingModelItem(AbstractDescriptorBuilder descriptorBuilder) {
        this.descriptorBuilder = descriptorBuilder;
    }

    @Override
    public ModelItem getParent() {
        return null;
    }

    @Nonnull
    @Override
    public List<ModelItem> getChildren() {
        if (modelItems == null) {
            modelItems = Arrays.<ModelItem>asList(
                    new GroupModelItem(this, GroupType.AND, descriptorBuilder),
                    new GroupModelItem(this, GroupType.OR, descriptorBuilder)
            );
        }
        return modelItems;
    }

    @Override
    public String getCaption() {
        return AppBeans.get(Messages.class).getMessage(AbstractFilterEditor.MESSAGES_PACK, "NewConditionDlg.grouping");
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return null;
    }
}