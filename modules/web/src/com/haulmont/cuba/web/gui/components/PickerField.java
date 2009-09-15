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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.*;
import com.vaadin.ui.TextField;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;

public class PickerField
        extends
        com.haulmont.cuba.web.gui.components.AbstractField<PickerField.PickerFieldComponent>
        implements
        com.haulmont.cuba.gui.components.PickerField, Component.Wrapper {
    private CaptionMode captionMode = CaptionMode.ITEM;
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
            return metaProperty.getRange().asClass();
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
            return ((Instance) item).<T>getValue(metaProperty.getName());
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
            ((Instance) item).setValue(metaProperty.getName(), value);
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        if (this.datasource != null) this.datasource.removeListener(dsListener);

        this.datasource = datasource;

        MetaClass metaClass = datasource.getMetaClass();
        this.metaProperty = metaClass.getProperty(property);

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

    public void setPickerButtonCaption(String caption) {
       component.setPickerButtonCaption(caption);
    }

    public void setPickerButtonIcon(String iconName) {
        component.setPickerButtonIcon(new ThemeResource(iconName));
    }

    public void setClearButtonCaption(String caption) {
        component.setClearButtonCaption(caption);
    }

    public void setClearButtonIcon(String iconName) {
        component.setClearButtonIcon(new ThemeResource(iconName));
    }

    public static class PickerFieldComponent extends CustomComponent implements com.vaadin.ui.Field {

        public static final int DEFAULT_WIDTH = 250;

        protected com.vaadin.ui.TextField field;
        protected com.vaadin.ui.Button pickerButton;
        protected com.vaadin.ui.Button clearButton;

        protected String buttonIcon;
        protected String clearButtonIcon;

        public PickerFieldComponent(Button.ClickListener listener) {
            field = new TextField();
            field.setReadOnly(true);
            field.setWidth("100%");
            field.setNullRepresentation("");

            pickerButton = new Button("...", listener);
            clearButton = new Button("Clear", new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    setValue(null);
                }
            });

            final HorizontalLayout container = new HorizontalLayout();
            container.setWidth("100%");

            container.addComponent(field);
            container.addComponent(pickerButton);
            container.addComponent(clearButton);
            container.setExpandRatio(field, 1);

            setCompositionRoot(container);
            setStyleName("pickerfield");
            setWidth(DEFAULT_WIDTH + "px");
        }

        public boolean isInvalidCommitted() {
            return field.isInvalidCommitted();
        }

        public void setInvalidCommitted(boolean isCommitted) {
            field.setInvalidCommitted(isCommitted);
        }

        public void commit() throws SourceException, com.vaadin.data.Validator.InvalidValueException {
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

        public void setWriteThrough(boolean writeTrough) throws SourceException, com.vaadin.data.Validator.InvalidValueException {
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

        public void addValidator(com.vaadin.data.Validator validator) {
            field.addValidator(validator);
        }

        public Collection getValidators() {
            return field.getValidators();
        }

        public void removeValidator(com.vaadin.data.Validator validator) {
            field.removeValidator(validator);
        }

        public boolean isValid() {
            return field.isValid();
        }

        public void validate() throws com.vaadin.data.Validator.InvalidValueException {
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

        public void setPickerButtonCaption(String caption) {
            pickerButton.setCaption(caption);
        }

        public String getPickerButtonCaption() {
            return pickerButton.getCaption();
        }

        public void setClearButtonCaption(String caption) {
            clearButton.setCaption(caption);
        }

        public String getClearButtonCaption() {
            return clearButton.getCaption();
        }

        public void setPickerButtonIcon(Resource icon) {
            pickerButton.setIcon(icon);
            pickerButton.setStyleName("link");
        }

        public Resource getPickerButtonIcon() {
            return pickerButton.getIcon();
        }

        public void setClearButtonIcon(Resource icon) {
            clearButton.setIcon(icon);
            clearButton.setStyleName("link");
        }

        public Resource getClearButtonIcon() {
            return clearButton.getIcon();
        }
    }

    protected class DsListener implements DatasourceListener<Entity> {
        public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
            value = item == null ? null : ((Instance) item).getValue(metaProperty.getName());
            component.setValue(getItemCaption(value));
        }

        public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {}

        public void valueChanged(Entity source, String property, Object prevValue, Object value) {
            if (ObjectUtils.equals(property, PickerField.this.metaProperty.getName()) &&
                    ObjectUtils.equals(datasource.getItem(), source)) {
                value = source == null ? null : ((Instance) source).getValue(property);
                component.setValue(getItemCaption(value));
            }
        }
    }
}
