/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
public abstract class DesktopAbstractField<C extends JComponent>
        extends
            DesktopAbstractComponent<C>
        implements
            Field {

    protected List<ValueListener> listeners = new ArrayList<>();
    protected ValueChangingListener valueChangingListener;

    protected boolean required;
    protected String requiredMessage;

    protected Set<Validator> validators = new HashSet<>();

    // todo move nimbus constants to theme
    protected Color requiredBgColor = (Color) UIManager.get("cubaRequiredBackground");
    protected Color defaultBgColor = (Color) UIManager.get("nimbusLightBackground");

    @Override
    public void addListener(ValueListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setValueChangingListener(ValueChangingListener listener) {
        valueChangingListener = listener;
    }

    @Override
    public void removeValueChangingListener() {
        valueChangingListener = null;
    }

    protected Object fireValueChanging(Object prevValue, Object value) {
        if (valueChangingListener != null)
            return valueChangingListener.valueChanging(this, "value", prevValue, value);
        else
            return value;
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
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
}