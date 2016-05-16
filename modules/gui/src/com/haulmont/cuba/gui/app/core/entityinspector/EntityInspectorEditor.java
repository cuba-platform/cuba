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

package com.haulmont.cuba.gui.app.core.entityinspector;

import com.haulmont.chile.core.datatypes.impl.ByteArrayDatatype;
import com.haulmont.chile.core.datatypes.impl.UUIDDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Categorized;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.AddAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.*;

/**
 */
public class EntityInspectorEditor extends AbstractWindow {

    public static final int CAPTION_MAX_LENGTH = 100;
    public static final int MAX_TEXT_LENGTH = 50;

    public static final WindowManager.OpenType OPEN_TYPE = WindowManager.OpenType.THIS_TAB;
    public static final int MAX_TEXTFIELD_STRING_LENGTH = 255;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected Security security;

    @Inject
    protected DataSupplier dataSupplier;

    @Inject
    protected BoxLayout buttonsBox;

    @Inject
    protected BoxLayout contentPane;

    @Inject
    protected BoxLayout runtimePane;

    @Inject
    protected BoxLayout tablesBox;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected Configuration configuration;

    @Inject
    protected ThemeConstants themeConstants;

    @WindowParam(name = "item")
    protected Entity item;

    @WindowParam(name = "parent")
    protected Entity parent;

    @WindowParam(name = "parentProperty")
    protected String parentProperty;

    @WindowParam(name = "parentDs")
    protected Datasource parentDs;

    @WindowParam(name = "datasource")
    protected Datasource datasource;

    protected MetaClass meta;
    protected DsContextImpl dsContext;
    protected Map<String, Datasource> datasources;

    protected Boolean isNew;
    protected Boolean autocommit;
    protected Boolean showSystemFields;
    protected Collection<Table> tables;

    protected Collection<Field> reserveLineSeparatorFields;
    protected RuntimePropsDatasource rDS;
    protected CollectionDatasource categoriesDs;

    protected ButtonsPanel buttonsPanel;
    protected Button commitButton;
    protected Button cancelButton;
    protected FieldGroup focusFieldGroup;
    protected String focusFieldId;

