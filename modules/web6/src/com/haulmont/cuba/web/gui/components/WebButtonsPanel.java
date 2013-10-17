/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 15.12.2009 16:29:37
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.client.ui.VHorizontalLayout;
import com.vaadin.ui.ClientWidget;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
@ClientWidget(VHorizontalLayout.class)
public class WebButtonsPanel extends WebHBoxLayout
        implements ButtonsPanel {

    public WebButtonsPanel() {
        super();
        setSpacing(true);
        setMargin(false);
    }

    @Override
    public void addButton(com.haulmont.cuba.gui.components.Button actionButton) {
        add(actionButton);
    }

    @Override
    public void removeButton(com.haulmont.cuba.gui.components.Button actionButton) {
        remove(actionButton);
    }

    @Override
    public Collection<com.haulmont.cuba.gui.components.Button> getButtons() {
        final Collection<Component> components = getComponents();
        final Collection<com.haulmont.cuba.gui.components.Button> buttons = new ArrayList<Button>(components.size());
        for (final Component component : components) {
            if (component instanceof Button)
                buttons.add((Button) component);
        }
        return buttons;
    }

    @Override
    public com.haulmont.cuba.gui.components.Button getButton(String id) {
        Component component = getComponent(id);
        if (component instanceof Button)
            return (Button) component;
        else
            return null;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        target.addAttribute("horizontal", true);
        super.paintContent(target);
    }
}
