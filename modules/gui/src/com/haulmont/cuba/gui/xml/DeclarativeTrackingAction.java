/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;

import javax.annotation.Nullable;

/**
 * @author artamonov
 * @version $Id$
 */
public class DeclarativeTrackingAction extends DeclarativeAction implements Action.HasTarget, Action.UiPermissionAware {

    public DeclarativeTrackingAction(String id, String caption, String description, String icon, String enable, String visible,
                                     String methodName, @Nullable String shortcut, Component.ActionsHolder holder) {
        super(id, caption, description, icon, enable, visible, methodName, shortcut, holder);
    }

    @Override
    protected boolean isApplicable() {
        return target != null && !target.getSelected().isEmpty();
    }
}