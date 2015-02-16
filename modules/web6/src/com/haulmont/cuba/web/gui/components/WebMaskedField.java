/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.MaskedField;
import com.haulmont.cuba.web.toolkit.ui.MaskedTextField;

/**
 * @author devyatkin
 * @version $Id$
 */
public class WebMaskedField extends WebAbstractTextField<MaskedTextField> implements MaskedField {

    protected String inputPrompt;

    @Override
    public void setMask(String mask) {
        component.setMask(mask);
    }

    @Override
    public String getMask() {
        return component.getMask();
    }

    @Override
    public void setValueMode(ValueMode mode) {
        component.setMaskedMode(mode == ValueMode.MASKED);
    }

    @Override
    public ValueMode getValueMode() {
        return component.isMaskedMode() ? ValueMode.MASKED : ValueMode.CLEAR;
    }

    @Override
    protected MaskedTextField createTextFieldImpl() {
        return new MaskedTextField();
    }

    @Override
    public void setDatatype(Datatype datatype) {
        //Do nothing
    }

    @Override
    public Datatype getDatatype() {
        return null;
    }

    @Override
    public Formatter getFormatter() {
        return null;
    }

    @Override
    public void setFormatter(Formatter formatter) {
    }

    @Override
    protected Datatype getActualDatatype() {
        return null;
    }

    @Override
    public int getMaxLength() {
        return 0;
    }

    @Override
    public void setMaxLength(int value) {
        //do nothing
    }

    @Override
    public boolean isTrimming() {
        return false;
    }

    @Override
    public void setTrimming(boolean trimming) {
        //do nothing
    }

    @Override
    public String getInputPrompt() {
        return inputPrompt;
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
    }
}