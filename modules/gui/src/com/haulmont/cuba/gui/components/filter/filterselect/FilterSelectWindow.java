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

package com.haulmont.cuba.gui.components.filter.filterselect;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.FilterEntity;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Window is used for selecting a (@code FilterEntity}
 * 
 */
public class FilterSelectWindow extends AbstractWindow {

    @Inject
    protected CollectionDatasource<FilterEntity, UUID> filterEntitiesDs;

    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    @Inject
    protected Table filterEntitiesTable;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected TextField nameFilterField;

    protected List<FilterEntity> filterEntities;

    protected Map<FilterEntity, String> captionsMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public void init(Map<String, Object> params) {
        ThemeConstants theme = themeConstantsManager.getConstants();
        getDialogOptions()
                .setHeight(Integer.valueOf(theme.get("cuba.gui.filterSelect.dialog.height")))
                .setWidth(Integer.valueOf(theme.get("cuba.gui.filterSelect.dialog.width")))
                .setResizable(true);

        filterEntitiesTable.addGeneratedColumn("name", new Table.ColumnGenerator<FilterEntity>() {
            @Override
            public Component generateCell(FilterEntity entity) {
                Label label = componentsFactory.createComponent(Label.class);
                String caption;
                if (Strings.isNullOrEmpty(entity.getCode())) {
                    caption = InstanceUtils.getInstanceName(entity);
                } else {
                    caption = messages.getMainMessage(entity.getCode());
                }
                label.setValue(caption);
                captionsMap.put(entity, caption);
                return label;
            }
        });

        filterEntities = (List<FilterEntity>) params.get("filterEntities");
        fillDatasource(null);

        filterEntitiesTable.setItemClickAction(new AbstractAction("selectByDblClk") {
            @Override
            public void actionPerform(Component component) {
                select();
            }

            @Override
            public String getCaption() {
                return messages.getMainMessage("filter.filterSelect.select");
            }
        });

        FilterHelper filterHelper = AppBeans.get(FilterHelper.class);
        filterHelper.addTextChangeListener(nameFilterField, new FilterHelper.TextChangeListener() {
            @Override
            public void textChanged(String text) {
                fillDatasource(text);
            }
        });
    }

    public void select() {
        FilterEntity item = filterEntitiesDs.getItem();
        if (item == null) {
            showNotification(messages.getMainMessage("filter.filterSelect.selectFilterEntity"), NotificationType.WARNING);
        } else {
            close(COMMIT_ACTION_ID);
        }
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }

    public FilterEntity getFilterEntity() {
        return filterEntitiesDs.getItem();
    }

    public void search() {
        fillDatasource(nameFilterField.getValue());
    }

    protected void fillDatasource(String nameFilterText) {
        filterEntitiesDs.clear();
        for (FilterEntity filterEntity : filterEntities) {
            if (passesFilter(filterEntity, nameFilterText)) {
                filterEntitiesDs.includeItem(filterEntity);
            }
        }
        filterEntitiesDs.refresh();
    }

    protected boolean passesFilter(FilterEntity filterEntity, String nameFilterText) {
        if (Strings.isNullOrEmpty(nameFilterText)) return true;
        String caption = captionsMap.get(filterEntity);
        return caption != null && caption.toLowerCase().contains(nameFilterText.toLowerCase());
    }
}