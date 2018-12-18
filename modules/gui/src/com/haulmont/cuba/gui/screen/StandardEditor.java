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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
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
public abstract class StandardEditor<T extends Entity> extends Screen implements EditorScreen<T> {

    protected boolean commitActionPerformed = false;

    private T entityToEdit;
    private boolean crossFieldValidate = true;
    private boolean justLocked = false;
    private boolean readOnly = false;

    protected StandardEditor() {
        addInitListener(this::initActions);
        addBeforeShowListener(this::beforeShow);
        addBeforeCloseListener(this::beforeClose);
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
    }

    private void beforeShow(@SuppressWarnings("unused") BeforeShowEvent beforeShowEvent) {
        setupEntityToEdit();
        setupLock();
        loadData();
    }

    private void beforeClose(BeforeCloseEvent event) {
        preventUnsavedChanges(event);
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
                    .onCommit(() -> result.resolveWith(closeWithCommit()))
                    .onDiscard(() -> result.resolveWith(closeWithDiscard()))
                    .onCancel(result::fail);
            } else {
                screenValidation.showUnsavedChangesDialog(this, action)
                        .onYes(() -> result.resolveWith(closeWithCommit()))
                        .onNo(result::fail);
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
        if (tools.screenContainsDynamicAttributes(getEditedEntityContainer().getView(), screenId)) {
            getEditedEntityLoader().setLoadDynamicAttributes(true);
        }

        if (getEntityStates().isNew(entityToEdit) || doNotReloadEditedEntity()) {
            T mergedEntity = getScreenData().getDataContext().merge(entityToEdit);

            if (getEditedEntityLoader().isLoadDynamicAttributes()
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

    private void loadData() {
        LoadDataBeforeShow annotation = getClass().getAnnotation(LoadDataBeforeShow.class);
        if (annotation != null && annotation.value()) {
            getScreenData().loadAll();
        }
    }

    protected void setupLock() {
        InstanceContainer<T> container = getEditedEntityContainer();
        Security security = getBeanLocator().get(Security.class);

        if (!getEntityStates().isNew(entityToEdit)
                && security.isEntityOpPermitted(container.getEntityMetaClass(), EntityOp.UPDATE)) {
            this.readOnly = false;

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

                Action commitAction = getWindow().getAction(WINDOW_COMMIT);
                if (commitAction != null) {
                    commitAction.setEnabled(false);
                }

                Action commitCloseAction = getWindow().getAction(WINDOW_COMMIT_AND_CLOSE);
                if (commitCloseAction != null) {
                    commitCloseAction.setEnabled(false);
                }

                this.readOnly = true;
            }
        }
    }

    protected void releaseLock() {
        if (justLocked) {
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
        if (readOnly) {
            return false;
        }

        return getScreenData().getDataContext().hasChanges();
    }

    protected OperationResult commitChanges() {
        ValidationErrors validationErrors = validateScreen();
        if (!validationErrors.isEmpty()) {
            ScreenValidation screenValidation = getBeanLocator().get(ScreenValidation.class);
            screenValidation.showValidationErrors(this, validationErrors);

            return OperationResult.fail();
        }

        getScreenData().getDataContext().commit();

        return OperationResult.success();
    }

    @Override
    public boolean isLocked() {
        return justLocked;
    }

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
                .then(() -> commitActionPerformed = true);
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
     * Adds a listener to {@link InitEntityEvent}.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe
     *    protected void onInitEntity(InitEntityEvent event) {
     *       // handle event here
     *    }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    @SuppressWarnings("unchecked")
    protected Subscription addInitEntityListener(Consumer<InitEntityEvent<T>> listener) {
        return getEventHub().subscribe(InitEntityEvent.class, (Consumer) listener);
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
    public static class InitEntityEvent<E> extends EventObject {
        private final E entity;

        public InitEntityEvent(Screen source, E entity) {
            super(source);
            this.entity = entity;
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        public E getEntity() {
            return entity;
        }
    }
}