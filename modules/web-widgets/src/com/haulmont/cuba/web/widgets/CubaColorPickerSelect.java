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

import com.vaadin.ui.Component;
import com.vaadin.ui.components.colorpicker.ColorPickerSelect;

public class CubaColorPickerSelect extends ColorPickerSelect {

    protected String allCaption;
    protected String redCaption;
    protected String greenCaption;
    protected String blueCaption;

    @Override
    protected Component initContent() {
        Component component = super.initContent();

        range.setTextInputAllowed(false);
        range.setItemCaptionGenerator(item -> {
            switch (item) {
                case ALL:
                    return allCaption;
                case RED:
                    return redCaption;
                case GREEN:
                    return greenCaption;
                case BLUE:
                    return blueCaption;
            }
            return null;
        });

        return component;
    }

    public void setAllCaption(String allCaption) {
        this.allCaption = allCaption;
        updateSelectedItemCaption();
    }

    public void setRedCaption(String redCaption) {
        this.redCaption = redCaption;
        updateSelectedItemCaption();
    }

    public void setGreenCaption(String greenCaption) {
        this.greenCaption = greenCaption;
        updateSelectedItemCaption();
    }

    public void setBlueCaption(String blueCaption) {
        this.blueCaption = blueCaption;
        updateSelectedItemCaption();
    }

    protected void updateSelectedItemCaption() {
        if (range != null && range.getSelectedItem().isPresent()) {
            range.updateSelectedItemCaption();
        }
    }
}