/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.data.PropertyValueStringify;
import com.haulmont.cuba.web.toolkit.ui.client.table.CubaTableState;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.shared.AbstractFieldState;

import java.util.Map;
import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTable extends com.vaadin.ui.Table {

    protected ActionManager shortcutsManager = new ActionManager();

    @Override
    protected CubaTableState getState() {
        return (CubaTableState) super.getState();
    }

    @Override
    protected CubaTableState getState(boolean markAsDirty) {
        return (CubaTableState) super.getState(markAsDirty);
    }

    public boolean isTextSelectionEnabled() {
        return getState(false).textSelectionEnabled;
    }

    public void setTextSelectionEnabled(boolean textSelectionEnabled) {
        if (isTextSelectionEnabled() != textSelectionEnabled) {
            getState(true).textSelectionEnabled = textSelectionEnabled;
        }
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
        if (property instanceof PropertyValueStringify)
            return ((PropertyValueStringify) property).getFormattedValue();

        return super.formatPropertyValue(rowId, colId, property);
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        // Actions
        if (shortcutsManager != null) {
            shortcutsManager.handleActions(variables, this);
        }
    }

    @Override
    protected void paintActions(PaintTarget target, Set<Action> actionSet) throws PaintException {
        super.paintActions(target, actionSet);
        shortcutsManager.paintActions(null, target);
    }

    @Override
    public void addShortcutListener(ShortcutListener listener) {
        /*if (listener.getKeyCode() != 13 || !(listener.getModifiers() == null || listener.getModifiers().length > 0)) {*/
            shortcutsManager.addAction(listener);
        /*} else
            shortcutListeners.add(listener);*/
    }

    @Override
    public void removeShortcutListener(ShortcutListener listener){
        /*shortcutListeners.remove(listener);*/
        shortcutsManager.removeAction(listener);
    }
}