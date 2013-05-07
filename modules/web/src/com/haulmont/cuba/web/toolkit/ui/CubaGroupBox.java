/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.groupbox.CubaGroupBoxServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.groupbox.CubaGroupBoxState;
import com.vaadin.ui.Panel;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaGroupBox extends Panel {
    private ExpandChangeHandler expandChangeHandler = null;

    public CubaGroupBox() {
        CubaGroupBoxServerRpc rpc = new CubaGroupBoxServerRpc() {
            @Override
            public void expand() {
                setExpanded(true);
            }

            @Override
            public void collapse() {
                if (getState().collapsable)
                    setExpanded(false);
            }
        };
        registerRpc(rpc);
    }

    @Override
    protected CubaGroupBoxState getState() {
        return (CubaGroupBoxState) super.getState();
    }

    @Override
    protected CubaGroupBoxState getState(boolean markAsDirty) {
        return (CubaGroupBoxState) super.getState(markAsDirty);
    }

    public boolean isExpanded() {
        return !getState(false).collapsable || getState(false).expanded;
    }

    public void setExpanded(boolean expanded) {
        if (expanded != getState(false).expanded) {
            getContent().setVisible(expanded);
            markAsDirtyRecursive();
        }

        getState().expanded = expanded;
        if (expandChangeHandler != null)
            expandChangeHandler.expandStateChanged(expanded);
    }

    public boolean isCollapsable() {
        return getState(false).collapsable;
    }

    public void setCollapsable(boolean collapsable) {
        getState().collapsable = collapsable;
        if (collapsable)
            setExpanded(true);
    }

    public ExpandChangeHandler getExpandChangeHandler() {
        return expandChangeHandler;
    }

    public void setExpandChangeHandler(ExpandChangeHandler expandChangeHandler) {
        this.expandChangeHandler = expandChangeHandler;
    }

    public interface ExpandChangeHandler {
        void expandStateChanged(boolean expanded);
    }
}