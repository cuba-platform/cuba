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

import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.SizeUnit;
import com.haulmont.cuba.gui.components.SizeWithUnit;
import com.haulmont.cuba.gui.components.Window;

/**
 * Parameters that will be used for opening next window in modal mode.
 * <br> E.g. to open an edit screen as modal dialog and set its width, use the following code in calling screen
 * controller:
 * <pre>
 * getDialogParams().setWidth(500);
 * openEditor("sales$Customer.edit", customer, WindowManager.OpenType.DIALOG, params);
 * </pre>
 * Parameters are reset to default values by the framework after opening of each window.
 *
 * @deprecated Use {@link WindowManager.OpenType} or {@link Frame.MessageType} with parameters.
 *             Also you can use {@link Window#getDialogOptions()} from window controller.
 */
@Deprecated
public class DialogParams {

    public static final float AUTO_SIZE_PX = -1.0f;

    private Float width;
    private SizeUnit widthUnit;
    private Float height;
    private SizeUnit heightUnit;
    private Boolean resizable;
    private Boolean closeable;
    private Boolean modal;

    public DialogParams() {
    }

    public DialogParams copyFrom(DialogParams dialogParams) {
        setHeight(dialogParams.getHeight());
        setHeightUnit(dialogParams.getHeightUnit());
        setModal(dialogParams.getModal());
        setWidth(dialogParams.getWidth());
        setWidthUnit(dialogParams.getWidthUnit());
        setCloseable(dialogParams.getCloseable());
        setResizable(dialogParams.getResizable());
        return this;
    }

    public Float getHeight() {
        return height;
    }

    /**
     * @deprecated Use {@link #setHeight(Float)} instead.
     */
    @Deprecated
    public DialogParams setHeight(Integer height) {
        return setHeight(height.floatValue());
    }

    public DialogParams setHeight(Float height) {
        this.height = height;
        return this;
    }

    public DialogParams setHeight(String height) {
        SizeWithUnit size = SizeWithUnit.parseStringSize(height);

        this.height = size.getSize();
        this.heightUnit = size.getUnit();

        return this;
    }

    public SizeUnit getHeightUnit() {
        return heightUnit;
    }

    public DialogParams setHeightUnit(SizeUnit heightUnit) {
        this.heightUnit = heightUnit;
        return this;
    }

    public Float getWidth() {
        return width;
    }

    /**
     * @deprecated Use {@link #setWidth(Float)} instead.
     */
    public DialogParams setWidth(Integer width) {
        return setWidth(width.floatValue());
    }

    public DialogParams setWidth(Float width) {
        this.width = width;
        return this;
    }

    public DialogParams setWidth(String width) {
        SizeWithUnit size = SizeWithUnit.parseStringSize(width);

        this.width = size.getSize();
        this.widthUnit = size.getUnit();

        return this;
    }

    public SizeUnit getWidthUnit() {
        return widthUnit;
    }

    public DialogParams setWidthUnit(SizeUnit widthUnit) {
        this.widthUnit = widthUnit;
        return this;
    }

    public DialogParams setWidthAuto() {
        this.width = AUTO_SIZE_PX;
        this.widthUnit = SizeUnit.PIXELS;
        return this;
    }

    public Boolean getResizable() {
        return resizable;
    }

    public DialogParams setResizable(Boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public Boolean getCloseable() {
        return closeable;
    }

    public DialogParams setCloseable(Boolean closeable) {
        this.closeable = closeable;
        return this;
    }

    public Boolean getModal() {
        return modal;
    }

    public DialogParams setModal(Boolean modal) {
        this.modal = modal;
        return this;
    }

    @Deprecated
    public DialogParams reset() {
        this.height = null;
        this.heightUnit = null;
        this.width = null;
        this.widthUnit = null;
        this.resizable = null;
        this.closeable = null;
        this.modal = null;
        return this;
    }
}