/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.vaadin.data.Property;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author krivopustov
 */
public class WebLookupPickerField extends WebLookupField implements LookupPickerField {

    protected WebPickerField pickerField;
    protected boolean updateComponentValue = false;

    public WebLookupPickerField() {
    }

    @Override
    protected void createComponent() {
        super.createComponent();

        // delegate error indication
        this.componentErrorHandler = new ComponentErrorHandler() {
            @Override
            public boolean handleError(ErrorMessage message) {
                if (message instanceof UserError)
                    return false;

                pickerField.component.setComponentError(message);
                return true;
            }
        };

        final ComboBox selectComponent = component;
        final WebPickerField.Picker picker = new WebPickerField.Picker(this, component) {
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

    protected void initValueSync(final ComboBox selectComponent, final WebPickerField.Picker picker) {
        selectComponent.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (updateComponentValue)
                    return;

                updateComponentValue = true;
                if (!ObjectUtils.equals(selectComponent.getValue(), picker.getValue())) {
                    picker.setValueIgnoreReadOnly(selectComponent.getValue());
                }
                updateComponentValue = false;
            }
        });

        picker.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (updateComponentValue)
                    return;

                updateComponentValue = true;
                if (!ObjectUtils.equals(selectComponent.getValue(), picker.getValue())) {
                    selectComponent.setValueIgnoreReadOnly(picker.getValue());
                }
                updateComponentValue = false;
            }
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
        LookupAction action = new LookupAction(this);
        addAction(action);
        return action;
    }

    @Override
    public ClearAction addClearAction() {
        ClearAction action = new ClearAction(this);
        addAction(action);
        return action;
    }

    @Override
    public OpenAction addOpenAction() {
        OpenAction action = new OpenAction(this);
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
    @Nullable
    public Action getAction(String id) {
        return pickerField.getAction(id);
    }

    @Nonnull
    @Override
    public Action getActionNN(String id) {
        Action action = getAction(id);
        if (action == null) {
            throw new IllegalStateException("Unable to find action with id " + id);
        }
        return action;
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);
        pickerField.setFrame(frame);
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        pickerField.checkDatasourceProperty(datasource, property);
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
    public void setEditable(boolean editable) {
        super.setEditable(editable);
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
}