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

package com.haulmont.cuba.gui.app.core.categories;

import com.haulmont.cuba.core.entity.AttributeLocaleData;
import com.haulmont.cuba.core.entity.LocaleHelper;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.EditAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.components.data.datagrid.ContainerDataGridItems;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataComponents;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

public abstract class AbstractLocalizedTextFieldsFrame extends AbstractFrame {

    @Inject
    protected GlobalConfig globalConfig;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected Actions actions;
    @Inject
    protected Notifications notifications;
    @Inject
    protected Metadata metadata;
    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected MessageTools messageTools;
    @Inject
    private DataComponents dataComponents;

    protected static String LANGUAGE_WITH_CODE = "languageWithCode";
    protected static String NAME = "name";
    protected static String DESCRIPTION = "description";

    protected CollectionContainer<AttributeLocaleData> collectionContainer;
    protected DataGrid<AttributeLocaleData> dataGrid;

    @Override
    public void init(Map<String, Object> params) {
        Map<String, Locale> map = globalConfig.getAvailableLocales();

        dataGrid = uiComponents.create(DataGrid.NAME);
        initEditAction(dataGrid);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("250px");

        createColumns(dataGrid);

        dataGrid.setItems(getDataGridItems(map));
        dataGrid.setSortable(false);
        dataGrid.setColumnReorderingAllowed(false);
        dataGrid.setColumnsCollapsingAllowed(false);

        configureColumns(dataGrid);

        add(dataGrid);
    }

    protected DataGridItems<AttributeLocaleData> getDataGridItems(Map<String, Locale> map) {
        List<AttributeLocaleData> attributeLocaleDataList = new ArrayList<>();
        collectionContainer = dataComponents.createCollectionContainer(AttributeLocaleData.class);

        for (Map.Entry<String, Locale> entry : map.entrySet()) {
            AttributeLocaleData attributeLocaleData = metadata.create(AttributeLocaleData.class);
            attributeLocaleData.setLanguage(entry.getKey());
            attributeLocaleData.setLocale(entry.getValue().toString());
            attributeLocaleDataList.add(attributeLocaleData);
        }

        collectionContainer.setItems(attributeLocaleDataList);
        return new ContainerDataGridItems<>(collectionContainer);
    }

    protected void initEditAction(DataGrid<AttributeLocaleData> dataGrid) {
        dataGrid.setEditorEnabled(true);
        dataGrid.addAction(actions.create(EditAction.class)
                .withHandler(actionPerformedEvent -> {
            AttributeLocaleData selected = dataGrid.getSingleSelected();
            if (selected != null) {
                dataGrid.edit(selected);
            } else {
                notifications.create()
                        .withCaption("Item is not selected")
                        .show();
            }
        }));
    }

    protected void setColumnDescriptionProvider(DataGrid.Column<AttributeLocaleData> column, @Nullable String paramName) {
        String localeDescription = getMessage("localeDataDescription");

        column.setDescriptionProvider(attributeLocaleData -> {
            if (paramName != null) {
                String paramValue = attributeLocaleData.getValue(paramName);
                if (StringUtils.isNotEmpty(paramValue)) {
                    return formatMessage("localeDataDescriptionWithValue", paramValue);
                }
            }
            return localeDescription;
        });
    }

    protected void setValues(String localeBundle, String paramName) {
        Map<String, String> localizedNamesMap = LocaleHelper.getLocalizedValuesMap(localeBundle);

        for (AttributeLocaleData attributeLocaleData : collectionContainer.getItems()) {
            attributeLocaleData.setValue(paramName, localizedNamesMap.get(attributeLocaleData.getLocale()));
        }
    }

    protected String getValues(String paramName) {
        Properties properties = new Properties();

        for (AttributeLocaleData attributeLocaleData : collectionContainer.getItems()) {
            String value = attributeLocaleData.getValue(paramName);
            if (attributeLocaleData.getName() != null && value != null) {
                properties.put(attributeLocaleData.getLocale(), value);
            }
        }

        return LocaleHelper.convertPropertiesToString(properties);
    }

    protected abstract void createColumns(DataGrid<AttributeLocaleData> dataGrid);

    protected abstract void configureColumns(DataGrid<AttributeLocaleData> dataGrid);
}
