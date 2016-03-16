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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.components.compatibility.ComponentValueListenerWrapper;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 */
public abstract class DesktopAbstractField<C extends JComponent> extends DesktopAbstractComponent<C> implements Field {

    protected List<ValueChangeListener> listeners = new ArrayList<>();

    protected boolean required;
    protected String requiredMessage;

    protected Set<Validator> validators = new HashSet<>();

    // todo move nimbus constants to theme
    protected Color requiredBgColor = (Color) UIManager.get("cubaRequiredBackground");
    protected Color defaultBgColor = (Color) UIManager.get("nimbusLightBackground");

    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    @Override
    public void addListener(ValueListener listener) {
        addValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    @Override
    public void removeListener(ValueListener listener) {
        removeValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueChangeListener listener : new ArrayList<>(listeners)) {
            listener.valueChanged(new ValueChangeEvent(this, prevValue, value));
        }
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void addValidator(Validator validator) {
        if (!validators.contains(validator))
            validators.add(validator);
    }

    @Override
    public void removeValidator(Validator validator) {
        validators.remove(validator);
    }

    @Override
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    protected boolean isEmpty(Object value) {
        return value == null;
    }

    @Override
    public void validate() throws ValidationException {
        if (!isVisible() || !isEditable() || !isEnabled())
            return;

        Object value = getValue();
        if (isEmpty(value)) {
            if (isRequired())
                throw new RequiredValueMissingException(requiredMessage, this);
            else
                return;
        }

        for (Validator validator : validators) {
            validator.validate(value);
        }
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
        updateMissingValueState();
    }

    public void updateMissingValueState() {
        if (impl != null) {
            decorateMissingValue(impl, required);
        }
    }

    protected void decorateMissingValue(JComponent jComponent, boolean missingValueState) {
        jComponent.setBackground(missingValueState ? requiredBgColor : defaultBgColor);
    }

    @Override
    public void setRequiredMessage(String msg) {
        requiredMessage = msg;
    }

    @Override
    public String getRequiredMessage() {
        return requiredMessage;
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        Datasource datasource = getDatasource();
        MetaPropertyPath metaPropertyPath = getMetaPropertyPath();

        if (datasource != null && StringUtils.isNotEmpty(datasource.getId()) && metaPropertyPath != null) {
            return getClass().getSimpleName() + "_" + datasource.getId() + "_" + metaPropertyPath.toString();
        }

        return getClass().getSimpleName();
    }

    protected void resolveMetaPropertyPath(MetaClass metaClass, String property) {
        metaPropertyPath = getResolvedMetaPropertyPath(metaClass, property);
        this.metaProperty = metaPropertyPath.getMetaProperty();
    }

    protected MetaPropertyPath getResolvedMetaPropertyPath(MetaClass metaClass, String property) {
        MetaPropertyPath metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                .resolveMetaPropertyPath(metaClass, property);
        Preconditions.checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        return metaPropertyPath;
    }
}