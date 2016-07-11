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

        loadHSVVisibility(resultComponent, element);
        loadRGBVisibility(resultComponent, element);
        loadSwatchesVisibility(resultComponent, element);

        loadCancelButtonCaption(resultComponent, element);
        loadConfirmButtonCaption(resultComponent, element);

        loadLookupAllCaption(resultComponent, element);
        loadLookupBlueCaption(resultComponent, element);
        loadLookupGreenCaption(resultComponent, element);
        loadLookupRedCaption(resultComponent, element);

        loadPopupCaption(resultComponent, element);
        loadSwatchesTabCaption(resultComponent, element);
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
}