    public EntityInspectorEditor() {
        datasources = new HashMap<>();
        tables = new LinkedList<>();
        isNew = true;
        autocommit = true;
        showSystemFields = false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(Map<String, Object> params) {
        isNew = item == null || PersistenceHelper.isNew(item);
        meta = item != null ? item.getMetaClass() : metadata.getSession().getClass((String) params.get("metaClass"));
        autocommit = params.get("autocommit") != null ? (Boolean) params.get("autocommit") : true;
        showSystemFields = params.get("showSystemFields") != null ? (Boolean) params.get("showSystemFields") : false;

        if (meta == null)
            throw new IllegalStateException("Entity or entity's MetaClass must be specified");

        setCaption(meta.getName());
        initShortcuts();

        View view = createView(meta);

        dsContext = new DsContextImpl(dataSupplier);
        dsContext.setFrameContext(getDsContext().getFrameContext());
        setDsContext(dsContext);

        boolean createRequest = item == null || item.getId() == null;
        if (createRequest) {
            item = metadata.create(meta);
            setParentField(item, parentProperty, parent);
        } else {
            //edit request
            if (!isNew)
                item = loadSingleItem(meta, item.getId(), view);
        }
        createEmbeddedFields(meta, item);

        boolean categorizedEntity = item instanceof Categorized;

        if (datasource == null) {
            datasource = new DatasourceImpl<>();
            datasource.setup(dsContext, dataSupplier, meta.getName() + "Ds", item.getMetaClass(), view);
            ((DatasourceImpl) datasource).setParent(parentDs);
            ((DatasourceImpl) datasource).valid();
        }

        dsContext.register(datasource);
        createPropertyDatasources(datasource);
        if (categorizedEntity) {
            initRuntimePropertiesDatasources(view);
        }

        datasource.refresh();

        reserveLineSeparatorFields = new LinkedList<>();
        createDataComponents(meta, item);
        if (categorizedEntity) {
            createRuntimeDataComponents();
        }

        datasource.setItem(item);

        if (categorizedEntity) {
            rDS.refresh();
        }

        createCommitButtons();
        setCaption(meta.getName());

        if (focusFieldGroup != null && focusFieldId != null) {
            focusFieldGroup.requestFocus(focusFieldId);
        }
    }

    public Entity getItem() {
        return datasource.getItem();
    }

    private void initShortcuts() {
        final String commitShortcut =  configuration.getConfig(ClientConfig.class).getCommitShortcut();
        Action commitAction = new AbstractAction("commitAndClose", commitShortcut) {
            @Override
            public void actionPerform(Component component) {
                commitAndClose();
            }
        };
        addAction(commitAction);
    }

    private void setParentField(Entity item, String parentProperty, Entity parent) {
        if (parentProperty != null && parent != null && item != null)
            item.setValue(parentProperty, parent);
    }

    private void createRuntimeDataComponents() {
        if (rDS != null && categoriesDs != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("runtimeDs", rDS.getId());
            params.put("categoriesDs", categoriesDs.getId());
            params.put("fieldWidth", themeConstants.get("cuba.gui.EntityInspectorEditor.field.width"));
            params.put("borderVisible", Boolean.TRUE);

            RuntimePropertiesFrame runtimePropertiesFrame = (RuntimePropertiesFrame) openFrame(runtimePane, "runtimePropertiesFrame", params);
            runtimePropertiesFrame.setFrame(this.getFrame());
            runtimePropertiesFrame.setMessagesPack("com.haulmont.cuba.gui.app.core.entityinspector");
            runtimePropertiesFrame.setCategoryFieldVisible(false);

            runtimePropertiesFrame.setHeight(Component.AUTO_SIZE);
            runtimePropertiesFrame.setWidth("100%");

            runtimePane.add(runtimePropertiesFrame);
        }
    }

    private void initRuntimePropertiesDatasources(View view) {
        rDS = new RuntimePropsDatasourceImpl(dsContext, dataSupplier, "rDS", datasource.getId(), null);
        MetaClass categoriesMeta = metadata.getSession().getClass(Category.class);
        categoriesDs = new CollectionDatasourceImpl();
        ViewProperty categoryProperty = view.getProperty("category");
        if (categoryProperty == null) {
            throw new IllegalArgumentException("Category property not found. Not a categorized entity?");
        }
        categoriesDs.setup(dsContext, dataSupplier, "categoriesDs", categoriesMeta, categoryProperty.getView());
        categoriesDs.setQuery(String.format("select c from sys$Category c where c.entityType='%s'", meta.getName()));
        categoriesDs.refresh();
        dsContext.register(rDS);
        dsContext.register(categoriesDs);
    }

    /**
     * Recursively instantiates the embedded properties.
     * E.g. embedded properties of the embedded property will also be instantiated.
     *
     * @param metaClass meta class of the entity
     * @param item      entity instance
     */
    private void createEmbeddedFields(MetaClass metaClass, Entity item) {
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (isEmbedded(metaProperty)) {
                MetaClass embeddedMetaClass = metaProperty.getRange().asClass();
                Entity embedded = item.getValue(metaProperty.getName());
                if (embedded == null) {
                    embedded = metadata.create(embeddedMetaClass);
                    item.setValue(metaProperty.getName(), embedded);
                }
                createEmbeddedFields(embeddedMetaClass, embedded);
            }
        }
    }

    /**
     * Returns metaProperty of the referred entity annotated with either nullIndicatorAttributeName or
     * nullIndicatorColumnName property.
     *
     * @param embeddedMetaProperty embedded property of the current entity
     * @return property of the referred entity
     */
    private MetaProperty getNullIndicatorProperty(MetaProperty embeddedMetaProperty) {
//        EmbeddedMapping embeddedMapping = embeddedMetaProperty.getAnnotatedElement().getAnnotation(EmbeddedMapping.class);

//        if (embeddedMapping == null)
            return null;

//        MetaClass meta = embeddedMetaProperty.getRange().asClass();
//
//        String attributeName = embeddedMapping.nullIndicatorAttributeName();
//        String columnName = embeddedMapping.nullIndicatorColumnName();
//
//        if (!isEmpty(attributeName))
//            return meta.getProperty(attributeName);
//        else if (!isEmpty(columnName))
//            return findPropertyByMappedColumn(meta, columnName);
//        else
//            return null;
    }

    /**
     * Checks if the property is embedded
     *
     * @param metaProperty meta property
     * @return true if embedded, false otherwise
     */
    private boolean isEmbedded(MetaProperty metaProperty) {
        return metaProperty.getAnnotatedElement().isAnnotationPresent(javax.persistence.Embedded.class);
    }

    /**
     * Loads single item by id
     *
     * @param meta item's meta class
     * @param id   item's id
     * @param view view
     * @return loaded item if found, null otherwise
     */
    private Entity loadSingleItem(MetaClass meta, Object id, View view) {
        LoadContext ctx = new LoadContext(meta);
        ctx.setLoadDynamicAttributes(true);
        ctx.setView(view);
        String primaryKeyName = metadata.getTools().getPrimaryKeyName(meta);
        if (primaryKeyName == null)
            throw new IllegalStateException("Entity " + meta.getName() + " has no primary key");
        String query = String.format("select e from %s e where e.%s = :id", meta.getName(), primaryKeyName);
        LoadContext.Query q = ctx.setQueryString(query);
        q.setParameter("id", id);
        return dataSupplier.load(ctx);
    }

