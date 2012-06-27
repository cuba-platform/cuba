/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.03.11 19:05
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.global.UserSession;

import java.util.Set;

/**
 * The <code>RemoveAction</code> variant that excludes instances from the list, but doesn't delete them from DB.
 * Makes sense for removing items from non-aggregating OneToMany collections.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ExcludeAction extends RemoveAction {

    private static final long serialVersionUID = 62824552031621792L;

    public static final String ACTION_ID = ListActionType.EXCLUDE.getId();

    protected boolean confirm;

    /**
     * The simplest constructor. Autocommit and Confirm properties are set to false, the action has default name.
     * @param owner     component containing this action
     */
    public ExcludeAction(ListComponent owner) {
        this(owner, false, false, ACTION_ID);
    }

    /**
     * Constructor that allows to specify autocommit and confirm value. The action has default name.
     * @param owner         component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param confirm       whether to show the confirmation dialog to user
     */
    public ExcludeAction(ListComponent owner, boolean autocommit, boolean confirm) {
        this(owner, autocommit, confirm, ACTION_ID);
    }

    /**
     * Constructor that allows to specify all parameters.
     * @param owner         component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param confirm       whether to show the confirmation dialog to user
     * @param id            action's name
     */
    public ExcludeAction(ListComponent owner, boolean autocommit, boolean confirm, String id) {
        super(owner, autocommit, id);
        this.confirm = confirm;
        this.caption = MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Exclude");
    }

    /**
     * Whether the action is currently enabled. Override to provide specific behaviour.
     * @return  true if enabled
     */
    public boolean isEnabled() {
        if (!enabled)
            return false;

        UserSession userSession = UserSessionProvider.getUserSession();
        if (owner.getDatasource() instanceof PropertyDatasource) {
            MetaProperty metaProperty = ((PropertyDatasource) owner.getDatasource()).getProperty();
            return userSession.isEntityAttrPermitted(
                    metaProperty.getDomain(), metaProperty.getName(), EntityAttrAccess.MODIFY);
        }
        return true;
    }

    @Override
    public void actionPerform(Component component) {
        if(!isEnabled()) return;
        final Set selected = owner.getSelected();
        if (!selected.isEmpty()) {
            if (confirm) {
                confirmAndRemove(selected);
            } else {
                doRemove(selected, autocommit);
                afterRemove(selected);
            }
        }
    }

    @Override
    protected void doRemove(Set selected, boolean autocommit) {
        @SuppressWarnings({"unchecked"})
        final CollectionDatasource ds = owner.getDatasource();
        for (Object item : selected) {
            ds.excludeItem((Entity) item);
        }

        if (this.autocommit) {
            try {
                ds.commit();
            } catch (RuntimeException e) {
                ds.refresh();
                throw e;
            }
        }
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
}
