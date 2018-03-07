/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.grid.CubaGridState;
import com.vaadin.event.Action;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Validatable;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Grid;

import java.lang.reflect.Method;
import java.util.*;

import static com.haulmont.cuba.web.widgets.CubaGrid.BeforeEditorOpenListener.EDITOR_OPEN_METHOD;
import static com.haulmont.cuba.web.widgets.CubaGrid.EditorCloseListener.EDITOR_CLOSE_METHOD;
import static com.haulmont.cuba.web.widgets.CubaGrid.EditorPostCommitListener.EDITOR_POST_COMMIT_METHOD;
import static com.haulmont.cuba.web.widgets.CubaGrid.EditorPreCommitListener.EDITOR_PRE_COMMIT_METHOD;

public class CubaGrid extends Grid implements Action.ShortcutNotifier {

    protected CubaGridEditorFieldFactory editorFieldFactory;

    protected Collection<Field<?>> editorFields = new ArrayList<>();

    public CubaGrid(CubaGridEditorFieldFactory editorFieldFactory) {
        this(null, null, editorFieldFactory);
    }

    public CubaGrid(String caption, Container.Indexed dataSource, CubaGridEditorFieldFactory editorFieldFactory) {
        super(caption, dataSource);

        this.editorFieldFactory = editorFieldFactory;
        setEditorErrorHandler(new CubaDefaultEditorErrorHandler());

        // FIXME: gg, workaround for https://github.com/vaadin/framework/issues/9040
        addEditorCloseListener(event -> focus());
    }

    public CubaGridEditorFieldFactory getCubaEditorFieldFactory() {
        return editorFieldFactory;
    }

    public void setCubaEditorFieldFactory(CubaGridEditorFieldFactory editorFieldFactory) {
        this.editorFieldFactory = editorFieldFactory;
    }

    public void repaint() {
        datasourceExtension.refreshCache();
    }

    @Override
    protected CubaGridState getState() {
        return (CubaGridState) super.getState();
    }

    @Override
    protected CubaGridState getState(boolean markAsDirty) {
        return (CubaGridState) super.getState(markAsDirty);
    }

    public Map<String, String> getColumnIds() {
        return getState().columnIds;
    }

    public void setColumnIds(Map<String, String> ids) {
        getState().columnIds = ids;
    }

    public void addColumnId(String column, String value) {
        if (getState().columnIds == null) {
            getState().columnIds = new HashMap<>();
        }

        getState().columnIds.put(column, value);
    }

    public void removeColumnId(String column) {
        if (getState().columnIds != null) {
            getState().columnIds.remove(column);
        }
    }

    @Override
    protected void doEditItem() {
        clearFields(editorFields);

        Map<Column, Field> columnFieldMap = new HashMap<>();
        for (Column column : getColumns()) {
            Field<?> field = editorFieldFactory.createField(editedItemId, column.getPropertyId());
            column.getState().editorConnector = field;
            if (field != null) {
                configureField(field);
                editorFields.add(field);
                columnFieldMap.put(column, field);
            }
        }

        editorActive = true;
        // Must ensure that all fields, recursively, are sent to the client
        // This is needed because the fields are hidden using isRendered
        for (Field<?> f : getEditorFields()) {
            f.markAsDirtyRecursive();
        }

        fireEditorOpenEvent(columnFieldMap);
    }

    protected void clearFields(Collection<Field<?>> fields) {
        for (Field<?> field : fields) {
            field.setParent(null);
        }
        fields.clear();
    }

    protected void configureField(Field<?> field) {
        field.setParent(this);
        field.setBuffered(isEditorBuffered());
        field.setEnabled(isEnabled());
    }

    @Override
    protected Collection<Field<?>> getEditorFields() {
        Collection<Field<?>> fields = editorFields != null ? editorFields : Collections.emptyList();
        assert allAttached(fields);
        return fields;
    }

