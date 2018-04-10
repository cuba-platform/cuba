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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigLayoutHelper;
import com.haulmont.cuba.desktop.sys.vcl.CollapsiblePanel;
import com.haulmont.cuba.desktop.sys.vcl.ToolTipButton;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionDescriptor;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionValue;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class DesktopFieldGroup extends DesktopAbstractComponent<JPanel>
        implements FieldGroup, AutoExpanding, Component.UiPermissionAware {

    private final Logger log = LoggerFactory.getLogger(DesktopFieldGroup.class);

    public static final String DEFAULT_FIELD_WIDTH = "200px";

    protected String description;

    protected MigLayout layout;
    protected Datasource datasource;
    protected CollapsiblePanel collapsiblePanel;

    protected int rows = 0;
    protected int cols = 1;

    protected boolean editable = true;
    protected boolean borderVisible = false;

    protected FieldCaptionAlignment captionAlignment;

    protected Map<String, FieldConfig> fields = new HashMap<>();
    protected List<List<FieldConfig>> columnFieldMapping = new ArrayList<>();
    {
        columnFieldMapping.add(new ArrayList<>());
    }

    protected Map<Integer, Integer> columnFieldCaptionWidth = null;
    protected int fieldCaptionWidth = -1;

    protected boolean requestUpdateCaptionWidth = false;

    protected List<EditableChangeListener> editableChangeListeners = new ArrayList<>();

    protected FieldGroupFieldFactory fieldFactory;

    public DesktopFieldGroup() {
        LC lc = new LC();
        lc.hideMode(3); // Invisible components will not participate in the layout at all and it will for instance not take up a grid cell.
        lc.insets("0 0 0 0");
        if (LayoutAdapter.isDebug()) {
            lc.debug(1000);
        }

        layout = new MigLayout(lc);
        impl = new JPanel(layout);
        assignClassDebugProperty(impl);
        collapsiblePanel = new CollapsiblePanel(super.getComposition());
        assignClassDebugProperty(collapsiblePanel);
        collapsiblePanel.setBorderVisible(false);

        setWidth(Component.AUTO_SIZE);

        fieldFactory = AppBeans.get(FieldGroupFieldFactory.NAME);
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
        for (final Map.Entry<String, FieldConfig> entry : fields.entrySet()) {
            if (entry.getKey().equals(fieldId)) {
                return entry.getValue();
            }
        }
        return null;
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

    @Override
    public List<Component> getOwnComponents() {
        return getColumnOrderedFields().stream()
                .filter(FieldConfig::isBound)
                .map(FieldConfig::getComponent)
                .collect(Collectors.toList());
    }

    @Override
    public void addField(FieldConfig field) {
        addField(field, 0);
    }

    @Override
    public void addField(FieldConfig fc, int colIndex) {
        checkArgument(!fields.containsKey(fc.getId()), "Field '%s' is already registered", fc.getId());
        checkArgument(this == ((FieldConfigImpl) fc).getOwner(), "Field does not belong to this FieldGroup");

        if (colIndex < 0 || colIndex >= getColumns()) {
            throw new IllegalArgumentException(String.format("Illegal column number %s, available amount of columns is %s",
                    colIndex, getColumns()));
        }

        addFieldInternal(fc, colIndex, -1);
    }

    @Override
    public void addField(FieldConfig fc, int colIndex, int rowIndex) {
        checkArgument(!fields.containsKey(fc.getId()), "Field '%s' is already registered", fc.getId());
        checkArgument(this == ((FieldConfigImpl) fc).getOwner(), "Field does not belong to this FieldGroup");

        if (colIndex < 0 || colIndex >= getColumns()) {
            throw new IllegalArgumentException(String.format("Illegal column number %s, available amount of columns is %s",
                    colIndex, getColumns()));
        }
        List<FieldConfig> colFields = columnFieldMapping.get(colIndex);
        if (rowIndex < 0 || rowIndex > colFields.size()) {
            throw new IllegalArgumentException(String.format("Illegal row number %s, available amount of rows is %s",
                    rowIndex, getRows()));
        }

        addFieldInternal(fc, colIndex, rowIndex);
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

            impl.revalidate();
            impl.repaint();
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

            boolean wasBound = fc.isBound();
            if (fc.isBound()) {
                FieldConfigImpl fci = (FieldConfigImpl) fc;

                impl.remove(fci.getCompositionNN().getComposition());
                if (fci.getLabel() != null) {
                    impl.remove(fci.getLabel());
                }
                if (fci.getToolTipButton() != null) {
                    impl.remove(fci.getToolTipButton());
                }

                reattachColumnFields(colIndex);

                this.rows = detectRowsCount();
            }

            ((FieldConfigImpl) fc).setManaged(false);

            if (fc.getComponent() != null) {
                fc.getComponent().setParent(null);
            }

            if (wasBound) {
                impl.revalidate();
                impl.repaint();
            }
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

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

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

                setupFieldComponent(fci);

                DesktopAbstractComponent fieldImpl = (DesktopAbstractComponent) fieldComponent;
                fci.setComposition(fieldImpl);

                assignTypicalAttributes(fieldComponent);

                if (generatedField.getAttachMode() == FieldAttachMode.APPLY_DEFAULTS) {
                    applyFieldDefaults(fci);
                }

                int columnIndex = fci.getColumn();
                if (!reattachColumns.contains(columnIndex)) {
                    reattachColumns.add(columnIndex);
                }
            }
        }

        if (!reattachColumns.isEmpty()) {
            this.rows = detectRowsCount();

            for (Integer reattachColumnIndex : reattachColumns) {
                reattachColumnFields(reattachColumnIndex);
            }
        }

        impl.revalidate();
        impl.repaint();
    }

    protected void setupFieldComponent(FieldConfig fieldConfig) {
        Component fieldComponent = fieldConfig.getComponent();

        if (fieldComponent instanceof DesktopCheckBox) {
            fieldComponent.setAlignment(Alignment.MIDDLE_LEFT);
        }
    }

    protected void reattachColumnFields(int colIndex) {
        fields.values().stream()
                .filter(FieldConfig::isBound)
                .map(fieldConfig -> ((FieldConfigImpl) fieldConfig))
                .filter(fci -> fci.getColumn() == colIndex)
                .forEach(fci -> {
                    impl.remove(fci.getCompositionNN().getComposition());
                    if (fci.getLabel() != null) {
                        impl.remove(fci.getLabel());
                    }
                    if (fci.getToolTipButton() != null) {
                        impl.remove(fci.getToolTipButton());
                    }
                });

        List<FieldConfig> columnFCs = columnFieldMapping.get(colIndex);
        int insertRowIndex = 0;
        for (FieldConfig fc : columnFCs) {
            if (fc.isBound()) {
                FieldConfigImpl fci = (FieldConfigImpl) fc;

                Component fieldComponent = fci.getComponentNN();
                JComponent composition = fieldComponent.unwrapComposition(JComponent.class);

                JLabel label = fci.getLabel();
                if (label != null) {
                    int preferredCaptionWidth = getPreferredCaptionWidth(colIndex);
                    if (preferredCaptionWidth > 0) {
                        label.setPreferredSize(new Dimension(preferredCaptionWidth, 25));
                        label.setMaximumSize(new Dimension(preferredCaptionWidth, 25));
                        label.setMinimumSize(new Dimension(preferredCaptionWidth, 25));
                    } else {
                        label.setPreferredSize(new Dimension(label.getPreferredSize().width, 25));
                    }
                    label.setVisible(fieldComponent.isVisible());

                    CC labelCc = new CC();
                    MigLayoutHelper.applyAlignment(labelCc, Alignment.TOP_LEFT);

                    impl.add(label, labelCc.cell(colIndex * 3, insertRowIndex, 1, 1));
                }

                ToolTipButton toolTipButton = fci.getToolTipButton();
                if (fci.getToolTipButton() != null) {
                    updateTooltipButton(fci, fieldComponent);

                    DesktopToolTipManager.getInstance().registerTooltip(toolTipButton);
                    impl.add(toolTipButton, new CC().cell(colIndex * 3 + 2, insertRowIndex, 1, 1).alignY("top"));
                }

                CC cell = new CC().cell(colIndex * 3 + 1, insertRowIndex, 1, 1);

                MigLayoutHelper.applyWidth(cell, (int) fieldComponent.getWidth(), fieldComponent.getWidthSizeUnit(), false);
                MigLayoutHelper.applyHeight(cell, (int) fieldComponent.getHeight(), fieldComponent.getHeightSizeUnit(), false);
                MigLayoutHelper.applyAlignment(cell, fieldComponent.getAlignment());

                composition.putClientProperty(getSwingPropertyId(), fci.getId());
                impl.add(composition, cell);

                insertRowIndex++;
            }
        }

        impl.validate();
        impl.repaint();
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

    protected void managedFieldComponentAssigned(FieldConfigImpl fci, FieldAttachMode mode) {
        DesktopAbstractComponent fieldImpl = (DesktopAbstractComponent) fci.getComponentNN();
        fci.setComposition(fieldImpl);

        if (StringUtils.isNotEmpty(fci.getContextHelpText())
                || hasContextHelpIconClickListeners(fci.getComponentNN())) {
            updateTooltipButton(fci, fci.getComponentNN());
        }

        if (fieldImpl.getCaption() != null) {
            fci.getLabel().setText(fieldImpl.getCaption());
        }

        assignTypicalAttributes(fci.getComponentNN());

        if (mode == FieldAttachMode.APPLY_DEFAULTS) {
            applyFieldDefaults(fci);
        }

        this.rows = detectRowsCount();

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
                fieldComponent.setWidth(DEFAULT_FIELD_WIDTH);
            }
        } else {
            DesktopAbstractComponent composition = fci.getCompositionNN();
            if (fci.getTargetCaption() != null) {
                fci.getLabel().setText(fci.getTargetCaption());
            }
            if (fci.getTargetVisible() != null) {
                composition.setVisible(fci.getTargetVisible());
            }
            if (fci.getTargetWidth() != null) {
                composition.setWidth(fci.getTargetWidth());
            } else {
                composition.setWidth(DEFAULT_FIELD_WIDTH);
            }
        }

        if (fieldComponent instanceof Component.HasFormatter && fci.getTargetFormatter() != null) {
            ((Component.HasFormatter) fieldComponent).setFormatter(fci.getTargetFormatter());
        }

        if (StringUtils.isNotEmpty(fci.getTargetStylename())) {
            fieldComponent.setStyleName(fci.getTargetStylename());
        }

        App app = App.getInstance();
        if (app != null && app.isTestMode()) {
            fci.getCompositionNN().getComposition().setName(fci.getId());
        }
    }

    protected void doSetParentEnabled(FieldConfig fc, boolean enabled) {
        if (fc.getComponent() != null) {
            Component component = fc.getComponent();
            if (component instanceof DesktopAbstractComponent) {
                ((DesktopAbstractComponent) component).setParentEnabled(enabled);
            }

            FieldConfigImpl fci = (FieldConfigImpl) fc;
            if (fci.getLabel() != null) {
                fci.getLabel().setEnabled(enabled);
            }
        }
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        for (FieldConfig field : fields.values()) {
            doSetParentEnabled(field, parentEnabled && enabled);
        }
    }

    @Override
    public boolean isBorderVisible() {
        return borderVisible;
    }

    @Override
    public void setBorderVisible(boolean borderVisible) {
        this.borderVisible = borderVisible;
        collapsiblePanel.setBorderVisible(borderVisible);
    }

    @Override
    public FieldCaptionAlignment getCaptionAlignment() {
        return captionAlignment;
    }

    @Override
    public void setCaptionAlignment(FieldCaptionAlignment captionAlignment) {
        this.captionAlignment = captionAlignment;
        
        log.warn("setCaptionAlignment not implemented for desktop");
    }

    @Override
    public int getFieldCaptionWidth() {
        return fieldCaptionWidth;
    }

    @Override
    public void setFieldCaptionWidth(int fixedCaptionWidth) {
        this.fieldCaptionWidth = fixedCaptionWidth;

        updateCaptionWidths();
    }

    @Override
    public int getFieldCaptionWidth(int column) {
        if (columnFieldCaptionWidth != null) {
            Integer value = columnFieldCaptionWidth.get(column);
            return value != null ? value : -1;
        }
        return -1;
    }

    @Override
    public void setFieldCaptionWidth(int column, int width) {
        if (columnFieldCaptionWidth == null) {
            columnFieldCaptionWidth = new HashMap<>();
        }
        columnFieldCaptionWidth.put(column, width);

        updateCaptionWidths();
    }

    protected void updateCaptionWidths() {
        if (!requestUpdateCaptionWidth) {
            SwingUtilities.invokeLater(() -> {
                requestUpdateCaptionWidth = false;

                for (FieldConfig fieldConfig : fields.values()) {
                    JLabel label = ((FieldConfigImpl) fieldConfig).getLabel();

                    if (label != null) {
                        int col = ((FieldConfigImpl) fieldConfig).getColumn();
                        int preferredCaptionWidth = getPreferredCaptionWidth(col);

                        if (preferredCaptionWidth > 0) {
                            label.setPreferredSize(new Dimension(preferredCaptionWidth, 25));
                            label.setMaximumSize(new Dimension(preferredCaptionWidth, 25));
                            label.setMinimumSize(new Dimension(preferredCaptionWidth, 25));
                        }
                    }
                }
            });
            requestUpdateCaptionWidth = true;
        }
    }

    protected int getPreferredCaptionWidth(int col) {
        int preferredCaptionWidth = -1;
        if (fieldCaptionWidth > 0) {
            preferredCaptionWidth = fieldCaptionWidth;
        }
        if (columnFieldCaptionWidth != null
                && columnFieldCaptionWidth.containsKey(col)) {
            preferredCaptionWidth = columnFieldCaptionWidth.get(col);
        }
        return preferredCaptionWidth;
    }

    @Override
    public int getColumns() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public DesktopAbstractComponent getCellComponent(int colIndex, int rowIndex) {
        if (colIndex < 0 || colIndex >= getColumns()) {
            throw new IllegalArgumentException(String.format("Illegal column number %s, available amount of columns is %s",
                    colIndex, getColumns()));
        }
        List<FieldConfig> colFields = columnFieldMapping.get(colIndex);
        if (rowIndex < 0 || rowIndex > colFields.size()) {
            throw new IllegalArgumentException(String.format("Illegal column number %s, available amount of columns is %s",
                    colIndex, getColumns()));
        }

        for (FieldConfig fieldConfig : fields.values()) {
            DesktopAbstractComponent composition = ((FieldConfigImpl) fieldConfig).getComposition();
            if (composition != null) {
                JComponent jComponent = composition.getComposition();
                Object componentConstraints = layout.getComponentConstraints(jComponent);
                if (componentConstraints instanceof CC) {
                    CC cc = (CC) componentConstraints;
                    if (cc.getCellY() == rowIndex) {
                        int ccColIndex = (cc.getCellX() - 1) / 3;
                        if (colIndex == ccColIndex) {
                            return composition;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void setColumns(int columns) {
        if (this.cols != columns) {
            this.cols = columns;

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
    public float getColumnExpandRatio(int col) {
        return 0;
    }

    @Override
    public void setColumnExpandRatio(int col, float ratio) {
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
    public void addCustomField(String fieldId, CustomFieldGenerator fieldGenerator) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        addCustomField(field, fieldGenerator);
    }

    @Override
    public void addCustomField(FieldConfig fc, CustomFieldGenerator fieldGenerator) {
        if (!fc.isCustom()) {
            throw new IllegalStateException(String.format("Field '%s' must be defined as custom", fc.getId()));
        }

        FieldConfigImpl fci = (FieldConfigImpl) fc;

        Component fieldComponent = fieldGenerator.generateField(fc.getTargetDatasource(), fci.getTargetProperty());
        fc.setComponent(fieldComponent);
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (id != null && App.getInstance().isTestMode()) {
            for (FieldConfig fc : fields.values()) {
                Component fieldComponent = fc.getComponent();
                if (fieldComponent != null) {
                    JComponent jComponent = DesktopComponentsHelper.getComposition(fieldComponent);
                    if (jComponent != null) {
                        jComponent.setName(fc.getId());
                    }
                }
            }
        }
    }

    protected void assignTypicalAttributes(Component c) {
        if (c instanceof BelongToFrame) {
            BelongToFrame belongToFrame = (BelongToFrame) c;
            if (belongToFrame.getFrame() == null) {
                belongToFrame.setFrame(getFrame());
            }
        }
        c.setParent(this);
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        if (editable != isEditable()) {
            this.editable = editable;

            EditableChangeEvent event = new EditableChangeEvent(this);
            for (EditableChangeListener listener : new ArrayList<>(editableChangeListeners)) {
                listener.editableChanged(event);
            }
        }
    }

    @Override
    public String getCaption() {
        return collapsiblePanel.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        collapsiblePanel.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public JComponent getComposition() {
        return collapsiblePanel;
    }

    @Override
    public boolean expandsWidth() {
        return true;
    }

    @Override
    public boolean expandsHeight() {
        return false;
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
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId())) {
            return "fieldGroup_" + datasource.getId();
        }

        return getClass().getSimpleName();
    }

    public void updateCaptionVisibility(DesktopAbstractComponent child) {
        FieldConfig field = fields.values().stream()
                .filter(entry -> entry.getComponent() == child)
                .findFirst()
                .orElse(null);

        FieldConfigImpl fci = (FieldConfigImpl) field;
        if (fci != null && fci.getLabel() != null) {
            fci.getLabel().setVisible(child.isComponentVisible());
        }
    }

    public void updateCaptionText(DesktopAbstractComponent child) {
        FieldConfig field = fields.values().stream()
                .filter(entry -> entry.getComponent() == child)
                .findFirst()
                .orElse(null);

        FieldConfigImpl fci = (FieldConfigImpl) field;
        if (fci != null && fci.getLabel() != null) {
            fci.getLabel().setText(child.getCaption());
        }
    }

    public void updateChildEnabled(DesktopAbstractComponent child) {
        FieldConfig field = fields.values().stream()
                .filter(entry -> entry.getComponent() == child)
                .findFirst()
                .orElse(null);

        FieldConfigImpl fci = (FieldConfigImpl) field;
        if (fci != null && fci.getLabel() != null) {
            fci.getLabel().setEnabled(child.isEnabledWithParent());
        }
    }

    public void updateContextHelp(DesktopAbstractComponent child) {
        FieldConfig field = fields.values().stream()
                .filter(entry -> entry.getComponent() == child)
                .findFirst()
                .orElse(null);

        FieldConfigImpl fci = (FieldConfigImpl) field;
        if (fci != null) {
            updateTooltipButton(fci, child);
        }
    }

    protected void updateTooltipButton(FieldConfigImpl fci, Component component) {
        ToolTipButton toolTipButton = fci.getToolTipButton();

        boolean hasContextHelpIconClickListeners = hasContextHelpIconClickListeners(component);
        String contextHelpText = DesktopComponentsHelper.getContextHelpText(
                fci.getContextHelpText(),
                BooleanUtils.isTrue(fci.isContextHelpTextHtmlEnabled()));

        ActionListener toolTipButtonActionListener = fci.getToolTipButtonActionListener();
        if (hasContextHelpIconClickListeners) {
            if (toolTipButtonActionListener == null) {
                toolTipButtonActionListener = e ->
                        fireContextHelpIconClickEvent(component);
                toolTipButton.addActionListener(toolTipButtonActionListener);
            }

            toolTipButton.setToolTipText(null);
        } else {
            if (toolTipButtonActionListener != null) {
                toolTipButton.removeActionListener(toolTipButtonActionListener);
                fci.setToolTipButtonActionListener(null);
            }

            toolTipButton.setToolTipText(contextHelpText);
        }

        toolTipButton.setVisible(component.isVisible()
                && (StringUtils.isNotEmpty(contextHelpText) || hasContextHelpIconClickListeners));
    }

    protected boolean hasContextHelpIconClickListeners(Component component) {
        return component instanceof HasContextHelpClickHandler
                && ((HasContextHelpClickHandler) component).getContextHelpIconClickHandler() != null;
    }

    protected void fireContextHelpIconClickEvent(Component component) {
        if (component instanceof HasContextHelpClickHandler) {
            ContextHelpIconClickEvent event = new ContextHelpIconClickEvent((HasContextHelp) component);
            ((HasContextHelpClickHandler) component).fireContextHelpIconClickEvent(event);
        }
    }

    @Override
    public void applyPermission(UiPermissionDescriptor permissionDescriptor) {
        checkNotNullArgument(permissionDescriptor);

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
        checkNotNullArgument(listener);

        if (!editableChangeListeners.contains(listener)) {
            editableChangeListeners.add(listener);
        }
    }

    @Override
    public void removeEditableChangeListener(EditableChangeListener listener) {
        checkNotNullArgument(listener);

        editableChangeListeners.remove(listener);
    }

    public class FieldConfigImpl implements FieldConfig {
        protected String id;
        protected Element xmlDescriptor;
        protected int column;

        protected Component component;
        protected DesktopAbstractComponent composition;
        protected JLabel label = new JLabel();
        protected ToolTipButton toolTipButton = new ToolTipButton();

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

        protected ActionListener toolTipButtonActionListener;

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
            return DesktopFieldGroup.this;
        }

        @Override
        public String getWidth() {
            if (composition != null && isWrapped()) {
                return ComponentsHelper.getComponentWidth(composition);
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
            return component != null && !(component instanceof Field);
        }

        @Override
        public Datasource getTargetDatasource() {
            if (component instanceof DatasourceComponent) {
                return ((DatasourceComponent) component).getDatasource();
            }
            if (targetDatasource != null) {
                return targetDatasource;
            }
            return DesktopFieldGroup.this.datasource;
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
            return targetRequiredMessage;
        }

        @Override
        public void setRequiredMessage(String requiredMessage) {
            if (component instanceof Field) {
                ((Field) component).setRequiredMessage(requiredMessage);
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
                return label.getText();
            }
            return targetCaption;
        }

        @Override
        public void setCaption(String caption) {
            if (component instanceof Field) {
                ((Field) component).setCaption(caption);
            } else if (composition != null && isWrapped()) {
                label.setText(caption);
            } else {
                this.targetCaption = caption;
            }
        }

        @Override
        public String getContextHelpText() {
            if (component instanceof Field) {
                return ((Field) component).getContextHelpText();
            }
            return targetContextHelpText;
        }

        @Override
        public void setContextHelpText(String contextHelpText) {
            if (component instanceof Field) {
                ((Field) component).setContextHelpText(contextHelpText);
            } else {
                this.targetContextHelpText = contextHelpText;
            }
        }

        @Override
        public Boolean isContextHelpTextHtmlEnabled() {
            if (component instanceof Field) {
                return ((Field) component).isContextHelpTextHtmlEnabled();
            }
            return targetContextHelpTextHtmlEnabled;
        }

        @Override
        public void setContextHelpTextHtmlEnabled(Boolean enabled) {
            if (component instanceof Field) {
                ((Field) component).setContextHelpTextHtmlEnabled(enabled);
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
        public String getDescription() {
            if (component instanceof Field) {
                return ((Field) component).getDescription();
            }
            return targetDescription;
        }

        @Override
        public void setDescription(String description) {
            if (component instanceof Field) {
                ((Field) component).setDescription(description);
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
        public DesktopAbstractComponent getComposition() {
            return composition;
        }

        public DesktopAbstractComponent getCompositionNN() {
            if (composition == null) {
                throw new IllegalStateException("FieldConfig is not bound to a Component");
            }
            return composition;
        }

        public void setComposition(DesktopAbstractComponent composition) {
            checkState(this.composition == null, "Unable to change composition for bound FieldConfig");

            this.composition = composition;

            if (isWrapped()) {
                if (targetCaption != null) {
                    label.setText(targetCaption);
                } else {
                    label.setText(composition.getCaption());
                }
            }
        }

        public boolean isManaged() {
            return managed;
        }

        public void setManaged(boolean managed) {
            this.managed = managed;
        }

        public ActionListener getToolTipButtonActionListener() {
            return toolTipButtonActionListener;
        }

        public void setToolTipButtonActionListener(ActionListener toolTipButtonActionListener) {
            this.toolTipButtonActionListener = toolTipButtonActionListener;
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

        public JLabel getLabel() {
            return label;
        }

        public void setLabel(JLabel label) {
            this.label = label;
        }

        public ToolTipButton getToolTipButton() {
            return toolTipButton;
        }

        public void setToolTipButton(ToolTipButton toolTipButton) {
            this.toolTipButton = toolTipButton;
        }

        @Override
        public String toString() {
            return "FieldConfig: " + id;
        }
    }
}