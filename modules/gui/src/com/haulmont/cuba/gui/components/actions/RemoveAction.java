/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Set;

/**
 * Standard list action to remove an entity instance.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor, setting properties, or overriding
 * method {@link #afterRemove(java.util.Set)} )}
 *
 * @author krivopustov
 * @version $Id$
 */
public class RemoveAction extends ItemTrackingAction {

    public static final String ACTION_ID = ListActionType.REMOVE.getId();

    protected boolean autocommit;

    protected String confirmationMessage;
    protected String confirmationTitle;

    protected Security security = AppBeans.get(Security.NAME);

    protected AfterRemoveHandler afterRemoveHandler;

    public interface AfterRemoveHandler {
        /**
         * @param removedItems  set of removed instances
         */
        void handle(Set removedItems);
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

        ThemeConstantsManager thCM = AppBeans.get(ThemeConstantsManager.NAME);
        this.icon = thCM.getThemeValue("actions.Remove.icon");

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

        CollectionDatasource ds = target.getDatasource();
        if (ds instanceof PropertyDatasource) {
            PropertyDatasource propertyDatasource = (PropertyDatasource) ds;

            MetaClass parentMetaClass = propertyDatasource.getMaster().getMetaClass();
            MetaProperty metaProperty = propertyDatasource.getProperty();

            boolean removePermitted = security.isEntityAttrPermitted(parentMetaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);

            if (metaProperty.getRange().getCardinality() != Range.Cardinality.MANY_TO_MANY) {
                removePermitted = removePermitted && security.isEntityOpPermitted(ds.getMetaClass(), EntityOp.DELETE);
            }
            return removePermitted;
        } else {
            return security.isEntityOpPermitted(ds.getMetaClass(), EntityOp.DELETE);
        }
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     *
     * @param component component invoking action
     */
    @Override
    public void actionPerform(Component component) {
        if (!isEnabled()) {
            return;
        }

        Set selected = target.getSelected();
        if (!selected.isEmpty()) {
            confirmAndRemove(selected);
        }
    }

    protected void confirmAndRemove(final Set selected) {
        final String messagesPackage = AppConfig.getMessagesPack();
        target.getFrame().showOptionDialog(
                getConfirmationTitle(messagesPackage),
                getConfirmationMessage(messagesPackage),
                Frame.MessageType.CONFIRMATION,
                new Action[]{
                        new DialogAction(Type.OK) {
                            @Override
                            public void actionPerform(Component component) {
                                doRemove(selected, autocommit);

                                // move focus to owner
                                target.requestFocus();

                                afterRemove(selected);
                                if (afterRemoveHandler != null) {
                                    afterRemoveHandler.handle(selected);
                                }
                            }
                        },
                        new DialogAction(Type.CANCEL, Status.PRIMARY) {
                            @Override
                            public void actionPerform(Component component) {
                                // move focus to owner
                                target.requestFocus();
                            }
                        }
                }
        );
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
     * Provides confirmation dialog message.
     * @param   messagesPackage   message pack containing the message
     * @return  localized message
     */
    public String getConfirmationMessage(String messagesPackage) {
        if (confirmationMessage != null)
            return confirmationMessage;
        else
            return messages.getMessage(messagesPackage, "dialogs.Confirmation.Remove");
    }

    /**
     * @param confirmationMessage   confirmation dialog message
     */
    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    /**
     * Provides confirmation dialog title.
     * @param   messagesPackage   message pack containing the title
     * @return  localized title
     */
    public String getConfirmationTitle(String messagesPackage) {
        if (confirmationTitle != null)
            return confirmationTitle;
        else
            return messages.getMessage(messagesPackage, "dialogs.Confirmation");
    }

    /**
     * @param confirmationTitle confirmation dialog title.
     */
    public void setConfirmationTitle(String confirmationTitle) {
        this.confirmationTitle = confirmationTitle;
    }

    protected void doRemove(Set selected, boolean autocommit) {
        CollectionDatasource datasource = target.getDatasource();
        for (Object item : selected) {
            datasource.removeItem((Entity) item);
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
}