/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ShortcutActionManager;
import com.haulmont.cuba.web.toolkit.ui.client.tree.CubaTreeState;
import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.Tree;

import java.util.Map;
import java.util.Set;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaTree extends Tree {

    protected static final String POPUP_SELECTION = "popupSelection";

    protected boolean popupSelection = false;

    /**
     * Keeps track of the ShortcutListeners added to this component, and manages the painting and handling as well.
     */
    protected ActionManager shortcutActionManager;

    @Override
    protected CubaTreeState getState() {
        return (CubaTreeState) super.getState();
    }

    @Override
    protected CubaTreeState getState(boolean markAsDirty) {
        return (CubaTreeState) super.getState(markAsDirty);
    }

    public void setDoubleClickMode(boolean doubleClickMode) {
        if (getState(false).doubleClickMode != doubleClickMode) {
            getState().doubleClickMode = doubleClickMode;
        }
    }

    public boolean isDoubleClickMode() {
        return getState(false).doubleClickMode;
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        popupSelection = Boolean.TRUE.equals(variables.get(POPUP_SELECTION));

        if (shortcutActionManager != null) {
            shortcutActionManager.handleActions(variables, this);
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute(POPUP_SELECTION, popupSelection);
    }

    @Override
    public void addShortcutListener(ShortcutListener shortcut) {
        if (shortcutActionManager == null) {
            shortcutActionManager = new ShortcutActionManager(this);
        }

        shortcutActionManager.addAction(shortcut);
    }

    @Override
    public void removeShortcutListener(ShortcutListener shortcut) {
        if (shortcutActionManager != null) {
            shortcutActionManager.removeAction(shortcut);
        }
    }

    @Override
    protected void paintActions(PaintTarget target, Set<Action> actionSet) throws PaintException {
        super.paintActions(target, actionSet);

        if (shortcutActionManager != null) {
            shortcutActionManager.paintActions(null, target);
        }
    }
}