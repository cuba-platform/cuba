/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.tokenlistlabel.CubaTokenListLabelServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.tokenlistlabel.CubaTokenListLabelState;
import com.vaadin.ui.Panel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gorodnov
 * @version $Id$
 */
public class CubaTokenListLabel extends Panel {

    private List<RemoveTokenListener> listeners;
    private ClickListener clickListener;

    public CubaTokenListLabel() {
        CubaTokenListLabelServerRpc rpc = new CubaTokenListLabelServerRpc() {
            @Override
            public void removeToken() {
                fireRemoveListeners();
            }

            @Override
            public void itemClick() {
                fireClick();
            }
        };
        registerRpc(rpc);
    }

    @Override
    protected CubaTokenListLabelState getState() {
        return (CubaTokenListLabelState) super.getState();
    }

    @Override
    protected CubaTokenListLabelState getState(boolean markAsDirty) {
        return (CubaTokenListLabelState) super.getState(markAsDirty);
    }

    public void addListener(RemoveTokenListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    public void removeListener(RemoveTokenListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
        getState().canOpen = clickListener != null;
    }

    private void fireRemoveListeners() {
        if (listeners != null) {
            for (final RemoveTokenListener listener : listeners) {
                listener.removeToken(this);
            }
        }
    }

    private void fireClick() {
        if (clickListener != null) {
            clickListener.onClick(this);
        }
    }

    public interface RemoveTokenListener {
        void removeToken(CubaTokenListLabel source);
    }

    public interface ClickListener {
        void onClick(CubaTokenListLabel source);
    }

    public void setText(String text) {
        getState().text = text;
    }

    public String getText() {
        return getState(false).text;
    }

    public boolean isEditable() {
        return getState(false).editable;
    }

    public void setEditable(boolean editable) {
        getState().editable = editable;
    }
}