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

package com.haulmont.cuba.web.widgets;

import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Slider;

public class CubaSlider<V extends Number> extends CustomField<V> {
    protected Slider slider;
    protected V internalValue;

    public CubaSlider() {
        this.slider = createComponent();
    }

    protected Slider createComponent() {
        return new Slider();
    }

    @Override
    protected Slider initContent() {
        return slider;
    }

    @Override
    protected void doSetValue(V value) {
        internalValue = value;
        Double sliderValue = value == null
                ? slider.getEmptyValue()
                : value.doubleValue();
        slider.setValue(sliderValue);
    }

    @Override
    public V getValue() {
        return internalValue;
    }

    @Override
    public boolean isReadOnly() {
        return slider.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        slider.setReadOnly(readOnly);
    }

    @Override
    public boolean isEnabled() {
        return slider.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        slider.setEnabled(enabled);
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);

        if (slider != null) {
            slider.setWidth(width);
        }
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);

        if (slider != null) {
            slider.setHeight(height);
        }
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (slider != null) {
            slider.setWidth(width, unit);
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        if (slider != null) {
            slider.setHeight(height, unit);
        }
    }

    public Slider getInternalComponent() {
        return slider;
    }

    public void setMin(double min) {
        slider.setMin(min);
    }

    public double getMin() {
        return slider.getMin();
    }

    public void setMax(double max) {
        slider.setMax(max);
    }

    public double getMax() {
        return slider.getMax();
    }

    public void setResolution(int resolution) {
        slider.setResolution(resolution);
    }

    public int getResolution() {
        return slider.getResolution();
    }

    public void setUpdateValueOnClick(boolean updateValueOnClick) {
        slider.setUpdateValueOnClick(updateValueOnClick);
    }

    public boolean isUpdateValueOnClick() {
        return slider.isUpdateValueOnClick();
    }

    public SliderOrientation getOrientation() {
        return slider.getOrientation();
    }

    public void setOrientation(SliderOrientation orientation) {
        slider.setOrientation(orientation);
    }
}
