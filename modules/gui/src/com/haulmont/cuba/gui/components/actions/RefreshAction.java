/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Map;

/**
 * Standard list action to refresh a list of entities.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 *
 * @author krivopustov
 * @version $Id$
 */
public class RefreshAction extends BaseAction {

    public static final String ACTION_ID = ListActionType.REFRESH.getId();

    protected ListComponent owner;

    protected Map<String, Object> refreshParams;

    /**
     * The simplest constructor. The action has default name.
     * @param target    component containing this action
     */
    public RefreshAction(ListComponent target) {
        this(target, ACTION_ID);
    }

    /**
     * Constructor that allows to specify action's name.
     * @param target        component containing this action
     * @param id            action's identifier
     */
    public RefreshAction(ListComponent target, String id) {
        super(id);
        this.owner = target;
        this.caption = messages.getMainMessage("actions.Refresh");
        this.icon = "icons/refresh.png";
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     * @param component component invoking action
     */
    @Override
    public void actionPerform(Component component) {
        CollectionDatasource datasource = owner.getDatasource();

        Map<String, Object> params = getRefreshParams();
        if (params != null) {
            datasource.refresh(params);
        } else {
            datasource.refresh();
        }
    }

    /**
     * @return  parameters for {@link CollectionDatasource#refresh(java.util.Map)} method
     */
    public Map<String, Object> getRefreshParams() {
        return refreshParams;
    }

    /**
     * @param refreshParams parameters for {@link CollectionDatasource#refresh(java.util.Map)} method
     */
    public void setRefreshParams(Map<String, Object> refreshParams) {
        this.refreshParams = refreshParams;
    }
}
