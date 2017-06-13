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

import com.google.common.base.Joiner;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(ListEditorDelegate.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ListEditorDelegateImpl implements ListEditorDelegate {

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected WindowManagerProvider windowManagerProvider;

    @Inject
    protected WindowConfig windowConfig;

    protected Field actualField;

    protected List value;
    protected List prevValue;

    protected ListEditor.ItemType itemType;
    protected String entityName;
    protected String lookupScreen;
    protected boolean useLookupField;
    protected List<?> optionsList;
    protected Map<String, Object> optionsMap;
    protected String entityJoinClause;
    protected String entityWhereClause;
    protected Class<? extends Enum> enumClass;

    protected TextField displayValuesField;
    protected HBoxLayout layout;
    protected Button clearBtn;

    protected boolean displayDescription = true;

    protected boolean editable = true;

    @PostConstruct
    public void init() {
        WindowManager windowManager = windowManagerProvider.get();

        layout = componentsFactory.createComponent(HBoxLayout.class);
        layout.setStyleName("c-listeditor-layout");
        layout.setWidth("100%");

        displayValuesField = componentsFactory.createComponent(TextField.class);
        displayValuesField.setStyleName("c-listeditor-text");
        displayValuesField.setEditable(false);
        Button openEditorBtn = componentsFactory.createComponent(Button.class);
        openEditorBtn.setIcon("components/pickerfield/images/lookup-btn.png");
        openEditorBtn.setStyleName("c-listeditor-button");
        openEditorBtn.setCaption("");
        openEditorBtn.setAction(new AbstractAction("openEditor") {

            @Override
            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                Map<String, Object> params = new HashMap<>();
                params.put("itemType", itemType);
                params.put("entityName", entityName);
                params.put("useLookupField", useLookupField);
                params.put("optionsList", optionsList);
                params.put("optionsMap", optionsMap);
                params.put("enumClass", enumClass);
                params.put("lookupScreen", lookupScreen);
                params.put("entityJoinClause", entityJoinClause);
                params.put("entityWhereClause", entityWhereClause);
                params.put("values", getValue());
                params.put("editable", editable);
                ListEditorPopupWindow listEditorPopup = (ListEditorPopupWindow) windowManager
                        .openWindow(windowConfig.getWindowInfo("list-editor-popup"), WindowManager.OpenType.DIALOG, params);
                listEditorPopup.addCloseListener(actionId -> {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        actualField.setValue(listEditorPopup.getValue());
                    }
                });
            }
        });

        layout.add(displayValuesField);
        layout.add(openEditorBtn);
        layout.expand(displayValuesField);
    }

    @Override
    public void setActualField(Field actualField) {
        this.actualField = actualField;
    }

    @Override
    public HBoxLayout getLayout() {
        return layout;
    }

    @Override
    public List getValue() {
        return value;
    }

    @Override
    public void setValue(List newValue) {
        this.value = newValue;
        String strValue = null;
        if (newValue != null) {
            List<String> captions = ((List<Object>) newValue).stream()
                    .map(o -> ListEditorHelper.getValueCaption(o, itemType))
                    .collect(Collectors.toList());
            strValue = Joiner.on(", ").join(captions);
        }
        displayValuesField.setValue(strValue);
        if (displayDescription) {
            displayValuesField.setDescription(strValue);
        }
    }

    @Override
    public ListEditor.ItemType getItemType() {
        return itemType;
    }

    @Override
    public void setItemType(ListEditor.ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public String getLookupScreen() {
        return lookupScreen;
    }

    @Override
    public void setLookupScreen(String lookupScreen) {
        this.lookupScreen = lookupScreen;
    }

    @Override
    public boolean isUseLookupField() {
        return useLookupField;
    }

    @Override
    public void setUseLookupField(boolean useLookupField) {
        this.useLookupField = useLookupField;
    }

    @Override
    public List<?> getOptionsList() {
        return optionsList;
    }

    @Override
    public void setOptionsList(List<?> optionsList) {
        this.optionsList = optionsList;
    }

    @Override
    public Map<String, Object> getOptionsMap() {
        return optionsMap;
    }

    @Override
    public void setOptionsMap(Map<String, Object> optionsMap) {
        this.optionsMap = optionsMap;
    }

    @Override
    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    @Override
    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public boolean isDisplayDescription() {
        return displayDescription;
    }

    @Override
    public void setDisplayDescription(boolean displayDescription) {
        this.displayDescription = displayDescription;
    }

    @Override
    public String getEntityJoinClause() {
        return entityJoinClause;
    }

    @Override
    public void setEntityJoinClause(String entityJoinClause) {
        this.entityJoinClause = entityJoinClause;
    }

    @Override
    public String getEntityWhereClause() {
        return entityWhereClause;
    }

    @Override
    public void setEntityWhereClause(String entityWhereClause) {
        this.entityWhereClause = entityWhereClause;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        if (clearBtn != null) {
            clearBtn.setEnabled(editable);
        }
    }

    @Override
    public void setClearButtonVisible(boolean visible) {
        if (visible) {
            addClearBtn();
        } else {
            layout.remove(clearBtn);
        }
    }

    @Override
    public TextField getDisplayValuesField() {
        return displayValuesField;
    }

    protected void addClearBtn() {
        clearBtn = componentsFactory.createComponent(Button.class);
        clearBtn.setIcon("components/pickerfield/images/clear-btn.png");
        clearBtn.setStyleName("c-listeditor-button");
        clearBtn.setCaption("");
        clearBtn.setAction(new BaseAction("clear")
                        .withCaption("Clear")
                        .withHandler(event ->
                                actualField.setValue(null)
                        ));

        layout.add(clearBtn);
    }
}