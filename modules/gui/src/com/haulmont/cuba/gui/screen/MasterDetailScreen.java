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

package com.haulmont.cuba.gui.screen;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.actions.list.EditAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.DataUnit;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.options.ContainerOptions;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSourceProvider;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.gui.util.UnknownOperationResult;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * Displays a list of entities on the left and details of the currently selected instance on the right.
 */
@ParametersAreNonnullByDefault
public abstract class MasterDetailScreen<T extends Entity> extends StandardLookup<T> {

    /**
     * Indicates that the screen is in editing mode.
     */
    protected boolean editing;

    /**
     * Indicates that a new instance of entity is being created.
     */
    protected boolean creating;

    /**
     * Indicates that edited entity is pessimistically locked.
     */
    protected boolean justLocked;

    /**
     * Indicates whether the cross-field validation is enabled.
     */
    protected boolean crossFieldValidate = true;

    public MasterDetailScreen() {
        addInitListener(this::initMasterDetailScreen);
    }

    /**
     * Returns the left container with browse components. Override if the container id differs from "lookupBox".
     */
    protected ComponentContainer getLookupBox() {
        return (ComponentContainer) getWindow().getComponentNN("lookupBox");
    }

    /**
     * Returns the browse {@link Table} or {@link DataGrid}. Override the method if the table id differs from "table".
     */
    @SuppressWarnings("unchecked")
    protected ListComponent<T> getTable() {
        return (ListComponent) getWindow().getComponentNN("table");
    }

    /**
     * Returns the right container with edit components. Override if the container id differs from "editBox".
     */
    protected ComponentContainer getEditBox() {
        return (ComponentContainer) getWindow().getComponentNN("editBox");
    }

    /**
     * Returns the tab sheet with edit components. Can be null if the screen contains a field group only.
     * Override if the tab sheet id differs from "tabSheet".
     */
    @Nullable
    protected TabSheet getTabSheet() {
        return (TabSheet) getWindow().getComponent("tabSheet");
    }

    /**
     * Returns the field group. Override if the field group id differs from "fieldGroup".
     */
    protected Form getForm() {
        return (Form) getWindow().getComponentNN("form");
    }

    /**
     * Returns the container with edit actions (save, cancel). Override if the container id differs from "actionsPane".
     */
    protected ComponentContainer getActionsPane() {
        return (ComponentContainer) getWindow().getComponentNN("actionsPane");
    }

    /**
     * Returns the table's data container.
     */
    @SuppressWarnings("unchecked")
    protected CollectionContainer<T> getBrowseContainer() {
        DataUnit items = getTable().getItems();
        if (items instanceof ContainerDataUnit)
            return ((ContainerDataUnit<T>) items).getContainer();
        else
            throw new UnsupportedOperationException("Unsupported items: " + items);
    }

    /**
     * Returns the edit form's data container.
     */
    @SuppressWarnings("unchecked")
    protected InstanceContainer<T> getEditContainer() {
        return ((ContainerValueSourceProvider) getForm().getValueSourceProvider()).getContainer();
    }

    /**
     * @return currently edited entity instance loaded to editor form
     */
    @Nonnull
    protected T getEditedEntity() {
        if (!editing) {
            throw new IllegalStateException("Edited entity is not available because form is in non-editing state");
        }
        return getEditContainer().getItem();
    }

    /**
     * Returns the loader of the edit form's data container.
     */
    @SuppressWarnings("unchecked")
    protected InstanceLoader<T> getEditLoader() {
        DataLoader loader = ((HasLoader) getEditContainer()).getLoader();
        if (loader == null) {
            throw new IllegalStateException("Cannot find loader of editing container");
        }
        return (InstanceLoader<T>) loader;
    }

    /**
     * Returns the entity meta-class.
     */
    protected Class<T> getEntityClass() {
        return getBrowseContainer().getEntityMetaClass().getJavaClass();
    }

    /**
     * Method invoked on the screen initialization.
     */
    protected void initMasterDetailScreen(@SuppressWarnings("unused") InitEvent event) {
        initDataComponents();
        initOkCancelActions();
        initBrowseItemChangeListener();
        initBrowseCreateAction();
        initBrowseEditAction();
        initShortcuts();

        disableEditControls();
    }

