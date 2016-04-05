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

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Categorized;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.CollectionPropertyDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.EntityCopyUtils;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Date;

/**
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
    public Window wrapBy(Class<?> wrapperClass) {
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
            handlePreviouslyDeletedCompositionItems(item, parentDs);
        } else if (!PersistenceHelper.isNew(item)) {
            item = dataservice.reload(item, ds.getView(), ds.getMetaClass(), ds.getLoadDynamicAttributes());
        }

        if (item == null) {
            throw new EntityAccessException();
        }

        if (PersistenceHelper.isNew(item)
                && !ds.getMetaClass().equals(item.getMetaClass())) {
            Entity newItem = ds.getDataSupplier().newInstance(ds.getMetaClass());
            metadata.getTools().copy(item, newItem);
            item = newItem;
        }

        if (ds.getLoadDynamicAttributes() && item instanceof BaseGenericIdEntity) {
            DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.NAME);
            if (PersistenceHelper.isNew(item)) {
                dynamicAttributesGuiTools.initDefaultAttributeValues((BaseGenericIdEntity) item, item.getMetaClass());
            }
            if (item instanceof Categorized) {
                dynamicAttributesGuiTools.listenCategoryChanges(ds);
            }
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

    /**
     * This method is required for multi-level composition, when a user deletes records from nested editors, saves them
     * and then reopens. When an editor is opened, we reload the item from the database, hence we need to remove
     * nested items previously deleted by the user.
     */
    protected void handlePreviouslyDeletedCompositionItems(Entity entity, final DatasourceImplementation parentDs) {
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
                item = supplier.commit(item);
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