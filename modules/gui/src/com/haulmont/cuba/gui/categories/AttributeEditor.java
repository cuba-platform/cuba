/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.categories;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
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
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.util.*;
import java.util.List;

/**
 * Class that encapsulates editing of {@link com.haulmont.cuba.core.entity.CategoryAttribute} entities.
 * 
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class AttributeEditor extends AbstractEditor {

    private Container fieldsContainer;
    private TextField nameField;
    private CheckBox requiredField;
    private LookupField dataTypeField;
    private CategoryAttribute attribute;
    private boolean dataTypeFieldInited = false;
    private DataService dataService;

    @Inject
    private Datasource attributeDs;

    @Inject
    private ComponentsFactory factory;

    @Inject
    private WindowConfig windowConfig;

    private static final String FIELD_WIDTH = "200px";

    public AttributeEditor(IFrame frame) {
        super(frame);
    }

    public void init(Map<String, Object> params) {
        dataService = getDsContext().getDataService();
        fieldsContainer = getComponent("attributeProperties");

        nameField = factory.createComponent(TextField.NAME);
        nameField.setId("name");
        nameField.setRequired(true);
        nameField.setRequiredMessage(getMessage("nameRequired"));
        nameField.setCaption(getMessage("name"));
        nameField.setWidth(FIELD_WIDTH);
        nameField.requestFocus();
        nameField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setName((String) value);
            }
        });
        fieldsContainer.add(nameField);

        requiredField = factory.createComponent(CheckBox.NAME);
        requiredField.setId("required");
        requiredField.setCaption(getMessage("required"));
        requiredField.setWidth(FIELD_WIDTH);

        requiredField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setRequired((Boolean) value);
            }
        });
        fieldsContainer.add(requiredField);


        dataTypeField = factory.createComponent(LookupField.NAME);
        Map<String, Object> options = new HashMap<String, Object>();
        RuntimePropsDatasource.PropertyType[] types = RuntimePropsDatasource.PropertyType.values();
        for (RuntimePropsDatasource.PropertyType propertyType : types) {
            options.put(getMessage(propertyType.toString()), propertyType);
        }
        dataTypeField.setWidth(FIELD_WIDTH);

        dataTypeField.setNewOptionAllowed(false);
        dataTypeField.setRequired(true);
        dataTypeField.setRequiredMessage(getMessage("dataTypeRequired"));
        dataTypeField.setOptionsMap(options);
        dataTypeField.setCaption(getMessage("dataType"));
        dataTypeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (prevValue != null) {
                    clearValue(attribute);
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

    @Override
    public void commitAndClose() {
        CollectionDatasource parent = (CollectionDatasource) ((DatasourceImplementation) attributeDs).getParent();
        if (parent != null) {
            CategoryAttribute categoryAttribute = (CategoryAttribute) getItem();
            for (Object id : parent.getItemIds()) {
                CategoryAttribute ca = (CategoryAttribute) parent.getItem(id);
                if (ca.getName().equals(categoryAttribute.getName())
                        && (!ca.equals(categoryAttribute))) {
                    showNotification(getMessage("validationFail"), getMessage("uniqueName"), NotificationType.TRAY);
                    return;
                }
            }
        }

        super.commitAndClose();
    }

    private void generateDefaultValueField(Enum<RuntimePropsDatasource.PropertyType> dataType, boolean setValue) {

        boolean hasValue = (attribute.getDefaultValue() == null || !setValue) ? (false) : (true);
        clearComponents();
        if (RuntimePropsDatasource.PropertyType.STRING.equals(dataType)) {
            TextField textField = factory.createComponent(TextField.NAME);
            textField.setId("defaultValue");
            textField.setCaption(getMessage("defaultValue"));
            textField.setDatatype(Datatypes.get(String.class));
            textField.setWidth(FIELD_WIDTH);

            textField.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    attribute.setDefaultString((String) value);
                }
            });
            if (hasValue)
                textField.setValue(attribute.getDefaultString());
            fieldsContainer.add(textField);
        }

        if (RuntimePropsDatasource.PropertyType.DATE.equals(dataType)) {
            DateField dateField = factory.createComponent(DateField.NAME);
            dateField.setId("defaultValue");
            dateField.setCaption(getMessage("defaultValue"));
            fieldsContainer.add(dateField);
            dateField.addValidator(new DateValidator(MessageProvider.getMessage(AppConfig.getMessagesPack(), "validation.invalidDate")));
            dateField.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    attribute.setDefaultDate((Date) value);
                }
            });
            if (hasValue)
                dateField.setValue(attribute.getDefaultDate());
        } else if (RuntimePropsDatasource.PropertyType.INTEGER.equals(dataType)) {
            TextField textField = factory.createComponent(TextField.NAME);
            textField.setId("defaultValue");
            textField.setCaption(getMessage("defaultValue"));
            textField.addValidator(new IntegerValidator(MessageProvider.getMessage(AppConfig.getMessagesPack(),
                    "validation.invalidNumber")));
            textField.setDatatype(Datatypes.get(Integer.class));
            textField.setWidth(FIELD_WIDTH);
            textField.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    attribute.setDefaultInt((Integer) value);
                }
            });
            if (hasValue)
                textField.setValue(attribute.getDefaultInt());
            fieldsContainer.add(textField);
        } else if (RuntimePropsDatasource.PropertyType.DOUBLE.equals(dataType)) {
            TextField textField = factory.createComponent(TextField.NAME);
            textField.setId("defaultValue");
            textField.setCaption(getMessage("defaultValue"));
            textField.setDatatype(Datatypes.get(Double.class));
            textField.setWidth(FIELD_WIDTH);
            textField.addValidator(new DoubleValidator(
                    MessageProvider.getMessage(AppConfig.getMessagesPack(),
                            "validation.invalidNumber")));
            textField.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    attribute.setDefaultDouble((Double) value);
                }
            });

            if (hasValue)
                textField.setValue(attribute.getDefaultDouble());
            fieldsContainer.add(textField);
        } else if (RuntimePropsDatasource.PropertyType.BOOLEAN.equals(dataType)) {
            CheckBox checkBox = factory.createComponent(CheckBox.NAME);
            checkBox.setId("defaultValue");
            checkBox.setCaption(getMessage("defaultValue"));
            checkBox.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    attribute.setDefaultBoolean((Boolean) value);
                }
            });

            if (hasValue)
                checkBox.setValue(attribute.getDefaultBoolean());
            fieldsContainer.add(checkBox);
        }
    }

    private void generateDefaultEnumValueField(boolean setValue) {
        clearComponents();

        boolean hasValue = (attribute.getDataType() == null || !setValue) ? (false) : (true);

        TextField textField = factory.createComponent(TextField.NAME);
        textField.setId("enumeration");
        textField.setCaption(getMessage("ENUMERATION"));
        textField.setWidth(FIELD_WIDTH);
        textField.setRequired(true);  
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
        defaultValueField.setWidth(FIELD_WIDTH);
        fieldsContainer.add(defaultValueField);

        defaultValueField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setDefaultString((String) value);
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
        entityTypeField.setRequired(true);
        entityTypeField.setRequiredMessage(getMessage("entityTypeRequired"));
        entityTypeField.setWidth(FIELD_WIDTH);
        Map<String, Object> options = new TreeMap<String, Object>();
        MetaClass entityType = null;
        for (MetaClass metaClass : MetadataHelper.getAllPersistentMetaClasses()) {
            if (!BooleanUtils.isTrue((Boolean) metaClass.getAnnotations().get(SystemLevel.class.getName()))) {
                options.put(MessageUtils.getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", metaClass);
                if (hasValue && metaClass.getJavaClass().getName().equals(attribute.getDataType())) {
                    entityType = metaClass;
                }
            }
        }
        entityTypeField.setOptionsMap(options);

        fieldsContainer.add(entityTypeField);

        final LookupField entityField = factory.createComponent(LookupField.NAME);
        entityField.setId("entityField");
        entityField.setCaption(getMessage("defaultValue"));
        entityField.setWidth(FIELD_WIDTH);
        fieldsContainer.add(entityField);

        entityTypeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setDataType(((MetaClass) value).getJavaClass().getName());
                fillEntities(entityField, ((MetaClass) value).getJavaClass());
            }
        });
        entityTypeField.setValue(entityType);

        final LookupField screenField = factory.createComponent(LookupField.NAME);
        screenField.setId("screenField");
        screenField.setCaption(getMessage("screen"));
        screenField.setWidth(FIELD_WIDTH);
        fieldsContainer.add(screenField);

        Collection<WindowInfo> windowInfoCollection = windowConfig.getWindows();
        List screensList = new ArrayList();
        for (WindowInfo windowInfo : windowInfoCollection) {
            screensList.add(windowInfo.getId());
        }
        screenField.setOptionsList(screensList);

        screenField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setScreen(value != null ? value.toString() : null);
            }
        });
        screenField.setValue(attribute.getScreen());
    }

    private void fillEntities(LookupField entityField, Class clazz) {
        Map<String, Object> entitiesMap = new HashMap<String, Object>();
        String entityClassName = MetadataProvider.getSession().getClass(clazz).getName();
        LoadContext entitiesContext = new LoadContext(clazz);
        LoadContext.Query query = entitiesContext.setQueryString("select a from " + entityClassName + " a");
        entitiesContext.setView("_minimal");
        List<BaseUuidEntity> list = dataService.loadList(entitiesContext);
        for (BaseUuidEntity entity : list) {
            entitiesMap.put(InstanceUtils.getInstanceName(entity), entity);
        }
        entityField.setOptionsMap(entitiesMap);

        if (attribute.getDefaultEntityId() != null) {
            LoadContext entityContext = new LoadContext(clazz);
            LoadContext.Query query2 = entityContext.setQueryString("select a from " + entityClassName + " a where a.id =:e");
            query2.addParameter("e", attribute.getDefaultEntityId());
            entityContext.setView("_minimal");

            BaseUuidEntity entity = dataService.load(entityContext);
            if (entity != null) {
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
                else
                    attribute.setDefaultEntityId(null);
            }
        });
    }

    public void setItem(Entity item) {
        super.setItem(item);

        attribute = (CategoryAttribute) getItem();
        nameField.setValue(attribute.getName());
        requiredField.setValue(attribute.getRequired());
        if (BooleanUtils.isTrue(attribute.getIsEntity())) {
            dataTypeField.setValue(RuntimePropsDatasource.PropertyType.ENTITY);
        } else {
            if (attribute.getDataType() != null) {
                RuntimePropsDatasource.PropertyType type = RuntimePropsDatasource.PropertyType.valueOf(attribute.getDataType());
                dataTypeField.setValue(type);
            }
        }
    }

    private void clearComponents() {
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
        Component component5 = fieldsContainer.getComponent("screenField");
        if (component5 != null) {
            fieldsContainer.remove(component5);
        }
    }

    private void clearValue(CategoryAttribute attribute) {
        attribute.setDefaultString(null);
        attribute.setDefaultInt(null);
        attribute.setDefaultDouble(null);
        attribute.setDefaultBoolean(null);
        attribute.setDefaultDate(null);
        attribute.setDefaultEntityId(null);
        attribute.setEnumeration(null);
    }
}
