/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.filter.AbstractParam;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebDateField;
import com.haulmont.cuba.web.gui.components.WebLookupField;
import com.haulmont.cuba.web.gui.components.WebPickerField;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.TemporalType;
import java.text.ParseException;
import java.util.*;

public class Param extends AbstractParam<Component> {

    public static final String TEXT_COMPONENT_WIDTH = "120px";

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource, boolean inExpr, boolean required) {
        super(name,javaClass,entityWhere,entityView,datasource,inExpr, required);
    }

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource, boolean inExpr, UUID categoryAttrId, boolean required) {
        super(name,javaClass,entityWhere,entityView,datasource,inExpr,categoryAttrId, required);
    }

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource, MetaProperty property, boolean inExpr, boolean required) {
        super(name, javaClass, entityWhere, entityView, datasource, property, inExpr, required);
    }

    @Override
    public Component createEditComponent() {
        Component component;

        switch (type) {
            case DATATYPE:
                component = createDatatypeField(Datatypes.getNN(javaClass));
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
            case RUNTIME_ENUM:
                component = createRuntimeEnumLookup();
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
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object v = field.getValue();
                setValue(Boolean.TRUE.equals(v) ? true : null);
            }
        });

        field.setValue(BooleanUtils.isTrue((Boolean) value));
        return field;
    }

    private Component createDatatypeField(Datatype datatype) {
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
        field.setWidth(TEXT_COMPONENT_WIDTH);

        field.addListener(new Property.ValueChangeListener() {
            @Override
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
        if (value instanceof List) {
            StringBuilder stringValue = new StringBuilder();
            boolean firstPart = true;
            for (String val : (List<String>) value) {
                if (firstPart)
                    firstPart = false;
                else
                    stringValue.append(',');
                stringValue.append(val);
            }
            field.setValue(stringValue.toString());
        } else
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

        final WebDateField dateField = new WebDateField();

        com.haulmont.cuba.gui.components.DateField.Resolution resolution;
        String formatStr;
        boolean dateOnly = false;
        if (property != null) {
            TemporalType tt = (TemporalType) property.getAnnotations().get("temporal");
            dateOnly = (tt == TemporalType.DATE);
        } else if (javaClass.equals(java.sql.Date.class)) {
            dateOnly = true;
        }
        if (dateOnly) {
            resolution = com.haulmont.cuba.gui.components.DateField.Resolution.DAY;
            formatStr = MessageProvider.getMessage(AppConfig.getMessagesPack(), "dateFormat");
        } else {
            resolution = com.haulmont.cuba.gui.components.DateField.Resolution.MIN;
            formatStr = MessageProvider.getMessage(AppConfig.getMessagesPack(), "dateTimeFormat");
        }
        dateField.setResolution(resolution);
        dateField.setDateFormat(formatStr);


        dateField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                setValue(value);
            }
        });

        dateField.setValue(value);
        return dateField.getComponent();
    }

    private AbstractField createNumberField(final Datatype datatype) {
        final AbstractField field = new TextField();
        ((TextField) field).setNullRepresentation("");

        field.addListener(new Property.ValueChangeListener() {
            @Override
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
                                p = datatype.parse(part, UserSessionProvider.getLocale());
                            } catch (ParseException e) {
                                App.getInstance().getWindowManager().showNotification(MessageProvider.getMessage(AbstractParam.class,
                                        "Param.numberInvalid"), IFrame.NotificationType.ERROR);
                                return;
                            }
                            ((List) v).add(p);
                        }
                    } else {
                        try {
                            v = datatype.parse((String) value, UserSessionProvider.getLocale());
                        } catch (ParseException e) {
                            App.getInstance().getWindowManager().showNotification(MessageProvider.getMessage(AbstractParam.class,
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

        field.setValue(datatype.format(value, UserSessionProvider.getLocale()));
        return field;
    }

    private AbstractField createBooleanField() {
        final AbstractSelect field = new Select();
        field.setNullSelectionAllowed(true);
        field.setImmediate(true);

        field.addItem(Boolean.TRUE);
        field.setItemCaption(Boolean.TRUE, MessageProvider.getMessage(AbstractParam.class, "Boolean.TRUE"));

        field.addItem(Boolean.FALSE);
        field.setItemCaption(Boolean.FALSE, MessageProvider.getMessage(AbstractParam.class, "Boolean.FALSE"));

        field.addListener(new Property.ValueChangeListener() {
            @Override
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
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object value = field.getValue();
                if (value == null || value instanceof UUID)
                    setValue(value);
                else if ((value instanceof String  && !StringUtils.isBlank((String) value)) || value instanceof List)
                    if (inExpr) {
                        List list = new ArrayList();
                        if (value instanceof List) {
                            list = (List) value;
                            setValue(list);
                        } else {
                            String[] parts = ((String) value).split(",");
                            try {
                                for (String part : parts) {
                                    list.add(UUID.fromString(part.trim()));
                                }
                                setValue(list);
                            } catch (IllegalArgumentException ie) {
                                App.getInstance().getAppWindow().showNotification(MessageProvider.getMessage(AbstractParam.class,
                                        "Param.uuid.Err"), Window.Notification.TYPE_TRAY_NOTIFICATION);
                                setValue(null);
                            }
                        }
                    } else {
                        try{
                            setValue(UUID.fromString((String) value));
                        }catch(IllegalArgumentException ie){
                            App.getInstance().getAppWindow().showNotification(MessageProvider.getMessage(AbstractParam.class,
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
                picker.setWidth(TEXT_COMPONENT_WIDTH);
                picker.setFrame(datasource.getDsContext().getWindowContext().getFrame());
                picker.addLookupAction();
                picker.addClearAction();

                picker.addListener(
                        new ValueListener() {
                            @Override
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
                    .buildCollectionDatasource();

            ds.setRefreshOnComponentValueChange(true);
            ((DatasourceImplementation) ds).initialized();

            if (!StringUtils.isBlank(entityWhere)) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                        "select e from " + metaClass.getName() + " e",
                        metaClass.getName());
                transformer.addWhere(entityWhere);
                String q = transformer.getResult();
                ds.setQuery(q);
            }
            
            if (WindowParams.DISABLE_AUTO_REFRESH.getBool(datasource.getDsContext().getWindowContext())) {
                if (ds instanceof CollectionDatasource.Suspendable)
                    ((CollectionDatasource.Suspendable) ds).refreshIfNotSuspended();
                else
                    ds.refresh();
            }

            if (inExpr) {
                final ListEditComponent component = new ListEditComponent(ds);
                initListEdit(component);
                return component;

            } else {
                final WebLookupField lookup = new WebLookupField();
                lookup.setWidth(TEXT_COMPONENT_WIDTH);
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
                        setValue(value);
                    }
                });

                lookup.setValue(value);

                return lookup.getComponent();
            }
        }
    }

    private void initListEdit(final ListEditComponent component) {
        component.setWidth(TEXT_COMPONENT_WIDTH);
        component.addListener(
                new Property.ValueChangeListener() {
                    @Override
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

    private Component createRuntimeEnumLookup() {
        DataService dataService = ServiceLocator.lookup(DataService.NAME);
        LoadContext context = new LoadContext(CategoryAttribute.class);
        LoadContext.Query q = context.setQueryString("select a from sys$CategoryAttribute a where a.id = :id");
        context.setView("_local");
        q.addParameter("id", categoryAttrId);
        CategoryAttribute categoryAttribute = (CategoryAttribute) dataService.load(context);

        runtimeEnum = new LinkedList<String>();
        String enumerationString = categoryAttribute.getEnumeration();
        String[] array = StringUtils.split(enumerationString, ',');
        for (String s : array) {
            runtimeEnum.add(s);
        }

        if (inExpr) {
            final ListEditComponent component = new ListEditComponent(runtimeEnum);
            initListEdit(component);
            return component;

        } else {
            WebLookupField lookup = new WebLookupField();
            lookup.setOptionsList(runtimeEnum);

            lookup.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    setValue(value);
                }
            });

            lookup.setValue(value);

            return lookup.getComponent();
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
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    setValue(value);
                }
            });

            lookup.setValue(value);

            return lookup.getComponent();
        }
    }
}
