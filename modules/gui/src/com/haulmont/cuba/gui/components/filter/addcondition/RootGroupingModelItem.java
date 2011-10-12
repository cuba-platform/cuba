/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractFilterEditor;
import com.haulmont.cuba.gui.components.filter.GroupType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Root of grouping conditions branch.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RootGroupingModelItem implements ModelItem {

    private AbstractDescriptorBuilder descriptorBuilder;

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
        return Arrays.<ModelItem>asList(
                new GroupModelItem(this, GroupType.AND, descriptorBuilder),
                new GroupModelItem(this, GroupType.OR, descriptorBuilder)
        );
    }

    @Override
    public String getCaption() {
        return MessageProvider.getMessage(AbstractFilterEditor.MESSAGES_PACK, "NewConditionDlg.grouping");
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return null;
    }
}
