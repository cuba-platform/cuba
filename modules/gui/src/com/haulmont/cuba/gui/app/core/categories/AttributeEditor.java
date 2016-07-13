/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.core.categories;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ScreensHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.*;

/**
 * Class that encapsulates editing of {@link com.haulmont.cuba.core.entity.CategoryAttribute} entities.
 * <p>
 *
 */
public class AttributeEditor extends AbstractEditor<CategoryAttribute> {
    protected static final Multimap<PropertyType, String> FIELDS_VISIBLE_FOR_DATATYPES = ArrayListMultimap.create();
    protected static final Set<String> ALWAYS_VISIBLE_FIELDS = new HashSet<>(Arrays.asList("name", "code", "required", "dataType"));

    static {
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.BOOLEAN, "defaultBoolean");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.STRING, "defaultString");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.STRING, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.STRING, "rowsCount");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DOUBLE, "defaultDouble");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DOUBLE, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.INTEGER, "defaultInt");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.INTEGER, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DATE, "defaultDate");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DATE, "defaultDateIsCurrent");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DATE, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENUMERATION, "enumeration");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENUMERATION, "defaultString");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENUMERATION, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "entityClass");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "screen");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "lookup");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "defaultEntityId");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "width");
    }

    protected CategoryAttribute attribute;

    protected DataSupplier dataSupplier;

    @Inject
    protected FieldGroup attributeFieldGroup;
    protected LookupField dataTypeField;
    protected LookupField screenField;
    protected LookupField entityTypeField;
    protected PickerField defaultEntityField;

    @Inject
    protected Datasource<CategoryAttribute> attributeDs;

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

    @Inject
    protected DatatypeFormatter datatypeFormatter;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected ScreensHelper screensHelper;

    @Inject
    protected ComponentsFactory componentsFactory;

    protected String fieldWidth;

    @Inject
    protected Table targetScreensTable;

    @Inject
    private CollectionDatasource<ScreenAndComponent, UUID> screensDs;

    @Override
    public void init(Map<String, Object> params) {
        getDialogOptions().setWidth(themeConstants.getInt("cuba.gui.AttributeEditor.width"));

        fieldWidth = themeConstants.get("cuba.gui.AttributeEditor.field.width");

        dataSupplier = getDsContext().getDataSupplier();

        initFieldGroup();

        targetScreensTable.addAction(new AbstractAction("create") {
            @Override
            public void actionPerform(Component component) {
                screensDs.addItem(new ScreenAndComponent());
            }
        });
        targetScreensTable.addAction(new RemoveAction(targetScreensTable));
    }

    protected void initFieldGroup() {
        attributeFieldGroup.addCustomField("defaultBoolean", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                LookupField lookupField = factory.createComponent(LookupField.class);
                Map<String, Object> options = new TreeMap<>();
                options.put(datatypeFormatter.formatBoolean(true), true);
                options.put(datatypeFormatter.formatBoolean(false), false);
                lookupField.setOptionsMap(options);
                lookupField.setDatasource(attributeDs, "defaultBoolean");
                return lookupField;
            }
        });

        attributeFieldGroup.addCustomField("dataType", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                dataTypeField = factory.createComponent(LookupField.class);
                Map<String, Object> options = new TreeMap<>();
                PropertyType[] types = PropertyType.values();
                for (PropertyType propertyType : types) {
                    options.put(getMessage(propertyType.toString()), propertyType);
                }
                dataTypeField.setWidth(fieldWidth);

                dataTypeField.setNewOptionAllowed(false);
                dataTypeField.setRequired(true);
                dataTypeField.setRequiredMessage(getMessage("dataTypeRequired"));
                dataTypeField.setOptionsMap(options);
                dataTypeField.setCaption(getMessage("dataType"));
                dataTypeField.setFrame(frame);
                dataTypeField.setDatasource(datasource, propertyId);

                return dataTypeField;
            }
        });

        attributeFieldGroup.addCustomField("screen", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                screenField = factory.createComponent(LookupField.class);
                screenField.setId("screenField");
                screenField.setCaption(getMessage("screen"));
                screenField.setWidth(fieldWidth);
                screenField.setRequired(true);
                screenField.setRequiredMessage(getMessage("entityScreenRequired"));
                screenField.setFrame(frame);
                screenField.setDatasource(datasource, propertyId);

                return screenField;
            }
        });

        attributeFieldGroup.addCustomField("entityClass", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                entityTypeField = factory.createComponent(LookupField.class);
                entityTypeField.setId("entityClass");
                entityTypeField.setCaption(getMessage("entityType"));
                entityTypeField.setRequired(true);
                entityTypeField.setRequiredMessage(getMessage("entityTypeRequired"));
                entityTypeField.setWidth(fieldWidth);
                entityTypeField.setFrame(frame);
                Map<String, Object> options = new TreeMap<>();
                MetaClass entityType = null;
                for (MetaClass metaClass : metadataTools.getAllPersistentMetaClasses()) {
                    if (!metadataTools.isSystemLevel(metaClass)) {
                        options.put(messageTools.getDetailedEntityCaption(metaClass), metaClass.getJavaClass().getName());
                        if (attribute != null
                                && metaClass.getJavaClass().getName().equals(attribute.getEntityClass())) {
                            entityType = metaClass;
                        }
                    }
                }
                entityTypeField.setOptionsMap(options);
                entityTypeField.setValue(entityType);
                entityTypeField.setDatasource(datasource, propertyId);

                return entityTypeField;
            }
        });

        attributeFieldGroup.addCustomField("defaultEntityId", (datasource, propertyId) -> {
            defaultEntityField = factory.createComponent(PickerField.class);
            defaultEntityField.addValueChangeListener(e -> {
                if (e.getValue() instanceof BaseUuidEntity) {
                    attribute.setDefaultEntityId(((BaseUuidEntity) e.getValue()).getId());
                } else {
                    attribute.setDefaultEntityId(null);
                }
            });

            return defaultEntityField;
        });

        attributeDs.addItemPropertyChangeListener(e -> {
            String property = e.getProperty();
            if ("dataType".equalsIgnoreCase(property)
                    || "lookup".equalsIgnoreCase(property)
                    || "defaultDateIsCurrent".equalsIgnoreCase(property)
                    || "entityClass".equalsIgnoreCase(property)) {
                setupVisibility();
            }
            if ("name".equalsIgnoreCase(property)) {
                fillAttributeCode();
            }
        });
    }

    private void setupVisibility() {
        for (FieldGroup.FieldConfig fieldConfig : attributeFieldGroup.getFields()) {
            if (!ALWAYS_VISIBLE_FIELDS.contains(fieldConfig.getId())) {
                attributeFieldGroup.setVisible(fieldConfig.getId(), false);
            }
        }

        Collection<String> componentIds = FIELDS_VISIBLE_FOR_DATATYPES.get(attribute.getDataType());
        if (componentIds != null) {
            for (String componentId : componentIds) {
                attributeFieldGroup.getFieldComponent(componentId).setVisible(true);
            }
        }

        if (attribute.getDataType() == PropertyType.ENTITY) {
            if (StringUtils.isNotBlank(attribute.getEntityClass())) {
                defaultEntityField.setEditable(true);
                Class entityClass = attribute.getJavaClassForEntity();
                defaultEntityField.setMetaClass(metadata.getClass(entityClass));
                fillDefaultEntities(entityClass);
                fillSelectEntityScreens(entityClass);
            } else {
                defaultEntityField.setEditable(false);
            }

            if (Boolean.TRUE.equals(attribute.getLookup())) {
                attributeFieldGroup.setVisible("screen", false);
            } else {
                attributeFieldGroup.setVisible("screen", true);
            }
        }

        if (attribute.getDataType() == PropertyType.DATE) {
            if (Boolean.TRUE.equals(attribute.getDefaultDateIsCurrent())) {
                attributeFieldGroup.setVisible("defaultDate", false);
            } else {
                attributeFieldGroup.setVisible("defaultDate", true);
            }
        }
    }

    protected void fillSelectEntityScreens(Class entityClass) {
        String value = attribute.getScreen();
        Map<String, Object> screensMap = screensHelper.getAvailableBrowserScreens(entityClass);
        screenField.setValue(null);             // While #PL-4731 unfixed
        screenField.setOptionsMap(screensMap);
        screenField.setValue(screensMap.containsValue(value) ? value : null);
    }

    protected void fillDefaultEntities(Class entityClass) {
        String entityClassName = metadata.getClassNN(entityClass).getName();
        if (attribute.getDefaultEntityId() != null) {
            LoadContext<Entity> entityContext = new LoadContext<>(entityClass);
            LoadContext.Query query2 = entityContext.setQueryString("select a from " + entityClassName + " a where a.id =:e");
            query2.setParameter("e", attribute.getDefaultEntityId());
            entityContext.setView("_minimal");

            Entity entity = dataSupplier.load(entityContext);
            if (entity != null) {
                defaultEntityField.setValue(entity);
            } else {
                defaultEntityField.setValue(null);
            }
        }
    }

    protected void fillAttributeCode() {
        CategoryAttribute attribute = getItem();
        if (StringUtils.isBlank(attribute.getCode()) && StringUtils.isNotBlank(attribute.getName())) {
            String categoryName = StringUtils.EMPTY;
            if (attribute.getCategory() != null) {
                categoryName = StringUtils.defaultString(attribute.getCategory().getName());
            }
            attribute.setCode(StringUtils.deleteWhitespace(categoryName + attribute.getName()));
        }
    }

    @Override
    public boolean preCommit() {
        Collection<ScreenAndComponent> screens = screensDs.getItems();
        StringBuilder stringBuilder = new StringBuilder();
        for (ScreenAndComponent screenAndComponent : screens) {
            if (StringUtils.isNotBlank(screenAndComponent.getScreen())) {
                stringBuilder.append(screenAndComponent.getScreen());
                if (StringUtils.isNotBlank(screenAndComponent.getComponent())) {
                    stringBuilder.append("#");
                    stringBuilder.append(screenAndComponent.getComponent());
                }
                stringBuilder.append(",");
            }
        }

        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        attribute.setTargetScreens(stringBuilder.toString());
        return true;
    }

    @Override
    public void postValidate(ValidationErrors errors) {
        @SuppressWarnings("unchecked")
        CollectionDatasource<CategoryAttribute, UUID> parent
                = (CollectionDatasource<CategoryAttribute, UUID>) ((DatasourceImplementation) attributeDs).getParent();
        if (parent != null) {
            CategoryAttribute categoryAttribute = getItem();
            for (UUID id : parent.getItemIds()) {
                CategoryAttribute ca = parent.getItemNN(id);
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

    @Override
    protected void postInit() {
        attribute = getItem();
        Set<String> targetScreens = attribute.targetScreensSet();
        for (String targetScreen : targetScreens) {
            if (targetScreen.contains("#")) {
                String[] split = targetScreen.split("#");
                screensDs.addItem(new ScreenAndComponent(split[0], split[1]));
            } else {
                screensDs.addItem(new ScreenAndComponent(targetScreen, null));
            }
        }

        MetaClass categorizedEntityMetaClass = metadata.getClass(attribute.getCategory().getEntityType());
        final Map<String, Object> optionsMap = categorizedEntityMetaClass != null ?
                screensHelper.getAvailableScreens(categorizedEntityMetaClass.getJavaClass()) :
                Collections.<String, Object>emptyMap();

        targetScreensTable.addGeneratedColumn(
                "screen",
                new Table.ColumnGenerator<ScreenAndComponent>() {
                    @Override
                    public Component generateCell(ScreenAndComponent entity) {
                        final LookupField lookupField = componentsFactory.createComponent(LookupField.class);
                        lookupField.setDatasource(targetScreensTable.getItemDatasource(entity), "screen");
                        lookupField.setOptionsMap(optionsMap);
                        lookupField.setNewOptionAllowed(true);
                        lookupField.setNewOptionHandler(new LookupField.NewOptionHandler() {
                            @Override
                            public void addNewOption(String caption) {
                                optionsMap.put(caption, caption);
                                lookupField.setValue(caption);
                            }
                        });
                        lookupField.setRequired(true);
                        lookupField.setWidth("100%");
                        return lookupField;
                    }
                }
        );
        setupVisibility();
    }
}