    /**
     * Initializes data components.
     * <p>Default implementation unlinks browse loader from DataContext to prevent from the edited entity being
     * referenced from both edit and browse data containers. Keep in mind that as a result, entities loaded
     * in the browse table are not tracked.
     */
    protected void initDataComponents() {
        CollectionContainer<T> browseContainer = getBrowseContainer();
        DataLoader browseLoader = ((HasLoader) browseContainer).getLoader();
        if (browseLoader != null) {
            browseLoader.setDataContext(null);
        }
    }

    /**
     * Initializes OK/Cancel editor buttons.
     */
    protected void initOkCancelActions() {
        ((BaseAction) getWindow().getActionNN("save")).withHandler(actionPerformedEvent -> saveChanges());
        ((BaseAction) getWindow().getActionNN("cancel")).withHandler(actionPerformedEvent -> discardChanges());
    }

    /**
     * Adds a listener that reloads the selected record and sets it to editCt.
     */
    protected void initBrowseItemChangeListener() {
        getBrowseContainer().addItemChangeListener(e -> {
            if (e.getItem() != null) {
                InstanceLoader<T> editLoader = getEditLoader();

                DynamicAttributesGuiTools tools = getBeanLocator().get(DynamicAttributesGuiTools.NAME);
                String screenId = getScreenContext().getWindowInfo().getId();
                if (tools.screenContainsDynamicAttributes(getEditContainer().getView(), screenId)) {
                    editLoader.setLoadDynamicAttributes(true);
                }

                editLoader.setEntityId(e.getItem().getId());
                editLoader.load();
            } else {
                if (!editing) {
                    getEditContainer().setItem(null);
                }
            }
        });
    }

    /**
     * Adds a CreateAction that removes selection in table, sets a newly created item to editDs
     * and enables controls for record editing.
     */
    protected void initBrowseCreateAction() {
        ListComponent<T> table = getTable();
        CreateAction createAction = (CreateAction) table.getActionNN("create");
        createAction.withHandler(actionPerformedEvent -> {
            T entity = getBeanLocator().get(Metadata.class).create(getEntityClass());
            T trackedEntity = getScreenData().getDataContext().merge(entity);

            DynamicAttributesGuiTools tools = getBeanLocator().get(DynamicAttributesGuiTools.NAME);
            String screenId = getScreenContext().getWindowInfo().getId();

            InstanceLoader<T> instanceLoader = null;
            InstanceContainer<T> editedEntityContainer = getEditContainer();
            if (editedEntityContainer instanceof HasLoader) {
                if (((HasLoader) editedEntityContainer).getLoader() instanceof InstanceLoader) {
                    instanceLoader = getEditLoader();
                    if (tools.screenContainsDynamicAttributes(editedEntityContainer.getView(), screenId)) {
                        instanceLoader.setLoadDynamicAttributes(true);
                    }
                }
            }

            if (instanceLoader != null
                    && instanceLoader.isLoadDynamicAttributes()
                    && trackedEntity instanceof BaseGenericIdEntity) {
                tools.initDefaultAttributeValues((BaseGenericIdEntity) trackedEntity, trackedEntity.getMetaClass());
            }

            fireEvent(InitEntityEvent.class, new InitEntityEvent<>(this, trackedEntity));

            getEditContainer().setItem(trackedEntity);
            refreshOptionsForLookupFields();
            enableEditControls(true);
            table.setSelected(Collections.emptyList());
        });
    }

    /**
     * Adds an EditAction that enables controls for editing.
     */
    protected void initBrowseEditAction() {
        ListComponent<T> table = getTable();
        EditAction editAction = (EditAction) table.getActionNN("edit");
        editAction.withHandler(actionPerformedEvent -> {
            T item = table.getSingleSelected();
            if (item != null) {
                if (lockIfNeeded(item)) {
                    refreshOptionsForLookupFields();
                    enableEditControls(false);
                }
            }
        });
        editAction.addEnabledRule(() ->
                table.getSelected().size() == 1
                && getBeanLocator().get(Security.class).isEntityOpPermitted(getEntityClass(), EntityOp.UPDATE));
    }

    /**
     * Adds ESCAPE shortcut that invokes cancel() method.
     */
    protected void initShortcuts() {
        ComponentContainer editBox = getEditBox();
        if (editBox instanceof ShortcutNotifier) {
            ((ShortcutNotifier) editBox).addShortcutAction(
                    new ShortcutAction(new KeyCombination(KeyCombination.Key.ESCAPE),
                            shortcutTriggeredEvent -> discardChanges()));
        }
    }

