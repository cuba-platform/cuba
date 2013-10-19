/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VTokenListLabel;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ClientWidget(VTokenListLabel.class)
public class TokenListLabel extends Label {

    private String key;

    private List<RemoveTokenListener> listeners;
    private ClickListener clickListener;

    private boolean editable;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        target.addAttribute("key", key);
        target.addAttribute("editable", editable);
        target.addAttribute("canopen", clickListener != null);
        super.paintContent(target);
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        if (canFireEvent(variables, "removeToken"))
            fireRemoveListeners();
        if (canFireEvent(variables, "itemClick"))
            fireClick();
    }

    public void addListener(RemoveTokenListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<RemoveTokenListener>();
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
    }

    private boolean canFireEvent(Map<String, Object> variables, String event) {
        if (variables.containsKey(event)) {
            String key = (String) variables.get(event);
            return key.equals(this.key);
        }
        return false;
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
        void removeToken(TokenListLabel source);
    }

    public interface ClickListener {
        void onClick(TokenListLabel source);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
