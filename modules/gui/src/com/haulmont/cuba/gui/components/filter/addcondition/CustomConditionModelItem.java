/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractCustomConditionDescriptor;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Custom condition node.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class CustomConditionModelItem implements ModelItem {

    private ModelItem parent;
    private AbstractCustomConditionDescriptor descriptor;

    CustomConditionModelItem(ModelItem parent, AbstractCustomConditionDescriptor descriptor) {
        this.parent = parent;
        this.descriptor = descriptor;
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
        return descriptor.getLocCaption();
    }

    @Override
    public AbstractConditionDescriptor getDescriptor() {
        return descriptor;
    }
}
