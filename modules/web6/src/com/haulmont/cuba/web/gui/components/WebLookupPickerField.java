/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;

import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebLookupPickerField
        extends WebLookupField
        implements LookupPickerField {

    protected WebPickerField pickerField;

    public WebLookupPickerField() {
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

        final Component selectComponent = component;
        Picker picker = new Picker(this, component) {
            @Override
            public void setRequired(boolean required) {
                super.setRequired(required);
                ((FilterSelect) selectComponent).setNullSelectionAllowed(!required);
            }
        };
        pickerField = new WebPickerField(picker);
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
    public void removeAction(Action action) {
        pickerField.removeAction(action);
    }

    @Override
    public Collection<Action> getActions() {
        return pickerField.getActions();
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
    public Action getAction(String id) {
        return pickerField.getAction(id);
    }

    @Override
    public void setFrame(IFrame frame) {
        super.setFrame(frame);
        pickerField.setFrame(frame);
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
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

    @Override
    public void setWidth(String width) {
        pickerField.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        pickerField.setHeight(height);
    }

    @Override
    public float getHeight() {
        return pickerField.getHeight();
    }

    @Override
    public float getWidth() {
        return pickerField.getWidth();
    }

    @Override
    public void setCaption(String caption) {
        pickerField.setCaption(caption);
    }

    @Override
    public String getCaption() {
        return pickerField.getCaption();
    }

    public static class Picker extends WebPickerField.Picker {

        public Picker(PickerField owner, AbstractField field) {
            super(owner, field);
        }

        @Override
        public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
            if (newValue instanceof Entity)
                newValue = ((Entity) newValue).getId();
            super.setValue(newValue);
        }
    }
}