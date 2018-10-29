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

package com.haulmont.cuba.web.widgets.grid;

import com.google.common.base.Strings;
import com.haulmont.cuba.web.widgets.CubaEnhancedGrid;
import com.vaadin.data.HasValue;
import com.vaadin.data.PropertySet;
import com.vaadin.data.ValidationResult;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.EditorImpl;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.util.ReflectTools;

import java.util.*;
import java.util.stream.Collectors;

public class CubaEditorImpl<T> extends EditorImpl<T> {
    /**
     * Constructor for internal implementation of the Editor.
     *
     * @param propertySet the property set to use for configuring the default binder
     */
    public CubaEditorImpl(PropertySet<T> propertySet) {
        super(propertySet);
    }

    protected CubaEnhancedGrid<T> getEnhancedGrid() {
        //noinspection unchecked
        return (CubaEnhancedGrid<T>) super.getParent();
    }

    public T getBean() {
        return edited;
    }

    @Override
    protected void doEdit(T bean) {
        Objects.requireNonNull(bean, "Editor can't edit null");
        if (!isEnabled()) {
            throw new IllegalStateException(
                    "Editing is not allowed when Editor is disabled.");
        }

        edited = bean;

        getParent().getColumns().stream().filter(Grid.Column::isEditable)
                .forEach(c -> {
                    CubaEditorField<?> editorField = getEnhancedGrid().getColumnEditorField(bean, c);
                    configureField(editorField);
                    addComponentToGrid(editorField);
                    columnFields.put(c, editorField);
                    getState().columnFields.put(getInternalIdForColumn(c), editorField.getConnectorId());
                });

        eventRouter.fireEvent(new CubaEditorOpenEvent<>(this, edited, Collections.unmodifiableMap(columnFields)));
    }

    protected void configureField(CubaEditorField<?> field) {
        field.setBuffered(isBuffered());
        field.setEnabled(isEnabled());
        field.addValueChangeListener(this::onFieldValueChange);
    }

    protected void onFieldValueChange(HasValue.ValueChangeEvent<?> ignored) {
        isEditorFieldsValid();
    }

    @Override
    public boolean save() {
        if (isOpen() && isBuffered()) {
            eventRouter.fireEvent(new CubaEditorBeforeSaveEvent<>(this, edited));
            if (isEditorFieldsValid()) {
                commitFields();
                refresh(edited);
                eventRouter.fireEvent(new EditorSaveEvent<>(this, edited));
                return true;
            }
        }
        return false;
    }

    protected boolean isEditorFieldsValid() {
        Map<Component, ValidationResult> errors = getValidationErrors();
        handleValidation(errors);
        return errors.isEmpty();
    }

    protected void handleValidation(Map<Component, ValidationResult> errors) {
        boolean ok = errors.isEmpty();
        if (saving) {
            rpc.confirmSave(ok);
            saving = false;
        }

        if (ok) {
            rpc.setErrorMessage(null, Collections.emptyList());
        } else {
            List<Component> fields = errors.keySet().stream()
                    .filter(columnFields.values()::contains)
                    .collect(Collectors.toList());

            Map<Component, Grid.Column<T, ?>> fieldToColumn = new HashMap<>();
            columnFields.entrySet().stream()
                    .filter(entry -> fields.contains(entry.getValue()))
                    .forEach(entry -> fieldToColumn.put(entry.getValue(),
                            entry.getKey()));

            String message = generateErrorMessage(fieldToColumn, errors);

            List<String> columnIds = fieldToColumn.values().stream()
                    .map(this::getInternalIdForColumn)
                    .collect(Collectors.toList());

            rpc.setErrorMessage(message, columnIds);
        }
    }

    protected String generateErrorMessage(Map<Component, Grid.Column<T, ?>> fieldToColumn,
                                          Map<Component, ValidationResult> errors) {
        return errors.entrySet().stream()
                .filter(entry ->
                        !Strings.isNullOrEmpty(entry.getValue().getErrorMessage())
                                && fieldToColumn.containsKey(entry.getKey()))
                .map(entry ->
                        fieldToColumn.get(entry.getKey()).getCaption() + ": " +
                                entry.getValue().getErrorMessage())
                .collect(Collectors.joining("; "));
    }

    protected Map<Component, ValidationResult> getValidationErrors() {
        Map<Component, ValidationResult> errors = new HashMap<>();
        columnFields.values().forEach(field -> {
            ValidationResult validationResult = ((CubaEditorField<?>) field).validate();
            if (validationResult.isError()) {
                errors.put(field, validationResult);
            }
        });
        return errors;
    }

    protected void commitFields() {
        columnFields.values().forEach(field -> {
            ((CubaEditorField<?>) field).commit();
        });
    }

    public Registration addBeforeSaveListener(CubaEditorBeforeSaveListener<T> listener) {
        return eventRouter.addListener(CubaEditorBeforeSaveEvent.class, listener,
                ReflectTools.getMethod(CubaEditorBeforeSaveListener.class));
    }
}
