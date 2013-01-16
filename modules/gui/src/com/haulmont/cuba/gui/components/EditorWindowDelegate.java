/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.*;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
* <p>$Id$</p>
*
* @author krivopustov
*/
public class EditorWindowDelegate extends WindowDelegate {

    protected Entity item;
    protected boolean justLocked;
    protected boolean commitActionPerformed;
    protected boolean commitAndCloseButtonExists;

    protected Metadata metadata = AppBeans.get(Metadata.class);
    protected Messages messages = AppBeans.get(Messages.class);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
    protected LockService lockService = AppBeans.get(LockService.class);

    public EditorWindowDelegate(Window window) {
        super(window);
    }

    public Window wrapBy(Class<Window> wrapperClass) {
        final Window.Editor editor = (Window.Editor) super.wrapBy(wrapperClass);

        final Component commitAndCloseButton = ComponentsHelper.findComponent(editor, Window.Editor.WINDOW_COMMIT_AND_CLOSE);
        if (commitAndCloseButton != null) {
            commitAndCloseButtonExists = true;

            this.window.addAction(
                    new AbstractAction(Window.Editor.WINDOW_COMMIT_AND_CLOSE) {
                        @Override
                        public String getCaption() {
                            return messages.getMainMessage("actions.OkClose");
                        }

                        public void actionPerform(Component component) {
                            editor.commitAndClose();
                        }
                    }
            );
        }

        this.window.addAction(
                new AbstractAction(Window.Editor.WINDOW_COMMIT) {
                    @Override
                    public String getCaption() {
                        return messages.getMainMessage("actions.Ok");
                    }

                    public void actionPerform(Component component) {
                        if (!commitAndCloseButtonExists) {
                            editor.commitAndClose();
                        } else {
                            if (editor.commit()) {
                                commitActionPerformed = true;
                            }
                        }
                    }
                }
        );

        this.window.addAction(
                new AbstractAction(Window.Editor.WINDOW_CLOSE) {
                    @Override
                    public String getCaption() {
                        return messages.getMainMessage("actions.Cancel");
                    }

                    public void actionPerform(Component component) {
                        editor.close(commitActionPerformed ? Window.COMMIT_ACTION_ID : getId());
                    }
                }
        );

        return editor;
    }

    public Entity getItem() {
        return item;
    }

