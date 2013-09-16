/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;

/**
 * Interface used by generic filter editor to get result from the condition adding dialog.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface SelectionHandler {

    void select(AbstractConditionDescriptor descriptor);
}
