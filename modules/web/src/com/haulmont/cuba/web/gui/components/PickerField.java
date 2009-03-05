/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 12:33:12
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.Validator;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.ui.TextField;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;

public class PickerField
    extends
        com.haulmont.cuba.web.gui.components.AbstractField<PickerField.PickerFieldComponent>
    implements
        com.haulmont.cuba.gui.components.PickerField, Component.Wrapper
{
    private PickerField.CaptionMode captionMode = PickerField.CaptionMode.ITEM;
    private String captionProperty;

    protected Object value;
    protected DsListener dsListener;
    private MetaClass metaClass;

    public PickerField() {
        dsListener = new DsListener();
        component = new PickerFieldComponent(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                final MetaClass metaClass = getMetaClass();

                final com.haulmont.cuba.gui.components.IFrame frame = getFrame();
                frame.openLookup(metaClass.getName() + ".lookup", new Window.Lookup.Handler() {
                    public void handleLookup(Collection items) {
                        if (!items.isEmpty()) {
                            final Object item = items.iterator().next();
                            // TODO (abramov) reload id needed
                            setValue(item);
                        }
                    }
                }, WindowManager.OpenType.THIS_TAB);
            }
        });
        component.setImmediate(true);
    }

    public MetaClass getMetaClass() {
        final Datasource ds = getDatasource();
        if (ds != null) {
            final MetaClass metaClass = ds.getMetaClass();
            return metaClass.getProperty(property).getRange().asClass();
        } else {
            return metaClass;
        }
    }

    public void setMetaClass(MetaClass metaClass) {
        final Datasource ds = getDatasource();
        if (ds != null) throw new IllegalStateException("Datasource is not null");
        this.metaClass = metaClass;
    }

    @Override
    public <T> T getValue() {
        if (datasource != null) {
            final Entity item = datasource.getItem();
            return ((Instance) item).<T>getValue(property);
        } else {
            return (T) value;
        }
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        component.setValue(getItemCaption(value));
        if (datasource != null) {
            final Entity item = datasource.getItem();
            ((Instance) item).setValue(property, value);
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        if (this.datasource != null) this.datasource.removeListener(dsListener);

        this.datasource = datasource;
        this.property = property;

        MetaClass metaClass = datasource.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);

        setRequired(metaProperty.isMandatory());
        this.metaClass = metaProperty.getRange().asClass();

        this.datasource.addListener(dsListener);
    }

    protected String getItemCaption(Object value) {
        if (CaptionMode.PROPERTY.equals(captionMode)) {
            return String.valueOf(value == null ? "" : ((Instance) value).getValue(captionProperty));
        } else {
            return String.valueOf(value);
        }
    }

    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
    }

    public String getCaptionProperty() {
        return captionProperty;
    }

    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    public static class PickerFieldComponent extends CustomComponent implements com.itmill.toolkit.ui.Field {
        protected com.itmill.toolkit.ui.TextField field;
        protected com.itmill.toolkit.ui.Button button;

        public PickerFieldComponent(Button.ClickListener listener) {
            field = new TextField();
            field.setReadOnly(true);

            button = new Button("...", listener);

            VerticalLayout layout = new VerticalLayout();
            final HorizontalLayout container = new HorizontalLayout();

            container.addComponent(field);
            container.addComponent(button);
            container.setExpandRatio(field, 1);

            layout.addComponent(container);
            layout.setComponentAlignment(container, com.itmill.toolkit.ui.Alignment.BOTTOM_LEFT);

            setCompositionRoot(layout);
        }

        public boolean isInvalidCommitted() {
            return field.isInvalidCommitted();
        }

        public void setInvalidCommitted(boolean isCommitted) {
            field.setInvalidCommitted(isCommitted);
        }

        public void commit() throws SourceException, Validator.InvalidValueException {
            field.commit();
        }

        public void discard() throws SourceException {
            field.discard();
        }

        public boolean isModified() {
            return field.isModified();
        }

        public boolean isWriteThrough() {
            return field.isWriteThrough();
        }

        public void setWriteThrough(boolean writeTrough) throws SourceException, Validator.InvalidValueException {
            field.setWriteThrough(writeTrough);
        }

        public boolean isReadThrough() {
            return field.isReadThrough();
        }

        public void setReadThrough(boolean readTrough) throws SourceException {
            field.setReadThrough(readTrough);
        }

        public Object getValue() {
            return field.getValue();
        }

        public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
            field.setReadOnly(false);
            field.setValue(newValue);
            field.setReadOnly(true);
        }

        public Class getType() {
            return field.getType();
        }

        public Property getPropertyDataSource() {
            return field.getPropertyDataSource();
        }

        public void setPropertyDataSource(Property newDataSource) {
            field.setPropertyDataSource(newDataSource);
        }

        public void addValidator(Validator validator) {
            field.addValidator(validator);
        }

        public Collection getValidators() {
            return field.getValidators();
        }

        public void removeValidator(Validator validator) {
            field.removeValidator(validator);
        }

        public boolean isValid() {
            return field.isValid();
        }

        public void validate() throws Validator.InvalidValueException {
            field.validate();
        }

        public boolean isInvalidAllowed() {
            return field.isInvalidAllowed();
        }

        public void setInvalidAllowed(boolean invalidAllowed) throws UnsupportedOperationException {
            field.setInvalidAllowed(invalidAllowed);
        }

        public void addListener(ValueChangeListener listener) {
            field.addListener(listener);
        }

        public void removeListener(ValueChangeListener listener) {
            field.removeListener(listener);
        }

        public void valueChange(Property.ValueChangeEvent event) {
            field.valueChange(event);
        }

        public void focus() {
            field.focus();
        }

        public int getTabIndex() {
            return field.getTabIndex();
        }

        public void setTabIndex(int tabIndex) {
            field.setTabIndex(tabIndex);
        }

        public boolean isRequired() {
            return field.isRequired();
        }

        public void setRequired(boolean required) {
            field.setRequired(required);
        }

        public void setRequiredError(String requiredMessage) {
            field.setRequiredError(requiredMessage);
        }

        public String getRequiredError() {
            return field.getRequiredError();
        }
    }

    protected class DsListener implements DatasourceListener<Entity> {
        public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
            value = item == null ? null : ((Instance) item).getValue(property);
            component.setValue(getItemCaption(value));
        }

        public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {}

        public void valueChanged(Entity source, String property, Object prevValue, Object value) {
            if (ObjectUtils.equals(property, PickerField.this.property) &&
                    ObjectUtils.equals(datasource.getItem(), source))
            {
                value = source == null ? null : ((Instance) source).getValue(property);
                component.setValue(getItemCaption(value));
            }
        }
    }
}
