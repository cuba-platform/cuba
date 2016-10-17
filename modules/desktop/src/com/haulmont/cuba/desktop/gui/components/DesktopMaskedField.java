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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.MaskedField;

public class DesktopMaskedField extends DesktopTextField implements MaskedField {

    protected String mask;
    protected ValueMode mode = ValueMode.CLEAR;
    protected boolean sendNullRepresentation = true;

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

    @Override
    public boolean isSendNullRepresentation() {
        return sendNullRepresentation;
    }

    @Override
    public void setSendNullRepresentation(boolean sendNullRepresentation) {
        this.sendNullRepresentation = sendNullRepresentation;
    }
}