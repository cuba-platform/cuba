/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopDateField;
import com.haulmont.cuba.desktop.gui.components.DesktopLookupField;
import com.haulmont.cuba.desktop.gui.components.DesktopPickerField;
import com.haulmont.cuba.desktop.gui.components.DesktopTextField;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.DateField;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class Param extends AbstractParam<JComponent> {
    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource, boolean inExpr) {
        super(name, javaClass, entityWhere, entityView, datasource, inExpr);
    }

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource, boolean inExpr, UUID categoryAttrId) {
        super(name, javaClass, entityWhere, entityView, datasource, inExpr, categoryAttrId);
    }

    public Param(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource, MetaProperty property, boolean inExpr) {
        super(name, javaClass, entityWhere, entityView, datasource, property, inExpr);
    }

    public JComponent createEditComponent() {
        JComponent component;

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
            case RUNTIME_ENUM:
                component = createRuntimeEnumLookup();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported param type: " + type);
        }

        return component;
    }

    private JComponent createUnaryField() {
        final JCheckBox field = new JCheckBox();

        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean v = field.isSelected();
                setValue(Boolean.TRUE.equals(v) ? true : null);
            }
        });

        field.setSelected(BooleanUtils.isTrue((Boolean) value));
        return field;
    }

    private JComponent createDatatypeField(Datatype datatype) {
        if (datatype == null)
            throw new IllegalStateException("Unable to find Datatype for " + javaClass);

        JComponent component;

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

    private JComponent createTextField() {
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
                setValue(StringUtils.trimToNull((String) value));
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
        return field.getComponent();
    }

    private JComponent createDateField(Class javaClass) {
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
            formatStr = MessageProvider.getMessage(AppConfig.getMessagesPack(), "dateFormat");
        } else {
            resolution = DateField.Resolution.MIN;
            formatStr = MessageProvider.getMessage(AppConfig.getMessagesPack(), "dateTimeFormat");
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
        return field.getComposition();
    }

    private JComponent createNumberField(final Datatype datatype) {
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
                                p = datatype.parse(part);
                            } catch (ParseException e) {
                                App.getInstance().getWindowManager().showNotification(MessageProvider.getMessage(Param.class,
                                        "Param.numberInvalid"), IFrame.NotificationType.ERROR);
                                return;
                            }
                            ((java.util.List) v).add(p);
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

        field.setValue(value == null ? "" : datatype.format(value));
        return field.getComposition();
    }

    private JComponent createBooleanField() {
        final JComboBox field = new JComboBox();

        field.addItem("");
        field.addItem(Boolean.TRUE);
        field.addItem(Boolean.FALSE);
        field.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setValue(field.getSelectedItem());
            }
        });

        field.setSelectedItem(value);
        return field;
    }

    private JComponent createUuidField() {
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
                            App.getInstance().getWindowManager().showNotification
                                    (MessageProvider.getMessage(this.getClass(), "Param.uuid.Err"), IFrame.NotificationType.HUMANIZED);
                        }
                    }
                else if (value instanceof String && StringUtils.isBlank((String) value))
                    setValue(null);
                else
                    throw new IllegalStateException("Invalid value: " + value);
            }
        });

        field.setValue(value == null ? "" : value.toString());
        return field.getComposition();
    }

    private JComponent createEntityLookup() {
        MetaClass metaClass = MetadataProvider.getSession().getClass(javaClass);

        PersistenceManagerService persistenceManager = ServiceLocator.lookup(PersistenceManagerService.NAME);
        boolean useLookupScreen = persistenceManager.useLookupScreen(metaClass.getName());

        if (useLookupScreen) {
            if (inExpr) {
                final ListEditComponent component = new ListEditComponent(metaClass);
                initListEdit(component);
                return component;
            } else {
                DesktopPickerField picker = new DesktopPickerField();
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
                final DesktopLookupField lookup = new DesktopLookupField();
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
                new ValueListener() {

                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        setValue(value);
                    }
                }
        );
        if (value != null) {
            Map<Object, String> values = new HashMap<Object, String>();
            for (Object v : (java.util.List) value) {
                values.put(v, getValueCaption(v));
            }
            component.setValues(values);
        }
    }

    private JComponent createRuntimeEnumLookup() {
        DataService dataService = ServiceLocator.lookup(DataService.NAME);
        LoadContext context = new LoadContext(CategoryAttribute.class);
        LoadContext.Query q = context.setQueryString("select a from sys$CategoryAttribute a where a.id = :id");
        context.setView("_local");
        q.addParameter("id", categoryAttrId);
        CategoryAttribute categoryAttribute = dataService.load(context);

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
            DesktopLookupField lookup = new DesktopLookupField();
            lookup.setOptionsList(runtimeEnum);

            lookup.addListener(new ValueListener() {
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    setValue(value);
                }
            });

            lookup.setValue(value);
            return lookup.getComponent();
        }
    }

    private JComponent createEnumLookup() {
        if (inExpr) {
            final ListEditComponent component = new ListEditComponent(javaClass);
            initListEdit(component);
            return component;
        } else {
            Map<String, Object> options = new HashMap<String, Object>();
            for (Object obj : javaClass.getEnumConstants()) {
                options.put(MessageProvider.getMessage((Enum) obj), obj);
            }

            DesktopLookupField lookup = new DesktopLookupField();
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
}
