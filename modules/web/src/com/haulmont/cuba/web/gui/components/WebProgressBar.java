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

import com.haulmont.cuba.gui.components.ProgressBar;

/**
 * Web realization of progress bar depending on vaadin {@link ProgressBar} component.
 * <p/>
 * Note that indeterminate bar implemented here just like as determinate, but with fixed 0.0 value
 * <p/>
 *
 */
public class WebProgressBar extends WebAbstractField<com.vaadin.ui.ProgressBar> implements ProgressBar {
    protected boolean indeterminate;

    public WebProgressBar() {
        component = new com.vaadin.ui.ProgressBar();
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);
        component.setIndeterminate(false);
    }

    @Override
    public boolean isIndeterminate() {
        return component.isIndeterminate();
    }

    @Override
    public void setIndeterminate(boolean indeterminate) {
        if (this.indeterminate != indeterminate)
            component.setIndeterminate(indeterminate);

        if (indeterminate) {
            component.setValue(0.0f);
        }
    }
}