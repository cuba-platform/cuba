/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;

/**
 * @author artamonov
 * @version $Id$
 */
public class ItemTrackingAction extends BaseAction {

    public ItemTrackingAction(String id) {
        this(null, id);
    }

    protected ItemTrackingAction(ListComponent owner, String id) {
        super(owner, id, null);
    }

    @Override
    public void actionPerform(Component component) {
    }

    @Override
    protected boolean isApplicable() {
        return !getTargetSelection().isEmpty();
    }
}