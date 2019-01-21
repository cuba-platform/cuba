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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.desktop.gui.icons.IconResolver;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.icons.Icons;
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

public class DesktopPopupButton extends DesktopAbstractActionsHolderComponent<JButton> implements PopupButton {

    public static final String DROP_DOWN_ICON = "/components/popupbutton/open-popup.png";

    protected JPopupMenu popup;

    protected String icon;

    protected DesktopResources resources = App.getInstance().getResources();

    protected List<Action> initializedActions = new ArrayList<>();
    protected final JLabel captionLabel;
    protected final JLabel rightIcon;

    protected boolean togglePopupVisibilityOnClick = true; // just stub
    protected PopupOpenDirection popupOpenDirection = PopupOpenDirection.BOTTOM_RIGHT; // just stub
    protected boolean closePopupOnOutsideClick = true; // just stub
    protected Component popupContent; // just stub

    protected List<PopupVisibilityListener> popupVisibilityListeners = new ArrayList<>();

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
        firePopupVisibilityEvent();
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

    @Override
    protected void updateEnabled() {
        boolean resolvedEnabled = isEnabledWithParent();

        getComposition().setEnabled(resolvedEnabled);
        captionLabel.setEnabled(resolvedEnabled);
        rightIcon.setEnabled(resolvedEnabled);

        requestContainerUpdate();

        if (parent instanceof DesktopFieldGroup) {
            ((DesktopFieldGroup) parent).updateChildEnabled(this);
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
        // unsupported
    }

    @Override
    public float getMenuWidth() {
        return 0; // unsupported
    }

    @Override
    public int getMenuWidthUnits() {
        return UNITS_PIXELS; // unsupported
    }

    @Override
    public boolean isAutoClose() {
        return true;
    }

    @Override
    public void setAutoClose(boolean autoClose) {
    }

    @Override
    public void setShowActionIcons(boolean showActionIcons) {
        // do nothing
    }

    @Override
    public boolean isShowActionIcons() {
        return false;
    }

    @Override
    public boolean isTogglePopupVisibilityOnClick() {
        return togglePopupVisibilityOnClick;
    }

    @Override
    public void setTogglePopupVisibilityOnClick(boolean togglePopupVisibilityOnClick) {
        this.togglePopupVisibilityOnClick = togglePopupVisibilityOnClick;
    }

    @Override
    public PopupOpenDirection getPopupOpenDirection() {
        return popupOpenDirection;
    }

    @Override
    public void setPopupOpenDirection(PopupOpenDirection popupOpenDirection) {
        this.popupOpenDirection = popupOpenDirection;
    }

    @Override
    public boolean isClosePopupOnOutsideClick() {
        return closePopupOnOutsideClick;
    }

    @Override
    public void setClosePopupOnOutsideClick(boolean closePopupOnOutsideClick) {
        this.closePopupOnOutsideClick = closePopupOnOutsideClick;
    }

    @Override
    public void setPopupContent(Component popupContent) {
        this.popupContent = popupContent;
    }

    @Override
    public Component getPopupContent() {
        return popupContent;
    }

    @Override
    public void addPopupVisibilityListener(PopupVisibilityListener listener) {
        if (!popupVisibilityListeners.contains(listener)) {
            popupVisibilityListeners.add(listener);
        }
    }

    @Override
    public void removePopupVisibilityListener(PopupVisibilityListener listener) {
        popupVisibilityListeners.remove(listener);
    }

    protected void firePopupVisibilityEvent() {
        PopupVisibilityEvent event = new PopupVisibilityEvent(this);
        for (PopupVisibilityListener listener : popupVisibilityListeners) {
            listener.popupVisibilityChange(event);
        }
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

        IconResolver iconResolver = AppBeans.get(IconResolver.class);
        if (icon != null) {
            captionLabel.setIcon(iconResolver.getIconResource(icon));
        } else {
            captionLabel.setIcon(iconResolver.getIconResource(DROP_DOWN_ICON));
        }

        if (StringUtils.isNotEmpty(getCaption()) || icon != null) {
            rightIcon.setBorder(new EmptyBorder(0, 5, 0, 0));
        } else {
            rightIcon.setBorder(null);
        }
    }

    @Override
    public void setIconFromSet(Icons.Icon icon) {
        String iconPath = AppBeans.get(Icons.class)
                .get(icon);
        setIcon(iconPath);
    }

    @Override
    public boolean isFocusable() {
        return impl.isFocusable();
    }

    @Override
    public void setFocusable(boolean focusable) {
        impl.setFocusable(focusable);
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
        protected boolean responsive = false;

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
        public boolean isResponsive() {
            return responsive;
        }

        @Override
        public void setResponsive(boolean responsive) {
            this.responsive = responsive;
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
        public boolean isVisibleItself() {
            return DesktopPopupButton.this.isVisibleItself();
        }

        @Override
        public boolean isEnabledItself() {
            return action.isEnabled();
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
        public void setStyleName(String styleName) {
            DesktopPopupButton.this.setStyleName(styleName);
        }

        @Override
        public void addStyleName(String styleName) {
            DesktopPopupButton.this.addStyleName(styleName);
        }

        @Override
        public void removeStyleName(String styleName) {
            DesktopPopupButton.this.removeStyleName(styleName);
        }

        @Override
        public <X> X unwrap(Class<X> internalComponentClass) {
            return null;
        }

        @Override
        public <X> X unwrapComposition(Class<X> internalCompositionClass) {
            return null;
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

        // just stub
        @Override
        public void setIconFromSet(Icons.Icon icon) {
        }

        @Override
        public boolean isFocusable() {
            return DesktopPopupButton.this.isFocusable();
        }

        @Override
        public void setFocusable(boolean focusable) {
        }

        @Override
        public int getTabIndex() {
            return 0;
        }

        @Override
        public void setTabIndex(int tabIndex) {
        }

        @Override
        public void setDisableOnClick(boolean value) {
        }

        @Override
        public boolean isDisableOnClick() {
            return false;
        }

        @Override
        public boolean isUseResponsePending() {
            return false;
        }

        @Override
        public void setUseResponsePending(boolean useResponsePending) {
        }

        @Override
        public void setCaptionAsHtml(boolean captionAsHtml) {
        }

        @Override
        public boolean isCaptionAsHtml() {
            return false;
        }
    }
}