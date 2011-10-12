/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