    /**
     * Loads options of LookupFields if any.
     */
    protected void refreshOptionsForLookupFields() {
        for (Component component : getForm().getOwnComponents()) {
            if (component instanceof LookupField) {
                Options options = ((LookupField) component).getOptions();
                if (options instanceof ContainerOptions) {
                    CollectionContainer container = ((ContainerOptions) options).getContainer();
                    if (container instanceof HasLoader) {
                        DataLoader optionsLoader = ((HasLoader) container).getLoader();
                        if (optionsLoader != null) {
                            optionsLoader.load();
                        }
                    }
                }
            }
        }
    }

    /**
     * Enables controls for editing.
     * @param creating indicates that a new instance is being created
     */
    protected void enableEditControls(boolean creating) {
        this.editing = true;
        this.creating = creating;
        initEditComponents(true);
        getForm().focusFirstComponent();
    }

    /**
     * Disables edit controls.
     */
    protected void disableEditControls() {
        this.editing = false;
        initEditComponents(false);
        ((Component.Focusable) getTable()).focus();
    }

    /**
     * Initializes edit controls, depending on if they should be enabled or disabled.
     * @param enabled if true - enables edit controls and disables controls on the left side of the splitter
     *                if false - vice versa
     */
    protected void initEditComponents(boolean enabled) {
        TabSheet tabSheet = getTabSheet();
        if (tabSheet != null) {
            ComponentsHelper.walkComponents(tabSheet, (component, name) -> {
                if (component instanceof FieldGroup) {
                    ((FieldGroup) component).setEditable(enabled);
                } else if (component instanceof Table) {
                    ((Table) component).getActions().forEach(action -> action.setEnabled(enabled));
                } else if (!(component instanceof ComponentContainer)) {
                    component.setEnabled(enabled);
                }
            });
        }
        getForm().setEditable(enabled);
        getActionsPane().setVisible(enabled);
        getLookupBox().setEnabled(!enabled);
    }

    /**
     * Pessimistic lock before start of editing, if it is configured for the entity.
     */
    protected boolean lockIfNeeded(Entity entity) {
        LockService lockService = getBeanLocator().get(LockService.class);

        LockInfo lockInfo = lockService.lock(getLockName(), entity.getId().toString());
        if (lockInfo == null) {
            justLocked = true;
        } else if (!(lockInfo instanceof LockNotSupported)) {
            Messages messages = getBeanLocator().get(Messages.class);
            DatatypeFormatter datatypeFormatter = getBeanLocator().get(DatatypeFormatter.class);
            Notifications notifications = getScreenContext().getNotifications();

            notifications.create(NotificationType.HUMANIZED)
                    .withCaption(messages.getMainMessage("entityLocked.msg"))
                    .withDescription(String.format(messages.getMainMessage("entityLocked.desc"),
                                    lockInfo.getUser().getLogin(),
                                    datatypeFormatter.formatDateTime(lockInfo.getSince())))
                    .show();

            return false;
        }
        return true;
    }

    /**
     * Releases pessimistic lock if the entity was locked.
     */
    protected void releaseLock() {
        if (justLocked) {
            Entity entity = getEditContainer().getItemOrNull();
            if (entity != null) {
                getBeanLocator().get(LockService.class).unlock(getLockName(), entity.getId().toString());
            }
        }
    }

    /**
     * Returns the name of the pessimistic lock.
     */
    protected String getLockName() {
        InstanceContainer<T> container = getEditContainer();
        return getBeanLocator().get(ExtendedEntities.class)
                .getOriginalOrThisMetaClass(container.getEntityMetaClass())
                .getName();
    }

    /**
     * Method invoked when clicking on the Ok button after editing an existing or creating a new record.
     */
    protected void saveChanges() {
        if (!editing) {
            return;
        }

        commitEditorChanges()
                .then(() -> {
                    T editedItem = getEditContainer().getItem();
                    if (creating) {
                        getBrowseContainer().getMutableItems().add(0, editedItem);
                    } else {
                        getBrowseContainer().replaceItem(editedItem);
                    }
                    getTable().setSelected(editedItem);

                    releaseLock();
                    disableEditControls();
                });
    }

