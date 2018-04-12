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
package com.haulmont.cuba.gui.components;

import javax.annotation.Nullable;
import java.util.EventObject;

/**
 * A {@link Button} with a popup. The popup can contain actions.
 */
public interface PopupButton extends ActionsHolder, Component.HasCaption, Component.BelongToFrame,
        Component.HasIcon, Component.Focusable {

    String NAME = "popupButton";

    /**
     * @return true if popup is opened
     */
    boolean isPopupVisible();
    /**
     * Open or close popup panel.
     *
     * @param popupVisible whether open or close popup panel.
     */
    void setPopupVisible(boolean popupVisible);

    /**
     * Set menu width.
     *
     * @param width new menu width
     */
    void setMenuWidth(String width);
    /**
     * @return menu width
     */
    float getMenuWidth();
    /**
     * @return one of width units: {@link #UNITS_PIXELS}, {@link #UNITS_PERCENTAGE}
     *
     * @deprecated Use {@link #getMenuWidthSizeUnit()}
     */
    @Deprecated
    int getMenuWidthUnits();

    /**
     * Gets the menu width property units.
     *
     * @return units used in the menu width property.
     */
    SizeUnit getMenuWidthSizeUnit();

    /**
     * @return whether to close menu automatically after action triggerring or not
     */
    boolean isAutoClose();
    /**
     * Set menu automatic close after option click.
     *
     * @param autoClose whether to close menu automatically after action triggerring or not
     */
    void setAutoClose(boolean autoClose);

    /**
     * Set show icons for action buttons
     */
    void setShowActionIcons(boolean showActionIcons);
    /**
     * Return show icons for action buttons
     */
    boolean isShowActionIcons();

    /**
     * @return if sequential click on popup will toggle popup visibility
     */
    boolean isTogglePopupVisibilityOnClick();
    /**
     * Sets sequential click on popup will toggle popup visibility.
     *
     * @param togglePopupVisibilityOnClick true if sequential click on popup should toggle popup visibility
     */
    void setTogglePopupVisibilityOnClick(boolean togglePopupVisibilityOnClick);

    /**
     * @return opening direction for the popup
     */
    PopupOpenDirection getPopupOpenDirection();
    /**
     * Sets opening direction for the popup.
     *
     * @param direction new direction
     */
    void setPopupOpenDirection(PopupOpenDirection direction);

    /**
     * @return true if a click outside the popup closing the popup, otherwise - false
     */
    boolean isClosePopupOnOutsideClick();
    /**
     * If set to true, clicking on outside the popup closes it. Note that this doesn't affect clicking on the button itself.
     *
     * @param closePopupOnOutsideClick whether to close popup on ouside click
     */
    void setClosePopupOnOutsideClick(boolean closePopupOnOutsideClick);

    /**
     * Set custom inner content for the popup. Actions are ignored if a custom popup content is set.
     *
     * @param popupContent popup component.
     */
    void setPopupContent(@Nullable Component popupContent);
    /**
     * @return popup content component
     */
    @Nullable
    Component getPopupContent();

    void addPopupVisibilityListener(PopupVisibilityListener listener);
    void removePopupVisibilityListener(PopupVisibilityListener listener);

    /**
     * Popup window visibility change listener.
     */
    @FunctionalInterface
    interface PopupVisibilityListener {
        void popupVisibilityChange(PopupVisibilityEvent popupVisibilityEvent);
    }

    /**
     * This event is received by the PopupVisibilityListeners when the visibility of the popup changes.
     */
    class PopupVisibilityEvent extends EventObject {
        public PopupVisibilityEvent(PopupButton popupButton) {
            super(popupButton);
        }

        @Override
        public PopupButton getSource() {
            return (PopupButton) super.getSource();
        }
    }

    /**
     * Opening direction for the popup.
     */
    enum PopupOpenDirection {
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        BOTTOM_CENTER,
    }
}