/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 29.05.2010 14:00:05
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.event.ActionManager;
import com.vaadin.event.Action;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.PaintException;
import com.haulmont.cuba.toolkit.gwt.client.ui.VVerticalActionsLayout;

import java.util.Map;

@SuppressWarnings("serial")
@ClientWidget(VVerticalActionsLayout.class)
public class VerticalActionsLayout  extends VerticalLayout implements Action.Container {
    private ActionManager actionManager;

    public void addActionHandler(com.vaadin.event.Action.Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    public void removeActionHandler(com.vaadin.event.Action.Handler actionHandler) {
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
