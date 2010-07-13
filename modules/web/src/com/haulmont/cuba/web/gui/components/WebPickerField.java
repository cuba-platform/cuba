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
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.BaseTheme;

import java.util.Collection;
import java.util.Collections;

public class WebPickerField
        extends
            WebAbstractField<WebPickerField.PickerFieldComponent>
        implements
            PickerField, Component.Wrapper
{
    private static final long serialVersionUID = -938974178359790495L;
    
    private CaptionMode captionMode = CaptionMode.ITEM;
    private String captionProperty;

    protected Object value;

    private MetaClass metaClass;

    public WebPickerField() {
        component = new PickerFieldComponent();
        component.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                if (isEditable()) {
                    final MetaClass metaClass = getMetaClass();

                    WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
                    String windowAlias = metaClass.getName() + ".lookup";
                    WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);

                    WindowManager wm = App.getInstance().getWindowManager();
                    wm.openLookup(windowInfo, new Window.Lookup.Handler() {
                        public void handleLookup(Collection items) {
                            if (!items.isEmpty()) {
                                final Object item = items.iterator().next();
                                __setValue(item);
                            }
                        }
                    }, WindowManager.OpenType.THIS_TAB);
                }
            }
        });
        component.setImmediate(true);
    }

    private void __setValue(Object newValue) {
        if (component.getPropertyDataSource() != null) {
            setValue(newValue);
        } else {
            value = newValue;
            ItemWrapper wrapper = new ItemWrapper(newValue, metaClass) {
                @Override
                public String toString() {
                    if (CaptionMode.PROPERTY.equals(captionMode)) {
                        return String.valueOf(getValue() == null ? ""
                                : ((Instance) getValue()).getValue(captionProperty));
                    } else {
                        return super.toString();
                    }
                }
            };
            setValue(wrapper);
        }
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
        if (component.getPropertyDataSource() != null) {
            return (T) super.getValue();
        } else {
            return (T) value;
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        MetaClass metaClass = datasource.getMetaClass();
        this.metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new RuntimeException(String.format("Property '%s' not found in class %s", property, metaClass));
        }

        final MetaPropertyPath propertyPath = new MetaPropertyPath(metaProperty.getDomain(), metaProperty);
        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(propertyPath));
        final Property itemProperty = wrapper.getItemProperty(propertyPath);

        component.setPropertyDataSource(itemProperty);

        setRequired(metaProperty.isMandatory());
        
        this.metaClass = metaProperty.getRange().asClass();
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
                    @Override
                    public String toString() {
                        if (CaptionMode.PROPERTY.equals(captionMode)) {
                            return String.valueOf(getValue() == null ? "" : ((Instance) getValue()).getValue(captionProperty));
                        } else {
                            return String.valueOf(getValue());
                        }
                    }
                };
            }
        };
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

    public class PickerFieldComponent extends CustomComponent implements com.vaadin.ui.Field {

        public static final int DEFAULT_WIDTH = 250;

        protected com.vaadin.ui.TextField field;
        protected com.vaadin.ui.Button pickerButton;
        protected com.vaadin.ui.Button clearButton;

        protected String buttonIcon;
        protected String clearButtonIcon;

        protected boolean required;
        protected String requiredError;

        public PickerFieldComponent() {
            field = new TextField() {
                @Override
                public boolean isRequired() {
                    return PickerFieldComponent.this.required;
                }

                @Override
                public String getRequiredError() {
                    return PickerFieldComponent.this.requiredError; 
                }
            };
            field.setReadOnly(true);
            field.setWidth("100%");
            field.setNullRepresentation("");

            pickerButton = new Button();
            pickerButton.addStyleName("pickButton");

            clearButton = new Button("", new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    if (isEditable()) {
                        __setValue(null);
                    }
                }
            });
            clearButton.addStyleName("clearButton");

            updateIcons();

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

        @Override
        public void paintContent(PaintTarget target) throws PaintException {
            paintCommonContent(target);
            super.paintContent(target);
        }

        protected void paintCommonContent(PaintTarget target) throws PaintException {
            // If the field is modified, but not committed, set modified attribute
            if (isModified()) {
                target.addAttribute("modified", true);
            }

            // Adds the required attribute
            if (!isReadOnly() && isRequired()) {
                target.addAttribute("required", true);
            }

            // Hide the error indicator if needed
            if (isRequired() && getValue() == null && getComponentError() == null
                    && getErrorMessage() != null) {
                target.addAttribute("hideErrors", true);
            }
        }

        private void updateIcons() {
            if (isReadOnly()) {
                setPickerButtonIcon(new ThemeResource("pickerfield/img/lookup-btn-readonly.png"));
                setClearButtonIcon(new ThemeResource("pickerfield/img/clear-btn-readonly.png"));
            } else {
                setPickerButtonIcon(new ThemeResource("pickerfield/img/lookup-btn.png"));
                setClearButtonIcon(new ThemeResource("pickerfield/img/clear-btn.png"));
            }
        }

        @Override
        public void setReadOnly(boolean readOnly) {
            super.setReadOnly(readOnly);
            updateIcons();
        }

        public void addListener(Button.ClickListener listener) {
            pickerButton.addListener(listener);
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
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
            requestRepaint();
        }

        public void setRequiredError(String requiredMessage) {
            this.requiredError = requiredMessage;
            requestRepaint();
        }

        public String getRequiredError() {
            return requiredError;
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
            if (icon != null) {
                pickerButton.setIcon(icon);
                pickerButton.addStyleName(BaseTheme.BUTTON_LINK);
            } else {
                pickerButton.setIcon(null);
                pickerButton.removeStyleName(BaseTheme.BUTTON_LINK);
            }
        }

        public Resource getPickerButtonIcon() {
            return pickerButton.getIcon();
        }

        public void setClearButtonIcon(Resource icon) {
            if (icon != null) {
                clearButton.setIcon(icon);
                clearButton.addStyleName(BaseTheme.BUTTON_LINK);
            } else {
                clearButton.setIcon(null);
                clearButton.removeStyleName(BaseTheme.BUTTON_LINK);
            }
        }

        public Resource getClearButtonIcon() {
            return clearButton.getIcon();
        }
    }
}
