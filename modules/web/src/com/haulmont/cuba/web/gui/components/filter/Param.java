/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.10.2009 16:29:55
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebLookupField;
import com.haulmont.cuba.web.gui.components.WebPickerField;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.TemporalType;
import java.text.ParseException;
import java.util.*;

public class Param {

    public enum Type {
        ENTITY,
        ENUM,
        DATATYPE,
        UNARY
    }

    public static final String NULL = "NULL";

    private String name;
    private Type type;
    private Class javaClass;
    private Object value;
    private String entityWhere;
    private String entityView;
    private Datasource datasource;
    private MetaProperty property;
    private boolean inExpr;

    private List<ValueListener> listeners = new ArrayList<ValueListener>();

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource, boolean inExpr) {
        this(name, javaClass, entityWhere, entityView, datasource, null, inExpr);
    }

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                 MetaProperty property, boolean inExpr)
    {
        this.name = name;
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
        this.entityWhere = entityWhere;
        this.entityView = entityView;
        this.datasource = datasource;
        this.property = property;
        this.inExpr = inExpr;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Class getJavaClass() {
        return javaClass;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        if (!ObjectUtils.equals(value, this.value)) {
            Object prevValue = this.value;
            this.value = value;
            for (ValueListener listener : listeners) {
                listener.valueChanged(this, "value", prevValue, value);
            }
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

            case DATATYPE:
            case UNARY:
                Datatype datatype = Datatypes.getInstance().get(javaClass);
                if (datatype == null)
                    throw new UnsupportedOperationException("Unsupported parameter class: " + javaClass);

                try {
                    value = datatype.parse(text);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;

            default:
                throw new IllegalStateException("Param type unknown");
        }
        return value;
    }

    private Object loadEntity(String id) {
        DataService service = ServiceLocator.getDataService();
        LoadContext ctx = new LoadContext(javaClass).setId(UUID.fromString(id));
        Entity entity = service.load(ctx);
        return entity;
    }

    public String formatValue() {
        if (value == null)
            return NULL;

        if (value instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            for (Iterator iterator = ((Collection) value).iterator(); iterator.hasNext();) {
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

            case DATATYPE:
            case UNARY:
                Datatype datatype = Datatypes.getInstance().get(javaClass);
                if (datatype == null)
                    throw new UnsupportedOperationException("Unsupported parameter class: " + javaClass);

                return datatype.format(v);

            default:
                throw new IllegalStateException("Param type unknown");
        }
    }

    public Component createEditComponent() {
        Component component;

        switch (type) {
            case DATATYPE:
                component = createDatatypeField(Datatypes.getInstance().get(javaClass));
                break;
            case ENTITY:
                component = createEntityLookup();
                break;
            case UNARY:
                component = createUnaryField();
                break;
            case ENUM:
                component = createEnumLookup();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported param type: " + type);
        }

        return component;
    }

    private AbstractField createUnaryField() {
        final CheckBox field = new CheckBox();
        field.setImmediate(true);

        field.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object v = field.getValue();
                setValue(Boolean.TRUE.equals(v) ? true : null);
            }
        });

        field.setValue(BooleanUtils.isTrue((Boolean) value));
        return field;
    }

    private Component createDatatypeField(Datatype datatype) {
        if (datatype == null)
            throw new IllegalStateException("Unable to find Datatype for " + javaClass);

        Component component;

        if (String.class.equals(javaClass)) {
            component = createTextField();
        } else if (Date.class.isAssignableFrom(javaClass)) {
            component = createDateField(javaClass);
        } else if (Number.class.isAssignableFrom(javaClass)) {
            component = createNumberField(datatype);
        } else if (Boolean.class.isAssignableFrom(javaClass)) {
            component = createBooleanField();
        } else if (UUID.class.equals(javaClass)) {
            component = createUuidField();
        } else
            throw new UnsupportedOperationException("Unsupported param class: " + javaClass);

        return component;
    }

    private Component createTextField() {
        final TextField field = new com.haulmont.cuba.web.toolkit.ui.TextField();
        field.setNullRepresentation("");

        field.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object value = null;
                if (!StringUtils.isBlank((String) field.getValue())) {
                    if (inExpr) {
                        value = new ArrayList<String>();
                        String[] parts = ((String) field.getValue()).split(",");
                        for (String part : parts) {
                            ((List) value).add(part.trim());
                        }
                    } else {
                        value = field.getValue();
                    }
                }
                setValue(value);
            }
        });

        field.setValue(value);
        return field;
    }

    private Component createDateField(Class javaClass) {
        if (inExpr) {
            if (property != null) {
                TemporalType tt = (TemporalType) property.getAnnotations().get("temporal");
                if (tt == TemporalType.DATE) {
                    javaClass = java.sql.Date.class;
                }
            }
            final ListEditComponent component = new ListEditComponent(javaClass);
            initListEdit(component);
            return component;
        }

        final AbstractField field = new com.haulmont.cuba.web.toolkit.ui.DateField();
        field.setImmediate(true);

        int resolution;
        String formatStr;
        boolean dateOnly = false;
        if (property != null) {
            TemporalType tt = (TemporalType) property.getAnnotations().get("temporal");
            dateOnly = (tt == TemporalType.DATE);
        } else if (javaClass.equals(java.sql.Date.class)) {
            dateOnly = true;
        }
        if (dateOnly) {
            resolution = DateField.RESOLUTION_DAY;
            formatStr = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "dateFormat");
        } else {
            resolution = DateField.RESOLUTION_MIN;
            formatStr = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "dateTimeFormat");
        }
        ((DateField) field).setResolution(resolution);
        ((DateField) field).setDateFormat(formatStr);

        field.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                setValue(field.getValue());
            }
        });

        field.setValue(value);
        return field;
    }

    private AbstractField createNumberField(final Datatype datatype) {
        final AbstractField field = new TextField();
        ((TextField) field).setNullRepresentation("");

        field.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object value = field.getValue();
                if (value == null || value instanceof Number)
                    setValue(value);
                else if (value instanceof String && !StringUtils.isBlank((String) value)) {
                    Object v;
                    if (inExpr) {
                        v = new ArrayList();
                        String[] parts = ((String) value).split(",");
                        for (String part : parts) {
                            Object p;
                            try {
                                p = datatype.parse(part);
                            } catch (ParseException e) {
                                App.getInstance().getWindowManager().showNotification(MessageProvider.getMessage(Param.class,
                                        "Param.numberInvalid"), IFrame.NotificationType.ERROR);
                                return;
                            }
                            ((List) v).add(p);
                        }
                    } else {
                        try {
                            v = datatype.parse((String) value);
                        } catch (ParseException e) {
                            App.getInstance().getWindowManager().showNotification(MessageProvider.getMessage(Param.class,
                                    "Param.numberInvalid"), IFrame.NotificationType.ERROR);
                            return;
                        }
                    }
                    setValue(v);
                } else if (value instanceof String && StringUtils.isBlank((String) value)) {
                    setValue(null);
                } else
                    throw new IllegalStateException("Invalid value: " + value);
            }
        });

        field.setValue(value);
        return field;
    }

    private AbstractField createBooleanField() {
        final AbstractSelect field = new Select();
        field.setNullSelectionAllowed(true);
        field.setImmediate(true);

        field.addItem(Boolean.TRUE);
        field.setItemCaption(Boolean.TRUE, MessageProvider.getMessage(getClass(), "Boolean.TRUE"));

        field.addItem(Boolean.FALSE);
        field.setItemCaption(Boolean.FALSE, MessageProvider.getMessage(getClass(), "Boolean.FALSE"));

        field.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                setValue(field.getValue());
            }
        });

        field.setValue(value);
        return field;
    }

    private AbstractField createUuidField() {
        final TextField field = new TextField();
        field.setNullRepresentation("");

        field.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object value = field.getValue();
                if (value == null || value instanceof UUID)
                    setValue(value);
                else if (value instanceof String && !StringUtils.isBlank((String) value))
                    if (inExpr) {
                        List list = new ArrayList();
                        String[] parts = ((String) value).split(",");
                        for (String part : parts) {
                            list.add(UUID.fromString(part.trim()));
                        }
                        setValue(list);
                    } else {
                        try{
                            setValue(UUID.fromString((String) value));
                        }catch(IllegalArgumentException ie){
                            App.getInstance().getAppWindow().showNotification(MessageProvider.getMessage(this.getClass(),
                                    "Param.uuid.Err"), Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                else if (value instanceof String && StringUtils.isBlank((String) value))
                    setValue(null);
                else
                    throw new IllegalStateException("Invalid value: " + value);
            }
        });

        field.setValue(value);
        return field;
    }

    private Component createEntityLookup() {
        MetaClass metaClass = MetadataProvider.getSession().getClass(javaClass);

        PersistenceManagerService persistenceManager = ServiceLocator.lookup(PersistenceManagerService.NAME);
        boolean useLookupScreen = persistenceManager.useLookupScreen(metaClass.getName());

        if (useLookupScreen) {
            if (inExpr) {
                final ListEditComponent component = new ListEditComponent(metaClass);
                initListEdit(component);
                return component;

            } else {
                WebPickerField picker = new WebPickerField();
                picker.setMetaClass(metaClass);

                picker.addListener(
                        new ValueListener() {
                            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                                setValue(value);
                            }
                        }
                );
                picker.setValue(value);

                return picker.getComponent();
            }
        } else {
            CollectionDatasource ds = new DsBuilder(datasource.getDsContext())
                    .setMetaClass(metaClass)
                    .setViewName(entityView)
                    .setFetchMode(CollectionDatasource.FetchMode.AUTO)
                    .buildCollectionDatasource();

            ds.setRefreshOnComponentValueChange(true);
            ((DatasourceImplementation) ds).initialized();

            Map<String, Object> params = datasource.getDsContext().getWindowContext().getParams();
            if (BooleanUtils.isTrue((Boolean) params.get("disableAutoRefresh"))) {
                if (ds instanceof CollectionDatasource.Suspendable)
                    ((CollectionDatasource.Suspendable) ds).refreshIfNotSuspended();
                else
                    ds.refresh();
            }

            if (!StringUtils.isBlank(entityWhere)) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                        "select e from " + metaClass.getName() + " e",
                        metaClass.getName());
                transformer.addWhere(entityWhere);
                String q = transformer.getResult();
                ds.setQuery(q);
            }

            if (inExpr) {
                final ListEditComponent component = new ListEditComponent(ds);
                initListEdit(component);
                return component;

            } else {
                final WebLookupField lookup = new WebLookupField();
                lookup.setOptionsDatasource(ds);

                ds.addListener(
                        new CollectionDsListenerAdapter() {
                            @Override
                            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                                lookup.setValue(null);
                            }
                        }
                );

                lookup.addListener(new ValueListener() {
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        setValue(value);
                    }
                });

                lookup.setValue(value);

                return lookup.getComponent();
            }
        }
    }

    private void initListEdit(final ListEditComponent component) {
        component.addListener(
                new Property.ValueChangeListener() {
                    public void valueChange(Property.ValueChangeEvent event) {
                        setValue(component.getValue());
                    }
                }
        );
        if (value != null) {
            Map<Object, String> values = new HashMap<Object, String>();
            for (Object v : (List) value) {
                values.put(v, getValueCaption(v));
            }
            component.setValues(values);
        }
    }

    private String getValueCaption(Object v) {
        if (v == null)
            return null;

        switch (type) {
            case ENTITY:
                if (v instanceof Instance)
                    return ((Instance) v).getInstanceName();
                else
                    v.toString();

            case ENUM:
                return MessageProvider.getMessage((Enum) v);

            case DATATYPE:
            case UNARY:
                Datatype datatype = Datatypes.get(javaClass);
                if (datatype == null)
                    throw new UnsupportedOperationException("Unsupported parameter class: " + javaClass);

                return datatype.format(v, UserSessionProvider.getLocale());

            default:
                throw new IllegalStateException("Param type unknown");
        }
    }

    private Component createEnumLookup() {
        if (inExpr) {
            final ListEditComponent component = new ListEditComponent(javaClass);
            initListEdit(component);
            return component;

        } else {
            Map<String, Object> options = new HashMap<String, Object>();
            for (Object obj : javaClass.getEnumConstants()) {
                options.put(MessageProvider.getMessage((Enum) obj), obj);
            }

            WebLookupField lookup = new WebLookupField();
            lookup.setOptionsMap(options);

            lookup.addListener(new ValueListener() {
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    setValue(value);
                }
            });

            lookup.setValue(value);

            return lookup.getComponent();
        }
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
