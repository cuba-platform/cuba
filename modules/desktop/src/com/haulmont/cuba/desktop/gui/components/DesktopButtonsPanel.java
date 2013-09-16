/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopButtonsPanel extends DesktopHBox implements ButtonsPanel {

    public DesktopButtonsPanel() {
        setSpacing(true);
    }

    @Override
    public void addButton(Button actionButton) {
        add(actionButton);
    }

    @Override
    public void removeButton(Button actionButton) {
        remove(actionButton);
    }

    @Override
    public Collection<Button> getButtons() {
        final Collection<Component> components = getComponents();
        final Collection<Button> buttons = new ArrayList<>(components.size());
        for (final Component component : components) {
            if (component instanceof Button)
                buttons.add((Button) component);
        }
        return buttons;
    }

    @Override
    public Button getButton(String id) {
        Component component = getComponent(id);
        if (component instanceof Button)
            return (Button) component;
        else
            return null;
    }

    @Override
    public void setEnabled(boolean enabled) {
        Collection<Button> buttons = getButtons();
        for (Button button : buttons) {
            button.setEnabled(enabled);
        }
    }

    public void setFocusableForAllButtons(boolean focusable) {
        for (Button button : getButtons()) {
            JComponent jButton = DesktopComponentsHelper.unwrap(button);
            jButton.setFocusable(focusable);
        }
    }
}