/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Interface to be implemented by nodes in generic filter condition adding dialogs.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ModelItem {

    @Nullable
    ModelItem getParent();

    @Nonnull
    List<ModelItem> getChildren();

    String getCaption();

    @Nullable
    AbstractConditionDescriptor getDescriptor();
}
