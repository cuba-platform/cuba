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
import com.haulmont.cuba.gui.components.Window;

/**
 * Parameters that will be used for opening next window in modal mode.
 * <p/> E.g. to open an edit screen as modal dialog and set its width, use the following code in calling screen
 * controller:
 * <pre>
 * getDialogParams().setWidth(500);
 * openEditor("sales$Customer.edit", customer, WindowManager.OpenType.DIALOG, params);
 * </pre>
 * Parameters are reset to default values by the framework after opening of each window.
 *
 * @deprecated Use {@link WindowManager.OpenType} or {@link Frame.MessageType} with parameters.
 *             Also you can use {@link Window#getDialogOptions()} from window controller.
 *
 */
@Deprecated
public class DialogParams {

    public static final int AUTO_SIZE_PX = -1;

    private Integer width;
    private Integer height;
    private Boolean resizable;
    private Boolean closeable;
    private Boolean modal;

    public DialogParams() {
    }

    public DialogParams copyFrom(DialogParams dialogParams) {
        setHeight(dialogParams.getHeight());
        setModal(dialogParams.getModal());
        setWidth(dialogParams.getWidth());
        setCloseable(dialogParams.getCloseable());
        setResizable(dialogParams.getResizable());
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public DialogParams setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public DialogParams setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public DialogParams setWidthAuto() {
        this.width = AUTO_SIZE_PX;
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
        this.width = null;
        this.resizable = null;
        this.closeable = null;
        this.modal = null;
        return this;
    }
}