    /**
     * Creates components representing item data
     * (fieldGroup, fieldGroups for embedded properties, tables for the referred entities)
     *
     * @param metaClass item meta class
     */
    private void createDataComponents(MetaClass metaClass, Entity item) {
        FieldGroup fieldGroup = componentsFactory.createComponent(FieldGroup.class);
        fieldGroup.setWidth("100%");
        LinkedList<FieldGroup.FieldConfig> customFields = new LinkedList<>();

        contentPane.add(fieldGroup);
        fieldGroup.setFrame(frame);
        MetadataTools tools = metadata.getTools();
        MetaProperty primaryKeyProperty = tools.getPrimaryKeyProperty(metaClass);
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            boolean isRequired = isRequired(metaProperty);
            boolean isReadonly = metaProperty.isReadOnly();
            switch (metaProperty.getType()) {
                case DATATYPE:
                case ENUM:
                    boolean includeId = primaryKeyProperty.equals(metaProperty)
                            && String.class.equals(metaProperty.getJavaType());
                    //skip system properties
                    if (tools.isSystem(metaProperty) && !showSystemFields && !includeId) {
                        continue;
                    }
                    if (metaProperty.getType() != MetaProperty.Type.ENUM
                            && (isByteArray(metaProperty) || isUuid(metaProperty))) {
                        continue;
                    }

                    if (includeId && !isNew) {
                        isReadonly = true;
                    }
                    addField(metaClass, metaProperty, item, fieldGroup, isRequired, false, isReadonly, customFields);
                    break;
                case COMPOSITION:
                case ASSOCIATION:
                    if (metaProperty.getRange().getCardinality().isMany()) {
                        addTable(metaClass, metaProperty);
                    } else {
                        if (isEmbedded(metaProperty)) {
                            Entity propertyValue = item.getValue(metaProperty.getName());
                            addEmbeddedFieldGroup(metaProperty, "", propertyValue);
                        } else {
                            addField(metaClass, metaProperty, item, fieldGroup, isRequired, true, isReadonly, customFields);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        fieldGroup.setDatasource(datasource);
        createCustomFields(fieldGroup, customFields);
        fieldGroup.setBorderVisible(true);
    }
    /**
     * Creates field group for the embedded property
     *
     * @param embeddedMetaProperty meta property of the embedded property
     * @param embeddedItem current value of the embedded property
     */
    private void addEmbeddedFieldGroup(MetaProperty embeddedMetaProperty, String fqnPrefix, Entity embeddedItem) {
        String fqn = fqnPrefix.isEmpty() ? embeddedMetaProperty.getName()
                : fqnPrefix + "." + embeddedMetaProperty.getName();
        Datasource embedDs = datasources.get(fqn);
        if (embedDs == null) {
            throw new IllegalStateException(String.format("Datasource %s for property %s not found", fqn,
                    embeddedMetaProperty.getName()));
        }
        FieldGroup fieldGroup = componentsFactory.createComponent(FieldGroup.class);
        contentPane.add(fieldGroup);
        fieldGroup.setFrame(frame);
        fieldGroup.setCaption(getPropertyCaption(embeddedMetaProperty));

        MetaClass embeddableMetaClass = embeddedMetaProperty.getRange().asClass();
        Collection<FieldGroup.FieldConfig> customFields = new LinkedList<>();
        MetaProperty nullIndicatorProperty = getNullIndicatorProperty(embeddedMetaProperty);
        for (MetaProperty metaProperty : embeddableMetaClass.getProperties()) {
            boolean isRequired = isRequired(metaProperty) || metaProperty.equals(nullIndicatorProperty);
            boolean isReadonly = metaProperty.isReadOnly();
            switch (metaProperty.getType()) {
                case DATATYPE:
                case ENUM:
                    //skip system properties
                    if (metadata.getTools().isSystem(metaProperty) && !showSystemFields) {
                        continue;
                    }
                    if (metaProperty.getType() != MetaProperty.Type.ENUM
                            && (isByteArray(metaProperty) || isUuid(metaProperty)))  {
                        continue;
                    }
                    addField(embeddableMetaClass, metaProperty, embeddedItem, fieldGroup, isRequired, false, isReadonly, customFields);
                    break;
                case COMPOSITION:
                case ASSOCIATION:
                    if (metaProperty.getRange().getCardinality().isMany()) {
                        throw new IllegalStateException("tables for the embeddable entities are not supported");
                    } else {
                        if (isEmbedded(metaProperty)) {
                            Entity propertyValue = embeddedItem.getValue(metaProperty.getName());
                            addEmbeddedFieldGroup(metaProperty, fqn, propertyValue);
                        } else {
                            addField(embeddableMetaClass, metaProperty, embeddedItem, fieldGroup, isRequired, true, isReadonly, customFields);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        fieldGroup.setDatasource(embedDs);
        fieldGroup.setBorderVisible(true);
    }

    private boolean isByteArray(MetaProperty metaProperty) {
        return ByteArrayDatatype.NAME.equals(metaProperty.getRange().asDatatype().getName());
    }

    private boolean isUuid(MetaProperty metaProperty) {
        return UUIDDatatype.NAME.equals(metaProperty.getRange().asDatatype().getName());
    }

    private boolean isRequired(MetaProperty metaProperty) {
        if (metaProperty.isMandatory())
            return true;

        ManyToOne many2One = metaProperty.getAnnotatedElement().getAnnotation(ManyToOne.class);
        if (many2One != null && !many2One.optional())
            return true;

        OneToOne one2one = metaProperty.getAnnotatedElement().getAnnotation(OneToOne.class);
        return one2one != null && !one2one.optional();
    }

    /**
     * Creates and registers in dsContext property datasource for each of the entity non-datatype
     * and non-enum property
     *
     * @param masterDs master datasource
     */
    private void createPropertyDatasources(Datasource masterDs) {
        for (MetaProperty metaProperty : meta.getProperties()) {
            switch (metaProperty.getType()) {
                case COMPOSITION:
                case ASSOCIATION:
                    NestedDatasource propertyDs;
                    if (metaProperty.getRange().getCardinality().isMany()) {
                        propertyDs = new CollectionPropertyDatasourceImpl();
                    } else {
                        if (isEmbedded(metaProperty)) {
                            propertyDs = new EmbeddedDatasourceImpl();
                        } else {
                            propertyDs = new PropertyDatasourceImpl();
                        }
                    }
                    propertyDs.setup(metaProperty.getName() + "Ds", masterDs, metaProperty.getName());
                    if (isEmbedded(metaProperty)) {
                        createNestedEmbeddedDatasources(metaProperty.getRange().asClass(), metaProperty.getName(), propertyDs);
                    }
                    datasources.put(metaProperty.getName(), propertyDs);
                    dsContext.register(propertyDs);
                    break;
                default:
                    break;
            }
        }
    }

    private void createNestedEmbeddedDatasources(MetaClass metaClass, String fqnPrefix, Datasource masterDs) {
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (MetaProperty.Type.ASSOCIATION == metaProperty.getType()
                    || MetaProperty.Type.COMPOSITION == metaProperty.getType()) {
                if (isEmbedded(metaProperty)) {
                    String fqn = fqnPrefix + "." + metaProperty.getName();
                    MetaClass propertyMetaClass = metaProperty.getRange().asClass();
                    NestedDatasource propertyDs = new EmbeddedDatasourceImpl();
                    propertyDs.setup(fqn + "Ds", masterDs, metaProperty.getName());
                    createNestedEmbeddedDatasources(propertyMetaClass, fqn, propertyDs);
                    datasources.put(fqn, propertyDs);
                    dsContext.register(propertyDs);
                }
            }
        }
    }

    private void createCommitButtons() {
        buttonsPanel = componentsFactory.createComponent(ButtonsPanel.class);
        commitButton = componentsFactory.createComponent(Button.class);
        commitButton.setIcon("icons/ok.png");
        commitButton.setCaption(messages.getMessage(EntityInspectorEditor.class, "commit"));
        commitButton.setAction(new CommitAction());
        cancelButton = componentsFactory.createComponent(Button.class);
        cancelButton.setIcon("icons/cancel.png");
        cancelButton.setCaption(messages.getMessage(EntityInspectorEditor.class, "cancel"));
        cancelButton.setAction(new CancelAction());
        buttonsPanel.add(commitButton);
        buttonsPanel.add(cancelButton);
        buttonsBox.add(buttonsPanel);
    }

    /**
     * Adds field to the specified field group.
     * If the field should be custom, adds it to the specified customFields collection
     * which can be used later to create fieldGenerators
     *
     * @param metaProperty meta property of the item's property which field is creating
     * @param item entity instance containing given property
     * @param fieldGroup   field group to which created field will be added
     * @param customFields if the field is custom it will be added to this collection
     * @param required     true if the field is required
     * @param custom       true if the field is custom
     */
    private void addField(MetaClass metaClass, MetaProperty metaProperty, Entity item,
                          FieldGroup fieldGroup, boolean required, boolean custom, boolean readOnly,
                          Collection<FieldGroup.FieldConfig> customFields) {
        if (!attrViewPermitted(metaClass, metaProperty))
            return;

        if ((metaProperty.getType() == MetaProperty.Type.COMPOSITION
                || metaProperty.getType() == MetaProperty.Type.ASSOCIATION)
                && !entityOpPermitted(metaProperty.getRange().asClass(), EntityOp.READ))
            return;

        FieldGroup.FieldConfig field = new FieldGroup.FieldConfig(metaProperty.getName());
        String caption = getPropertyCaption(metaProperty);
        field.setCaption(caption);
        field.setType(metaProperty.getJavaType());
        field.setWidth(themeConstants.get("cuba.gui.EntityInspectorEditor.field.width"));
        field.setCustom(custom);
        field.setRequired(required);
        field.setEditable(!readOnly);
        field.setWidth("100%");

        if (requireTextArea(metaProperty, item)) {
            Element root = DocumentHelper.createElement("textArea");
            root.addAttribute("rows", "3");
            field.setXmlDescriptor(root);
        }

        if (focusFieldId == null && !readOnly) {
            focusFieldId = field.getId();
            focusFieldGroup = fieldGroup;
        }

        if (required) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            field.setRequiredError(messageTools.getDefaultRequiredMessage(metaClass, metaProperty.getName()));
        }
        fieldGroup.addField(field);
        if (custom)
            customFields.add(field);
    }

    /**
     * @param metaProperty meta property
     * @param item entity containing property of the given meta property
     * @return true if property require text area component; that is if it either too long or contains line separators
     */
    private boolean requireTextArea(MetaProperty metaProperty, Entity item) {
        if (!String.class.equals(metaProperty.getJavaType())) {
            return false;
        }

        Integer textLength = (Integer) metaProperty.getAnnotations().get("length");
        boolean isLong = textLength == null || textLength > MAX_TEXTFIELD_STRING_LENGTH;

        Object value = item.getValue(metaProperty.getName());
        boolean isContainsSeparator = value != null && containsSeparator((String) value);

        return isLong || isContainsSeparator;
    }

    private boolean containsSeparator(String s) {
        return s.indexOf('\n') >=0 || s.indexOf('\r') >= 0;
    }

    /**
     * Checks if specified property is a reference to entity's parent entity.
     * Parent entity can be specified during creating of this screen.
     *
     * @param metaProperty meta property
     * @return true if property references to a parent entity
     */
    private boolean isParentProperty(MetaProperty metaProperty) {
        return parentProperty != null && metaProperty.getName().equals(parentProperty);
    }

    /**
     * Creates custom fields and adds them to the fieldGroup
     */
    private void createCustomFields(FieldGroup fieldGroup, Collection<FieldGroup.FieldConfig> customFields) {
        for (FieldGroup.FieldConfig field : customFields) {
            //custom field generator creates an pickerField
            fieldGroup.addCustomField(field, new FieldGroup.CustomFieldGenerator() {
                @Override
                public Component generateField(Datasource datasource, String propertyId) {
                    MetaProperty metaProperty = datasource.getMetaClass().getPropertyNN(propertyId);
                    MetaClass propertyMeta = metaProperty.getRange().asClass();
                    PickerField field = componentsFactory.createComponent(PickerField.class);
                    String caption = getPropertyCaption(metaProperty);
                    field.setCaption(caption);
                    field.setMetaClass(propertyMeta);

                    PickerField.LookupAction lookupAction = field.addLookupAction();
                    //forwards lookup to the EntityInspectorBrowse window
                    lookupAction.setLookupScreen(EntityInspectorBrowse.SCREEN_NAME);
                    lookupAction.setLookupScreenOpenType(OPEN_TYPE);
                    lookupAction.setLookupScreenParams(Collections.singletonMap("entity", (Object) propertyMeta.getName()));

                    field.addClearAction();
                    //don't lets user to change parent
                    if (isParentProperty(metaProperty)) {
                        //set parent item if it has been retrieved
                        if (parent != null) {
                            if (parent.toString() == null) {
                                initNamePatternFields(parent);
                            }
                            field.setValue(parent);
                        }
                        field.setEditable(false);
                    }
                    field.setDatasource(datasource, propertyId);
                    return field;
                }
            });
        }
    }

    /**
     * Tries to initialize entity fields included in entity name pattern by default values
     *
     * @param entity instance
     */
    private void initNamePatternFields(Entity entity) {
        Collection<MetaProperty> properties = metadata.getTools().getNamePatternProperties(entity.getMetaClass());
        for (MetaProperty property : properties) {
            if (entity.getValue(property.getName()) == null) {
                if (property.getType() == MetaProperty.Type.DATATYPE)
                    try {
                        entity.setValue(property.getName(), property.getJavaType().newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
            }
        }
    }

    private String getPropertyCaption(MetaProperty metaProperty) {
        String caption = messageTools.getPropertyCaption(metaProperty);
        if (caption.length() < CAPTION_MAX_LENGTH)
            return caption;
        else
            return caption.substring(0, CAPTION_MAX_LENGTH);
    }

    /**
     * Creates a table for the entities in ONE_TO_MANY or MANY_TO_MANY relation with the current one
     */
    private void addTable(MetaClass metaClass, MetaProperty childMeta) {
        MetaClass meta = childMeta.getRange().asClass();

        //don't show empty table if the user don't have permissions on the attribute or the entity
        if (!attrViewPermitted(metaClass, childMeta.getName()) ||
                !entityOpPermitted(meta, EntityOp.READ)) {
            return;
        }

        //don't show table on new master item, because an exception occurred on safe new item in table
        if (isNew && childMeta.getType().equals(MetaProperty.Type.ASSOCIATION)) {
            return;
        }

        //vertical box for the table and its label
        BoxLayout vbox = componentsFactory.createComponent(VBoxLayout.class);
        vbox.setWidth("100%");
        CollectionDatasource propertyDs = (CollectionDatasource) datasources.get(childMeta.getName());

        Label label = componentsFactory.createComponent(Label.class);
        label.setValue(getPropertyCaption(childMeta));
        label.setStyleName("h2");

        Table table = componentsFactory.createComponent(Table.class);
        table.setMultiSelect(true);
        table.setFrame(frame);
        //place non-system properties columns first
        LinkedList<Table.Column> nonSystemPropertyColumns = new LinkedList<>();
        LinkedList<Table.Column> systemPropertyColumns = new LinkedList<>();
        for (MetaProperty metaProperty : meta.getProperties()) {
            if (metaProperty.getRange().isClass())
                continue; // because we use local views
            Table.Column column = new Table.Column(meta.getPropertyPath(metaProperty.getName()));
            if (!metadata.getTools().isSystem(metaProperty)) {
                column.setCaption(getPropertyCaption(metaProperty));
                nonSystemPropertyColumns.add(column);
            } else {
                column.setCaption(metaProperty.getName());
                systemPropertyColumns.add(column);
            }
            if (metaProperty.getJavaType().equals(String.class)) {
                column.setMaxTextLength(MAX_TEXT_LENGTH);
            }
        }
        for (Table.Column column : nonSystemPropertyColumns)
            table.addColumn(column);

        for (Table.Column column : systemPropertyColumns)
            table.addColumn(column);

        //set datasource so we could create a buttons panel
        table.setDatasource(propertyDs);

        //refresh ds to read ds size
        propertyDs.refresh();
        ButtonsPanel propertyButtonsPanel = createButtonsPanel(childMeta, propertyDs, table);
        table.setButtonsPanel(propertyButtonsPanel);

        RowsCount rowsCount = componentsFactory.createComponent(RowsCount.class);
        rowsCount.setDatasource(propertyDs);
        table.setRowsCount(rowsCount);
        table.setWidth("100%");
        vbox.setHeight(themeConstants.get("cuba.gui.EntityInspectorEditor.tableContainer.height"));
        vbox.add(label);
        vbox.add(table);
        vbox.expand(table);
        tablesBox.add(vbox);
        tables.add(table);
    }

    /**
     * Creates a buttons panel managing table's content.
     *
     * @param metaProperty property representing table's data
     * @param propertyDs   property's Datasource (CollectionPropertyDatasource usually)
     * @param table        table
     * @return buttons panel
     */
    @SuppressWarnings("unchecked")
    private ButtonsPanel createButtonsPanel(final MetaProperty metaProperty,
                                            final CollectionDatasource propertyDs, Table table) {
        MetaClass propertyMetaClass = metaProperty.getRange().asClass();
        ButtonsPanel propertyButtonsPanel = componentsFactory.createComponent(ButtonsPanel.class);

        Button createButton = componentsFactory.createComponent(Button.class);
        CreateAction createAction = new CreateAction(metaProperty, propertyDs, propertyMetaClass);
        createButton.setAction(createAction);
        table.addAction(createAction);
        createButton.setCaption(messages.getMessage(EntityInspectorEditor.class, "create"));
        createButton.setIcon("icons/create.png");

        Button addButton = componentsFactory.createComponent(Button.class);
        AddAction addAction = createAddAction(metaProperty, propertyDs, table, propertyMetaClass);
        table.addAction(addAction);
        addButton.setAction(addAction);
        addButton.setCaption(messages.getMessage(EntityInspectorEditor.class, "add"));
        addButton.setIcon("icons/add.png");

        Button editButton = componentsFactory.createComponent(Button.class);
        EditAction editAction = new EditAction(metaProperty, table, propertyDs);
        editButton.setAction(editAction);
        editButton.setCaption(messages.getMessage(EntityInspectorEditor.class, "edit"));
        editButton.setIcon("icons/edit.png");
        table.addAction(editAction);
        table.setItemClickAction(editAction);
        table.setEnterPressAction(editAction);

        RemoveAction removeAction = createRemoveAction(metaProperty, table);
        Button removeButton = componentsFactory.createComponent(Button.class);
        removeButton.setAction(removeAction);
        table.addAction(removeAction);
        removeButton.setCaption(messages.getMessage(EntityInspectorEditor.class, "remove"));
        removeButton.setIcon("icons/remove.png");

        propertyButtonsPanel.add(createButton);
        propertyButtonsPanel.add(addButton);
        propertyButtonsPanel.add(editButton);
        propertyButtonsPanel.add(removeButton);
        return propertyButtonsPanel;
    }

    private AddAction createAddAction(MetaProperty metaProperty, CollectionDatasource propertyDs,
                                      Table table, MetaClass propertyMetaClass) {
        Lookup.Handler addHandler = createAddHandler(metaProperty, propertyDs);
        AddAction addAction = new AddAction(table, addHandler, OPEN_TYPE);
        addAction.setWindowId(EntityInspectorBrowse.SCREEN_NAME);
        HashMap<String, Object> params = new HashMap<>();
        params.put("entity", propertyMetaClass.getName());
        MetaProperty inverseProperty = metaProperty.getInverse();
        if (inverseProperty != null)
            params.put("parentProperty", inverseProperty.getName());
        addAction.setWindowParams(params);
        addAction.setOpenType(OPEN_TYPE);
        addAction.setShortcut(configuration.getConfig(ClientConfig.class).getTableAddShortcut());
        return addAction;
    }

    @SuppressWarnings("unchecked")
    private Lookup.Handler createAddHandler(final MetaProperty metaProperty, final CollectionDatasource propertyDs) {
        Lookup.Handler result = new Lookup.Handler() {
            @Override
            public void handleLookup(Collection items) {
                for (Object item : items) {
                    Entity entity = (Entity) item;
                    if (!propertyDs.getItems().contains(entity)) {
                        MetaProperty inverseProperty = metaProperty.getInverse();
                        if (inverseProperty != null) {
                            if (!inverseProperty.getRange().getCardinality().isMany()) {
                                //set currently editing item to the child's parent property
                                entity.setValue(inverseProperty.getName(), datasource.getItem());
                                propertyDs.addItem(entity);
                            } else {
                                Collection properties = entity.getValue(inverseProperty.getName());
                                if (properties != null) {
                                    properties.add(datasource.getItem());
                                    propertyDs.addItem(entity);
                                }
                            }
                        }
                    }

                    propertyDs.addItem(entity);
                }
            }
        };

        propertyDs.refresh();
        return result;
    }

    public void commitAndClose() {
        try {
            validate();
            dsContext.commit();
            close(Window.COMMIT_ACTION_ID, true);
        } catch (ValidationException e) {
            showNotification("Validation error", e.getMessage(), NotificationType.TRAY);
        }
    }

    /**
     * Creates either Remove or Exclude action depending on property type
     */
    private RemoveAction createRemoveAction(MetaProperty metaProperty, Table table) {
        RemoveAction result;
        switch (metaProperty.getType()) {
            case COMPOSITION:
                result = new com.haulmont.cuba.gui.components.actions.RemoveAction(table);
                break;
            case ASSOCIATION:
                result = new com.haulmont.cuba.gui.components.actions.ExcludeAction(table);
                result.setShortcut(configuration.getConfig(ClientConfig.class).getTableRemoveShortcut());
                break;
            default:
                throw new IllegalArgumentException("property must contain an entity");
        }
        result.setAutocommit(false);
        return result;
    }

    /**
     * Creates a view, loading all the properties.
     * Referenced entities will be loaded with a LOCAL view.
     *
     * @param meta meta class
     * @return View instance
     */
    @SuppressWarnings("unchecked")
    private View createView(MetaClass meta) {
        View view = new View(meta.getJavaClass(), false);
        for (MetaProperty metaProperty : meta.getProperties()) {
            if (metaProperty.isReadOnly()) {
                continue;
            }

            switch (metaProperty.getType()) {
                case DATATYPE:
                case ENUM:
                    view.addProperty(metaProperty.getName());
                    break;
                case ASSOCIATION:
                case COMPOSITION:
                    String viewName = metaProperty.getRange().getCardinality().isMany() ? View.LOCAL : View.MINIMAL;
                    View propView = viewRepository.getView(metaProperty.getRange().asClass(), viewName);
                    view.addProperty(metaProperty.getName(), propView);
                    break;
                default:
                    throw new IllegalStateException("unknown property type");
            }
        }
        return view;
    }

    protected class CommitAction extends AbstractAction {

        protected CommitAction() {
            super("commit");
        }

        @Override
        public void actionPerform(Component component) {
            commitAndClose();
        }
    }

    protected class CancelAction extends AbstractAction {

        protected CancelAction() {
            super("cancel");
        }

        @Override
        public void actionPerform(Component component) {
            close(Window.CLOSE_ACTION_ID);
        }
    }

    /**
     * Opens entity inspector's editor to create entity
     */
    protected class CreateAction extends AbstractAction {

        private CollectionDatasource entitiesDs;
        private MetaClass entityMeta;
        protected MetaProperty metaProperty;

        protected CreateAction(MetaProperty metaProperty, CollectionDatasource entitiesDs, MetaClass entityMeta) {
            super("create");
            this.entitiesDs = entitiesDs;
            this.entityMeta = entityMeta;
            this.metaProperty = metaProperty;
            setShortcut(configuration.getConfig(ClientConfig.class).getTableInsertShortcut());
        }

        @Override
        @SuppressWarnings("unchecked")
        public void actionPerform(Component component) {
            Map<String, Object> editorParams = new HashMap<>();
            editorParams.put("metaClass", entityMeta.getName());
            editorParams.put("autocommit", Boolean.FALSE);
            MetaProperty inverseProperty = metaProperty.getInverse();
            if (inverseProperty != null)
                editorParams.put("parentProperty", inverseProperty.getName());
            editorParams.put("parent", item);
            if (metaProperty.getType() == MetaProperty.Type.COMPOSITION)
                editorParams.put("parentDs", entitiesDs);
            EntityInspectorEditor editor = (EntityInspectorEditor) openWindow("entityInspector.edit", OPEN_TYPE, editorParams);
            editor.addCloseListener(actionId -> {
                if (COMMIT_ACTION_ID.equals(actionId) && metaProperty.getType() == MetaProperty.Type.ASSOCIATION) {
                    boolean modified = entitiesDs.isModified();
                    entitiesDs.addItem(editor.getItem());
                    ((DatasourceImplementation) entitiesDs).setModified(modified);
                }
            });
        }
    }

    protected class EditAction extends ItemTrackingAction {

        private Table entitiesTable;
        private CollectionDatasource entitiesDs;
        private MetaProperty metaProperty;

        protected EditAction(MetaProperty metaProperty, Table entitiesTable, CollectionDatasource entitiesDs) {
            super(entitiesTable, "edit");
            this.entitiesTable = entitiesTable;
            this.entitiesDs = entitiesDs;
            this.metaProperty = metaProperty;
        }

        @Override
        public void actionPerform(Component component) {
            Set selected = entitiesTable.getSelected();

            if (selected.size() != 1)
                return;

            Entity editItem = (Entity) selected.toArray()[0];
            Map<String, Object> editorParams = new HashMap<>();
            editorParams.put("metaClass", editItem.getMetaClass());
            editorParams.put("item", editItem);
            editorParams.put("parent", item);
            editorParams.put("autocommit", Boolean.FALSE);
            MetaProperty inverseProperty = metaProperty.getInverse();
            if (inverseProperty != null)
                editorParams.put("parentProperty", inverseProperty.getName());
            if (metaProperty.getType() == MetaProperty.Type.COMPOSITION)
                editorParams.put("parentDs", entitiesDs);

            Window window = openWindow("entityInspector.edit", OPEN_TYPE, editorParams);
            window.addCloseListener(actionId -> {
                entitiesDs.refresh();
            });
        }
    }

    private boolean attrViewPermitted(MetaClass metaClass, String property) {
        return attrPermitted(metaClass, property, EntityAttrAccess.VIEW);
    }

    private boolean attrViewPermitted(MetaClass metaClass, MetaProperty metaProperty) {
        return attrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.VIEW);
    }

    private boolean attrPermitted(MetaClass metaClass, String property, EntityAttrAccess entityAttrAccess) {
        return security.isEntityAttrPermitted(metaClass, property, entityAttrAccess);
    }

    private boolean entityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        return security.isEntityOpPermitted(metaClass, entityOp);
    }
}