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

package com.haulmont.cuba.gui.components.filter;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.ValueConversionException;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.options.ContainerOptions;
import com.haulmont.cuba.gui.components.filter.dateinterval.DateInIntervalComponent;
import com.haulmont.cuba.gui.components.listeditor.ListEditorHelper;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataComponents;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.persistence.TemporalType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component(Param.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Param {

    public enum Type {
        ENTITY,
        ENUM,
        RUNTIME_ENUM,
        DATATYPE,
        UNARY
    }

    public enum ValueProperty {
        VALUE,
        DEFAULT_VALUE
    }

    public static final String NAME = "cuba_FilterParam";
    public static final String NULL = "NULL";

    protected static final List<Class> dateTimeClasses = ImmutableList.of(
            Date.class, java.sql.Date.class, LocalDate.class, LocalDateTime.class, OffsetDateTime.class);

    protected static final List<Class> timeClasses = ImmutableList.of(LocalTime.class, OffsetTime.class);

    protected String name;
    protected Type type;
    protected Class javaClass;
    protected Object value;
    protected Object defaultValue;
    protected boolean useUserTimeZone;
    protected String entityWhere;
    protected String entityView;
    protected MetaProperty property;
    protected boolean inExpr;
    protected boolean required;
    protected boolean isDateInterval;
    protected List<String> runtimeEnum;
    protected UUID categoryAttrId;
    protected Component editComponent;
    protected boolean isFoldersFilterEntitiesSet = false;

    @Inject
    protected BeanLocator beanLocator;

    @Inject
    protected Metadata metadata;
    @Inject
    protected Messages messages;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Configuration configuration;
    @Inject
    protected DataComponents dataComponents;

    @Inject
    protected DatatypeRegistry datatypeRegistry;

    protected ThemeConstants theme;

    private EventHub eventHub;

    private static final Logger log = LoggerFactory.getLogger(Param.class);

    protected EventHub getEventHub() {
        if (eventHub == null) {
            eventHub = new EventHub();
        }
        return eventHub;
    }

    public static class Builder {
        private String name;
        private Class javaClass;
        private String entityWhere;
        private String entityView;
        private MetaClass metaClass;
        private MetaProperty property;
        private boolean inExpr;
        private boolean required;
        private UUID categoryAttrId;
        private boolean isDateInterval;
        private boolean useUserTimeZone;

        private Builder() {
        }

        public static Builder getInstance() {
            return new Builder();
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setJavaClass(Class javaClass) {
            this.javaClass = javaClass;
            return this;
        }

        public Builder setEntityWhere(String entityWhere) {
            this.entityWhere = entityWhere;
            return this;
        }

        public Builder setEntityView(String entityView) {
            this.entityView = entityView;
            return this;
        }

        /**
         * @deprecated set java class instead
         */
        @Deprecated
        public Builder setMetaClass(MetaClass metaClass) {
            this.metaClass = metaClass;
            return this;
        }

        public Builder setProperty(MetaProperty property) {
            this.property = property;
            return this;
        }

        public Builder setInExpr(boolean inExpr) {
            this.inExpr = inExpr;
            return this;
        }

        public Builder setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder setCategoryAttrId(UUID categoryAttrId) {
            this.categoryAttrId = categoryAttrId;
            return this;
        }

        public Builder setUseUserTimeZone(boolean useUserTimeZone) {
            this.useUserTimeZone = useUserTimeZone;
            return this;
        }

        public Param build() {
            return AppBeans.getPrototype(Param.NAME, this);
        }

        public void setIsDateInterval(boolean isDateInterval) {
            this.isDateInterval = isDateInterval;
        }
    }

    public Param(Builder builder) {
        name = builder.name;
        setJavaClass(builder.javaClass);
        entityWhere = builder.entityWhere;
        entityView = (builder.entityView != null) ? builder.entityView : View.MINIMAL;
        property = builder.property;
        inExpr = builder.inExpr;
        required = builder.required;
        categoryAttrId = builder.categoryAttrId;
        isDateInterval = builder.isDateInterval;
        useUserTimeZone = builder.useUserTimeZone;

        if (DynamicAttributesUtils.isDynamicAttribute(builder.property)) {
            CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(builder.property);
            if (categoryAttribute.getDataType() == PropertyType.ENUMERATION) {
                type = Type.RUNTIME_ENUM;
            }
        }
    }

    @Inject
    protected void setThemeConstantsManager(ThemeConstantsManager themeManager) {
        this.theme = themeManager.getConstants();
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setJavaClass(Class javaClass) {
        if (javaClass != null) {
            this.javaClass = javaClass;
            if (Entity.class.isAssignableFrom(javaClass)) {
                type = Type.ENTITY;
            } else if (Enum.class.isAssignableFrom(javaClass)) {
                type = Type.ENUM;
            } else {
                type = Type.DATATYPE;
            }
        } else {
            type = Type.UNARY;
            this.javaClass = Boolean.class;
        }
    }

    public boolean isDateInterval() {
        return isDateInterval;
    }

    public void setDateInterval(boolean dateInterval) {
        isDateInterval = dateInterval;
    }

    public boolean isInExpr() {
        return inExpr;
    }

    public void setInExpr(boolean inExpr) {
        this.inExpr = inExpr;
    }

    /**
     * @return true if param is used for folder's filter entities set
     */
    public boolean isFoldersFilterEntitiesSet() {
        return isFoldersFilterEntitiesSet;
    }

    /**
     * Set true if param should be used for folder's filter entities set
     *
     * @param isFoldersFilterEntitiesSet filter entities set value
     */
    public void setFoldersFilterEntitiesSet(boolean isFoldersFilterEntitiesSet) {
        this.isFoldersFilterEntitiesSet = isFoldersFilterEntitiesSet;
    }

    public boolean isUseUserTimeZone() {
        return useUserTimeZone;
    }

    public void setUseUserTimeZone(boolean useUserTimeZone) {
        this.useUserTimeZone = useUserTimeZone;
    }

    public Class getJavaClass() {
        return javaClass;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        setValue(value, true);
    }

    @SuppressWarnings("unchecked")
    protected void setValue(Object value, boolean updateEditComponent) {
        if (!Objects.equals(value, this.value)) {
            Object prevValue = this.value;

            this.value = value;

            if (updateEditComponent && this.editComponent instanceof HasValue) {
                if (editComponent instanceof TextField) {
                    TextField textField = (TextField) this.editComponent;

                    if (value instanceof Collection) {
                        //if the value type is an array and the editComponent is a textField ('IN' condition for String attribute)
                        //then we should set the string value (not the array) to the text field
                        String caption = new TextStringBuilder().appendWithSeparators((Collection) value, ",").toString();
                        textField.setValue(caption);
                    } else {
                        textField.setValue(value);
                    }
                } else {
                    ((HasValue) editComponent).setValue(value);
                }
            }

            getEventHub().publish(ParamValueChangedEvent.class, new ParamValueChangedEvent(this, prevValue, value));
        }
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        if (value == null) {
            setValue(defaultValue, true);
        }
    }

    public void setDefaultValue(Object defaultValue, boolean updateEditComponent) {
        this.defaultValue = defaultValue;
        if (value == null) {
            setValue(defaultValue, updateEditComponent);
        }
    }

    @SuppressWarnings("unchecked")
    public void parseValue(String text) {
        if (NULL.equals(text)) {
            value = null;
            return;
        }

        if (inExpr) {
            if (StringUtils.isBlank(text)) {
                value = new ArrayList<>();
            } else {
                String[] parts = text.split(",");
                List list = new ArrayList(parts.length);
                if (type == Type.ENTITY) {
                    list = loadEntityList(parts);
                } else {
                    for (String part : parts) {
                        Object value = parseSingleValue(part);
                        if (value != null) {
                            list.add(value);
                        }
                    }
                }

                if (isFoldersFilterEntitiesSet) {
                    value = list;
                } else {
                    value = list.isEmpty() ? null : list;
                }
            }
        } else {
            value = parseSingleValue(text);
        }
    }

    protected Object parseSingleValue(String text) {
        Object value;
        switch (type) {
            case ENTITY:
                value = loadEntity(text);
                break;

            case ENUM:
                try {
                    value = Enum.valueOf(javaClass, text);
                } catch (IllegalArgumentException e) {
                    log.error("Cannot evaluate enum value: {}", e.getMessage());
                    value = null;
                }
                break;

            case RUNTIME_ENUM:
            case DATATYPE:
            case UNARY:
                Datatype datatype = Datatypes.getNN(javaClass);
                // hard code for compatibility with old datatypes
                if (datatype.getJavaClass().equals(Date.class)) {
                    try {
                        value = datatype.parse(text);
                    } catch (ParseException e) {
                        try {
                            value = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(text);
                        } catch (ParseException exception) {
                            throw new RuntimeException("Can not parse date from string " + text, e);
                        }
                    }
                } else {
                    try {
                        value = datatype.parse(text);
                    } catch (ParseException e) {
                        throw new RuntimeException("Parse exception for string " + text, e);
                    }
                }
                break;

            default:
                throw new IllegalStateException("Param type unknown");
        }
        return value;
    }

    protected List<Entity> loadEntityList(String[] ids) {
        MetaClass metaClass = metadata.getClassNN(javaClass);
        //noinspection unchecked
        LoadContext<Entity> ctx = new LoadContext<>(javaClass)
                .setView(View.BASE);
        ctx.setQueryString(String.format("select e from %s e where e.id in :ids", metaClass.getName()))
                .setParameter("ids", Arrays.asList(ids));
        return dataManager.loadList(ctx);
    }

    protected Object loadEntity(String id) {
        MetaProperty primaryKey = metadata.getTools().getPrimaryKeyProperty(metadata.getClassNN(javaClass));
        Object objectId = null;
        if (primaryKey != null) {
            try {
                objectId = primaryKey.getRange().asDatatype().parse(id);
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing entity ID", e);
            }
        }
        //noinspection unchecked
        LoadContext<Entity> ctx = new LoadContext<>(javaClass)
                .setView(View.BASE)
                .setId(objectId);
        return dataManager.load(ctx);
    }

    public String formatValue(Object value) {
        if (value == null) {
            return NULL;
        }

        if (value instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<Object> collection = (Collection) value;
            return collection.stream()
                    .map(this::formatSingleValue)
                    .collect(Collectors.joining(","));
        }

        return formatSingleValue(value);
    }

    protected String formatSingleValue(Object v) {
        switch (type) {
            case ENTITY:
                if (v instanceof UUID) {
                    return v.toString();
                } else if (v instanceof Entity) {
                    return ((Entity) v).getId().toString();
                }

            case ENUM:
                return ((Enum) v).name();
            case RUNTIME_ENUM:
            case DATATYPE:
            case UNARY:
                if (isDateInterval) {
                    return (String) v;
                }
                //noinspection unchecked
                return Datatypes.getNN(javaClass).format(v);

            default:
                throw new IllegalStateException("Param type unknown");
        }
    }

    /**
     * Creates an GUI component for condition parameter to specify default value.
     *
     * @return GUI component for condition parameter.
     */
    public Component createEditComponentForDefaultValue() {
        return createEditComponent(null, Param.ValueProperty.DEFAULT_VALUE);
    }

    /**
     * Creates an GUI component for condition parameter to specify value for filter.
     *
     * @return GUI component for condition parameter.
     */
    public Component createEditComponentForFilterValue(FilterDataContext filterDataContext) {
        return createEditComponent(filterDataContext, Param.ValueProperty.VALUE);
    }

    /**
     * Creates an GUI component for condition parameter.
     *
     * @param valueProperty What value the editor will be connected with: current filter value or default one.
     * @return GUI component for condition parameter.
     */
    protected Component createEditComponent(FilterDataContext context, ValueProperty valueProperty) {
        Component component;

        switch (type) {
            case DATATYPE:
                if (isDateInterval) {
                    DateInIntervalComponent dateInIntervalComponent = beanLocator.get(DateInIntervalComponent.class);
                    dateInIntervalComponent.addValueChangeListener(newValue -> {
                        _setValue(newValue == null ? null : newValue.getDescription(), valueProperty);
                    });
                    component = dateInIntervalComponent.createComponent((String) _getValue(valueProperty));
                } else {
                    component = createDatatypeField(Datatypes.getNN(javaClass), valueProperty);
                }
                break;
            case ENTITY:
                component = createEntityLookup(context, valueProperty);
                break;
            case UNARY:
                component = createUnaryField(valueProperty);
                break;
            case ENUM:
                component = createEnumLookup(valueProperty);
                break;
            case RUNTIME_ENUM:
                component = createRuntimeEnumLookup(valueProperty);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported param type: " + type);
        }

        this.editComponent = component;

        return component;
    }

    protected CheckBox createUnaryField(ValueProperty valueProperty) {
        CheckBox field = uiComponents.create(CheckBox.NAME);
        field.addValueChangeListener(e -> {
            Boolean newValue = BooleanUtils.isTrue(e.getValue()) ? true : null;
            _setValue(newValue, valueProperty);
        });
        field.setValue((Boolean) _getValue(valueProperty));
        field.setAlignment(Component.Alignment.MIDDLE_LEFT);
        return field;
    }

    protected Component createDatatypeField(Datatype datatype, ValueProperty valueProperty) {
        Component component;

        if (String.class.equals(javaClass)) {
            component = createTextField(valueProperty);
        } else if (dateTimeClasses.contains(javaClass)) {
            component = createDateField(javaClass, valueProperty);
        } else if (timeClasses.contains(javaClass)) {
            component = createTimeField(javaClass, valueProperty);
        } else if (Number.class.isAssignableFrom(javaClass)) {
            component = createNumberField(datatype, valueProperty);
        } else if (Boolean.class.isAssignableFrom(javaClass)) {
            component = createBooleanField(valueProperty);
        } else if (UUID.class.equals(javaClass)) {
            component = createUuidField(valueProperty);
        } else
            throw new UnsupportedOperationException("Unsupported param class: " + javaClass);

        return component;
    }

    protected void _setValue(Object value, ValueProperty valueProperty) {
        switch (valueProperty) {
            case VALUE:
                setValue(value, false);
                break;
            case DEFAULT_VALUE:
                setDefaultValue(value, false);
                break;
            default:
                throw new IllegalArgumentException(String.format("Value property %s not supported", valueProperty));
        }
    }

    protected Object _getValue(ValueProperty valueProperty) {
        switch (valueProperty) {
            case VALUE:
                return value;
            case DEFAULT_VALUE:
                return defaultValue;
            default:
                throw new IllegalArgumentException(String.format("Value property %s not supported", valueProperty));
        }
    }

    protected Component createTextField(ValueProperty valueProperty) {
        if (inExpr) {
            ListEditor listEditor = uiComponents.create(ListEditor.class);
            listEditor.setItemType(ListEditor.ItemType.STRING);
            listEditor.setDisplayValuesFieldEditable(true);
            initListEditor(listEditor, valueProperty);
            return listEditor;
        }

        TextField<String> field = uiComponents.create(TextField.NAME);
        field.setWidth(theme.get("cuba.gui.filter.Param.textComponent.width"));

        field.addValueChangeListener(e -> {
            String paramValue = null;
            if (!StringUtils.isBlank(e.getValue())) {
                paramValue = e.getValue();
            }

            if (configuration.getConfig(ClientConfig.class).getGenericFilterTrimParamValues()) {
                _setValue(StringUtils.trimToNull(paramValue), valueProperty);
            } else {
                _setValue(paramValue, valueProperty);
            }
        });

        Object _value = valueProperty == ValueProperty.DEFAULT_VALUE ? defaultValue : value;

        if (_value instanceof List) {
            field.setValue(StringUtils.join((Collection) _value, ","));
        } else if (_value instanceof String) {
            field.setValue((String) _value);
        } else {
            field.setValue("");
        }

        return field;
    }

    protected Component createDateField(Class javaClass, ValueProperty valueProperty) {
        UserSession userSession = userSessionSource.getUserSession();
        boolean supportTimezones = false;
        boolean dateOnly = false;
        if (property != null) {
            TemporalType tt = (TemporalType) property.getAnnotations().get(MetadataTools.TEMPORAL_ANN_NAME);
            dateOnly = tt == TemporalType.DATE || LocalDate.class.equals(javaClass);
            Object ignoreUserTimeZone = metadataTools.getMetaAnnotationValue(property, IgnoreUserTimeZone.class);
            supportTimezones = !dateOnly && !Boolean.TRUE.equals(ignoreUserTimeZone);
        } else if (LocalDate.class.equals(javaClass)) {
            dateOnly = true;
        } else if (java.sql.Date.class.equals(javaClass)) {
            dateOnly = true;
            if (useUserTimeZone) {
                supportTimezones = true;
            }
        } else {
            supportTimezones = true;
        }
        if (inExpr) {
            ListEditor listEditor = uiComponents.create(ListEditor.class);
            ListEditor.ItemType itemType = dateOnly ? ListEditor.ItemType.DATE : ListEditor.ItemType.DATETIME;
            listEditor.setItemType(itemType);
            if (userSession.getTimeZone() != null && supportTimezones) {
                listEditor.setTimeZone(userSession.getTimeZone());
            }
            initListEditor(listEditor, valueProperty);
            return listEditor;
        }

        DateField<Object> dateField = uiComponents.create(DateField.NAME);
        dateField.setDatatype(datatypeRegistry.getNN(javaClass));

        DateField.Resolution resolution;
        String formatStr;
        if (dateOnly) {
            resolution = com.haulmont.cuba.gui.components.DateField.Resolution.DAY;
            formatStr = messages.getMainMessage("dateFormat");
        } else {
            resolution = com.haulmont.cuba.gui.components.DateField.Resolution.MIN;
            formatStr = messages.getMainMessage("dateTimeFormat");
        }
        dateField.setResolution(resolution);
        dateField.setDateFormat(formatStr);
        if (userSession.getTimeZone() != null && supportTimezones) {
            dateField.setTimeZone(userSession.getTimeZone());
        }

        dateField.addValueChangeListener(e ->
                _setValue(e.getValue(), valueProperty));

        dateField.setValue(_getValue(valueProperty));
        return dateField;
    }

    protected Component createTimeField(Class javaClass, ValueProperty valueProperty) {
        TimeField<Object> timeField = uiComponents.create(TimeField.NAME);
        timeField.setDatatype(datatypeRegistry.get(javaClass));
        timeField.setFormat(messages.getMainMessage("timeFormat"));
        timeField.addValueChangeListener(e -> _setValue(e.getValue(), valueProperty));
        timeField.setValue(_getValue(valueProperty));
        timeField.setWidth(theme.get("cuba.gui.filter.Param.timeComponent.width"));
        return timeField;
    }

    protected Component createNumberField(Datatype datatype, ValueProperty valueProperty) {
        if (inExpr) {
            ListEditor listEditor = uiComponents.create(ListEditor.class);
            listEditor.setItemType(ListEditorHelper.itemTypeFromDatatype(datatype));
            listEditor.setDisplayValuesFieldEditable(true);
            initListEditor(listEditor, valueProperty);
            return listEditor;
        }
        TextField<String> field = uiComponents.create(TextField.NAME);

        field.addValueChangeListener(e -> {
            if (e.getValue() == null) {
                _setValue(e.getValue(), valueProperty);
            } else if (!StringUtils.isBlank(e.getValue())) {

                Object v;
                try {
                    v = datatype.parse(e.getValue(), userSessionSource.getLocale());
                } catch (ValueConversionException ex) {
                    showParseExceptionNotification(ex.getLocalizedMessage());
                    return;
                } catch (ParseException ex) {
                    showParseExceptionNotification(messages.getMainMessage("filter.param.numberInvalid"));
                    return;
                }
                _setValue(v, valueProperty);
            } else if (StringUtils.isBlank(e.getValue())) {
                _setValue(null, valueProperty);
            } else {
                throw new IllegalStateException("Invalid value: " + e.getValue());
            }
        });

        field.setValue(datatype.format(_getValue(valueProperty), userSessionSource.getLocale()));
        return field;
    }

    protected void showParseExceptionNotification(String message) {
        WindowManager wm = beanLocator.get(WindowManagerProvider.class).get();
        wm.showNotification(message, Frame.NotificationType.TRAY);
    }

    protected Component createBooleanField(ValueProperty valueProperty) {
        LookupField<Object> field = uiComponents.create(LookupField.NAME);
        field.setWidth(theme.get("cuba.gui.filter.Param.booleanLookup.width"));

        Map<String, Object> values = ParamsMap.of(
                messages.getMainMessage("filter.param.boolean.true"), Boolean.TRUE,
                messages.getMainMessage("filter.param.boolean.false"), Boolean.FALSE
        );

        field.setOptionsMap(values);
        field.addValueChangeListener(e ->
                _setValue(e.getValue(), valueProperty));

        field.setValue(_getValue(valueProperty));
        return field;
    }

    protected Component createUuidField(ValueProperty valueProperty) {
        if (inExpr) {
            ListEditor listEditor = uiComponents.create(ListEditor.class);
            listEditor.setItemType(ListEditor.ItemType.UUID);
            initListEditor(listEditor, valueProperty);
            return listEditor;
        }

        TextField<String> field = uiComponents.create(TextField.NAME);

        field.addValueChangeListener(e -> {
            String strValue = e.getValue();
            if (strValue == null) {
                _setValue(null, valueProperty);
            } else if (StringUtils.isNotBlank(strValue)) {
                try {
                    _setValue(UUID.fromString(strValue), valueProperty);
                } catch (IllegalArgumentException ie) {
                    beanLocator.get(WindowManagerProvider.class).get()
                            .showNotification(messages.getMainMessage("filter.param.uuid.Err"), Frame.NotificationType.TRAY);
                }
            } else {
                throw new IllegalStateException("Invalid value: " + strValue);
            }
        });

        Object _value = _getValue(valueProperty);
        if (_value instanceof String) {
            field.setValue((String) _value);
        }
        return field;
    }

    protected Component createEntityLookup(FilterDataContext filterDataContext, ValueProperty valueProperty) {
        MetaClass metaClass = metadata.getSession().getClassNN(javaClass);

        LookupType type = null;
        if (property != null && property.getRange().isClass()) {
            type = (LookupType) metadata.getTools()
                    .getMetaAnnotationAttributes(property.getAnnotations(), Lookup.class)
                    .get("type");
        }
        PersistenceManagerClient persistenceManager = beanLocator.get(PersistenceManagerClient.NAME);
        boolean useLookupScreen = type != null ?
                type == LookupType.SCREEN : persistenceManager.useLookupScreen(metaClass.getName());

        if (useLookupScreen) {
            if (inExpr) {
                ListEditor listEditor = uiComponents.create(ListEditor.class);
                listEditor.setItemType(ListEditor.ItemType.ENTITY);
                listEditor.setEntityName(metaClass.getName());
                initListEditor(listEditor, valueProperty);
                return listEditor;
            } else {
                PickerField<Entity> picker = uiComponents.create(PickerField.NAME);
                picker.setMetaClass(metaClass);

                picker.setWidth(theme.get("cuba.gui.filter.Param.textComponent.width"));
                picker.addLookupAction();
                picker.addClearAction();

                picker.addValueChangeListener(e ->
                        _setValue(e.getValue(), valueProperty));
                picker.setValue((Entity) _getValue(valueProperty));

                return picker;
            }
        } else {
            if (inExpr) {
                CollectionLoader<Entity> loader = createEntityOptionsLoader(metaClass);

                CollectionContainer<Entity> container =
                        dataComponents.createCollectionContainer(metaClass.getJavaClass());
                loader.setContainer(container);

                ListEditor listEditor = uiComponents.create(ListEditor.class);

                listEditor.setItemType(ListEditor.ItemType.ENTITY);
                listEditor.setEntityName(metaClass.getName());
                //noinspection unchecked
                listEditor.setOptions(new ContainerOptions<>(container));
                //noinspection unchecked
                initListEditor(listEditor, valueProperty);

                //noinspection unchecked
                Consumer<CollectionContainer.CollectionChangeEvent<?>> listener = e -> listEditor.setValue(null);

                if (filterDataContext != null) {
                    filterDataContext.registerCollectionLoader(this, loader);
                    filterDataContext.registerContainerCollectionChangeListener(this, container, listener);
                }

                return listEditor;
            } else {
                CollectionLoader<Entity> loader = createEntityOptionsLoader(metaClass);

                CollectionContainer<Entity> container =
                        dataComponents.createCollectionContainer(metaClass.getJavaClass());
                loader.setContainer(container);

                LookupPickerField<Entity> lookup = uiComponents.create(LookupPickerField.NAME);
                lookup.setWidth(theme.get("cuba.gui.filter.Param.textComponent.width"));
                lookup.addClearAction();
                lookup.setOptions(new ContainerOptions<>(container));

                Consumer<CollectionContainer.CollectionChangeEvent<?>> listener = e -> lookup.setValue(null);

                lookup.addValueChangeListener(e -> _setValue(e.getValue(), valueProperty));
                lookup.setValue((Entity) _getValue(valueProperty));

                if (filterDataContext != null) {
                    filterDataContext.registerCollectionLoader(this, loader);
                    filterDataContext.registerContainerCollectionChangeListener(this, container, listener);
                }

                return lookup;
            }
        }
    }

    protected CollectionLoader<Entity> createEntityOptionsLoader(MetaClass metaClass) {
        CollectionLoader<Entity> dataLoader = dataComponents.createCollectionLoader();

        dataLoader.setView(entityView);

        String query = String.format("select e from %s e", metaClass.getName());
        if (!Strings.isNullOrEmpty(entityWhere)) {
            QueryTransformer queryTransformer = QueryTransformerFactory.createTransformer(query);
            queryTransformer.addWhere(entityWhere);
            query = queryTransformer.getResult();
        }

        dataLoader.setQuery(query);

        return dataLoader;
    }

    protected Component createEnumLookup(ValueProperty valueProperty) {
        if (inExpr) {
            ListEditor listEditor = uiComponents.create(ListEditor.class);
            listEditor.setItemType(ListEditor.ItemType.ENUM);
            listEditor.setEnumClass(javaClass);
            initListEditor(listEditor, valueProperty);
            return listEditor;

        } else {
            LookupField<Object> lookup = uiComponents.create(LookupField.NAME);
            lookup.setOptionsEnum(javaClass);

            lookup.addValueChangeListener(e -> _setValue(e.getValue(), valueProperty));
            lookup.setValue(_getValue(valueProperty));

            return lookup;
        }
    }

    protected Component createRuntimeEnumLookup(ValueProperty valueProperty) {
        if (javaClass == Boolean.class) {
            return createBooleanField(valueProperty);
        }
        LoadContext<CategoryAttribute> context = new LoadContext<>(CategoryAttribute.class);
        LoadContext.Query q = context.setQueryString("select a from sys$CategoryAttribute a where a.id = :id");
        context.setView("_local");
        q.setParameter("id", categoryAttrId);
        CategoryAttribute categoryAttribute = dataManager.load(context);
        if (categoryAttribute == null) {
            throw new EntityAccessException(CategoryAttribute.class, categoryAttrId);
        }

        String enumerationString = categoryAttribute.getEnumeration();
        String[] array = StringUtils.split(enumerationString, ',');
        runtimeEnum = new ArrayList<>(array.length);
        for (String s : array) {
            String trimmedValue = StringUtils.trimToNull(s);
            if (trimmedValue != null) {
                runtimeEnum.add(trimmedValue);
            }
        }

        if (inExpr) {
            ListEditor listEditor = uiComponents.create(ListEditor.class);
            listEditor.setItemType(ListEditor.ItemType.STRING);
            listEditor.setOptionsMap(categoryAttribute.getLocalizedEnumerationMap());
            initListEditor(listEditor, valueProperty);
            return listEditor;
        } else {
            LookupField<Object> lookup = uiComponents.create(LookupField.NAME);
            lookup.setOptionsMap(categoryAttribute.getLocalizedEnumerationMap());

            lookup.addValueChangeListener(e -> {
                _setValue(e.getValue(), valueProperty);
            });

            lookup.setValue(_getValue(valueProperty));

            return lookup;
        }
    }

    protected void initListEditor(ListEditor<List<?>> listEditor, ValueProperty valueProperty) {
        listEditor.addValueChangeListener(e -> {
            Object value = e.getValue();
            if (value != null && ((List) value).isEmpty()) {
                value = null;
            }
            _setValue(value, valueProperty);
        });
        Object value = _getValue(valueProperty);
        if (value != null) {
            //noinspection unchecked
            listEditor.setValue((List<List<?>>) value);
        }
        listEditor.setClearButtonVisible(true);
    }

    public void toXml(Element element, ValueProperty valueProperty) {
        Element paramElem = element.addElement("param");
        paramElem.addAttribute("name", getName());
        paramElem.addAttribute("javaClass", getJavaClass().getName());
        if (runtimeEnum != null) {
            paramElem.addAttribute("categoryAttrId", categoryAttrId.toString());
        }
        if (isDateInterval) {
            paramElem.addAttribute("isDateInterval", "true");
        }

        paramElem.setText(formatValue(_getValue(valueProperty)));
    }

    /**
     * @param valueChangeHandler value change handler
     * @deprecated use {@link #addParamValueChangeListener(Consumer)}
     */
    @Deprecated
    public void addValueChangeListener(Consumer<ParamValueChangedEvent> valueChangeHandler) {
        getEventHub().subscribe(ParamValueChangedEvent.class, valueChangeHandler);
    }

    public Subscription addParamValueChangeListener(Consumer<ParamValueChangedEvent> valueChangeHandler) {
        return getEventHub().subscribe(ParamValueChangedEvent.class, valueChangeHandler);
    }

    public MetaProperty getProperty() {
        return property;
    }

    public static class ParamValueChangedEvent extends EventObject {
        protected final Object prevValue;
        protected final Object value;

        public ParamValueChangedEvent(Param source, Object prevValue, Object value) {
            super(source);
            this.prevValue = prevValue;
            this.value = value;
        }

        public Object getPrevValue() {
            return prevValue;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public Param getSource() {
            return (Param) super.getSource();
        }
    }
}