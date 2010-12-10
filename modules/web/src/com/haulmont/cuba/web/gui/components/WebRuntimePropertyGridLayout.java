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
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.validators.DateValidator;
import com.haulmont.cuba.gui.components.validators.DoubleValidator;
import com.haulmont.cuba.gui.components.validators.IntegerValidator;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.security.entity.AttributeEntity;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.util.*;
import java.util.List;

public class WebRuntimePropertyGridLayout extends WebGridLayout implements RuntimePropertyGridLayout {

    private static final long serialVersionUID = -4806876038643471003L;

    private static Log log = LogFactory.getLog(WebRuntimePropertyGridLayout.class);

    private Datasource mainDs;

    private String attributePropertyOrder;

    private String typeProperty;

    private String innerComponentWidth;

    private String dateFormat;

    private Boolean checkNewAttributes = true;

    private MetaClass attributeMetaClass;

    private MetaClass attributeValueMetaClass;

    private MetaClass mainEntityTypeMetaClass;

    private String inverseAttributePropertyInValue;

    private String inverseMainEntityPropertyInValue;

    private String[] attributeValuePropertyArr;
    private String[] attributePropertyArr;
    private String[] typePropertyArr;

    public void setMainDs(Datasource ds) {
        this.mainDs = ds;

        this.attributeMetaClass = defineMetaClass((String[])ArrayUtils.addAll(attributeValuePropertyArr, attributePropertyArr));
        this.attributeValueMetaClass = defineMetaClass(attributeValuePropertyArr);

        if (typePropertyArr != null)
            this.mainEntityTypeMetaClass = defineMetaClass(typePropertyArr);

        this.inverseAttributePropertyInValue = getInversePropertyName(attributeValueMetaClass, attributeMetaClass);
        this.inverseMainEntityPropertyInValue = getInversePropertyName(attributeValueMetaClass);

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

        if (Datasource.State.VALID.equals(this.mainDs.getState())) {
            executeLazyTask();
        }

    }

    protected CollectionDatasource createDatasource() {
        CollectionDatasource cds = new CollectionDatasourceImpl(mainDs.getDsContext(), mainDs.getDataService(),
                "valuesDs", defineMetaClass(attributeValuePropertyArr), createAttributesView());
        cds.setQuery("select e from " + attributeValueMetaClass.getName() + " e where e." + inverseMainEntityPropertyInValue
                + ".id = :custom$id order by e." + inverseAttributePropertyInValue + ".name");
        cds.refresh(Collections.singletonMap("id", mainDs.getItem().getId()));
        ((DsContextImplementation) mainDs.getDsContext()).register(cds);
        return cds;
    }

    protected void addNewElements(CollectionDatasource cds) {
        List<Entity> attributes = loadAttributes();
        List<Entity> addedAttributes = getAddedAttributes(attributes, cds);

        for (Entity entity : addedAttributes) {
            try {
                Entity en = (Entity) attributeValueMetaClass.getJavaClass().newInstance();
                Instance ins = (Instance) en;
                ins.setValue(inverseMainEntityPropertyInValue, mainDs.getItem());
                ins.setValue(inverseAttributePropertyInValue, entity);
                cds.addItem(en);
            } catch (Exception e) {
                log.error("error instantiating class " + attributeValueMetaClass);
            }
        }
    }


    public void executeLazyTask() {
        if (isAddNewProperties()) {
            removeAllComponents();
            checkAttributesType();

            CollectionDatasource cds = createDatasource();
            addNewElements(cds);

            int size = cds.size();
            if (size > 0) {
                setRows((int) Math.round((double) size / (getColumns() / 2)));
            } else {
                setVisible(false);
                return;
            }

            int row = 0;
            for (Iterator it = cds.getItemIds().iterator(); it.hasNext();) {
                Entity entity = null;
                for (int i = 0; i < getColumns(); i++) {
                    if (i % 2 == 0) {
                        if (!it.hasNext()) {
                            return;
                        }
                        entity = cds.getItem(it.next());
                        add(createCaption(entity), i, row);
                    } else {
                        add(defineComponent(entity), i, row);
                    }
                }
                row++;
            }
        }
    }

