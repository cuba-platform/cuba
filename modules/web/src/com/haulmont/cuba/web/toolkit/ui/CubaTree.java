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

    protected ActionManager shortcutsManager = new ActionManager();

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
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
        shortcutsManager.addAction(listener);
    }

    @Override
    public void removeShortcutListener(ShortcutListener listener) {
        shortcutsManager.removeAction(listener);
    }

}
