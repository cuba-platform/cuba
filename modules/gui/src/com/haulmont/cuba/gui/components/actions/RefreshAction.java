/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.03.11 18:20
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Map;

/**
 * Standard list action to refresh a list of entities.
 * <p>
 *      Action's behaviour can be customized by providing arguments to constructor, as well as overriding the following
 *      methods:
 *      <ul>
 *          <li>{@link #getCaption()}</li>
 *          <li>{@link #isEnabled()}</li>
 *          <li>{@link #getRefreshParams()}</li>
 *      </ul>
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RefreshAction extends AbstractAction {

    private static final long serialVersionUID = -5377483521909360667L;

    public static final String ACTION_ID = ListActionType.REFRESH.getId();

    protected ListComponent owner;

    /**
     * The simplest constructor. The action has default name.
     * @param owner    component containing this action
     */
    public RefreshAction(ListComponent owner) {
        this(owner, ACTION_ID);
    }

    /**
     * Constructor that allows to specify action's name.
     * @param owner        component containing this action
     * @param id            action's identifier
     */
    public RefreshAction(ListComponent owner, String id) {
        super(id);
        this.owner = owner;
    }

    /**
     * Returns the action's caption. Override to provide a specific caption.
     * @return  localized caption
     */
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Refresh");
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     * @param component component invoking action
     */
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
     * Provides parameters for {@link CollectionDatasource#refresh(java.util.Map)} method. Override to provide specific
     * value.
     * @return  map of parameters
     */
    protected Map<String, Object> getRefreshParams() {
        return null;
    }
}
