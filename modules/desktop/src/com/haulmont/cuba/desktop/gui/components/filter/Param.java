/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.gui.components.*;
import com.haulmont.cuba.desktop.sys.vcl.ExtendedComboBox;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.filter.AbstractParam;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.TemporalType;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.*;

/**
 * @author devyatkin
 * @version $Id$
 */
public class Param extends AbstractParam<ParamEditorComponent> {

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                 boolean inExpr, boolean required) {
        super(name, javaClass, entityWhere, entityView, datasource, inExpr, required);
    }

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                 boolean inExpr, UUID categoryAttrId, boolean required) {
        super(name, javaClass, entityWhere, entityView, datasource, inExpr, categoryAttrId, required);
    }

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                 MetaProperty property, boolean inExpr, boolean required) {
        super(name, javaClass, entityWhere, entityView, datasource, property, inExpr, required);
    }

    @Override
    public ParamEditorComponent createEditComponent() {
        ParamEditorComponent component;

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

    protected ParamEditorComponent createUnaryField() {
        final JCheckBox field = new JCheckBox();

        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean v = field.isSelected();
                setValue(Boolean.TRUE.equals(v) ? true : null);
            }
        });

        field.setSelected(BooleanUtils.isTrue((Boolean) value));
        return new NonRequiredParamEditorComponent(field);
    }

    protected ParamEditorComponent createDatatypeField(Datatype datatype) {
        ParamEditorComponent component;

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

    protected ParamEditorComponent createTextField() {
        final DesktopTextField field = new DesktopTextField();
        field.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (!StringUtils.isBlank((String) field.getValue())) {
                    if (inExpr) {
                        value = new ArrayList<String>();
                        String[] parts = ((String) field.getValue()).split(",");
                        for (String part : parts) {
                            ((java.util.List) value).add(part.trim());
                        }
                    } else {
                        value = field.getValue();
                    }
                }

                if (value instanceof String) {
                    Configuration configuration = AppBeans.get(Configuration.NAME);
                    if (configuration.getConfig(ClientConfig.class).getGenericFilterTrimParamValues()) {
                        setValue(StringUtils.trimToNull((String) value));
                    } else {
                        setValue(value);
                    }
                } else {
                    setValue(value);
                }
            }
        });
        if (value instanceof java.util.List) {
            StringBuilder stringValue = new StringBuilder();
            boolean firstPart = true;
            for (String val : (java.util.List<String>) value) {
                if (firstPart)
                    firstPart = false;
                else
                    stringValue.append(',');
                stringValue.append(val);
            }
            field.setValue(stringValue.toString());
        } else
            field.setValue(value);

        return new FieldParamEditorComponent(field);
    }

    protected ParamEditorComponent createDateField(Class javaClass) {
        if (inExpr) {
            if (property != null) {
                TemporalType tt = (TemporalType) property.getAnnotations().get("temporal");
                if (tt == TemporalType.DATE) {
                    javaClass = java.sql.Date.class;
                }
            }
            final ListEditComponent component = new ListEditComponent(javaClass);
            initListEdit(component);
            return new NonRequiredParamEditorComponent(component);
        }

        final DesktopDateField field = new DesktopDateField();

        DateField.Resolution resolution;
        String formatStr;
        boolean dateOnly = false;
        if (property != null) {
            TemporalType tt = (TemporalType) property.getAnnotations().get("temporal");
            dateOnly = (tt == TemporalType.DATE);
        } else if (javaClass.equals(java.sql.Date.class)) {
            dateOnly = true;
        }
        if (dateOnly) {
            resolution = DateField.Resolution.DAY;
            formatStr = messages.getMessage(AppConfig.getMessagesPack(), "dateFormat");
        } else {
            resolution = DateField.Resolution.MIN;
            formatStr = messages.getMessage(AppConfig.getMessagesPack(), "dateTimeFormat");
        }
        field.setResolution(resolution);
        field.setDateFormat(formatStr);

        field.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                setValue(field.getValue());
            }
        });

        field.setValue(value);
        return new FieldParamEditorComponent(field);
    }

    protected ParamEditorComponent createNumberField(final Datatype datatype) {
        final DesktopTextField field = new DesktopTextField();

        field.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {

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
                                p = datatype.parse(part, userSessionSource.getLocale());
                            } catch (ParseException e) {
                                DesktopComponentsHelper.getTopLevelFrame(field).getWindowManager()
                                        .showNotification(messages.getMessage(AbstractParam.class,
                                                "Param.numberInvalid"), IFrame.NotificationType.ERROR);
                                return;
                            }
                            ((java.util.List) v).add(p);
                        }
                    } else {
                        try {
                            v = datatype.parse((String) value, userSessionSource.getLocale());
                        } catch (ParseException e) {
                            DesktopComponentsHelper.getTopLevelFrame(field).getWindowManager()
                                    .showNotification(messages.getMessage(AbstractParam.class,
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

        field.setValue(value == null ? "" : datatype.format(value, userSessionSource.getLocale()));

        return new FieldParamEditorComponent(field);
    }

    protected ParamEditorComponent createBooleanField() {
        final JComboBox field = new ExtendedComboBox();

        field.addItem("");
        field.addItem(Boolean.TRUE);
        field.addItem(Boolean.FALSE);

        field.setSelectedItem(value);
        return new ParamEditorComponent(field) {

            protected boolean required;

            {
                field.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        setValue(field.getSelectedItem());
                        DesktopComponentsHelper.decorateMissingValue(field, required);
                    }
                });
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public void setRequired(boolean required) {
                this.required = required;

                DesktopComponentsHelper.decorateMissingValue(field, required);
            }
        };
    }

    protected ParamEditorComponent createUuidField() {
        final DesktopTextField field = new DesktopTextField();

        field.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (value == null || value instanceof UUID)
                    setValue(value);
                else if (value instanceof String && !StringUtils.isBlank((String) value))
                    if (inExpr) {
                        java.util.List list = new ArrayList();
                        String[] parts = ((String) value).split(",");
                        for (String part : parts) {
                            list.add(UUID.fromString(part.trim()));
                        }
                        setValue(list);
                    } else {
                        try {
                            setValue(UUID.fromString((String) value));
                        } catch (IllegalArgumentException ie) {
                            DesktopComponentsHelper.getTopLevelFrame(field).getWindowManager().showNotification
                                    (messages.getMessage(AbstractParam.class, "Param.uuid.Err"),
                                            IFrame.NotificationType.HUMANIZED);
                        }
                    }
                else if (value instanceof String && StringUtils.isBlank((String) value))
                    setValue(null);
                else
                    throw new IllegalStateException("Invalid value: " + value);
            }
        });

        field.setValue(value == null ? "" : value.toString());

        return new FieldParamEditorComponent(field);
    }

    protected ParamEditorComponent createEntityLookup() {
        MetaClass metaClass = AppBeans.get(Metadata.class).getSession().getClass(javaClass);

        PersistenceManagerService persistenceManager = AppBeans.get(PersistenceManagerService.NAME);
        boolean useLookupScreen = persistenceManager.useLookupScreen(metaClass.getName());

        if (useLookupScreen) {
            if (inExpr) {
                final ListEditComponent component = new ListEditComponent(metaClass);
                initListEdit(component);
                return new NonRequiredParamEditorComponent(component);
            } else {
                DesktopPickerField picker = new DesktopPickerField();
                picker.setMetaClass(metaClass);
                picker.setFrame(datasource.getDsContext().getFrameContext().getFrame());
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

                return new FieldParamEditorComponent(picker);
            }
        } else {
            CollectionDatasource ds = new DsBuilder(datasource.getDsContext())
                    .setMetaClass(metaClass)
                    .setViewName(entityView)
                    .buildCollectionDatasource();

            ds.setRefreshOnComponentValueChange(true);
            ((DatasourceImplementation) ds).initialized();

            if (WindowParams.DISABLE_AUTO_REFRESH.getBool(datasource.getDsContext().getFrameContext())) {
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
                return new NonRequiredParamEditorComponent(component);
            } else {
                final DesktopLookupField lookup = new DesktopLookupField();
                lookup.setOptionsDatasource(ds);

                ds.addListener(
                        new CollectionDsListenerAdapter<Entity>() {
                            @Override
                            public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                                Entity currentValue = lookup.getValue();
                                if (currentValue == null)
                                    return;
                                // If the selected entity not in options list, reset it
                                for (Object itemId : ds.getItemIds()) {
                                    if (itemId.equals(currentValue.getId()))
                                        return;
                                }
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
                return new FieldParamEditorComponent(lookup);
            }
        }
    }

    protected void initListEdit(final ListEditComponent component) {
        component.addListener(
                new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        setValue(value);
                    }
                }
        );
        if (value != null) {
            Map<Object, String> values = new HashMap<>();
            for (Object v : (java.util.List) value) {
                values.put(v, getValueCaption(v));
            }
            component.setValues(values);
        }
    }

    protected ParamEditorComponent createRuntimeEnumLookup() {
        DataService dataService = AppBeans.get(DataService.NAME);
        LoadContext context = new LoadContext(CategoryAttribute.class);
        LoadContext.Query q = context.setQueryString("select a from sys$CategoryAttribute a where a.id = :id");
        context.setView("_local");
        q.setParameter("id", categoryAttrId);
        CategoryAttribute categoryAttribute = dataService.load(context);

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
            final ListEditComponent component = new ListEditComponent(runtimeEnum);
            initListEdit(component);
            return new NonRequiredParamEditorComponent(component);
        } else {
            DesktopLookupField lookup = new DesktopLookupField();
            lookup.setOptionsList(runtimeEnum);

            lookup.addListener(new ValueListener() {
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    setValue(value);
                }
            });

            lookup.setValue(value);
            lookup.setRequired(required);

            return new FieldParamEditorComponent(lookup);
        }
    }

    protected ParamEditorComponent createEnumLookup() {
        if (inExpr) {
            final ListEditComponent component = new ListEditComponent(javaClass);
            initListEdit(component);
            return new NonRequiredParamEditorComponent(component);
        } else {
            Map<String, Object> options = new HashMap<>();
            for (Object obj : javaClass.getEnumConstants()) {
                options.put(messages.getMessage((Enum) obj), obj);
            }

            DesktopLookupField lookup = new DesktopLookupField();
            lookup.setOptionsMap(options);
            lookup.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    setValue(value);
                }
            });

            lookup.setValue(value);

            return new FieldParamEditorComponent(lookup);
        }
    }

    protected static class NonRequiredParamEditorComponent extends ParamEditorComponent {

        protected NonRequiredParamEditorComponent(JComponent component) {
            super(component);
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public void setRequired(boolean required) {
        }
    }

    protected static class FieldParamEditorComponent extends ParamEditorComponent {

        protected Field field;

        public FieldParamEditorComponent(Field field) {
            super(DesktopComponentsHelper.getComposition(field));
            this.field = field;
        }

        @Override
        public boolean isRequired() {
            return field.isRequired();
        }

        @Override
        public void setRequired(boolean required) {
            field.setRequired(required);
        }
    }
}