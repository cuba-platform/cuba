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
import org.apache.commons.lang.ObjectUtils;

public class CubaColorPicker extends CustomField {

    protected ColorPicker field;

    public CubaColorPicker() {
        initColorPicker();
        initLayout();
        setPrimaryStyleName("cuba-color-picker");
    }

    protected void initColorPicker() {
        field = new ColorPicker();
        field.addColorChangeListener(e ->
                setValue(e.getColor())
        );
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
        if (!ObjectUtils.equals(field.getColor(), newValue)) {
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

    protected void initLayout() {
        field = new ColorPicker();
        field.addColorChangeListener(e ->
                setValue(e.getColor())
        );
    }

    public class ColorPicker extends com.vaadin.ui.ColorPicker {
        protected String confirmButtonCaption;
        protected String cancelButtonCaption;
        protected String swatchesTabCaption;

        protected String lookupAllCaption;
        protected String lookupRedCaption;
        protected String lookupGreenCaption;
        protected String lookupBlueCaption;

        protected String redSliderCaption;
        protected String greenSliderCaption;
        protected String blueSliderCaption;
        protected String hueSliderCaption;
        protected String saturationSliderCaption;
        protected String valueSliderCaption;

        @Override
        protected void createPopupWindow() {
            window = new CubaColorPickerPopup(color);
            ((CubaColorPickerPopup) window).setConfirmButtonCaption(confirmButtonCaption);
            ((CubaColorPickerPopup) window).setCancelButtonCaption(cancelButtonCaption);
            ((CubaColorPickerPopup) window).setSwatchesTabCaption(swatchesTabCaption);
            ((CubaColorPickerPopup) window).setLookupAllCaption(lookupAllCaption);
            ((CubaColorPickerPopup) window).setLookupRedCaption(lookupRedCaption);
            ((CubaColorPickerPopup) window).setLookupGreenCaption(lookupGreenCaption);
            ((CubaColorPickerPopup) window).setLookupBlueCaption(lookupBlueCaption);
            window.setRedSliderCaption(redSliderCaption);
            window.setGreenSliderCaption(greenSliderCaption);
            window.setBlueSliderCaption(blueSliderCaption);
            window.setHueSliderCaption(hueSliderCaption);
            window.setSaturationSliderCaption(saturationSliderCaption);
            window.setValueSliderCaption(valueSliderCaption);
        }

        @Override
        public void setColor(Color color) {
            if (color == null) {
                color = new Color(0,0,0);
            }
            super.setColor(color);
        }

        public void setWindowCaption(String windowCaption) {
            this.popupCaption = windowCaption;
        }

        public String getWindowCaption() {
            return this.popupCaption;
        }

        public void setConfirmButtonCaption(String confirmButtonCaption) {
            this.confirmButtonCaption = confirmButtonCaption;
        }

        public String getConfirmButtonCaption() {
            return this.confirmButtonCaption;
        }

        public void setCancelButtonCaption(String cancelButtonCaption) {
            this.cancelButtonCaption = cancelButtonCaption;
        }

        public String getCancelButtonCaption() {
            return this.cancelButtonCaption;
        }

        public void setSwatchesTabCaption(String swatchesTabCaption) {
            this.swatchesTabCaption = swatchesTabCaption;
        }

        public String getSwatchesTabCaption() {
            return this.swatchesTabCaption;
        }

        public void setLookupAllCaption(String lookupAllCaption) {
            this.lookupAllCaption = lookupAllCaption;
        }

        public String getLookupAllCaption() {
            return this.lookupAllCaption;
        }

        public void setLookupRedCaption(String lookupRedCaption) {
            this.lookupRedCaption = lookupRedCaption;
        }

        public String getLookupRedCaption() {
            return this.lookupRedCaption;
        }

        public void setLookupGreenCaption(String lookupGreenCaption) {
            this.lookupGreenCaption = lookupGreenCaption;
        }

        public String getLookupGreenCaption() {
            return this.lookupGreenCaption;
        }

        public void setLookupBlueCaption(String lookupBlueCaption) {
            this.lookupBlueCaption = lookupBlueCaption;
        }

        public String getLookupBlueCaption() {
            return this.lookupBlueCaption;
        }

        public String getBlueSliderCaption() {
            return blueSliderCaption;
        }

        public void setBlueSliderCaption(String blueSliderCaption) {
            this.blueSliderCaption = blueSliderCaption;
        }

        public String getGreenSliderCaption() {
            return greenSliderCaption;
        }

        public void setGreenSliderCaption(String greenSliderCaption) {
            this.greenSliderCaption = greenSliderCaption;
        }

        public String getRedSliderCaption() {
            return redSliderCaption;
        }

        public void setRedSliderCaption(String redSliderCaption) {
            this.redSliderCaption = redSliderCaption;
        }

        public String getHueSliderCaption() {
            return hueSliderCaption;
        }

        public void setHueSliderCaption(String hueSliderCaption) {
            this.hueSliderCaption = hueSliderCaption;
        }

        public String getSaturationSliderCaption() {
            return saturationSliderCaption;
        }

        public void setSaturationSliderCaption(String saturationSliderCaption) {
            this.saturationSliderCaption = saturationSliderCaption;
        }

        public String getValueSliderCaption() {
            return valueSliderCaption;
        }

        public void setValueSliderCaption(String valueSliderCaption) {
            this.valueSliderCaption = valueSliderCaption;
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

    public void setSwatchesVisible(boolean value) {
        field.setSwatchesVisibility(value);
    }

    public boolean getSwatchesVisible() {
        return field.getSwatchesVisibility();
    }

    public void setRGBVisible(boolean value) {
        field.setRGBVisibility(value);
    }

    public boolean getRGBVisible() {
        return field.getRGBVisibility();
    }

    public void setHSVVisible(boolean value) {
        field.setHSVVisibility(value);
    }

    public boolean getHSVVisible() {
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