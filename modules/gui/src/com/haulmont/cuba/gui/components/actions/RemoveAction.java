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
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Frame.MessageType;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import org.springframework.context.annotation.Scope;

import java.util.HashSet;
import java.util.Set;

/**
 * Standard list action to remove an entity instance.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor, setting properties, or overriding
 * method {@link #afterRemove(java.util.Set)} )}
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 * &lt;bean id="cuba_RemoveAction" class="com.company.sample.gui.MyRemoveAction" scope="prototype"/&gt;
 * </pre>
 * Also, use {@code create()} static methods instead of constructors when creating the action programmatically.
 */
@org.springframework.stereotype.Component("cuba_RemoveAction")
@Scope("prototype")
public class RemoveAction extends ItemTrackingAction implements Action.HasBeforeActionPerformedHandler {

    public static final String ACTION_ID = ListActionType.REMOVE.getId();

    protected boolean autocommit;

    protected boolean confirm = true;
    protected String confirmationMessage;
    protected String confirmationTitle;

    protected AfterRemoveHandler afterRemoveHandler;

    protected BeforeActionPerformedHandler beforeActionPerformedHandler;

    public interface AfterRemoveHandler {
        /**
         * @param removedItems  set of removed instances
         */
        void handle(Set removedItems);
    }

    /**
     * Creates an action with default id. Autocommit is set to true.
     * @param target    component containing this action
     */
    public static RemoveAction create(ListComponent target) {
        return AppBeans.getPrototype("cuba_RemoveAction", target);
    }

    /**
     * Creates an action with default id.
     * @param target    component containing this action
     * @param autocommit    whether to commit datasource immediately
     */
    public static RemoveAction create(ListComponent target, boolean autocommit) {
        return AppBeans.getPrototype("cuba_RemoveAction", target, autocommit);
    }

    /**
     * Creates an action with the given id.
     * @param target    component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param id            action's identifier
     */
    public static RemoveAction create(ListComponent target, boolean autocommit, String id) {
        return AppBeans.getPrototype("cuba_RemoveAction", target, autocommit, id);
    }

    /**
     * The simplest constructor. The action has default name and autocommit=true.
     * @param target    component containing this action
     */
    public RemoveAction(ListComponent target) {
        this(target, true, ACTION_ID);
    }

    /**
     * Constructor that allows to specify autocommit value. The action has default name.
     * @param target        component containing this action
     * @param autocommit    whether to commit datasource immediately
     */
    public RemoveAction(ListComponent target, boolean autocommit) {
        this(target, autocommit, ACTION_ID);
    }

    /**
     * Constructor that allows to specify action's identifier and autocommit value.
     * @param target        component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param id            action's identifier
     */
    public RemoveAction(ListComponent target, boolean autocommit, String id) {
        super(id);

        this.target = target;
        this.autocommit = autocommit;
        this.caption = messages.getMainMessage("actions.Remove");

        this.icon = AppBeans.get(Icons.class).get(CubaIcon.REMOVE_ACTION);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig config = configuration.getConfig(ClientConfig.class);
        setShortcut(config.getTableRemoveShortcut());
    }

    /**
     * Check permissions for Action
     */
    @Override
    protected boolean isPermitted() {
        if (target == null || target.getDatasource() == null) {
            return false;
        }

        if (!checkRemovePermission()) {
            return false;
        }

        return super.isPermitted();
    }

