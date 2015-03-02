/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.gui.components.ListComponent;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class ItemTrackingAction extends BaseAction {

    public ItemTrackingAction(String id) {
        this(null, id);
    }

    protected ItemTrackingAction(ListComponent target, String id) {
        super(id, null);

        this.target = target;
    }

    @Override
    protected boolean isApplicable() {
        return target != null && !target.getSelected().isEmpty();
    }
}