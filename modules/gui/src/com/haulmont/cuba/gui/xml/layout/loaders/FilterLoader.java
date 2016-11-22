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

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.FilterImplementation;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.filter.FilterDelegate;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class FilterLoader extends AbstractComponentLoader<Filter> {

    public static final String DEFAULT_FILTER_ID = "filterWithoutId";

    @Override
    protected void loadId(Component component, Element element) {
        super.loadId(component, element);

        if (Strings.isNullOrEmpty(component.getId())) {
            component.setId(DEFAULT_FILTER_ID);
        }
    }

    @Override
    public void createComponent() {
        resultComponent = (Filter) factory.createComponent(Filter.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);
        loadStyleName(resultComponent, element);
        loadMargin(resultComponent, element);
        loadCaption(resultComponent, element);
        loadWidth(resultComponent, element, "100%");
        loadCollapsible(resultComponent, element, true);
        loadSettingsEnabled(resultComponent, element);

        String useMaxResults = element.attributeValue("useMaxResults");
        resultComponent.setUseMaxResults(useMaxResults == null || Boolean.parseBoolean(useMaxResults));

        String textMaxResults = element.attributeValue("textMaxResults");
        resultComponent.setTextMaxResults(Boolean.parseBoolean(textMaxResults));

        final String manualApplyRequired = element.attributeValue("manualApplyRequired");
        resultComponent.setManualApplyRequired(BooleanUtils.toBooleanObject(manualApplyRequired));

        String editable = element.attributeValue("editable");
        resultComponent.setEditable(editable == null || Boolean.parseBoolean(editable));

        String columnsQty = element.attributeValue("columnsCount");
        if (!Strings.isNullOrEmpty(columnsQty)) {
            resultComponent.setColumnsCount(Integer.parseInt(columnsQty));
        }

        String folderActionsEnabled = element.attributeValue("folderActionsEnabled");
        if (folderActionsEnabled != null) {
            resultComponent.setFolderActionsEnabled(Boolean.parseBoolean(folderActionsEnabled));
        }

        String datasource = element.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            CollectionDatasource ds = (CollectionDatasource) context.getDsContext().get(datasource);
            if (ds == null) {
                throw new GuiDevelopmentException("Can't find datasource by name: " + datasource, context.getCurrentFrameId());
            }
            resultComponent.setDatasource(ds);
        }

        Frame frame = context.getFrame();
        String applyTo = element.attributeValue("applyTo");
        if (!StringUtils.isEmpty(applyTo)) {
            context.addPostInitTask((context1, window) -> {
                Component c = frame.getComponent(applyTo);
                if (c == null) {
                    throw new GuiDevelopmentException("Can't apply component to component with ID: " + applyTo, context1.getFullFrameId());
                }
                resultComponent.setApplyTo(c);
            });
        }

        String modeSwitchVisible = element.attributeValue("modeSwitchVisible");
        resultComponent.setModeSwitchVisible(modeSwitchVisible == null || Boolean.parseBoolean(modeSwitchVisible));

        context.addPostInitTask((context1, window) -> {
            ((FilterImplementation) resultComponent).loadFiltersAndApplyDefault();
            String defaultMode = element.attributeValue("defaultMode");
            if (defaultMode != null &&"fts".equals(defaultMode)) {
                resultComponent.switchFilterMode(FilterDelegate.FilterMode.FTS_MODE);
            }
        });
    }
}
