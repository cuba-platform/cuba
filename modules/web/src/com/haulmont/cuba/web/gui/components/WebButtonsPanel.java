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

import java.util.ArrayList;
import java.util.Collection;

public class WebButtonsPanel extends WebHBoxLayout
        implements ButtonsPanel {

    public WebButtonsPanel() {
        super();
        setSpacing(true);
        setMargin(true);
    }

    public void addButton(com.haulmont.cuba.gui.components.Button actionButton) {
        add(actionButton);
    }

    public void removeButton(com.haulmont.cuba.gui.components.Button actionButton) {
        remove(actionButton);
    }

    public Collection<com.haulmont.cuba.gui.components.Button> getButtons() {
        final Collection<Component> components = getComponents();
        final Collection<com.haulmont.cuba.gui.components.Button> buttons = new ArrayList<Button>(components.size());
        for (final Component component : components) {
            buttons.add((Button) component);
        }
        return buttons;
    }

    public com.haulmont.cuba.gui.components.Button getButton(String id) {
        return getComponent(id);
    }

    @Override
    public void add(Component component) {
        if (component instanceof com.haulmont.cuba.gui.components.Button) {
            super.add(component);
        } else {
            throw new IllegalArgumentException("Component is not a button");
        }
    }

    @Override
    public void remove(Component component) {
        if (component instanceof com.haulmont.cuba.gui.components.Button) {
            super.remove(component);
        } else {
            throw new IllegalArgumentException("Component is not a button");
        }
    }
}
