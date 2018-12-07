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

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.gui.components.data.options.ContainerOptions;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public abstract class AbstractOptionsBaseLoader<T extends OptionsField> extends AbstractFieldLoader<T> {
    @Override
    public void loadComponent() {
        super.loadComponent();
    }

    protected void loadCaptionProperty(T component, Element element) {
        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionProperty(captionProperty);
            component.setCaptionMode(CaptionMode.PROPERTY);
        }
    }

    @SuppressWarnings("unchecked")
    protected void loadOptionsEnum(T resultComponent, Element element) {
        String optionsEnumClass = element.attributeValue("optionsEnum");
        if (StringUtils.isNotEmpty(optionsEnumClass)) {
            resultComponent.setOptionsEnum(getScripting().loadClass(optionsEnumClass));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        String datasource = element.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(datasource)) {
            Datasource ds = context.getDsContext().get(datasource);
            ((T) component).setOptionsDatasource((CollectionDatasource) ds);
        }

        super.loadDatasource(component, element);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadContainer(T component, Element element) {
        super.loadContainer(component, element);

        String containerId = element.attributeValue("optionsContainer");
        if (containerId != null) {
            FrameOwner frameOwner = context.getFrame().getFrameOwner();
            ScreenData screenData = UiControllerUtils.getScreenData(frameOwner);
            InstanceContainer container = screenData.getContainer(containerId);
            if (!(container instanceof CollectionContainer)) {
                throw new GuiDevelopmentException("Not a CollectionContainer: " + containerId, context.getCurrentFrameId());
            }
            component.setOptions(new ContainerOptions((CollectionContainer) container));
        }
    }
}