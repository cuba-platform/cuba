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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.ColorPicker;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class ColorPickerLoader extends AbstractFieldLoader<ColorPicker>{

    @Override
    public void createComponent() {
        resultComponent = (ColorPicker) factory.createComponent(ColorPicker.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadTabIndex(resultComponent, element);

        loadHSVVisibility(resultComponent, element);
        loadRGBVisibility(resultComponent, element);
        loadSwatchesVisibility(resultComponent, element);
        loadHistoryVisibility(resultComponent, element);
        loadDefaultCaptionEnabled(resultComponent, element);

        loadCancelButtonCaption(resultComponent, element);
        loadConfirmButtonCaption(resultComponent, element);

        loadLookupAllCaption(resultComponent, element);
        loadLookupBlueCaption(resultComponent, element);
        loadLookupGreenCaption(resultComponent, element);
        loadLookupRedCaption(resultComponent, element);
        loadButtonCaption(resultComponent, element);

        loadRedSliderCaption(resultComponent, element);
        loadGreenSliderCaption(resultComponent, element);
        loadBlueSliderCaption(resultComponent, element);
        loadHueSliderCaption(resultComponent, element);
        loadSaturationSliderCaption(resultComponent, element);
        loadValueSliderCaption(resultComponent, element);

        loadPopupCaption(resultComponent, element);
        loadSwatchesTabCaption(resultComponent, element);
        loadResponsive(resultComponent, element);
    }

    protected void loadRedSliderCaption(ColorPicker component, Element element) {
        String redSliderCaption = element.attributeValue("redSliderCaption");
        if (StringUtils.isNotEmpty(redSliderCaption)) {
            component.setRedSliderCaption(loadResourceString(redSliderCaption));
        }
    }

    protected void loadGreenSliderCaption(ColorPicker component, Element element) {
        String greenSliderCaption = element.attributeValue("greenSliderCaption");
        if (StringUtils.isNotEmpty(greenSliderCaption)) {
            component.setGreenSliderCaption(loadResourceString(greenSliderCaption));
        }
    }

    protected void loadBlueSliderCaption(ColorPicker component, Element element) {
        String blueSliderCaption = element.attributeValue("blueSliderCaption");
        if (StringUtils.isNotEmpty(blueSliderCaption)) {
            component.setBlueSliderCaption(loadResourceString(blueSliderCaption));
        }
    }

    protected void loadHueSliderCaption(ColorPicker component, Element element) {
        String hueSliderCaption = element.attributeValue("hueSliderCaption");
        if (StringUtils.isNotEmpty(hueSliderCaption)) {
            component.setHueSliderCaption(loadResourceString(hueSliderCaption));
        }
    }

    protected void loadSaturationSliderCaption(ColorPicker component, Element element) {
        String saturationSliderCaption = element.attributeValue("saturationSliderCaption");
        if (StringUtils.isNotEmpty(saturationSliderCaption)) {
            component.setSaturationSliderCaption(loadResourceString(saturationSliderCaption));
        }
    }

    protected void loadValueSliderCaption(ColorPicker component, Element element) {
        String valueSliderCaption = element.attributeValue("valueSliderCaption");
        if (StringUtils.isNotEmpty(valueSliderCaption)) {
            component.setValueSliderCaption(loadResourceString(valueSliderCaption));
        }
    }

    protected void loadPopupCaption(ColorPicker component, Element element) {
        String popupCaption = element.attributeValue("popupCaption");
        if (StringUtils.isNotEmpty(popupCaption)) {
            component.setPopupCaption(loadResourceString(popupCaption));
        }
    }

    protected void loadConfirmButtonCaption(ColorPicker component, Element element) {
        String confirmButtonCaption = element.attributeValue("confirmButtonCaption");
        if (StringUtils.isNotEmpty(confirmButtonCaption)) {
            component.setConfirmButtonCaption(loadResourceString(confirmButtonCaption));
        }
    }

    protected void loadCancelButtonCaption(ColorPicker component, Element element) {
        String cancelButtonCaption = element.attributeValue("cancelButtonCaption");
        if (StringUtils.isNotEmpty(cancelButtonCaption)) {
            component.setCancelButtonCaption(loadResourceString(cancelButtonCaption));
        }
    }

    protected void loadSwatchesTabCaption(ColorPicker component, Element element) {
        String swatchesTabCaption = element.attributeValue("swatchesTabCaption");
        if (StringUtils.isNotEmpty(swatchesTabCaption)) {
            component.setSwatchesTabCaption(loadResourceString(swatchesTabCaption));
        }
    }

    protected void loadLookupAllCaption(ColorPicker component, Element element) {
        String lookupAllCaption = element.attributeValue("lookupAllCaption");
        if (StringUtils.isNotEmpty(lookupAllCaption)) {
            component.setLookupAllCaption(loadResourceString(lookupAllCaption));
        }
    }

    protected void loadLookupRedCaption(ColorPicker component, Element element) {
        String lookupRedCaption = element.attributeValue("lookupRedCaption");
        if (StringUtils.isNotEmpty(lookupRedCaption)) {
            component.setLookupRedCaption(loadResourceString(lookupRedCaption));
        }
    }

    protected void loadLookupGreenCaption(ColorPicker component, Element element) {
        String lookupGreenCaption = element.attributeValue("lookupGreenCaption");
        if (StringUtils.isNotEmpty(lookupGreenCaption)) {
            component.setLookupGreenCaption(loadResourceString(lookupGreenCaption));
        }
    }

    protected void loadLookupBlueCaption(ColorPicker component, Element element) {
        String lookupBlueCaption = element.attributeValue("lookupBlueCaption");
        if (StringUtils.isNotEmpty(lookupBlueCaption)) {
            component.setLookupBlueCaption(loadResourceString(lookupBlueCaption));
        }
    }

    protected void loadSwatchesVisibility(ColorPicker component, Element element) {
        String swatchesVisible = element.attributeValue("swatchesVisible");
        if (StringUtils.isNotEmpty(swatchesVisible)) {
            component.setSwatchesVisible(BooleanUtils.toBoolean(swatchesVisible));
        }
    }

    protected void loadRGBVisibility(ColorPicker component, Element element) {
        String rgbVisible = element.attributeValue("rgbVisible");
        if (StringUtils.isNotEmpty(rgbVisible)) {
            component.setRGBVisible(BooleanUtils.toBoolean(rgbVisible));
        }
    }

    protected void loadHSVVisibility(ColorPicker component, Element element) {
        String hsvVisible = element.attributeValue("hsvVisible");
        if (StringUtils.isNotEmpty(hsvVisible)) {
            component.setHSVVisible(BooleanUtils.toBoolean(hsvVisible));
        }
    }

    protected void loadHistoryVisibility(ColorPicker component, Element element) {
        String historyVisible = element.attributeValue("historyVisible");
        if (StringUtils.isNotEmpty(historyVisible)) {
            component.setHistoryVisible(BooleanUtils.toBoolean(historyVisible));
        }
    }

    protected void loadDefaultCaptionEnabled(ColorPicker component, Element element) {
        String defaultCaptionEnabled = element.attributeValue("defaultCaptionEnabled");
        if (StringUtils.isNotEmpty(defaultCaptionEnabled)) {
            component.setDefaultCaptionEnabled(BooleanUtils.toBoolean(defaultCaptionEnabled));
        }
    }

    protected void loadButtonCaption(ColorPicker component, Element element) {
        String buttonCaption = element.attributeValue("buttonCaption");
        if (StringUtils.isNotEmpty(buttonCaption)) {
            component.setButtonCaption(loadResourceString(buttonCaption));
        }
    }
}
