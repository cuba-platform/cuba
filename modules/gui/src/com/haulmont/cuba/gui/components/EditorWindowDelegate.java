/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LockInfo;
import com.haulmont.cuba.core.global.LockNotSupported;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.*;

import java.text.SimpleDateFormat;
import java.util.Collection;
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

    public EditorWindowDelegate(Window window, WindowManager windowManager) {
        super(window, windowManager);
    }

    public Window wrapBy(Class<Window> wrapperClass) {
        final Window.Editor window = (Window.Editor) super.wrapBy(wrapperClass);

        final Component commitAndCloseButton = ComponentsHelper.findComponent(window, Window.Editor.WINDOW_COMMIT_AND_CLOSE);
        if (commitAndCloseButton != null) {
            commitAndCloseButtonExists = true;

            this.window.addAction(
                    new AbstractAction(Window.Editor.WINDOW_COMMIT_AND_CLOSE) {
                        @Override
                        public String getCaption() {
                            final String messagesPackage = AppConfig.getMessagesPack();
                            return MessageProvider.getMessage(messagesPackage, "actions.OkClose");
                        }

                        public void actionPerform(Component component) {
                            window.commitAndClose();
                        }
                    }
            );
        }

        this.window.addAction(
                new AbstractAction(Window.Editor.WINDOW_COMMIT) {
                    @Override
                    public String getCaption() {
                        final String messagesPackage = AppConfig.getMessagesPack();
                        return MessageProvider.getMessage(messagesPackage, "actions.Ok");
                    }

                    public void actionPerform(Component component) {
                        if (!commitAndCloseButtonExists) {
                            window.commitAndClose();
                        } else {
                            if (window.commit()) {
                                commitActionPerformed = true;
                                window.showNotification(MessageProvider.formatMessage(AppConfig.getMessagesPack(),
                                        "info.EntitySave", ((Instance) window.getItem()).getInstanceName()),
                                        IFrame.NotificationType.HUMANIZED);
                            }
                        }
                    }
                }
        );

        this.window.addAction(
                new AbstractAction(Window.Editor.WINDOW_CLOSE) {
                    @Override
                    public String getCaption() {
                        final String messagesPackage = AppConfig.getMessagesPack();
                        return MessageProvider.getMessage(messagesPackage, "actions.Cancel");
                    }

                    public void actionPerform(Component component) {
                        window.close(commitActionPerformed ? Window.COMMIT_ACTION_ID : getId());
                    }
                }
        );

        return window;
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

                DataService dataservice = ds.getDataService();
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
            item = (Entity) InstanceUtils.copy((Instance) item);
        } else {
            if (!PersistenceHelper.isNew(item)) {
                String useSecurityConstraintsParam = (String) window.getContext().getParams().get("useSecurityConstraints");
                boolean useSecurityConstraints = !("false".equals(useSecurityConstraintsParam));
                final DataService dataservice = ds.getDataService();
                item = dataservice.reload(item, ds.getView(), ds.getMetaClass(), useSecurityConstraints);
            }
        }

        if (PersistenceHelper.isNew(item)
                && !ds.getMetaClass().equals(((Instance) item).getMetaClass()))
        {
            Entity newItem = ds.getDataService().newInstance(ds.getMetaClass());
            InstanceUtils.copy(((Instance) item), ((Instance) newItem));
            item = newItem;
        }

        this.item = item;
        //noinspection unchecked
        ds.setItem(item);
        ((DatasourceImplementation) ds).setModified(false);

        LockService lockService = ServiceLocator.lookup(LockService.NAME);
        LockInfo lockInfo = lockService.lock(ds.getMetaClass().getName(), item.getId().toString());
        if (lockInfo == null) {
            justLocked = true;
        } else if (!(lockInfo instanceof LockNotSupported)) {
            String mp = AppConfig.getInstance().getMessagesPack();
            windowManager.showNotification(
                    MessageProvider.getMessage(mp, "entityLocked.msg"),
                    MessageProvider.formatMessage(mp, "entityLocked.desc",
                            lockInfo.getUser().getLogin(),
                            new SimpleDateFormat(MessageProvider.getMessage(mp, "dateTimeFormat")).format(lockInfo.getSince())
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

    public void setParentDs(Datasource parentDs) {
        Datasource ds = getDatasource();

        if (parentDs == null) {
            ((DatasourceImplementation) ds).setCommitMode(Datasource.CommitMode.DATASTORE);
        } else {
            ((DatasourceImplementation) ds).setCommitMode(Datasource.CommitMode.PARENT);
            ((DatasourceImplementation) ds).setParent(parentDs);
        }
    }

    public void commit() {
        final DsContext context = window.getDsContext();
        if (context != null) {
            context.commit();
            item = getDatasource().getItem();
        } else {
            if (item instanceof Datasource) {
                final Datasource ds = (Datasource) item;
                ds.commit();
            } else {
                DataService service = getDataService();
                item = service.commit(item, null);
            }
        }
    }

    protected DataService getDataService() {
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
            Entity entity = getDatasource().getItem();
            if (entity != null) {
                LockService lockService = ServiceLocator.lookup(LockService.NAME);
                lockService.unlock(getDatasource().getMetaClass().getName(), entity.getId().toString());
            }
        }
    }

}
