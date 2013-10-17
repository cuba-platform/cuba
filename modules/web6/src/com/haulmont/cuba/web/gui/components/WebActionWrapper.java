/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Action;

/**
 * {@link Action} adapter for web client.
 *
 * <p>$Id$</p>
 *
 * @author abramov
 */
class WebActionWrapper extends com.vaadin.event.Action {

    private final Action action;

    public WebActionWrapper(Action action) {
        super(""); // don't invoke action.getCaption() here as it may not be properly initialized at the moment
        this.action = action;
    }

    @Override
    public String getCaption() {
        return action.getCaption();
    }
}
