/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
@SuppressWarnings("serial")
public class TextField extends com.vaadin.ui.TextField implements Action.Container {

    private boolean allowFocusReadonly = false;

    @Override
    protected ActionManager getActionManager() {
        if (actionManager == null) {
            actionManager = new ActionManager(this);
        }
        return actionManager;
    }

    @Override
    public void addActionHandler(Action.Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    @Override
    public void removeActionHandler(Action.Handler actionHandler) {
        if (actionManager != null) {
            actionManager.removeActionHandler(actionHandler);
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (actionManager != null) {
            actionManager.paintActions(null, target);
        }
        target.addAttribute("allowFocusReadonly", allowFocusReadonly);
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        if (actionManager != null) {
            actionManager.handleActions(variables, this);
        }
    }

    @Override
    public void attach() {
        super.attach();
        if (actionManager != null) {
            actionManager.setViewer(this);
        }
    }

    @Override
    public void setNullRepresentation(String nullRepresentation) {
        super.setNullRepresentation(nullRepresentation);
        requestRepaint();
    }

    public boolean isAllowFocusReadonly() {
        return allowFocusReadonly;
    }

    public void setAllowFocusReadonly(boolean allowFocusReadonly) {
        this.allowFocusReadonly = allowFocusReadonly;
        requestRepaint();
    }
}
