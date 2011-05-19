/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;

import javax.swing.*;
import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopPopupButton
        extends DesktopAbstractComponent<JPanel>
        implements PopupButton
{
    public DesktopPopupButton() {
        impl = new JPanel(new java.awt.FlowLayout());
        impl.setBorder(BorderFactory.createLineBorder(java.awt.Color.gray));
        impl.add(new JLabel("TODO: popupButton"));
    }

    @Override
    public void setPopupComponent(Component component) {
    }

    @Override
    public void removePopupComponent() {
    }

    @Override
    public Component getPopupComponent() {
        return null;
    }

    @Override
    public boolean isPopupVisible() {
        return false;
    }

    @Override
    public void setPopupVisible(boolean popupVisible) {
    }

    @Override
    public void setMenuWidth(String width) {
    }

    @Override
    public boolean isAutoClose() {
        return false;
    }

    @Override
    public void setAutoClose(boolean autoClose) {
    }

    @Override
    public com.haulmont.cuba.gui.components.Action getAction() {
        return null;
    }

    @Override
    public void setAction(Action action) {
    }

    @Override
    public void addAction(Action action) {
    }

    @Override
    public void removeAction(Action action) {
    }

    @Override
    public Collection<Action> getActions() {
        return null;
    }

    @Override
    public Action getAction(String id) {
        return null;
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public void setCaption(String caption) {
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public void setIcon(String icon) {
    }
}
