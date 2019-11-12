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
import com.haulmont.cuba.core.app.BulkEditorDataService;
import com.haulmont.cuba.core.app.BulkEditorDataService.LoadDescriptor;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.validators.AbstractBeanValidator;
import com.haulmont.cuba.gui.config.DeviceInfo;
import com.haulmont.cuba.gui.config.DeviceInfoProvider;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.NestedDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.data.impl.EmbeddedDatasourceImpl;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.app.core.bulk.ColumnsMode.TWO_COLUMNS;

public class BulkEditorWindow extends AbstractWindow {

    public static final String COLUMN_COUNT_STYLENAME = "c-bulk-editor-columns-";

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
    protected UiComponents uiComponents;
    @Inject
    protected Security security;
    @Inject
    protected DeviceInfoProvider deviceInfoProvider;

    @Inject
    protected BulkEditorDataService bulkEditorDataService;

    @Inject
    protected DynamicAttributes dynamicAttributes;

    @Inject
    protected ScrollBoxLayout fieldsScrollBox;
    @Inject
    protected Label<String> infoLabel;
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
    protected Map<String, Consumer> fieldValidators;
    @WindowParam
    protected List<Consumer> modelValidators;
    @WindowParam
    protected BulkEditors.FieldSorter fieldSorter;
    @WindowParam
    protected ColumnsMode columnsMode = TWO_COLUMNS;

    protected Pattern excludeRegex;

    protected DsContextImpl dsContext;
    protected Datasource<Entity> datasource;
    protected Map<String, Datasource<Entity>> datasources = new HashMap<>();

    protected Map<String, ManagedField> managedFields = new LinkedHashMap<>();
    protected Map<String, Field> dataFields = new LinkedHashMap<>();

    protected List<Entity> items;
    protected List<String> managedEmbeddedProperties = new ArrayList<>();

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        checkNotNullArgument(metaClass);
        checkNotNullArgument(selected);

        String width = themeConstants.get("cuba.gui.BulkEditorWindow.width");
        String height = themeConstants.get("cuba.gui.BulkEditorWindow.height");

        getDialogOptions()
                .setWidth(width)
                .setHeight(height)
                .setResizable(true);

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

    @SuppressWarnings("unchecked")
    protected void createDataComponents() {
        if (managedFields.isEmpty()) {
            infoLabel.setValue(getMessage("bulk.noEditableProperties"));
            applyButton.setVisible(false);
            return;
        }

        List<ManagedField> editFields = new ArrayList<>(managedFields.values());

        // sort fields
        Comparator comparator;
        if (fieldSorter != null) {
            Map<MetaProperty, Integer> sorted = fieldSorter.sort(editFields.stream()
                    .map(ManagedField::getMetaProperty)
                    .collect(Collectors.toList()));
            comparator = Comparator.<ManagedField>comparingInt(item -> sorted.get(item.getMetaProperty()));
        } else {
            comparator = Comparator.comparing(ManagedField::getLocalizedName);
        }
        editFields.sort(comparator);

        CssLayout fieldsLayout = uiComponents.create(CssLayout.NAME);
        fieldsLayout.setStyleName("c-bulk-editor-fields-layout");
        fieldsLayout.setWidthFull();
        fieldsLayout.setHeightFull();

        int fromField;
        int toField = 0;
        int addedColumns = 0;

        for (int col = 0; col < columnsMode.getColumnsCount(); col++) {
            fromField = toField;
            toField += getFieldsCountForColumn(
                    editFields.size() - toField,
                    columnsMode.getColumnsCount() - col);

            DeviceInfo deviceInfo = deviceInfoProvider.getDeviceInfo();

            VBoxLayout column = uiComponents.create(VBoxLayout.NAME);
            column.setStyleName("c-bulk-editor-column");
            column.setWidth(Component.AUTO_SIZE);

            for (int fieldIndex = fromField; fieldIndex < toField; fieldIndex++) {
                ManagedField field = editFields.get(fieldIndex);

                CssLayout row = uiComponents.create(CssLayout.NAME);
                row.setStyleName("c-bulk-editor-row");
                row.setWidth("100%");

                Label<String> label = uiComponents.create(Label.NAME);
                label.setValue(field.getLocalizedName());
                label.setStyleName("c-bulk-editor-label");
                if (AppConfig.getClientType() == ClientType.DESKTOP) {
                    label.setHeight("25px");
                }
                row.add(label);

                Datasource<Entity> fieldDs = datasource;
                // field owner metaclass is embeddable only if field domain embeddable,
                // so we can check field domain
                if (metadataTools.isEmbeddable(field.getMetaProperty().getDomain())) {
                    fieldDs = datasources.get(field.getParentFqn());
                }

                BulkEditorFieldFactory fieldFactory = getFieldFactory();
                Field<?> editField = fieldFactory.createField(fieldDs, field.getMetaProperty());
                if (editField != null) {
                    editField.setFrame(getFrame());
                    editField.setStyleName("c-bulk-editor-field");

                    if (isPickerFieldWrapperNeeded(editField, deviceInfo)) {
                        CssLayout wrapper = uiComponents.create(CssLayout.NAME);
                        wrapper.setStyleName("c-bulk-editor-picker-field-wrapper");
                        wrapper.add(editField);
                        row.add(wrapper);
                    } else {
                        row.add(editField);
                    }

                    boolean required = editField.isRequired();
                    if (!required) {
                        Button clearButton = uiComponents.create(Button.class);
                        clearButton.setIconFromSet(CubaIcon.TRASH);
                        clearButton.setCaption("");
                        clearButton.setDescription(getMessage("bulk.clearAttribute"));

                        clearButton.addClickListener(e -> {
                            editField.setEnabled(!editField.isEnabled());
                            if (!editField.isEnabled()) {
                                if (editField instanceof ListEditor) {
                                    ((Field) editField).setValue(Collections.EMPTY_LIST);
                                } else {
                                    editField.setValue(null);
                                }

                                e.getButton().setIconFromSet(CubaIcon.EDIT);
                                e.getButton().setDescription(getMessage("bulk.editAttribute"));
                            } else {
                                e.getButton().setIconFromSet(CubaIcon.TRASH);
                                e.getButton().setDescription(getMessage("bulk.clearAttribute"));
                            }
                        });

                        row.add(clearButton);
                    } else {
                        // hidden component for correctly showing layout
                        Button spacerButton = uiComponents.create(Button.class);
                        spacerButton.setIconFromSet(CubaIcon.TRASH);
                        spacerButton.setStyleName("c-bulk-editor-spacer");
                        row.add(spacerButton);
                    }

                    // disable bean validator

                    //noinspection RedundantCast
                    editField.getValidators().stream()
                            .filter(v -> v instanceof AbstractBeanValidator)
                            .findFirst()
                            .ifPresent(((Field) editField)::removeValidator);

                    // disable required
                    editField.setRequired(false);

                    if (editField instanceof ListEditor) {
                        ((Field) editField).setValue(Collections.EMPTY_LIST);
                    } else {
                        editField.setValue(null);
                    }

                    if (fieldValidators != null) {
                        Consumer validator = fieldValidators.get(field.getFqn());
                        if (validator != null) {
                            editField.addValidator(validator);
                        }
                    }

                    column.add(row);

                    dataFields.put(field.getFqn(), editField);
                } else {
                    column.add(uiComponents.create(Label.class));
                }
            }
            fieldsLayout.add(column);
            // if there is no fields remain
            if (editFields.size() - toField == 0) {
                addedColumns = col + 1;
                break;
            }
        }
        fieldsLayout.addStyleName(COLUMN_COUNT_STYLENAME + addedColumns);
        fieldsScrollBox.add(fieldsLayout);

        dataFields.values().stream()
                .filter(f -> f instanceof Focusable)
                .findFirst()
                .ifPresent(f ->
                        ((Focusable) f).focus()
                );
    }

