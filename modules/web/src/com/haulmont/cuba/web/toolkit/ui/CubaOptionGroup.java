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

import com.haulmont.cuba.web.toolkit.ui.client.optiongroup.CubaOptionGroupState;
import com.haulmont.cuba.web.toolkit.ui.client.optiongroup.OptionGroupOrientation;
import com.vaadin.ui.OptionGroup;

/**
 */
public class CubaOptionGroup extends OptionGroup {

    public CubaOptionGroup() {
        setValidationVisible(false);
        setShowBufferedSourceException(false);
    }

    @Override
    protected CubaOptionGroupState getState() {
        return (CubaOptionGroupState) super.getState();
    }

    @Override
    protected CubaOptionGroupState getState(boolean markAsDirty) {
        return (CubaOptionGroupState) super.getState(markAsDirty);
    }

    public OptionGroupOrientation getOrientation() {
        return getState(false).orientation;
    }

    public void setOrientation(OptionGroupOrientation orientation) {
        if (orientation != getOrientation()) {
            getState().orientation = orientation;
        }
    }
}