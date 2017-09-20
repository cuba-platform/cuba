/*
 * Copyright (c) 2008-2017 Haulmont.
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

import com.haulmont.cuba.core.entity.CategoryAttributeEnumValue;
import com.haulmont.cuba.core.entity.LocaleHelper;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.listeditor.ListEditorHelper;
import com.haulmont.cuba.gui.components.listeditor.ListEditorWindowController;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class LocalizedEnumerationWindow extends AbstractWindow implements ListEditorWindowController {

    @Inject
    protected Button addBtn;

    @Inject
    protected TextField valueField;

    @Inject
    protected LocalizedNameFrame localizedFrame;

    @Inject
    protected Table<CategoryAttributeEnumValue> enumValuesTable;

    @Inject
    protected CollectionDatasource<CategoryAttributeEnumValue, UUID> enumValuesDs;

    @Inject
    protected ComponentsFactory factory;

    @WindowParam
    protected List<Object> values;

    @WindowParam
    protected Map<Object, String> valuesMap;

    @WindowParam
    protected String enumerationLocales;

    @Inject
    private Action add;

    @Override
    public void init(Map<String, Object> params) {
        enumValuesTable.addGeneratedColumn("cancel", entity -> {
            LinkButton delItemBtn = factory.createComponent(LinkButton.class);
            delItemBtn.setIcon("icons/item-remove.png");
            delItemBtn.setAction(new AbstractAction("") {
                @Override
                public void actionPerform(Component component) {
                    enumValuesDs.removeItem(entity);
                    valuesMap.remove(entity.getValue());
                }
            });
            return delItemBtn;
        });
        enumValuesTable.setColumnWidth("cancel", 30);

        enumValuesDs.addItemChangeListener(e -> {
            if (e.getPrevItem() == null) { // if the first time selected
                localizedFrame.setEditableFields(true);
            } else {
                e.getPrevItem().setLocalizedValues(localizedFrame.getValue());
            }
            if (e.getItem() == null) { // if item deleted and selection disappeared
                localizedFrame.clearFields();
                localizedFrame.setEditableFields(false);
            } else {
                String localizedValues = e.getItem().getLocalizedValues() == null ? "" : e.getItem().getLocalizedValues();
                localizedFrame.setValue(localizedValues);
            }
        });

        addBtn.setAction(add);

        initValues();
    }

    @Override
    public void ready() {
        localizedFrame.setEditableFields(false);
    }

    protected void initValues() {
        if (values == null) {
            values = new ArrayList<>();
        }

        valuesMap = values.stream()
                .collect(Collectors.toMap(Function.identity(), o -> ListEditorHelper.getValueCaption(o, ListEditor.ItemType.STRING)));

        Map<String, String> localizedValues = LocaleHelper.getLocalizedValuesMap(enumerationLocales);

        for (Map.Entry<Object, String> entry : valuesMap.entrySet()) {
            String localizedEnum = localizedValues.get(entry.getValue());
            addValueToDatasource(entry.getKey(), localizedEnum.replace("\\r\\n", "\r\n"));
        }

        enumValuesDs.commit();
    }

    @Override
    public List<Object> getValue() {
        return new ArrayList<>(valuesMap.keySet());
    }

    public String getLocalizedValues() {
        Properties properties = new Properties();
        for (CategoryAttributeEnumValue enumValue : enumValuesDs.getItems()) {
            if (enumValue.getLocalizedValues() == null) {
                properties.put(enumValue.getValue(), "");
            } else {
                String localizedValues = enumValue.getLocalizedValues().replaceAll("\r\n", "\\\\r\\\\n");
                properties.put(enumValue.getValue(), localizedValues);
            }
        }

        enumerationLocales = LocaleHelper.convertPropertiesToString(properties);
        return enumerationLocales;
    }

    protected boolean valueExists(Object value) {
        return valuesMap.keySet().contains(value);
    }

    protected void addValueToDatasource(Object value, String enumLocaleValues) {
        CategoryAttributeEnumValue enumValue = new CategoryAttributeEnumValue();
        enumValue.setValue(value.toString());
        enumValue.setLocalizedValues(enumLocaleValues);
        enumValuesDs.addItem(enumValue);
    }

    public void addEnumValue() {
        Object value = valueField.getValue();
        if (value != null) {
            if (!valueExists(value)) {
                valueField.setValue(null);
                addValueToDatasource(value, null);
                valuesMap.put(value, ListEditorHelper.getValueCaption(value, ListEditor.ItemType.STRING));
            }
        }
    }

    public void commit() {
        if (!enumValuesTable.getSelected().isEmpty()) {
            CategoryAttributeEnumValue enumValue = enumValuesTable.getSelected().iterator().next();
            enumValue.setLocalizedValues(localizedFrame.getValue());
        }
        enumValuesDs.commit();

        close(COMMIT_ACTION_ID);
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }
}
