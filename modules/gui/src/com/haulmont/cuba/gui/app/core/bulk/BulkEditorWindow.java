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

package com.haulmont.cuba.gui.app.core.bulk;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.NestedDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.data.impl.EmbeddedDatasourceImpl;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class BulkEditorWindow extends AbstractWindow {
    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataSupplier dataSupplier;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected Security security;

    @Inject
    protected DynamicAttributes dynamicAttributes;

    @Inject
    protected BoxLayout contentPane;

    @Inject
    protected Label infoLabel;

    @Inject
    protected Button applyButton;

    @Inject
    protected ThemeConstants themeConstants;

    @WindowParam(required = true)
    protected MetaClass metaClass;

    @WindowParam(required = true)
    protected Set<Entity> selected;

    @WindowParam
    protected String exclude;

    @WindowParam
    protected List<String> includeProperties;

    @WindowParam
    protected boolean loadDynamicAttributes = true;

    @WindowParam
    protected boolean useConfirmDialog = true;

    @WindowParam
    protected Map<String, Field.Validator> fieldValidators;

    @WindowParam
    protected List<Field.Validator> modelValidators;

    protected Pattern excludeRegex;

    protected DsContextImpl dsContext;
    protected Datasource<Entity> datasource;
    protected Map<String, Datasource<Entity>> datasources = new HashMap<>();

    protected Map<String, ManagedField> managedFields = new LinkedHashMap<>();
    protected Map<String, Field> dataFields = new LinkedHashMap<>();
    protected BulkEditorFieldFactory fieldFactory = new BulkEditorFieldFactory();
    protected List<Entity> items;

    protected List<String> managedEmbeddedProperties = new ArrayList<>();

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        String width = themeConstants.get("cuba.gui.BulkEditorWindow.width");
        String height = themeConstants.get("cuba.gui.BulkEditorWindow.height");

        getDialogOptions()
                .setWidth(width)
                .setHeight(height);

        checkNotNullArgument(metaClass);
        checkNotNullArgument(selected);

        if (StringUtils.isNotBlank(exclude)) {
            excludeRegex = Pattern.compile(exclude);
        }

        for (ManagedField managedField : getManagedFields(metaClass)) {
            managedFields.put(managedField.getFqn(), managedField);
        }

        View view = createView(metaClass);

        items = loadItems(view);

        dsContext = new DsContextImpl(dataSupplier);
        dsContext.setFrameContext(getDsContext().getFrameContext());
        setDsContext(dsContext);

        datasource = new DatasourceImpl<>();
        datasource.setup(dsContext, dataSupplier, metaClass.getName() + "Ds", metaClass, view);
        ((DatasourceImpl) datasource).valid();

        dsContext.register(datasource);
        createNestedEmbeddedDatasources(datasource, metaClass, "");

        Entity instance = metadata.create(metaClass);
        if (loadDynamicAttributes && (instance instanceof BaseGenericIdEntity)) {
            ((BaseGenericIdEntity) instance).setDynamicAttributes(new HashMap<>());
        }
        createEmbeddedFields(metaClass, instance, "");

        datasource.setItem(instance);
        datasource.setAllowCommit(false);

        createDataComponents();
    }

    protected void createDataComponents() {
        if (managedFields.isEmpty()) {
            infoLabel.setValue(getMessage("bulk.noEditableProperties"));
            applyButton.setVisible(false);
            return;
        }

        GridLayout grid = componentsFactory.createComponent(GridLayout.class);
        grid.setSpacing(true);
        grid.setColumns(4);
        grid.setRows((managedFields.size() + 1) / 2);
        grid.setStyleName("c-bulk-editor-grid");

        contentPane.add(grid);
        grid.setFrame(frame);

        List<ManagedField> editFields = new ArrayList<>(managedFields.values());
        editFields.sort((o1, o2) -> o1.getLocalizedName().compareTo(o2.getLocalizedName()));

        String fieldWidth = themeConstants.get("cuba.gui.BulkEditorWindow.field.width");

        for (ManagedField field : editFields) {
            Label label = componentsFactory.createComponent(Label.class);
            label.setFrame(getFrame());
            label.setValue(field.getLocalizedName());
            label.setAlignment(Alignment.TOP_LEFT);
            label.setStyleName("field-label");
            if (AppConfig.getClientType() == ClientType.DESKTOP) {
                label.setHeight("25px");
            }

            grid.add(label);

            Datasource<Entity> fieldDs = datasource;
            // field owner metaclass is embeddable only if field domain embeddable,
            // so we can check field domain
            if (metadataTools.isEmbeddable(field.getMetaProperty().getDomain())) {
                fieldDs = datasources.get(field.getParentFqn());
            }

            final Field editField = fieldFactory.createField(fieldDs, field.getMetaProperty());
            if (editField != null) {
                editField.setFrame(getFrame());
                editField.setWidth(fieldWidth);

                boolean required = editField.isRequired();

                BoxLayout boxLayout = componentsFactory.createComponent(HBoxLayout.class);
                boxLayout.setFrame(getFrame());
                boxLayout.setSpacing(true);

                boxLayout.add(editField);

                if (!required) {
                    final Button clearButton = componentsFactory.createComponent(Button.class);
                    clearButton.setFrame(getFrame());
                    Action action = new AbstractAction("actions.BulkClear") {
                        @Override
                        public void actionPerform(Component component) {
                            editField.setEnabled(!editField.isEnabled());
                            if (!editField.isEnabled()) {
                                if (editField instanceof ListEditor) {
                                    editField.setValue(Collections.EMPTY_LIST);
                                } else {
                                    editField.setValue(null);
                                }
                                setIcon("icons/edit.png");
                                clearButton.setDescription(getMessage("bulk.editAttribute"));
                            } else {
                                setIcon("icons/trash.png");
                                clearButton.setDescription(getMessage("bulk.clearAttribute"));
                            }
                        }
                    };
                    action.setCaption("");
                    action.setIcon("icons/trash.png");

                    clearButton.setAction(action);
                    clearButton.setDescription(getMessage("bulk.clearAttribute"));

                    boxLayout.add(clearButton);
                }

                editField.setRequired(false);

                if (editField instanceof ListEditor) {
                    editField.setValue(Collections.EMPTY_LIST);
                } else {
                    editField.setValue(null);
                }

                if (fieldValidators != null) {
                    Field.Validator validator = fieldValidators.get(field.getFqn());
                    if (validator != null) {
                        editField.addValidator(validator);
                    }
                }

                grid.add(boxLayout);

                dataFields.put(field.getFqn(), editField);
            } else {
                Label unknownLabel = componentsFactory.createComponent(Label.class);
                unknownLabel.setFrame(getFrame());
                grid.add(unknownLabel);
            }
        }

        if (!dataFields.isEmpty()) {
            dataFields.values().iterator().next().requestFocus();
        }
    }

    protected boolean isByteArray(MetaProperty metaProperty) {
        return metaProperty.getRange().asDatatype().getJavaClass().equals(byte[].class);
    }

    protected boolean isUuid(MetaProperty metaProperty) {
        return metaProperty.getRange().asDatatype().getJavaClass().equals(UUID.class);
    }

    /**
     * Recursively instantiates the embedded properties.
     * E.g. embedded properties of the embedded property will also be instantiated.
     *
     * @param metaClass meta class of the entity
     * @param item      entity instance
     */
    protected void createEmbeddedFields(MetaClass metaClass, Entity item, String fqnPrefix) {
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            String fqn = metaProperty.getName();
            if (StringUtils.isNotEmpty(fqnPrefix)) {
                fqn = fqnPrefix + "." + fqn;
            }

            if (managedEmbeddedProperties.contains(fqn) && metadataTools.isEmbedded(metaProperty)) {
                MetaClass embeddedMetaClass = metaProperty.getRange().asClass();
                Entity embedded = item.getValue(metaProperty.getName());
                if (embedded == null) {
                    embedded = metadata.create(embeddedMetaClass);
                    item.setValue(metaProperty.getName(), embedded);
                }
                createEmbeddedFields(embeddedMetaClass, embedded, fqn);
            }
        }
    }

    protected void createNestedEmbeddedDatasources(Datasource masterDs, MetaClass metaClass, String fqnPrefix) {
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (MetaProperty.Type.ASSOCIATION == metaProperty.getType()
                    || MetaProperty.Type.COMPOSITION == metaProperty.getType()) {
                String fqn = metaProperty.getName();
                if (StringUtils.isNotEmpty(fqnPrefix)) {
                    fqn = fqnPrefix + "." + fqn;
                }

                if (managedEmbeddedProperties.contains(fqn) && metadataTools.isEmbedded(metaProperty)) {
                    MetaClass propertyMetaClass = metaProperty.getRange().asClass();
                    @SuppressWarnings("unchecked")
                    NestedDatasource<Entity> propertyDs = new EmbeddedDatasourceImpl();
                    propertyDs.setup(fqn + "Ds", masterDs, metaProperty.getName());
                    propertyDs.setAllowCommit(false);
                    createNestedEmbeddedDatasources(propertyDs, propertyMetaClass, fqn);
                    datasources.put(fqn, propertyDs);
                    dsContext.register(propertyDs);
                }
            }
        }
    }

    /**
     * Creates a view, loading only necessary properties.
     * Referenced entities will be loaded with a MINIMAL view.
     *
     * @param meta meta class
     * @return View instance
     */
    protected View createView(MetaClass meta) {
        @SuppressWarnings("unchecked")
        View view = new View(meta.getJavaClass(), false);
        for (MetaProperty metaProperty : meta.getProperties()) {
            if (!managedFields.containsKey(metaProperty.getName())
                    && !managedEmbeddedProperties.contains(metaProperty.getName())) {
                continue;
            }

            switch (metaProperty.getType()) {
                case DATATYPE:
                case ENUM:
                    view.addProperty(metaProperty.getName());
                    break;
                case ASSOCIATION:
                case COMPOSITION:
                    View propView;
                    if (!metadataTools.isEmbedded(metaProperty)) {
                        propView = viewRepository.getView(metaProperty.getRange().asClass(), View.MINIMAL);
                        //in some cases JPA loads extended entities as instance of base class which leads to ClassCastException
                        //loading property lazy prevents this from happening
                        view.addProperty(metaProperty.getName(), propView, true);
                    } else {
                        // build view for embedded property
                        propView = createEmbeddedView(metaProperty.getRange().asClass(), metaProperty.getName());
                        view.addProperty(metaProperty.getName(), propView, false);
                    }
                    break;
                default:
                    throw new IllegalStateException("unknown property type");
            }
        }
        return view;
    }

    protected View createEmbeddedView(MetaClass meta, String fqnPrefix) {
        @SuppressWarnings("unchecked")
        View view = new View(meta.getJavaClass(), false);
        for (MetaProperty metaProperty : meta.getProperties()) {
            String fqn = fqnPrefix + "." + metaProperty.getName();

            if (!managedFields.containsKey(fqn)) {
                continue;
            }

            switch (metaProperty.getType()) {
                case DATATYPE:
                case ENUM:
                    view.addProperty(metaProperty.getName());
                    break;
                case ASSOCIATION:
                case COMPOSITION:
                    View propView;
                    if (!metadataTools.isEmbedded(metaProperty)) {
                        propView = viewRepository.getView(metaProperty.getRange().asClass(), View.MINIMAL);
                    } else {
                        // build view for embedded property
                        propView = createEmbeddedView(metaProperty.getRange().asClass(), fqn);
                    }
                    //in some cases JPA loads extended entities as instance of base class which leads to ClassCastException
                    //loading property lazy prevents this from happening
                    view.addProperty(metaProperty.getName(), propView, true);
                    break;
                default:
                    throw new IllegalStateException("unknown property type");
            }
        }
        return view;
    }

    protected boolean isPermitted(MetaClass metaClass, MetaProperty metaProperty) {
        return security.isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);
    }

    protected boolean isRangeClassPermitted(MetaProperty metaProperty) {
        if (metaProperty.getRange().isClass()) {
            MetaClass propertyMetaClass = metaProperty.getRange().asClass();
            if (metadataTools.isSystemLevel(propertyMetaClass)) {
                return false;
            }

            if (!security.isEntityOpPermitted(propertyMetaClass, EntityOp.READ)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isManagedDynamicAttribute(MetaClass metaClass, MetaProperty metaProperty) {
        if (!security.isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.MODIFY)) {
            return false;
        }

        if (!isRangeClassPermitted(metaProperty)) {
            return false;
        }

        return !(excludeRegex != null && excludeRegex.matcher(metaProperty.getName()).matches());
    }

    protected boolean isManagedAttribute(MetaClass metaClass, MetaProperty metaProperty) {
        if (metadataTools.isSystem(metaProperty)
                || metadataTools.isNotPersistent(metaProperty)
                || metadataTools.isSystemLevel(metaProperty)
                || metaProperty.getRange().getCardinality().isMany()
                || !isPermitted(metaClass, metaProperty)) {
            return false;
        }

        if (metaProperty.getRange().isDatatype() && (isByteArray(metaProperty) || isUuid(metaProperty))) {
            return false;
        }

        if (!isRangeClassPermitted(metaProperty)) {
            return false;
        }

        if (includeProperties != null && !includeProperties.isEmpty()) {
            return includeProperties.contains(metaProperty.getName());
        }

        return !(excludeRegex != null && excludeRegex.matcher(metaProperty.getName()).matches());
    }

    protected List<ManagedField> getManagedFields(MetaClass metaClass) {
        List<ManagedField> managedFields = new ArrayList<>();
        // sort Fields
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (isManagedAttribute(metaClass, metaProperty)) {
                String propertyCaption = messageTools.getPropertyCaption(metaClass, metaProperty.getName());
                if (!metadataTools.isEmbedded(metaProperty)) {
                    managedFields.add(new ManagedField(metaProperty.getName(), metaProperty,
                            propertyCaption, null));
                } else {
                    List<ManagedField> nestedFields = getManagedFields(metaProperty, metaProperty.getName(), propertyCaption);
                    if (nestedFields.size() > 0) {
                        managedEmbeddedProperties.add(metaProperty.getName());
                    }

                    managedFields.addAll(nestedFields);
                }
            }
        }

        if (loadDynamicAttributes) {
            List<CategoryAttribute> categoryAttributes = (List<CategoryAttribute>) dynamicAttributes.getAttributesForMetaClass(metaClass);
            if (!categoryAttributes.isEmpty()) {
                for (CategoryAttribute attribute : categoryAttributes) {
                    MetaPropertyPath metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(metaClass, attribute);
                    String propertyCaption = attribute.getLocaleName();

                    if (isManagedDynamicAttribute(metaClass, metaPropertyPath.getMetaProperty())) {
                        managedFields.add(new ManagedField(metaPropertyPath.getMetaProperty().getName(), metaPropertyPath.getMetaProperty(),
                                propertyCaption, null));
                    }
                }
            }
        }
        return managedFields;
    }

    protected List<ManagedField> getManagedFields(MetaProperty embeddedProperty, String fqnPrefix, String localePrefix) {
        List<ManagedField> managedFields = new ArrayList<>();
        MetaClass metaClass = embeddedProperty.getRange().asClass();
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (isManagedAttribute(metaClass, metaProperty)) {
                String fqn = fqnPrefix + "." + metaProperty.getName();
                String localeName = localePrefix + " " + messageTools.getPropertyCaption(metaClass, metaProperty.getName());

                if (!metadataTools.isEmbedded(metaProperty)) {
                    managedFields.add(new ManagedField(fqn, metaProperty, localeName, fqnPrefix));
                } else {
                    List<ManagedField> nestedFields = getManagedFields(metaProperty, fqn, localeName);
                    if (nestedFields.size() > 0) {
                        managedEmbeddedProperties.add(fqn);
                    }
                    managedFields.addAll(nestedFields);
                }
            }
        }
        return managedFields;
    }

    @Override
    protected boolean preClose(String actionId) {
        if (actionId.equals(CLOSE_ACTION_ID)) {
            cancelChanges();
            return false;
        }
        return super.preClose(actionId);
    }

    public void cancelChanges() {
        if (hasChanges()) {
            showOptionDialog(messages.getMainMessage("closeUnsaved.caption"),
                    messages.getMainMessage("closeUnsaved"),
                    MessageType.CONFIRMATION, new Action[]{
                            new DialogAction(Type.YES).withHandler(event -> {
                                close(CLOSE_ACTION_ID, true);
                            }),
                            new DialogAction(Type.NO, Status.PRIMARY)
                    });
        } else {
            close(CLOSE_ACTION_ID, true);
        }
    }

    private boolean hasChanges() {
        for (Map.Entry<String, Field> fieldEntry : dataFields.entrySet()) {
            Field field = fieldEntry.getValue();
            if (isFieldChanged(field)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isFieldChanged(Field field) {
        if (!field.isEnabled()) {
            return true;
        }

        if (field instanceof ListEditor) {
            return !((Collection) field.getValue()).isEmpty();
        } else if (field.getValue() != null) {
            return true;
        }
        return false;
    }

    public void applyChanges() {
        if (validateAll()) {
            StringBuilder sb = new StringBuilder();
            if (modelValidators != null) {
                for (Field.Validator moduleValidator : modelValidators) {
                    try {
                        moduleValidator.validate(datasource);
                    } catch (ValidationException e) {
                        sb.append(e.getMessage());
                        sb.append("\n");
                    }
                }
            }
            if (sb.length() == 0) {
                List<String> fields = new ArrayList<>();
                for (Map.Entry<String, Field> fieldEntry : dataFields.entrySet()) {
                    Field field = fieldEntry.getValue();
                    if (isFieldChanged(field)) {
                        String localizedName = managedFields.get(fieldEntry.getKey()).getLocalizedName();
                        fields.add("- " + localizedName);
                    }
                }

                if (!fields.isEmpty()) {
                    showConfirmDialogOrCommit(fields);
                } else {
                    showNotification(getMessage("bulk.noChanges"), NotificationType.HUMANIZED);
                }
            } else {
                showNotification(sb.toString(), NotificationType.TRAY);
            }
        }
    }

    protected void showConfirmDialogOrCommit(List<String> fields) {
        if (useConfirmDialog) {
            showOptionDialog(getMessage("bulk.confirmation"),
                    formatMessage("bulk.applyConfirmation", items.size(), StringUtils.join(fields, "\n")),
                    MessageType.CONFIRMATION, new Action[]{
                            new AbstractAction("actions.Apply") {
                                {
                                    icon = AppBeans.get(Icons.class)
                                            .get(CubaIcon.DIALOG_OK);
                                }

                                @Override
                                public void actionPerform(Component component) {
                                    commitChanges();
                                }
                            },
                            new DialogAction(Type.CANCEL, Status.PRIMARY)
                    });
        } else {
            commitChanges();
        }
    }

    protected List<Entity> loadItems(View view) {
        LoadContext.Query query = new LoadContext.Query(String.format("select e from %s e where e.%s in :ids", metaClass,
                metadataTools.getPrimaryKeyName(metaClass)));

        List<Object> ids = selected.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
        query.setParameter("ids", ids);

        LoadContext<Entity> lc = new LoadContext<>(metaClass);
        lc.setSoftDeletion(false);
        lc.setQuery(query);
        lc.setView(view);
        lc.setLoadDynamicAttributes(loadDynamicAttributes);

        return dataSupplier.loadList(lc);
    }

    protected void commitChanges() {
        List<String> fields = new ArrayList<>();
        for (Map.Entry<String, Field> fieldEntry : dataFields.entrySet()) {
            Field field = fieldEntry.getValue();
            if (isFieldChanged(field)) {
                fields.add(managedFields.get(fieldEntry.getKey()).getFqn());
            }
        }
        for (Map.Entry<String, Field> fieldEntry : dataFields.entrySet()) {
            Field field = fieldEntry.getValue();
            if (!field.isEnabled()) {
                for (Entity item : items) {
                    ensureEmbeddedPropertyCreated(item, fieldEntry.getKey());

                    item.setValueEx(fieldEntry.getKey(), null);
                }
            } else if (isFieldChanged(field)) {
                for (Entity item : items) {
                    ensureEmbeddedPropertyCreated(item, fieldEntry.getKey());

                    item.setValueEx(fieldEntry.getKey(), field.getValue());
                }
            }
        }

        Set<Entity> committed = dataSupplier.commit(new CommitContext(items));

        Logger logger = LoggerFactory.getLogger(BulkEditorWindow.class);
        logger.info("Applied bulk editing for {} entries of {}. Changed properties: {}",
                committed.size(), metaClass, StringUtils.join(fields, ", "));

        showNotification(formatMessage("bulk.successMessage", committed.size()), NotificationType.HUMANIZED);
        close(COMMIT_ACTION_ID);
    }

    protected void ensureEmbeddedPropertyCreated(Entity item, String propertyPath) {
        if (!StringUtils.contains(propertyPath, ".")) {
            return;
        }

        MetaPropertyPath path = metaClass.getPropertyPath(propertyPath);

        if (path != null) {
            Entity currentItem = item;
            for (MetaProperty property : path.getMetaProperties()) {
                if (metadataTools.isEmbedded(property)) {
                    Object currentItemValue = currentItem.getValue(property.getName());
                    if (currentItemValue == null) {
                        Entity newItem = metadata.create(property.getRange().asClass());
                        currentItem.setValue(property.getName(), newItem);
                        currentItem = newItem;
                    } else {
                        currentItem = (Entity) currentItemValue;
                    }
                } else {
                    break;
                }
            }
        }
    }

    protected static class ManagedField {

        protected final String fqn;
        protected final String parentFqn;

        protected final String localizedName;

        protected final MetaProperty metaProperty;

        public ManagedField(String fqn, MetaProperty metaProperty, String localizedName, String parentFqn) {
            this.fqn = fqn;
            this.metaProperty = metaProperty;
            this.localizedName = localizedName;
            this.parentFqn = parentFqn;
        }

        public String getFqn() {
            return fqn;
        }

        public String getParentFqn() {
            return parentFqn;
        }

        public String getLocalizedName() {
            return localizedName;
        }

        public MetaProperty getMetaProperty() {
            return metaProperty;
        }
    }
}