    protected int getFieldsCountForColumn(int remainFields, int remainColumns) {
        int fieldsForColumn = remainFields / remainColumns;
        return remainFields % remainColumns == 0 ? fieldsForColumn : ++fieldsForColumn;
    }

    protected boolean isPickerFieldWrapperNeeded(Field field, DeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            return false;
        }

        boolean isPickerField = field instanceof PickerField;
        boolean isAffectedBrowser = deviceInfo.isFirefox() || deviceInfo.isEdge() || deviceInfo.isIE()
                || deviceInfo.isSafari();

        return isPickerField && isAffectedBrowser;
    }

    protected BulkEditorFieldFactory getFieldFactory() {
        return new BulkEditorFieldFactory();
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
                        view.addProperty(metaProperty.getName(), propView);
                    } else {
                        // build view for embedded property
                        propView = createEmbeddedView(metaProperty.getRange().asClass(), metaProperty.getName());
                        view.addProperty(metaProperty.getName(), propView);
                    }
                    break;
                default:
                    throw new IllegalStateException("unknown property type");
            }
        }
        return view;
    }

    protected View createEmbeddedView(MetaClass meta, String fqnPrefix) {
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
                    view.addProperty(metaProperty.getName(), propView);
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

            if (metadataTools.isSystemLevel(propertyMetaClass)
                    || !security.isEntityOpPermitted(propertyMetaClass, EntityOp.READ)) {
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
                    managedFields.add(new ManagedField(metaProperty.getName(), metaProperty, propertyCaption, null));
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

                            new DialogAction(Type.YES)
                                    .withHandler(event -> close(CLOSE_ACTION_ID, true)),

                            new DialogAction(Type.NO, Status.PRIMARY)
                    });
        } else {
            close(CLOSE_ACTION_ID, true);
        }
    }

    protected boolean hasChanges() {
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
                for (Consumer moduleValidator : modelValidators) {
                    try {
                        moduleValidator.accept(datasource);
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
                    MessageType.CONFIRMATION, new Action[] {

                            new DialogAction(Type.OK)
                                    .withCaption(getMessage("actions.Apply"))
                                    .withHandler(e -> commitBulkChanges()),

                            new DialogAction(Type.CANCEL, Status.PRIMARY)
                    });
        } else {
            commitBulkChanges();
        }
    }

    protected List<Entity> loadItems(View view) {
        LoadDescriptor ld = new LoadDescriptor(selected, metaClass, view, loadDynamicAttributes);
        return bulkEditorDataService.reload(ld);
    }

    protected void commitBulkChanges() {
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