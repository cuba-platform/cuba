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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.ListEditor;
import com.haulmont.cuba.gui.components.listeditor.ListEditorDelegate;
import com.haulmont.cuba.gui.data.Datasource;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.Supplier;

public class DesktopListEditor extends DesktopAbstractField<JPanel> implements ListEditor {

    private final ListEditorDelegate delegate;
    private List prevValue;

    public DesktopListEditor() {
        delegate = AppBeans.get(ListEditorDelegate.class);
        delegate.setActualField(this);
        //do not display the description because it causes an exception on desktop client
        delegate.setDisplayDescription(false);
        impl = (JPanel) DesktopComponentsHelper.getComposition(delegate.getLayout());
    }

    @Override
    public void setValue(Object newValue) {
        if (newValue != null && !(newValue instanceof List)) {
            throw new IllegalArgumentException("Value type must be List");
        }
        delegate.setValue((List) newValue);
        fireValueChanged(newValue);
    }

    protected void fireValueChanged(Object value) {
        if (!Objects.equals(prevValue, value)) {
            Object oldValue = prevValue;
            prevValue = (List) value;

            if (listeners != null && !listeners.isEmpty()) {
                ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value);
                for (ValueChangeListener listener : listeners) {
                    listener.valueChanged(event);
                }
            }
        }
    }

    @Override
    public List getValue() {
        return delegate.getValue();
    }

    @Override
    public ItemType getItemType() {
        return delegate.getItemType();
    }

    @Override
    public void setItemType(ItemType itemType) {
        delegate.setItemType(itemType);
    }

    @Override
    public boolean isUseLookupField() {
        return delegate.isUseLookupField();
    }

    @Override
    public void setUseLookupField(boolean useLookupField) {
        delegate.setUseLookupField(useLookupField);
    }

    @Override
    public String getLookupScreen() {
        return delegate.getLookupScreen();
    }

    @Override
    public void setLookupScreen(String lookupScreen) {
        delegate.setLookupScreen(lookupScreen);
    }

    @Override
    public String getEntityName() {
        return delegate.getEntityName();
    }

    @Override
    public void setEntityName(String entityName) {
        delegate.setEntityName(entityName);
    }

    @Override
    public List<?> getOptionsList() {
        return delegate.getOptionsList();
    }

    @Override
    public void setOptionsList(List<?> optionsList) {
        delegate.setOptionsList(optionsList);
    }

    @Override
    public void setClearButtonVisible(boolean visible) {
        delegate.setClearButtonVisible(visible);
    }

    @Override
    public boolean isClearButtonVisible() {
        return delegate.isClearButtonVisible();
    }

    @Override
    public String getEntityJoinClause() {
        return delegate.getEntityJoinClause();
    }

    @Override
    public void setEntityJoinClause(String entityJoinClause) {
        delegate.setEntityJoinClause(entityJoinClause);
    }

    @Override
    public String getEntityWhereClause() {
        return delegate.getEntityWhereClause();
    }

    @Override
    public void setEntityWhereClause(String entityWhereClause) {
        delegate.setEntityWhereClause(entityWhereClause);
    }

    @Override
    public Map<String, Object> getOptionsMap() {
        return delegate.getOptionsMap();
    }

    @Override
    public void setOptionsMap(Map<String, Object> optionsMap) {
        delegate.setOptionsMap(optionsMap);
    }

    @Override
    public Class<? extends Enum> getEnumClass() {
        return delegate.getEnumClass();
    }

    @Override
    public void setEnumClass(Class<? extends Enum> enumClass) {
        delegate.setEnumClass(enumClass);
    }

    @Override
    public Datasource getDatasource() {
        return null;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return null;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return null;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public String getContextHelpText() {
        return null;
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return false;
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        delegate.setEditable(editable);
    }

    @Override
    public void setEditorWindowId(String windowId) {
    }

    @Override
    public String getEditorWindowId() {
        return null;
    }

    @Override
    public void addEditorCloseListener(EditorCloseListener listener) {
    }

    @Override
    public void removeEditorCloseListener(EditorCloseListener listener) {
    }

    @Override
    public void setEditorParamsSupplier(Supplier<Map<String, Object>> paramsSupplier) {
    }

    @Override
    public Supplier<Map<String, Object>> getEditorParamsSupplier() {
        return null;
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        delegate.setTimeZone(timeZone);
    }

    @Override
    public TimeZone getTimeZone() {
        return delegate.getTimeZone();
    }

    @Override
    public boolean isDisplayValuesFieldEditable() {
        return delegate.isDisplayValuesFieldEditable();
    }

    @Override
    public void setDisplayValuesFieldEditable(boolean displayValuesFieldEditable) {
        delegate.setDisplayValuesFieldEditable(displayValuesFieldEditable);
    }
}
