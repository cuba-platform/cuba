/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.GroupType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Grouping condition node.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class GroupModelItem implements ModelItem {

    private GroupType groupType;
    private AbstractDescriptorBuilder descriptorBuilder;
    private ModelItem parent;

    GroupModelItem(ModelItem parent, GroupType groupType, AbstractDescriptorBuilder descriptorBuilder) {
        this.parent = parent;
        this.groupType = groupType;
        this.descriptorBuilder = descriptorBuilder;
    }

    @Override
    public ModelItem getParent() {
        return parent;
    }

    @Nonnull
    @Override
    public List<ModelItem> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String getCaption() {
        return MessageProvider.getMessage(groupType);
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return descriptorBuilder.buildGroupConditionDescriptor(groupType);
    }
}
