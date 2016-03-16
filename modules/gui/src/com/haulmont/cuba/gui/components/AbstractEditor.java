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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;

import javax.annotation.Nullable;

/**
 * Base class for edit screen controllers.
 *
 */
public class AbstractEditor<T extends Entity> extends AbstractWindow implements Window.Editor {

    protected boolean showSaveNotification = true;

    public AbstractEditor() {
    }

    @Override
    public T getItem() {
        return (T) ((Editor) frame).getItem();
    }

    @Nullable
    @Override
    public Datasource getParentDs() {
        return ((Editor) frame).getParentDs();
    }

    @Override
    public void setParentDs(Datasource parentDs) {
        ((Editor) frame).setParentDs(parentDs);
    }

    /**
     * Called by the framework to set an edited entity after creation of all components and datasources, and after
     * {@link #init(java.util.Map)}.
     * <p>Don't override this method in subclasses, use hooks {@link #initNewItem(com.haulmont.cuba.core.entity.Entity)}
     * and {@link #postInit()} instead.</p>
     * @param item  entity instance
     */
    @Override
    public void setItem(Entity item) {
        if (PersistenceHelper.isNew(item)) {
            DatasourceImplementation parentDs = (DatasourceImplementation) ((Editor) frame).getParentDs();
            if (parentDs == null || !parentDs.getItemsToCreate().contains(item)) {
                //noinspection unchecked
                initNewItem((T) item);
            }
        }
        ((Editor) frame).setItem(item);
        postInit();
    }

    @Override
    public boolean isModified() {
        return getDsContext() != null && getDsContext().isModified();
    }

    /**
     * Called by the framework to validate and commit changes.
     * <p>Don't override this method in subclasses, use hooks {@link #postValidate(ValidationErrors)}, {@link #preCommit()}
     * and {@link #postCommit(boolean, boolean)} instead.</p>
     * @return true if commit was succesful
     */
    @Override
    public boolean commit() {
        return ((Editor) frame).commit();
    }

    /**
     * Commit changes with optional validation.
     * <p>Don't override this method in subclasses, use hooks {@link #postValidate(ValidationErrors)}, {@link #preCommit()}
     * and {@link #postCommit(boolean, boolean)} instead.</p>
     * @param validate false to avoid validation
     * @return true if commit was succesful
     */
    @Override
    public boolean commit(boolean validate) {
        return ((Editor) frame).commit(validate);
    }

    /**
     * Validate, commit and close the window if commit was successful.
     * Passes {@link #COMMIT_ACTION_ID} to associated {@link CloseListener}s
     * <p>Don't override this method in subclasses, use hooks {@link #postValidate(ValidationErrors)}, {@link #preCommit()}
     * and {@link #postCommit(boolean, boolean)} instead.</p>
     */
    @Override
    public void commitAndClose() {
        ((Editor) frame).commitAndClose();
    }

    @Override
    public boolean isLocked() {
        return ((Editor) frame).isLocked();
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
     * <p/>
     * This method can be called second time by {@link #postCommit(boolean, boolean)} if the window is not closed after
     * commit. Then {@link #getItem()} contains instance, returned from {@code DataService.commit()}.
     * This is useful for initialization of components that have to show fresh information from the current instance.
     * <p/>
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
     * @param committed whether any data were actually changed and committed
     * @param close     whether the window is going to be closed
     * @return  true to continue, false to abort
     */
    protected boolean postCommit(boolean committed, boolean close) {
        if (committed && !close) {
            if (showSaveNotification) {
                Entity entity = ((Editor) frame).getItem();
                frame.showNotification(
                        messages.formatMessage(AppConfig.getMessagesPack(), "info.EntitySave",
                                messages.getTools().getEntityCaption(entity.getMetaClass()), entity.getInstanceName()),
                        NotificationType.TRAY);
            }
            postInit();
        }
        return true;
    }

    public boolean isShowSaveNotification() {
        return showSaveNotification;
    }

    public void setShowSaveNotification(boolean showSaveNotification) {
        this.showSaveNotification = showSaveNotification;
    }
}
