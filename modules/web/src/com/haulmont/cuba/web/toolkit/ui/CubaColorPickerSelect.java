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
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.components.colorpicker.ColorPickerSelect;

public class CubaColorPickerSelect extends ColorPickerSelect {

    public CubaColorPickerSelect() {
        super();
        range.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
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
