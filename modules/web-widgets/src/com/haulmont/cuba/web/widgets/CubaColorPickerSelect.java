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

import com.vaadin.v7.ui.AbstractSelect;
import com.vaadin.v7.ui.components.colorpicker.ColorPickerSelect;

public class CubaColorPickerSelect extends ColorPickerSelect {

    public CubaColorPickerSelect() {
        range.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        range.setTextInputAllowed(false);
    }

    public void setAllCaption(String allCaption) {
        range.setItemCaption(ColorRangePropertyId.ALL, allCaption);
    }

    public void setRedCaption(String redCaption) {
        range.setItemCaption(ColorRangePropertyId.RED, redCaption);
    }

    public void setGreenCaption(String greenCaption) {
        range.setItemCaption(ColorRangePropertyId.GREEN, greenCaption);
    }

    public void setBlueCaption(String blueCaption) {
        range.setItemCaption(ColorRangePropertyId.BLUE, blueCaption);
    }
}