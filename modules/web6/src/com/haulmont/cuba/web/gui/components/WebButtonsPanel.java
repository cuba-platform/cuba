/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebButtonsPanel extends WebHBoxLayout implements ButtonsPanel {

    public WebButtonsPanel() {
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
        final Collection<com.haulmont.cuba.gui.components.Button> buttons = new ArrayList<>(components.size());
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
}