    public void setItem(Entity item) {
        final Datasource ds = getDatasource();

        if (ds.getCommitMode().equals(Datasource.CommitMode.PARENT)) {
            Datasource parentDs = ((DatasourceImpl) ds).getParent();
            //We have to reload items in parent datasource because when item in child datasource is commited,
            //item in parant datasource must already have all item fields loaded.
            if (parentDs != null) {
                Collection justChangedItems = new HashSet(((AbstractDatasource) parentDs).getItemsToCreate());
                justChangedItems.addAll(((AbstractDatasource) parentDs).getItemsToUpdate());

                DataSupplier dataservice = ds.getDataSupplier();
                if ((parentDs instanceof CollectionDatasourceImpl) && !(justChangedItems.contains(item)) && ((CollectionDatasourceImpl) parentDs).containsItem(item)) {
                    item = dataservice.reload(item, ds.getView(), ds.getMetaClass());
                    ((CollectionDatasourceImpl) parentDs).updateItem(item);
                } else if ((parentDs instanceof LazyCollectionDatasource) && !(justChangedItems.contains(item)) && ((LazyCollectionDatasource) parentDs).containsItem(item)) {
                    item = dataservice.reload(item, ds.getView(), ds.getMetaClass());
                    ((LazyCollectionDatasource) parentDs).updateItem(item);
                } else if ((parentDs instanceof CollectionPropertyDatasourceImpl) && !(justChangedItems.contains(item)) && ((CollectionPropertyDatasourceImpl) parentDs).containsItem(item)) {
                    item = dataservice.reload(item, ds.getView(), ds.getMetaClass());
                    ((CollectionPropertyDatasourceImpl) parentDs).replaceItem(item);
                }
            }
            item = (Entity) InstanceUtils.copy(item);
        } else {
            if (!PersistenceHelper.isNew(item)) {
                String useSecurityConstraintsParam = (String) window.getContext().getParams().get("useSecurityConstraints");
                boolean useSecurityConstraints = !("false".equals(useSecurityConstraintsParam));
                final DataSupplier dataservice = ds.getDataSupplier();
                item = dataservice.reload(item, ds.getView(), ds.getMetaClass(), useSecurityConstraints);
            }
        }

        if (item == null) {
            throw new EntityAccessException();
        }

        if (PersistenceHelper.isNew(item)
                && !ds.getMetaClass().equals(item.getMetaClass()))
        {
            Entity newItem = ds.getDataSupplier().newInstance(ds.getMetaClass());
            InstanceUtils.copy(item, newItem);
            item = newItem;
        }

        this.item = item;
        //noinspection unchecked
        ds.setItem(item);
        ((DatasourceImplementation) ds).setModified(false);

        if (userSessionSource.getUserSession().isEntityOpPermitted(ds.getMetaClass(), EntityOp.UPDATE)) {
            LockInfo lockInfo = lockService.lock(getMetaClassForLocking(ds).getName(), item.getId().toString());
            if (lockInfo == null) {
                justLocked = true;
            } else if (!(lockInfo instanceof LockNotSupported)) {
                window.getWindowManager().showNotification(
                        messages.getMainMessage("entityLocked.msg"),
                        String.format(messages.getMainMessage("entityLocked.desc"),
                                lockInfo.getUser().getLogin(),
                                Datatypes.get(Date.class).format(lockInfo.getSince(), userSessionSource.getLocale())
                        ),
                        IFrame.NotificationType.HUMANIZED
                );
                Action action = window.getAction(Window.Editor.WINDOW_COMMIT);
                if (action != null)
                    action.setEnabled(false);
                action = window.getAction(Window.Editor.WINDOW_COMMIT_AND_CLOSE);
                if (action != null)
                    action.setEnabled(false);
            }
        }
    }

    public void setParentDs(Datasource parentDs) {
        Datasource ds = getDatasource();
        ((DatasourceImplementation) ds).setParent(parentDs);
    }

    public boolean commit(boolean close) {
        if (wrapper instanceof AbstractEditor && !((AbstractEditor) wrapper).preCommit())
            return false;

        boolean committed;
        final DsContext context = window.getDsContext();
        if (context != null) {
            committed = context.commit();
            item = getDatasource().getItem();
        } else {
            if (item instanceof Datasource) {
                final Datasource ds = (Datasource) item;
                ds.commit();
            } else {
                DataSupplier supplier = getDataService();
                item = supplier.commit(item, null);
            }
            committed = true;
        }

        return !(wrapper instanceof AbstractEditor) || ((AbstractEditor) wrapper).postCommit(committed, close);
    }

    protected DataSupplier getDataService() {
        final DsContext context = window.getDsContext();
        if (context == null) {
            throw new UnsupportedOperationException();
        } else {
            return context.getDataService();
        }
    }

    public boolean isLocked() {
        return !justLocked;
    }

    public void releaseLock() {
        if (justLocked) {
            Datasource ds = getDatasource();
            Entity entity = ds.getItem();
            if (entity != null) {
                lockService.unlock(getMetaClassForLocking(ds).getName(), entity.getId().toString());
            }
        }
    }

    protected MetaClass getMetaClassForLocking(Datasource ds) {
        // lock original metaClass, if any, because by convention all the configuration is based on original entities
        MetaClass metaClass = metadata.getExtendedEntities().getOriginalMetaClass(ds.getMetaClass());
        if (metaClass == null) {
            metaClass = ds.getMetaClass();
        }
        return metaClass;
    }
}