    /**
     * Validates editor form and commits data context.
     *
     * @return operation result
     */
    protected OperationResult commitEditorChanges() {
        ValidationErrors validationErrors = validateEditorForm();
        if (!validationErrors.isEmpty()) {
            ScreenValidation screenValidation = getBeanLocator().get(ScreenValidation.class);
            screenValidation.showValidationErrors(this, validationErrors);
            return OperationResult.fail();
        }

        Runnable standardCommitAction = () -> {
            getScreenData().getDataContext().commit();
            fireEvent(AfterCommitChangesEvent.class, new AfterCommitChangesEvent(this));
        };

        BeforeCommitChangesEvent beforeEvent = new BeforeCommitChangesEvent(this, standardCommitAction);
        fireEvent(BeforeCommitChangesEvent.class, beforeEvent);

        if (beforeEvent.isCommitPrevented()) {
            if (beforeEvent.getCommitResult() != null) {
                return beforeEvent.getCommitResult();
            }

            return OperationResult.fail();
        }

        standardCommitAction.run();

        return OperationResult.success();
    }

    /**
     * Method invoked when clicking the Cancel button, discards changes and disables controls for editing.
     */
    protected void discardChanges() {
        releaseLock();
        getScreenData().getDataContext().evictAll();
        getEditContainer().setItem(null);

        T selectedItem = getBrowseContainer().getItemOrNull();
        if (selectedItem != null) {
            View view = getEditContainer().getView();
            boolean loadDynamicAttributes = getEditLoader().isLoadDynamicAttributes();
            T reloadedItem = getBeanLocator().get(DataManager.class)
                    .reload(selectedItem, view, null, loadDynamicAttributes);
            getBrowseContainer().setItem(reloadedItem);
        }

        disableEditControls();
    }

    /**
     * @return {@code true} if the cross-field validation is enabled, {@code false} otherwise
     */
    protected boolean isCrossFieldValidate() {
        return crossFieldValidate;
    }

    /**
     * Sets whether the cross-field validation is active. {@code true} by default.
     *
     * @param crossFieldValidate {@code true} to enable the cross-field validation, {@code false} otherwise
     */
    protected void setCrossFieldValidate(boolean crossFieldValidate) {
        this.crossFieldValidate = crossFieldValidate;
    }

    /**
     * Validates screen data. Default implementation validates visible and enabled UI components. <br>
     * Can be overridden in subclasses.
     *
     * @return validation errors
     */
    protected ValidationErrors validateEditorForm() {
        ValidationErrors validationErrors = validateUiComponents();

        validateAdditionalRules(validationErrors);

        return validationErrors;
    }

    /**
     * Validates visible and enabled UI components. <br>
     * Can be overridden in subclasses.
     *
     * @return validation errors
     */
    protected ValidationErrors validateUiComponents() {
        ScreenValidation screenValidation = getBeanLocator().get(ScreenValidation.NAME);
        return screenValidation.validateUiComponents(getForm().getComponents());
    }

    /**
     * Validates the cross-field rules if passed validation errors are empty.
     *
     * @param errors errors found during components validation
     */
    protected void validateAdditionalRules(ValidationErrors errors) {
        // all previous validations return no errors
        if (isCrossFieldValidate() && errors.isEmpty()) {
            ScreenValidation screenValidation = getBeanLocator().get(ScreenValidation.NAME);

            ValidationErrors validationErrors =
                    screenValidation.validateCrossFieldRules(this, getEditContainer().getItem());

            errors.addAll(validationErrors);
        }
    }

    /**
     * Adds a listener to {@link InitEntityEvent}.
     *
     * @param listener listener
     * @return subscription
     */
    @SuppressWarnings("unchecked")
    protected Subscription addInitEntityListener(Consumer<InitEntityEvent<T>> listener) {
        return getEventHub().subscribe(InitEntityEvent.class, (Consumer) listener);
    }

    /**
     * Adds a listener to {@link StandardEditor.BeforeCommitChangesEvent}.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addBeforeCommitChangesListener(Consumer<BeforeCommitChangesEvent> listener) {
        return getEventHub().subscribe(BeforeCommitChangesEvent.class, listener);
    }

    /**
     * Adds a listener to {@link StandardEditor.AfterCommitChangesEvent}.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addAfterCommitChangesListener(Consumer<AfterCommitChangesEvent> listener) {
        return getEventHub().subscribe(AfterCommitChangesEvent.class, listener);
    }

    /**
     * Event sent before the new entity instance is set to edited entity container.
     * <p>
     * Use this event listener to initialize default values in the new entity instance, for example:
     * <pre>
     *     &#64;Subscribe
     *     protected void onInitEntity(InitEntityEvent&lt;Foo&gt; event) {
     *         event.getEntity().setStatus(Status.ACTIVE);
     *     }
     * </pre>
     *
     * @param <E> type of entity
     * @see #addInitEntityListener(Consumer)
     */
    public static class InitEntityEvent<E extends Entity> extends EventObject {
        protected final E entity;

