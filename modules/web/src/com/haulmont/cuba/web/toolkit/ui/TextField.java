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
import com.vaadin.event.ActionManager;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.client.ui.VTextField;
import com.vaadin.ui.ClientWidget;

import java.util.Map;

@SuppressWarnings("serial")
@ClientWidget(VTextField.class)
public class TextField extends com.vaadin.ui.TextField implements Action.Container {

    protected ActionManager actionsContainer = null;

    public void addActionHandler(Action.Handler actionHandler) {
        if (actionsContainer == null) {
            actionsContainer = new ActionManager(this);
        }
        actionsContainer.addActionHandler(actionHandler);
    }

    public void removeActionHandler(Action.Handler actionHandler) {
        if (actionsContainer != null) {
            actionsContainer.removeActionHandler(actionHandler);
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (actionsContainer != null) {
            actionsContainer.paintActions(null, target);
        }
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        if (actionsContainer != null) {
            actionsContainer.handleActions(variables, this);
        }
    }
}
