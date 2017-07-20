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

package com.haulmont.cuba.gui.components.listeditor;

import com.google.common.base.Strings;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A dialog that is used for editing values of the {@link ListEditor} component.
 */
public class ListEditorPopupWindow extends AbstractWindow {

    @Inject
    protected HBoxLayout addItemLayout;

    @Inject
    protected ScrollBoxLayout valuesLayout;

    @Inject
    protected ComponentsFactory componentsFactory;

    @WindowParam
    protected String entityName;

    @WindowParam
    protected String lookupScreen;

    @WindowParam
    protected Boolean useLookupField;

    @WindowParam
    protected String entityJoinClause;

    @WindowParam
    protected String entityWhereClause;

    @WindowParam(required = true)
    protected ListEditor.ItemType itemType;

    @WindowParam
    protected List<Object> values;

    @WindowParam
    protected List<Object> optionsList;

    @WindowParam
    protected Map<String, Object> optionsMap;

    @WindowParam
    protected Class<? extends Enum> enumClass;

    @WindowParam
    protected Boolean editable;

    @Inject
    protected Metadata metadata;

    @Inject
    protected ThemeConstants theme;

    @Inject
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools;

    protected Map<Object, String> valuesMap;

    @Inject
    private Action commit;
    @Inject
    private Button commitBtn;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogOptions()
                .setWidth(theme.get("cuba.gui.listEditor.popup.dialog.width"))
                .setHeight(theme.get("cuba.gui.listEditor.popup.dialog.height"))
                .setResizable(true);

        if (editable == null) {
            editable = true;
        }

        initAddComponentLayout();
        initValues();

        commit.setEnabled(editable);

