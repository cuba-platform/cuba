/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.data.Datasource;

/**
 * Base class for edit screen controllers.
 *
 * @author Abramov
 * @version $Id$
 */
public class AbstractEditor<T extends Entity> extends AbstractWindow implements Window.Editor {

    protected boolean showSaveNotification = true;

    public AbstractEditor() {
    }

    @Override
    public T getItem() {
        return (T) ((Editor) frame).getItem();
    }

    @Override
    public void setParentDs(Datasource parentDs) {
        ((Editor) frame).setParentDs(parentDs);
    }

    /**
     * Called by the framework to set an edited entity after creation of all components and datasources, and after
     * {@link #init(java.util.Map)}.
     * <p>Don't override this method in subclasses, use hooks {@link #initItem(com.haulmont.cuba.core.entity.Entity)}
     * and {@link #postInit()} instead.</p>
     * @param item  entity instance
     */
    @Override
    public void setItem(Entity item) {
        //noinspection unchecked
        initItem((T) item);
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
     * Hook to be implemented in subclasses. Called by {@link #setItem(com.haulmont.cuba.core.entity.Entity)}.
     * Allows to additionally initialize the entity instance before setting it into the datasource.
     * @param item  entity instance
     */
    protected void initItem(T item) {
    }

    /**
     * Hook to be implemented in subclasses. Called by {@link #setItem(com.haulmont.cuba.core.entity.Entity)}.
     * At the moment of calling the main datasource is initialized and {@link #getItem()} returns reloaded entity instance.
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
                frame.showNotification(messages.formatMessage(AppConfig.getMessagesPack(),
                        "info.EntitySave", ((Editor) frame).getItem().getInstanceName()),
                        NotificationType.HUMANIZED);
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
