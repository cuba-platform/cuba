/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

/**
 * Dialog options of window. Can be changed at run time from window controller:
 * <pre>
 * getDialogOptions()
 *     .setWidth(640)
 *     .setHeight(480);
 * </pre>
 *
 * Dialog mode of window can be forced using setForceDialog method:
 * <pre>
 * getDialogOptions()
 *     .setForceDialog(true);
 * </pre>
 *
 * @author artamonov
 */
public class DialogOptions {
    private Integer width;
    private Integer height;
    private Boolean resizable;
    private Boolean closeable;
    private Boolean modal;

    private Boolean forceDialog;

    public DialogOptions() {
    }

    public Boolean getCloseable() {
        return closeable;
    }

    public DialogOptions setCloseable(Boolean closeable) {
        this.closeable = closeable;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public DialogOptions setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Boolean getModal() {
        return modal;
    }

    public DialogOptions setModal(Boolean modal) {
        this.modal = modal;
        return this;
    }

    public Boolean getResizable() {
        return resizable;
    }

    public DialogOptions setResizable(Boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public DialogOptions setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Boolean getForceDialog() {
        return forceDialog;
    }

    public DialogOptions setForceDialog(Boolean forceDialog) {
        this.forceDialog = forceDialog;
        return this;
    }

    public DialogOptions setWidthAuto() {
        return setWidth(-1);
    }

    public DialogOptions setHeightAuto() {
        return setHeight(-1);
    }
}