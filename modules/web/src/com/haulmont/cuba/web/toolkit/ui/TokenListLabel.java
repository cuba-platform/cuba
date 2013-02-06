/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 22.07.2010 18:56:27
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

//import com.haulmont.cuba.toolkit.gwt.client.ui.VTokenListLabel;
//import com.haulmont.cuba.web.gui.components.WebTokenList;
//import com.vaadin.server.PaintException;
//import com.vaadin.server.PaintTarget;
//import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@ClientWidget(VTokenListLabel.class)
public class TokenListLabel extends Label {
//
//    private String key;
//
//    private List<RemoveTokenListener> listeners;
//
//    private boolean editable;
//
//    @Override
//    public void paintContent(PaintTarget target) throws PaintException {
//        target.addAttribute("key", key);
//        target.addAttribute("editable", editable);
//        super.paintContent(target);
//    }
//
//    @Override
//    public void changeVariables(Object source, Map<String, Object> variables) {
//        if (variables.containsKey("removeToken")) {
//            String key = (String) variables.get("removeToken");
//            if (key.equals(this.key)) {
//                fireRemoveListeners();
//            }
//        }
//    }
//
//    public void addListener(RemoveTokenListener listener) {
//        if (listeners == null) {
//            listeners = new ArrayList<RemoveTokenListener>();
//        }
//        listeners.add(listener);
//    }
//
//    public void removeListener(RemoveTokenListener listener) {
//        if (listeners != null) {
//            listeners.remove(listener);
//            if (listeners.isEmpty()) {
//                listeners = null;
//            }
//        }
//    }
//
//    private void fireRemoveListeners() {
//        if (listeners != null) {
//            for (final RemoveTokenListener listener : listeners) {
//                listener.removeToken(this);
//            }
//        }
//    }
//
//    public interface RemoveTokenListener {
//        void removeToken(TokenListLabel source);
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }
//
//    public String getKey() {
//        return key;
//    }
//
//    public boolean isEditable() {
//        return editable;
//    }
//
//    public void setEditable(boolean editable) {
//        this.editable = editable;
//    }
}
