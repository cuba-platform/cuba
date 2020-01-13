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
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * A component for displaying a two different views to data. The minimized view is normally used to render the component,
 * and when it is clicked the full view is displayed on a popup.
 */
public interface PopupView extends Component.HasCaption, Component.BelongToFrame,
        Component.HasIcon, HasContextHelp, HasHtmlCaption, HasHtmlDescription {
    String NAME = "popupView";

    /**
     * Set visibility for the popup window.
     *
     * @param popupVisible popup visibility.
     */
    void setPopupVisible(boolean popupVisible);
    /**
     * @return true if popup is visible.
     */
    boolean isPopupVisible();

    /**
     * Set value for the label of component. Value of the label can contain HTML.
     *
     * @param minimizedValue label text.
     */
    void setMinimizedValue(String minimizedValue);
    /**
     * @return value of the label of component.
     */
    String getMinimizedValue();

    /**
     * Set inner content for the popup window.
     *
     * @param popupContent popup component.
     */
    void setPopupContent(Component popupContent);
    /**
     * @return popup content component.
     */
    Component getPopupContent();

    /**
     * Set possibility to close popup window on cursor out.
     *
     * @param hideOnMouseOut popup hide option.
     */
    void setHideOnMouseOut(boolean hideOnMouseOut);
    /**
     * @return true if popup window closes on cursor out.
     */
    boolean isHideOnMouseOut();

    /**
     * Set caption rendering as HTML.
     *
     * @param captionAsHtml true if we want to show caption as HTML.
     */
    void setCaptionAsHtml(boolean captionAsHtml);
    /**
     * @return true if caption is shown as HTML.
     */
    boolean isCaptionAsHtml();

    /**
     * Sets the popup position.
     *
     * @param top  the top popup position in pixels
     * @param left the left popup position in pixels
     */
    void setPopupPosition(int top, int left);

    /**
     * Sets the top popup position.
     *
     * @param top the top popup position in pixels
     */
    void setPopupPositionTop(int top);

    /**
     * @return top popup position if position is set via {@link #setPopupPosition(int, int)}
     */
    int getPopupPositionTop();

    /**
     * Sets the left popup position.
     *
     * @param left the left popup position in pixels
     */
    void setPopupPositionLeft(int left);

    /**
     * @return left popup position if position is set via {@link #setPopupPosition(int, int)}
     */
    int getPopupPositionLeft();

    /**
     * Sets the popup position.
     *
     * @param position the popup position
     */
    void setPopupPosition(PopupPosition position);

    /**
     * return {@code PopupPosition} or {@code null} if position is set via {@link #setPopupPosition(PopupPosition)}
     */
    PopupPosition getPopupPosition();

    /**
     * Popup position.
     */
    enum PopupPosition {
        /**
         * The default popup position is in the middle of the minimized value.
         */
        DEFAULT,

        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,

        MIDDLE_LEFT,
        MIDDLE_CENTER,
        MIDDLE_RIGHT,

        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT;

        @Nullable
        public static PopupPosition fromId(String position) {
            for (PopupPosition popupPosition : values()) {
                if (popupPosition.name().equals(position)) {
                    return popupPosition;
                }
            }
            return null;
        }
    }

    Subscription addPopupVisibilityListener(Consumer<PopupVisibilityEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removePopupVisibilityListener(Consumer<PopupVisibilityEvent> listener);

    /**
     * Event sent when the visibility of the popup changes.
     */
    class PopupVisibilityEvent extends EventObject {
        public PopupVisibilityEvent(PopupView popupView) {
            super(popupView);
        }

        @Override
        public PopupView getSource() {
            return (PopupView) super.getSource();
        }

        /**
         * @return popup view
         * @deprecated Use {@link #getSource()}
         */
        @Deprecated
        public PopupView getPopupView() {
            return getSource();
        }

        public boolean isPopupVisible() {
            return getSource().isPopupVisible();
        }
    }
}