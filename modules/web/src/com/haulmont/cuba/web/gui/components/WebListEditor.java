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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.ListEditor;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.listeditor.ListEditorDelegate;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class WebListEditor<V> extends WebV8AbstractField<WebListEditor.CubaListEditor<V>, List<V>, List<V>>
        implements ListEditor<V>, InitializingBean {

    protected static final String LISTEDITOR_STYLENAME = "c-listeditor";

    protected ListEditorDelegate<V> delegate;

    public WebListEditor() {
        initDelegate();
        component = createComponent();
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);

        delegate.getLayout().setParent(this);
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);

        delegate.getLayout().setFrame(frame);
    }

    protected CubaListEditor<V> createComponent() {
        return new CubaListEditor<>(delegate.getLayout());
    }

    protected void initComponent(Component component) {
        component.setStyleName(LISTEDITOR_STYLENAME);
    }

    protected void initDelegate() {
        delegate = createDelegate();
        delegate.setActualField(this);
    }

    protected ListEditorDelegate<V> createDelegate() {
        return AppBeans.get(ListEditorDelegate.NAME);
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
        setValueToPresentation(convertToPresentation(newValue));

        delegate.setValue(newValue);

        List<V> oldValue = internalValue;
        this.internalValue = newValue;

        if (!Objects.equals(oldValue, newValue)) {
            ValueChangeEvent event = new ValueChangeEvent<>(this, oldValue, newValue);
            publish(ValueChangeEvent.class, event);
        }
    }

    @Override
    public List<V> getValue() {
        return delegate.getValue();
    }

    @Override
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(delegate.getValue());
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return delegate.getDisplayValuesField().getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        delegate.getDisplayValuesField().setTabIndex(tabIndex);
    }

    public static class CubaListEditor<V> extends CustomField<List<V>> {
        private final Component content;

        public CubaListEditor(HBoxLayout mainLayout) {
            content = WebComponentsHelper.unwrap(mainLayout);
        }

        @Override
        protected Component initContent() {
            return content;
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty() || CollectionUtils.isEmpty(getValue());
        }

        @Override
        protected void doSetValue(List<V> value) {
            // delegated to ListEditorDelegate
        }

        @Override
        public List<V> getValue() {
            // delegated to ListEditorDelegate
            return null;
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
    public Subscription addEditorCloseListener(Consumer<EditorCloseEvent> listener) {
        delegate.addEditorCloseListener(listener);

        return () -> removeEditorCloseListener(listener);
    }

    @Override
    public void removeEditorCloseListener(Consumer<EditorCloseEvent> listener) {
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

    @Override
    public void addListItemValidator(Consumer<? super V> validator) {
        delegate.addListItemValidator(validator);
    }

    @Override
    public Collection<Consumer<? super V>> getListItemValidators() {
        return delegate.getListItemValidators();
    }

    @Override
    public void setOptions(Options<V> options) {
        delegate.setOptions(options);
    }

    @Override
    public Options<V> getOptions() {
        return delegate.getOptions();
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> optionCaptionProvider) {
        delegate.setOptionCaptionProvider(optionCaptionProvider);
    }

    @Override
    public Function<? super V, String> getOptionCaptionProvider() {
        return delegate.getOptionCaptionProvider();
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