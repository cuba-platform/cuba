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

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import java.util.Objects;

public class CubaColorPickerWrapper extends CustomField {

    protected CubaColorPicker field;

    public CubaColorPickerWrapper() {
        initColorPicker();
        setFocusDelegate(field);
        setPrimaryStyleName("c-color-picker");
        setWidthUndefined();
    }

    protected void initColorPicker() {
        field = new CubaColorPicker();
        field.addColorChangeListener(e ->
                setValue(e.getColor())
        );
        field.setCaption(null);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        if (field != null) {
            field.setReadOnly(readOnly);
        }
    }

    @Override
    protected void setInternalValue(Object newValue) {
        if (!Objects.equals(field.getColor(), newValue)) {
            field.setColor((Color) newValue);
        }

        super.setInternalValue(newValue);
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (field != null) {
            if (width < 0) {
                field.setWidthUndefined();
            } else {
                field.setWidth("100%");
            }
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        if (field != null) {
            if (height < 0) {
                field.setHeightUndefined();
            } else {
                field.setHeight("100%");
            }
        }
    }

    @Override
    protected Component initContent() {
        return field;
    }

    @Override
    public Class getType() {
        return Color.class;
    }

    public void setDefaultCaptionEnabled(boolean value) {
        field.setDefaultCaptionEnabled(value);

        if (value) {
            removeStyleName("color-maxwidth");
        } else {
            addStyleName("color-maxwidth");
        }
    }

    public boolean isDefaultCaptionEnabled() {
        return field.isDefaultCaptionEnabled();
    }

    public void setButtonCaption(String value) {
        field.setCaption(value);
    }

    public String getButtonCaption() {
        return field.getCaption();
    }

    public void setHistoryVisible(boolean value) {
        field.setHistoryVisibility(value);
    }

    public boolean isHistoryVisible() {
        return field.getHistoryVisibility();
    }

    public void setSwatchesVisible(boolean value) {
        field.setSwatchesVisibility(value);
    }

    public boolean isSwatchesVisible() {
        return field.getSwatchesVisibility();
    }

    public void setRGBVisible(boolean value) {
        field.setRGBVisibility(value);
    }

    public boolean isRGBVisible() {
        return field.getRGBVisibility();
    }

    public void setHSVVisible(boolean value) {
        field.setHSVVisibility(value);
    }

    public boolean isHSVVisible() {
        return field.getHSVVisibility();
    }

    public void setPopupCaption(String popupCaption) {
        field.setWindowCaption(popupCaption);
    }

    public String getPopupCaption() {
        return field.getWindowCaption();
    }

    public void setConfirmButtonCaption(String caption) {
        field.setConfirmButtonCaption(caption);
    }

    public String getConfirmButtonCaption() {
        return field.getConfirmButtonCaption();
    }

    public void setCancelButtonCaption(String caption) {
        field.setCancelButtonCaption(caption);
    }

    public String getCancelButtonCaption() {
        return field.getCancelButtonCaption();
    }

    public void setSwatchesTabCaption(String caption) {
        field.setSwatchesTabCaption(caption);
    }

    public String getSwatchesTabCaption() {
        return field.getSwatchesTabCaption();
    }

    public void setLookupAllCaption(String lookupAllCaption) {
        field.setLookupAllCaption(lookupAllCaption);
    }

    public String getLookupAllCaption() {
        return field.getLookupAllCaption();
    }

    public void setLookupRedCaption(String lookupRedCaption) {
        field.setLookupRedCaption(lookupRedCaption);
    }

    public String getLookupRedCaption() {
        return field.getLookupRedCaption();
    }

    public void setLookupGreenCaption(String lookupGreenCaption) {
        field.setLookupGreenCaption(lookupGreenCaption);
    }

    public String getLookupGreenCaption() {
        return field.getLookupGreenCaption();
    }

    public void setLookupBlueCaption(String lookupBlueCaption) {
        field.setLookupBlueCaption(lookupBlueCaption);
    }

    public String getLookupBlueCaption() {
        return field.getLookupBlueCaption();
    }

    public void setRedSliderCaption(String redSliderCaption) {
        field.setRedSliderCaption(redSliderCaption);
    }

    public String getRedSliderCaption() {
        return field.getRedSliderCaption();
    }

    public void setGreenSliderCaption(String greenSliderCaption) {
        field.setGreenSliderCaption(greenSliderCaption);
    }

    public String getGreenSliderCaption() {
        return field.getGreenSliderCaption();
    }

    public void setBlueSliderCaption(String blueSliderCaption) {
        field.setBlueSliderCaption(blueSliderCaption);
    }

    public String getBlueSliderCaption() {
        return field.getBlueSliderCaption();
    }

    public void setHueSliderCaption(String hueSliderCaption) {
        field.setHueSliderCaption(hueSliderCaption);
    }

    public String getHueSliderCaption() {
        return field.getHueSliderCaption();
    }

    public void setSaturationSliderCaption(String saturationSliderCaption) {
        field.setSaturationSliderCaption(saturationSliderCaption);
    }

    public String getSaturationSliderCaption() {
        return field.getSaturationSliderCaption();
    }

    public void setValueSliderCaption(String valueSliderCaption) {
        field.setValueSliderCaption(valueSliderCaption);
    }

    public String getValueSliderCaption() {
        return field.getValueSliderCaption();
    }
}