        if (!editable) {
            commitBtn.requestFocus();
        }
    }

    public List<Object> getValue() {
        return new ArrayList<>(valuesMap.keySet());
    }

    protected void initValues() {
        if (values == null) {
            values = new ArrayList<>();
        }

        valuesMap = values.stream()
                .collect(Collectors.toMap(Function.identity(), o -> ListEditorHelper.getValueCaption(o, itemType)));

        for (Map.Entry<Object, String> entry : valuesMap.entrySet()) {
            addValueToLayout(entry.getKey(), entry.getValue());
        }
    }

    protected void initAddComponentLayout() {
        addItemLayout.removeAll();
        final Field componentForAdding;

        if (optionsMap != null) {
            componentForAdding = createLookupField();
            ((LookupField)componentForAdding).setOptionsMap(optionsMap);
            addItemLayout.add(componentForAdding);
            addItemLayout.expand(componentForAdding);
        } else if (optionsList != null) {
            componentForAdding = createLookupField();
            ((LookupField)componentForAdding).setOptionsList(optionsList);
            addItemLayout.add(componentForAdding);
            addItemLayout.expand(componentForAdding);
        } else {
            componentsFactory.createComponent(HBoxLayout.class);
            switch (itemType) {
                case ENTITY:
                    componentForAdding = createComponentForEntity();
                    break;
                case INTEGER:
                    componentForAdding = createTextField(Datatypes.get(Integer.class));
                    break;
                case LONG:
                    componentForAdding = createTextField(Datatypes.get(Long.class));
                    break;
                case DOUBLE:
                    componentForAdding = createTextField(Datatypes.get(Double.class));
                    break;
                case BIGDECIMAL:
                    componentForAdding = createTextField(Datatypes.get(BigDecimal.class));
                    break;
                case STRING:
                    componentForAdding = createTextField(Datatypes.get(String.class));
                    break;
                case UUID:
                    componentForAdding = createTextField(Datatypes.get(UUID.class));
                    break;
                case DATE:
                    componentForAdding = createComponentForDate(DateField.Resolution.DAY);
                    break;
                case DATETIME:
                    componentForAdding = createComponentForDate(DateField.Resolution.MIN);
                    break;
                case ENUM:
                    componentForAdding = createComponentForEnum();
                    break;
                default:
                    throw new IllegalStateException("Cannot process the itemType " + itemType);
            }

            addItemLayout.add(componentForAdding);
            addItemLayout.expand(componentForAdding);

            componentForAdding.setEditable(editable);
            if (editable) {
                componentForAdding.requestFocus();
            }

            if (itemType != ListEditor.ItemType.ENTITY) {
                Button addBtn = componentsFactory.createComponent(Button.class);
                addBtn.setAction(new AbstractAction("add") {
                    @Override
                    public void actionPerform(Component component) {
                        _addValue(componentForAdding);
                    }
                });
                addBtn.setCaption(getMessage("actions.Add"));
                addItemLayout.add(addBtn);
                addBtn.setEnabled(editable);
            }
        }
    }

    protected void _addValue(Field componentForAdding) {
        Object value = componentForAdding.getValue();
        if (value != null) {
            componentForAdding.setValue(null);
            if (!valueExists(value)) {
                addValueToLayout(value, ListEditorHelper.getValueCaption(value, itemType));
            }
        }
    }

    protected TextField createTextField(Datatype datatype) {
        TextField textField = componentsFactory.createComponent(TextField.class);
        textField.setDatatype(datatype);

        if (!BooleanUtils.isFalse(editable)) {
            FilterHelper.ShortcutListener shortcutListener = new FilterHelper.ShortcutListener("add", new KeyCombination(KeyCombination.Key.ENTER)) {
                @Override
                public void handleShortcutPressed() {
                    _addValue(textField);
                }
            };
            AppBeans.get(FilterHelper.class).addShortcutListener(textField, shortcutListener);
        }
        return textField;
    }

    protected Field createComponentForEntity() {
        Preconditions.checkNotNullArgument(entityName, getMessage("entityNameParamNotDefined"));
        MetaClass metaClass = metadata.getClassNN(entityName);
        Field componentForEntity;
        if (BooleanUtils.isNotTrue(useLookupField)) {
            PickerField pickerField = componentsFactory.createComponent(PickerField.class);
            pickerField.setMetaClass(metaClass);

            if (!Strings.isNullOrEmpty(entityJoinClause) || !Strings.isNullOrEmpty(entityWhereClause)) {
                PickerField.LookupAction lookupAction = dynamicAttributesGuiTools.createLookupAction(pickerField, entityJoinClause, entityWhereClause);
                pickerField.addAction(lookupAction);
            } else {
                pickerField.addLookupAction();
                if (!Strings.isNullOrEmpty(lookupScreen)) {
                    PickerField.LookupAction lookupAction = (PickerField.LookupAction) pickerField.getAction(PickerField.LookupAction.NAME);
                    if (lookupAction != null) {
                        lookupAction.setLookupScreen(lookupScreen);
                    }
                }
            }
            componentForEntity = pickerField;
        } else {
            LookupField lookupField = componentsFactory.createComponent(LookupField.class);
            CollectionDatasource optionsDs;
            if (!Strings.isNullOrEmpty(entityJoinClause) || !Strings.isNullOrEmpty(entityWhereClause)) {
                optionsDs = dynamicAttributesGuiTools.createOptionsDatasourceForLookup(metaClass, entityJoinClause, entityWhereClause);
            } else {
                optionsDs = DsBuilder.create()
                        .setMetaClass(metaClass)
                        .setViewName(View.MINIMAL)
                        .buildCollectionDatasource();
                optionsDs.refresh();
            }
            lookupField.setOptionsDatasource(optionsDs);
            componentForEntity = lookupField;
        }
        componentForEntity.addValueChangeListener(e -> {
            Entity selectedEntity = (Entity) e.getValue();
            if (selectedEntity != null && !valueExists(selectedEntity)) {
                this.addValueToLayout(selectedEntity, ListEditorHelper.getValueCaption(selectedEntity, itemType));
            }
            componentForEntity.setValue(null);
        });
        return componentForEntity;
    }

    protected DateField createComponentForDate(DateField.Resolution resolution) {
        DateField dateField = componentsFactory.createComponent(DateField.class);
        dateField.setResolution(resolution);
        return dateField;
    }

    protected LookupField createLookupField() {
        LookupField lookupField = componentsFactory.createComponent(LookupField.class);
        lookupField.addValueChangeListener(e -> {
            Object selectedValue = e.getValue();
            if (selectedValue != null) {
                this.addValueToLayout(selectedValue, ListEditorHelper.getValueCaption(selectedValue, itemType));
            }
            lookupField.setValue(null);
        });

        return lookupField;
    }

    protected LookupField createComponentForEnum() {
        if (enumClass == null) {
            throw new IllegalStateException("EnumClass parameter is not defined");
        }
        LookupField lookupField = createLookupField();
        Enum[] enumConstants = enumClass.getEnumConstants();
        Map<String, Enum> enumValuesMap = Stream.of(enumConstants)
                .collect(Collectors.toMap(o -> messages.getMessage(o), Function.identity()));
        lookupField.setOptionsMap(enumValuesMap);
        return lookupField;
    }

    protected void addValueToLayout(final Object value, String str) {
        final BoxLayout itemLayout = componentsFactory.createComponent(HBoxLayout.class);
        itemLayout.setSpacing(true);

        Label itemLab = componentsFactory.createComponent(Label.class);
        itemLab.setValue(str);
        itemLayout.add(itemLab);
        itemLab.setAlignment(Alignment.MIDDLE_LEFT);

        LinkButton delItemBtn = componentsFactory.createComponent(LinkButton.class);
        delItemBtn.setIcon("icons/item-remove.png");
        delItemBtn.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                valuesMap.remove(value);
                valuesLayout.remove(itemLayout);
            }
        });
        itemLayout.add(delItemBtn);

        if (BooleanUtils.isFalse(editable)) {
            delItemBtn.setEnabled(false);
        }

        valuesLayout.add(itemLayout);
        valuesMap.put(value, str);
    }

    protected boolean valueExists(Object value) {
        return valuesMap.keySet().contains(value);
    }

    public void commit() {
        close(COMMIT_ACTION_ID);
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }
}