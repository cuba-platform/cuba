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

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.AbstractWindow;

/**
 * Dialog options of a window. Can be changed at run time from the window controller:
 * <pre>
 * getDialogOptions()
 *     .setWidth(640)
 *     .setHeight(480);
 * </pre>
 * <p>
 * A window can be forced to open as a dialog in {@link AbstractWindow#init} using the {@link #setForceDialog(Boolean)} method:
 * <pre>
 * getDialogOptions()
 *     .setForceDialog(true);
 * </pre>
 *
 */
public class DialogOptions {
    private Integer width;
    private Integer height;
    private Boolean resizable;
    private Boolean closeable;
    private Boolean modal;
    private Boolean centered;

    private Boolean forceDialog;

    public DialogOptions() {
    }

    /**
     * @return true if a window is opened as a dialog and has close button or if set to true from the window controller
     */
    public Boolean getCloseable() {
        return closeable;
    }

    /**
     * Set closeable option if a window will be opened as a dialog or change closeable option if the window is already opened as a dialog.
     *
     * @param closeable closeable
     */
    public DialogOptions setCloseable(Boolean closeable) {
        this.closeable = closeable;
        return this;
    }

    /**
     * @return actual height in pixels if window opened as a dialog or if value is set from window controller
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Set height of a window if it will be opened as a dialog or change height at run time if the window is already opened as a dialog.
     *
     * @param height height in pixels
     */
    public DialogOptions setHeight(Integer height) {
        this.height = height;
        return this;
    }

    /**
     * @return true if a window is opened as a dialog and the window is modal or if set to true from the window controller
     */
    public Boolean getModal() {
        return modal;
    }

    /**
     * Set modal option if a window will be opened as a dialog or change modal option if the window is already opened as a dialog.
     *
     * @param modal modal
     */
    public DialogOptions setModal(Boolean modal) {
        this.modal = modal;
        return this;
    }

    /**
     * @return true if a window is opened as a dialog and the window is resizable or if set to true from the window controller
     */
    public Boolean getResizable() {
        return resizable;
    }

    /**
     * Set resizable option if a window will be opened as a dialog or change resizable option if the window is already opened as a dialog.
     *
     * @param resizable resizable
     */
    public DialogOptions setResizable(Boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    /**
     * @return actual width in pixels if window opened as a dialog or if value is set from window controller
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Set width of a window if it will be opened as a dialog or change width at run time if the window is already opened as a dialog.
     *
     * @param width width in pixels
     */
    public DialogOptions setWidth(Integer width) {
        this.width = width;
        return this;
    }

    /**
     * @return true if set to true from a window controller
     */
    public Boolean getForceDialog() {
        return forceDialog;
    }

    /**
     * Force a window manager to open a window as a dialog. Can be set from {@link AbstractWindow#init} method before the window is shown.
     *
     * @param forceDialog force dialog option
     */
    public DialogOptions setForceDialog(Boolean forceDialog) {
        this.forceDialog = forceDialog;
        return this;
    }

    /**
     * Set width of a window to AUTO if it will be opened as a dialog or change width at run time if the window is already opened as a dialog.
     */
    public DialogOptions setWidthAuto() {
        return setWidth(-1);
    }

    /**
     * Set height of a window to AUTO if it will be opened as a dialog or change height at run time if the window is already opened as a dialog.
     */
    public DialogOptions setHeightAuto() {
        return setHeight(-1);
    }

    /**
     * Set centered option if window will be shown in the center of screen or change its position if the window is already opened as dialog.
     * */
    public DialogOptions setCentered(boolean centered) {
        this.centered = centered;
        return this;
    }

    /**
     * @return true if window should be centered
     * */
    public Boolean getCentered() {
        return centered;
    }
}