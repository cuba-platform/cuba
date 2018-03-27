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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.ListEditor;
import com.haulmont.cuba.gui.components.listeditor.ListEditorDelegate;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.Supplier;

public class WebListEditor<V> extends WebAbstractField<WebListEditor.CubaListEditor, List<V>> implements ListEditor<V> {

    protected static final String LISTEDITOR_STYLENAME = "c-listeditor";

    protected ListEditorDelegate delegate;

    public WebListEditor() {
        delegate = AppBeans.get(ListEditorDelegate.class);
        delegate.setActualField(this);
        component = new CubaListEditor(delegate.getLayout());
        setStyleName(LISTEDITOR_STYLENAME);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(LISTEDITOR_STYLENAME, ""));
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
    public Map<String, Object> getOptionsMap() {
        return delegate.getOptionsMap();
    }

    @Override
    public void setOptionsMap(Map<String, Object> optionsMap) {
        delegate.setOptionsMap(optionsMap);
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
    public Class<? extends Enum> getEnumClass() {
        return delegate.getEnumClass();
    }

    @Override
    public void setEnumClass(Class<? extends Enum> enumClass) {
        delegate.setEnumClass(enumClass);
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
    public void setValue(List<V> newValue) {
        super.setValue(newValue);
        delegate.setValue(newValue);

        Object oldValue = internalValue;
        if (!Objects.equals(oldValue, newValue)) {
            internalValue = newValue;

            ValueChangeEvent event = new ValueChangeEvent(this, oldValue, newValue);
            getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<V> getValue() {
        return delegate.getValue();
    }

    @Override
    public int getTabIndex() {
        return delegate.getDisplayValuesField().getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        delegate.getDisplayValuesField().setTabIndex(tabIndex);
    }

    public static class CubaListEditor extends CustomField<List> {
        private final Component content;

        public CubaListEditor(HBoxLayout mainLayout) {
            content = WebComponentsHelper.unwrap(mainLayout);
        }

        @Override
        protected Component initContent() {
            return content;
        }

        @Override
        public Class<List> getType() {
            return List.class;
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty() || CollectionUtils.isEmpty(getValue());
        }
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        delegate.setEditable(editable);
    }

    @Override
    public boolean isEditable() {
        return delegate.isEditable();
    }

    @Override
    public void setEditorWindowId(String windowId) {
        delegate.setEditorWindowId(windowId);
    }

    @Override
    public String getEditorWindowId() {
        return delegate.getEditorWindowId();
    }

    @Override
    public void addEditorCloseListener(EditorCloseListener listener) {
        delegate.addEditorCloseListener(listener);
    }

    @Override
    public void removeEditorCloseListener(EditorCloseListener listener) {
        delegate.removeEditorCloseListener(listener);
    }

    @Override
    public void setEditorParamsSupplier(Supplier<Map<String, Object>> paramsSupplier) {
        delegate.setEditorParamsSupplier(paramsSupplier);
    }

    @Override
    public Supplier<Map<String, Object>> getEditorParamsSupplier() {
        return delegate.getEditorParamsSupplier();
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        delegate.setTimeZone(timeZone);
    }

    @Override
    public TimeZone getTimeZone() {
        return delegate.getTimeZone();
    }
}