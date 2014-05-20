/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.button.CubaButtonClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.button.CubaButtonState;
import com.vaadin.shared.MouseEventDetails;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaButton extends com.vaadin.ui.Button {

    public CubaButton() {
    }

    public CubaButton(String caption) {
        super(caption);
    }

    public CubaButton(String caption, ClickListener listener) {
        super(caption, listener);
    }

    @Override
    protected CubaButtonState getState() {
        return (CubaButtonState) super.getState();
    }

    @Override
    protected CubaButtonState getState(boolean markAsDirty) {
        return (CubaButtonState) super.getState(markAsDirty);
    }

    @Override
    protected void fireClick(MouseEventDetails details) {
        try {
            super.fireClick(details);
        } finally {
            if (getState(false).useResponsePending) {
                getRpcProxy(CubaButtonClientRpc.class).onClickHandled();
            }
        }
    }

    public boolean isUseResponsePending() {
        return getState(false).useResponsePending;
    }

    public void setUseResponsePending(boolean useResponsePending) {
        if (isUseResponsePending() != useResponsePending) {
            getState().useResponsePending = useResponsePending;
        }
    }
}