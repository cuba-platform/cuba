/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.actions.picker;

import com.haulmont.cuba.gui.components.ActionType;

/**
 * Standard picker field action for opening an entity instance if it is a one-to-one composition.
 * <p>
 * Should be defined for {@code PickerField} or its subclass in a screen XML descriptor.
 * <p>
 * The action instance can be parameterized using the nested {@code properties} XML element or programmatically in the
 * screen controller.
 */
@ActionType(OpenCompositionAction.ID)
public class OpenCompositionAction extends OpenAction {

    public static final String ID = "picker_open_composition";

    public OpenCompositionAction() {
        super(ID);
    }

    public OpenCompositionAction(String id) {
        super(id);
    }

    @Override
    protected boolean checkFieldValue() {
        return true;
    }
}