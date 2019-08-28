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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.filter.SecurityJpqlGenerator;
import com.haulmont.cuba.core.sys.xmlparsing.Dom4jTools;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.components.autocomplete.JpqlSuggestionFactory;
import com.haulmont.cuba.gui.components.autocomplete.Suggestion;
import com.haulmont.cuba.gui.components.data.options.MapOptions;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FakeFilterSupport;
import com.haulmont.cuba.gui.components.filter.FilterParser;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.edit.FilterEditor;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.sys.ScreensHelper;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.dom4j.Element;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

import static com.haulmont.cuba.core.app.dynamicattributes.PropertyType.*;
import static com.haulmont.cuba.core.entity.CategoryAttributeOptionsLoaderType.*;
import static java.lang.String.format;

/**
 * Class that encapsulates editing of {@link CategoryAttribute} entities.
 */
public class AttributeEditor extends AbstractEditor<CategoryAttribute> {

    protected static final Multimap<PropertyType, String> FIELDS_VISIBLE_FOR_TYPES = ArrayListMultimap.create();
    protected static final Set<PropertyType> SUPPORTED_OPTIONS_TYPES = ImmutableSet.of(STRING, DOUBLE, DECIMAL, INTEGER, ENTITY);

    protected static final String WHERE = " where ";

