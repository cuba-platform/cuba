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

import com.haulmont.bali.util.StringHelper;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.ListEditor;
import com.haulmont.cuba.gui.components.listeditor.ListEditorDelegate;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WebListEditor extends WebAbstractField<WebListEditor.CubaListEditor> implements ListEditor {

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
    public void setValue(Object newValue) {
        if (newValue != null && !(newValue instanceof List)) {
            throw new IllegalArgumentException("Value type must be List");
        }
        super.setValue(newValue);
        delegate.setValue((List) newValue);

        Object oldValue = prevValue;
        if (!Objects.equals(oldValue, newValue)) {
            prevValue = newValue;

            ValueChangeEvent event = new ValueChangeEvent(this, oldValue, newValue);
            getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
        }
    }

    @Override
    public List getValue() {
        return delegate.getValue();
    }

    public class CubaListEditor extends CustomField<List> {

        private final Component content;

        public CubaListEditor(HBoxLayout mainLayout) {
            content = WebComponentsHelper.unwrap(mainLayout);
        }

        @Override
        protected Component initContent() {
            return content;
        }

        @Override
        public Class getType() {
            return List.class;
        }
    }

    @Override
    public void setEditable(boolean editable) {
        delegate.setEditable(editable);
    }

    @Override
    public boolean isEditable() {
        return delegate.isEditable();
    }
}
