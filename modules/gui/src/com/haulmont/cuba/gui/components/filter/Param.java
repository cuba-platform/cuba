/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateTimeDatatype;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.condition.FilterConditionUtils;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import javax.persistence.TemporalType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author devyatkin
 * @version $Id$
 */
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

    public static final String NULL = "NULL";

    protected String name;
    protected Type type;
    protected Class javaClass;
    protected Object value;
    protected Object defaultValue;
    protected String entityWhere;
    protected String entityView;
    protected Datasource datasource;
    protected MetaProperty property;
    protected boolean inExpr;
    protected boolean required;
    protected List<String> runtimeEnum;
    protected UUID categoryAttrId;
    protected Component editComponent;

    protected Messages messages = AppBeans.get(Messages.NAME);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
    protected ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);
    protected ThemeConstants theme = AppBeans.get(ThemeConstantsManager.class).getConstants();

    private List<ValueListener> listeners = new ArrayList<>();

    public Param(String name, Class javaClass, String entityWhere, String entityView,
                 Datasource datasource, boolean inExpr, boolean required) {
        this(name, javaClass, entityWhere, entityView, datasource, null, inExpr, required);
    }

    public Param(String name, Class javaClass, String entityWhere, String entityView,
                 Datasource datasource, MetaProperty property, boolean inExpr, boolean required, UUID categoryAttrId) {
        this(name, javaClass, entityWhere, entityView, datasource, property, inExpr, required);
        this.categoryAttrId = categoryAttrId;
    }

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                 MetaProperty property, boolean inExpr, boolean required) {
        this.name = name;
        setJavaClass(javaClass);
        this.entityWhere = entityWhere;
        this.entityView = (entityView != null) ? entityView : View.MINIMAL;
        this.datasource = datasource;
        this.property = property;
        this.inExpr = inExpr;
        this.required = required;
        if (DynamicAttributesUtils.isDynamicAttribute(property)) {
            CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(property);
            if (categoryAttribute.getDataType() == PropertyType.ENUMERATION) {
                type = Type.RUNTIME_ENUM;
            }
        }
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

    public Class getJavaClass() {
        return javaClass;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        setValue(value, true);
    }

    protected void setValue(Object value, boolean updateEditComponent) {
        if (!ObjectUtils.equals(value, this.value)) {
            Object prevValue = this.value;
            this.value = value;
            for (ValueListener listener : listeners) {
                listener.valueChanged(this, "value", prevValue, value);
            }
            if (updateEditComponent && this.editComponent instanceof Component.HasValue) {
                ((Component.HasValue) editComponent).setValue(value);
            }
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

    public void parseValue(String text) {
        if (NULL.equals(text)) {
            value = null;
            return;
        }

        if (inExpr) {
            String[] parts = text.split(",");
            List list = new ArrayList(parts.length);
            for (String part : parts) {
                list.add(parseSingleValue(part));
            }
            value = list;
        } else {
            value = parseSingleValue(text);
        }
    }

    private Object parseSingleValue(String text) {
        Object value;
        switch (type) {
            case ENTITY:
                value = loadEntity(text);
                break;

            case ENUM:
                value = Enum.valueOf(javaClass, text);
                break;

            case RUNTIME_ENUM:
                value = text;
                break;

            case DATATYPE:
            case UNARY:
                Datatype datatype = Datatypes.getNN(javaClass);
                //hardcode for compatibility with old datatypes
                if (datatype instanceof DateTimeDatatype) {
                    try {
                        value = datatype.parse(text);
                    } catch (ParseException e) {
                        try {
                            value = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(text);
                        } catch (ParseException exception) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    try {
                        value = datatype.parse(text);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            default:
                throw new IllegalStateException("Param type unknown");
        }
        return value;
    }

    private Object loadEntity(String id) {
        LoadContext ctx = new LoadContext(javaClass).setId(UUID.fromString(id));
        DataService dataService = AppBeans.get(DataService.NAME);
        Entity entity = dataService.load(ctx);
        return entity;
    }

    public String formatValue(Object value) {
        if (value == null)
            return NULL;

        if (value instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            for (Iterator iterator = ((Collection) value).iterator(); iterator.hasNext(); ) {
                Object v = iterator.next();
                sb.append(formatSingleValue(v));
                if (iterator.hasNext())
                    sb.append(",");
            }
            return sb.toString();
        } else {
            return formatSingleValue(value);
        }
    }

    private String formatSingleValue(Object v) {
        switch (type) {
            case ENTITY:
                if (v instanceof UUID)
                    return v.toString();
                else if (v instanceof Entity)
                    return ((Entity) v).getId().toString();

            case ENUM:
                return ((Enum) v).name();
            case RUNTIME_ENUM:
                return (String) v;

            case DATATYPE:
            case UNARY:
                Datatype<Object> datatype = Datatypes.getNN(javaClass);
                return datatype.format(v);

            default:
                throw new IllegalStateException("Param type unknown");
        }
    }

    protected String getValueCaption(Object v) {
        if (v == null)
            return null;

        switch (type) {
            case ENTITY:
                if (v instanceof Instance)
                    return ((Instance) v).getInstanceName();
                else
                    return v.toString();

            case ENUM:
                return messages.getMessage((Enum) v);

            case RUNTIME_ENUM:
                return (String) v;

            case DATATYPE:
                return FilterConditionUtils.formatParamValue(this, v);
            case UNARY:
                Datatype<Object> datatype = Datatypes.getNN(javaClass);
                return datatype.format(v, userSessionSource.getLocale());

            default:
                throw new IllegalStateException("Param type unknown");
        }
    }

    /**
     * Creates an GUI component for condition parameter.
     *
     * @param valueProperty What value the editor will be connected with: current filter value or default one.
     * @return GUI component for condition parameter.
     */
    public Component createEditComponent(ValueProperty valueProperty) {
        Component component;

        switch (type) {
            case DATATYPE:
                component = createDatatypeField(Datatypes.getNN(javaClass), valueProperty);
                break;
            case ENTITY:
                component = createEntityLookup(valueProperty);
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

    private CheckBox createUnaryField(final ValueProperty valueProperty) {
        CheckBox field = componentsFactory.createComponent(CheckBox.class);
        field.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                value = BooleanUtils.isTrue((Boolean) value) ? true : null;
                _setValue(value, valueProperty);
            }
        });
        field.setValue(_getValue(valueProperty));
        field.setAlignment(Component.Alignment.MIDDLE_LEFT);
        return field;
    }

    private Component createDatatypeField(Datatype datatype, ValueProperty valueProperty) {
        Component component;

        if (String.class.equals(javaClass)) {
            component = createTextField(valueProperty);
        } else if (Date.class.isAssignableFrom(javaClass)) {
            component = createDateField(javaClass, valueProperty);
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

    private void _setValue(Object value, ValueProperty valueProperty) {
        switch (valueProperty) {
            case VALUE:
                setValue(value, false);
                break;
            case DEFAULT_VALUE:
                setDefaultValue(value);
                break;
            default:
                throw new IllegalArgumentException("Value property " + valueProperty + " not supported");
        }
    }

    private Object _getValue(ValueProperty valueProperty) {
        switch (valueProperty) {
            case VALUE:
                return value;
            case DEFAULT_VALUE:
                return defaultValue;
            default:
                throw new IllegalArgumentException("Value property " + valueProperty + " not supported");
        }
    }


    private Component createTextField(final ValueProperty valueProperty) {
        TextField field = componentsFactory.createComponent(TextField.class);

        field.setWidth(theme.get("cuba.gui.filter.Param.textComponent.width"));

        field.addListener(new ValueListener() {

            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                Object paramValue = null;
                if (!StringUtils.isBlank((String) value)) {
                    if (inExpr) {
                        paramValue = new ArrayList<String>();
                        String[] parts = ((String) value).split(",");
                        for (String part : parts) {
                            ((List) paramValue).add(part.trim());
                        }
                    } else {
                        paramValue = value;
                    }
                }

                if (paramValue instanceof String) {
                    Configuration configuration = AppBeans.get(Configuration.NAME);
                    if (configuration.getConfig(ClientConfig.class).getGenericFilterTrimParamValues()) {
                        _setValue(StringUtils.trimToNull((String) paramValue), valueProperty);
                    } else {
                        _setValue(paramValue, valueProperty);
                    }
                } else {
                    _setValue(paramValue, valueProperty);
                }

            }
        });

        Object _value = valueProperty == ValueProperty.DEFAULT_VALUE ? defaultValue : value;

        if (_value instanceof List) {
            field.setValue(StringUtils.join((Collection) _value, ","));
        } else if (_value instanceof String) {
            field.setValue(_value);
        } else {
            field.setValue("");
        }

        return field;
    }

    private Component createDateField(Class javaClass, final ValueProperty valueProperty) {
        if (inExpr) {
            if (property != null) {
                TemporalType tt = (TemporalType) property.getAnnotations().get("temporal");
                if (tt == TemporalType.DATE) {
                    javaClass = java.sql.Date.class;
                }
            }
            final InListParamComponent inListParamComponent = new InListParamComponent(javaClass);
            initListEdit(inListParamComponent, valueProperty);
            return inListParamComponent.getComponent();
        }

        DateField dateField = componentsFactory.createComponent(DateField.class);

        DateField.Resolution resolution;
        String formatStr;
        boolean dateOnly = false;
        if (property != null) {
            TemporalType tt = (TemporalType) property.getAnnotations().get("temporal");
            dateOnly = (tt == TemporalType.DATE);
        } else if (javaClass.equals(java.sql.Date.class)) {
            dateOnly = true;
        }
        Messages messages = AppBeans.get(Messages.NAME);

        if (dateOnly) {
            resolution = com.haulmont.cuba.gui.components.DateField.Resolution.DAY;
            formatStr = messages.getMessage(AppConfig.getMessagesPack(), "dateFormat");
        } else {
            resolution = com.haulmont.cuba.gui.components.DateField.Resolution.MIN;
            formatStr = messages.getMessage(AppConfig.getMessagesPack(), "dateTimeFormat");
        }
        dateField.setResolution(resolution);
        dateField.setDateFormat(formatStr);

        dateField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                _setValue(value, valueProperty);
            }
        });

        dateField.setValue(_getValue(valueProperty));
        return dateField;
    }

    private Component createNumberField(final Datatype datatype, final ValueProperty valueProperty) {
        TextField field = componentsFactory.createComponent(TextField.class);

        field.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                if (value == null || value instanceof Number)
                    _setValue(value, valueProperty);
                else if (value instanceof String && !StringUtils.isBlank((String) value)) {
                    UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
                    Messages messages = AppBeans.get(Messages.NAME);

                    Object v;
                    if (inExpr) {
                        v = new ArrayList();
                        String[] parts = ((String) value).split(",");
                        for (String part : parts) {
                            Object p;
                            try {
                                p = datatype.parse(part, userSessionSource.getLocale());
                            } catch (ParseException e) {
                                AppBeans.get(WindowManagerProvider.class).get().showNotification(messages.getMessage(Param.class,
                                        "Param.numberInvalid"), Frame.NotificationType.ERROR);
                                return;
                            }
                            ((List) v).add(p);
                        }
                    } else {
                        try {
                            v = datatype.parse((String) value, userSessionSource.getLocale());
                        } catch (ParseException e) {
                            AppBeans.get(WindowManagerProvider.class).get().showNotification(messages.getMessage(Param.class,
                                    "Param.numberInvalid"), Frame.NotificationType.ERROR);
                            return;
                        }
                    }
                    _setValue(v, valueProperty);
                } else if (value instanceof String && StringUtils.isBlank((String) value)) {
                    _setValue(null, valueProperty);
                } else
                    throw new IllegalStateException("Invalid value: " + value);
            }
        });

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        field.setValue(datatype.format(_getValue(valueProperty), sessionSource.getLocale()));
        return field;
    }

    private Component createBooleanField(final ValueProperty valueProperty) {
        Messages messages = AppBeans.get(Messages.NAME);
        ThemeConstants theme = AppBeans.get(ThemeConstantsManager.class).getConstants();

        LookupField field = componentsFactory.createComponent(LookupField.class);
        field.setWidth(theme.get("cuba.gui.filter.Param.booleanLookup.width"));

        Map<String, Object> values = new HashMap<>();
        values.put(messages.getMessage(Param.class, "Boolean.TRUE"), Boolean.TRUE);
        values.put(messages.getMessage(Param.class, "Boolean.FALSE"), Boolean.FALSE);

        field.setOptionsMap(values);

        field.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                _setValue(value, valueProperty);
            }
        });

        field.setValue(_getValue(valueProperty));
        return field;
    }

    private Component createUuidField(final ValueProperty valueProperty) {
        TextField field = componentsFactory.createComponent(TextField.class);

        field.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                String strValue = (String) value;
                if (strValue == null)
                    _setValue(strValue, valueProperty);
                else if ((!StringUtils.isBlank(strValue))) {
                    Messages messages = AppBeans.get(Messages.NAME);

                    if (inExpr) {
                        List list = new ArrayList();
                        String[] parts = strValue.split(",");
                        try {
                            for (String part : parts) {
                                list.add(UUID.fromString(part.trim()));
                            }
                            _setValue(list, valueProperty);
                        } catch (IllegalArgumentException ie) {
                            AppBeans.get(WindowManagerProvider.class).get().showNotification(messages.getMessage(Param.class,
                                    "Param.uuid.Err"), Frame.NotificationType.TRAY);
                            _setValue(null, valueProperty);
                        }
                    } else {
                        try {
                            _setValue(UUID.fromString(strValue), valueProperty);
                        } catch (IllegalArgumentException ie) {
                            AppBeans.get(WindowManagerProvider.class).get().showNotification(messages.getMessage(Param.class,
                                    "Param.uuid.Err"), Frame.NotificationType.TRAY);
                        }
                    }
                } else if (StringUtils.isBlank(strValue))
                    _setValue(null, valueProperty);
                else
                    throw new IllegalStateException("Invalid value: " + strValue);

            }
        });

        Object _value = _getValue(valueProperty);
        if (_value instanceof List) {
            field.setValue(StringUtils.join((Collection) _value, ","));
        } else if (_value instanceof String) {
            field.setValue(_value);
        } else {
            field.setValue("");
        }

        return field;
    }


    private Component createEntityLookup(final ValueProperty valueProperty) {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        MetaClass metaClass = metadata.getSession().getClass(javaClass);

        ThemeConstants theme = AppBeans.get(ThemeConstantsManager.class).getConstants();
        PersistenceManagerService persistenceManager = AppBeans.get(PersistenceManagerService.NAME);
        boolean useLookupScreen = persistenceManager.useLookupScreen(metaClass.getName());

        if (useLookupScreen) {
            if (inExpr) {
                final InListParamComponent inListParamComponent = new InListParamComponent(metaClass);
                initListEdit(inListParamComponent, valueProperty);
                return inListParamComponent.getComponent();
            } else {
                PickerField picker = componentsFactory.createComponent(PickerField.class);
                picker.setMetaClass(metaClass);

                picker.setWidth(theme.get("cuba.gui.filter.Param.textComponent.width"));
                picker.setFrame(datasource.getDsContext().getFrameContext().getFrame());
                picker.addLookupAction();
                picker.addClearAction();

                picker.addListener(
                        new ValueListener() {
                            @Override
                            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                                _setValue(value, valueProperty);
                            }
                        }
                );
                picker.setValue(_getValue(valueProperty));

                return picker;
            }
        } else {
            CollectionDatasource ds = new DsBuilder(datasource.getDsContext())
                    .setMetaClass(metaClass)
                    .setViewName(entityView)
                    .buildCollectionDatasource();

            ds.setRefreshOnComponentValueChange(true);
            ((DatasourceImplementation) ds).initialized();

            if (!StringUtils.isBlank(entityWhere)) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                        "select e from " + metaClass.getName() + " e");
                transformer.addWhere(entityWhere);
                String q = transformer.getResult();
                ds.setQuery(q);
            }

            if (WindowParams.DISABLE_AUTO_REFRESH.getBool(datasource.getDsContext().getFrameContext())) {
                if (ds instanceof CollectionDatasource.Suspendable)
                    ((CollectionDatasource.Suspendable) ds).refreshIfNotSuspended();
                else
                    ds.refresh();
            }

            if (inExpr) {
                final InListParamComponent inListParamComponent = new InListParamComponent(ds);
                initListEdit(inListParamComponent, valueProperty);
                return inListParamComponent.getComponent();
            } else {
                final LookupPickerField lookup = componentsFactory.createComponent(LookupPickerField.class);
                lookup.addClearAction();
                lookup.setWidth(theme.get("cuba.gui.filter.Param.textComponent.width"));
                lookup.setOptionsDatasource(ds);

                ds.addListener(
                        new CollectionDsListenerAdapter<Entity>() {
                            @Override
                            public void collectionChanged(CollectionDatasource ds, Operation operation,
                                                          List<Entity> items) {
                                lookup.setValue(null);
                            }
                        }
                );

                lookup.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        _setValue(value, valueProperty);
                    }
                });

                lookup.setValue(_getValue(valueProperty));

                return lookup;
            }
        }
    }

    private Component createEnumLookup(final ValueProperty valueProperty) {
        if (inExpr) {
            final InListParamComponent inListParamComponent = new InListParamComponent(javaClass);
            initListEdit(inListParamComponent, valueProperty);
            return inListParamComponent.getComponent();
        } else {
            Map<String, Object> options = new HashMap<>();
            Messages messages = AppBeans.get(Messages.NAME);
            for (Object obj : javaClass.getEnumConstants()) {
                options.put(messages.getMessage((Enum) obj), obj);
            }

            LookupField lookup = componentsFactory.createComponent(LookupField.class);
            lookup.setOptionsMap(options);

            lookup.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    _setValue(value, valueProperty);
                }
            });

            lookup.setValue(_getValue(valueProperty));

            return lookup;
        }
    }

    private Component createRuntimeEnumLookup(final ValueProperty valueProperty) {
        DataService dataService = AppBeans.get(DataService.NAME);
        LoadContext<CategoryAttribute> context = new LoadContext<>(CategoryAttribute.class);
        LoadContext.Query q = context.setQueryString("select a from sys$CategoryAttribute a where a.id = :id");
        context.setView("_local");
        q.setParameter("id", categoryAttrId);
        CategoryAttribute categoryAttribute = dataService.load(context);
        if (categoryAttribute == null)
            throw new EntityAccessException();

        runtimeEnum = new LinkedList<>();
        String enumerationString = categoryAttribute.getEnumeration();
        String[] array = StringUtils.split(enumerationString, ',');
        for (String s : array) {
            String trimmedValue = StringUtils.trimToNull(s);
            if (trimmedValue != null) {
                runtimeEnum.add(trimmedValue);
            }
        }

        if (inExpr) {
            final InListParamComponent inListParamComponent = new InListParamComponent(runtimeEnum);
            initListEdit(inListParamComponent, valueProperty);
            return inListParamComponent.getComponent();
        } else {
            LookupField lookup = componentsFactory.createComponent(LookupField.class);
            lookup.setOptionsList(runtimeEnum);

            lookup.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    _setValue(value, valueProperty);
                }
            });

            lookup.setValue(_getValue(valueProperty));

            return lookup;
        }
    }

    private void initListEdit(final InListParamComponent component, final ValueProperty valueProperty) {
        component.addValueListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                _setValue(value, valueProperty);

            }
        });
        Object _value = _getValue(valueProperty);
        if (_value != null) {
            Map<Object, String> values = new HashMap<>();
            for (Object v : (List) _value) {
                values.put(v, getValueCaption(v));
            }
            component.setValues(values);
        }
    }

    public void toXml(Element element, ValueProperty valueProperty) {
        Element paramElem = element.addElement("param");
        paramElem.addAttribute("name", getName());
        paramElem.addAttribute("javaClass", getJavaClass().getName());
        if (runtimeEnum != null) {
            paramElem.addAttribute("categoryAttrId", categoryAttrId.toString());
        }

        paramElem.setText(formatValue(_getValue(valueProperty)));
    }

    public void addListener(ValueListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    public MetaProperty getProperty() {
        return property;
    }
}