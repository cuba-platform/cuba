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
 * JavaDoc for all properties
 */
public interface DialogWindow extends Window {
    /**
     * Name that is used to register a client type specific screen implementation in
     * {@link com.haulmont.cuba.gui.xml.layout.ComponentsFactory}
     */
    String NAME = "dialogWindow";

    void setDialogWidth(String dialogWidth);
    float getDialogWidth();
    SizeUnit getDialogWidthUnit();

    void setDialogHeight(String dialogHeight);
    float getDialogHeight();
    SizeUnit getDialogHeightUnit();

    void setDialogStylename(String stylename);
    String getDialogStylename();

    void setResizable(boolean resizable);
    boolean isResizable();

    void setDraggable(boolean draggable);
    boolean isDraggable();

    void setModal(boolean modal);
    boolean isModal();

    void setCloseOnClickOutside(boolean closeOnClickOutside);
    boolean isCloseOnClickOutside();

    void setWindowMode(WindowMode mode);
    WindowMode getWindowMode();

    void center();

    default void setPosition(int x, int y) {
        setPositionX(x);
        setPositionY(y);
    }

    void setPositionX(int positionX);
    int getPositionX();

    void setPositionY(int positionY);
    int getPositionY();

    enum WindowMode {
        /**
         * Normal mode. The window size and position is determined by the window state.
         */
        NORMAL,
        /**
         * Maximized mode. The window is positioned in the top left corner and fills the whole screen.
         */
        MAXIMIZED;
    }
}