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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.textfield.CubaMaskedTextFieldState;

public class CubaMaskedTextField extends CubaTextField {

    public CubaMaskedTextField() {
        // directly init value locale to avoid unnecessary converted value setting
        setInternalValue(null);
    }

    public boolean isMaskedMode() {
       return getState(false).maskedMode;
    }

    public void setMaskedMode(boolean maskedMode) {
        getState(true).maskedMode = maskedMode;
    }

    public boolean isSendNullRepresentation() {
        return getState(false).sendNullRepresentation;
    }

    public void setSendNullRepresentation(boolean sendNullRepresentation) {
        getState(true).sendNullRepresentation = sendNullRepresentation;
    }

    @Override
    protected CubaMaskedTextFieldState getState() {
        return (CubaMaskedTextFieldState) super.getState();
    }

    @Override
    protected CubaMaskedTextFieldState getState(boolean markAsDirty) {
        return (CubaMaskedTextFieldState) super.getState(markAsDirty);
    }

    public boolean isTimeMask() {
        return getState().isTimeMask;
    }

    public void setTimeMask(boolean isTimeMask) {
        getState(true).isTimeMask = isTimeMask;
    }

    public void setMask(String mask) {
        getState(true).mask = mask;
    }

    public String getMask(){
        return getState(false).mask;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if (readOnly == isReadOnly())
            return;
        super.setReadOnly(readOnly);
    }
}