/*
 * Copyright (c) 2008-2018 Haulmont.
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
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SupportsUserAction;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;

import javax.annotation.Nullable;
import javax.inject.Inject;

@ActionType(ClearAction.ID)
public class ClearAction extends BaseAction implements PickerField.PickerFieldAction {

    public static final String ID = "picker_clear";

    protected PickerField pickerField;
    protected Icons icons;

    protected boolean editable = true;

    public ClearAction() {
        super(ID);
    }

    public ClearAction(String id) {
        super(id);
    }

    @Override
    public void setPickerField(@Nullable PickerField pickerField) {
        this.pickerField = pickerField;
    }

    @Override
    public void editableChanged(PickerField pickerField, boolean editable) {
        setEditable(editable);

        if (editable) {
            setIcon(icons.get(CubaIcon.PICKERFIELD_CLEAR));
        } else {
            setIcon(icons.get(CubaIcon.PICKERFIELD_CLEAR_READONLY));
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    protected void setEditable(boolean editable) {
        boolean oldValue = this.editable;
        if (oldValue != editable) {
            this.editable = editable;
            firePropertyChange(PROP_EDITABLE, oldValue, editable);
        }
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icons = icons;

        setIcon(icons.get(CubaIcon.PICKERFIELD_CLEAR));
    }

    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            // todo composition
            // Set the value as if the user had set it
            ((SupportsUserAction) pickerField).setValueFromUser(pickerField.getEmptyValue());
        } else {
            // call action perform handlers from super, delegate execution
            super.actionPerform(component);
        }
    }
}