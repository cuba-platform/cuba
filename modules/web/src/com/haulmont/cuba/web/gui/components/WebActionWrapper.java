/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Action;

/**
 * {@link Action} adapter for web client.
 *
 * @author abramov
 * @version $Id$
 */
class WebActionWrapper extends com.vaadin.event.Action {

    private final Action action;

    public WebActionWrapper(Action action) {
        super(""); // don't invoke action.getCaption() here as it may not be properly initialized at the moment
        this.action = action;
    }

    @Override
    public String getCaption() {
        StringBuilder sb = new StringBuilder();
        sb.append(action.getCaption());
        if (action.getShortcut() != null) {
            sb.append(" (").append(action.getShortcut().format()).append(")");
        }
        return sb.toString();
    }
}