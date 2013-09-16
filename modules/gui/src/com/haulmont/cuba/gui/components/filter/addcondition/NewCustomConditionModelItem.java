/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Custom condition creation node.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class NewCustomConditionModelItem implements ModelItem {

    private AbstractConditionDescriptor conditionCreator;

    public NewCustomConditionModelItem(AbstractDescriptorBuilder descriptorBuilder) {
        conditionCreator = descriptorBuilder.buildCustomConditionDescriptor();
    }

    @Override
    public ModelItem getParent() {
        return null;
    }

    @Nonnull
    @Override
    public List<ModelItem> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String getCaption() {
        return conditionCreator.getLocCaption();
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return conditionCreator;
    }
}
