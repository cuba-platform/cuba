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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionDescriptor;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionValue;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldGroup;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldGroupLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldWrapper;
import com.vaadin.server.Sizeable;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebFieldGroup extends WebAbstractComponent<CubaFieldGroupLayout>
        implements FieldGroup, Component.UiPermissionAware {

    protected CubaFieldGroup wrapper;
    protected boolean wrapperAttached = false;

    protected Map<String, FieldConfig> fields = new HashMap<>();
    protected List<List<FieldConfig>> columnFieldMapping = new ArrayList<>();
    {
        columnFieldMapping.add(new ArrayList<>());
    }

    protected Datasource<Entity> datasource;

    protected FieldGroupFieldFactory fieldFactory;

    public WebFieldGroup() {
        wrapper = new CubaFieldGroup();
        component = new CubaFieldGroupLayout();

        fieldFactory = AppBeans.get(FieldGroupFieldFactory.NAME);
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        AppUI ui = AppUI.getCurrent();
        if (ui != null && id != null) {
            for (final FieldConfig fc : fields.values()) {
                com.vaadin.ui.Field field = ((FieldConfigImpl) fc).getComposition();
                if (field != null) {
                    field.setId(ui.getTestIdManager().getTestId(id + "_" + fc.getId()));
                }
            }
        }
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (id != null && AppUI.getCurrent().isTestMode()) {
            for (FieldConfig fc : fields.values()) {
                com.vaadin.ui.Field field = ((FieldConfigImpl) fc).getComposition();
                if (field != null) {
                    field.setCubaId(fc.getId());
                }
            }
        }
    }

    @Override
    public FieldConfig createField(String id) {
        return new FieldConfigImpl(id);
    }

    @Override
    public List<FieldConfig> getFields() {
        return getColumnOrderedFields();
    }

    @Override
    public List<FieldConfig> getFields(int column) {
        return Collections.unmodifiableList(columnFieldMapping.get(column));
    }

    @Override
    public FieldConfig getField(int column, int row) {
        return columnFieldMapping.get(column).get(row);
    }

    @Override
    public FieldConfig getField(String fieldId) {
        return fields.get(fieldId);
    }

    @Override
    public FieldConfig getFieldNN(String fieldId) {
        FieldConfig fieldConfig = fields.get(fieldId);
        if (fieldConfig == null) {
            throw new IllegalArgumentException("Unable to find field with id " + fieldId);
        }

        return fieldConfig;
    }

    @Override
    public List<Component> getOwnComponents() {
        return getColumnOrderedFields().stream()
                .filter(FieldConfig::isBound)
                .map(FieldConfig::getComponent)
                .collect(Collectors.toList());
    }

    @Override
    public FieldCaptionAlignment getCaptionAlignment() {
        if (component.isUseInlineCaption()) {
            return FieldCaptionAlignment.LEFT;
        }
        return FieldCaptionAlignment.TOP;
    }

    @Override
    public void setCaptionAlignment(FieldCaptionAlignment captionAlignment) {
        component.setUseInlineCaption(WebComponentsHelper.convertFieldGroupCaptionAlignment(captionAlignment));
    }

    @Override
    public void addField(FieldConfig field) {
        addField(field, 0);
    }

    @Override
    public void addField(FieldConfig fc, int colIndex) {
        checkArgument(!fields.containsKey(fc.getId()), "Field '%s' is already registered", fc.getId());
        checkArgument(this == ((FieldConfigImpl) fc).getOwner(), "Field does not belong to this FieldGroup");

        if (colIndex < 0 || colIndex >= component.getColumns()) {
            throw new IllegalArgumentException(String.format("Illegal column number %s, available amount of columns is %s",
                    colIndex, component.getColumns()));
        }

        addFieldInternal(fc, colIndex, -1);
    }

    @Override
    public void addField(FieldConfig fc, int colIndex, int rowIndex) {
        checkArgument(!fields.containsKey(fc.getId()), "Field '%s' is already registered", fc.getId());
        checkArgument(this == ((FieldConfigImpl) fc).getOwner(), "Field does not belong to this FieldGroup");

        if (colIndex < 0 || colIndex >= component.getColumns()) {
            throw new IllegalArgumentException(String.format("Illegal column number %s, available amount of columns is %s",
                    colIndex, component.getColumns()));
        }
        List<FieldConfig> colFields = columnFieldMapping.get(colIndex);
        if (rowIndex < 0 || rowIndex > colFields.size()) {
            throw new IllegalArgumentException(String.format("Illegal row number %s, available amount of rows is %s",
                    rowIndex, component.getRows()));
        }

        addFieldInternal(fc, colIndex, rowIndex);
    }

    @Override
    public void add(Component childComponent) {
        throw new UnsupportedOperationException("Add component is not supported by FieldGroup component");
    }

    @Override
    public void remove(Component childComponent) {
        throw new UnsupportedOperationException("Remove component is not supported by FieldGroup component");
    }

    @Override
    public void removeAll() {
        throw new UnsupportedOperationException("Remove all components are not supported by FieldGroup component");
    }

    @Nullable
    @Override
    public Component getOwnComponent(String id) {
        FieldConfig fieldConfig = getField(id);
        if (fieldConfig != null && fieldConfig.isBound()) {
            return fieldConfig.getComponent();
        }
        return null;
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    protected void addFieldInternal(FieldConfig fc, int colIndex, int rowIndex) {
        List<FieldConfig> colFields = columnFieldMapping.get(colIndex);
        if (rowIndex == -1) {
            rowIndex = colFields.size();
        }

        fields.put(fc.getId(), fc);
        colFields.add(rowIndex, fc);

        FieldConfigImpl fci = (FieldConfigImpl) fc;

        fci.setColumn(colIndex);
        fci.setManaged(true);

        if (fc.getComponent() != null) {
            managedFieldComponentAssigned(fci, fci.getAttachMode());
        }
    }

    protected void reattachColumnFields(int colIndex) {
        for (int i = 0; i < component.getRows(); i++) {
            component.removeComponent(colIndex, i);
        }

        List<FieldConfig> columnFCs = columnFieldMapping.get(colIndex);
        int insertRowIndex = 0;
        for (FieldConfig fc : columnFCs) {
            if (fc.isBound()) {
                component.addComponent(((FieldConfigImpl) fc).getComposition(), colIndex, insertRowIndex);
                insertRowIndex++;
            }
        }
    }

    @Override
    public void removeField(String fieldId) {
        removeField(getFieldNN(fieldId));
    }

    @Override
    public void removeField(FieldConfig fc) {
        checkArgument(this == ((FieldConfigImpl) fc).getOwner(), "Field is not belong to this FieldGroup");

        if (fields.values().contains(fc)) {
            int colIndex = ((FieldConfigImpl) fc).getColumn();
            columnFieldMapping.get(colIndex).remove(fc);
            fields.remove(fc.getId());

            if (fc.isBound()) {
                reattachColumnFields(colIndex);

                component.setRows(detectRowsCount());
            }

            ((FieldConfigImpl) fc).setManaged(false);

            if (fc.getComponent() != null) {
                fc.getComponent().setParent(null);
            }
        }
    }

    protected void managedFieldComponentAssigned(FieldConfigImpl fci, FieldAttachMode mode) {
        com.vaadin.ui.Field fieldImpl = getFieldImplementation(fci.getComponentNN());
        fci.setComposition(fieldImpl);

        assignTypicalAttributes(fci.getComponentNN());

        if (mode == FieldAttachMode.APPLY_DEFAULTS) {
            applyFieldDefaults(fci);
        }

        assignDebugId(fci, fieldImpl);

        component.setRows(detectRowsCount());

        reattachColumnFields(fci.getColumn());
    }

    protected void applyFieldDefaults(FieldConfigImpl fci) {
        Component fieldComponent = fci.getComponentNN();

        if (fieldComponent instanceof Field) {
            Field cubaField = (Field) fieldComponent;

            if (fci.getTargetCaption() != null) {
                cubaField.setCaption(fci.getTargetCaption());
            }
            if (fci.getTargetDescription() != null) {
                // we check empty for description since Vaadin components have "" description by default
                cubaField.setDescription(fci.getTargetDescription());
            }
            if (cubaField instanceof HasInputPrompt && fci.getTargetInputPrompt() != null) {
                ((HasInputPrompt) cubaField).setInputPrompt(fci.getTargetInputPrompt());
            }
            if (fci.getTargetRequired() != null) {
                cubaField.setRequired(fci.getTargetRequired());
            }
            if (fci.getTargetRequiredMessage() != null) {
                cubaField.setRequiredMessage(fci.getTargetRequiredMessage());
            }
            if (fci.getTargetContextHelpText() != null) {
                cubaField.setContextHelpText(fci.getTargetContextHelpText());
            }
            if (fci.getTargetContextHelpTextHtmlEnabled() != null) {
                cubaField.setContextHelpTextHtmlEnabled(fci.getTargetContextHelpTextHtmlEnabled());
            }
            if (fci.getTargetContextHelpIconClickHandler() != null) {
                cubaField.setContextHelpIconClickHandler(fci.getTargetContextHelpIconClickHandler());
            }
            if (fci.getTargetEditable() != null) {
                cubaField.setEditable(fci.getTargetEditable());
            }
            if (fci.getTargetVisible() != null) {
                cubaField.setVisible(fci.getTargetVisible());
            }
            if (cubaField instanceof Component.Focusable && fci.getTargetTabIndex() != null) {
                ((Component.Focusable) cubaField).setTabIndex(fci.getTargetTabIndex());
            }
            for (Field.Validator validator : fci.getTargetValidators()) {
                cubaField.addValidator(validator);
            }

            if (fci.getTargetWidth() != null) {
                fieldComponent.setWidth(fci.getTargetWidth());
            } else {
                if (App.isBound()) {
                    ThemeConstants theme = App.getInstance().getThemeConstants();
                    fieldComponent.setWidth(theme.get("cuba.web.WebFieldGroup.defaultFieldWidth"));
                }
            }
        } else {
            com.vaadin.ui.Field composition = fci.getCompositionNN();
            if (fci.getTargetCaption() != null) {
                composition.setCaption(fci.getTargetCaption());
            }
            if (fci.getTargetDescription() != null) {
                ((CubaFieldWrapper) composition).setDescription(fci.getTargetDescription());
            }
            if (fci.getTargetRequired() != null) {
                composition.setRequired(fci.getTargetRequired());
            }
            if (fci.getTargetRequiredMessage() != null) {
                composition.setRequiredError(fci.getTargetRequiredMessage());
            }
            if (fci.getTargetEditable() != null) {
                composition.setReadOnly(!fci.getTargetEditable());
            }
            if (fci.getTargetVisible() != null) {
                composition.setVisible(fci.getTargetVisible());
            }

            if (fci.getTargetWidth() != null) {
                composition.setWidth(fci.getTargetWidth());
            } else {
                if (App.isBound()) {
                    ThemeConstants theme = App.getInstance().getThemeConstants();
                    composition.setWidth(theme.get("cuba.web.WebFieldGroup.defaultFieldWidth"));
                }
            }
        }

        if (fieldComponent instanceof Component.HasFormatter && fci.getTargetFormatter() != null) {
            ((Component.HasFormatter) fieldComponent).setFormatter(fci.getTargetFormatter());
        }

        if (StringUtils.isNotEmpty(fci.getTargetStylename())) {
            fieldComponent.setStyleName(fci.getTargetStylename());
        }
    }

    @Override
    public float getColumnExpandRatio(int col) {
        return component.getColumnExpandRatio(col);
    }

    @Override
    public void setColumnExpandRatio(int col, float ratio) {
        component.setColumnExpandRatio(col, ratio);
    }

    @Override
    public FieldGroupFieldFactory getFieldFactory() {
        return fieldFactory;
    }

    @Override
    public void setFieldFactory(FieldGroupFieldFactory fieldFactory) {
        this.fieldFactory = fieldFactory;
    }

    @Override
    public int getFieldCaptionWidth() {
        return component.getFixedCaptionWidth();
    }

    @Override
    public void setFieldCaptionWidth(int fixedCaptionWidth) {
        component.setFixedCaptionWidth(fixedCaptionWidth);
    }

    @Override
    public int getFieldCaptionWidth(int column) {
        return component.getFieldCaptionWidth(column);
    }

    @Override
    public void setFieldCaptionWidth(int column, int width) {
        component.setFieldCaptionWidth(column, width);
    }

    @Override
    @Deprecated
    public void addCustomField(String fieldId, CustomFieldGenerator fieldGenerator) {
        addCustomField(getFieldNN(fieldId), fieldGenerator);
    }

    @Override
    @Deprecated
    public void addCustomField(FieldConfig fc, CustomFieldGenerator fieldGenerator) {
        if (!fc.isCustom()) {
            throw new IllegalStateException(String.format("Field '%s' must be defined as custom", fc.getId()));
        }

        FieldConfigImpl fci = (FieldConfigImpl) fc;

        Component fieldComponent = fieldGenerator.generateField(fc.getTargetDatasource(), fci.getTargetProperty());
        fc.setComponent(fieldComponent);
    }

    protected com.vaadin.ui.Field getFieldImplementation(Component c) {
        com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(c);
        if (composition instanceof com.vaadin.ui.Field) {
            return (com.vaadin.ui.Field) composition;
        } else {
            return new CubaFieldWrapper(c);
        }
    }

    protected void assignTypicalAttributes(Component c) {
        if (getFrame() != null && c instanceof BelongToFrame) {
            BelongToFrame belongToFrame = (BelongToFrame) c;
            if (belongToFrame.getFrame() == null) {
                belongToFrame.setFrame(getFrame());
            }
        }

        c.setParent(this);
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDatasource(final Datasource datasource) {
        if (this.datasource != null) {
            throw new UnsupportedOperationException("Changing datasource is not supported by the FieldGroup component");
        }

        this.datasource = datasource;

        assignAutoDebugId();
    }

    @Override
    public void bind() {
        bindDeclarativeFieldConfigs();
    }

    protected void bindDeclarativeFieldConfigs() {
        List<Integer> reattachColumns = new ArrayList<>();

        for (FieldConfig fc : getColumnOrderedFields()) {
            if (!fc.isCustom() && !fc.isBound()) {
                FieldConfigImpl fci = (FieldConfigImpl) fc;

                Datasource targetDs = fc.getTargetDatasource();
                if (targetDs == null) {
                    throw new IllegalStateException(String.format("Unable to get datasource for field '%s'", id));
                }

                FieldGroupFieldFactory.GeneratedField generatedField = fieldFactory.createField(fc);
                Component fieldComponent = generatedField.getComponent();
                
                fci.assignComponent(fieldComponent);
                fci.setAttachMode(generatedField.getAttachMode());

                com.vaadin.ui.Field fieldImpl = getFieldImplementation(fieldComponent);
                fci.setComposition(fieldImpl);

                assignTypicalAttributes(fieldComponent);

                if (generatedField.getAttachMode() == FieldAttachMode.APPLY_DEFAULTS) {
                    applyFieldDefaults(fci);
                }

                assignDebugId(fc, fieldImpl);

                int columnIndex = fci.getColumn();
                if (!reattachColumns.contains(columnIndex)) {
                    reattachColumns.add(columnIndex);
                }
            }
        }

        if (!reattachColumns.isEmpty()) {
            component.setRows(detectRowsCount());

            for (Integer reattachColumnIndex : reattachColumns) {
                reattachColumnFields(reattachColumnIndex);
            }
        }
    }

    protected void assignDebugId(FieldConfig fc, com.vaadin.ui.Field composition) {
        AppUI ui = AppUI.getCurrent();
        if (ui != null) {
            if (ui.isTestMode()) {
                String debugId = getDebugId();
                if (composition != null) {
                    if (debugId != null) {
                        TestIdManager testIdManager = ui.getTestIdManager();
                        composition.setId(testIdManager.getTestId(debugId + "_" + fc.getId()));
                    }
                    composition.setCubaId(fc.getId());
                }
            }
        }
    }

    protected int detectRowsCount() {
        int rowsCount = 0;
        for (List<FieldConfig> fields : columnFieldMapping) {
            long boundCount = fields.stream()
                    .filter(FieldConfig::isBound)
                    .count();

            rowsCount = (int) Math.max(rowsCount, boundCount);
        }
        return Math.max(rowsCount, 1);
    }

    @Override
    public int getColumns() {
        return component.getColumns();
    }

    @Override
    public void setColumns(int columns) {
        if (component.getColumns() != columns) {
            component.setColumns(columns);

            List<List<FieldConfig>> oldColumnFields = this.columnFieldMapping;
            this.columnFieldMapping = new ArrayList<>();
            for (int i = 0; i < columns; i++) {
                if (i < oldColumnFields.size()) {
                    columnFieldMapping.add(oldColumnFields.get(i));
                } else {
                    columnFieldMapping.add(new ArrayList<>());
                }
            }
        }
    }

    @Override
    public String getCaption() {
        if (wrapperAttached) {
            return wrapper.getCaption();
        } else {
            return component.getCaption();
        }
    }

    @Override
    public void setCaption(String caption) {
        if (wrapperAttached) {
            wrapper.setCaption(caption);
        } else {
            component.setCaption(caption);
        }
    }

    @Override
    public boolean isEditable() {
        return !component.isReadOnly();
    }

    @Override
    public void setEditable(boolean editable) {
        if (editable != isEditable()) {
            component.setReadOnly(!editable);

            EditableChangeEvent event = new EditableChangeEvent(this);
            getEventRouter().fireEvent(EditableChangeListener.class, EditableChangeListener::editableChanged, event);
        }
    }

    @Override
    public boolean isBorderVisible() {
        return wrapper.isBorderVisible();
    }

    @Override
    public void setBorderVisible(boolean borderVisible) {
        wrapper.setBorderVisible(borderVisible);

        if (component.getParent() != null && !wrapperAttached) {
            LoggerFactory.getLogger(WebFieldGroup.class)
                    .warn("Unable to set border visible for FieldGroup after adding to component tree");
            return;
        }

        if (borderVisible && !wrapperAttached) {
            wrapper.setContent(component);

            wrapperAttached = true;
        }
    }

    @Override
    public com.vaadin.ui.Component getComposition() {
        if (wrapperAttached) {
            // wrapper is connected to layout
            return wrapper;
        }

        return super.getComposition();
    }

    @Override
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    @Override
    public void validate() throws ValidationException {
        if (!isVisible() || !isEditableWithParent() || !isEnabled()) {
            return;
        }

        Map<Component.Validatable, ValidationException> problemFields = null; // lazily initialized

        // validate column by column
        List<FieldConfig> fieldsByColumns = getColumnOrderedFields();

        for (FieldConfig fc : fieldsByColumns) {
            Component fieldComponent = fc.getComponent();

            // If has valid state
            if ((fieldComponent instanceof Validatable) &&
                    (fieldComponent instanceof Editable)) {
                // If editable
                try {
                    ((Validatable) fieldComponent).validate();
                } catch (ValidationException ex) {
                    if (problemFields == null) {
                        problemFields = new LinkedHashMap<>();
                    }
                    problemFields.put((Validatable) fieldComponent, ex);
                }
            }
        }

        if (problemFields != null && !problemFields.isEmpty()) {
            FieldsValidationException validationException = new FieldsValidationException();
            validationException.setProblemFields(problemFields);

            throw validationException;
        }
    }

    @Override
    public void requestFocus() {
        for (FieldConfig fc : getColumnOrderedFields()) {
            Component component = fc.getComponent();
            if (component != null
                    && component.isEnabled()
                    && component.isVisible()
                    && component instanceof Focusable
                    && ((Focusable) component).isFocusable()) {

                component.requestFocus();
                break;
            }
        }
    }

    @Override
    public void requestFocus(String fieldId) {
        FieldConfig field = getFieldNN(fieldId);
        Component componentField = field.getComponentNN();
        componentField.requestFocus();
    }

    /**
     * @return flat list of column fields
     */
    protected List<FieldConfig> getColumnOrderedFields() {
        return columnFieldMapping.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId())) {
            return "fieldGroup_" + datasource.getId();
        }

        return getClass().getSimpleName();
    }

    @Override
    public void applyPermission(UiPermissionDescriptor permissionDescriptor) {
        checkNotNullArgument(permissionDescriptor);

        final Logger log = LoggerFactory.getLogger(WebFieldGroup.class);

        final String subComponentId = permissionDescriptor.getSubComponentId();
        final UiPermissionValue permissionValue = permissionDescriptor.getPermissionValue();
        final String screenId = permissionDescriptor.getScreenId();

        if (subComponentId != null) {
            final FieldGroup.FieldConfig field = getField(subComponentId);
            if (field != null) {
                if (permissionValue == UiPermissionValue.HIDE) {
                    field.setVisible(false);
                } else if (permissionValue == UiPermissionValue.READ_ONLY) {
                    field.setEditable(false);
                }
            } else {
                log.info("Couldn't find suitable component {} in window {} for UI security rule", subComponentId, screenId);
            }
        } else {
            final String actionHolderComponentId = permissionDescriptor.getActionHolderComponentId();
            FieldConfig fieldConfig = getField(actionHolderComponentId);
            if (fieldConfig == null
                    || fieldConfig.getComponent() == null
                    || !((fieldConfig.getComponent() instanceof Component.SecuredActionsHolder))) {
                log.info("Couldn't find suitable component {} in window {} for UI security rule", actionHolderComponentId, screenId);
                return;
            }

            Component fieldComponent = fieldConfig.getComponent();
            String actionId = permissionDescriptor.getActionId();
            ActionsPermissions permissions = ((SecuredActionsHolder) fieldComponent).getActionsPermissions();
            if (permissionValue == UiPermissionValue.HIDE) {
                permissions.addHiddenActionPermission(actionId);
            } else if (permissionValue == UiPermissionValue.READ_ONLY) {
                permissions.addDisabledActionPermission(actionId);
            }
        }
    }

    @Override
    public void addEditableChangeListener(EditableChangeListener listener) {
        getEventRouter().addListener(EditableChangeListener.class, listener);
    }

    @Override
    public void removeEditableChangeListener(EditableChangeListener listener) {
        getEventRouter().removeListener(EditableChangeListener.class, listener);
    }

    public class FieldConfigImpl implements FieldConfig {
        protected String id;
        protected Element xmlDescriptor;
        protected int column;

        protected Component component;
        protected com.vaadin.ui.Field composition;

        protected boolean managed = false;

        protected String targetWidth;
        protected String targetStylename;
        protected Datasource targetDatasource;
        protected Boolean targetRequired;
        protected Boolean targetEditable;
        protected Boolean targetEnabled;
        protected Boolean targetVisible;
        protected String targetProperty;
        protected Integer targetTabIndex;
        protected String targetRequiredMessage;
        protected CollectionDatasource targetOptionsDatasource;
        protected String targetCaption;
        protected String targetDescription;
        protected String targetContextHelpText;
        protected Boolean targetContextHelpTextHtmlEnabled;
        protected String targetInputPrompt;
        protected Formatter targetFormatter;
        protected boolean isTargetCustom;

        protected List<Field.Validator> targetValidators = new ArrayList<>(0);
        protected Consumer<ContextHelpIconClickEvent> targetContextHelpIconClickHandler;
        protected FieldAttachMode attachMode = FieldAttachMode.APPLY_DEFAULTS;

        public FieldConfigImpl(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public boolean isBound() {
            return component != null;
        }

        public FieldGroup getOwner() {
            return WebFieldGroup.this;
        }

        @Override
        public String getWidth() {
            if (composition != null && isWrapped()) {
                float width = composition.getWidth();
                Sizeable.Unit widthUnit = composition.getWidthUnits();
                return width + widthUnit.getSymbol();
            }
            if (component != null) {
                return ComponentsHelper.getComponentWidth(component);
            }
            return targetWidth;
        }

        @Override
        public void setWidth(String width) {
            if (composition != null && isWrapped()) {
                composition.setWidth(width);
            } else if (component != null) {
                component.setWidth(width);
            } else {
                targetWidth = width;
            }
        }

        @Override
        public String getStyleName() {
            if (component != null) {
                return component.getStyleName();
            }
            return targetStylename;
        }

        @Override
        public void setStyleName(String stylename) {
            if (component != null) {
                component.setStyleName(stylename);

                if (composition != null && isWrapped()) {
                    composition.setStyleName(stylename);
                }
            } else {
                this.targetStylename = stylename;
            }
        }

        protected boolean isWrapped() {
            return component != null && component.unwrapComposition(com.vaadin.ui.Component.class) != composition;
        }

        @Override
        public Datasource getTargetDatasource() {
            if (component instanceof DatasourceComponent) {
                return ((DatasourceComponent) component).getDatasource();
            }
            if (targetDatasource != null) {
                return targetDatasource;
            }
            return WebFieldGroup.this.datasource;
        }

        @Override
        public Datasource getDatasource() {
            if (component instanceof DatasourceComponent) {
                return ((DatasourceComponent) component).getDatasource();
            }

            return targetDatasource;
        }

        @Override
        public void setDatasource(Datasource datasource) {
            checkState(this.component == null, "FieldConfig is already bound to component");

            this.targetDatasource = datasource;
        }

        @Override
        public Boolean isRequired() {
            if (component instanceof Field) {
                return ((Field) component).isRequired();
            }
            return targetRequired;
        }

        @Override
        public void setRequired(Boolean required) {
            if (component instanceof Field) {
                checkNotNullArgument(required, "Unable to reset required flag for the bound FieldConfig");

                ((Field) component).setRequired(required);
            } else if (composition != null && isWrapped()) {
                composition.setRequired(required);
            } else {
                this.targetRequired = required;
            }
        }

        @Override
        public Boolean isEditable() {
            if (component instanceof Editable) {
                return ((Field) component).isEditable();
            }
            return targetEditable;
        }

        @Override
        public void setEditable(Boolean editable) {
            if (component instanceof Editable) {
                checkNotNullArgument(editable, "Unable to reset editable flag for the bound FieldConfig");

                ((Editable) component).setEditable(editable);
            } else if (composition != null && isWrapped()) {
                composition.setReadOnly(!editable);
            } else {
                this.targetEditable = editable;
            }
        }

        @Override
        public Boolean isEnabled() {
            if (component != null) {
                return component.isEnabled();
            }
            return targetEnabled;
        }

        @Override
        public void setEnabled(Boolean enabled) {
            if (component != null) {
                checkNotNullArgument(enabled, "Unable to reset enabled flag for the bound FieldConfig");

                component.setEnabled(enabled);

                if (composition != null && isWrapped()) {
                    composition.setEnabled(enabled);
                }
            } else {
                this.targetEnabled = enabled;
            }
        }

        @Override
        public Boolean isVisible() {
            if (component != null) {
                return component.isVisible();
            }
            return targetVisible;
        }

        @Override
        public void setVisible(Boolean visible) {
            if (component != null) {
                checkNotNullArgument(visible, "Unable to reset visible flag for the bound FieldConfig");

                component.setVisible(visible);

                if (composition != null && isWrapped()) {
                    composition.setVisible(visible);
                }
            } else {
                this.targetVisible = visible;
            }
        }

        @Override
        public String getProperty() {
            if (component instanceof DatasourceComponent) {
                MetaPropertyPath metaPropertyPath = ((DatasourceComponent) component).getMetaPropertyPath();
                return metaPropertyPath != null ? metaPropertyPath.toString() : null;
            }
            return targetProperty;
        }

        @Override
        public void setProperty(String property) {
            checkState(this.component == null, "Unable to change property for bound FieldConfig");

            this.targetProperty = property;
        }

        @Override
        public Integer getTabIndex() {
            return targetTabIndex;
        }

        @Override
        public void setTabIndex(Integer tabIndex) {
            if (component instanceof Focusable) {
                checkNotNullArgument(tabIndex, "Unable to reset tabIndex for the bound FieldConfig");

                ((Focusable) component).setTabIndex(tabIndex);
            } else {
                this.targetTabIndex = tabIndex;
            }
        }

        @Override
        public String getRequiredError() {
            return getRequiredMessage();
        }

        @Override
        public void setRequiredError(String requiredError) {
            setRequiredMessage(requiredError);
        }

        @Override
        public boolean isCustom() {
            return isTargetCustom;
        }

        @Override
        public void setCustom(boolean custom) {
            checkState(this.component == null, "Unable to change custom flag for bound FieldConfig");

            this.isTargetCustom = custom;
        }

        @Override
        public String getRequiredMessage() {
            if (component instanceof Field) {
                return ((Field) component).getRequiredMessage();
            }
            if (composition != null && isWrapped()) {
                return composition.getRequiredError();
            }
            return targetRequiredMessage;
        }

        @Override
        public void setRequiredMessage(String requiredMessage) {
            if (component instanceof Field) {
                ((Field) component).setRequiredMessage(requiredMessage);
            } else if (composition!= null && isWrapped()) {
                composition.setRequiredError(requiredMessage);
            } else {
                this.targetRequiredMessage = requiredMessage;
            }
        }

        @Nullable
        @Override
        public Component getComponent() {
            return component;
        }

        @Override
        public Component getComponentNN() {
            if (component == null) {
                throw new IllegalStateException("FieldConfig is not bound to a Component");
            }
            return component;
        }

        @Override
        public void setComponent(Component component) {
            checkState(this.component == null, "Unable to change component for bound FieldConfig");

            this.component = component;

            if (managed && component != null) {
                managedFieldComponentAssigned(this, FieldAttachMode.APPLY_DEFAULTS);
            }
        }

        @Override
        public void setComponent(Component component, FieldAttachMode mode) {
            checkState(this.component == null, "Unable to change component for bound FieldConfig");

            this.attachMode = mode;
            this.component = component;

            if (managed && component != null) {
                managedFieldComponentAssigned(this, mode);
            }
        }

        public void assignComponent(Component component) {
            checkState(this.component == null, "Unable to change component for bound FieldConfig");

            this.component = component;
        }

        public void setAttachMode(FieldAttachMode attachMode) {
            this.attachMode = attachMode;
        }

        @Override
        public void addValidator(Field.Validator validator) {
            if (component instanceof Field) {
                ((Field) component).addValidator(validator);
            } else {
                if (!targetValidators.contains(validator)) {
                    targetValidators.add(validator);
                }
            }
        }

        @Override
        public void removeValidator(Field.Validator validator) {
            if (component instanceof Field) {
                ((Field) component).removeValidator(validator);
            }
            targetValidators.remove(validator);
        }

        @Override
        public void setOptionsDatasource(CollectionDatasource optionsDatasource) {
            if (component instanceof OptionsField) {
                ((OptionsField) component).setOptionsDatasource(optionsDatasource);
            } else {
                this.targetOptionsDatasource = optionsDatasource;
            }
        }

        @Override
        public CollectionDatasource getOptionsDatasource() {
            if (component instanceof OptionsField) {
                return ((OptionsField) component).getOptionsDatasource();
            }
            return targetOptionsDatasource;
        }

        @Override
        public String getCaption() {
            if (component instanceof Field) {
                return ((Field) component).getCaption();
            }
            if (composition != null && isWrapped()) {
                return composition.getCaption();
            }
            return targetCaption;
        }

        @Override
        public void setCaption(String caption) {
            if (component instanceof Field) {
                ((Field) component).setCaption(caption);
            } else if (composition != null && isWrapped()) {
                composition.setCaption(caption);
            } else {
                this.targetCaption = caption;
            }
        }

        @Override
        public String getDescription() {
            if (component instanceof Field) {
                return ((Field) component).getDescription();
            }
            if (composition != null && isWrapped()) {
                return composition.getDescription();
            }
            return targetDescription;
        }

        @Override
        public void setDescription(String description) {
            if (component instanceof Field) {
                ((Field) component).setDescription(description);
            } else if (composition != null && isWrapped()) {
                ((CubaFieldWrapper) composition).setDescription(description);
            } else {
                this.targetDescription = description;
            }
        }

        @Override
        public String getInputPrompt() {
            if (component instanceof HasInputPrompt) {
                return ((HasInputPrompt) component).getInputPrompt();
            }
            return targetInputPrompt;
        }

        @Override
        public void setInputPrompt(String inputPrompt) {
            if (component instanceof HasInputPrompt) {
                ((HasInputPrompt) component).setInputPrompt(inputPrompt);
            } else {
                this.targetInputPrompt = inputPrompt;
            }
        }

        @Override
        public String getContextHelpText() {
            if (component instanceof Field) {
                return ((Field) component).getContextHelpText();
            }
            if (composition != null && isWrapped()) {
                return composition.getContextHelpText();
            }
            return targetContextHelpText;
        }

        @Override
        public void setContextHelpText(String contextHelpText) {
            if (component instanceof Field) {
                ((Field) component).setContextHelpText(contextHelpText);
            } else if (composition != null && isWrapped()) {
                composition.setContextHelpText(contextHelpText);
            } else {
                this.targetContextHelpText = contextHelpText;
            }
        }

        @Override
        public Boolean isContextHelpTextHtmlEnabled() {
            if (component instanceof Field) {
                return ((Field) component).isContextHelpTextHtmlEnabled();
            }
            if (composition != null && isWrapped()) {
                return composition.isContextHelpTextHtmlEnabled();
            }
            return BooleanUtils.isTrue(targetContextHelpTextHtmlEnabled);
        }

        @Override
        public void setContextHelpTextHtmlEnabled(Boolean enabled) {
            if (component instanceof Field) {
                checkNotNullArgument(enabled, "Unable to reset contextHelpTextHtmlEnabled " +
                        "flag for the bound FieldConfig");
                ((Field) component).setContextHelpTextHtmlEnabled(enabled);
            } else if (composition != null && isWrapped()) {
                checkNotNullArgument(enabled, "Unable to reset contextHelpTextHtmlEnabled " +
                        "flag for the bound FieldConfig");
                composition.setContextHelpTextHtmlEnabled(enabled);
            } else {
                this.targetContextHelpTextHtmlEnabled = enabled;
            }
        }

        @Override
        public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
            if (component instanceof Field) {
                return ((Field) component).getContextHelpIconClickHandler();
            }
            return targetContextHelpIconClickHandler;
        }

        @Override
        public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
            if (component instanceof Field) {
                ((Field) component).setContextHelpIconClickHandler(handler);
            } else {
                this.targetContextHelpIconClickHandler = handler;
            }
        }

        @Override
        public Formatter getFormatter() {
            if (component instanceof HasFormatter) {
                return ((HasFormatter) component).getFormatter();
            }
            return targetFormatter;
        }

        @Override
        public void setFormatter(Formatter formatter) {
            if (component instanceof HasFormatter) {
                ((HasFormatter) component).setFormatter(formatter);
            } else {
                this.targetFormatter = formatter;
            }
        }

        @Override
        public Element getXmlDescriptor() {
            return xmlDescriptor;
        }

        @Override
        public void setXmlDescriptor(Element element) {
            this.xmlDescriptor = element;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        @Nullable
        public com.vaadin.ui.Field getComposition() {
            return composition;
        }

        public com.vaadin.ui.Field getCompositionNN() {
            if (composition == null) {
                throw new IllegalStateException("FieldConfig is not bound to a Component");
            }
            return composition;
        }

        public void setComposition(com.vaadin.ui.Field composition) {
            checkState(this.composition == null, "Unable to change composition for bound FieldConfig");

            this.composition = composition;

            if (isWrapped()) {
                if (targetCaption != null) {
                    composition.setCaption(targetCaption);
                }
                if (targetRequired != null) {
                    composition.setRequired(targetRequired);
                }
                if (targetRequiredMessage != null) {
                    composition.setRequiredError(targetRequiredMessage);
                }
                if (targetDescription != null) {
                    ((CubaFieldWrapper) composition).setDescription(targetDescription);
                }
            }
        }

        public boolean isManaged() {
            return managed;
        }

        public void setManaged(boolean managed) {
            this.managed = managed;
        }

        public String getTargetWidth() {
            return targetWidth;
        }

        public void setTargetWidth(String targetWidth) {
            this.targetWidth = targetWidth;
        }

        public String getTargetStylename() {
            return targetStylename;
        }

        public void setTargetStylename(String targetStylename) {
            this.targetStylename = targetStylename;
        }

        public void setTargetDatasource(Datasource targetDatasource) {
            this.targetDatasource = targetDatasource;
        }

        public Boolean getTargetRequired() {
            return targetRequired;
        }

        public void setTargetRequired(Boolean targetRequired) {
            this.targetRequired = targetRequired;
        }

        public Boolean getTargetEditable() {
            return targetEditable;
        }

        public void setTargetEditable(Boolean targetEditable) {
            this.targetEditable = targetEditable;
        }

        public Boolean getTargetEnabled() {
            return targetEnabled;
        }

        public void setTargetEnabled(Boolean targetEnabled) {
            this.targetEnabled = targetEnabled;
        }

        public Boolean getTargetVisible() {
            return targetVisible;
        }

        public void setTargetVisible(Boolean targetVisible) {
            this.targetVisible = targetVisible;
        }

        public String getTargetProperty() {
            return targetProperty;
        }

        public void setTargetProperty(String targetProperty) {
            this.targetProperty = targetProperty;
        }

        public Integer getTargetTabIndex() {
            return targetTabIndex;
        }

        public void setTargetTabIndex(Integer targetTabIndex) {
            this.targetTabIndex = targetTabIndex;
        }

        public String getTargetRequiredMessage() {
            return targetRequiredMessage;
        }

        public void setTargetRequiredMessage(String targetRequiredMessage) {
            this.targetRequiredMessage = targetRequiredMessage;
        }

        public String getTargetContextHelpText() {
            return targetContextHelpText;
        }

        public void setTargetContextHelpText(String targetContextHelpText) {
            this.targetContextHelpText = targetContextHelpText;
        }

        public Boolean getTargetContextHelpTextHtmlEnabled() {
            return targetContextHelpTextHtmlEnabled;
        }

        public void setTargetContextHelpTextHtmlEnabled(Boolean targetContextHelpTextHtmlEnabled) {
            this.targetContextHelpTextHtmlEnabled = targetContextHelpTextHtmlEnabled;
        }

        public Consumer<ContextHelpIconClickEvent> getTargetContextHelpIconClickHandler() {
            return targetContextHelpIconClickHandler;
        }

        public void setTargetContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> targetContextHelpIconClickHandler) {
            this.targetContextHelpIconClickHandler = targetContextHelpIconClickHandler;
        }

        public CollectionDatasource getTargetOptionsDatasource() {
            return targetOptionsDatasource;
        }

        public void setTargetOptionsDatasource(CollectionDatasource targetOptionsDatasource) {
            this.targetOptionsDatasource = targetOptionsDatasource;
        }

        public String getTargetCaption() {
            return targetCaption;
        }

        public void setTargetCaption(String targetCaption) {
            this.targetCaption = targetCaption;
        }

        public String getTargetDescription() {
            return targetDescription;
        }

        public void setTargetDescription(String targetDescription) {
            this.targetDescription = targetDescription;
        }

        public String getTargetInputPrompt() {
            return targetInputPrompt;
        }

        public void setTargetInputPrompt(String targetInputPrompt) {
            this.targetInputPrompt = targetInputPrompt;
        }

        public Formatter getTargetFormatter() {
            return targetFormatter;
        }

        public void setTargetFormatter(Formatter targetFormatter) {
            this.targetFormatter = targetFormatter;
        }

        public List<Field.Validator> getTargetValidators() {
            return targetValidators;
        }

        public void setTargetValidators(List<Field.Validator> targetValidators) {
            this.targetValidators = targetValidators;
        }

        public FieldAttachMode getAttachMode() {
            return attachMode;
        }

        @Override
        public String toString() {
            return "FieldConfig: " + id;
        }
    }
}