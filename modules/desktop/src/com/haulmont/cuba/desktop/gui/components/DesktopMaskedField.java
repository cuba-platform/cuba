/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.MaskedField;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopMaskedField extends DesktopTextField implements MaskedField {

    protected String mask;
    protected ValueMode mode = ValueMode.CLEAR;

    @Override
    public void setMask(String mask) {
        this.mask = mask;
    }

    @Override
    public String getMask() {
        return mask;
    }

    @Override
    public void setValueMode(ValueMode mode) {
        this.mode = mode;
    }

    @Override
    public ValueMode getValueMode() {
        return mode;
    }
}