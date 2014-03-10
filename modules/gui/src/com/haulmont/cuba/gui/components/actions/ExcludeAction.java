/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.security.entity.EntityAttrAccess;

import java.util.Set;

/**
 * The <code>RemoveAction</code> variant that excludes instances from the list, but doesn't delete them from DB.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ExcludeAction extends RemoveAction {

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
        this.caption = messages.getMainMessage("actions.Exclude");

        refreshState();
    }

    @Override
    protected boolean isPermitted() {
        boolean removePermitted = true;

        CollectionDatasource ds = owner.getDatasource();
        if (ds instanceof PropertyDatasource) {
            MetaProperty metaProperty = ((PropertyDatasource) ds).getProperty();
            removePermitted = userSession.isEntityAttrPermitted(
                    metaProperty.getDomain(), metaProperty.getName(), EntityAttrAccess.MODIFY);
        }

        return removePermitted;
    }

    @Override
    public void actionPerform(Component component) {
        if (!isEnabled())
            return;
        Set selected = owner.getSelected();
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
        CollectionDatasource ds = owner.getDatasource();
        for (Object item : selected) {
            ds.excludeItem((Entity) item);
        }

        if (autocommit && (ds.getCommitMode() != Datasource.CommitMode.PARENT)) {
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