    static {
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.BOOLEAN, "defaultBoolean");
        FIELDS_VISIBLE_FOR_TYPES.put(STRING, "defaultString");
        FIELDS_VISIBLE_FOR_TYPES.put(STRING, "lookup");
        FIELDS_VISIBLE_FOR_TYPES.put(STRING, "isCollection");
        FIELDS_VISIBLE_FOR_TYPES.put(STRING, "width");
        FIELDS_VISIBLE_FOR_TYPES.put(STRING, "rowsCount");
        FIELDS_VISIBLE_FOR_TYPES.put(DOUBLE, "defaultDouble");
        FIELDS_VISIBLE_FOR_TYPES.put(DOUBLE, "minDouble");
        FIELDS_VISIBLE_FOR_TYPES.put(DOUBLE, "maxDouble");
        FIELDS_VISIBLE_FOR_TYPES.put(DOUBLE, "lookup");
        FIELDS_VISIBLE_FOR_TYPES.put(DOUBLE, "isCollection");
        FIELDS_VISIBLE_FOR_TYPES.put(DOUBLE, "width");
        FIELDS_VISIBLE_FOR_TYPES.put(DECIMAL, "defaultDecimal");
        FIELDS_VISIBLE_FOR_TYPES.put(DECIMAL, "minDecimal");
        FIELDS_VISIBLE_FOR_TYPES.put(DECIMAL, "maxDecimal");
        FIELDS_VISIBLE_FOR_TYPES.put(DECIMAL, "width");
        FIELDS_VISIBLE_FOR_TYPES.put(DECIMAL, "isCollection");
        FIELDS_VISIBLE_FOR_TYPES.put(DECIMAL, "numberFormatPattern");
        FIELDS_VISIBLE_FOR_TYPES.put(DECIMAL, "lookup");
        FIELDS_VISIBLE_FOR_TYPES.put(INTEGER, "defaultInt");
        FIELDS_VISIBLE_FOR_TYPES.put(INTEGER, "minInt");
        FIELDS_VISIBLE_FOR_TYPES.put(INTEGER, "maxInt");
        FIELDS_VISIBLE_FOR_TYPES.put(INTEGER, "lookup");
        FIELDS_VISIBLE_FOR_TYPES.put(INTEGER, "isCollection");
        FIELDS_VISIBLE_FOR_TYPES.put(INTEGER, "width");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.DATE, "defaultDate");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.DATE, "defaultDateIsCurrent");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.DATE, "isCollection");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.DATE, "width");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.DATE_WITHOUT_TIME, "defaultDateWithoutTime");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.DATE_WITHOUT_TIME, "defaultDateIsCurrent");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.DATE_WITHOUT_TIME, "width");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.DATE_WITHOUT_TIME, "isCollection");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.ENUMERATION, "enumeration");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.ENUMERATION, "defaultString");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.ENUMERATION, "width");
        FIELDS_VISIBLE_FOR_TYPES.put(PropertyType.ENUMERATION, "isCollection");
        FIELDS_VISIBLE_FOR_TYPES.put(ENTITY, "entityClass");
        FIELDS_VISIBLE_FOR_TYPES.put(ENTITY, "screen");
        FIELDS_VISIBLE_FOR_TYPES.put(ENTITY, "lookup");
        FIELDS_VISIBLE_FOR_TYPES.put(ENTITY, "defaultEntityId");
        FIELDS_VISIBLE_FOR_TYPES.put(ENTITY, "isCollection");
        FIELDS_VISIBLE_FOR_TYPES.put(ENTITY, "width");
    }

    @Inject
    protected Dom4jTools dom4JTools;

    @Inject
    protected FieldGroup attributeFieldGroup;
    @Inject
    protected FieldGroup optionalAttributeFieldGroup;
    @Inject
    protected FieldGroup calculatedAttrsAndOptionsFieldGroup;
    @Inject
    protected Table<ScreenAndComponent> targetScreensTable;
    @Inject
    protected TabSheet tabsheet;
    @Named("attributeFieldGroup.dataType")
    protected LookupField<PropertyType> dataType;
    @Named("optionalAttributeFieldGroup.entityClass")
    protected LookupField<String> entityClassField;
    @Named("optionalAttributeFieldGroup.screen")
    protected LookupField<String> screen;
    @Named("optionalAttributeFieldGroup.defaultBoolean")
    protected LookupField defaultBoolean;
    @Named("optionalAttributeFieldGroup.defaultDecimal")
    protected TextField defaultDecimal;
    @Named("optionalAttributeFieldGroup.defaultEntityId")
    protected PickerField<Entity> defaultEntityId;
    @Named("optionalAttributeFieldGroup.minDecimal")
    protected TextField minDecimal;
    @Named("optionalAttributeFieldGroup.maxDecimal")
    protected TextField maxDecimal;
    @Named("attributeFieldGroup.validatorGroovyScript")
    protected SourceCodeEditor validatorGroovyScript;
    @Named("calculatedAttrsAndOptionsFieldGroup.optionsLoaderType")
    protected LookupField<CategoryAttributeOptionsLoaderType> optionsLoaderType;
    @Named("calculatedAttrsAndOptionsFieldGroup.optionsLoaderScript")
    protected SourceCodeEditor optionsLoaderScript;
    @Named("calculatedAttrsAndOptionsFieldGroup.constraintWizard")
    protected HBoxLayout constraintWizardBox;
    @Named("calculatedAttrsAndOptionsFieldGroup.joinClause")
    protected SourceCodeEditor joinClause;
    @Named("calculatedAttrsAndOptionsFieldGroup.whereClause")
    protected SourceCodeEditor whereClause;
    @Named("calculatedAttrsAndOptionsFieldGroup.recalculationScript")
    protected SourceCodeEditor recalculationScript;
    protected ListEditor<String> enumerationListEditor;
    protected ListEditor<CategoryAttribute> dependsOnAttributesListEditor;
    protected LocalizedNameAndDescriptionFrame localizedFrame;

    @Inject
    protected Datasource<CategoryAttribute> attributeDs;
    @Inject
    protected Datasource<CategoryAttributeConfiguration> configurationDs;
    @Inject
    protected CollectionDatasource<ScreenAndComponent, UUID> screensDs;

    @Inject
    protected UiComponents uiComponents;
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
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected ReferenceToEntitySupport referenceToEntitySupport;
    @Inject
    protected GlobalConfig globalConfig;
    @Inject
    protected ClientConfig clientConfig;
    @Inject
    protected Icons icons;
    @Inject
    protected FilterParser filterParser;

    protected String fieldWidth;

    @Override
    public void init(Map<String, Object> params) {
        getDialogOptions().setWidth(themeConstants.get("cuba.gui.AttributeEditor.width"));
        fieldWidth = themeConstants.get("cuba.gui.AttributeEditor.field.width");

        tabsheet.addSelectedTabChangeListener(event -> {
            String tabName = event.getSelectedTab().getName();
            getDialogOptions().setWidth(themeConstants.get("cuba.gui.AttributeEditor.width"));
            if ("main".equals(tabName) && getItem().getDataType() != null) {
                getDialogOptions().setWidth(themeConstants.get("cuba.gui.AttributeEditor.twoColumnsWidth"));
            }
            getDialogOptions().center();
        });

        initLocalizedFrame();
        initAttributesFieldGroup();
        initCalculatedAttrsAndOptionsFieldGroup();
        initScreenTableActions();
    }

    @Override
    protected void postInit() {
        CategoryAttribute attribute = getItem();
        CategoryAttributeConfiguration configuration = attribute.getConfiguration();

        initScreensTable(attribute);

        if (attribute.getDataType() == ENTITY && Boolean.TRUE.equals(attribute.getLookup()) && configuration.getOptionsLoaderType() == null) {
            attribute.getConfiguration().setOptionsLoaderType(JPQL);
            ((DatasourceImplementation<CategoryAttribute>) attributeDs).modified(getItem());
        }

        String enumeration = attribute.getEnumeration();
        if (!Strings.isNullOrEmpty(enumeration)) {
            Iterable<String> items = Splitter.on(",").omitEmptyStrings().split(enumeration);
            enumerationListEditor.setValue(Lists.newArrayList(items));
        }

        if (localizedFrame != null) {
            localizedFrame.setNamesValue(attribute.getLocaleNames());
            localizedFrame.setDescriptionsValue(attribute.getLocaleDescriptions());
        }

        dependsOnAttributesListEditor.setOptionsList(getAttributesOptions());

        setupNumberFormat();
        changeAttributesUI();
        initDsListeners();

        getDialogOptions().center();
    }

    protected void initScreenTableActions() {
        Action createAction = new BaseAction("create")
                .withCaption(getMessage("targetScreensTable.create"))
                .withIcon(icons.get(CubaIcon.CREATE_ACTION))
                .withShortcut(clientConfig.getTableInsertShortcut())
                .withHandler(e ->
                        screensDs.addItem(metadata.create(ScreenAndComponent.class))
                );
        targetScreensTable.addAction(createAction);
        Action removeAction = new RemoveAction(targetScreensTable);
        removeAction.setCaption(getMessage("targetScreensTable.remove"));
        targetScreensTable.addAction(removeAction);
    }

    protected void initScreensTable(CategoryAttribute attribute) {
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
        Map<String, String> optionsMap = categorizedEntityMetaClass != null ?
                new HashMap<>(screensHelper.getAvailableScreens(categorizedEntityMetaClass.getJavaClass(), true)) :
                new HashMap<>();

        targetScreensTable.addGeneratedColumn(
                "screen",
                entity -> {
                    LookupField<String> lookupField = uiComponents.create(LookupField.NAME);
                    lookupField.setDatasource(targetScreensTable.getItemDatasource(entity), "screen");
                    lookupField.setOptionsMap(optionsMap);
                    //noinspection RedundantCast
                    lookupField.setNewOptionHandler((Consumer<String>) caption -> {
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
    }

    protected void initDsListeners() {
        attributeDs.addItemPropertyChangeListener(e -> {
            String property = e.getProperty();
            if ("dataType".equals(property)
                    || "lookup".equals(property)
                    || "defaultDateIsCurrent".equals(property)
                    || "entityClass".equals(property)) {
                changeAttributesUI();
                changeAttributeValues();
            }
            if (e.getPrevValue()==null && "dataType".equals(property)) {
                getDialogOptions().center();
            }
            if ("name".equals(property)) {
                setAttributeCodeValue();
            }
            if ("screen".equals(property) || "joinClause".equals(property) || "whereClause".equals(property)) {
                dynamicAttributesGuiTools.initEntityPickerField(defaultEntityId, e.getItem());
            }
        });
        configurationDs.addItemPropertyChangeListener(e -> {
            ((DatasourceImplementation<CategoryAttribute>) attributeDs).modified(getItem());
            if ("numberFormatPattern".equals(e.getProperty())) {
                setupNumberFormat();
            }
            if ("optionsLoaderType".equals(e.getProperty())) {
                changeAttributesUI();
                changeAttributeValues();
            }
        });
    }

    @SuppressWarnings("unchecked")
    protected void initAttributesFieldGroup() {
        defaultBoolean.setOptionsMap(getBooleanOptions());
        dataType.setOptionsMap(getTypeOptions());
        entityClassField.setOptionsMap(getEntityOptions());

        defaultEntityId.addValueChangeListener(e -> {
            Entity entity = e.getValue();
            if (entity != null) {
                getItem().setObjectDefaultEntityId(referenceToEntitySupport.getReferenceId(entity));
            } else {
                getItem().setObjectDefaultEntityId(null);
            }
            ((DatasourceImplementation<CategoryAttribute>) attributeDs).modified(getItem());
        });

        enumerationListEditor = uiComponents.create(ListEditor.NAME);
        enumerationListEditor.setWidth("100%");
        enumerationListEditor.setItemType(ListEditor.ItemType.STRING);
        enumerationListEditor.setRequired(true);
        enumerationListEditor.setRequiredMessage(getMessage("enumRequired"));
        enumerationListEditor.addValueChangeListener(e -> {
            List<?> list = e.getValue() != null ? e.getValue() : Collections.emptyList();
            getItem().setEnumeration(Joiner.on(",").join(list));
        });

        if (localizedFrame != null) {
            enumerationListEditor.setEditorWindowId("localizedEnumerationWindow");
            enumerationListEditor.setEditorParamsSupplier(() ->
                    ParamsMap.of("enumerationLocales", getItem().getEnumerationLocales()));
            enumerationListEditor.addEditorCloseListener(closeEvent -> {
                if (closeEvent.getActionId().equals(COMMIT_ACTION_ID)) {
                    LocalizedEnumerationWindow enumerationWindow = (LocalizedEnumerationWindow) closeEvent.getWindow();
                    getItem().setEnumerationLocales(enumerationWindow.getLocalizedValues());
                }
            });
        }

        optionalAttributeFieldGroup.getFieldNN("enumeration").setComponent(enumerationListEditor);

        validatorGroovyScript.setContextHelpIconClickHandler(e -> showMessageDialog(getMessage("validatorScript"), getMessage("validatorScriptHelp"),
                MessageType.CONFIRMATION_HTML.modal(false).width(560f)));
        validatorGroovyScript.setHeight(themeConstants.get("cuba.gui.AttributeEditor.validatorGroovyScriptField.height"));
    }

    protected void initCalculatedAttrsAndOptionsFieldGroup() {
        recalculationScript.setHeight(themeConstants.get("cuba.gui.AttributeEditor.recalculationGroovyScriptField.height"));
        recalculationScript.setContextHelpIconClickHandler(e ->
                showMessageDialog(getMessage("recalculationScript"), getMessage("recalculationScriptHelp"),
                        MessageType.CONFIRMATION_HTML
                                .modal(false)
                                .width(560f)));

        dependsOnAttributesListEditor = uiComponents.create(ListEditor.NAME);
        dependsOnAttributesListEditor.setValueSource(new DatasourceValueSource(configurationDs, "dependsOnAttributes"));
        dependsOnAttributesListEditor.setWidth(fieldWidth);
        dependsOnAttributesListEditor.setFrame(frame);
        dependsOnAttributesListEditor.setItemType(ListEditor.ItemType.ENTITY);
        dependsOnAttributesListEditor.setEntityName("sys$CategoryAttribute");
        dependsOnAttributesListEditor.addValidator(categoryAttributes -> {
            if (recalculationScript.getValue() != null && CollectionUtils.isEmpty(categoryAttributes)) {
                throw new ValidationException(getMessage("dependsOnAttributesValidationMsg"));
            }
        });

        calculatedAttrsAndOptionsFieldGroup.getFieldNN("dependsOnAttributes").setComponent(dependsOnAttributesListEditor);

        whereClause.setSuggester((source, text, cursorPosition) -> requestHint(whereClause, cursorPosition));
        whereClause.setHeight(themeConstants.get("cuba.gui.customConditionFrame.whereField.height"));

        joinClause.setSuggester((source, text, cursorPosition) -> requestHint(joinClause, cursorPosition));
        joinClause.setHeight(themeConstants.get("cuba.gui.customConditionFrame.joinField.height"));
    }

    protected void initLocalizedFrame() {
        if (globalConfig.getAvailableLocales().size() > 1) {
            tabsheet.getTab("localization").setVisible(true);

            localizedFrame = (LocalizedNameAndDescriptionFrame) openFrame(
                    tabsheet.getTabComponent("localization"), "localizedNameAndDescriptionFrame");
            localizedFrame.setWidth("100%");
            localizedFrame.setHeight("250px");
        }
    }

    protected void changeAttributesUI() {
        CategoryAttribute attribute = getItem();
        CategoryAttributeConfiguration configuration = attribute.getConfiguration();

        for (FieldGroup.FieldConfig fieldConfig : optionalAttributeFieldGroup.getFields()) {
            fieldConfig.setVisible(false);
        }

        Collection<String> fields = FIELDS_VISIBLE_FOR_TYPES.get(attribute.getDataType());
        if (fields != null && fields.size() > 0) {
            for (String componentId : fields) {
                optionalAttributeFieldGroup.getFieldNN(componentId).setVisible(true);
            }
            if ("main".equals(tabsheet.getSelectedTab().getName())) {
                getDialogOptions().setWidth(themeConstants.get("cuba.gui.AttributeEditor.twoColumnsWidth"));
                optionalAttributeFieldGroup.setVisible(true);
            }
        }
        if (attribute.getDataType() == ENTITY) {
            if (!Strings.isNullOrEmpty(attribute.getEntityClass())) {
                Class entityClass = attribute.getJavaClassForEntity();
                defaultEntityId.setEditable(true);
                defaultEntityId.setMetaClass(metadata.getClass(entityClass));
                dynamicAttributesGuiTools.initEntityPickerField(defaultEntityId, attribute);
                screen.setOptionsMap(screensHelper.getAvailableBrowserScreens(entityClass));
                setDefaultEntityFieldValue();
            } else {
                defaultEntityId.setEditable(false);
            }
            screen.setVisible(!Boolean.TRUE.equals(attribute.getLookup()));
        }

        if (attribute.getDataType() == PropertyType.DATE) {
            optionalAttributeFieldGroup.getFieldNN("defaultDate").setVisible(!Boolean.TRUE.equals(attribute.getDefaultDateIsCurrent()));
        }

        if (attribute.getDataType() == PropertyType.DATE_WITHOUT_TIME) {
            optionalAttributeFieldGroup.getFieldNN("defaultDateWithoutTime").setVisible(!Boolean.TRUE.equals(attribute.getDefaultDateIsCurrent()));
        }

        CategoryAttributeOptionsLoaderType optionsType = configuration.getOptionsLoaderType();

        boolean jpqlLoaderVisible = optionsType == JPQL;
        joinClause.setVisible(jpqlLoaderVisible);
        whereClause.setVisible(jpqlLoaderVisible);
        constraintWizardBox.setVisible(jpqlLoaderVisible);

        boolean scriptLoaderVisible = optionsType == SQL
                || optionsType == GROOVY;
        optionsLoaderScript.setVisible(scriptLoaderVisible);

        if (optionsType == GROOVY) {
            optionsLoaderScript.setContextHelpIconClickHandler(e -> showMessageDialog(getMessage("optionsLoaderGroovyScript"), getMessage("optionsLoaderGroovyScriptHelp"),
                    MessageType.CONFIRMATION_HTML.modal(false).width(560f)));
            optionsLoaderScript.setMode(SourceCodeEditor.Mode.Groovy);
        } else if (optionsType == SQL) {
            optionsLoaderScript.setMode(SourceCodeEditor.Mode.SQL);
        } else {
            optionsLoaderScript.setContextHelpIconClickHandler(null);
            optionsLoaderScript.setMode(SourceCodeEditor.Mode.Text);
        }

        optionsLoaderType.setEnabled(Boolean.TRUE.equals(attribute.getLookup()));
        optionsLoaderType.setRequired(Boolean.TRUE.equals(attribute.getLookup()));
        optionsLoaderType.setOptionsMap(getLoaderOptions());
    }

    protected void changeAttributeValues() {
        CategoryAttribute attribute = getItem();
        CategoryAttributeConfiguration configuration = attribute.getConfiguration();

        if (attribute.getDataType() == ENTITY) {
            if (!Strings.isNullOrEmpty(attribute.getEntityClass())) {
                Map<String, String> options = ((MapOptions<String>) screen.getOptions()).getItemsCollection();
                attribute.setScreen(options.containsValue(attribute.getScreen()) ? attribute.getScreen() : null);
            }
        }

        if (attribute.getDataType() == PropertyType.DATE) {
            if (Boolean.TRUE.equals(attribute.getDefaultDateIsCurrent())) {
                attribute.setDefaultDate(null);
            }
        }

        if (attribute.getDataType() == PropertyType.DATE_WITHOUT_TIME) {
            if (Boolean.TRUE.equals(attribute.getDefaultDateIsCurrent())) {
                attribute.setDefaultDateWithoutTime(null);
            }
        }

        if (attribute.getDataType() == PropertyType.BOOLEAN) {
            attribute.setIsCollection(null);
        }

        if (attribute.getDataType() == null || !SUPPORTED_OPTIONS_TYPES.contains(attribute.getDataType())) {
            attribute.setLookup(false);
        }

        if (!Boolean.TRUE.equals(attribute.getLookup())) {
            configuration.setOptionsLoaderType(null);
            configuration.setOptionsLoaderScript(null);
            attribute.setWhereClause(null);
            attribute.setJoinClause(null);
        } else {
            CategoryAttributeOptionsLoaderType optionsType = configuration.getOptionsLoaderType();
            if (optionsType == JPQL) {
                configuration.setOptionsLoaderScript(null);
            } else if (optionsType == GROOVY || optionsType == SQL) {
                attribute.setWhereClause(null);
                attribute.setJoinClause(null);
            } else if (optionsType == null) {
                configuration.setOptionsLoaderScript(null);
                attribute.setWhereClause(null);
                attribute.setJoinClause(null);
                if (attribute.getDataType() == ENTITY) {
                    configuration.setOptionsLoaderType(JPQL);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void setDefaultEntityFieldValue() {
        CategoryAttribute attribute = getItem();
        MetaClass metaClass = metadata.getClassNN(attribute.getJavaClassForEntity());
        if (attribute.getObjectDefaultEntityId() != null) {
            LoadContext<Entity> lc = new LoadContext<>(attribute.getJavaClassForEntity()).setView(View.MINIMAL);
            String pkName = referenceToEntitySupport.getPrimaryKeyForLoadingEntity(metaClass);
            lc.setQueryString(format("select e from %s e where e.%s = :entityId", metaClass.getName(), pkName))
                    .setParameter("entityId", attribute.getObjectDefaultEntityId());
            Entity entity = dataManager.load(lc);
            if (entity != null) {
                defaultEntityId.setValue(entity);
            } else {
                defaultEntityId.setValue(null);
            }
        }
    }

    protected void setAttributeCodeValue() {
        CategoryAttribute attribute = getItem();
        if (Strings.isNullOrEmpty(attribute.getCode()) && !Strings.isNullOrEmpty(attribute.getName())) {
            String categoryName = StringUtils.EMPTY;
            if (attribute.getCategory() != null) {
                categoryName = StringUtils.defaultString(attribute.getCategory().getName());
            }
            attribute.setCode(StringUtils.deleteWhitespace(categoryName + attribute.getName()));
        }
    }

    @Override
    public boolean preCommit() {
        CategoryAttribute attribute = getItem();
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
            attribute.setLocaleNames(localizedFrame.getNamesValue());
            attribute.setLocaleDescriptions(localizedFrame.getDescriptionsValue());
        }

        if (getDsContext().isModified()) {
            attribute.setAttributeConfigurationJson(new Gson().toJson(configurationDs.getItem()));
        }

        return true;
    }

    @Override
    public void postValidate(ValidationErrors errors) {
        CategoryAttribute attribute = getItem();
        if (attribute.getDataType() == INTEGER
                || attribute.getDataType() == DOUBLE
                || attribute.getDataType() == DECIMAL) {
            if (attribute.getConfiguration().getMinValue() != null &&
                    attribute.getConfiguration().getMaxValue() != null &&
                    compareNumbers(attribute.getDataType(),
                            attribute.getConfiguration().getMinValue(),
                            attribute.getConfiguration().getMaxValue()) > 0) {

                errors.add(getMessage("minGreaterThanMax"));

            } else if (attribute.getDefaultValue() != null) {
                if (attribute.getConfiguration().getMinValue() != null &&
                        compareNumbers(attribute.getDataType(), attribute.getConfiguration().getMinValue(),
                                (Number) attribute.getDefaultValue()) > 0) {

                    errors.add(getMessage("defaultLessThanMin"));
                }

                if (attribute.getConfiguration().getMaxValue() != null &&
                        compareNumbers(attribute.getDataType(),
                                attribute.getConfiguration().getMaxValue(), (Number) attribute.getDefaultValue()) < 0) {

                    errors.add(getMessage("defaultGreaterThanMax"));
                }
            }
        }

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

    protected List<Suggestion> requestHint(SourceCodeEditor sender, int senderCursorPosition) {
        String joinStr = joinClause.getValue();
        String whereStr = whereClause.getValue();

        // CAUTION: the magic entity name!  The length is three character to match "{E}" length in query
        String entityAlias = "a39";

        int queryPosition = -1;
        Class javaClassForEntity = getItem().getJavaClassForEntity();
        if (javaClassForEntity == null) {
            return new ArrayList<>();
        }

        String queryStart = format("select %s from %s %s ", entityAlias, metadata.getClassNN(javaClassForEntity), entityAlias);

        StringBuilder queryBuilder = new StringBuilder(queryStart);
        if (StringUtils.isNotEmpty(joinStr)) {
            if (sender == joinClause) {
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
            if (sender == whereClause) {
                queryPosition = queryBuilder.length() + WHERE.length() + senderCursorPosition - 1;
            }
            queryBuilder.append(WHERE).append(whereStr);
        }
        String query = queryBuilder.toString();
        query = query.replace("{E}", entityAlias);

        return JpqlSuggestionFactory.requestHint(query, queryPosition, sender.getAutoCompleteSupport(), senderCursorPosition);
    }

    private int compareNumbers(PropertyType type, Number first, Number second) {
        if (type == INTEGER) {
            return Integer.compare((Integer) first, (Integer) second);
        } else if (type == DOUBLE) {
            return Double.compare((Double) first, (Double) second);
        } else if (type == DECIMAL) {
            return ((BigDecimal) first).compareTo((BigDecimal) second);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("unchecked")
    protected void setupNumberFormat() {
        Datatype datatype = dynamicAttributesGuiTools.getCustomNumberDatatype(getItem());
        if (datatype != null) {
            defaultDecimal.setDatatype(datatype);
            minDecimal.setDatatype(datatype);
            maxDecimal.setDatatype(datatype);

            defaultDecimal.setValue(defaultDecimal.getValue());
            minDecimal.setValue(minDecimal.getValue());
            maxDecimal.setValue(maxDecimal.getValue());
        }
    }

    public void openConstraintWizard() {
        CategoryAttribute attribute = getItem();
        Class entityClass = attribute.getJavaClassForEntity();

        if (entityClass == null) {
            showNotification(getMessage("selectEntityType"));
            return;
        }
        MetaClass metaClass = metadata.getClassNN(entityClass);

        FakeFilterSupport filterSupport = new FakeFilterSupport(this, metaClass);
        Filter fakeFilter = filterSupport.createFakeFilter();
        FilterEntity filterEntity = filterSupport.createFakeFilterEntity(attribute.getFilterXml());
        ConditionsTree conditionsTree = filterSupport.createFakeConditionsTree(fakeFilter, filterEntity);

        Map<String, Object> params = new HashMap<>();
        params.put("filter", fakeFilter);
        params.put("filterEntity", filterEntity);
        params.put("conditionsTree", conditionsTree);
        params.put("useShortConditionForm", true);

        FilterEditor filterEditor = (FilterEditor) openWindow("filterEditor", OpenType.DIALOG, params);
        filterEditor.addCloseListener(actionId -> {
            if (!COMMIT_ACTION_ID.equals(actionId)) {
                return;
            }

            filterEntity.setXml(filterParser.getXml(filterEditor.getConditions(), Param.ValueProperty.DEFAULT_VALUE));

            if (filterEntity.getXml() != null) {
                Element element = dom4JTools.readDocument(filterEntity.getXml()).getRootElement();
                com.haulmont.cuba.core.global.filter.FilterParser filterParser =
                        new com.haulmont.cuba.core.global.filter.FilterParser(element);
                String jpql = new SecurityJpqlGenerator().generateJpql(filterParser.getRoot());
                attribute.setWhereClause(jpql);
                Set<String> joins = filterParser.getRoot().getJoins();
                if (!joins.isEmpty()) {
                    String joinsStr = new TextStringBuilder().appendWithSeparators(joins, " ").toString();
                    attribute.setJoinClause(joinsStr);
                }
                attribute.setFilterXml(filterEntity.getXml());
            }
        });
    }

    protected List<CategoryAttribute> getAttributesOptions() {
        List<CategoryAttribute> optionsList;
        if (getItem().getCategory().getCategoryAttrs() != null) {
            optionsList = new ArrayList<>(getItem().getCategory().getCategoryAttrs());
            optionsList.remove(getItem());
        } else {
            optionsList = new ArrayList<>();
        }
        return optionsList;
    }

    protected Map<String, Boolean> getBooleanOptions() {
        Map<String, Boolean> booleanOptions = new TreeMap<>();
        booleanOptions.put(datatypeFormatter.formatBoolean(true), Boolean.TRUE);
        booleanOptions.put(datatypeFormatter.formatBoolean(false), Boolean.FALSE);
        return booleanOptions;
    }

    protected Map<String, PropertyType> getTypeOptions() {
        Map<String, PropertyType> options = new TreeMap<>();
        PropertyType[] types = PropertyType.values();
        for (PropertyType propertyType : types) {
            options.put(getMessage(propertyType.toString()), propertyType);
        }
        return options;
    }

    protected Map<String, CategoryAttributeOptionsLoaderType> getLoaderOptions() {
        CategoryAttribute attribute = getItem();
        Map<String, CategoryAttributeOptionsLoaderType> options = new TreeMap<>();
        for (CategoryAttributeOptionsLoaderType type : CategoryAttributeOptionsLoaderType.values()) {
            if (attribute.getDataType() != ENTITY && type == JPQL) {
                continue;
            }
            if (attribute.getDataType() == ENTITY && type == SQL) {
                continue;
            }
            options.put(messages.getMessage(type), type);
        }
        return options;
    }

    protected Map<String, String> getEntityOptions() {
        Map<String, String> options = new TreeMap<>();
        for (MetaClass metaClass : metadataTools.getAllPersistentMetaClasses()) {
            if (!metadataTools.isSystemLevel(metaClass)) {
                if (metadata.getTools().hasCompositePrimaryKey(metaClass) && !HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
                    continue;
                }
                options.put(messageTools.getDetailedEntityCaption(metaClass), metaClass.getJavaClass().getName());
            }
        }
        return options;
    }
}