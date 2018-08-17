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
import com.haulmont.cuba.gui.components.SizeUnit;
import com.haulmont.cuba.gui.components.SizeWithUnit;

import static com.haulmont.cuba.gui.components.Component.AUTO_SIZE;

/**
 * Dialog options of a window. Can be changed at run time from the window controller:
 * <pre>
 * getDialogOptions()
 *     .setWidth("640px")
 *     .setHeight("480px");
 * </pre>
 * <p>
 * A window can be forced to open as a dialog in {@link AbstractWindow#init} using the {@link #setForceDialog(Boolean)} method:
 * <pre>
 * getDialogOptions()
 *     .setForceDialog(true);
 * </pre>
 *
 * @deprecated JavaDoc
 */
@Deprecated
public class DialogOptions {
    private Float width;
    private SizeUnit widthUnit;
    private Float height;
    private SizeUnit heightUnit;

    private Integer positionX;
    private Integer positionY;

    private Boolean resizable;
    private Boolean closeable;
    private Boolean modal;
    private Boolean closeOnClickOutside;
    private Boolean maximized;

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
     * @return units used in height property
     */
    public SizeUnit getHeightUnit() {
        return heightUnit;
    }

    /**
     * Sets the unit used for the height.
     *
     * @param heightUnit the unit used for the height
     */
    public DialogOptions setHeightUnit(SizeUnit heightUnit) {
        return setHeight(this.height, heightUnit);
    }

    /**
     * @return actual height if window opened as a dialog or if value is set from window controller
     */
    public Float getHeight() {
        return height;
    }

    /**
     * Set height of a window if it will be opened as a dialog or change height
     * at run time if the window is already opened as a dialog. If the {@link #heightUnit}
     * is set then its value will be used, {@link com.haulmont.cuba.gui.components.SizeUnit#PIXELS} otherwise.
     *
     * @param height the height value in the unit used for the height
     * @deprecated Use {@link #setHeight(Float)} instead.
     */
    @Deprecated
    public DialogOptions setHeight(Integer height) {
        return setHeight(height.floatValue());
    }

    /**
     * Set height of a window if it will be opened as a dialog or change height
     * at run time if the window is already opened as a dialog. If the {@link #heightUnit}
     * is set then its value will be used, {@link com.haulmont.cuba.gui.components.SizeUnit#PIXELS} otherwise.
     *
     * @param height the height value in the unit used for the height
     */
    public DialogOptions setHeight(Float height) {
        return setHeight(height, this.heightUnit);
    }

    /**
     * Sets the height of a window if it will be opened as a dialog
     * using String presentation or change height at run time if the window is already opened as a dialog.
     * <p>
     * Null, empty or 'AUTO' string will produce undefined height and set units to pixels.
     *
     * @param height height string representation
     */
    public DialogOptions setHeight(String height) {
        SizeWithUnit size = SizeWithUnit.parseStringSize(height);
        return setHeight(size.getSize(), size.getUnit());
    }

    protected DialogOptions setHeight(Float height, SizeUnit unit) {
        this.height = height;
        this.heightUnit = unit;

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
     * @return units used in width property
     */
    public SizeUnit getWidthUnit() {
        return widthUnit;
    }

    /**
     * Sets the unit used for the width.
     *
     * @param widthUnit the unit used for the width
     */
    public DialogOptions setWidthUnit(SizeUnit widthUnit) {
        return setWidth(this.width, widthUnit);
    }

    /**
     * @return actual width if window opened as a dialog or if value is set from window controller
     */
    public Float getWidth() {
        return width;
    }

    /**
     * Set width of a window if it will be opened as a dialog or change width
     * at run time if the window is already opened as a dialog. If the {@link #widthUnit}
     * is set then its value will be used, {@link com.haulmont.cuba.gui.components.SizeUnit#PIXELS} otherwise.
     *
     * @param width the width value in the unit used for the height
     * @deprecated Use {@link #setWidth(Float)} instead.
     */
    @Deprecated
    public DialogOptions setWidth(Integer width) {
        return setWidth(width.floatValue());
    }

    /**
     * Set width of a window if it will be opened as a dialog or change width
     * at run time if the window is already opened as a dialog. If the {@link #widthUnit}
     * is set then its value will be used, {@link com.haulmont.cuba.gui.components.SizeUnit#PIXELS} otherwise.
     *
     * @param width the width value in the unit used for the height
     */
    public DialogOptions setWidth(Float width) {
        return setWidth(width, this.widthUnit);
    }

    /**
     * Sets the width of a window if it will be opened as a dialog
     * using String presentation or change width at run time if the window is already opened as a dialog.
     * <p>
     * Null, empty or 'AUTO' string will produce undefined width and set units to pixels.
     *
     * @param width width string representation
     */
    public DialogOptions setWidth(String width) {
        SizeWithUnit size = SizeWithUnit.parseStringSize(width);
        return setWidth(size.getSize(), size.getUnit());
    }

    protected DialogOptions setWidth(Float width, SizeUnit unit) {
        this.width = width;
        this.widthUnit = unit;
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
        return setWidth(AUTO_SIZE);
    }

    /**
     * Set height of a window to AUTO if it will be opened as a dialog or change height at run time if the window is already opened as a dialog.
     */
    public DialogOptions setHeightAuto() {
        return setHeight(AUTO_SIZE);
    }

    /**
     * Center window only if it is already opened
     */
    public DialogOptions center() {
        return this;
    }

    /**
     * Set closeOnClickOutside to true if a window should be closed by click on outside window area.
     * It works when a window has a modal mode.
     */
    public DialogOptions setCloseOnClickOutside(Boolean closeOnClickOutside) {
        this.closeOnClickOutside = closeOnClickOutside;
        return this;
    }

    /**
     * @return true if a window can be closed by click on outside window area
     */
    public Boolean getCloseOnClickOutside() {
        return closeOnClickOutside;
    }

    /**
     * Set maximized to true if a window should be maximized across the screen.
     */
    public DialogOptions setMaximized(Boolean maximized) {
        this.maximized = maximized;
        return this;
    }

    /**
     * @return true if a window is maximized across the screen.
     */
    public Boolean getMaximized() {
        return maximized;
    }

    /**
     * @return x position of top-left corner of dialog window
     */
    public Integer getPositionX() {
        return positionX;
    }

    /**
     * Set the distance of Window left border in pixels from left border of the
     * containing (main window).
     *
     * @param positionX x position
     */
    public DialogOptions setPositionX(Integer positionX) {
        this.positionX = positionX;
        return this;
    }

    /**
     * @return y position of top-left corner of dialog window
     */
    public Integer getPositionY() {
        return positionY;
    }

    /**
     * Sets the distance of Window left border in pixels from top border of the
     * containing (main window).
     *
     * @param positionY y position
     */
    public DialogOptions setPositionY(Integer positionY) {
        this.positionY = positionY;
        return this;
    }
}