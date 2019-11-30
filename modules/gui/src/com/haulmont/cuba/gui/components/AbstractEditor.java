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
package com.haulmont.cuba.gui.components;

import com.google.common.collect.Iterables;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Categorized;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.CollectionPropertyDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.data.impl.EntityCopyUtils;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.screen.ReadOnlyScreensSupport;
import com.haulmont.cuba.gui.screen.ReadOnlyAwareScreen;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Base class for edit screen controllers.
 */
public class AbstractEditor<T extends Entity> extends AbstractWindow
        implements Window.Editor<T>, ReadOnlyAwareScreen {

    protected boolean showSaveNotification = true;

    protected boolean readOnly = false;
    protected boolean readOnlyDueToLock = false;
    protected boolean justLocked = false;
    protected boolean crossFieldValidate = true;

    protected boolean showEnableEditingBtn = true;

    protected boolean commitActionPerformed = false;

    public AbstractEditor() {
        addInitListener(this::initCommitActions);
    }

    protected void initCommitActions(@SuppressWarnings("unused") InitEvent event) {
        Component commitAndCloseButton =
                ComponentsHelper.findComponent(getFrame(), WINDOW_COMMIT_AND_CLOSE);

        Configuration configuration = getBeanLocator().get(Configuration.NAME);

        boolean commitAndCloseButtonExists = false;
        String commitShortcut = configuration.getConfig(ClientConfig.class).getCommitShortcut();
        if (commitAndCloseButton != null) {
            commitAndCloseButtonExists = true;

            getFrame().addAction(
                    new BaseAction(WINDOW_COMMIT_AND_CLOSE)
                            .withCaption(messages.getMainMessage("actions.OkClose"))
                            .withPrimary(true)
                            .withShortcut(commitShortcut)
                            .withHandler(e -> commitAndClose()));
        }

        boolean finalCommitAndCloseButtonExists = commitAndCloseButtonExists;

        Action commitAction = new BaseAction(WINDOW_COMMIT)
                .withCaption(messages.getMainMessage(commitAndCloseButtonExists ? "actions.Save" : "actions.Ok"))
                .withPrimary(!commitAndCloseButtonExists)
                .withShortcut(commitAndCloseButtonExists ? null : commitShortcut)
                .withHandler(e -> {
                    if (!finalCommitAndCloseButtonExists) {
                        commitAndClose();
                    } else {
                        if (commit()) {
                            commitActionPerformed = true;
                        }
                    }
                });
        getFrame().addAction(commitAction);

        Action closeAction = new BaseAction(WINDOW_CLOSE)
                .withCaption(messages.getMainMessage("actions.Cancel"))
                .withHandler(e ->
                        close(commitActionPerformed ? Window.COMMIT_ACTION_ID : getId())
                );

        getFrame().addAction(closeAction);

        Action enableEditingAction = new BaseAction(ENABLE_EDITING)
                .withCaption(messages.getMainMessage("actions.EnableEditing"))
                .withHandler(e -> setReadOnly(false));
        enableEditingAction.setVisible(false);
        getFrame().addAction(enableEditingAction);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getItem() {
        return (T) getDatasourceInternal().getItem();
    }

    @Nullable
    @Override
    public Datasource getParentDs() {
        Datasource ds = getDatasourceInternal();
        return ((DatasourceImplementation) ds).getParent();
    }

    /**
     * Called by the framework to set an edited entity after creation of all components and datasources, and after
     * {@link #init(java.util.Map)}.
     * <p>Don't override this method in subclasses, use hooks {@link #initNewItem(com.haulmont.cuba.core.entity.Entity)}
     * and {@link #postInit()} instead.</p>
     * @param item  entity instance
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setItem(Entity item) {
        if (PersistenceHelper.isNew(item)) {
            DatasourceImplementation parentDs = (DatasourceImplementation) getParentDs();
            if (parentDs == null || !parentDs.getItemsToCreate().contains(item)) {
                initNewItem((T) item);
            }
        }

        setItemInternal(item);

        postInit();
    }

    protected Datasource getDatasourceInternal() {
        Datasource ds = null;
        Element element = ((Component.HasXmlDescriptor) getFrame()).getXmlDescriptor();
        String datasourceName = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasourceName)) {
            DsContext context = getDsContext();
            if (context != null) {
                ds = context.get(datasourceName);
            }
        }

        if (ds == null) {
            throw new GuiDevelopmentException("Can't find main datasource", getFrame().getId());
        }

        return ds;
    }

    @SuppressWarnings("unchecked")
    protected void setItemInternal(Entity item) {
        Datasource ds = getDatasourceInternal();
        DataSupplier dataservice = ds.getDataSupplier();

        DatasourceImplementation parentDs = (DatasourceImplementation) ((DatasourceImplementation) ds).getParent();

        DynamicAttributesGuiTools dynamicAttributesGuiTools = getBeanLocator().get(DynamicAttributesGuiTools.NAME);
        if (dynamicAttributesGuiTools.screenContainsDynamicAttributes(ds.getView(), getFrame().getId())) {
            ds.setLoadDynamicAttributes(true);
        }

        Class<? extends Entity> entityClass = item.getClass();
        Object entityId = item.getId();

        EntityStates entityStates = getBeanLocator().get(EntityStates.class);

        if (parentDs != null) {
            if (!PersistenceHelper.isNew(item)
                    && !parentDs.getItemsToCreate().contains(item) && !parentDs.getItemsToUpdate().contains(item)
                    && parentDs instanceof CollectionDatasource
                    && ((CollectionDatasource) parentDs).containsItem(item.getId())
                    && !entityStates.isLoadedWithView(item, ds.getView())) {
                item = dataservice.reload(item, ds.getView(), ds.getMetaClass(), ds.getLoadDynamicAttributes());
                if (parentDs instanceof CollectionPropertyDatasourceImpl) {
                    ((CollectionPropertyDatasourceImpl) parentDs).replaceItem(item);
                } else {
                    ((CollectionDatasource) parentDs).updateItem(item);
                }
            }
            item = EntityCopyUtils.copyCompositions(item);
            handlePreviouslyDeletedCompositionItems(item, parentDs);
        } else if (!PersistenceHelper.isNew(item)) {
            item = dataservice.reload(item, ds.getView(), ds.getMetaClass(), ds.getLoadDynamicAttributes());
        }

        if (item == null) {
            throw new EntityAccessException(entityClass, entityId);
        }

        if (PersistenceHelper.isNew(item)
                && !ds.getMetaClass().equals(item.getMetaClass())) {
            Entity newItem = ds.getDataSupplier().newInstance(ds.getMetaClass());
            MetadataTools metadataTools = getBeanLocator().get(MetadataTools.NAME);
            metadataTools.copy(item, newItem);
            item = newItem;
        }

        if (ds.getLoadDynamicAttributes() && item instanceof BaseGenericIdEntity) {
            if (PersistenceHelper.isNew(item)) {
                dynamicAttributesGuiTools.initDefaultAttributeValues((BaseGenericIdEntity) item, item.getMetaClass());
            }
            if (item instanceof Categorized) {
                dynamicAttributesGuiTools.listenCategoryChanges(ds);
            }
        }

        ds.setItem(item);

        if (PersistenceHelper.isNew(item)) {
            // The new item may contain references which were created in initNewItem() and are also new. Below we
            // make sure that they will be saved on commit.
            for (Datasource datasource : ds.getDsContext().getAll()) {
                if (datasource instanceof NestedDatasource && ((NestedDatasource) datasource).getMaster() == ds) {
                    if (datasource.getItem() != null && PersistenceHelper.isNew(datasource.getItem()))
                        ((DatasourceImplementation) datasource).modified(datasource.getItem());
                }
            }
        }

        ((DatasourceImplementation) ds).setModified(false);

        Security security = getBeanLocator().get(Security.NAME);
        if (!PersistenceHelper.isNew(item)) {
            if (security.isEntityOpPermitted(ds.getMetaClass(), EntityOp.UPDATE)) {
                readOnlyDueToLock = false;

                LockService lockService = getBeanLocator().get(LockService.NAME);

                LockInfo lockInfo = lockService.lock(getMetaClassForLocking(ds).getName(), item.getId().toString());
                if (lockInfo == null) {
                    justLocked = true;
                    addAfterCloseListener(afterCloseEvent -> {
                        releaseLock();
                    });
                } else if (!(lockInfo instanceof LockNotSupported)) {
                    UserSessionSource userSessionSource = getBeanLocator().get(UserSessionSource.NAME);

                    getFrame().getWindowManager().showNotification(
                            messages.getMainMessage("entityLocked.msg"),
                            String.format(messages.getMainMessage("entityLocked.desc"),
                                    lockInfo.getUser().getLogin(),
                                    Datatypes.getNN(Date.class).format(lockInfo.getSince(), userSessionSource.getLocale())
                            ),
                            Frame.NotificationType.HUMANIZED
                    );

                    readOnlyDueToLock = true;
                    showEnableEditingBtn = false;
                    setReadOnly(true);
                }
            } else {
                showEnableEditingBtn = false;
                setReadOnly(true);
            }
        }
    }

    public void releaseLock() {
        if (justLocked) {
            Datasource ds = getDatasourceInternal();
            Entity entity = ds.getItem();
            if (entity != null) {
                getBeanLocator().get(LockService.class).unlock(getMetaClassForLocking(ds).getName(), entity.getId().toString());
            }
        }
    }

    /**
     * This method is required for multi-level composition, when a user deletes records from nested editors, saves them
     * and then reopens. When an editor is opened, we reload the item from the database, hence we need to remove
     * nested items previously deleted by the user.
     */
    protected void handlePreviouslyDeletedCompositionItems(Entity entity, DatasourceImplementation parentDs) {
        Metadata metadata = getBeanLocator().get(Metadata.NAME);
        for (MetaProperty property : metadata.getClassNN(entity.getClass()).getProperties()) {
            if (!PersistenceHelper.isLoaded(entity, property.getName()))
                return;

            if (property.getType() == MetaProperty.Type.COMPOSITION) {
                for (Datasource datasource : parentDs.getDsContext().getAll()) {
                    if (datasource instanceof NestedDatasource
                            && ((NestedDatasource) datasource).getMaster().equals(parentDs)) {
                        Object value = entity.getValue(property.getName());
                        if (value instanceof Collection) {
                            Collection collection = (Collection) value;
                            //noinspection unchecked
                            collection.removeAll(((DatasourceImplementation) datasource).getItemsToDelete());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setParentDs(Datasource parentDs) {
        Datasource ds = getDatasourceInternal();
        ((DatasourceImplementation) ds).setParent(parentDs);
    }

    protected MetaClass getMetaClassForLocking(Datasource ds) {
        Metadata metadata = getBeanLocator().get(Metadata.NAME);
        // lock original metaClass, if any, because by convention all the configuration is based on original entities
        MetaClass metaClass = metadata.getExtendedEntities().getOriginalMetaClass(ds.getMetaClass());
        if (metaClass == null) {
            metaClass = ds.getMetaClass();
        }
        return metaClass;
    }

    @Override
    public boolean isModified() {
        return getDsContext() != null && getDsContext().isModified();
    }

    /**
     * Called by the framework to validate and commit changes.
     * <p>Don't override this method in subclasses, use hooks {@link #postValidate(ValidationErrors)}, {@link #preCommit()}
     * and {@link #postCommit(boolean, boolean)} instead.</p>
     *
     * @return true if commit was successful
     */
    @Override
    public boolean commit() {
        return commit(true);
    }

    /**
     * Commit changes with optional validation.
     * <p>Don't override this method in subclasses, use hooks {@link #postValidate(ValidationErrors)}, {@link #preCommit()}
     * and {@link #postCommit(boolean, boolean)} instead.</p>
     *
     * @param validate false to avoid validation
     * @return true if commit was successful
     */
    @Override
    public boolean commit(boolean validate) {
        if (validate && !validateAll())
            return false;

        return commitInternal(false);
    }

    protected boolean commitInternal(boolean close) {
        if (!preCommit())
            return false;

        boolean committed;

        DsContext context = getDsContext();
        if (context != null) {
            committed = context.commit();
        } else {
            DataSupplier supplier = getDataService();
            supplier.commit(getItem());
            committed = true;
        }

        return postCommit(committed, close);
    }

    public void validateAdditionalRules(ValidationErrors errors) {
        // all previous validations return no errors
        if (crossFieldValidate && errors.isEmpty()) {
            BeanValidation beanValidation = getBeanLocator().get(BeanValidation.NAME);

            Validator validator = beanValidation.getValidator();
            Set<ConstraintViolation<Entity>> violations = validator.validate(getItem(), UiCrossFieldChecks.class);

            violations.stream()
                    .filter(violation -> {
                        Path propertyPath = violation.getPropertyPath();

                        Path.Node lastNode = Iterables.getLast(propertyPath);
                        return lastNode.getKind() == ElementKind.BEAN;
                    })
                    .forEach(violation -> errors.add(violation.getMessage()));
        }
    }

    private DataSupplier getDataService() {
        DsContext context = getDsContext();
        if (context == null) {
            throw new UnsupportedOperationException();
        } else {
            return context.getDataSupplier();
        }
    }

    /**
     * Tries to validate and commit data. If data committed successfully then closes the screen with
     * {@link #WINDOW_COMMIT_AND_CLOSE} action. May show validation errors or open an additional dialog before closing
     * the screen.
     *
     * @return result of close request
     */
    public OperationResult closeWithCommit() {
        if (validateAll()) {
            boolean committed = commitInternal(true);
            if (committed) {
                return close(WINDOW_COMMIT_AND_CLOSE_ACTION);
            }
        }
        return OperationResult.fail();
    }

    /**
     * Validate, commit and close the window if commit was successful.
     * Passes {@link #COMMIT_ACTION_ID} to associated {@link CloseListener}s
     * <p>Don't override this method in subclasses, use hooks {@link #postValidate(ValidationErrors)}, {@link #preCommit()}
     * and {@link #postCommit(boolean, boolean)} instead.</p>
     */
    @Override
    public void commitAndClose() {
        closeWithCommit();
    }

    @Override
    public void setEntityToEdit(T entity) {
        setItem(entity);
    }

    @Override
    public T getEditedEntity() {
        return getItem();
    }

    @Override
    public boolean hasUnsavedChanges() {
        return !readOnlyDueToLock
                && super.hasUnsavedChanges();
    }

    @Override
    public boolean isLocked() {
        return justLocked;
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
                disableCommitAction();
            }
        }
    }

    protected void disableCommitAction() {
        Action action = getFrame().getAction(WINDOW_COMMIT);
        if (action != null) {
            action.setEnabled(false);
        }

        action = getFrame().getAction(WINDOW_COMMIT_AND_CLOSE);
        if (action != null) {
            action.setEnabled(false);
        }
    }

    @Override
    public boolean isCrossFieldValidate() {
        return crossFieldValidate;
    }

    @Override
    public void setCrossFieldValidate(boolean crossFieldValidate) {
        this.crossFieldValidate = crossFieldValidate;
    }

    /**
     * Hook to be implemented in subclasses. Called by {@link #setItem(com.haulmont.cuba.core.entity.Entity)} when
     * the editor is opened for a new entity instance. Allows to additionally initialize the new entity instance
     * before setting it into the datasource.
     * @param item  entity instance
     */
    protected void initNewItem(T item) {
    }

    /**
     * Hook to be implemented in subclasses. Called by {@link #setItem(com.haulmont.cuba.core.entity.Entity)}.
     * At the moment of calling the main datasource is initialized and {@link #getItem()} returns reloaded entity instance.
     * <br>
     * This method can be called second time by {@link #postCommit(boolean, boolean)} if the window is not closed after
     * commit. Then {@link #getItem()} contains instance, returned from {@code DataService.commit()}.
     * This is useful for initialization of components that have to show fresh information from the current instance.
     * <br>
     * Example:
     * <pre>
     * protected void postInit() {
     *     if (!PersistenceHelper.isNew(getItem())) {
     *        diffFrame.loadVersions(getItem());
     *        entityLogDs.refresh();
     *    }
     * }
     * </pre>
     */
    protected void postInit() {
    }

    /**
     * Hook to be implemented in subclasses. Called by the framework when all validation is done and datasources are
     * going to be committed.
     * @return  true to continue, false to abort
     */
    protected boolean preCommit() {
        return true;
    }

    /**
     * Hook to be implemented in subclasses. Called by the framework after committing datasources.
     * The default implementation notifies about commit and calls {@link #postInit()} if the window is not closing.
     *
     * @param committed whether any data were actually changed and committed
     * @param close     whether the window is going to be closed
     * @return true to continue, false to abort
     */
    protected boolean postCommit(boolean committed, boolean close) {
        if (committed && !close) {
            if (showSaveNotification) {
                Entity entity = getItem();
                MetadataTools metadataTools = getBeanLocator().get(MetadataTools.class);

                showNotification(
                        messages.formatMainMessage("info.EntitySave",
                                messages.getTools().getEntityCaption(entity.getMetaClass()),
                                metadataTools.getInstanceName(entity)),
                        NotificationType.TRAY);
            }
            postInit();

            afterWindowApplyPostInit();
        }
        return true;
    }

    /**
     * Called after "postInit" in "Apply" action processing.
     */
    protected void afterWindowApplyPostInit() {
        if (!WindowParams.DISABLE_RESUME_SUSPENDED.getBool(getContext())) {
            ((DsContextImplementation) getDsContext()).resumeSuspended();
        }
    }

    public boolean isShowSaveNotification() {
        return showSaveNotification;
    }

    public void setShowSaveNotification(boolean showSaveNotification) {
        this.showSaveNotification = showSaveNotification;
    }
}