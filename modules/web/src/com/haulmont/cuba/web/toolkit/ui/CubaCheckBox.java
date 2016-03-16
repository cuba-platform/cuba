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

import com.haulmont.cuba.web.toolkit.ui.client.checkbox.CubaCheckBoxState;
import com.vaadin.ui.CheckBox;

/**
 */
public class CubaCheckBox extends CheckBox {

    public CubaCheckBox() {
        setValidationVisible(false);
        setShowBufferedSourceException(false);
    }

    public CubaCheckBox(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    protected CubaCheckBoxState getState() {
        return (CubaCheckBoxState) super.getState();
    }

    @Override
    protected CubaCheckBoxState getState(boolean markAsDirty) {
        return (CubaCheckBoxState) super.getState(markAsDirty);
    }

    public boolean isCaptionManagedByLayout() {
        return getState(false).captionManagedByLayout;
    }

    public void setCaptionManagedByLayout(boolean captionManagedByLayout) {
        if (isCaptionManagedByLayout() != captionManagedByLayout) {
            getState().captionManagedByLayout = captionManagedByLayout;
        }
    }
}