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
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.security.entity.EntityOp;
import org.springframework.context.annotation.Scope;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Standard list action to edit an entity instance.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor, setting properties, or overriding
 * methods {@link #afterCommit(com.haulmont.cuba.core.entity.Entity)}, {@link #afterWindowClosed(com.haulmont.cuba.gui.components.Window)}
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 * &lt;bean id="cuba_EditAction" class="com.company.sample.gui.MyEditAction" scope="prototype"/&gt;
 * </pre>
 * Also, use {@code create()} static methods instead of constructors when creating the action programmatically.
 */
@org.springframework.stereotype.Component("cuba_EditAction")
@Scope("prototype")
public class EditAction extends ItemTrackingAction implements Action.HasOpenType, Action.HasBeforeActionPerformedHandler {

    public static final String ACTION_ID = ListActionType.EDIT.getId();

    protected OpenType openType;

    protected String windowId;
    protected Map<String, Object> windowParams;
    protected Supplier<Map<String, Object>> windowParamsSupplier;

    // Set default caption only once
    protected boolean captionInitialized = false;

    protected AfterCommitHandler afterCommitHandler;

    protected AfterWindowClosedHandler afterWindowClosedHandler;

    protected Window.CloseListener editorCloseListener;

    protected BeforeActionPerformedHandler beforeActionPerformedHandler;

    protected BulkEditorIntegration bulkEditorIntegration = new BulkEditorIntegration();

    public interface AfterCommitHandler {
        /**
        * @param entity    new committed entity instance
        */
        void handle(Entity entity);
    }

    public interface AfterWindowClosedHandler {
        /**
         * @param window        the editor window
         * @param closeActionId ID of action caused the screen closing
         */
        void handle(Window window, String closeActionId);
    }

    /**
     * Creates an action with default id, opening the edit screen in THIS tab.
     * @param target    component containing this action
     */
    public static EditAction create(ListComponent target) {
        return AppBeans.getPrototype("cuba_EditAction", target);
    }

    /**
     * Creates an action with default id.
     * @param target    component containing this action
     * @param openType  how to open the editor screen
     */
    public static EditAction create(ListComponent target, OpenType openType) {
        return AppBeans.getPrototype("cuba_EditAction", target, openType);
    }

    /**
     * Creates an action with the given id.
     * @param target    component containing this action
     * @param openType  how to open the editor screen
     * @param id        action name
     */
    public static EditAction create(ListComponent target, OpenType openType, String id) {
        return AppBeans.getPrototype("cuba_EditAction", target, openType, id);
    }

    /**
     * The simplest constructor. The action has default name and opens the editor screen in THIS tab.
     * @param target    component containing this action
     */
    public EditAction(ListComponent target) {
        this(target, OpenType.THIS_TAB, ACTION_ID);
    }

    /**
     * Constructor that allows to specify how the editor screen opens. The action has default name.
     * @param target    component containing this action
     * @param openType  how to open the editor screen
     */
    public EditAction(ListComponent target, OpenType openType) {
        this(target, openType, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name and how the editor screen opens.
     * @param target    component containing this action
     * @param openType  how to open the editor screen
     * @param id        action name
     */
    public EditAction(ListComponent target, OpenType openType, String id) {
        super(id);

        this.target = target;
        this.openType = openType;

        this.icon = AppBeans.get(Icons.class).get(CubaIcon.EDIT_ACTION);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig config = configuration.getConfig(ClientConfig.class);
        setShortcut(config.getTableEditShortcut());
    }

    @Override
    public void setCaption(String caption) {
        super.setCaption(caption);

        captionInitialized = true;
    }

    @Override
    public void refreshState() {
        super.refreshState();

        if (target == null || target.getDatasource() == null)
            return;

        if (!captionInitialized) {
            if (security.isEntityOpPermitted(target.getDatasource().getMetaClass(), EntityOp.UPDATE)) {
                setCaption(messages.getMainMessage("actions.Edit"));
            } else {
                setCaption(messages.getMainMessage("actions.View"));
            }
        }
    }

    /**
     * Check permissions for Action
     */
    @Override
    protected boolean isPermitted() {
        if (target == null || target.getDatasource() == null) {
            return false;
        }

        CollectionDatasource ownerDatasource = target.getDatasource();
        boolean entityOpPermitted = security.isEntityOpPermitted(ownerDatasource.getMetaClass(), EntityOp.READ);
        if (!entityOpPermitted) {
            return false;
        }

        return super.isPermitted();
    }

    /**
     * This method is invoked by the action owner component.
     *
     * @param component component invoking the action
     */
    @Override
    public void actionPerform(Component component) {
        if (beforeActionPerformedHandler != null) {
            if (!beforeActionPerformedHandler.beforeActionPerformed())
                return;
        }

        final Set selected = target.getSelected();
        if (selected.size() == 1) {
            Datasource parentDs = null;
            final CollectionDatasource datasource = target.getDatasource();
            if (datasource instanceof PropertyDatasource) {
                MetaProperty metaProperty = ((PropertyDatasource) datasource).getProperty();
                if (metaProperty.getType().equals(MetaProperty.Type.COMPOSITION)) {
                    parentDs = datasource;
                }
            }

            Map<String, Object> params = prepareWindowParams();

            internalOpenEditor(datasource, datasource.getItem(), parentDs, params);
        } else if (selected.size() > 1 && bulkEditorIntegration.isEnabled()) {
            boolean isBulkEditorPermitted = userSession.isSpecificPermitted(BulkEditor.PERMISSION);
            if (isBulkEditorPermitted) {
                // if bulk editor integration enabled and permitted for user

                Map<String, Object> params = ParamsMap.of(
                        "metaClass", target.getDatasource().getMetaClass(),
                        "selected", selected,
                        "exclude", bulkEditorIntegration.getExcludePropertiesRegex(),
                        "fieldValidators", bulkEditorIntegration.getFieldValidators(),
                        "modelValidators", bulkEditorIntegration.getModelValidators()
                );

                Window bulkEditor = target.getFrame()
                        .openWindow("bulkEditor", bulkEditorIntegration.getOpenType(), params);
                bulkEditor.addCloseListener(actionId -> {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        target.getDatasource().refresh();
                    }
                    target.requestFocus();

                    Consumer<BulkEditorCloseEvent> afterEditorCloseHandler =
                            bulkEditorIntegration.getAfterEditorCloseHandler();
                    if (afterEditorCloseHandler != null) {
                        afterEditorCloseHandler.accept(new BulkEditorCloseEvent(this, bulkEditor, actionId));
                    }
                });
            }
        }
    }

    protected Map<String, Object> prepareWindowParams() {
        Map<String, Object> windowParams = getWindowParams();
        Map<String, Object> supplierParams = null;
        if (windowParamsSupplier != null) {
            supplierParams = windowParamsSupplier.get();
        }

        Map<String, Object> params = Collections.emptyMap();
        if (supplierParams != null || windowParams != null) {
            params = new HashMap<>();
            params.putAll(windowParams != null ? windowParams : Collections.emptyMap());
            params.putAll(supplierParams != null ? supplierParams : Collections.emptyMap());
        }
        return params;
    }

    protected void internalOpenEditor(CollectionDatasource datasource, Entity existingItem,
                                      Datasource parentDs, Map<String, Object> params) {

        Window.Editor window = target.getFrame().openEditor(getWindowId(), existingItem, getOpenType(), params, parentDs);
        if (editorCloseListener == null) {
            window.addCloseListener(actionId -> {
                // move focus to owner
                target.requestFocus();

                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    Entity editedItem = window.getItem();
                    if (editedItem != null) {
                        if (parentDs == null) {
                            //noinspection unchecked
                            datasource.updateItem(editedItem);
                        }
                        afterCommit(editedItem);
                        if (afterCommitHandler != null) {
                            afterCommitHandler.handle(editedItem);
                        }
                    }
                }

                afterWindowClosed(window);
                if (afterWindowClosedHandler != null) {
                    afterWindowClosedHandler.handle(window, actionId);
                }
            });
        } else {
            window.addCloseListener(editorCloseListener);
        }
    }

    /**
     * @return  editor screen open type
     */
    @Override
    public OpenType getOpenType() {
        return openType;
    }

    /**
     * @param openType  editor screen open type
     */
    @Override
    public void setOpenType(OpenType openType) {
        this.openType = openType;
    }

    /**
     * @return  editor screen identifier
     */
    public String getWindowId() {
        if (windowId != null) {
            return windowId;
        } else {
            MetaClass metaClass = target.getDatasource().getMetaClass();
            WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
            return windowConfig.getEditorScreenId(metaClass);
        }
    }

    /**
     * @param windowId  editor screen identifier
     */
    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }

    /**
     * @return  editor screen parameters
     */
    public Map<String, Object> getWindowParams() {
        return windowParams;
    }

    /**
     * @param windowParams  editor screen parameters
     */
    public void setWindowParams(Map<String, Object> windowParams) {
        this.windowParams = windowParams;
    }

    /**
     * @return supplier that provides editor screen parameters
     */
    public Supplier<Map<String, Object>> getWindowParamsSupplier() {
        return windowParamsSupplier;
    }

    /**
     * @param windowParamsSupplier supplier that provides editor screen parameters
     */
    public void setWindowParamsSupplier(Supplier<Map<String, Object>> windowParamsSupplier) {
        this.windowParamsSupplier = windowParamsSupplier;
    }

    /**
     * Hook invoked after the editor was committed and closed
     * @param entity    new committed entity instance
     */
    protected void afterCommit(Entity entity) {
    }

    /**
     * Hook invoked always after the editor was closed
     * @param window    the editor window
     */
    protected void afterWindowClosed(Window window) {
    }

    /**
     * @param afterCommitHandler handler that is invoked after the editor was commited and closed
     */
    public void setAfterCommitHandler(AfterCommitHandler afterCommitHandler) {
        this.afterCommitHandler = afterCommitHandler;
    }

    /**
     * @param afterWindowClosedHandler handler that is always invoked after the editor closed
     */
    public void setAfterWindowClosedHandler(AfterWindowClosedHandler afterWindowClosedHandler) {
        this.afterWindowClosedHandler = afterWindowClosedHandler;
    }

    /**
     * Overwrites default close listener for editor window.
     *
     * @param editorCloseListener new close listener
     */
    public void setEditorCloseListener(Window.CloseListener editorCloseListener) {
        this.editorCloseListener = editorCloseListener;
    }

    @Override
    public BeforeActionPerformedHandler getBeforeActionPerformedHandler() {
        return beforeActionPerformedHandler;
    }

    @Override
    public void setBeforeActionPerformedHandler(BeforeActionPerformedHandler handler) {
        beforeActionPerformedHandler = handler;
    }

    /**
     * @return bulk editor integration options.
     */
    public BulkEditorIntegration getBulkEditorIntegration() {
        return bulkEditorIntegration;
    }

    /**
     * BulkEditor integration options.
     * <br>
     * If integration {@link BulkEditorIntegration#isEnabled()} and user selects
     * multiple rows in {@link ListComponent} then action will show {@link BulkEditor} window.
     */
    public static class BulkEditorIntegration {
        protected boolean enabled = false;
        protected OpenType openType = OpenType.DIALOG;
        protected String excludePropertiesRegex;
        protected Map<String, Field.Validator> fieldValidators;
        protected List<Field.Validator> modelValidators;
        protected Consumer<BulkEditorCloseEvent> afterEditorCloseHandler;

        public boolean isEnabled() {
            return enabled;
        }

        public BulkEditorIntegration setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public OpenType getOpenType() {
            return openType;
        }

        public BulkEditorIntegration setOpenType(OpenType openType) {
            this.openType = openType;
            return this;
        }

        public String getExcludePropertiesRegex() {
            return excludePropertiesRegex;
        }

        public BulkEditorIntegration setExcludePropertiesRegex(String excludePropertiesRegex) {
            this.excludePropertiesRegex = excludePropertiesRegex;
            return this;
        }

        public Map<String, Field.Validator> getFieldValidators() {
            return fieldValidators;
        }

        public BulkEditorIntegration setFieldValidators(Map<String, Field.Validator> fieldValidators) {
            this.fieldValidators = fieldValidators;
            return this;
        }

        public List<Field.Validator> getModelValidators() {
            return modelValidators;
        }

        public BulkEditorIntegration setModelValidators(List<Field.Validator> modelValidators) {
            this.modelValidators = modelValidators;
            return this;
        }

        public Consumer<BulkEditorCloseEvent> getAfterEditorCloseHandler() {
            return afterEditorCloseHandler;
        }

        public void setAfterEditorCloseHandler(Consumer<BulkEditorCloseEvent> afterEditorCloseHandler) {
            this.afterEditorCloseHandler = afterEditorCloseHandler;
        }
    }

    /**
     * Event that is fired when {@link BulkEditor} windows gets closed.
     */
    public static class BulkEditorCloseEvent extends EventObject {
        private Window bulkEditorWindow;
        private String actionId;

        public BulkEditorCloseEvent(EditAction action, Window bulkEditorWindow, String actionId) {
            super(action);
            this.bulkEditorWindow = bulkEditorWindow;
            this.actionId = actionId;
        }

        @Override
        public EditAction getSource() {
            return (EditAction) super.getSource();
        }

        public Window getBulkEditorWindow() {
            return bulkEditorWindow;
        }

        public String getActionId() {
            return actionId;
        }
    }
}