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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ColorPicker;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.CubaColorPicker;
import com.haulmont.cuba.web.toolkit.ui.converters.ColorStringConverter;

public class WebColorPicker extends WebAbstractField<CubaColorPicker> implements ColorPicker {

    public WebColorPicker() {
        component = new CubaColorPicker();
        component.setConverter(new ColorStringConverter());
        attachListener(component);
        setCaptions();
    }

    protected void setCaptions() {
        Messages messages = AppBeans.get(Messages.NAME);
        component.setPopupCaption(messages.getMainMessage("colorPicker.popupCaption"));
        component.setSwatchesTabCaption(messages.getMainMessage("colorPicker.swatchesTabCaption"));
        component.setConfirmButtonCaption(messages.getMainMessage("colorPicker.confirmButtonCaption"));
        component.setCancelButtonCaption(messages.getMainMessage("colorPicker.cancelButtonCaption"));

        component.setLookupAllCaption(messages.getMainMessage("colorPicker.lookupAll"));
        component.setLookupRedCaption(messages.getMainMessage("colorPicker.lookupRed"));
        component.setLookupGreenCaption(messages.getMainMessage("colorPicker.lookupGreen"));
        component.setLookupBlueCaption(messages.getMainMessage("colorPicker.lookupBlue"));

        component.setRedSliderCaption(messages.getMainMessage("colorPicker.redSliderCaption"));
        component.setGreenSliderCaption(messages.getMainMessage("colorPicker.greenSliderCaption"));
        component.setBlueSliderCaption(messages.getMainMessage("colorPicker.blueSliderCaption"));
        component.setHueSliderCaption(messages.getMainMessage("colorPicker.hueSliderCaption"));
        component.setSaturationSliderCaption(messages.getMainMessage("colorPicker.saturationSliderCaption"));
        component.setValueSliderCaption(messages.getMainMessage("colorPicker.valueSliderCaption"));
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public void setSwatchesVisible(boolean value) {
        component.setSwatchesVisible(value);
    }

    @Override
    public boolean getSwatchesVisible() {
        return component.getSwatchesVisible();
    }

    @Override
    public void setRGBVisible(boolean value) {
        component.setRGBVisible(value);
    }

    @Override
    public boolean getRGBVisible() {
        return component.getRGBVisible();
    }

    @Override
    public void setHSVVisible(boolean value) {
        component.setHSVVisible(value);
    }

    @Override
    public boolean getHSVVisible() {
        return component.getHSVVisible();
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getValue() {
        return (String) component.getConvertedValue();
    }

    @Override
    public void setPopupCaption(String popupCaption) {
        component.setPopupCaption(popupCaption);
    }

    @Override
    public String getPopupCaption() {
        return component.getPopupCaption();
    }

    @Override
    public void setConfirmButtonCaption(String caption) {
        component.setConfirmButtonCaption(caption);
    }

    @Override
    public String getConfirmButtonCaption() {
        return component.getConfirmButtonCaption();
    }

    @Override
    public void setCancelButtonCaption(String caption) {
        component.setCancelButtonCaption(caption);
    }

    @Override
    public String getCancelButtonCaption() {
        return component.getCancelButtonCaption();
    }

    @Override
    public void setSwatchesTabCaption(String caption) {
        component.setSwatchesTabCaption(caption);
    }

    @Override
    public String getSwatchesTabCaption() {
        return component.getSwatchesTabCaption();
    }

    @Override
    public void setLookupAllCaption(String caption) {
        component.setLookupAllCaption(caption);
    }

    @Override
    public String getLookupAllCaption() {
        return component.getLookupAllCaption();
    }

    @Override
    public void setLookupRedCaption(String caption) {
        component.setLookupRedCaption(caption);
    }

    @Override
    public String getLookupRedCaption() {
        return component.getLookupRedCaption();
    }

    @Override
    public void setLookupGreenCaption(String caption) {
        component.setLookupGreenCaption(caption);
    }

    @Override
    public String getLookupGreenCaption() {
        return component.getLookupGreenCaption();
    }

    @Override
    public void setLookupBlueCaption(String caption) {
        component.setLookupBlueCaption(caption);
    }

    @Override
    public String getLookupBlueCaption() {
        return component.getLookupBlueCaption();
    }

    @Override
    public void setRedSliderCaption(String caption) {
        component.setRedSliderCaption(caption);
    }

    @Override
    public String getRedSliderCaption() {
        return component.getRedSliderCaption();
    }

    @Override
    public void setGreenSliderCaption(String caption) {
        component.setGreenSliderCaption(caption);
    }

    @Override
    public String getGreenSliderCaption() {
        return component.getGreenSliderCaption();
    }

    @Override
    public void setBlueSliderCaption(String caption) {
        component.setBlueSliderCaption(caption);
    }

    @Override
    public String getBlueSliderCaption() {
        return component.getBlueSliderCaption();
    }

    @Override
    public void setHueSliderCaption(String caption) {
        component.setHueSliderCaption(caption);
    }

    @Override
    public String getHueSliderCaption() {
        return component.getHueSliderCaption();
    }

    @Override
    public void setSaturationSliderCaption(String caption) {
        component.setSaturationSliderCaption(caption);
    }

    @Override
    public String getSaturationSliderCaption() {
        return component.getSaturationSliderCaption();
    }

    @Override
    public void setValueSliderCaption(String caption) {
        component.setValueSliderCaption(caption);
    }

    @Override
    public String getValueSliderCaption() {
        return component.getValueSliderCaption();
    }
}