        public InitEntityEvent(Screen source, E entity) {
            super(source);
            this.entity = entity;
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        /**
         * @return initializing entity
         */
        public E getEntity() {
            return entity;
        }
    }

    /**
     * Event sent before commit of data context from {@link #commitEditorChanges()} call.
     * <br>
     * Use this event listener to prevent commit and/or show additional dialogs to user before commit, for example:
     * <pre>
     *     &#64;Subscribe
     *     protected void onBeforeCommit(BeforeCommitChangesEvent event) {
     *         if (getEditedEntity().getDescription() == null) {
     *             notifications.create().withCaption("Description required").show();
     *             event.preventCommit();
     *         }
     *     }
     * </pre>
     *
     * Show dialog and resume commit after:
     * <pre>
     *     &#64;Subscribe
     *     protected void onBeforeCommit(BeforeCommitChangesEvent event) {
     *         if (getEditedEntity().getDescription() == null) {
     *             dialogs.createOptionDialog()
     *                     .withCaption("Question")
     *                     .withMessage("Do you want to set default description?")
     *                     .withActions(
     *                             new DialogAction(DialogAction.Type.YES).withHandler(e -&gt; {
     *                                 getEditedEntity().setDescription("No description");
     *
     *                                 // retry commit and resume action
     *                                 event.resume(commitEditorChanges());
     *                             }),
     *                             new DialogAction(DialogAction.Type.NO).withHandler(e -&gt; {
     *                                 // trigger standard commit and resume action
     *                                 event.resume();
     *                             })
     *                     )
     *                     .show();
     *
     *             event.preventCommit();
     *         }
     *     }
     * </pre>
     *
     * @see #addBeforeCommitChangesListener(Consumer)
     */
    public static class BeforeCommitChangesEvent extends EventObject {

        protected final Runnable resumeAction;

        protected boolean commitPrevented = false;
        protected OperationResult commitResult;

        public BeforeCommitChangesEvent(Screen source, Runnable resumeAction) {
            super(source);
            this.resumeAction = resumeAction;
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        /**
         * @return data context of the screen
         */
        public DataContext getDataContext() {
            return getSource().getScreenData().getDataContext();
        }

        /**
         * Prevents commit of the editor form.
         */
        public void preventCommit() {
            preventCommit(new UnknownOperationResult());
        }

        /**
         * Prevents commit of the editor form.
         *
         * @param commitResult result object that will be used to resume entity saving
         */
        public void preventCommit(OperationResult commitResult) {
            this.commitPrevented = true;
            this.commitResult = commitResult;
        }

        /**
         * Resume standard execution.
         */
        public void resume() {
            if (resumeAction != null) {
                resumeAction.run();
            }
            if (commitResult instanceof UnknownOperationResult) {
                ((UnknownOperationResult) commitResult).resume(OperationResult.success());
            }
        }

        /**
         * Resume with the passed result ignoring standard execution. The standard commit will not be performed.
         */
        public void resume(OperationResult result) {
            if (commitResult instanceof UnknownOperationResult) {
                ((UnknownOperationResult) commitResult).resume(result);
            }
        }

        /**
         * @return result passed to the {@link #preventCommit(OperationResult)} method
         */
        @Nullable
        public OperationResult getCommitResult() {
            return commitResult;
        }

        /**
         * @return whether the commit was prevented by invoking {@link #preventCommit()} method
         */
        public boolean isCommitPrevented() {
            return commitPrevented;
        }
    }

    /**
     * Event sent after commit of data context from {@link #commitEditorChanges()} call.
     * <br>
     * Use this event listener to notify users after commit, for example:
     * <pre>
     *     &#64;Subscribe
     *     protected void onAfterCommit(AfterCommitChanges event) {
     *         notifications.create().withCaption("Committed").show();
     *     }
     * </pre>
     *
     * @see #addAfterCommitChangesListener(Consumer)
     */
    public static class AfterCommitChangesEvent extends EventObject {

        public AfterCommitChangesEvent(Screen source) {
            super(source);
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        /**
         * @return data context of the screen
         */
        public DataContext getDataContext() {
            return getSource().getScreenData().getDataContext();
        }
    }
}