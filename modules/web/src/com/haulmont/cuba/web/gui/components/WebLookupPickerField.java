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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ActionsPermissions;
import com.haulmont.cuba.gui.components.Component.SecuredActionsHolder;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.ComboBox;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

public class WebLookupPickerField<V extends Entity> extends WebLookupField<V> implements LookupPickerField<V>, SecuredActionsHolder {

    protected WebPickerField pickerField;
    protected boolean updateComponentValue = false;
    protected boolean refreshOptionsOnLookupClose = false;

    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    public WebLookupPickerField() {
    }

    @Override
    protected void createComponent() {
        super.createComponent();

        // delegate error indication
        this.componentErrorHandler = message -> {
            if (message instanceof UserError) {
                return false;
            }

            pickerField.component.setComponentError(message);
            return true;
        };

        component.setCustomValueEquals(InstanceUtils::propertyValueEquals);

        ComboBox selectComponent = component;
        WebPickerField.Picker picker = new WebPickerField.Picker(this, component) {
            @Override
            public void setRequired(boolean required) {
                super.setRequired(required);
                selectComponent.setNullSelectionAllowed(!required);
            }
        };
        pickerField = new WebPickerField(picker);

        // Required for custom components in fieldgroup
        initValueSync(selectComponent, picker);
    }

    protected void initValueSync(ComboBox selectComponent, WebPickerField.Picker picker) {
        selectComponent.addValueChangeListener(event -> {
            if (updateComponentValue)
                return;

            updateComponentValue = true;
            if (!Objects.equals(selectComponent.getValue(), picker.getValue())) {
                picker.setValueIgnoreReadOnly(selectComponent.getValue());
            }
            updateComponentValue = false;
        });

        picker.addValueChangeListener(event -> {
            if (updateComponentValue)
                return;

            updateComponentValue = true;
            if (!Objects.equals(selectComponent.getValue(), picker.getValue())) {
                selectComponent.setValueIgnoreReadOnly(picker.getValue());
            }
            updateComponentValue = false;
        });
    }

    @Override
    public Component getComposition() {
        return pickerField.getComposition();
    }

    @Override
    public Component getComponent() {
        return pickerField.getComponent();
    }

    @Override
    public MetaClass getMetaClass() {
        return pickerField.getMetaClass();
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        pickerField.setMetaClass(metaClass);
    }

    @Override
    public LookupAction addLookupAction() {
        LookupAction action = LookupAction.create(this);
        addAction(action);
        return action;
    }

    @Override
    public ClearAction addClearAction() {
        ClearAction action = ClearAction.create(this);
        addAction(action);
        return action;
    }

    @Override
    public OpenAction addOpenAction() {
        OpenAction action = OpenAction.create(this);
        addAction(action);
        return action;
    }

    @Override
    public void addFieldListener(FieldListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFieldEditable(boolean editable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAction(Action action) {
        pickerField.addAction(action);
    }

    @Override
    public void addAction(Action action, int index) {
        pickerField.addAction(action, index);
    }

    @Override
    public void removeAction(@Nullable Action action) {
        pickerField.removeAction(action);
    }

    @Override
    public void removeAction(@Nullable String id) {
        pickerField.removeAction(id);
    }

    @Override
    public void removeAllActions() {
        pickerField.removeAllActions();
    }

    @Override
    public Collection<Action> getActions() {
        return pickerField.getActions();
    }

    @Override
    public void setCaption(String caption) {
        pickerField.setCaption(caption);
    }

    @Override
    public String getCaption() {
        return pickerField.getCaption();
    }

    @Override
    public void setDescription(String description) {
        pickerField.setDescription(description);
    }

    @Override
    public String getDescription() {
        return pickerField.getDescription();
    }

    @Override
    public String getContextHelpText() {
        return pickerField.getContextHelpText();
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
        pickerField.setContextHelpText(contextHelpText);
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return pickerField.isContextHelpTextHtmlEnabled();
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
        pickerField.setContextHelpTextHtmlEnabled(enabled);
    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return pickerField.getContextHelpIconClickHandler();
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        pickerField.setContextHelpIconClickHandler(handler);
    }

    @Override
    @Nullable
    public Action getAction(String id) {
        return pickerField.getAction(id);
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);
        pickerField.setFrame(frame);
        pickerField.getComposition().setCubaId(component.getCubaId());
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        if (datasource != null) {
            pickerField.checkDatasourceProperty(datasource, property);
        }
        super.setDatasource(datasource, property);
        pickerField.setDatasource(datasource, property);
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        super.setOptionsDatasource(datasource);
        if (pickerField.getMetaClass() == null && datasource != null) {
            pickerField.setMetaClass(datasource.getMetaClass());
        }
    }

    @Override
    public void commit() {
        super.commit();
        pickerField.commit();
    }

    @Override
    public void discard() {
        super.discard();
        pickerField.discard();
    }

    @Override
    public boolean isBuffered() {
        return pickerField.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        super.setBuffered(buffered);
        pickerField.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return pickerField.isModified();
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        super.setEditableToComponent(editable);

        pickerField.setEditable(editable);
    }

    @Override
    public void setRequired(boolean required) {
        component.setNullSelectionAllowed(!required);
        pickerField.setRequired(required);
    }

    @Override
    public void setRequiredMessage(String msg) {
        pickerField.setRequiredMessage(msg);
    }

    @Override
    public String getRequiredMessage() {
        return pickerField.getRequiredMessage();
    }

    @Override
    public boolean isRequired() {
        return pickerField.isRequired();
    }

    @Override
    public void setRefreshOptionsOnLookupClose(boolean refresh) {
        refreshOptionsOnLookupClose = refresh;
    }

    @Override
    public boolean isRefreshOptionsOnLookupClose() {
        return refreshOptionsOnLookupClose;
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }
}