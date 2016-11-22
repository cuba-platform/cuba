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
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
@Component(ListEditorDelegate.NAME)
@Scope("prototype")
public class ListEditorDelegateImpl implements ListEditorDelegate{

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
    protected List<Object> optionsList;

    protected TextField displayValuesField;
    protected HBoxLayout layout;

    protected boolean displayDescription = true;

    @PostConstruct
    public void init() {
        WindowManager windowManager = windowManagerProvider.get();

        layout = componentsFactory.createComponent(HBoxLayout.class);
        layout.setSpacing(true);
        layout.setWidth("100%");

        displayValuesField = componentsFactory.createComponent(TextField.class);
        displayValuesField.setEditable(false);
        Button openEditorBtn = componentsFactory.createComponent(Button.class);
        openEditorBtn.setCaption("...");
        openEditorBtn.setAction(new AbstractAction("openEditor") {

            @Override
            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                Map<String, Object> params = new HashMap<>();
                params.put("itemType", itemType);
                params.put("entityName", entityName);
                params.put("useLookupField", useLookupField);
                params.put("optionsList", optionsList);
                params.put("lookupScreen", lookupScreen);
                params.put("values", getValue());
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
        List<String> captions = ((List<Object>)newValue).stream()
                .map(o -> ListEditorHelper.getValueCaption(o, itemType))
                .collect(Collectors.toList());
        String strValue = Joiner.on(", ").join(captions);
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
    public List<Object> getOptionsList() {
        return optionsList;
    }

    @Override
    public void setOptionsList(List<Object> optionsList) {
        this.optionsList = optionsList;
    }

    public boolean isDisplayDescription() {
        return displayDescription;
    }

    @Override
    public void setDisplayDescription(boolean displayDescription) {
        this.displayDescription = displayDescription;
    }
}
