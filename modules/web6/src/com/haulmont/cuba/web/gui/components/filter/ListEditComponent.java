/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebDateField;
import com.haulmont.cuba.web.gui.components.WebLookupField;
import com.haulmont.cuba.web.gui.components.WebPickerField;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.terminal.*;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.text.StrBuilder;

import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ListEditComponent extends CustomComponent implements com.vaadin.ui.Field {

    public static final int DEFAULT_WIDTH = 250;

    protected com.vaadin.ui.TextField field;
    protected com.vaadin.ui.Button pickerButton;
    protected com.vaadin.ui.Button clearButton;

    protected boolean required;
    protected String requiredError;

    private Class itemClass;
    private MetaClass metaClass;
    private CollectionDatasource collectionDatasource;
    private List<String> runtimeEnum;

    private List listValue;
    private Map<Object, String> values = new LinkedHashMap<>();

    private List<ValueChangeListener> listeners = new ArrayList<>();

    public ListEditComponent(Class itemClass) {
        this.itemClass = itemClass;

        field = new TextField() {
            @Override
            public boolean isRequired() {
                return ListEditComponent.this.required;
            }

            @Override
            public String getRequiredError() {
                return ListEditComponent.this.requiredError;
            }
        };
        field.setReadOnly(true);
        field.setWidth("100%");
        field.setNullRepresentation("");

        pickerButton = new Button();
        pickerButton.addStyleName("pickButton");
        pickerButton.addListener(
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ListEditWindow window = new ListEditWindow(values);
                        com.haulmont.cuba.web.App.getInstance().getAppWindow().addWindow(window);
                    }
                }
        );

        clearButton = new Button();
        clearButton.addStyleName("clearButton");
        clearButton.addListener(
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        setValue(null);
                        values.clear();
                        field.setReadOnly(false);
                        field.setValue(null);
                        field.setReadOnly(true);
                    }
                }
        );

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

    public ListEditComponent(CollectionDatasource collectionDatasource) {
        this(collectionDatasource.getMetaClass().getJavaClass());
        this.collectionDatasource = collectionDatasource;
    }

    public ListEditComponent(MetaClass metaClass) {
        this(metaClass.getJavaClass());
        this.metaClass = metaClass;
    }

    public ListEditComponent(List<String> values) {
        this(String.class);
        this.runtimeEnum = values;
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

    @Override
    public boolean isInvalidCommitted() {
        return field.isInvalidCommitted();
    }

    @Override
    public void setInvalidCommitted(boolean isCommitted) {
        field.setInvalidCommitted(isCommitted);
    }

    @Override
    public void commit() throws SourceException, com.vaadin.data.Validator.InvalidValueException {
        field.commit();
    }

    @Override
    public void discard() throws SourceException {
        field.discard();
    }

    @Override
    public boolean isModified() {
        return field.isModified();
    }

    @Override
    public boolean isWriteThrough() {
        return field.isWriteThrough();
    }

    @Override
    public void setWriteThrough(boolean writeTrough) throws SourceException, com.vaadin.data.Validator.InvalidValueException {
        field.setWriteThrough(writeTrough);
    }

    @Override
    public boolean isReadThrough() {
        return field.isReadThrough();
    }

    @Override
    public void setReadThrough(boolean readTrough) throws SourceException {
        field.setReadThrough(readTrough);
    }

    @Override
    public Object getValue() {
        return listValue;
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        if (!ObjectUtils.equals(listValue, newValue)) {
            listValue = (List) newValue;
            for (ValueChangeListener listener : listeners) {
                listener.valueChange(new ValueChangeEvent(this));
            }
        }
    }

    public void setValues(Map<Object, String> values) {
        this.values = values;
        if (values.isEmpty())
            setValue(null);
        else
            setValue(new ArrayList(values.keySet()));

        String caption = new StrBuilder().appendWithSeparators(values.values(), ",").toString();
        field.setReadOnly(false);
        field.setValue(caption);
        field.setReadOnly(true);
    }

    public Map<Object, String> getValues() {
        return values;
    }

    @Override
    public Class getType() {
        return List.class;
    }

    @Override
    public Property getPropertyDataSource() {
        return field.getPropertyDataSource();
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        field.setPropertyDataSource(newDataSource);
    }

    @Override
    public void addValidator(com.vaadin.data.Validator validator) {
        field.addValidator(validator);
    }

    @Override
    public Collection<Validator> getValidators() {
        return field.getValidators();
    }

    @Override
    public void removeValidator(com.vaadin.data.Validator validator) {
        field.removeValidator(validator);
    }

    @Override
    public boolean isValid() {
        return field.isValid();
    }

    @Override
    public void validate() throws com.vaadin.data.Validator.InvalidValueException {
        field.validate();
    }

    @Override
    public boolean isInvalidAllowed() {
        return field.isInvalidAllowed();
    }

    @Override
    public void setInvalidAllowed(boolean invalidAllowed) throws UnsupportedOperationException {
        field.setInvalidAllowed(invalidAllowed);
    }

    @Override
    public void addListener(ValueChangeListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeListener(ValueChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        field.valueChange(event);
    }

    @Override
    public void focus() {
        field.focus();
    }

    @Override
    public int getTabIndex() {
        return field.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        field.setTabIndex(tabIndex);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
        requestRepaint();
    }

    @Override
    public void setRequiredError(String requiredMessage) {
        this.requiredError = requiredMessage;
        requestRepaint();
    }

    @Override
    public String getRequiredError() {
        return requiredError;
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

    private class ListEditWindow extends Window {
        protected static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";

        private static final String COMPONENT_WIDTH = "140";
        private VerticalLayout listLayout;
        private Map<Object, String> values;
        private Messages messages;

        private ListEditWindow(Map<Object, String> values) {
            super(AppBeans.get(Messages.class).getMessage(MESSAGES_PACK, "ListEditWindow.caption"));
            setWidth(200, Sizeable.UNITS_PIXELS);
            setModal(true);

            this.messages = AppBeans.get(Messages.class);

            this.values = new HashMap<>(values);

            VerticalLayout contentLayout = new VerticalLayout();

            listLayout = new VerticalLayout();
            listLayout.setMargin(false, false, true, false);
            listLayout.setSpacing(true);
            for (Map.Entry<Object, String> entry : values.entrySet()) {
                addItemLayout(entry.getKey(), entry.getValue());
            }
            contentLayout.addComponent(listLayout);

            final Field field;

            if (collectionDatasource != null) {
                final WebLookupField lookup = new WebLookupField();
                lookup.setWidth(COMPONENT_WIDTH);
                lookup.setOptionsDatasource(collectionDatasource);

                collectionDatasource.addListener(
                        new CollectionDsListenerAdapter<Entity>() {
                            @Override
                            public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                                lookup.setValue(null);
                            }
                        }
                );

                lookup.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        if (value != null && !containsName(value)) {
                            String str = addEntityInstance((Instance) value);
                            addItemLayout(value, str);
                        }
                        lookup.setValue(null);
                    }
                });

                field = lookup.getComponent();

            } else if (metaClass != null) {
                final WebPickerField picker = new WebPickerField();
                picker.setWidth(COMPONENT_WIDTH);
                picker.setMetaClass(metaClass);
                PickerField.LookupAction action = picker.addLookupAction();
                action.setLookupScreenOpenType(WindowManager.OpenType.DIALOG);
                picker.addClearAction();

                picker.addListener(
                        new ValueListener() {
                            @Override
                            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                                if (value != null && !containsName(value)) {
                                    String str = addEntityInstance((Instance) value);
                                    addItemLayout(value, str);
                                }
                                picker.setValue(null);
                            }
                        }
                );

                field = picker.getComponent();

            } else if (runtimeEnum != null) {
                final WebLookupField lookup = new WebLookupField();
                lookup.setWidth(COMPONENT_WIDTH);
                lookup.setOptionsList(runtimeEnum);

                lookup.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        if (value != null && !containsName(value)) {
                            String str = addRuntimeEnumValue((String) value);
                            addItemLayout(value, str);
                        }
                        lookup.setValue(null);
                    }
                });

                field = lookup.getComponent();
            } else if (itemClass.isEnum()) {
                Map<String, Object> options = new HashMap<>();
                for (Object obj : itemClass.getEnumConstants()) {
                    options.put(messages.getMessage((Enum) obj), obj);
                }

                final WebLookupField lookup = new WebLookupField();
                lookup.setWidth(COMPONENT_WIDTH);
                lookup.setOptionsMap(options);

                lookup.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        if (value != null && !containsName(value)) {
                            String str = addEnumValue((Enum) value);
                            addItemLayout(value, str);
                        }
                        lookup.setValue(null);
                    }
                });

                field = lookup.getComponent();

            } else if (Date.class.isAssignableFrom(itemClass)) {
                final WebDateField dateField = new WebDateField();

                field = dateField.getComponent();
                DateField.Resolution resolution;
                String dateFormat;
                if (itemClass.equals(java.sql.Date.class)) {
                    resolution = DateField.Resolution.DAY;
                    dateFormat = messages.getMessage(AppConfig.getMessagesPack(), "dateFormat");
                } else {
                    resolution = DateField.Resolution.MIN;
                    dateFormat = messages.getMessage(AppConfig.getMessagesPack(), "dateTimeFormat");
                }
                dateField.setResolution(resolution);
                dateField.setDateFormat(dateFormat);

                dateField.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        if (value != null) {
                            String str = addDate((Date) value);
                            addItemLayout(value, str);
                            field.setValue(null);
                        }
                    }
                });
            } else
                throw new UnsupportedOperationException();

            contentLayout.addComponent(field);

            HorizontalLayout bottomLayout = new HorizontalLayout();
            bottomLayout.setMargin(true, false, true, false);
            bottomLayout.setSpacing(true);

            Button okBtn = new Button(messages.getMessage(AppConfig.getMessagesPack(), "actions.Ok"));
            okBtn.setIcon(new ThemeResource("icons/ok.png"));
            okBtn.setStyleName(WebButton.ICON_STYLE);
            okBtn.addListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            commitList();
                        }
                    }
            );
            bottomLayout.addComponent(okBtn);

            Button cancelBtn = new Button(messages.getMessage(AppConfig.getMessagesPack(), "actions.Cancel"));
            cancelBtn.setIcon(new ThemeResource("icons/cancel.png"));
            cancelBtn.setStyleName(WebButton.ICON_STYLE);
            cancelBtn.addListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            close();
                        }
                    }
            );
            bottomLayout.addComponent(cancelBtn);

            contentLayout.addComponent(bottomLayout);

            setContent(contentLayout);
        }

        private boolean containsName(Object value) {
            String name = ((Instance) value).getInstanceName();
            Map<Object, String> values = ListEditWindow.this.values;
            return values.containsValue(name);
        }

        private String addRuntimeEnumValue(String value) {
            values.put(value, value);
            return value;
        }

        private String addEnumValue(Enum en) {
            String str = messages.getMessage(en);
            values.put(en, str);
            return str;
        }

        private String addEntityInstance(Instance value) {
            String str = value.getInstanceName();
            values.put(value, str);
            return str;
        }

        private void addItemLayout(final Object value, String str) {
            final HorizontalLayout itemLayout = new HorizontalLayout();
            itemLayout.setSpacing(true);

            Label itemLab = new Label(str);
            itemLayout.addComponent(itemLab);
            itemLayout.setComponentAlignment(itemLab, Alignment.MIDDLE_LEFT);

            Button delItemBtn = new Button();
            delItemBtn.setStyleName(BaseTheme.BUTTON_LINK);
            delItemBtn.setIcon(new ThemeResource("icons/tab-remove.png"));
            delItemBtn.addStyleName("filter-param-list-edit-del");
            delItemBtn.addListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            values.remove(value);
                            listLayout.removeComponent(itemLayout);
                        }
                    }
            );
            itemLayout.addComponent(delItemBtn);
            itemLayout.setComponentAlignment(delItemBtn, Alignment.MIDDLE_LEFT);

            listLayout.addComponent(itemLayout);
        }

        private String addDate(Date date) {
            String str = Datatypes.get(itemClass).format(date, AppBeans.get(UserSessionSource.class).getUserSession().getLocale());

            values.put(date, str);
            return str;
        }

        private void commitList() {
            setValues(values);
            close();
        }
    }
}