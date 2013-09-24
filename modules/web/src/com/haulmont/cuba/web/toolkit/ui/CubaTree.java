/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

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

    private static final String POPUP_SELECTION = "popupSelection";

    private boolean popupSelection = false;

    protected ActionManager shortcutsManager = new ActionManager();

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        popupSelection = Boolean.TRUE.equals(variables.get(POPUP_SELECTION));
        if (shortcutsManager != null) {
            shortcutsManager.handleActions(variables, this);
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute(POPUP_SELECTION, popupSelection);
    }

    @Override
    protected void paintActions(PaintTarget target, Set<Action> actionSet) throws PaintException {
        super.paintActions(target, actionSet);
    }

    @Override
    public void addShortcutListener(ShortcutListener listener) {
        super.addShortcutListener(listener);
        shortcutsManager.addAction(listener);
    }

    @Override
    public void removeShortcutListener(ShortcutListener listener) {
        super.removeShortcutListener(listener);
        shortcutsManager.removeAction(listener);
    }

}
