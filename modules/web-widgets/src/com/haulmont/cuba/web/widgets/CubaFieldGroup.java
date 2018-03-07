/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */
package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.fieldgroup.CubaFieldGroupState;
import com.vaadin.ui.Layout;

public class CubaFieldGroup extends CubaGroupBox {
    public CubaFieldGroup() {
        setLayout(new CubaFieldGroupLayout());
        setSizeUndefined();
    }

    public boolean isBorderVisible() {
        return getState(false).borderVisible;
    }

    public void setBorderVisible(boolean borderVisible) {
        if (getState().borderVisible != borderVisible) {
            getState().borderVisible = borderVisible;
            markAsDirty();
        }
    }

    @Override
    protected CubaFieldGroupState getState() {
        return (CubaFieldGroupState) super.getState();
    }

    @Override
    protected CubaFieldGroupState getState(boolean markAsDirty){
        return (CubaFieldGroupState) super.getState(markAsDirty);
    }

    public CubaFieldGroupLayout getLayout() {
        return (CubaFieldGroupLayout) super.getContent();
    }

    public void setLayout(Layout newLayout) {
        if (newLayout == null) {
            newLayout = new CubaFieldGroupLayout();
        }
        if (newLayout instanceof CubaFieldGroupLayout) {
            super.setContent(newLayout);
        } else {
            throw new IllegalArgumentException("FieldGroup supports only CubaFieldGroupLayout");
        }
    }
}