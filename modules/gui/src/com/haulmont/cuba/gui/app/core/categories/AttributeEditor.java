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

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.HasUuid;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.filter.SecurityJpqlGenerator;
import com.haulmont.cuba.gui.ScreensHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.components.autocomplete.JpqlSuggestionFactory;
import com.haulmont.cuba.gui.components.autocomplete.Suggestion;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FakeFilterSupport;
import com.haulmont.cuba.gui.components.filter.FilterParser;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.edit.FilterEditor;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.AbstractDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.dom4j.Element;

import javax.inject.Inject;
import java.util.*;

import static java.lang.String.format;

/**
 * Class that encapsulates editing of {@link CategoryAttribute} entities.
 * <p>
 */
public class AttributeEditor extends AbstractEditor<CategoryAttribute> {
    protected static final Multimap<PropertyType, String> FIELDS_VISIBLE_FOR_DATATYPES = ArrayListMultimap.create();
    protected static final Set<String> ALWAYS_VISIBLE_FIELDS = Sets.newHashSet("name", "code", "required", "dataType");
    protected static final String WHERE = " where ";

    static {
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.BOOLEAN, "defaultBoolean");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.STRING, "defaultString");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.STRING, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.STRING, "rowsCount");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.STRING, "isCollection");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DOUBLE, "defaultDouble");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DOUBLE, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DOUBLE, "isCollection");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.INTEGER, "defaultInt");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.INTEGER, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.INTEGER, "isCollection");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DATE, "defaultDate");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DATE, "defaultDateIsCurrent");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DATE, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.DATE, "isCollection");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENUMERATION, "enumeration");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENUMERATION, "defaultString");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENUMERATION, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "entityClass");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "screen");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "lookup");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "defaultEntityId");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "width");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "joinClause");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "whereClause");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "constraintWizard");
        FIELDS_VISIBLE_FOR_DATATYPES.put(PropertyType.ENTITY, "isCollection");
    }

    protected CategoryAttribute attribute;

    @Inject
    protected FieldGroup attributeFieldGroup;
    protected LookupField dataTypeField;
    protected LookupField screenField;
    protected LookupField entityTypeField;
    protected PickerField defaultEntityField;
    protected PickerField.LookupAction entityLookupAction;
    protected String fieldWidth;

    @Inject
    protected Datasource<CategoryAttribute> attributeDs;

    @Inject
    protected ComponentsFactory factory;

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

    @Inject
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected ReferenceToEntitySupport referenceToEntitySupport;

    @Inject
    protected Table targetScreensTable;

    @Inject
    protected TabSheet tabsheet;

    @Inject
    protected CollectionDatasource<ScreenAndComponent, UUID> screensDs;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected ClientConfig clientConfig;

    @Inject
    protected Icons icons;

    protected LocalizedNameFrame localizedFrame;

    private ListEditor enumerationListEditor;
    private SourceCodeEditor joinField;
    private SourceCodeEditor whereField;

    @Override
    public void init(Map<String, Object> params) {
        getDialogOptions().setWidth(themeConstants.get("cuba.gui.AttributeEditor.width"));

        fieldWidth = themeConstants.get("cuba.gui.AttributeEditor.field.width");

        initLocalizedFrame();
        initFieldGroup();

        Action createAction = initCreateScreenAndComponentAction();
        targetScreensTable.addAction(createAction);
        Action removeAction = new RemoveAction(targetScreensTable);
        removeAction.setCaption(getMessage("targetScreensTable.remove"));
        targetScreensTable.addAction(removeAction);
    }

    protected Action initCreateScreenAndComponentAction() {
        Action createAction = new BaseAction("create") {
            @Override
            public void actionPerform(Component component) {
                screensDs.addItem(new ScreenAndComponent());
            }
        };
        createAction.setCaption(getMessage("targetScreensTable.create"));
        String icon = icons.get(CubaIcon.CREATE_ACTION);
        createAction.setIcon(icon);
        createAction.setShortcut(clientConfig.getTableInsertShortcut());
        return createAction;
    }

    protected void initLocalizedFrame() {
        if (globalConfig.getAvailableLocales().size() > 1) {
            tabsheet.getTab("localization").setVisible(true);
            localizedFrame = (LocalizedNameFrame) openFrame(
                    tabsheet.getTabComponent("localization"), "localizedNameFrame");
            localizedFrame.setWidth("100%");
            localizedFrame.setHeight("250px");
        }
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
                        if (metadata.getTools().hasCompositePrimaryKey(metaClass) && !HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
                            continue;
                        }
                        options.put(messageTools.getDetailedEntityCaption(metaClass), metaClass.getJavaClass().getName());
                        if (attribute != null && metaClass.getJavaClass().getName().equals(attribute.getEntityClass())) {
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
            defaultEntityField.setCaption(messages.getMessage(CategoryAttribute.class, "CategoryAttribute.defaultEntityId"));
            defaultEntityField.addValueChangeListener(e -> {
                Entity entity = (Entity) e.getValue();
                if (entity != null) {
                    attribute.setObjectDefaultEntityId(referenceToEntitySupport.getReferenceId(entity));
                } else {
                    attribute.setObjectDefaultEntityId(null);
                }
                ((AbstractDatasource) attributeDs).modified(attribute);
            });
            entityLookupAction = defaultEntityField.addLookupAction();
            defaultEntityField.addClearAction();

            return defaultEntityField;
        });

        attributeFieldGroup.addCustomField("enumeration", (datasource, propertyId) -> {
            enumerationListEditor = factory.createComponent(ListEditor.class);
            enumerationListEditor.setWidth("100%");
            enumerationListEditor.setItemType(ListEditor.ItemType.STRING);
            enumerationListEditor.setRequired(true);
            enumerationListEditor.setRequiredMessage(getMessage("enumRequired"));
            enumerationListEditor.addValueChangeListener(e -> {
                List<String> value = (List<String>) e.getValue();
                attribute.setEnumeration(Joiner.on(",").join(value));
            });
            if (localizedFrame != null) {
                enumerationListEditor.setEditorWindowId("localizedEnumerationWindow");
                enumerationListEditor.setEditorParamsSupplier(() ->
                        ParamsMap.of("enumerationLocales", attribute.getEnumerationLocales()));
                enumerationListEditor.addEditorCloseListener(closeEvent -> {
                    if (closeEvent.getActionId().equals(COMMIT_ACTION_ID)) {
                        LocalizedEnumerationWindow enumerationWindow = (LocalizedEnumerationWindow) closeEvent.getWindow();
                        attribute.setEnumerationLocales(enumerationWindow.getLocalizedValues());
                    }
                });
            }
            return enumerationListEditor;
        });

        attributeFieldGroup.addCustomField("whereClause", (datasource, propertyId) -> {
            whereField = factory.createComponent(SourceCodeEditor.class);
            whereField.setDatasource(attributeDs, "whereClause");
            whereField.setWidth("100%");
            whereField.setHeight(themeConstants.get("cuba.gui.customConditionFrame.whereField.height"));
            whereField.setSuggester((source, text, cursorPosition) -> requestHint(whereField, text, cursorPosition));
            whereField.setHighlightActiveLine(false);
            whereField.setShowGutter(false);
            return whereField;
        });

        attributeFieldGroup.addCustomField("joinClause", (datasource, propertyId) -> {
            joinField = factory.createComponent(SourceCodeEditor.class);
            joinField.setDatasource(attributeDs, "joinClause");
            joinField.setWidth("100%");
            joinField.setHeight(themeConstants.get("cuba.gui.customConditionFrame.joinField.height"));
            joinField.setSuggester((source, text, cursorPosition) -> requestHint(joinField, text, cursorPosition));
            joinField.setHighlightActiveLine(false);
            joinField.setShowGutter(false);
            return joinField;
        });

        attributeFieldGroup.addCustomField("constraintWizard", (datasource, propertyId) -> {
            HBoxLayout hbox = factory.createComponent(HBoxLayout.class);
            hbox.setWidth("100%");
            LinkButton linkButton = factory.createComponent(LinkButton.class);
            linkButton.setAction(new BaseAction("constraintWizard")
                    .withHandler(event ->
                            openConstraintWizard()
                    ));

            linkButton.setCaption(getMessage("constraintWizard"));
            linkButton.setAlignment(Alignment.MIDDLE_LEFT);
            hbox.add(linkButton);
            return hbox;
        });

        attributeDs.addItemPropertyChangeListener(e -> {
            String property = e.getProperty();
            CategoryAttribute attribute = getItem();
            if ("dataType".equalsIgnoreCase(property)
                    || "lookup".equalsIgnoreCase(property)
                    || "defaultDateIsCurrent".equalsIgnoreCase(property)
                    || "entityClass".equalsIgnoreCase(property)) {
                setupVisibility();
            }
            if ("name".equalsIgnoreCase(property)) {
                fillAttributeCode();
            }
            if ("screen".equalsIgnoreCase(property) || "joinClause".equals(property) || "whereClause".equals(property)) {
                dynamicAttributesGuiTools.initEntityPickerField(defaultEntityField, attribute);
            }
        });
    }

    public void openConstraintWizard() {
        Class entityClass = attribute.getJavaClassForEntity();
        if (entityClass == null) {
            showNotification(getMessage("selectEntityType"));
            return;
        }
        MetaClass metaClass = metadata.getClassNN(entityClass);
        FakeFilterSupport fakeFilterSupport = new FakeFilterSupport(this, metaClass);

        final Filter fakeFilter = fakeFilterSupport.createFakeFilter();
        final FilterEntity filterEntity = fakeFilterSupport.createFakeFilterEntity(attribute.getFilterXml());
        final ConditionsTree conditionsTree = fakeFilterSupport.createFakeConditionsTree(fakeFilter, filterEntity);

        Map<String, Object> params = new HashMap<>();
        params.put("filter", fakeFilter);
        params.put("filterEntity", filterEntity);
        params.put("conditions", conditionsTree);
        params.put("useShortConditionForm", true);

        FilterEditor filterEditor = (FilterEditor) openWindow("filterEditor", WindowManager.OpenType.DIALOG, params);
        filterEditor.addCloseListener(actionId -> {
            if (!COMMIT_ACTION_ID.equals(actionId)) return;
            FilterParser filterParser1 = AppBeans.get(FilterParser.class);
            filterEntity.setXml(filterParser1.getXml(filterEditor.getConditions(), Param.ValueProperty.DEFAULT_VALUE));
            if (filterEntity.getXml() != null) {
                Element element = Dom4j.readDocument(filterEntity.getXml()).getRootElement();
                com.haulmont.cuba.core.global.filter.FilterParser filterParser = new com.haulmont.cuba.core.global.filter.FilterParser(element);
                String jpql = new SecurityJpqlGenerator().generateJpql(filterParser.getRoot());
                attribute.setWhereClause(jpql);
                Set<String> joins = filterParser.getRoot().getJoins();
                if (!joins.isEmpty()) {
                    String joinsStr = new StrBuilder().appendWithSeparators(joins, " ").toString();
                    attribute.setJoinClause(joinsStr);
                }
                attribute.setFilterXml(filterEntity.getXml());
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
                attributeFieldGroup.setVisible(componentId, true);
            }
        }

        if (attribute.getDataType() == PropertyType.ENTITY) {
            if (StringUtils.isNotBlank(attribute.getEntityClass())) {
                defaultEntityField.setEditable(true);
                whereField.setEnabled(true);
                joinField.setEnabled(true);
                Class entityClass = attribute.getJavaClassForEntity();
                MetaClass metaClass = metadata.getClass(entityClass);
                defaultEntityField.setMetaClass(metaClass);
                fillDefaultEntities(entityClass);
                fillSelectEntityScreens(entityClass);

                dynamicAttributesGuiTools.initEntityPickerField(defaultEntityField, attribute);
            } else {
                defaultEntityField.setEditable(false);
                whereField.setEnabled(false);
                joinField.setEnabled(false);
            }

            if (Boolean.TRUE.equals(attribute.getLookup())) {
                attributeFieldGroup.setVisible("screen", false);
            } else {
                attributeFieldGroup.setVisible("screen", true);
            }

            getDialogOptions().center();
        }

        if (attribute.getDataType() == PropertyType.DATE) {
            if (Boolean.TRUE.equals(attribute.getDefaultDateIsCurrent())) {
                attributeFieldGroup.setVisible("defaultDate", false);
                attributeFieldGroup.setFieldValue("defaultDate", null);
            } else {
                attributeFieldGroup.setVisible("defaultDate", true);
            }
        }

        if (attribute.getDataType() == PropertyType.BOOLEAN ||
                attribute.getDataType() == PropertyType.ENUMERATION) {
            attributeFieldGroup.setFieldValue("isCollection", null);
        }
    }

    protected void fillSelectEntityScreens(Class entityClass) {
        String value = attribute.getScreen();
        Map<String, Object> screensMap = screensHelper.getAvailableBrowserScreens(entityClass);
        screenField.setValue(null);             // While #PL-4731 unfixed
        screenField.setOptionsMap(screensMap);
        screenField.setValue(screensMap.containsValue(value) ? value : null);
    }

    @SuppressWarnings("unchecked")
    protected void fillDefaultEntities(Class entityClass) {
        MetaClass metaClass = metadata.getClassNN(entityClass);
        if (attribute.getObjectDefaultEntityId() != null) {
            LoadContext<Entity> lc = new LoadContext<>(entityClass).setView(View.MINIMAL);
            String pkName = referenceToEntitySupport.getPrimaryKeyForLoadingEntity(metaClass);
            lc.setQueryString(format("select e from %s e where e.%s = :entityId", metaClass.getName(), pkName))
                    .setParameter("entityId", attribute.getObjectDefaultEntityId());
            Entity entity = dataManager.load(lc);
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

        if (localizedFrame != null) {
            attribute.setLocaleNames(localizedFrame.getValue());
        }

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
                new HashMap<>(screensHelper.getAvailableScreens(categorizedEntityMetaClass.getJavaClass())) :
                Collections.emptyMap();

        targetScreensTable.addGeneratedColumn(
                "screen",
                entity -> {
                    final LookupField lookupField = componentsFactory.createComponent(LookupField.class);
                    lookupField.setDatasource(targetScreensTable.getItemDatasource(entity), "screen");
                    lookupField.setOptionsMap(optionsMap);
                    lookupField.setNewOptionAllowed(true);
                    lookupField.setNewOptionHandler(caption -> {
                        if (caption != null && !optionsMap.containsKey(caption)) {
                            optionsMap.put(caption, caption);
                            lookupField.setValue(caption);
                        }
                    });
                    lookupField.setRequired(true);
                    lookupField.setWidth("100%");
                    return lookupField;
                }
        );

        String enumeration = attribute.getEnumeration();
        if (!Strings.isNullOrEmpty(enumeration)) {
            Iterable<String> items = Splitter.on(",").omitEmptyStrings().split(enumeration);
            enumerationListEditor.setValue(Lists.newArrayList(items));
        }

        if (localizedFrame != null) {
            localizedFrame.setValue(attribute.getLocaleNames());
        }

        setupVisibility();
    }

    protected List<Suggestion> requestHint(SourceCodeEditor sender, String text, int senderCursorPosition) {
        String joinStr = joinField.getValue();
        String whereStr = whereField.getValue();

        // CAUTION: the magic entity name!  The length is three character to match "{E}" length in query
        String entityAlias = "a39";

        int queryPosition = -1;
        Class javaClassForEntity = attribute.getJavaClassForEntity();
        if (javaClassForEntity == null) return new ArrayList<>();

        MetaClass metaClass = metadata.getClassNN(javaClassForEntity);
        String queryStart = "select " + entityAlias + " from " + metaClass.getName() + " " + entityAlias + " ";

        StringBuilder queryBuilder = new StringBuilder(queryStart);
        if (StringUtils.isNotEmpty(joinStr)) {
            if (sender == joinField) {
                queryPosition = queryBuilder.length() + senderCursorPosition - 1;
            }
            if (!StringUtils.containsIgnoreCase(joinStr, "join") && !StringUtils.contains(joinStr, ",")) {
                queryBuilder.append("join ").append(joinStr);
                queryPosition += "join ".length();
            } else {
                queryBuilder.append(joinStr);
            }
        }
        if (StringUtils.isNotEmpty(whereStr)) {
            if (sender == whereField) {
                queryPosition = queryBuilder.length() + WHERE.length() + senderCursorPosition - 1;
            }
            queryBuilder.append(WHERE).append(whereStr);
        }
        String query = queryBuilder.toString();
        query = query.replace("{E}", entityAlias);

        return JpqlSuggestionFactory.requestHint(query, queryPosition, sender.getAutoCompleteSupport(), senderCursorPosition);
    }
}
