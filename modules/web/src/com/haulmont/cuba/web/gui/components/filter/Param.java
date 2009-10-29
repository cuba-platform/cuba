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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.GenericDataService;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.web.gui.components.WebLookupField;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.vaadin.ui.*;
import com.vaadin.data.Property;
import com.vaadin.terminal.UserError;

import java.text.ParseException;
import java.util.UUID;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.BooleanUtils;

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

    public Param(String name, Class javaClass) {
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
        this.value = value;
    }

    public void parseValue(String text) {
        if (NULL.equals(text)) {
            value = null;
            return;
        }

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

        switch (type) {
            case ENTITY:
                if (value instanceof UUID)
                    return value.toString();
                else if (value instanceof Entity)
                    return ((Entity) value).getId().toString();

            case ENUM:
                return ((Enum) value).name();

            case DATATYPE:
            case UNARY:
                Datatype datatype = Datatypes.getInstance().get(javaClass);
                if (datatype == null)
                    throw new UnsupportedOperationException("Unsupported parameter class: " + javaClass);

                return datatype.format(value);

            default:
                throw new IllegalStateException("Param type unknown");
        }
    }

    public Component createEditComponent() {
        final AbstractField field;

        switch (type) {
            case DATATYPE:
                field = createDatatypeField(Datatypes.getInstance().get(javaClass));
                break;
            case ENTITY:
                field = createEntityLookup();
                break;
            case UNARY:
                field = createUnaryField();
                break;
            case ENUM:
                field = createEnumLookup();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported param type: " + type);
        }

        return field;
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

    private AbstractField createDatatypeField(Datatype datatype) {
        if (datatype == null)
            throw new IllegalStateException("Unable to find Datatype for " + javaClass);

        final AbstractField field;

        if (String.class.equals(javaClass)) {
            field = createTextField();
        } else if (Date.class.equals(javaClass)) {
            field = createDateField(datatype);
        } else if (Number.class.isAssignableFrom(javaClass)) {
            field = createNumberField(datatype);
        } else if (Boolean.class.isAssignableFrom(javaClass)) {
            field = createBooleanField();
        } else if (UUID.class.equals(javaClass)) {
            field = createUuidField();
        } else
            throw new UnsupportedOperationException("Unsupported param class: " + javaClass);

        return field;
    }

    private AbstractField createTextField() {
        final AbstractField field = new TextField();
        ((TextField) field).setNullRepresentation("");

        field.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                setValue(field.getValue());
            }
        });

        field.setValue(value);
        return field;
    }

    private AbstractField createDateField(Datatype datatype) {
        final AbstractField field = new DateField() {
            @Override
            protected Date handleUnparsableDateString(String dateString) throws ConversionException {
                setComponentError(new UserError(MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "validation.invalidDate")));
                return null;
            }
        };
        field.setImmediate(true);
        ((DateField) field).setResolution(DateField.RESOLUTION_MIN);

        if (((DateDatatype) datatype).getFormatPattern() != null)
            ((DateField) field).setDateFormat(((DateDatatype) datatype).getFormatPattern());

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
                    try {
                        v = datatype.parse((String) value);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    setValue(v);
                } else
                    throw new IllegalStateException("Invalid value: " + value);
            }
        });

        field.setValue(value);
        return field;
    }

    private AbstractField createBooleanField() {
        final AbstractSelect field = new NativeSelect();
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
                    setValue(UUID.fromString((String) value));
                else
                    throw new IllegalStateException("Invalid value: " + value);
            }
        });

        field.setValue(value);
        return field;
    }

    private AbstractField createEntityLookup() {
        MetaClass metaClass = MetadataProvider.getSession().getClass(javaClass);
        CollectionDatasourceImpl ds =
                new CollectionDatasourceImpl(null, new GenericDataService(false), "ds", metaClass, null);

        WebLookupField lookup = new WebLookupField();
        lookup.setOptionsDatasource(ds);
        ds.initialized();

        lookup.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                setValue(value);
            }
        });

        lookup.setValue(value);

        return lookup.getComponent();
    }

    private AbstractField createEnumLookup() {
        List<Object> list = Arrays.asList(javaClass.getEnumConstants());

        WebLookupField lookup = new WebLookupField();
        lookup.setOptionsList(list);

        lookup.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                setValue(value);
            }
        });

        lookup.setValue(value);

        return lookup.getComponent();
    }
}
