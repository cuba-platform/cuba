/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui;

import java.io.Serializable;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class DialogParams implements Serializable {

    private static final long serialVersionUID = -5408074764288510987L;

    private Integer width;
    private Integer height;
    private Boolean resizable;
    private Boolean closeable;

    public DialogParams() {
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

    public DialogParams reset() {
        this.height = null;
        this.width = null;
        resizable = null;
        return this;
    }
}
