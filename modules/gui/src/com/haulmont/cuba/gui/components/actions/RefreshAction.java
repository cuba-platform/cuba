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
import com.haulmont.cuba.gui.components.List;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Map;

public class RefreshAction extends AbstractAction {

    private static final long serialVersionUID = -5377483521909360667L;

    public static final String ACTION_ID = "refresh";

    protected List owner;

    public RefreshAction(List owner) {
        this(owner, ACTION_ID);
    }

    public RefreshAction(List owner, String id) {
        super(id);
        this.owner = owner;
    }

    public String getCaption() {
        final String messagesPackage = AppConfig.getInstance().getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Refresh");
    }

    public void actionPerform(Component component) {
        CollectionDatasource datasource = owner.getDatasource();

        Map<String, Object> params = getRefreshParams();
        if (params != null) {
            datasource.refresh(params);
        } else {
            datasource.refresh();
        }
    }

    protected Map<String, Object> getRefreshParams() {
        return null;
    }
}
