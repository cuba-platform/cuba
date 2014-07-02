/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.categories;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ScreensHelper;
import com.haulmont.cuba.gui.components.*;
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

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

/**
 * Class that encapsulates editing of {@link com.haulmont.cuba.core.entity.CategoryAttribute} entities.
 * <p/>
 *
 * @author devyatkin
 * @version $Id$
 */
public class AttributeEditor extends AbstractEditor<CategoryAttribute> {

    protected Container fieldsContainer;
    protected TextField nameField;
    protected TextField codeField;
    protected CheckBox requiredField;
    protected LookupField screenField;
    protected CheckBox lookupField;
    protected LookupField dataTypeField;
    protected CategoryAttribute attribute;
    protected boolean dataTypeFieldInited = false;
    protected DataService dataService;

    @Inject
    protected Datasource attributeDs;

    @Inject
    protected ComponentsFactory factory;

    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected MessageTools messageTools;

    protected static final String FIELD_WIDTH = "200px";

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setWidth(251);

        dataService = getDsContext().getDataSupplier();
        fieldsContainer = getComponent("attributeProperties");

        nameField = factory.createComponent(TextField.NAME);
        nameField.setId("name");
        nameField.setRequired(true);
        nameField.setRequiredMessage(getMessage("nameRequired"));
        nameField.setCaption(getMessage("name"));
        nameField.setWidth(FIELD_WIDTH);
        nameField.setFrame(frame);
        nameField.requestFocus();
        nameField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setName((String) value);
            }
        });
        nameField.setMaxLength(CategoryAttribute.NAME_FIELD_LENGTH);
        fieldsContainer.add(nameField);

        codeField = factory.createComponent(TextField.NAME);
        codeField.setId("code");
        codeField.setCaption(getMessage("code"));
        codeField.setWidth(FIELD_WIDTH);
        codeField.setFrame(frame);
        codeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setCode((String) value);
            }
        });
        codeField.setMaxLength(CategoryAttribute.CODE_FIELD_LENGTH);
        fieldsContainer.add(codeField);

        requiredField = factory.createComponent(CheckBox.NAME);
        requiredField.setId("required");
        requiredField.setCaption(getMessage("required"));
        requiredField.setWidth(FIELD_WIDTH);
        requiredField.setFrame(frame);

        requiredField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setRequired((Boolean) value);
            }
        });
        fieldsContainer.add(requiredField);

        dataTypeField = factory.createComponent(LookupField.NAME);
        Map<String, Object> options = new HashMap<>();
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
        dataTypeField.setFrame(frame);
        dataTypeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (prevValue != null) {
                    clearValue(attribute);
                }
                lookupField.setVisible(false);
                if (RuntimePropsDatasource.PropertyType.ENTITY.equals(value)) {
                    attribute.setIsEntity(true);
                    generateDefaultEntityValueField(!dataTypeFieldInited);
                    lookupField.setVisible(true);
                } else if (RuntimePropsDatasource.PropertyType.ENUMERATION.equals(value)) {
                    attribute.setIsEntity(false);
                    attribute.setDataType(value.toString());
                    generateDefaultEnumValueField(!dataTypeFieldInited);
                } else {
                    if (RuntimePropsDatasource.PropertyType.BOOLEAN.equals(value)) {
                        requiredField.setVisible(false);
                    }
                    attribute.setDataType(value.toString());
                    attribute.setIsEntity(false);
                    generateDefaultValueField((Enum<RuntimePropsDatasource.PropertyType>) value, !dataTypeFieldInited);
                }
                dataTypeFieldInited = true;
            }
        });
        fieldsContainer.add(dataTypeField);

        lookupField = factory.createComponent(CheckBox.NAME);
        lookupField.setId("lookup");
        lookupField.setCaption(getMessage("lookup"));
        lookupField.setWidth(FIELD_WIDTH);
        lookupField.setFrame(frame);
        lookupField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setLookup((Boolean) value);
            }
        });
        lookupField.setVisible(false);
        fieldsContainer.add(lookupField);
    }

    @Override
    public void postValidate(ValidationErrors errors) {
        CollectionDatasource parent = (CollectionDatasource) ((DatasourceImplementation) attributeDs).getParent();
        if (parent != null) {
            CategoryAttribute categoryAttribute = getItem();
            for (Object id : parent.getItemIds()) {
                CategoryAttribute ca = (CategoryAttribute) parent.getItem(id);
                if (ca.getName().equals(categoryAttribute.getName())
                        && (!ca.equals(categoryAttribute))) {
                    errors.add(getMessage("uniqueName"));
                    return;
                } else if (ca.getCode() != null && ca.getCode().equals(categoryAttribute.getCode())
                        && (!ca.equals(categoryAttribute))) {
                    errors.add(getMessage("uniqueCode"));
                    return;
                }
            }
        }
    }

    protected void generateDefaultValueField(Enum<RuntimePropsDatasource.PropertyType> dataType, boolean setValue) {

        boolean hasValue = (attribute.getDefaultValue() == null || !setValue) ? (false) : (true);
        clearComponents();
        if (RuntimePropsDatasource.PropertyType.STRING.equals(dataType)) {
            TextField textField = factory.createComponent(TextField.NAME);
            textField.setId("stringDefaultValueField");
            textField.setCaption(getMessage("defaultValue"));
            textField.setDatatype(Datatypes.getNN(String.class));
            textField.setWidth(FIELD_WIDTH);
            textField.setFrame(frame);

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
            BoxLayout boxLayout = factory.createComponent(BoxLayout.VBOX);
            boxLayout.setId("defaultDateBox");
            CheckBox checkBox = factory.createComponent(CheckBox.NAME);
            checkBox.setId("defaultDateIsCurrent");
            checkBox.setCaption(getMessage("currentDate"));
            checkBox.setWidth(Component.AUTO_SIZE);
            final DateField dateField = factory.createComponent(DateField.NAME);
            dateField.setId("dateDefaultValueField");
            dateField.setCaption(getMessage("defaultValue"));
            dateField.setFrame(frame);
            dateField.setWidth(FIELD_WIDTH);
            boxLayout.add(checkBox);
            boxLayout.add(dateField);
            fieldsContainer.add(boxLayout);
            checkBox.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    if (BooleanUtils.isTrue((Boolean) value)) {
                        dateField.setVisible(false);
                        attribute.setDefaultDateIsCurrent(true);
                    } else {
                        dateField.setVisible(true);
                        attribute.setDefaultDateIsCurrent(false);
                    }
                }
            });
            dateField.addValidator(new DateValidator(messages.getMessage(AppConfig.getMessagesPack(), "validation.invalidDate")));
            dateField.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    attribute.setDefaultDate((Date) value);
                }
            });
            if (BooleanUtils.isTrue(attribute.getDefaultDateIsCurrent())) {
                checkBox.setValue(true);
                dateField.setVisible(false);
            }
            if (hasValue)
                dateField.setValue(attribute.getDefaultDate());
        } else if (RuntimePropsDatasource.PropertyType.INTEGER.equals(dataType)) {
            TextField textField = factory.createComponent(TextField.NAME);
            textField.setId("intDefaultValueField");
            textField.setCaption(getMessage("defaultValue"));
            textField.addValidator(new IntegerValidator(messages.getMessage(AppConfig.getMessagesPack(),
                    "validation.invalidNumber")));
            textField.setDatatype(Datatypes.get(Integer.class));
            textField.setWidth(FIELD_WIDTH);
            textField.setFrame(frame);
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
            textField.setId("doubleDefaultValueField");
            textField.setCaption(getMessage("defaultValue"));
            textField.setDatatype(Datatypes.get(Double.class));
            textField.setWidth(FIELD_WIDTH);
            textField.setFrame(frame);
            textField.addValidator(new DoubleValidator(
                    messages.getMessage(AppConfig.getMessagesPack(),
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
            checkBox.setId("booleanDefaultValueField");
            checkBox.setCaption(getMessage("defaultValue"));
            checkBox.setFrame(frame);
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

    protected void generateDefaultEnumValueField(boolean setValue) {
        clearComponents();

        boolean hasValue = (attribute.getDataType() == null || !setValue) ? (false) : (true);

        TextField textField = factory.createComponent(TextField.NAME);
        textField.setId("enumeration");
        textField.setCaption(getMessage("ENUMERATION"));
        textField.setWidth(FIELD_WIDTH);
        textField.setRequired(true);
        textField.setRequiredMessage(getMessage("enumRequired"));
        textField.setFrame(frame);
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
        defaultValueField.setId("enumDefaultValueField");
        defaultValueField.setCaption(getMessage("defaultValue"));
        defaultValueField.setWidth(FIELD_WIDTH);
        defaultValueField.setFrame(frame);
        fieldsContainer.add(defaultValueField);

        defaultValueField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setDefaultString((String) value);
            }
        });
        defaultValueField.setValue(attribute.getDefaultValue());
    }

    protected void generateDefaultEntityValueField(boolean setValue) {
        clearComponents();

        boolean hasValue = (attribute.getDataType() == null || !setValue) ? (false) : (true);

        LookupField entityTypeField = factory.createComponent(LookupField.NAME);
        entityTypeField.setId("entityType");
        entityTypeField.setCaption(getMessage("entityType"));
        entityTypeField.setRequired(true);
        entityTypeField.setRequiredMessage(getMessage("entityTypeRequired"));
        entityTypeField.setWidth(FIELD_WIDTH);
        entityTypeField.setFrame(frame);
        Map<String, Object> options = new TreeMap<>();
        MetaClass entityType = null;
        for (MetaClass metaClass : metadataTools.getAllPersistentMetaClasses()) {
            if (!BooleanUtils.isTrue((Boolean) metaClass.getAnnotations().get(SystemLevel.class.getName()))) {
                options.put(messageTools.getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", metaClass);
                if (hasValue && metaClass.getJavaClass().getName().equals(attribute.getDataType())) {
                    entityType = metaClass;
                }
            }
        }
        entityTypeField.setOptionsMap(options);

        fieldsContainer.add(entityTypeField);

        final LookupField entityField = factory.createComponent(LookupField.NAME);
        entityField.setId("entityDefaultValueField");
        entityField.setCaption(getMessage("defaultValue"));
        entityField.setWidth(FIELD_WIDTH);
        entityField.setFrame(frame);
        fieldsContainer.add(entityField);

        entityTypeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                attribute.setDataType(((MetaClass) value).getJavaClass().getName());
                fillEntities(entityField, ((MetaClass) value).getJavaClass());
            }
        });
        entityTypeField.setValue(entityType);

        screenField = factory.createComponent(LookupField.NAME);
        screenField.setId("screenField");
        screenField.setCaption(getMessage("screen"));
        screenField.setWidth(FIELD_WIDTH);
        screenField.setRequired(true);
        screenField.setRequiredMessage(getMessage("entityScreenRequired"));
        screenField.setFrame(frame);
        fieldsContainer.add(screenField);

        lookupField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, Object value) {
                if ((Boolean) value) {
                    screenField.setValue(null);
                    screenField.setEnabled(false);
                } else {
                    screenField.setEnabled(true);
                }
            }
        });

        List<WindowInfo> windowInfoCollection =  new ArrayList<>(windowConfig.getWindows());
        ScreensHelper.sortWindowInfos(windowInfoCollection);

        List<String> screensList = new ArrayList<>();
        for (WindowInfo windowInfo : windowInfoCollection) {
            String windowId = windowInfo.getId();
            if (!windowId.contains(".")
                    || windowId.contains(Window.BROWSE_WINDOW_SUFFIX)
                    || windowId.contains(Window.LOOKUP_WINDOW_SUFFIX)) {
                screensList.add(windowId);
            }
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

    protected void fillEntities(LookupField entityField, Class clazz) {
        Map<String, Object> entitiesMap = new HashMap<>();
        String entityClassName = metadata.getClass(clazz).getName();
        LoadContext entitiesContext = new LoadContext(clazz);
        entitiesContext.setQueryString("select a from " + entityClassName + " a");
        entitiesContext.setView("_minimal");
        List<BaseUuidEntity> list = dataService.loadList(entitiesContext);
        for (BaseUuidEntity entity : list) {
            entitiesMap.put(InstanceUtils.getInstanceName(entity), entity);
        }
        entityField.setOptionsMap(entitiesMap);

        if (attribute.getDefaultEntityId() != null) {
            LoadContext entityContext = new LoadContext(clazz);
            LoadContext.Query query2 = entityContext.setQueryString("select a from " + entityClassName + " a where a.id =:e");
            query2.setParameter("e", attribute.getDefaultEntityId());
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

    @Override
    protected void postInit() {
        attribute = getItem();
        nameField.setValue(attribute.getName());
        codeField.setValue(attribute.getCode());
        requiredField.setValue(attribute.getRequired());
        lookupField.setValue(attribute.getLookup());
        if (BooleanUtils.isTrue(attribute.getIsEntity())) {
            dataTypeField.setValue(RuntimePropsDatasource.PropertyType.ENTITY);
        } else {
            if (attribute.getDataType() != null) {
                RuntimePropsDatasource.PropertyType type = RuntimePropsDatasource.PropertyType.valueOf(attribute.getDataType());
                dataTypeField.setValue(type);
            }
        }

        if (screenField != null) {
            screenField.setEnabled(BooleanUtils.isNotTrue(attribute.getLookup()));
        }

        if (dataTypeField.getValue() != null && dataTypeField.getValue().equals(RuntimePropsDatasource.PropertyType.BOOLEAN)) {
            requiredField.setVisible(false);
        }
    }

    protected void clearComponents() {
        Component component = fieldsContainer.getComponent("stringDefaultValueField");
        if (component != null)
            fieldsContainer.remove(component);
        component = fieldsContainer.getComponent("intDefaultValueField");
        if (component != null)
            fieldsContainer.remove(component);
        component = fieldsContainer.getComponent("doubleDefaultValueField");
        if (component != null)
            fieldsContainer.remove(component);
        component = fieldsContainer.getComponent("booleanDefaultValueField");
        if (component != null)
            fieldsContainer.remove(component);
        component = fieldsContainer.getComponent("enumDefaultValueField");
        if (component != null)
            fieldsContainer.remove(component);
        component = fieldsContainer.getComponent("entityDefaultValueField");
        if (component != null)
            fieldsContainer.remove(component);
        component = fieldsContainer.getComponent("entityType");
        if (component != null)
            fieldsContainer.remove(component);
        component = fieldsContainer.getComponent("entityField");
        if (component != null) {
            fieldsContainer.remove(component);
        }
        component = fieldsContainer.getComponent("enumeration");
        if (component != null) {
            fieldsContainer.remove(component);
        }
        component = fieldsContainer.getComponent("screenField");
        if (component != null) {
            fieldsContainer.remove(component);
        }
        component = fieldsContainer.getComponent("defaultDateBox");
        if (component != null) {
            fieldsContainer.remove(component);
        }
    }

    protected void clearValue(CategoryAttribute attribute) {
        attribute.setDefaultString(null);
        attribute.setDefaultInt(null);
        attribute.setDefaultDouble(null);
        attribute.setDefaultBoolean(null);
        attribute.setDefaultDate(null);
        attribute.setDefaultEntityId(null);
        attribute.setEnumeration(null);
    }
}
