/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.Resources;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.PopupButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopPopupButton
        extends DesktopAbstractActionOwnerComponent<JButton>
        implements PopupButton
{
    private JPopupMenu popup;

    private String icon;

    public static final String DEFAULT_ICON = "/popupbutton/open-popup.png";

    private Resources resources = App.getInstance().getResources();

    public DesktopPopupButton() {
        popup = new JPopupMenu();

        impl = new JButton();
        impl.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (popup.isVisible())
                            popup.setVisible(false);
                        else
                            showPopup();
                    }
                }
        );
        impl.setIcon(resources.getIcon(DEFAULT_ICON));
        DesktopComponentsHelper.adjustSize(impl);
    }

    private void showPopup() {
        popup.removeAll();
        for (final Action action : actionsOrder) {
            final JMenuItem menuItem = new JMenuItem(action.getCaption());
            menuItem.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            action.actionPerform(DesktopPopupButton.this);
                        }
                    }
            );
            menuItem.setEnabled(action.isEnabled());

            action.addPropertyChangeListener(
                    new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if (Action.PROP_CAPTION.equals(evt.getPropertyName())) {
                                menuItem.setText(action.getCaption());
                            } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
                                menuItem.setEnabled(action.isEnabled());
                            }
                        }
                    }
            );

            popup.add(menuItem);
        }

        int popupHeight = actionsOrder.size() * 25;

        Point pt = new Point();
        SwingUtilities.convertPointToScreen(pt, impl);

        int y;
        if (pt.getY() + impl.getHeight() + popupHeight < Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
            y = impl.getHeight();
        } else {
            y = -popupHeight;
        }

        popup.show(impl, 0, y);
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
        return true;
    }

    @Override
    public void setAutoClose(boolean autoClose) {
    }

    @Override
    public String getCaption() {
        return impl.getText();
    }

    @Override
    public void setCaption(String caption) {
        impl.setText(caption);
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
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
        if (icon != null)
            impl.setIcon(resources.getIcon(icon));
        else
            impl.setIcon(resources.getIcon(DEFAULT_ICON));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
}
