/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 10.04.2009 17:07:58
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import org.apache.commons.lang.ObjectUtils;

public abstract class ActionWrapper extends AbstractAction {
    protected Action action;

    protected ActionWrapper(String id) {
        super(id);
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if (action != null && !ObjectUtils.equals(getId(), action.getId())) {
            throw new IllegalStateException("Action have different id");
        }
        this.action = action;
    }

    @Override
    public String getId() {
        if (action != null) return action.getId();
        return super.getId();
    }

    @Override
    public String getCaption() {
        if (action != null) return action.getCaption();
        return super.getCaption();
    }

    @Override
    public boolean isEnabled() {
        if (action != null) return action.isEnabled();
        return super.isEnabled();
    }
}
