/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.ImmutableList;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionDescriptor;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionValue;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ValueSourceProvider;
import com.haulmont.cuba.gui.components.form.ComponentArea;
import com.haulmont.cuba.gui.components.form.ComponentPosition;
import com.haulmont.cuba.gui.components.security.ActionsPermissions;
import com.haulmont.cuba.gui.sys.TestIdManager;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.widgets.CubaFieldGroupLayout;
import com.vaadin.ui.GridLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebForm extends WebAbstractComponent<CubaFieldGroupLayout> implements Form, UiPermissionAware {

    private static final Logger log = LoggerFactory.getLogger(WebForm.class);

    protected List<List<ComponentPosition>> columnComponentMapping = new ArrayList<>();

    protected ValueSourceProvider valueSourceProvider;

    public WebForm() {
        // add first column
        columnComponentMapping.add(new ArrayList<>());

        component = createComponent();
    }

    protected CubaFieldGroupLayout createComponent() {
        return new CubaFieldGroupLayout();
    }

    protected boolean editable = true;

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        AppUI ui = AppUI.getCurrent();
        if (ui != null && id != null) {
            for (final Component component : getOwnComponents()) {
                com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(component);
                if (composition != null) {
                    composition.setId(ui.getTestIdManager().getTestId(id + "_" + component.getId()));
                }
            }
        }
    }

    @Override
    public boolean isEditable() {
        return this.editable;
    }

    @Override
    public void setEditable(boolean editable) {
        if (editable != isEditable()) {
            this.editable = editable;

            EditableChangeEvent event = new EditableChangeEvent(this);
            publish(EditableChangeEvent.class, event);
        }
    }

    @Override
    public Subscription addEditableChangeListener(Consumer<EditableChangeEvent> listener) {
        return getEventHub().subscribe(EditableChangeEvent.class, listener);
    }

    @Override
    public void removeEditableChangeListener(Consumer<EditableChangeEvent> listener) {
        unsubscribe(EditableChangeEvent.class, listener);
    }

    @Override
    public void add(Component childComponent) {
        add(childComponent, 0);
    }

    @Override
    public void add(Component childComponent, int column) {
        int rowIndex = detectRowsCount(column);
        add(childComponent, column, rowIndex);
    }

    @Override
    public void add(Component childComponent, int column, int colSpan, int rowSpan) {
        int rowIndex = detectRowsCount(column);
        add(childComponent, column, rowIndex, colSpan, rowSpan);
    }

    @Override
    public void add(Component childComponent, int column, int row) {
        add(childComponent, column, row, 1, 1);
    }

    @Override
    public void add(Component childComponent, int column, int row, int colSpan, int rowSpan) {
        checkArgument(column >= 0 && column < this.component.getColumns(),
                "Illegal column number %s, must be between [0; %s]", column, this.component.getColumns());

        checkArgument((column + colSpan) <= this.component.getColumns(),
                "Illegal colSpan number %s, available amount of columns is %s, column number is %s",
                colSpan, this.component.getColumns(), column);

        int rowsCount = detectRowsCount(column);
        checkArgument(row >= 0 && row <= rowsCount,
                "Illegal row number %s, available amount of rows in column %s is %s", row, column, rowsCount);

        checkExistingOverlaps(column, row, colSpan, rowSpan);

        addComponentInternal(childComponent, column, row, colSpan, rowSpan);
    }

    protected void checkExistingOverlaps(int startColumn, int startRow, int colSpan, int rowSpan) {
        int endColumn = startColumn + colSpan - 1;
        int endRow = startRow + rowSpan - 1;
        List<ComponentArea> componentAreas = calculateComponentAreas();
        for (ComponentArea area : componentAreas) {
            // Check that a component isn't inserted to the existing component area excluding top left point.
            if (area.getStartColumn() < startColumn && startColumn <= area.getEndColumn()
                    && area.getStartRow() <= startRow && startRow <= area.getEndRow()
                    || area.getStartRow() < startRow && startRow <= area.getEndRow()
                    && area.getStartColumn() <= startColumn && startColumn <= area.getEndColumn()) {
                throw new IllegalArgumentException(String.format(
                        "Given coordinates [%s, %s] - [%s, %s] overlap existing component: [%s, %s] - [%s, %s]",
                        startColumn, startRow, endColumn, endRow,
                        area.getStartColumn(), area.getStartRow(), area.getEndColumn(), area.getEndRow()));
            }
        }
    }

    protected void addComponentInternal(Component childComponent,
                                        int column, int row, int colSpan, int rowSpan) {
        List<ComponentPosition> componentPositions = columnComponentMapping.get(column);
        int insertIndex = calculateInsertIndex(column, row, componentPositions);

        componentPositions.add(insertIndex, new ComponentPosition(childComponent, colSpan, rowSpan));

        managedComponentAssigned(childComponent);
    }

    protected int calculateInsertIndex(int column, int row, List<ComponentPosition> componentPositions) {
        // Convert grid row index to column components array index:
        // Remove all components row spans of this column
        int insertIndex = row;
        for (int i = 0; i < componentPositions.size() && i < row; i++) {
            ComponentPosition position = componentPositions.get(i);
            if (position.getRowSpan() > 1) {
                insertIndex -= (position.getRowSpan() - 1);
            }
        }

        // Remove all components row spans from previous columns, only if a components overlaps this column
        for (int colIndex = 0; colIndex < column; colIndex++) {
            List<ComponentPosition> positions = columnComponentMapping.get(colIndex);
            for (int rowIndex = 0; rowIndex < row && rowIndex < positions.size(); rowIndex++) {
                ComponentPosition position = positions.get(rowIndex);
                if (colIndex + position.getColSpan() > column) {
                    insertIndex -= position.getRowSpan();
                }
            }
        }
        return Math.min(insertIndex, componentPositions.size());
    }

    protected void managedComponentAssigned(Component childComponent) {
        com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);
        assignTypicalAttributes(childComponent);
        assignDebugId(vComponent, childComponent.getId());
        applyDefaults(childComponent);

        this.component.setRows(detectRowsCount());

        reattachColumnFields();
    }

    protected void applyDefaults(Component childComponent) {
        if (childComponent instanceof HasCaption
                && ((HasCaption) childComponent).getCaption() == null) {
            ((HasCaption) childComponent).setCaption(" "); // Set an empty caption for proper positioning
        }
    }

    protected void assignTypicalAttributes(Component component) {
        if (getFrame() != null && component instanceof BelongToFrame) {
            BelongToFrame belongToFrame = (BelongToFrame) component;
            if (belongToFrame.getFrame() == null) {
                belongToFrame.setFrame(getFrame());
            }
        }

        component.setParent(this);
    }

    protected void assignDebugId(com.vaadin.ui.Component composition, String id) {
        AppUI ui = AppUI.getCurrent();
        if (ui == null) {
            return;
        }

        String debugId = getDebugId();
        if (ui.isPerformanceTestMode()) {
            if (composition != null && debugId != null) {
                TestIdManager testIdManager = ui.getTestIdManager();
                composition.setId(testIdManager.getTestId(debugId + "_" + id));
            }
        }
    }

    /**
     * @param column a column index
     * @return rows count for the given column index
     */
    protected int detectRowsCount(int column) {
        List<ComponentPosition> componentPositions = columnComponentMapping.get(column);

        // Calculate rows count considering row spans
        int rowsCount = componentPositions.stream()
                .map(ComponentPosition::getRowSpan)
                .reduce(Integer::sum)
                .orElse(0);

        // If a component from a previous column overlaps current column,
        // then increase rows count by its row span value
        for (int i = 0; i < column; i++) {
            List<ComponentPosition> positions = columnComponentMapping.get(i);
            for (ComponentPosition position : positions) {
                if (i + position.getColSpan() > column) {
                    rowsCount += position.getRowSpan();
                }
            }
        }

        return rowsCount;
    }

    /**
     * @return max rows count among all columns
     */
    protected int detectRowsCount() {
        int lastRowIndex = IntStream.range(0, columnComponentMapping.size())
                .map(this::detectRowsCount)
                .max().orElse(0);

        return Math.max(lastRowIndex, 1);
    }

    protected void reattachColumnFields() {
        List<ComponentArea> componentAreas = calculateComponentAreas();

        this.component.removeAllComponents();

        for (ComponentArea componentArea : componentAreas) {
            com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(componentArea.getComponent());
            this.component.addComponent(composition,
                    componentArea.getStartColumn(), componentArea.getStartRow(),
                    componentArea.getEndColumn(), componentArea.getEndRow());
        }
    }

    protected List<ComponentArea> calculateComponentAreas() {
        List<ComponentArea> componentAreas = new ArrayList<>();
        // Inspired by GridLayoutLoader logic
        boolean[][] spanMatrix = new boolean[this.component.getColumns()][this.component.getRows()];

        for (int col = 0; col < columnComponentMapping.size(); col++) {
            int row = 0;
            List<ComponentPosition> columnFields = columnComponentMapping.get(col);
            for (ComponentPosition componentPosition : columnFields) {

                while (spanMatrix[col][row]) {
                    row++;
                }

                Component component = componentPosition.getComponent();
                int colSpan = componentPosition.getColSpan();
                int rowSpan = componentPosition.getRowSpan();

                if (colSpan == 1 && rowSpan == 1) {
                    componentAreas.add(new ComponentArea(component, col, row, col, row));
                } else {
                    fillSpanMatrix(spanMatrix, col, row, colSpan, rowSpan);

                    int endColumn = col + colSpan - 1;
                    int endRow = row + rowSpan - 1;

                    componentAreas.add(new ComponentArea(component, col, row, endColumn, endRow));
                }

                row++;
            }

        }

        return componentAreas;
    }

    protected void fillSpanMatrix(boolean[][] spanMatrix, int col, int row, int colspan, int rowspan) {
        for (int i = col; i < (col + colspan); i++) {
            for (int j = row; j < (row + rowspan); j++) {
                if (spanMatrix[i][j]) {
                    throw new IllegalStateException(String.format("Can't insert a component to the given location: " +
                                    "col - %s, row - %s, colspan - %s, rowspan - %s",
                            col, row, colspan, rowspan));
                }

                spanMatrix[i][j] = true;
            }
        }
    }

    @Override
    public void remove(Component childComponent) {
        for (List<ComponentPosition> components : columnComponentMapping) {
            ComponentPosition toRemove = findComponentPosition(components, childComponent);
            if (toRemove != null) {
                components.remove(toRemove);
                reattachColumnFields();
                component.setRows(detectRowsCount());
                childComponent.setParent(null);
                break;
            }
        }
    }

    @Nullable
    protected ComponentPosition findComponentPosition(List<ComponentPosition> components, Component component) {
        for (ComponentPosition componentPosition : components) {
            if (componentPosition.getComponent().equals(component)) {
                return componentPosition;
            }
        }
        return null;
    }

    @Override
    public void removeAll() {
        component.removeAllComponents();

        List<Component> components = new ArrayList<>(getOwnComponents());

        columnComponentMapping.clear();
        columnComponentMapping.add(new ArrayList<>());
        component.setColumns(1);

        for (Component component : components) {
            component.setParent(null);
        }
    }

    @Nullable
    @Override
    public Component getOwnComponent(String id) {
        Preconditions.checkNotNullArgument(id);

        return getOwnComponents().stream()
                .filter(component -> Objects.equals(id, component.getId()))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return columnComponentMapping.stream()
                .flatMap(List::stream)
                .map(ComponentPosition::getComponent)
                .collect(Collectors.toList());
    }

    @Override
    public Stream<Component> getOwnComponentsStream() {
        return columnComponentMapping.stream()
                .flatMap(List::stream)
                .map(ComponentPosition::getComponent);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public Collection<Component> getComponents(int column) {
        return columnComponentMapping.get(column).stream()
                .map(ComponentPosition::getComponent)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public Component getComponent(int column, int row) {
        checkArgument(column >= 0 && column < component.getColumns()
                        && row >= 0 && row < component.getRows(),
                "Illegal coordinates for the component: [%s, %s]. Must be between [0, 0] - [%s, %s]",
                column, row, component.getColumns() - 1, component.getRows() - 1);

        List<ComponentArea> componentAreas = calculateComponentAreas();
        for (ComponentArea area : componentAreas) {
            if (area.getStartColumn() <= column && column <= area.getEndColumn()
                    && area.getStartRow() <= row && row <= area.getEndRow()) {
                return area.getComponent();
            }
        }

        throw new IllegalStateException(
                String.format("Can't find a component for the given coordinates: [%s, %s]", column, row));
    }

    @Override
    public CaptionPosition getCaptionPosition() {
        return component.isUseInlineCaption()
                ? CaptionPosition.LEFT
                : CaptionPosition.TOP;
    }

    @Override
    public void setCaptionPosition(CaptionPosition captionAlignment) {
        component.setUseInlineCaption(CaptionPosition.LEFT.equals(captionAlignment));
    }

    @Override
    public int getChildrenCaptionWidth() {
        return component.getFixedCaptionWidth();
    }

    @Override
    public void setChildrenCaptionWidth(int width) {
        component.setFixedCaptionWidth(width);
    }

    @Override
    public int getChildrenCaptionWidth(int column) {
        return component.getFieldCaptionWidth(column);
    }

    @Override
    public void setChildrenCaptionWidth(int column, int width) {
        component.setFieldCaptionWidth(column, width);
    }

    @Override
    public CaptionAlignment getChildrenCaptionAlignment() {
        return WebWrapperUtils.fromVaadinFieldGroupCaptionAlignment(
                component.getColumnCaptionAlignment());
    }

    @Override
    public void setChildrenCaptionAlignment(CaptionAlignment alignment) {
        component.setColumnCaptionAlignment(
                WebWrapperUtils.toVaadinFieldGroupCaptionAlignment(alignment));
    }

    @Override
    public CaptionAlignment getChildrenCaptionAlignment(int column) {
        return WebWrapperUtils.fromVaadinFieldGroupCaptionAlignment(
                component.getColumnCaptionAlignment(column));
    }

    @Override
    public void setChildrenCaptionAlignment(int column, CaptionAlignment alignment) {
        component.setColumnCaptionAlignment(column,
                WebWrapperUtils.toVaadinFieldGroupCaptionAlignment(alignment));
    }

    @Override
    public int getColumns() {
        return component.getColumns();
    }

    @Override
    public void setColumns(int columns) {
        if (component.getColumns() != columns) {
            try {
                component.setColumns(columns);
            } catch (GridLayout.OutOfBoundsException e) {
                // Replace the default exception with something more meaningful
                throw new IllegalStateException("Can't shrink columns with components within them");
            }

            List<List<ComponentPosition>> oldColumnComponents = columnComponentMapping;
            columnComponentMapping = new ArrayList<>();
            for (int i = 0; i < columns; i++) {
                if (i < oldColumnComponents.size()) {
                    columnComponentMapping.add(oldColumnComponents.get(i));
                } else {
                    columnComponentMapping.add(new ArrayList<>());
                }
            }
        }
    }

    @Override
    public ValueSourceProvider getValueSourceProvider() {
        return valueSourceProvider;
    }

    @Override
    public void setValueSourceProvider(ValueSourceProvider provider) {
        if (this.valueSourceProvider != null) {
            throw new UnsupportedOperationException("Changing value source provider is not supported " +
                    "by the Form component");
        }

        this.valueSourceProvider = provider;
    }

    @Override
    public void applyPermission(UiPermissionDescriptor permissionDescriptor) {
        checkNotNullArgument(permissionDescriptor);

        String subComponentId = permissionDescriptor.getSubComponentId();
        UiPermissionValue permissionValue = permissionDescriptor.getPermissionValue();
        String screenId = permissionDescriptor.getScreenId();

        if (subComponentId != null) {
            Component component = getComponent(subComponentId);
            if (component != null) {
                if (permissionValue == UiPermissionValue.HIDE) {
                    component.setVisible(false);
                } else if (permissionValue == UiPermissionValue.READ_ONLY
                        && this.component instanceof Editable) {
                    ((Editable) component).setEditable(false);
                }
            } else {
                log.info("Couldn't find suitable component {} in window {} for UI security rule",
                        subComponentId, screenId);
            }
        } else {
            String actionHolderComponentId = permissionDescriptor.getActionHolderComponentId();
            Component component = getComponent(actionHolderComponentId);
            if (!(component instanceof SecuredActionsHolder)) {
                log.info("Couldn't find suitable component {} in window {} for UI security rule",
                        actionHolderComponentId, screenId);
                return;
            }

            String actionId = permissionDescriptor.getActionId();
            ActionsPermissions permissions = ((SecuredActionsHolder) component).getActionsPermissions();
            if (permissionValue == UiPermissionValue.HIDE) {
                permissions.addHiddenActionPermission(actionId);
            } else if (permissionValue == UiPermissionValue.READ_ONLY) {
                permissions.addDisabledActionPermission(actionId);
            }
        }
    }

    @Override
    public void attached() {
        super.attached();

        getOwnComponentsStream().forEach(component ->
                ((AttachNotifier) component).attached());
    }

    @Override
    public void detached() {
        super.detached();

        getOwnComponentsStream().forEach(component ->
                ((AttachNotifier) component).detached());
    }
}
