/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopPopupButton
        extends DesktopAbstractActionsHolderComponent<JButton>
        implements PopupButton
{
    private JPopupMenu popup;

    private String icon;

    public static final String DEFAULT_ICON = "/components/popupbutton/open-popup.png";

    private DesktopResources resources = App.getInstance().getResources();

    private List<Action> initializedActions = new ArrayList<Action>();

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
        for (final Action action : actionList) {
            final JMenuItem menuItem = new JMenuItem(action.getCaption());
            menuItem.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            action.actionPerform((Component) action.getOwner());
                        }
                    }
            );
            menuItem.setEnabled(action.isEnabled());
            menuItem.setVisible(action.isVisible());

            initAction(action, menuItem);

            popup.add(menuItem);
        }

        int popupHeight = actionList.size() * 25;

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

    private void initAction(final Action action, final JMenuItem menuItem) {
        if (initializedActions.contains(action))
            return;

        action.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (Action.PROP_CAPTION.equals(evt.getPropertyName())) {
                            menuItem.setText(action.getCaption());
                        } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
                            menuItem.setEnabled(action.isEnabled());
                        } else if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
                            menuItem.setVisible(action.isVisible());
                        }
                    }
                }
        );

        initializedActions.add(action);
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
        return getImpl().getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        getImpl().setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
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
    public void addAction(Action action) {
        super.addAction(action);
        action.addOwner(new ButtonStub(action));
    }

    /**
     * This class is only needed to serve as a pseudo-owner for actions.
     */
    private class ButtonStub implements Button {

        private Action action;

        public ButtonStub(Action action) {
            this.action = action;
        }

        @Override
        public Action getAction() {
            return action;
        }

        @Override
        public void setAction(Action action) {
        }

        @Override
        public <A extends IFrame> A getFrame() {
            return (A) DesktopPopupButton.this.getFrame();
        }

        @Override
        public void setFrame(IFrame frame) {
        }

        @Override
        public String getId() {
            return "__" + action.getId() + "_button";
        }

        @Override
        public void setId(String id) {
        }

        @Override
        public String getDebugId() {
            return getId();
        }

        @Override
        public void setDebugId(String id) {
        }

        @Override
        public boolean isEnabled() {
            return action.isEnabled();
        }

        @Override
        public void setEnabled(boolean enabled) {
        }

        @Override
        public boolean isVisible() {
            return DesktopPopupButton.this.isVisible();
        }

        @Override
        public void setVisible(boolean visible) {
        }

        @Override
        public void requestFocus() {
        }

        @Override
        public float getHeight() {
            return 0;
        }

        @Override
        public int getHeightUnits() {
            return 0;
        }

        @Override
        public void setHeight(String height) {
        }

        @Override
        public float getWidth() {
            return 0;
        }

        @Override
        public int getWidthUnits() {
            return 0;
        }

        @Override
        public void setWidth(String width) {
        }

        @Override
        public Alignment getAlignment() {
            return DesktopPopupButton.this.getAlignment();
        }

        @Override
        public void setAlignment(Alignment alignment) {
        }

        @Override
        public String getStyleName() {
            return DesktopPopupButton.this.getStyleName();
        }

        @Override
        public void setStyleName(String name) {
        }

        @Override
        public String getCaption() {
            return action.getCaption();
        }

        @Override
        public void setCaption(String caption) {
        }

        @Override
        public String getDescription() {
            return DesktopPopupButton.this.getDescription();
        }

        @Override
        public void setDescription(String description) {
        }

        @Override
        public String getIcon() {
            return action.getIcon();
        }

        @Override
        public void setIcon(String icon) {
        }
    }
}
