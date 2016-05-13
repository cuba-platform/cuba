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

import com.haulmont.cuba.gui.components.ProgressBar;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 */
public class ProgressBarLoader extends AbstractComponentLoader<ProgressBar> {
    @Override
    public void createComponent() {
        resultComponent = (ProgressBar) factory.createComponent(ProgressBar.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadEditable(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadAlign(resultComponent, element);

        loadIndeterminate(resultComponent, element);
    }

    protected void loadIndeterminate(ProgressBar component, Element element) {
        String indeterminate = element.attributeValue("indeterminate");
        if (StringUtils.isNotEmpty(indeterminate)) {
            component.setIndeterminate(Boolean.parseBoolean(indeterminate));
        }
    }
}
