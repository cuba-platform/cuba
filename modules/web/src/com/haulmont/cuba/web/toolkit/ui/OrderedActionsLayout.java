/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractOrderedLayout;

import java.util.Map;

@SuppressWarnings("serial")
public class OrderedActionsLayout extends AbstractOrderedLayout implements Action.Container {

    private ActionManager actionManager;

    public void addActionHandler(Action.Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    public void removeActionHandler(Action.Handler actionHandler) {
        if (actionManager != null) {
            actionManager.removeActionHandler(actionHandler);
        }
    }

    protected ActionManager getActionManager() {
        if (actionManager == null) {
            actionManager = new ActionManager(this);
        }
        return actionManager;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (actionManager != null) {
            actionManager.paintActions(null, target);
        }
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        if (actionManager != null) {
            actionManager.handleActions(variables, this);
        }
    }
}
