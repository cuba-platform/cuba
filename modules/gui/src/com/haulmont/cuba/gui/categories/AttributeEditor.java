/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.categories;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.validators.DateValidator;
import com.haulmont.cuba.gui.components.validators.DoubleValidator;
import com.haulmont.cuba.gui.components.validators.IntegerValidator;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;

import java.text.ParseException;
import java.util.*;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class AttributeEditor extends AbstractEditor {

    private Container fieldsContainer;
    private TextField nameField;
    private LookupField dataTypeField;
    private CategoryAttribute attribute;
    private Datasource ds;
    private boolean dataTypeFieldInited = false;
    private DataService dataService;
    private ComponentsFactory factory = AppConfig.getFactory();

    public AttributeEditor(IFrame frame) {
        super(frame);
    }

    public void init(Map<String, Object> params) {
        ds = getDsContext().get("attributeDs");
        dataService = getDsContext().getDataService();
        fieldsContainer = getComponent("attributeProperties");
        nameField = factory.createComponent(TextField.NAME);
        nameField.setId("name");
        nameField.setRequired(true);
        nameField.setCaption(getMessage("name"));

        nameField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setName((String) value);
            }
        });
        fieldsContainer.add(nameField);
        dataTypeField = factory.createComponent(LookupField.NAME);
        Map<String, Object> options = new HashMap<String, Object>();
        RuntimePropsDatasource.PropertyType[] types = RuntimePropsDatasource.PropertyType.values();
        for (RuntimePropsDatasource.PropertyType propertyType : types) {
            options.put(getMessage(propertyType.toString()), propertyType);
        }

        dataTypeField.setNewOptionAllowed(false);
        dataTypeField.setRequired(true);
        dataTypeField.setOptionsMap(options);
        dataTypeField.setCaption(getMessage("dataType"));
        dataTypeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (prevValue != null) {
                    attribute.setDefaultValue(null);
                    attribute.setDefaultEntityId(null);
                }
                if (RuntimePropsDatasource.PropertyType.ENTITY.equals(value)) {
                    attribute.setIsEntity(true);
                    generateDefaultEntityValueField(!dataTypeFieldInited);

                } else if (RuntimePropsDatasource.PropertyType.ENUMERATION.equals(value)) {
                    attribute.setIsEntity(false);
                    attribute.setDataType(value.toString());
                    generateDefaultEnumValueField(!dataTypeFieldInited);
                } else {
                    attribute.setDataType(value.toString());
                    attribute.setIsEntity(false);
                    generateDefaultValueField((Enum<RuntimePropsDatasource.PropertyType>) value, !dataTypeFieldInited);
                }
                dataTypeFieldInited = true;
            }
        });
        fieldsContainer.add(dataTypeField);
    }

    private void generateDefaultValueField(Enum<RuntimePropsDatasource.PropertyType> dataType, boolean setValue) {

        boolean hasValue = (attribute.getDefaultValue() == null || !setValue) ? (false) : (true);
        clearComponents();
        try {
            if (RuntimePropsDatasource.PropertyType.STRING.equals(dataType)) {
                TextField textField = factory.createComponent(TextField.NAME);
                textField.setId("defaultValue");
                textField.setCaption(getMessage("defaultValue"));
                textField.setDatatype(Datatypes.get(String.class));

                textField.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        attribute.setDefaultValue((String) value);
                    }
                });
                if (hasValue)
                    textField.setValue(attribute.getDefaultValue());
                fieldsContainer.add(textField);
            }

            if (RuntimePropsDatasource.PropertyType.DATE.equals(dataType)) {
                DateField dateField = factory.createComponent(DateField.NAME);
                dateField.setId("defaultValue");
                dateField.setCaption(getMessage("defaultValue"));
                if (hasValue)
                    dateField.setValue(Datatypes.get(Date.class).parse(attribute.getDefaultValue()));
                fieldsContainer.add(dateField);
                dateField.addValidator(new DateValidator(MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "validation.invalidDate")));
                dateField.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        attribute.setDefaultValue(Datatypes.get(Date.class).format((Date) value));
                    }
                });
            } else if (RuntimePropsDatasource.PropertyType.INTEGER.equals(dataType)) {
                TextField textField = factory.createComponent(TextField.NAME);
                textField.setId("defaultValue");
                textField.setCaption(getMessage("defaultValue"));
                textField.addValidator(new IntegerValidator(MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(),
                        "validation.invalidNumber")));
                textField.setDatatype(Datatypes.get(Integer.class));

                textField.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        attribute.setDefaultValue(Datatypes.get(Integer.class).format((Integer) value));
                    }
                });
                if (hasValue)
                    textField.setValue(Datatypes.get(Integer.class).parse(attribute.getDefaultValue()));
                fieldsContainer.add(textField);
            } else if (RuntimePropsDatasource.PropertyType.DOUBLE.equals(dataType)) {
                TextField textField = factory.createComponent(TextField.NAME);
                textField.setId("defaultValue");
                textField.setCaption(getMessage("defaultValue"));
                textField.setDatatype(Datatypes.get(Double.class));
                textField.addValidator(new DoubleValidator(
                        MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(),
                                "validation.invalidNumber")));
                textField.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        attribute.setDefaultValue(Datatypes.get(Double.class).format((Double) value));
                    }
                });

                if (hasValue)
                    textField.setValue(Datatypes.get(Double.class).parse(attribute.getDefaultValue()));
                fieldsContainer.add(textField);
            } else if (RuntimePropsDatasource.PropertyType.BOOLEAN.equals(dataType)) {
                CheckBox checkBox = factory.createComponent(CheckBox.NAME);
                checkBox.setId("defaultValue");
                checkBox.setCaption(getMessage("defaultValue"));
                checkBox.addListener(new ValueListener() {
                    @Override
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        attribute.setDefaultValue(Datatypes.get(Boolean.class).format((Boolean) value));
                    }
                });

                if (hasValue)
                    checkBox.setValue(Datatypes.get(Boolean.class).parse(attribute.getDefaultValue()));
                fieldsContainer.add(checkBox);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateDefaultEnumValueField(boolean setValue) {
        clearComponents();

        boolean hasValue = (attribute.getDataType() == null || !setValue) ? (false) : (true);

        TextField textField = factory.createComponent(TextField.NAME);
        textField.setId("enumeration");
        textField.setCaption(getMessage("ENUMERATION"));
        textField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setEnumeration((String) value);
            }
        });
        if (hasValue)
            textField.setValue(attribute.getEnumeration());
        fieldsContainer.add(textField);

        final TextField defaultValueField = factory.createComponent(TextField.NAME);
        defaultValueField.setId("defaultValue");
        defaultValueField.setCaption(getMessage("defaultValue"));
        fieldsContainer.add(defaultValueField);

        defaultValueField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setDefaultValue((String) value);
            }
        });
        defaultValueField.setValue(attribute.getDefaultValue());
    }

    private void generateDefaultEntityValueField(boolean setValue) {
        clearComponents();

        boolean hasValue = (attribute.getDataType() == null || !setValue) ? (false) : (true);

        LookupField entityTypeField = factory.createComponent(LookupField.NAME);
        entityTypeField.setId("entityType");
        entityTypeField.setCaption(getMessage("entityType"));
        Map<String, Object> options = new HashMap<String, Object>();
        MetaClass entityType = null;
        for (MetaClass metaClass : MetadataHelper.getAllPersistentMetaClasses()) {
            options.put(MessageUtils.getEntityCaption(metaClass), metaClass);
            if (hasValue && metaClass.getJavaClass().getName().equals(attribute.getDataType())) {
                entityType = metaClass;
            }
        }
        entityTypeField.setOptionsMap(options);

        fieldsContainer.add(entityTypeField);

        final LookupField entityField = factory.createComponent(LookupField.NAME);
        entityField.setId("entityField");
        entityField.setCaption(getMessage("defaultValue"));
        fieldsContainer.add(entityField);

        entityTypeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                fillEntities(entityField, ((MetaClass) value).getJavaClass());
                attribute.setDataType(((MetaClass) value).getJavaClass().getName());
                fillEntities(entityField, ((MetaClass) value).getJavaClass());
            }
        });
        entityTypeField.setValue(entityType);
    }

    private void fillEntities(LookupField entityField, Class clazz) {
        Map<String, Object> entitiesMap = new HashMap<String, Object>();
        String entityClassName = MetadataProvider.getSession().getClass(clazz).getName();
        LoadContext entitiesContext = new LoadContext(clazz);
        LoadContext.Query query = entitiesContext.setQueryString("select a from " + entityClassName + " a");
        entitiesContext.setView("_minimal");
        List<BaseUuidEntity> list = dataService.loadList(entitiesContext);
        for (BaseUuidEntity entity : list) {
            entitiesMap.put(InstanceUtils.getInstanceName((Instance) entity), entity);
        }
        entityField.setOptionsMap(entitiesMap);

        if (attribute.getDefaultEntityId() != null) {
            LoadContext entityContext = new LoadContext(clazz);
            LoadContext.Query query2 = entityContext.setQueryString("select a from " + entityClassName + " a where a.id =:e");
            query2.addParameter("e", attribute.getDefaultEntityId());
            entityContext.setView("_minimal");

            BaseUuidEntity entity = dataService.load(entityContext);
            if (entity != null) {
                String entityName = InstanceUtils.getInstanceName((Instance) entity);
                entityField.setValue(entity);
            } else {
                entityField.setValue(null);
            }
        }
        entityField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (value != null)
                    attribute.setDefaultEntityId(((BaseUuidEntity) value).getId());
            }
        });
    }

    public void setItem(Entity item) {
        super.setItem(item);

        attribute = (CategoryAttribute) getItem();
        nameField.setValue(attribute.getName());
        if (BooleanUtils.isTrue(attribute.getIsEntity())) {
            dataTypeField.setValue(RuntimePropsDatasource.PropertyType.ENTITY);
        } else {
            if (attribute.getDataType() != null) {
                RuntimePropsDatasource.PropertyType type = RuntimePropsDatasource.PropertyType.valueOf(attribute.getDataType());
                dataTypeField.setValue(type);
            }

        }
    }

    protected void clearComponents() {
        Component component = fieldsContainer.getComponent("defaultValue");
        if (component != null)
            fieldsContainer.remove(component);
        Component component2 = fieldsContainer.getComponent("entityType");
        if (component2 != null)
            fieldsContainer.remove(component2);
        Component component3 = fieldsContainer.getComponent("entityField");
        if (component3 != null) {
            fieldsContainer.remove(component3);
        }
        Component component4 = fieldsContainer.getComponent("enumeration");
        if (component4 != null) {
            fieldsContainer.remove(component4);
        }
    }

}
