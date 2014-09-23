/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebDateField;
import com.haulmont.cuba.web.gui.components.WebLookupField;
import com.haulmont.cuba.web.gui.components.WebPickerField;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.text.StrBuilder;

import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ListEditComponent extends CustomField {

    protected com.vaadin.ui.TextField field;
    protected com.vaadin.ui.Button pickerButton;
    protected com.vaadin.ui.Button clearButton;

    protected Class itemClass;
    protected MetaClass metaClass;
    protected CollectionDatasource collectionDatasource;
    protected List<String> runtimeEnum;

    protected List listValue;
    protected Map<Object, String> values = new LinkedHashMap<>();
    protected ListEditWindow listEditWindow;

    protected List<ValueChangeListener> listeners = new LinkedList<>();
    protected HorizontalLayout composition;

    public ListEditComponent(Class itemClass) {
        this.itemClass = itemClass;

        field = new TextField() {
            @Override
            public boolean isRequired() {
                return ListEditComponent.this.isRequired();
            }

            @Override
            public String getRequiredError() {
                return ListEditComponent.this.getRequiredError();
            }
        };
        field.setReadOnly(true);
        field.setWidth("100%");
        field.setNullRepresentation("");

        pickerButton = new CubaButton();
        pickerButton.addStyleName("pickButton");
        pickerButton.addClickListener(
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (listEditWindow == null) {
                            listEditWindow = new ListEditWindow(values);
                        } else {
                            listEditWindow.setValues(values);
                        }
                        com.haulmont.cuba.web.App.getInstance().getAppUI().addWindow(listEditWindow);
                    }
                }
        );

        clearButton = new CubaButton();
        clearButton.addStyleName("clearButton");
        clearButton.addClickListener(
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

        composition = new HorizontalLayout();
        composition.setWidth("100%");

        composition.addComponent(field);
        composition.addComponent(pickerButton);
        composition.addComponent(clearButton);
        composition.setExpandRatio(field, 1);

        setStyleName("cuba-pickerfield");

        ThemeConstants theme = App.getInstance().getThemeConstants();
        setWidth(theme.get("cuba.web.filter.ListEditComponent.width"));
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
    protected Component initContent() {
        return composition;
    }

    private void updateIcons() {
        if (isReadOnly()) {
            setPickerButtonIcon(new VersionedThemeResource("components/pickerfield/images/lookup-btn-readonly.png"));
            setClearButtonIcon(new VersionedThemeResource("components/pickerfield/images/clear-btn-readonly.png"));
        } else {
            setPickerButtonIcon(new VersionedThemeResource("components/pickerfield/images/lookup-btn.png"));
            setClearButtonIcon(new VersionedThemeResource("components/pickerfield/images/clear-btn.png"));
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        updateIcons();
    }

    public void addListener(Button.ClickListener listener) {
        pickerButton.addClickListener(listener);
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
    public void setBuffered(boolean buffered) {
        field.setBuffered(buffered);
    }

    @Override
    public boolean isBuffered() {
        return field.isBuffered();
    }

    @Override
    public boolean isModified() {
        return field.isModified();
    }

    @Override
    public Object getValue() {
        return listValue;
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
        if (!ObjectUtils.equals(listValue, newValue)) {
            listValue = (List) newValue;
            for (ValueChangeListener listener : listeners) {
                listener.valueChange(new ValueChangeEvent(this));
            }
        }
    }

    public void setValues(Map<Object, String> values) {
        this.values = values;
        if (values.isEmpty()) {
            setValue(null);
        } else {
            //noinspection unchecked
            setValue(new ArrayList(values.keySet()));
        }

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
    public void removeAllValidators() {
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
    public void addValueChangeListener(ValueChangeListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
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

        private VerticalLayout listLayout;
        private Map<Object, String> values;
        private Messages messages;

        private void setValues(Map<Object, String> values) {
            this.values = new HashMap<>(values);
        }

        private ListEditWindow(Map<Object, String> values) {
            this.messages = AppBeans.get(Messages.NAME);
            setCaption(messages.getMessage(MESSAGES_PACK, "ListEditWindow.caption"));

            ThemeConstants theme = App.getInstance().getThemeConstants();

            setWidth(theme.get("cuba.web.filter.ListEditComponent.window.width"));
            setHeight(theme.get("cuba.web.filter.ListEditComponent.window.height"));
            setModal(true);

            this.values = new HashMap<>(values);

            VerticalLayout contentLayout = new VerticalLayout();

            listLayout = new VerticalLayout();
            listLayout.setMargin(new MarginInfo(false, false, true, false));
            listLayout.setSpacing(true);
            listLayout.setHeight(-1, Unit.PIXELS);
            for (Map.Entry<Object, String> entry : values.entrySet()) {
                addItemLayout(entry.getKey(), entry.getValue());
            }

            Panel editAreaPanel = new Panel();
            editAreaPanel.setSizeFull();
            editAreaPanel.setContent(listLayout);
            contentLayout.addComponent(editAreaPanel);
            contentLayout.setExpandRatio(editAreaPanel, 1.0f);
            contentLayout.setHeight(100, Unit.PERCENTAGE);

            final Field field;
            Button addButton = null;
            HorizontalLayout dateFieldLayout = null;

            String pickerWidth = theme.get("cuba.web.filter.ListEditComponent.window.picker.width");

            if (collectionDatasource != null) {
                final WebLookupField lookup = new WebLookupField();
                lookup.setWidth(pickerWidth);
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
                        if (value != null && !containsValue((Instance) value)) {
                            String str = addEntityInstance((Instance) value);
                            addItemLayout(value, str);
                        }
                        lookup.setValue(null);
                    }
                });

                field = lookup.getComponent();

            } else if (metaClass != null) {
                final WebPickerField picker = new WebPickerField();
                picker.setWidth(pickerWidth);
                picker.setMetaClass(metaClass);
                PickerField.LookupAction action = picker.addLookupAction();
                action.setLookupScreenOpenType(WindowManager.OpenType.DIALOG);
                picker.addClearAction();

                picker.addListener(
                        new ValueListener() {
                            @Override
                            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                                if (value != null && !containsValue((Instance) value)) {
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
                lookup.setWidth(pickerWidth);
                lookup.setOptionsList(runtimeEnum);

                lookup.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        if (value != null && !containsValue((String) value)) {
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
                lookup.setWidth(pickerWidth);
                lookup.setOptionsMap(options);

                lookup.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        if (value != null && !containsValue((Enum) value)) {
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
                dateFieldLayout = new HorizontalLayout();

                // change width of dialog
                this.setWidth(theme.get("cuba.web.filter.ListEditComponent.window.withDate.width"));

                addButton = new CubaButton(messages.getMessage(getClass(), "actions.Add"));
                addButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        Date date = dateField.getValue();
                        if (date != null) {
                            String str = addDate(date);
                            addItemLayout(date, str);
                            field.setValue(null);
                        }
                    }
                });
                com.haulmont.cuba.gui.components.DateField.Resolution resolution;
                String dateFormat;
                if (itemClass.equals(java.sql.Date.class)) {
                    resolution = com.haulmont.cuba.gui.components.DateField.Resolution.DAY;
                    dateFormat = messages.getMessage(AppConfig.getMessagesPack(), "dateFormat");
                } else {
                    resolution = com.haulmont.cuba.gui.components.DateField.Resolution.MIN;
                    dateFormat = messages.getMessage(AppConfig.getMessagesPack(), "dateTimeFormat");
                }
                dateField.setResolution(resolution);
                dateField.setDateFormat(dateFormat);
            } else
                throw new UnsupportedOperationException();

            if (dateFieldLayout == null)
                contentLayout.addComponent(field);
            else {
                dateFieldLayout.setMargin(new MarginInfo(true, false, true, false));
                dateFieldLayout.setSpacing(true);
                dateFieldLayout.addComponent(field);
                dateFieldLayout.addComponent(addButton);
                contentLayout.addComponent(dateFieldLayout);
            }

            HorizontalLayout bottomLayout = new HorizontalLayout();
            bottomLayout.setMargin(new MarginInfo(true, false, true, false));
            bottomLayout.setSpacing(true);

            Button okBtn = new CubaButton(messages.getMessage(AppConfig.getMessagesPack(), "actions.Ok"));
            okBtn.setIcon(new VersionedThemeResource("icons/ok.png"));
            okBtn.setStyleName(WebButton.ICON_STYLE);
            okBtn.addClickListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            commitList();
                        }
                    }
            );
            bottomLayout.addComponent(okBtn);

            Button cancelBtn = new CubaButton(messages.getMessage(AppConfig.getMessagesPack(), "actions.Cancel"));
            cancelBtn.setIcon(new VersionedThemeResource("icons/cancel.png"));
            cancelBtn.setStyleName(WebButton.ICON_STYLE);
            cancelBtn.addClickListener(
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

        private boolean containsValue(String value) {
            return ListEditWindow.this.values.containsValue(value);
        }

        private boolean containsValue(Instance value) {
            return this.values.containsValue(value.getInstanceName());
        }

        private boolean containsValue(Enum value) {
            return this.values.containsValue(messages.getMessage(value));
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

            Button delItemBtn = new CubaButton();
            delItemBtn.setStyleName(BaseTheme.BUTTON_LINK);
            delItemBtn.setIcon(new VersionedThemeResource("icons/item-remove.png"));
            delItemBtn.addStyleName("filter-param-list-edit-del");
            delItemBtn.addClickListener(
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
            UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
            Locale locale = sessionSource.getUserSession().getLocale();
            Datatype datatype = Datatypes.get(itemClass);

            String str = datatype.format(date, locale);

            values.put(date, str);
            return str;
        }

        private void commitList() {
            ListEditComponent.this.setValues(values);
            close();
        }
    }
}