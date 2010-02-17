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
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.validators.DateValidator;
import com.haulmont.cuba.gui.components.validators.DoubleValidator;
import com.haulmont.cuba.gui.components.validators.IntegerValidator;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.security.entity.AttributeEntity;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WebRuntimePropertyGridLayout extends WebGridLayout implements RuntimePropertyGridLayout {

    private Datasource mainDs;

    private String attributeProperty;

    private String attributeValueProperty;

    private String typeProperty;

    private DateField.Resolution resolution;

    private String innerComponentWidth;

    private String dateFormat;

    private Boolean checkNewAttributes;

    public void executeLazyTask() {
        if (isAddNewProperties()) {
            removeAllComponents();
            checkAttributesType();

            MetaClass metaClass = defineMetaClass(attributeValueProperty);
            CollectionDatasource cds = new CollectionDatasourceImpl(mainDs.getDsContext(), mainDs.getDataService(),
                    "valuesDs", defineMetaClass(attributeValueProperty), createAttributesView());
            cds.setQuery("select e from " + metaClass.getName() + " e where e." + getInversePropertyName(metaClass)
                    + ".id = :custom$id");
            cds.refresh(Collections.singletonMap("id", mainDs.getItem().getId()));
            ((DsContextImplementation) mainDs.getDsContext()).register(cds);

            List<Entity> attributes = loadAttributes(typeProperty, attributeProperty);
            List<Entity> addedAttributes = getAddedAttributes(attributes, cds);

            String entityPropertyName = getInversePropertyName(metaClass);
            String attributePropertyName = getInversePropertyName(metaClass, defineMetaClass(typeProperty, attributeProperty));
            for (Entity entity : addedAttributes) {
                try {
                    Entity en = (Entity) metaClass.getJavaClass().newInstance();
                    Instance ins = (Instance) en;
                    ins.setValue(entityPropertyName, mainDs.getItem());
                    ins.setValue(attributePropertyName, entity);
                    cds.addItem(en);
                } catch (Exception e) {
                    //TODO Log here
                }
            }


            int row = 0;
            int size = cds.size();
            if (size > 0) {
                setRows(size / (getColumns() / 2));
            }
            for (Object o : cds.getItemIds()) {
                Entity entity = cds.getItem(o);
                add(createCaption(entity), 0, row);
                add(defineComponent(entity), 1, row);
                row++;
            }
        }
    }

    protected List<Entity> getAddedAttributes(List<Entity> attributes, CollectionDatasource values) {
        MetaClass valueMetaClass = defineMetaClass(attributeValueProperty);
        MetaClass attributeMetaClass = defineMetaClass(typeProperty, attributeProperty);
        String inversePropertyName = getInversePropertyName(valueMetaClass, attributeMetaClass);
        List<Entity> result = new ArrayList<Entity>(attributes);
        List<Entity> valuesList = new ArrayList<Entity>();
        for (Object id : values.getItemIds()) {
            Instance ins = (Instance) values.getItem(id);
            Entity value = ins.getValue(inversePropertyName);
            if (value != null) {
                valuesList.add(value);
            }
        }
        result.removeAll(valuesList);
        return result;
    }

    protected void checkAttributesType() {
        MetaClass metaClass = defineMetaClass(typeProperty, attributeProperty);
        if (metaClass == null) {
            throw new IllegalArgumentException("there is no property " + attributeProperty + " in metaclass " + metaClass.getName());
        }
        if (!ClassUtils.isAssignable(metaClass.getJavaClass(), AttributeEntity.class)) {
            throw new IllegalArgumentException("property " + attributeProperty + " isn't assignable to " + AttributeEntity.class.getName());
        }
    }

    protected MetaClass defineMetaClass(String... properties) {
        MetaClass mainClass = mainDs.getMetaClass();
        if (properties.length == 0) {
            return null;
        } else if (properties.length == 1) {
            return mainClass.getProperty(properties[0]).getRange().asClass();
        } else {
            MetaProperty property = null;
            for (int i = 0; i < properties.length; i++) {
                property = mainClass.getProperty(properties[i]);
                if (property == null) {
                    throw new RuntimeException("property " + properties[i] + " is not exist in class " + mainClass.getName());
                } else if (property.getRange().isClass() || property.getRange().isOrdered()) {
                    mainClass = property.getRange().asClass();
                }
            }
            return property.getRange().asClass();
        }
    }

    protected String createPath(String... properties) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < properties.length; i++) {
            sb.append(properties[i]);
            sb.append(".");
        }
        String propertyPath = sb.toString();
        return propertyPath.substring(0, propertyPath.length() - 1);
    }

    protected View createAttributesView() {
        MetaClass valueMetaClass = defineMetaClass(attributeValueProperty);
        MetaClass attributeMetaClass = defineMetaClass(typeProperty, attributeProperty);
        String inversePropertyName = getInversePropertyName(valueMetaClass, attributeMetaClass);
        return new View(MetadataProvider.getViewRepository().getView(valueMetaClass.getJavaClass(), View.LOCAL), "attributesView")
                .addProperty(inversePropertyName, MetadataProvider.getViewRepository().getView(attributeMetaClass.getJavaClass(), View.LOCAL));
    }

    protected List<Entity> loadAttributes(String... path) {
        DataService service = ServiceLocator.getDataService();
        MetaClass attributesMetaClass = defineMetaClass(path);
        MetaClass propertyTypeMetaclass = defineMetaClass(typeProperty);

        LoadContext lc = new LoadContext(attributesMetaClass);
//        String propertyPath = createPath(path);
        String typeName = getInversePropertyName(attributesMetaClass, propertyTypeMetaclass);
        Instance ins = (Instance) mainDs.getItem();
        Entity type = ins.getValue(typeProperty);
        if (type == null) {
            return Collections.EMPTY_LIST;
        }
        lc.setQueryString("select e from " + attributesMetaClass.getName() + " e where e." + typeName + ".id = :id");
        lc.getQuery().addParameter("id", type.getId());
        return service.loadList(lc);
    }

    protected Boolean isAddNewProperties() {
        return BooleanUtils.isTrue(checkNewAttributes) || PersistenceHelper.isNew(mainDs.getItem());
    }

    protected String getInversePropertyName(MetaClass metaClass) {
        return getInversePropertyName(metaClass, mainDs.getMetaClass());
    }

    protected String getInversePropertyName(MetaClass metaClass, MetaClass parentMetaClass) {
        for (MetaProperty mp : metaClass.getProperties()) {
            if ((mp.getRange().isClass() || mp.getRange().isOrdered()) && mp.getRange().asClass().equals(parentMetaClass)) {
                return mp.getName();
            }
        }
        throw new RuntimeException("there is no property with type " + mainDs.getMetaClass().getName()
                + " in class " + metaClass.getName());
    }

    protected Component defineComponent(Entity entity) {
        final Instance instance = ((Instance) entity);
        String val = instance.getValue("value");
        MetaClass valueMetaClass = defineMetaClass(attributeValueProperty);
        MetaClass attributeMetaClass = defineMetaClass(typeProperty, attributeProperty);
        AttributeEntity value = instance.getValue(getInversePropertyName(valueMetaClass, attributeMetaClass));
        if (value == null) {
            throw new RuntimeException("attribute entity can not be null");
        }
        try {
            Field field;
            final Class fieldClass;
            switch (value.getAttributeType()) {
                case BOOLEAN: {
                    field = new WebCheckBox();
                    field.setValue(Datatypes.getInstance().get(Boolean.class).parse(val));
                    fieldClass = Boolean.class;
                    break;
                }
                case DATE: {
                    field = new WebDateField();
                    if (resolution != null) {
                        ((DateField) field).setResolution(resolution);
                    }
                    if (!StringUtils.isEmpty(dateFormat)) {
                        ((DateField) field).setDateFormat(dateFormat);
                    }
                    field.addValidator(new DateValidator());
                    field.setValue(Datatypes.getInstance().get(Date.class).parse(val));
                    fieldClass = Date.class;
                    break;
                }
                case DOUBLE: {
                    field = new WebTextField();
                    field.addValidator(new DoubleValidator());
                    field.setValue(Datatypes.getInstance().get(Double.class).parse(val));
                    fieldClass = Double.class;
                    break;
                }
                case STRING: {
                    field = new WebTextField();
                    field.setValue(val);
                    fieldClass = String.class;
                    break;
                }
                case INTEGER: {
                    field = new WebTextField();
                    field.addValidator(new IntegerValidator());
                    field.setValue(Datatypes.getInstance().get(Integer.class).parse(val));
                    fieldClass = Integer.class;
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
        MetaClass valueMetaClass = defineMetaClass(attributeValueProperty);
        MetaClass attributeMetaClass = defineMetaClass(typeProperty, attributeProperty);
        AttributeEntity value = instance.getValue(getInversePropertyName(valueMetaClass, attributeMetaClass));
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

    public Datasource getMainDs() {
        return mainDs;
    }

    public void setMainDs(Datasource ds) {
        this.mainDs = ds;
        this.mainDs.addListener(new DatasourceListener<Entity>() {

            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
            }

            public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
                if (Datasource.State.VALID.equals(state)) {
                    executeLazyTask();
                }
            }

            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
            }
        });

    }

    public Boolean getCheckNewAttributes() {
        return checkNewAttributes;
    }

    public void setCheckNewAttributes(Boolean checkNewAttributes) {
        this.checkNewAttributes = checkNewAttributes;
    }

    public String getAttributeValueProperty() {
        return attributeValueProperty;
    }

    public void setAttributeValueProperty(String attributeValueProperty) {
        this.attributeValueProperty = attributeValueProperty;
    }

    public String getTypeProperty() {
        return typeProperty;
    }

    public void setTypeProperty(String typeProperty) {
        this.typeProperty = typeProperty;
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

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }


}
