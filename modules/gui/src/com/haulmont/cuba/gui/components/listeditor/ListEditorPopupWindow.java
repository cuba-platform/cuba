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
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.options.EnumOptions;
import com.haulmont.cuba.gui.components.data.options.MapOptions;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import org.apache.commons.lang3.BooleanUtils;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A dialog that is used for editing values of the {@link ListEditor} component.
 */
public class ListEditorPopupWindow extends AbstractWindow implements ListEditorWindowController {

    @Inject
    protected HBoxLayout addItemLayout;

    @Inject
    protected ScrollBoxLayout valuesLayout;

    @Inject
    protected UiComponents uiComponents;

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
    protected Options options;

    @WindowParam
    protected Class<? extends Enum> enumClass;

    @WindowParam
    protected Function<Object, String> optionCaptionProvider;

    @WindowParam
    protected Boolean editable;

    @WindowParam
    protected TimeZone timeZone;

    @WindowParam
    protected Collection<Consumer> validators;

    @Inject
    protected Metadata metadata;

    @Inject
    protected ThemeConstants theme;

    @Inject
    protected FilterHelper filterHelper;

    @Inject
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools;

    @Inject
    private Action commit;
    @Inject
    private Button commitBtn;

    protected Map<Object, String> valuesMap;

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
            commitBtn.focus();
        }
    }

    @Override
    public List<Object> getValue() {
        return new ArrayList<>(valuesMap.keySet());
    }

    protected void initValues() {
        if (values == null) {
            values = new ArrayList<>();
        }

        valuesMap = values.stream()
                .collect(Collectors.toMap(Function.identity(), o -> ListEditorHelper.getValueCaption(o, itemType,
                        timeZone, optionCaptionProvider)));

        for (Map.Entry<Object, String> entry : valuesMap.entrySet()) {
            addValueToLayout(entry.getKey(), entry.getValue());
        }
    }

    protected void initAddComponentLayout() {
        addItemLayout.removeAll();
        Field componentForAdding;

        if (options != null) {
            componentForAdding = createLookupField();
            //noinspection unchecked
            ((LookupField) componentForAdding).setOptions(options);
            addItemLayout.add(componentForAdding);
            addItemLayout.expand(componentForAdding);
        } else {
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

            componentForAdding.setId("listValueField");

            if (validators != null && !validators.isEmpty()) {
                for (Consumer validator : validators) {
                    //noinspection unchecked
                    componentForAdding.addValidator(validator);
                }
            }

            addItemLayout.add(componentForAdding);
            addItemLayout.expand(componentForAdding);

            componentForAdding.setEditable(editable);
            if (editable) {
                if (componentForAdding instanceof Component.Focusable) {
                    ((Component.Focusable) componentForAdding).focus();
                }
            }

            if (itemType != ListEditor.ItemType.ENTITY) {
                Button addBtn = uiComponents.create(Button.class);
                addBtn.setId("add");
                addBtn.setCaption(getMessage("actions.Add"));
                addBtn.addClickListener(e ->
                        _addValue(componentForAdding)
                );
                addItemLayout.add(addBtn);
                addBtn.setEnabled(editable);
            }
        }
    }

    protected void _addValue(Field componentForAdding) {
        Object value = componentForAdding.getValue();

        if (value != null && validate(Collections.singletonList(componentForAdding))) {
            componentForAdding.setValue(null);

            if (!valueExists(value)) {
                addValueToLayout(value, ListEditorHelper.getValueCaption(value, itemType, timeZone, optionCaptionProvider));
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected TextField createTextField(Datatype datatype) {
        TextField textField = uiComponents.create(TextField.class);
        textField.setDatatype(datatype);

        if (!BooleanUtils.isFalse(editable)) {
            FilterHelper.ShortcutListener shortcutListener = new FilterHelper.ShortcutListener("add", new KeyCombination(KeyCombination.Key.ENTER)) {
                @Override
                public void handleShortcutPressed() {
                    _addValue(textField);
                }
            };
            filterHelper.addShortcutListener(textField, shortcutListener);
        }
        return textField;
    }

    protected Field createComponentForEntity() {
        Preconditions.checkNotNullArgument(entityName, getMessage("entityNameParamNotDefined"));
        MetaClass metaClass = metadata.getClassNN(entityName);
        Field<?> componentForEntity;
        if (BooleanUtils.isNotTrue(useLookupField)) {
            PickerField pickerField = uiComponents.create(PickerField.class);
            pickerField.setMetaClass(metaClass);

            PickerField.LookupAction lookupAction;
            if (!Strings.isNullOrEmpty(entityJoinClause) || !Strings.isNullOrEmpty(entityWhereClause)) {
                lookupAction = dynamicAttributesGuiTools.createLookupAction(pickerField, entityJoinClause, entityWhereClause);
                pickerField.addAction(lookupAction);
            } else {
                lookupAction = pickerField.addLookupAction();
                if (!Strings.isNullOrEmpty(lookupScreen)) {
                    lookupAction.setLookupScreen(lookupScreen);
                }
            }
            componentForEntity = pickerField;

            lookupAction.setAfterLookupSelectionHandler(items -> {
                if (items != null && items.size() > 0) {
                    for (Object item : items) {
                        if (item != null && !valueExists(item)) {
                            this.addValueToLayout(item, ListEditorHelper.getValueCaption(item, itemType, timeZone));
                        }
                    }
                }
                componentForEntity.setValue(null);
            });
        } else {
            LookupField lookupField = uiComponents.create(LookupField.class);
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
            lookupField.setOptionCaptionProvider(optionCaptionProvider);
            componentForEntity = lookupField;

            componentForEntity.addValueChangeListener(e -> {
                Entity selectedEntity = (Entity) e.getValue();
                if (selectedEntity != null && !valueExists(selectedEntity)) {
                    this.addValueToLayout(selectedEntity, ListEditorHelper.getValueCaption(selectedEntity, itemType,
                            timeZone, optionCaptionProvider));
                }
                componentForEntity.setValue(null);
            });
        }
        return componentForEntity;
    }

    protected DateField createComponentForDate(DateField.Resolution resolution) {
        DateField dateField = uiComponents.create(DateField.class);
        dateField.setResolution(resolution);
        if (timeZone != null) {
            dateField.setTimeZone(timeZone);
        }
        return dateField;
    }

    protected LookupField createLookupField() {
        LookupField<?> lookupField = uiComponents.create(LookupField.class);
        lookupField.addValueChangeListener(e -> {
            Object selectedValue = e.getValue();
            if (selectedValue != null) {
                this.addValueToLayout(selectedValue, ListEditorHelper.getValueCaption(selectedValue, itemType,
                        timeZone, optionCaptionProvider));
            }
            lookupField.setValue(null);
        });
        lookupField.setOptionCaptionProvider(optionCaptionProvider);

        return lookupField;
    }

    protected LookupField createComponentForEnum() {
        if (enumClass == null) {
            throw new IllegalStateException("EnumClass parameter is not defined");
        }
        LookupField lookupField = createLookupField();
        //noinspection unchecked
        lookupField.setOptions(new EnumOptions(enumClass));
        return lookupField;
    }

    protected void addValueToLayout(final Object value, String str) {
        BoxLayout itemLayout = uiComponents.create(HBoxLayout.class);
        itemLayout.setId("itemLayout");
        itemLayout.setSpacing(true);

        Label<String> itemLab = uiComponents.create(Label.NAME);
        if (options instanceof MapOptions) {
            //noinspection unchecked
            Map<String, Object> optionsMap = ((MapOptions) options).getItemsCollection();
            str = optionsMap.entrySet()
                    .stream()
                    .filter(entry -> Objects.equals(entry.getValue(), value))
                    .findFirst()
                    .get().getKey();
        }
        itemLab.setValue(str);
        itemLayout.add(itemLab);
        itemLab.setAlignment(Alignment.MIDDLE_LEFT);

        LinkButton delItemBtn = uiComponents.create(LinkButton.class);
        delItemBtn.setIcon("icons/item-remove.png");
        delItemBtn.addClickListener(e -> {
            valuesMap.remove(value);
            valuesLayout.remove(itemLayout);
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