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
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class FilterLoader extends AbstractComponentLoader<Filter> {

    public static final String DEFAULT_FILTER_ID = "filterWithoutId";
    public static final String FTS_MODE_VALUE = "fts";

    @Override
    protected void loadId(Component component, Element element) {
        super.loadId(component, element);

        if (Strings.isNullOrEmpty(component.getId())) {
            component.setId(DEFAULT_FILTER_ID);
        }
    }

    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(Filter.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadAlign(resultComponent, element);
        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);
        loadStyleName(resultComponent, element);
        loadMargin(resultComponent, element);
        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadWidth(resultComponent, element, "100%");
        loadCollapsible(resultComponent, element, true);
        loadSettingsEnabled(resultComponent, element);
        loadBorderVisible(resultComponent, element);

        String useMaxResults = element.attributeValue("useMaxResults");
        resultComponent.setUseMaxResults(useMaxResults == null || Boolean.parseBoolean(useMaxResults));

        String textMaxResults = element.attributeValue("textMaxResults");
        resultComponent.setTextMaxResults(Boolean.parseBoolean(textMaxResults));

        final String manualApplyRequired = element.attributeValue("manualApplyRequired");
        resultComponent.setManualApplyRequired(BooleanUtils.toBooleanObject(manualApplyRequired));

        String editable = element.attributeValue("editable");
        resultComponent.setEditable(editable == null || Boolean.parseBoolean(editable));

        String columnsCount = element.attributeValue("columnsCount");
        if (!Strings.isNullOrEmpty(columnsCount)) {
            resultComponent.setColumnsCount(Integer.parseInt(columnsCount));
        }

        String folderActionsEnabled = element.attributeValue("folderActionsEnabled");
        if (folderActionsEnabled != null) {
            resultComponent.setFolderActionsEnabled(Boolean.parseBoolean(folderActionsEnabled));
        }

        String dataLoaderId = element.attributeValue("dataLoader");
        if (!StringUtils.isBlank(dataLoaderId)) {
            FrameOwner frameOwner = context.getFrame().getFrameOwner();
            ScreenData screenData = UiControllerUtils.getScreenData(frameOwner);
            DataLoader dataLoader = screenData.getLoader(dataLoaderId);
            if (!(dataLoader instanceof CollectionLoader)) {
                throw new IllegalStateException(String.format("Filter cannot work with %s because it is not a CollectionLoader", dataLoaderId));
            }
            resultComponent.setDataLoader((CollectionLoader) dataLoader);

        } else {
            String datasource = element.attributeValue("datasource");
            if (!StringUtils.isBlank(datasource)) {
                CollectionDatasource ds = (CollectionDatasource) context.getDsContext().get(datasource);
                if (ds == null) {
                    throw new GuiDevelopmentException("Can't find datasource by name: " + datasource, context.getCurrentFrameId());
                }
                resultComponent.setDatasource(ds);
            }
        }

        Frame frame = context.getFrame();
        String applyTo = element.attributeValue("applyTo");
        if (!StringUtils.isEmpty(applyTo)) {
            context.addPostInitTask((c, w) -> {
                Component applyToComponent = frame.getComponent(applyTo);
                if (c == null) {
                    throw new GuiDevelopmentException("Can't apply component to component with ID: " + applyTo, context.getFullFrameId());
                }
                resultComponent.setApplyTo(applyToComponent);
            });
        }

        String modeSwitchVisible = element.attributeValue("modeSwitchVisible");
        if (StringUtils.isNotEmpty(modeSwitchVisible)) {
            resultComponent.setModeSwitchVisible(Boolean.parseBoolean(modeSwitchVisible));
        }

        context.addPostInitTask((context1, window) -> {
            ((FilterImplementation) resultComponent).loadFiltersAndApplyDefault();
            String defaultMode = element.attributeValue("defaultMode");
            if (FTS_MODE_VALUE.equals(defaultMode)) {
                resultComponent.switchFilterMode(FilterDelegate.FilterMode.FTS_MODE);
            }
        });
    }

    protected void loadBorderVisible(Filter resultComponent, Element element) {
        String borderVisible = element.attributeValue("borderVisible");
        if (StringUtils.isNotEmpty(borderVisible)) {
            resultComponent.setBorderVisible(Boolean.valueOf(borderVisible));
        }
    }
}
