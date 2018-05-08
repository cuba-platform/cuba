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
 *
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.OptionsList;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class OptionsListLoader extends AbstractOptionsBaseLoader<OptionsList> {
    @Override
    public void createComponent() {
        resultComponent = (OptionsList) factory.createComponent(OptionsList.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        String multiselect = element.attributeValue("multiselect");
        if (StringUtils.isNotEmpty(multiselect)) {
            resultComponent.setMultiSelect(Boolean.parseBoolean(multiselect));
        }

        loadCaptionProperty(resultComponent, element);
        loadOptionsEnum(resultComponent, element);
        loadNullOptionVisible(resultComponent, element);
        loadTabIndex(resultComponent, element);
    }

    protected void loadNullOptionVisible(OptionsList resultComponent, Element element) {
        String nullOptionVisible = element.attributeValue("nullOptionVisible");
        if (StringUtils.isNotEmpty(nullOptionVisible)) {
            resultComponent.setNullOptionVisible(Boolean.parseBoolean(nullOptionVisible));
        }
    }
}