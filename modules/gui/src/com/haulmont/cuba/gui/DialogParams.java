/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui;

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
 * @author degtyarjov
 * @version $Id$
 */
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

    public DialogParams reset() {
        this.height = null;
        this.width = null;
        this.resizable = null;
        this.closeable = null;
        this.modal = null;
        return this;
    }
}