    protected boolean allAttached(Collection<? extends Component> components) {
        for (Component component : components) {
            if (component.getParent() != this) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void doCancelEditor() {
        Object itemId = editedItemId;
        editedItemId = null;
        editorActive = false;
        fireEditorCloseEvent(itemId);

        // to prevent one more detach in case of changing datasource
        clearFields(editorFields);

        // Mark Grid as dirty so the client side gets to know that the editors
        // are no longer attached
        markAsDirty();
    }

    @Override
    public void saveEditor() throws FieldGroup.CommitException {
        try {
            editorSaving = true;
            commitEditor();
        } finally {
            editorSaving = false;
        }
    }

    protected void commitEditor() throws FieldGroup.CommitException {
        if (!isEditorBuffered()) {
            // Not using buffered mode, nothing to do
            return;
        }
        try {
            fireEditorPreCommitEvent();

            Map<Field<?>, Validator.InvalidValueException> invalidValueExceptions = commitFields();
            if (invalidValueExceptions.isEmpty()) {
                fireEditorPostCommitEvent();
            } else {
                throw new FieldGroup.FieldGroupInvalidValueException(invalidValueExceptions);
            }
        } catch (Exception e) {
            throw new FieldGroup.CommitException("Commit failed", null, e);
        }
    }

    protected Map<Field<?>, Validator.InvalidValueException> commitFields() {
        Map<Field<?>, Validator.InvalidValueException> invalidValueExceptions = new HashMap<>();

        editorFields.forEach(field -> {
            try {
                field.commit();
            } catch (Validator.InvalidValueException e) {
                invalidValueExceptions.put(field, e);
            }
        });

        return invalidValueExceptions;
    }

    @Override
    protected boolean isEditorFieldsValid() {
        try {
            editorFields.forEach(Validatable::validate);
            return true;
        } catch (Validator.InvalidValueException e) {
            return false;
        }
    }

    protected void fireEditorPreCommitEvent() {
        fireEvent(new EditorPreCommitEvent(this, editedItemId));
    }

    protected void fireEditorPostCommitEvent() {
        fireEvent(new EditorPostCommitEvent(this, editedItemId));
    }

    protected void fireEditorCloseEvent(Object editedItemId) {
        fireEvent(new EditorCloseEvent(this, editedItemId));
    }

    protected void fireEditorOpenEvent(Map<Column, Field> columnFieldMap) {
        fireEvent(new BeforeEditorOpenEvent(this, editedItemId, columnFieldMap));
    }

    public class BeforeEditorOpenEvent extends com.vaadin.v7.ui.Grid.EditorEvent {
        Map<Column, Field> columnFieldMap;

        protected BeforeEditorOpenEvent(Grid source, Object itemID, Map<Column, Field> columnFieldMap) {
            super(source, itemID);
            this.columnFieldMap = columnFieldMap;
        }

        public Map<Column, Field> getColumnFieldMap() {
            return columnFieldMap;
        }
    }

    public interface BeforeEditorOpenListener {
        Method EDITOR_OPEN_METHOD =
                ReflectTools.findMethod(BeforeEditorOpenListener.class, "beforeEditorOpened", BeforeEditorOpenEvent.class);

        void beforeEditorOpened(BeforeEditorOpenEvent event);
    }

    public void addEditorOpenListener(BeforeEditorOpenListener listener) {
        addListener(BeforeEditorOpenEvent.class, listener, EDITOR_OPEN_METHOD);
    }

    public void removeEditorOpenListener(BeforeEditorOpenListener listener) {
        removeListener(BeforeEditorOpenEvent.class, listener, EDITOR_OPEN_METHOD);
    }

    public interface EditorCloseListener {
        Method EDITOR_CLOSE_METHOD =
                ReflectTools.findMethod(EditorCloseListener.class, "editorClosed", EditorCloseEvent.class);

        void editorClosed(EditorCloseEvent event);
    }

    public void addEditorCloseListener(EditorCloseListener listener) {
        addListener(EditorCloseEvent.class, listener, EDITOR_CLOSE_METHOD);
    }

    public void removeEditorCloseListener(EditorCloseListener listener) {
        removeListener(EditorCloseEvent.class, listener, EDITOR_CLOSE_METHOD);
    }

    public interface EditorPreCommitListener {
        Method EDITOR_PRE_COMMIT_METHOD =
                ReflectTools.findMethod(EditorPreCommitListener.class, "preCommit", EditorPreCommitEvent.class);

        void preCommit(EditorPreCommitEvent event);
    }

    public static class EditorPreCommitEvent extends EditorEvent {
        public EditorPreCommitEvent(Grid source, Object itemId) {
            super(source, itemId);
        }
    }

    public void addEditorPreCommitListener(EditorPreCommitListener listener) {
        addListener(EditorPreCommitEvent.class, listener, EDITOR_PRE_COMMIT_METHOD);
    }

    public void removeEditorPreCommitListener(EditorPreCommitListener listener) {
        removeListener(EditorPreCommitEvent.class, listener, EDITOR_PRE_COMMIT_METHOD);
    }

    public interface EditorPostCommitListener {
        Method EDITOR_POST_COMMIT_METHOD =
                ReflectTools.findMethod(EditorPostCommitListener.class, "postCommit", EditorPostCommitEvent.class);

        void postCommit(EditorPostCommitEvent event);
    }

    public static class EditorPostCommitEvent extends EditorEvent {
        public EditorPostCommitEvent(Grid source, Object itemId) {
            super(source, itemId);
        }
    }

    public void addEditorPostCommitListener(EditorPostCommitListener listener) {
        addListener(EditorPostCommitEvent.class, listener, EDITOR_POST_COMMIT_METHOD);
    }

    public void removeEditorPostCommitListener(EditorPostCommitListener listener) {
        removeListener(EditorPostCommitEvent.class, listener, EDITOR_POST_COMMIT_METHOD);
    }

    public class CubaDefaultEditorErrorHandler implements EditorErrorHandler {
        @Override
        public void commitError(CommitErrorEvent event) {
            Map<Field<?>, Validator.InvalidValueException> invalidFields = event
                    .getCause().getInvalidFields();

            if (!invalidFields.isEmpty()) {
                Object firstErrorPropertyId = null;
                Field<?> firstErrorField = null;

                for (Column column : getColumns()) {
                    Object propertyId = column.getPropertyId();
                    Field<?> field = (Field<?>) column.getState().editorConnector;

                    if (invalidFields.keySet().contains(field)) {
                        event.addErrorColumn(column);

                        if (firstErrorPropertyId == null) {
                            firstErrorPropertyId = propertyId;
                            firstErrorField = field;
                        }
                    }
                }

                /*
                 * Validation error, show first failure as
                 * "<Column header>: <message>"
                 */
                String caption = getColumn(firstErrorPropertyId)
                        .getHeaderCaption();
                String message = invalidFields.get(firstErrorField)
                        .getLocalizedMessage();

                event.setUserErrorMessage(caption + ": " + message);
            } else {
                com.vaadin.server.ErrorEvent.findErrorHandler(CubaGrid.this)
                        .error(new ConnectorErrorEvent(CubaGrid.this, event.getCause()));
            }
        }
    }
}
