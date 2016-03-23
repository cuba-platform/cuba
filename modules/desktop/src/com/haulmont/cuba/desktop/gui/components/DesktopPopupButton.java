/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Component.CENTER_ALIGNMENT;

/**
 */
public class DesktopPopupButton extends DesktopAbstractActionsHolderComponent<JButton> implements PopupButton {

    public static final String DROP_DOWN_ICON = "/components/popupbutton/open-popup.png";

    protected JPopupMenu popup;

    protected String icon;

    protected DesktopResources resources = App.getInstance().getResources();

    protected List<Action> initializedActions = new ArrayList<>();
    protected final JLabel captionLabel;
    private final JLabel rightIcon;

    public DesktopPopupButton() {
        popup = new JPopupMenu();

        impl = new JButton();
        impl.setLayout(new BoxLayout(impl, BoxLayout.X_AXIS));

        captionLabel = new JLabel();
        impl.add(Box.createHorizontalGlue());
        impl.add(captionLabel);
        captionLabel.setAlignmentX(CENTER_ALIGNMENT);

        rightIcon = new JLabel();
        rightIcon.setIcon(resources.getIcon(DROP_DOWN_ICON));
        rightIcon.setAlignmentX(CENTER_ALIGNMENT);
        impl.add(rightIcon);
        impl.add(Box.createHorizontalGlue());

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
        DesktopComponentsHelper.adjustSize(impl);
    }

    protected void showPopup() {
        popup.removeAll();

        for (final Action action : actionList) {
            if (action.isVisible()) {
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
                menuItem.setName(action.getId());

                initAction(action, menuItem);

                popup.add(menuItem);
            }
        }

        int popupHeight = popup.getComponentCount() * 25;

        Point pt = new Point();
        SwingUtilities.convertPointToScreen(pt, impl);

        int y;
        if (pt.getY() + impl.getHeight() + popupHeight < Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
            y = impl.getHeight();
        } else {
            y = -popupHeight;
        }

        // do not show ugly empty popup
        if (popup.getComponentCount() > 0) {
            popup.show(impl, 0, y);
        }
    }

    protected void initAction(final Action action, final JMenuItem menuItem) {
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
        return popup.isVisible();
    }

    @Override
    public void setPopupVisible(boolean popupVisible) {
        if (popupVisible && !popup.isVisible()) {
            showPopup();
        } else if (popup.isVisible()) {
            popup.setVisible(false);
        }
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
    public void setShowActionIcons(boolean iconsEnabled) {
        // do nothing
    }

    @Override
    public boolean isShowActionIcons() {
        return false;
    }

    @Override
    public String getCaption() {
        return impl.getText();
    }

    @Override
    public void setCaption(String caption) {
        captionLabel.setText(caption);

        if (StringUtils.isNotEmpty(caption) || icon != null) {
            rightIcon.setBorder(new EmptyBorder(0, 5, 0, 0));
        } else {
            rightIcon.setBorder(null);
        }
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
        if (icon != null) {
            captionLabel.setIcon(resources.getIcon(icon));
        } else {
            captionLabel.setIcon(resources.getIcon(DROP_DOWN_ICON));
        }

        if (StringUtils.isNotEmpty(getCaption()) || icon != null) {
            rightIcon.setBorder(new EmptyBorder(0, 5, 0, 0));
        } else {
            rightIcon.setBorder(null);
        }
    }

    @Override
    public void addAction(Action action, int index) {
        super.addAction(action, index);
        action.addOwner(new ButtonStub(action));
    }

    /**
     * This class is only needed to serve as a pseudo-owner for actions.
     */
    protected class ButtonStub implements Button {

        protected Action action;

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
        public Frame getFrame() {
            return DesktopPopupButton.this.getFrame();
        }

        @Override
        public void setFrame(Frame frame) {
        }

        @Override
        public String getId() {
            return "__" + action.getId() + "_button";
        }

        @Override
        public void setId(String id) {
        }

        @Override
        public Component getParent() {
            return null;
        }

        @Override
        public void setParent(Component parent) {
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