    protected boolean checkRemovePermission() {
        CollectionDatasource ds = target.getDatasource();
        if (ds instanceof PropertyDatasource) {
            PropertyDatasource propertyDatasource = (PropertyDatasource) ds;

            MetaClass parentMetaClass = propertyDatasource.getMaster().getMetaClass();
            MetaProperty metaProperty = propertyDatasource.getProperty();

            boolean modifyPermitted = security.isEntityAttrPermitted(parentMetaClass, metaProperty.getName(),
                    EntityAttrAccess.MODIFY);
            if (!modifyPermitted) {
                return false;
            }

            if (metaProperty.getRange().getCardinality() != Range.Cardinality.MANY_TO_MANY) {
                boolean deletePermitted = security.isEntityOpPermitted(ds.getMetaClass(), EntityOp.DELETE);
                if (!deletePermitted) {
                    return false;
                }
            }
        } else {
            boolean entityOpPermitted = security.isEntityOpPermitted(ds.getMetaClass(), EntityOp.DELETE);
            if (!entityOpPermitted) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is invoked by the action owner component.
     *
     * @param component component invoking the action
     */
    @Override
    public void actionPerform(Component component) {
        if (!isEnabled()) {
            return;
        }

        if (beforeActionPerformedHandler != null) {
            if (!beforeActionPerformedHandler.beforeActionPerformed())
                return;
        }

        @SuppressWarnings("unchecked")
        Set<Entity> selected = target.getSelected();
        if (!selected.isEmpty()) {
            if (confirm) {
                confirmAndRemove(selected);
            } else {
                remove(selected);
            }
        }
    }

    protected void confirmAndRemove(Set<Entity> selected) {
        target.getFrame().showOptionDialog(
                getConfirmationTitle(),
                getConfirmationMessage(),
                MessageType.CONFIRMATION,
                new Action[]{
                        new DialogAction(Type.OK, Status.PRIMARY).withHandler(event -> {
                            try {
                                remove(selected);
                            } finally {
                                target.requestFocus();
                                Set<Entity> filtered = new HashSet<>(selected);
                                filtered.retainAll(target.getDatasource().getItems());
                                //noinspection unchecked
                                target.setSelected(filtered);
                            }
                        }),
                        new DialogAction(Type.CANCEL).withHandler(event -> {
                            // move focus to owner
                            target.requestFocus();
                        })
                }
        );
    }

    protected void remove(Set<Entity> selected) {
        doRemove(selected, autocommit);

        // move focus to owner
        target.requestFocus();

        afterRemove(selected);
        if (afterRemoveHandler != null) {
            afterRemoveHandler.handle(selected);
        }
    }

    /**
     * @return  whether to commit datasource immediately after deletion
     */
    public boolean isAutocommit() {
        return autocommit;
    }

    /**
     * @param autocommit    whether to commit datasource immediately after deletion
     */
    public void setAutocommit(boolean autocommit) {
        this.autocommit = autocommit;
    }

    /**
     * @return  whether to show the confirmation dialog to user
     */
    public boolean isConfirm() {
        return confirm;
    }

    /**
     * @param confirm   whether to show the confirmation dialog to user
     */
    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    /**
     * Provides confirmation dialog message.
     * @return  localized message
     */
    public String getConfirmationMessage() {
        if (confirmationMessage != null)
            return confirmationMessage;
        else
            return messages.getMainMessage("dialogs.Confirmation.Remove");
    }

    /**
     * @param confirmationMessage   confirmation dialog message
     */
    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    /**
     * Provides confirmation dialog title.
     * @return  localized title
     */
    public String getConfirmationTitle() {
        if (confirmationTitle != null)
            return confirmationTitle;
        else
            return messages.getMainMessage("dialogs.Confirmation");
    }

    /**
     * @param confirmationTitle confirmation dialog title.
     */
    public void setConfirmationTitle(String confirmationTitle) {
        this.confirmationTitle = confirmationTitle;
    }

    protected void doRemove(Set<Entity> selected, boolean autocommit) {
        CollectionDatasource datasource = target.getDatasource();
        for (Entity item : selected) {
            datasource.removeItem(item);
        }

        if (autocommit && (datasource.getCommitMode() != Datasource.CommitMode.PARENT)) {
            try {
                datasource.commit();
            } catch (RuntimeException e) {
                datasource.refresh();
                throw e;
            }
        }
    }

    /**
     * Hook invoked after remove.
     * @param selected  set of removed instances
     */
    protected void afterRemove(Set selected) {
    }

    /**
     * @param afterRemoveHandler handler that is invoked after remove
     */
    public void setAfterRemoveHandler(AfterRemoveHandler afterRemoveHandler) {
        this.afterRemoveHandler = afterRemoveHandler;
    }

    @Override
    public BeforeActionPerformedHandler getBeforeActionPerformedHandler() {
        return beforeActionPerformedHandler;
    }

    @Override
    public void setBeforeActionPerformedHandler(BeforeActionPerformedHandler handler) {
        beforeActionPerformedHandler = handler;
    }
}