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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.MaskedField;
import com.haulmont.cuba.web.toolkit.ui.CubaMaskedTextField;

/**
 */
public class WebMaskedField extends WebAbstractTextField<CubaMaskedTextField> implements MaskedField {

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
    public boolean isSendNullRepresentation() {
        return component.isSendNullRepresentation();
    }

    @Override
    public void setSendNullRepresentation(boolean sendNullRepresentation) {
        component.setSendNullRepresentation(sendNullRepresentation);
    }

    @Override
    public Datatype getDatatype() {
        return null;
    }

    @Override
    public void setDatatype(Datatype datatype) {
        //Do nothing
    }

    @Override
    protected CubaMaskedTextField createTextFieldImpl() {
        return new CubaMaskedTextField();
    }

    @Override
    public Formatter getFormatter() {
        return null;
    }

    @Override
    public void setFormatter(Formatter formatter) {
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

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }
}
