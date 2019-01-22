/*
 * Copyright (c) 2008-2018 Haulmont.
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

/**
 * UI component that represents a dialog window of application.
 */
public interface DialogWindow extends Window {
    /**
     * Name that is used to register a client type specific screen implementation in
     * {@link com.haulmont.cuba.gui.UiComponents}.
     */
    String NAME = "dialogWindow";

    /**
     * Sets dialog width.
     *
     * @param dialogWidth width
     */
    void setDialogWidth(String dialogWidth);
    /**
     * @return dialog width
     */
    float getDialogWidth();
    /**
     * @return dialog width unit
     */
    SizeUnit getDialogWidthUnit();

    /**
     * Sets dialog height.
     *
     * @param dialogHeight height
     */
    void setDialogHeight(String dialogHeight);
    /**
     * @return height
     */
    float getDialogHeight();
    /**
     * @return dialog height unit
     */
    SizeUnit getDialogHeightUnit();

    /**
     * Sets the custom CSS style.
     *
     * @param stylename style name
     */
    void setDialogStylename(String stylename);
    /**
     * @return custom style name
     */
    String getDialogStylename();

    /**
     * Sets window resizable.
     *
     * @param resizable resizable flag
     */
    void setResizable(boolean resizable);
    /**
     * @return true if window is resizable by the end-user, otherwise false.
     */
    boolean isResizable();

    /**
     * Enables or disables that a window can be dragged (moved) by the user. By default a window is draggable.
     *
     * @param draggable draggable flag
     */
    void setDraggable(boolean draggable);
    /**
     *
     * @return true if window is draggable
     */
    boolean isDraggable();

    /**
     * Sets window modality. When a modal window is open, components outside
     * that window cannot be accessed.
     *
     * @param modal modal flag
     */
    void setModal(boolean modal);
    /**
     * @return true if window is modal
     */
    boolean isModal();

    /**
     * Sets if window can be closed by click outside of window content (by modality curtain).
     *
     * @param closeOnClickOutside true if window to be closed by click outside of window content (by modality curtain)
     */
    void setCloseOnClickOutside(boolean closeOnClickOutside);
    /**
     * @return true if window can be closed by click outside of window content (by modality curtain)
     */
    boolean isCloseOnClickOutside();

    /**
     * Sets the mode for the window.
     *
     * @param mode mode
     */
    void setWindowMode(WindowMode mode);
    /**
     * @return the mode of the window
     */
    WindowMode getWindowMode();

    /**
     * Sets this window to be centered on screen.
     */
    void center();

    /**
     * Sets the position of the window on the screen.
     *
     * @param x left position in pixels
     * @param y top position in pixels
     */
    default void setPosition(int x, int y) {
        setPositionX(x);
        setPositionY(y);
    }

    /**
     * Sets the distance of Window left border in pixels from left border of the containing (main window).
     *
     * @param positionX left position in pixels
     */
    void setPositionX(int positionX);
    /**
     *
     * @return the distance of Window left border in pixels from left border of the containing (main window)
     * or -1 if unspecified
     */
    int getPositionX();

    /**
     * Sets the distance of Window top border in pixels from top border of the containing (main window).
     *
     * @param positionY top position in pixels
     */
    void setPositionY(int positionY);
    /**
     *
     * @return distance of Window top border in pixels from top border of the containing (main window)
     * or -1 if unspecified
     */
    int getPositionY();

    /**
     * Supported dialog window modes.
     */
    enum WindowMode {
        /**
         * Normal mode. The window size and position is determined by the window state.
         */
        NORMAL,
        /**
         * Maximized mode. The window is positioned in the top left corner and fills the whole screen.
         */
        MAXIMIZED
    }
}