    protected List<Entity> getAddedAttributes(List<Entity> attributes, CollectionDatasource values) {
        List<Entity> result = new ArrayList<Entity>(attributes);
        List<Entity> valuesList = new ArrayList<Entity>();
        for (Object id : values.getItemIds()) {
            Instance ins = (Instance) values.getItem(id);
            Entity value = ins.getValue(inverseAttributePropertyInValue);
            if (value != null) {
                valuesList.add(value);
            }
        }
        result.removeAll(valuesList);
        return result;
    }

    protected void checkAttributesType() {
        if (attributeMetaClass == null) {
            throw new IllegalArgumentException("there is no property " + arrToString(attributePropertyArr, ".") + " in metaclass " + attributeMetaClass.getName());
        }
        if (!ClassUtils.isAssignable(attributeMetaClass.getJavaClass(), AttributeEntity.class)) {
            throw new IllegalArgumentException("property " + arrToString(attributePropertyArr, ".") + " isn't assignable to " + AttributeEntity.class.getName());
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

    protected View createAttributesView() {
        String inversePropertyName = getInversePropertyName(attributeValueMetaClass, attributeMetaClass);
        return new View(MetadataProvider.getViewRepository().getView(attributeValueMetaClass.getJavaClass(), View.LOCAL), "attributesView")
                .addProperty(inversePropertyName, MetadataProvider.getViewRepository().getView(attributeMetaClass.getJavaClass(), View.LOCAL));
    }

    protected List<Entity> loadAttributes() {
        if (mainEntityTypeMetaClass == null)
            return loadAttributesAll();
        else
            return loadAttributesByType();
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
        AttributeEntity value = instance.getValue(inverseAttributePropertyInValue);
        if (value == null) {
            throw new RuntimeException("attribute entity can not be null");
        }

        String val;
        if (PersistenceHelper.isNew(mainDs.getItem())) {
            val = value.getDefaultValue();
        } else {
            val = instance.getValue("value");
        }
        try {
            Field field;
            switch (value.getAttributeType()) {
                case BOOLEAN: {
                    field = new WebCheckBox();
                    setListenerToField(field, instance, Boolean.class);
                    field.setValue(Datatypes.getInstance().get(Boolean.class).parse(val));
                    break;
                }
                case DATE: {
                    field = new WebDateField();
                    setListenerToField(field, instance, Date.class);
                    ((DateField) field).setResolution(DateField.Resolution.DAY);
                    if (!StringUtils.isEmpty(dateFormat)) {
                        ((DateField) field).setDateFormat(dateFormat);
                    }
                    field.addValidator(new DateValidator());
                    field.setValue(Datatypes.getInstance().get(Date.class).parse(val));
                    break;
                }
                case DATE_TIME: {
                    field = new WebDateField();
                    setListenerToField(field, instance, Date.class);
                    ((DateField) field).setResolution(DateField.Resolution.MIN);
                    if (!StringUtils.isEmpty(dateFormat)) {
                        ((DateField) field).setDateFormat(dateFormat);
                    }
                    field.addValidator(new DateValidator());
                    field.setValue(Datatypes.getInstance().get(Date.class).parse(val));
                    break;
                }
                case DOUBLE: {
                    field = new WebTextField();
                    setListenerToField(field, instance, Double.class);
                    field.addValidator(new DoubleValidator());
                    field.setValue(Datatypes.getInstance().get(Double.class).parse(val));
                    break;
                }
                case STRING: {
                    field = new WebTextField();
                    setListenerToField(field, instance, String.class);
                    field.setValue(val);
                    break;
                }
                case INTEGER: {
                    field = new WebTextField();
                    setListenerToField(field, instance, Integer.class);
                    field.addValidator(new IntegerValidator());
                    //field.setValue(Datatypes.getInstance().get(Integer.class).parse(val));
                    field.setValue(val);
                    break;
                }
                case DICTIONARY:
                case ENTITY: {
                    field = new WebLookupField();
                    setListenerToField(field, instance, null);
                    ((LookupField) field).setOptionsDatasource(createDatasource(value.getMetaClassName(), value.getWhereCondition()));
                    field.setValue(loadEntity(value.getMetaClassName(), val == null ? null : UUID.fromString(val)));
                    break;
                }
                default:
                    throw new IllegalArgumentException("no variants for " + value.getAttributeType().getId());
            }

            if (!StringUtils.isEmpty(innerComponentWidth)) {
                field.setWidth(innerComponentWidth);
            }
            if (BooleanUtils.isTrue(value.getMandatory())) {
                field.setRequired(true);
            }
            return field;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setListenerToField(Field field, final Instance instance, final Class type) {
        field.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                setValue(instance, value, type);
            }
        });
    }

    protected void setValue(Instance instance, Object value, Class type) {
        if (value != null) {
            if (value instanceof Entity) {
                instance.setValue("value", ((Entity) value).getId().toString());
            } else {
                if (value instanceof String) {
                    instance.setValue("value", value);
                } else {
                    instance.setValue("value", Datatypes.getInstance().get(type).format(value));
                }
            }
        }
    }

    protected Entity loadEntity(String metaclass, UUID id) {
        if (id == null) {
            return null;
        }
        DataService service = ServiceLocator.getDataService();
        LoadContext lc = new LoadContext(MetadataProvider.getSession().getClass(metaclass)).setId(id);
        return service.load(lc);
    }

    protected CollectionDatasource createDatasource(String metaclass, String whereCondition) {
        QueryTransformer transformer = QueryTransformerFactory.createTransformer("select e from " + metaclass + " e", metaclass);
        if (whereCondition != null) {
            transformer.addWhere(whereCondition);
        }
        CollectionDatasource cds = new CollectionDatasourceImpl(mainDs.getDsContext(), mainDs.getDataService(),
                "optionsDs", MetadataProvider.getSession().getClass(metaclass), View.MINIMAL);
        cds.setQuery(transformer.getResult());
        cds.refresh();
        return cds;
    }

    protected Component createCaption(Entity entity) {
        Label label = new WebLabel();
        Instance instance = (Instance) entity;
        AttributeEntity value = instance.getValue(inverseAttributePropertyInValue);
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

    private List<Entity> loadAttributesByType() {
        DataService service = ServiceLocator.getDataService();
        LoadContext lc = new LoadContext(attributeMetaClass);
        String typeName = getInversePropertyName(attributeMetaClass, mainEntityTypeMetaClass);
        Instance ins = (Instance) mainDs.getItem();
        Entity type = ins.getValueEx(typeProperty);
        if (type == null) {
            return Collections.EMPTY_LIST;
        }
        lc.setQueryString("select e from " + attributeMetaClass.getName() + " e where e." + typeName + ".id = :id" +
                (attributePropertyOrder != null ? " order by e." + attributePropertyOrder : ""));
        lc.getQuery().addParameter("id", type.getId());
        return service.loadList(lc);
    }

    private List<Entity> loadAttributesAll() {
        DataService service = ServiceLocator.getDataService();
        LoadContext lc = new LoadContext(attributeMetaClass);
        lc.setQueryString("select e from " + attributeMetaClass.getName() + " e order by e.name");
        return service.loadList(lc);
    }

    public Datasource getMainDs() {
        return mainDs;
    }


    public Boolean getCheckNewAttributes() {
        return checkNewAttributes;
    }

    public void setCheckNewAttributes(Boolean checkNewAttributes) {
        this.checkNewAttributes = checkNewAttributes;
    }

    public String getAttributeValueProperty() {
        return arrToString(attributeValuePropertyArr, ".");
    }

    public void setAttributeValueProperty(String attributeValueProperty) {
        this.attributeValuePropertyArr = attributeValueProperty.split("\\.");
    }

    public String getTypeProperty() {
        return typeProperty;
    }

    public void setTypeProperty(String typeProperty) {
        this.typeProperty = typeProperty;
        typePropertyArr = typeProperty.split("\\.");
    }

    public String getAttributeProperty() {
        return arrToString(attributePropertyArr, ".");
    }

    public void setAttributeProperty(String value) {
        this.attributePropertyArr = value.split("\\.");
    }

    public String getAttributePropertyOrder() {
        return attributePropertyOrder;
    }

    public void setAttributePropertyOrder(String value) {
        this.attributePropertyOrder = value;
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

    private String arrToString(String[] arr, String delimiter) {
        StringBuilder sb = new StringBuilder(arr[0]);
        for (int i = 1; i < arr.length; i++)
            sb.append(delimiter).append(arr[i]);

        return sb.toString();
    }

}
