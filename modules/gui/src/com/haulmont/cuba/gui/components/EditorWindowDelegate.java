/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionPropertyDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.EntityCopyUtils;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EditorWindowDelegate extends WindowDelegate {

    protected Entity item;
    protected boolean justLocked;
    protected boolean commitActionPerformed;
    protected boolean commitAndCloseButtonExists;
    protected boolean readOnly;

    protected Metadata metadata = AppBeans.get(Metadata.NAME);
    protected Messages messages = AppBeans.get(Messages.NAME);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
    protected LockService lockService = AppBeans.get(LockService.NAME);
    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    public EditorWindowDelegate(Window window) {
        super(window);
    }

    @Override
    public Window wrapBy(Class<Window> wrapperClass) {
        final Window.Editor editor = (Window.Editor) super.wrapBy(wrapperClass);

        final Component commitAndCloseButton = ComponentsHelper.findComponent(editor,
                Window.Editor.WINDOW_COMMIT_AND_CLOSE);
        String commitShortcut = configuration.getConfig(ClientConfig.class).getCommitShortcut();
        if (commitAndCloseButton != null) {
            commitAndCloseButtonExists = true;

            this.window.addAction(
                    new AbstractAction(Window.Editor.WINDOW_COMMIT_AND_CLOSE, commitShortcut) {
                        @Override
                        public String getCaption() {
                            return messages.getMainMessage("actions.OkClose");
                        }

                        @Override
                        public void actionPerform(Component component) {
                            editor.commitAndClose();
                        }
                    }
            );
        }

        AbstractAction commitAction = new AbstractAction(Window.Editor.WINDOW_COMMIT) {
            @Override
            public String getCaption() {
                return messages.getMainMessage(commitAndCloseButtonExists ? "actions.Save" : "actions.Ok");
            }

            @Override
            public void actionPerform(Component component) {
                if (!commitAndCloseButtonExists) {
                    editor.commitAndClose();
                } else {
                    if (editor.commit()) {
                        commitActionPerformed = true;
                    }
                }
            }
        };
        if (!commitAndCloseButtonExists) {
            commitAction.setShortcut(commitShortcut);
        }
        this.window.addAction(commitAction);


        this.window.addAction(
                new AbstractAction(Window.Editor.WINDOW_CLOSE) {
                    @Override
                    public String getCaption() {
                        return messages.getMainMessage("actions.Cancel");
                    }

                    @Override
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

    @SuppressWarnings("unchecked")
    public void setItem(Entity item) {
        Datasource ds = getDatasource();
        DataSupplier dataservice = ds.getDataSupplier();

        DatasourceImplementation parentDs = (DatasourceImplementation) ((DatasourceImplementation) ds).getParent();

        if (parentDs != null) {
            if (!PersistenceHelper.isNew(item)
                    && !parentDs.getItemsToCreate().contains(item) && !parentDs.getItemsToUpdate().contains(item)
                    && parentDs instanceof CollectionDatasource
                    && ((CollectionDatasource) parentDs).containsItem(item)) {
                item = dataservice.reload(item, ds.getView(), ds.getMetaClass());
                if (parentDs instanceof CollectionPropertyDatasourceImpl) {
                    ((CollectionPropertyDatasourceImpl) parentDs).replaceItem(item);
                } else {
                    ((CollectionDatasource) parentDs).updateItem(item);
                }
            }
            item = EntityCopyUtils.copyCompositions(item);

        } else if (!PersistenceHelper.isNew(item)) {
            boolean useSecConstraints = !WindowParams.DISABLE_SECURITY_CONSTRAINTS.getBool(window.getContext());
            item = dataservice.reload(item, ds.getView(), ds.getMetaClass(), useSecConstraints, ds.getLoadDynamicAttributes());
        }

        if (item == null) {
            throw new EntityAccessException();
        }

        if (PersistenceHelper.isNew(item)
                && !ds.getMetaClass().equals(item.getMetaClass())) {
            Entity newItem = ds.getDataSupplier().newInstance(ds.getMetaClass());
            InstanceUtils.copy(item, newItem);
            item = newItem;
        }

        if (PersistenceHelper.isNew(item) && ds.getLoadDynamicAttributes() && item instanceof BaseGenericIdEntity) {
            DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.NAME);
            dynamicAttributesGuiTools.initDefaultAttributeValues((BaseGenericIdEntity) item);
        }

        this.item = item;
        ds.setItem(item);
        ((DatasourceImplementation) ds).setModified(false);

        Security security = AppBeans.get(Security.NAME);
        if (!PersistenceHelper.isNew(item) && security.isEntityOpPermitted(ds.getMetaClass(), EntityOp.UPDATE)) {
            readOnly = false;
            LockInfo lockInfo = lockService.lock(getMetaClassForLocking(ds).getName(), item.getId().toString());
            if (lockInfo == null) {
                justLocked = true;
            } else if (!(lockInfo instanceof LockNotSupported)) {
                window.getWindowManager().showNotification(
                        messages.getMainMessage("entityLocked.msg"),
                        String.format(messages.getMainMessage("entityLocked.desc"),
                                lockInfo.getUser().getLogin(),
                                Datatypes.getNN(Date.class).format(lockInfo.getSince(), userSessionSource.getLocale())
                        ),
                        Frame.NotificationType.HUMANIZED
                );
                Action action = window.getAction(Window.Editor.WINDOW_COMMIT);
                if (action != null)
                    action.setEnabled(false);
                action = window.getAction(Window.Editor.WINDOW_COMMIT_AND_CLOSE);
                if (action != null)
                    action.setEnabled(false);
                readOnly = true;
            }
        }
    }

    public void setParentDs(Datasource parentDs) {
        Datasource ds = getDatasource();
        ((DatasourceImplementation) ds).setParent(parentDs);
    }

    @Nullable
    public Datasource getParentDs() {
        Datasource ds = getDatasource();
        return ((DatasourceImplementation) ds).getParent();
    }

    public boolean isModified() {
        if (readOnly)
            return false;
        else if (wrapper instanceof Window.Committable)
            return ((Window.Committable) wrapper).isModified();
        else
            return window.getDsContext() != null && window.getDsContext().isModified();
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
            return context.getDataSupplier();
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