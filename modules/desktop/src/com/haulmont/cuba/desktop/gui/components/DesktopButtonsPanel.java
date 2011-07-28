/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopButtonsPanel extends DesktopHBox implements ButtonsPanel {

    public DesktopButtonsPanel() {
        setSpacing(true);
    }

    public void addButton(Button actionButton) {
        add(actionButton);
    }

    public void removeButton(Button actionButton) {
        remove(actionButton);
    }

    public Collection<Button> getButtons() {
        final Collection<Component> components = getComponents();
        final Collection<Button> buttons = new ArrayList<Button>(components.size());
        for (final Component component : components) {
            if (component instanceof Button)
                buttons.add((Button) component);
        }
        return buttons;
    }

    public Button getButton(String id) {
        Component component = getComponent(id);
        if (component instanceof Button)
            return (Button) component;
        else
            return null;
    }

    public void setEnabled(boolean enabled) {
        Collection<Button> buttons = getButtons();
        for (Button button : buttons) {
            button.setEnabled(enabled);
        }
    }
}
