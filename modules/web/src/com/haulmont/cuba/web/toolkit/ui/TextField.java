/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 24.11.2009 15:29:23
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.event.Action;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.PaintException;
import com.haulmont.cuba.web.toolkit.utils.ActionsContainer;

import java.util.Map;

@SuppressWarnings("serial")
public class TextField extends com.vaadin.ui.TextField implements Action.Container {

    protected ActionsContainer actionsContainer = null;

    public void addActionHandler(Action.Handler actionHandler) {
        if (actionsContainer == null) {
            actionsContainer = new ActionsContainer(this);
        }
        actionsContainer.addActionHandler(actionHandler);
    }

    public void removeActionHandler(Action.Handler actionHandler) {
        if (actionsContainer != null) {
            actionsContainer.removeActionHandler(actionHandler);
            if (actionsContainer.getActionHandlers().isEmpty()) {
                actionsContainer = null;
            }
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (actionsContainer != null) {
            actionsContainer.paintActions(target);
        }
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        if (actionsContainer != null) {
            actionsContainer.changeVariables(variables);
        }
    }
}
