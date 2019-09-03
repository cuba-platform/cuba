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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Slider;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class SliderLoader extends AbstractFieldLoader<Slider> {
    @Override
    public void createComponent() {
        resultComponent = factory.create(Slider.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();
        loadDatatype(resultComponent, element);

        loadMin(resultComponent, element);
        loadMax(resultComponent, element);

        loadResolution(resultComponent, element);
        loadOrientation(resultComponent, element);

        loadUpdateValueOnClick(resultComponent, element);
    }

    protected void loadMin(Slider component, Element element) {
        String min = element.attributeValue("min");
        if (StringUtils.isNotEmpty(min)) {
            component.setMin(Double.parseDouble(min));
        }
    }

    protected void loadMax(Slider component, Element element) {
        String max = element.attributeValue("max");
        if (StringUtils.isNotEmpty(max)) {
            component.setMax(Double.parseDouble(max));
        }
    }

    protected void loadResolution(Slider component, Element element) {
        String resolution = element.attributeValue("resolution");
        if (StringUtils.isNotEmpty(resolution)) {
            component.setResolution(Integer.parseInt(resolution));
        }
    }

    protected void loadUpdateValueOnClick(Slider component, Element element) {
        String updateValueOnClick = element.attributeValue("updateValueOnClick");
        if (StringUtils.isNotEmpty(updateValueOnClick)) {
            component.setUpdateValueOnClick(Boolean.parseBoolean(updateValueOnClick));
        }
    }
}
