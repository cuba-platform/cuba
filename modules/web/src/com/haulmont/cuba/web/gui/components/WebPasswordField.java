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

import com.haulmont.cuba.gui.components.CapsLockIndicator;
import com.haulmont.cuba.gui.components.PasswordField;
import com.haulmont.cuba.web.widgets.CubaPasswordField;

public class WebPasswordField extends WebAbstractTextField<CubaPasswordField, String> implements PasswordField {

    protected CapsLockIndicator capsLockIndicator;

    @Override
    protected CubaPasswordField createTextFieldImpl() {
        return new CubaPasswordField();
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }

    @Override
    public boolean isAutocomplete() {
        return component.isAutocomplete();
    }

    @Override
    public void setAutocomplete(Boolean value) {
        component.setAutocomplete(value);
    }

    @Override
    public void setCapsLockIndicator(CapsLockIndicator capsLockIndicator) {
        this.capsLockIndicator = capsLockIndicator;

        component.setCapsLockIndicator(capsLockIndicator);
    }

    @Override
    public CapsLockIndicator getCapsLockIndicator() {
        return capsLockIndicator;
    }
}