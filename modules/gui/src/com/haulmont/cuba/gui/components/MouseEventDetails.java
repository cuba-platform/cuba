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
 * Class to store mouse event details.
 */
public class MouseEventDetails {

    /**
     * Constants for mouse buttons.
     */
    public enum MouseButton {
        LEFT,
        RIGHT,
        MIDDLE
    }

    protected MouseButton button;
    protected int clientX;
    protected int clientY;
    protected boolean altKey;
    protected boolean ctrlKey;
    protected boolean metaKey;
    protected boolean shiftKey;
    protected boolean doubleClick;
    protected int relativeX = -1;
    protected int relativeY = -1;

    public MouseEventDetails() {
    }

    public MouseButton getButton() {
        return button;
    }

    public void setButton(MouseButton button) {
        this.button = button;
    }

    public int getClientX() {
        return clientX;
    }

    public void setClientX(int clientX) {
        this.clientX = clientX;
    }

    public int getClientY() {
        return clientY;
    }

    public void setClientY(int clientY) {
        this.clientY = clientY;
    }

    public boolean isAltKey() {
        return altKey;
    }

    public void setAltKey(boolean altKey) {
        this.altKey = altKey;
    }

    public boolean isCtrlKey() {
        return ctrlKey;
    }

    public void setCtrlKey(boolean ctrlKey) {
        this.ctrlKey = ctrlKey;
    }

    public boolean isMetaKey() {
        return metaKey;
    }

    public void setMetaKey(boolean metaKey) {
        this.metaKey = metaKey;
    }

    public boolean isShiftKey() {
        return shiftKey;
    }

    public void setShiftKey(boolean shiftKey) {
        this.shiftKey = shiftKey;
    }

    public boolean isDoubleClick() {
        return doubleClick;
    }

    public void setDoubleClick(boolean doubleClick) {
        this.doubleClick = doubleClick;
    }

    public int getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(int relativeX) {
        this.relativeX = relativeX;
    }

    public int getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(int relativeY) {
        this.relativeY = relativeY;
    }
}