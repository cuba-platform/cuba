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

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.components.CapsLockIndicator;
import com.haulmont.cuba.gui.components.PasswordField;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.DataAwareComponentsTools;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.web.widgets.CubaPasswordField;
import com.vaadin.shared.ui.ValueChangeMode;
import org.springframework.beans.factory.InitializingBean;

import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.nullToEmpty;

public class WebPasswordField extends WebV8AbstractField<CubaPasswordField, String, String>
        implements PasswordField, InitializingBean {

    protected CapsLockIndicator capsLockIndicator;

    public WebPasswordField() {
        this.component = createComponent();

        attachValueChangeListener(component);
    }

    @Override
    protected void valueBindingConnected(ValueSource<String> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            DataAwareComponentsTools dataAwareComponentsTools = beanLocator.get(DataAwareComponentsTools.class);
            EntityValueSource entityValueSource = (EntityValueSource) valueSource;

            dataAwareComponentsTools.setupMaxLength(this, entityValueSource);
        }
    }

    protected CubaPasswordField createComponent() {
        return new CubaPasswordField();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initComponent(component);
    }

    protected void initComponent(CubaPasswordField component) {
        component.setValueChangeMode(ValueChangeMode.BLUR);
    }

    @Override
    protected String convertToPresentation(String modelValue) throws ConversionException {
        return nullToEmpty(super.convertToPresentation(modelValue));
    }

    @Override
    protected String convertToModel(String componentRawValue) throws ConversionException {
        String value = emptyToNull(componentRawValue);
        return super.convertToModel(value);
    }

    @Override
    public boolean isEmpty() {
        return Strings.isNullOrEmpty(getValue());
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }

    @Override
    public boolean isAutocomplete() {
        return component.isAutocomplete();
    }

    @Override
    public void setAutocomplete(Boolean value) {
        component.setAutocomplete(value);
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public boolean isBuffered() {
        return super.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        super.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }

    @Override
    public void setCapsLockIndicator(CapsLockIndicator capsLockIndicator) {
        this.capsLockIndicator = capsLockIndicator;

        if (capsLockIndicator != null) {
            component.setCapsLockIndicator(capsLockIndicator.unwrap(com.vaadin.ui.Component.class));
        } else {
            component.setCapsLockIndicator(null);
        }
    }

    @Override
    public CapsLockIndicator getCapsLockIndicator() {
        return capsLockIndicator;
    }

    @Override
    public String getInputPrompt() {
        return component.getPlaceholder();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        component.setPlaceholder(inputPrompt);
    }

    @Override
    public void setHtmlName(String htmlName) {
        component.setHtmlName(htmlName);
    }

    @Override
    public String getHtmlName() {
        return component.getHtmlName();
    }
}