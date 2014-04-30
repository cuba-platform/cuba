/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Interface to be implemented by nodes in generic filter condition adding dialogs.
 *
 * @author krivopustov
 * @version $Id$
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