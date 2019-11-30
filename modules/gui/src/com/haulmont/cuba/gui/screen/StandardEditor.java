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

import com.google.common.base.Strings;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.events.TriggerOnce;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.gui.util.UnknownOperationResult;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Base class for editor screens. <br>
 * Supports pessimistic locking, cross field validation and checks for unsaved changes on close.
 *
 * @param <T> type of entity
 */
public abstract class StandardEditor<T extends Entity> extends Screen
        implements EditorScreen<T>, ReadOnlyAwareScreen {

    protected boolean showSaveNotification = true;
    protected boolean commitActionPerformed = false;

    private T entityToEdit;
    private boolean crossFieldValidate = true;
    private boolean justLocked = false;
    private boolean readOnly = false;
    private boolean readOnlyDueToLock = false;

    protected boolean showEnableEditingBtn = true;

    // whether user has edited entity after screen opening
    private boolean modifiedAfterOpen = false;

    protected StandardEditor() {
        addInitListener(this::initActions);
        addBeforeShowListener(this::beforeShow);
        addBeforeCloseListener(this::beforeClose);
        addAfterShowListener(this::afterShow);
    }

    protected void initActions(@SuppressWarnings("unused") InitEvent event) {
        Window window = getWindow();

        Configuration configuration = getBeanLocator().get(Configuration.NAME);
        Messages messages = getBeanLocator().get(Messages.NAME);
        Icons icons = getBeanLocator().get(Icons.NAME);

        String commitShortcut = configuration.getConfig(ClientConfig.class).getCommitShortcut();

        Action commitAndCloseAction = new BaseAction(WINDOW_COMMIT_AND_CLOSE)
                .withCaption(messages.getMainMessage("actions.Ok"))
                .withIcon(icons.get(CubaIcon.EDITOR_OK))
                .withPrimary(true)
                .withShortcut(commitShortcut)
                .withHandler(this::commitAndClose);

        window.addAction(commitAndCloseAction);

        Action commitAction = new BaseAction(WINDOW_COMMIT)
                .withCaption(messages.getMainMessage("actions.Save"))
                .withHandler(this::commit);

        window.addAction(commitAction);

        Action closeAction = new BaseAction(WINDOW_CLOSE)
                .withIcon(icons.get(CubaIcon.EDITOR_CANCEL))
                .withCaption(messages.getMainMessage("actions.Cancel"))
                .withHandler(this::cancel);

        window.addAction(closeAction);

        Action enableEditingAction = new BaseAction(ENABLE_EDITING)
                .withCaption(messages.getMainMessage("actions.EnableEditing"))
                .withIcon(icons.get(CubaIcon.ENABLE_EDITING))
                .withHandler(this::enableEditing);
        enableEditingAction.setVisible(false);
        window.addAction(enableEditingAction);
    }

    protected void enableEditing(Action.ActionPerformedEvent actionPerformedEvent) {
        setReadOnly(false);
    }

    private void beforeShow(@SuppressWarnings("unused") BeforeShowEvent beforeShowEvent) {
        setupEntityToEdit();
        setupLock();
    }

    private void afterShow(@SuppressWarnings("unused") AfterShowEvent event) {
        setupModifiedTracking();
    }

    private void beforeClose(BeforeCloseEvent event) {
        preventUnsavedChanges(event);
    }

    protected void setupModifiedTracking() {
        DataContext dataContext = getScreenData().getDataContext();
        if (dataContext != null) {
            dataContext.addChangeListener(this::onChangeEvent);
            dataContext.addPostCommitListener(this::onPostCommitEvent);
        }
    }

    protected void onChangeEvent(@SuppressWarnings("unused") DataContext.ChangeEvent event) {
        setModifiedAfterOpen(true);
    }

    protected void onPostCommitEvent(@SuppressWarnings("unused") DataContext.PostCommitEvent event) {
        setModifiedAfterOpen(false);
    }

    protected void preventUnsavedChanges(BeforeCloseEvent event) {
        CloseAction action = event.getCloseAction();

        if (action instanceof ChangeTrackerCloseAction
                && ((ChangeTrackerCloseAction) action).isCheckForUnsavedChanges()
                && hasUnsavedChanges()) {
            Configuration configuration = getBeanLocator().get(Configuration.NAME);
            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

            ScreenValidation screenValidation = getBeanLocator().get(ScreenValidation.NAME);

            UnknownOperationResult result = new UnknownOperationResult();

            if (clientConfig.getUseSaveConfirmation()) {
                screenValidation.showSaveConfirmationDialog(this, action)
                    .onCommit(() -> result.resume(closeWithCommit()))
                    .onDiscard(() -> result.resume(closeWithDiscard()))
                    .onCancel(result::fail);
            } else {
                screenValidation.showUnsavedChangesDialog(this, action)
                        .onDiscard(() -> result.resume(closeWithDiscard()))
                        .onCancel(result::fail);
            }

            event.preventWindowClose(result);
        }
    }

    protected void setupEntityToEdit() {
        if (getScreenData().getDataContext() == null) {
            throw new IllegalStateException("No DataContext defined. Make sure the editor screen XML descriptor has <data> element");
        }

        DynamicAttributesGuiTools tools = getBeanLocator().get(DynamicAttributesGuiTools.NAME);
        String screenId = getScreenContext().getWindowInfo().getId();

        InstanceLoader instanceLoader = null;
        InstanceContainer<T> editedEntityContainer = getEditedEntityContainer();
        if (editedEntityContainer instanceof HasLoader) {
            if (((HasLoader) editedEntityContainer).getLoader() instanceof InstanceLoader) {
                instanceLoader = getEditedEntityLoader();
                if (tools.screenContainsDynamicAttributes(editedEntityContainer.getView(), screenId)) {
                    instanceLoader.setLoadDynamicAttributes(true);
                }
            }
        }

        if (getEntityStates().isNew(entityToEdit) || doNotReloadEditedEntity()) {
            T mergedEntity = getScreenData().getDataContext().merge(entityToEdit);

            if (instanceLoader != null
                    && instanceLoader.isLoadDynamicAttributes()
                    && getEntityStates().isNew(entityToEdit)
                    && mergedEntity instanceof BaseGenericIdEntity) {
                tools.initDefaultAttributeValues((BaseGenericIdEntity) mergedEntity, mergedEntity.getMetaClass());
            }

            fireEvent(InitEntityEvent.class, new InitEntityEvent<>(this, mergedEntity));

            InstanceContainer<T> container = getEditedEntityContainer();
            container.setItem(mergedEntity);
        } else {
            InstanceLoader loader = getEditedEntityLoader();
            loader.setEntityId(entityToEdit.getId());
        }
    }

    protected void setupLock() {
        InstanceContainer<T> container = getEditedEntityContainer();
        Security security = getBeanLocator().get(Security.class);

        if (!getEntityStates().isNew(entityToEdit)) {
            if (security.isEntityOpPermitted(container.getEntityMetaClass(), EntityOp.UPDATE)) {
                this.readOnlyDueToLock = false;

                LockService lockService = getBeanLocator().get(LockService.class);

                LockInfo lockInfo = lockService.lock(getLockName(), entityToEdit.getId().toString());
                if (lockInfo == null) {
                    this.justLocked = true;

                    addAfterDetachListener(afterDetachEvent ->
                            releaseLock()
                    );
                } else if (!(lockInfo instanceof LockNotSupported)) {
                    Messages messages = getBeanLocator().get(Messages.class);
                    DatatypeFormatter datatypeFormatter = getBeanLocator().get(DatatypeFormatter.class);

                    getScreenContext().getNotifications().create(NotificationType.HUMANIZED)
                            .withCaption(messages.getMainMessage("entityLocked.msg"))
                            .withDescription(
                                    messages.formatMainMessage("entityLocked.desc",
                                            lockInfo.getUser().getLogin(),
                                            datatypeFormatter.formatDateTime(lockInfo.getSince())
                                    ))
                            .show();

                    this.readOnlyDueToLock = true;
                    this.showEnableEditingBtn = false;
                    setReadOnly(true);
                }
            } else {
                this.showEnableEditingBtn = false;
                setReadOnly(true);
            }
        }
    }

    protected void releaseLock() {
        if (isLocked()) {
            Entity entity = getEditedEntityContainer().getItemOrNull();
            if (entity != null) {
                getBeanLocator().get(LockService.class).unlock(getLockName(), entity.getId().toString());
            }
        }
    }

    protected String getLockName() {
        InstanceContainer<T> container = getEditedEntityContainer();
        return getBeanLocator().get(ExtendedEntities.class)
                .getOriginalOrThisMetaClass(container.getEntityMetaClass())
                .getName();
    }

    protected boolean doNotReloadEditedEntity() {
        if (isEntityModifiedInParentContext()) {
            InstanceContainer<T> container = getEditedEntityContainer();
            if (getEntityStates().isLoadedWithView(entityToEdit, container.getView())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isEntityModifiedInParentContext() {
        boolean result = false;
        DataContext parentDc = getScreenData().getDataContext().getParent();
        while (!result && parentDc != null) {
            result = isEntityModifiedRecursive(entityToEdit, parentDc, new HashSet<>());
            parentDc = parentDc.getParent();
        }
        return result;
    }

    protected boolean isEntityModifiedRecursive(Entity entity, DataContext dataContext, HashSet<Object> visited) {
        if (visited.contains(entity)) {
            return false;
        }
        visited.add(entity);

        if (dataContext.isModified(entity) || dataContext.isRemoved(entity))
            return true;

        for (MetaProperty property : entity.getMetaClass().getProperties()) {
            if (property.getRange().isClass()) {
                if (getEntityStates().isLoaded(entity, property.getName())) {
                    Object value = entity.getValue(property.getName());
                    if (value != null) {
                        if (value instanceof Collection) {
                            for (Object item : ((Collection) value)) {
                                if (isEntityModifiedRecursive((Entity) item, dataContext, visited)) {
                                    return true;
                                }
                            }
                        } else {
                            if (isEntityModifiedRecursive((Entity) value, dataContext, visited)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    protected InstanceLoader getEditedEntityLoader() {
        InstanceContainer<T> container = getEditedEntityContainer();
        if (container == null) {
            throw new IllegalStateException("Edited entity container not defined");
        }
        DataLoader loader = null;
        if (container instanceof HasLoader) {
            loader = ((HasLoader) container).getLoader();
        }
        if (loader == null) {
            throw new IllegalStateException("Loader of edited entity container not found");
        }
        if (!(loader instanceof InstanceLoader)) {
            throw new IllegalStateException(String.format(
                    "Loader %s of edited entity container %s must implement InstanceLoader", loader, container));
        }
        return (InstanceLoader) loader;
    }

    protected InstanceContainer<T> getEditedEntityContainer() {
        EditedEntityContainer annotation = getClass().getAnnotation(EditedEntityContainer.class);
        if (annotation == null || Strings.isNullOrEmpty(annotation.value())) {
            throw new IllegalStateException(
                    String.format("StandardEditor %s does not declare @EditedEntityContainer", getClass())
            );
        }
        String[] parts = annotation.value().split("\\.");
        ScreenData screenData;
        if (parts.length == 1) {
            screenData = getScreenData();
        } else {
            Frame frame = getWindow();
            for (int i = 0; i < parts.length - 1; i++) {
                String part = parts[i];
                Component component = frame.getComponent(part);
                if (!(component instanceof Frame)) {
                    throw new IllegalStateException("Path to EditedEntityContainer must include frames only");
                }
                frame = (Frame) component;
            }
            screenData = UiControllerUtils.getScreenData(frame.getFrameOwner());
        }
        return screenData.getContainer(parts[parts.length - 1]);
    }

    @Override
    public T getEditedEntity() {
        T editedEntity = getEditedEntityContainer().getItemOrNull();
        return editedEntity != null ? editedEntity : entityToEdit;
    }

    @Override
    public void setEntityToEdit(T item) {
        this.entityToEdit = item;
    }

    @Override
    public boolean hasUnsavedChanges() {
        if (isReadOnlyDueToLock()) {
            return false;
        }

        // The editor has to be saved if its edited entity was changed after merging into DataContext or if
        // the DataContext contains other modified entities. If the editor is opened for a new instance and it wasn't
        // modified after that, there is no unsaved changes from the user point of view, although DataContext considers
        // this instance as modified.

        DataContext dataContext = getScreenData().getDataContext();

        if (!dataContext.getRemoved().isEmpty()) {
            return true;
        }
        for (Entity modified : dataContext.getModified()) {
            if (!getEntityStates().isNew(modified)) {
                return true;
            }
        }
        // if only new entities are registered as modified in DataContext, check whether they were modified after
        // opening the screen
        return isModifiedAfterOpen();
    }

    /**
     * Validates screen and commits data context.
     *
     * @return operation result
     */
    protected OperationResult commitChanges() {
        ValidationErrors validationErrors = validateScreen();
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

    @Override
    public boolean isLocked() {
        return justLocked;
    }

    protected void setModifiedAfterOpen(boolean entityModified) {
        this.modifiedAfterOpen = entityModified;
    }

    /**
     * @return true if data is modified after screen opening
     */
    protected boolean isModifiedAfterOpen() {
        return modifiedAfterOpen;
    }

    /**
     * @return true if the editor switched to read-only mode because the entity is locked by another user
     */
    protected boolean isReadOnlyDueToLock() {
        return readOnlyDueToLock;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if (this.readOnly != readOnly) {
            this.readOnly = readOnly;

            ReadOnlyScreensSupport readOnlyScreensSupport = getBeanLocator().get(ReadOnlyScreensSupport.NAME);
            readOnlyScreensSupport.setScreenReadOnly(this, readOnly, showEnableEditingBtn);

            if (readOnlyDueToLock) {
                disableCommitActions();
            }
        }
    }

    protected void disableCommitActions() {
        Action commitAction = getWindow().getAction(WINDOW_COMMIT);
        if (commitAction != null) {
            commitAction.setEnabled(false);
        }

        Action commitCloseAction = getWindow().getAction(WINDOW_COMMIT_AND_CLOSE);
        if (commitCloseAction != null) {
            commitCloseAction.setEnabled(false);
        }
    }

    /**
     * @return true if cross-field validation is enabled
     */
    protected boolean isCrossFieldValidate() {
        return crossFieldValidate;
    }

    protected void setCrossFieldValidate(boolean crossFieldValidate) {
        this.crossFieldValidate = crossFieldValidate;
    }

    /**
     * Validates screen data. Default implementation validates visible and enabled UI components. <br>
     * Can be overridden in subclasses.
     *
     * @return validation errors
     */
    protected ValidationErrors validateScreen() {
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
        return screenValidation.validateUiComponents(getWindow());
    }

    protected void validateAdditionalRules(ValidationErrors errors) {
        // all previous validations return no errors
        if (isCrossFieldValidate() && errors.isEmpty()) {
            ScreenValidation screenValidation = getBeanLocator().get(ScreenValidation.NAME);

            ValidationErrors validationErrors = screenValidation.validateCrossFieldRules(this, getEditedEntity());

            errors.addAll(validationErrors);

            ValidationEvent validateEvent = new ValidationEvent(this);
            fireEvent(ValidationEvent.class, validateEvent);
            errors.addAll(validateEvent.getErrors());
        }
    }

    private EntityStates getEntityStates() {
        return getBeanLocator().get(EntityStates.NAME);
    }

    protected void commitAndClose(@SuppressWarnings("unused") Action.ActionPerformedEvent event) {
        closeWithCommit();
    }

    protected void commit(@SuppressWarnings("unused") Action.ActionPerformedEvent event) {
        commitChanges()
                .then(() -> {
                    commitActionPerformed = true;
                    if (showSaveNotification) {
                        Entity entity = getEditedEntity();
                        MetadataTools metadataTools = getBeanLocator().get(MetadataTools.NAME);
                        Messages messages = getBeanLocator().get(Messages.NAME);

                        getScreenContext().getNotifications().create(NotificationType.TRAY)
                                .withCaption(messages.formatMainMessage("info.EntitySave",
                                        messages.getTools().getEntityCaption(entity.getMetaClass()),
                                        metadataTools.getInstanceName(entity)))
                                .show();
                    }
                });
    }

    protected void cancel(@SuppressWarnings("unused") Action.ActionPerformedEvent event) {
        close(commitActionPerformed ?
                WINDOW_COMMIT_AND_CLOSE_ACTION : WINDOW_CLOSE_ACTION);
    }

    /**
     * Tries to validate and commit data. If data committed successfully then closes the screen with
     * {@link #WINDOW_COMMIT_AND_CLOSE} action. May show validation errors or open an additional dialog before closing
     * the screen.
     *
     * @return result of close request
     */
    public OperationResult closeWithCommit() {
        return commitChanges()
                .compose(() -> close(WINDOW_COMMIT_AND_CLOSE_ACTION));
    }

    /**
     * Ignores the unsaved changes and closes the screen with {@link #WINDOW_DISCARD_AND_CLOSE_ACTION} action.
     *
     * @return result of close request
     */
    public OperationResult closeWithDiscard() {
        return close(WINDOW_DISCARD_AND_CLOSE_ACTION);
    }

    /**
     * @return whether a notification will be shown in case of successful commit
     */
    public boolean isShowSaveNotification() {
        return showSaveNotification;
    }

    /**
     * Sets whether a notification will be shown in case of successful commit.
     *
     * @param showSaveNotification {@code true} if a notification needs to be shown, {@code false} otherwise
     */
    public void setShowSaveNotification(boolean showSaveNotification) {
        this.showSaveNotification = showSaveNotification;
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
     * Adds a listener to {@link BeforeCommitChangesEvent}.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addBeforeCommitChangesListener(Consumer<BeforeCommitChangesEvent> listener) {
        return getEventHub().subscribe(BeforeCommitChangesEvent.class, listener);
    }

    /**
     * Adds a listener to {@link AfterCommitChangesEvent}.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addAfterCommitChangesListener(Consumer<AfterCommitChangesEvent> listener) {
        return getEventHub().subscribe(AfterCommitChangesEvent.class, listener);
    }

    /**
     * Adds a listener to {@link ValidationEvent}.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addValidationEventListener(Consumer<ValidationEvent> listener) {
        return getEventHub().subscribe(ValidationEvent.class, listener);
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
    @TriggerOnce
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
     * Event sent before commit of data context from {@link #commitChanges()} call.
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
     *                                 event.resume(commitChanges());
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

        public BeforeCommitChangesEvent(Screen source, @Nullable Runnable resumeAction) {
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
         * Prevents commit of the screen.
         */
        public void preventCommit() {
            preventCommit(new UnknownOperationResult());
        }

        /**
         * Prevents commit of the screen.
         *
         * @param commitResult result object that will be returned from the {@link #commitChanges()}} method
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
     * Event sent after commit of data context from {@link #commitChanges()} call.
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

    /**
     * Event sent when screen is validated from {@link #validateAdditionalRules(ValidationErrors)} call.
     * <br>
     * Use this event listener to perform additional screen validation, for example:
     * <pre>
     *     &#64;Subscribe
     *     protected void onScreenValidation(ValidationEvent event) {
     *         ValidationErrors errors = performCustomValidation();
     *         event.addErrors(errors);
     *     }
     * </pre>
     */
    public static class ValidationEvent extends EventObject {

        ValidationErrors errors = new ValidationErrors();

        public ValidationEvent(Screen source) {
            super(source);
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        public void addErrors(ValidationErrors errors) {
            Preconditions.checkNotNullArgument(errors, "Validation errors cannot be null");

            this.errors.addAll(errors);
        }

        public ValidationErrors getErrors() {
            return errors;
        }
    }
}