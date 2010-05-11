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

public class WebButtonsPanel extends WebGridLayout
        implements ButtonsPanel {

    public WebButtonsPanel() {
        super();
        setSpacing(true);
        setWidth("100%");
        setRows(1);
        setColumns(1);
    }

    public void addButton(com.haulmont.cuba.gui.components.Button actionButton) {
        int buttonsCount = getButtons().size();
        if (buttonsCount > 0) {
            setColumnExpandRatio(buttonsCount - 1, 0);
        }
        setColumns(getColumns() + 1);
        super.add(actionButton);
        setColumnExpandRatio(buttonsCount, 1);
    }

    public void removeButton(com.haulmont.cuba.gui.components.Button actionButton) {
        super.remove(actionButton);
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
        if (component instanceof Button) {
            addButton((Button) component);
        } else {
            throw new IllegalArgumentException("Component is not a button");
        }
    }

    @Override
    public void remove(Component component) {
        if (component instanceof Button) {
            removeButton((Button) component);
        } else {
            throw new IllegalArgumentException("Component is not a button");
        }
    }
}
