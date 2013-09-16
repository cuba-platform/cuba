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
 * Root of runtime properties branch.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RootRuntimePropertiesModelItem implements ModelItem {

    private AbstractConditionDescriptor descriptor;

    public RootRuntimePropertiesModelItem(AbstractDescriptorBuilder descriptorBuilder) {
        descriptor = descriptorBuilder.buildRuntimePropConditionDescriptor();
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
        return descriptor.getLocCaption();
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return descriptor;
    }
}
