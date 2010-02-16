/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 15.02.2010 16:18:50
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.validators.DoubleValidator;
import com.haulmont.cuba.gui.components.validators.IntegerValidator;
import com.haulmont.cuba.gui.components.validators.DateValidator;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.security.entity.AttributeEntity;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.util.Date;

public class WebRuntimePropertyGridLayout extends WebGridLayout implements RuntimePropertyGridLayout {

    private CollectionDatasource attributesDs;

    private CollectionDatasource valuesDs;

    private String attributeProperty;

    private DateField.Resolution resolution;

    private String innerComponentWidth;

    public CollectionDatasource getAttributesDs() {
        return attributesDs;
    }

    public void setAttributesDs(CollectionDatasource ds) {
        this.attributesDs = ds;
        this.attributesDs.addListener(new CollectionDatasourceListener<Entity>() {
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                executeLazyTask();
            }

            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
            }

            public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
                executeLazyTask();
            }

            public void valueChanged(Entity source, String property, Object prevValue, Object value) {

            }
        });
    }

    public String getAttributeProperty() {
        return attributeProperty;
    }

    public void setAttributeProperty(String value) {
        this.attributeProperty = value;
    }

    public DateField.Resolution getResolution() {
        return resolution;
    }

    public void setResolution(DateField.Resolution resolution) {
        this.resolution = resolution;
    }

    public String getInnerComponentWidth() {
        return innerComponentWidth;
    }

    public void setInnerComponentWidth(String innerComponentWidth) {
        this.innerComponentWidth = innerComponentWidth;
    }

    public void checkAttributesType() {
        MetaClass metaClass = attributesDs.getMetaClass();
        MetaProperty property = metaClass.getProperty(attributeProperty);
        if (property == null) {
            throw new IllegalArgumentException("there is no property " + attributeProperty + " in metaclass " + metaClass.getName());
        }
        if (!property.getRange().isClass()) {
            throw new IllegalArgumentException("property " + attributeProperty + " in metaclass " + metaClass.getName() + " is not an entity");
        }
        if (!ClassUtils.isAssignable(property.getRange().asClass().getJavaClass(), AttributeEntity.class)) {
            throw new IllegalArgumentException("property " + attributeProperty + " isn't assignable to " + AttributeEntity.class.getName());
        }
    }

    protected Component defineComponent(Entity entity) {
        final Instance instance = ((Instance) entity);
        String val = instance.getValue("value");
        AttributeEntity value = instance.getValue(attributeProperty);
        if (value == null) {
            throw new RuntimeException("attribute entity can not be null");
        }
        try {
            Field field;
            switch (value.getAttributeType()) {
                case BOOLEAN: {
                    field = new WebCheckBox();
                    field.setValue(Datatypes.getInstance().get(Boolean.class).parse(val));
                    break;
                }
                case DATE: {
                    field = new WebDateField();
                    if (resolution != null) {
                        ((DateField) field).setResolution(resolution);
                    }
                    field.addValidator(new DateValidator());
                    field.setValue(Datatypes.getInstance().get(Date.class).parse(val));
                    break;
                }
                case DOUBLE: {
                    field = new WebTextField();
                    field.addValidator(new DoubleValidator());
                    field.setValue(Datatypes.getInstance().get(Double.class).parse(val));
                    break;
                }
                case STRING: {
                    field = new WebTextField();
                    field.setValue(val);
                    break;
                }
                case INTEGER: {
                    field = new WebTextField();
                    field.addValidator(new IntegerValidator());
                    field.setValue(Datatypes.getInstance().get(Integer.class).parse(val));
                    break;
                }
                default:
                    throw new IllegalArgumentException("no variants for " + value.getAttributeType().getId());
            }

            if (!StringUtils.isEmpty(innerComponentWidth)) {
                field.setWidth(innerComponentWidth);
            }
            
            field.addListener(new ValueListener() {
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    instance.setValue("value", value);
                }
            });
            return field;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected Component createCaption(Entity entity) {
        Label label = new WebLabel();
        Instance instance = (Instance) entity;
        AttributeEntity value = instance.getValue(attributeProperty);
        if (value == null) {
            throw new RuntimeException("attribute entity can not be null");
        }
        if (!StringUtils.isEmpty(value.getDisplayName())) {
            label.setValue(value.getDisplayName());
        } else {
            label.setValue(value.getName());
        }
        return label;
    }

    public void executeLazyTask() {
        if (attributesDs != null) {
            removeAllComponents();
            checkAttributesType();

            int row = 0;
            int size = attributesDs.size();
            if (size > 0) {
                setRows(size / (getColumns() / 2));
            }
            for (Object o : attributesDs.getItemIds()) {
                Entity entity = attributesDs.getItem(o);
                add(createCaption(entity), 0, row);
                add(defineComponent(entity), 1, row);
                row++;
            }
        }
    }
}
