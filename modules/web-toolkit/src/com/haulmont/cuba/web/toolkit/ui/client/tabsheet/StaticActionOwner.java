/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ui.Action;
import com.vaadin.client.ui.ActionOwner;

/**
 * @author artamonov
 * @version $Id$
 */
public class StaticActionOwner implements ActionOwner {

    private Action[] actions;

    private ApplicationConnection connection;

    private String paintableId;

    public StaticActionOwner(ApplicationConnection connection, String paintableId) {
        this.connection = connection;
        this.paintableId = paintableId;
    }

    @Override
    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    @Override
    public ApplicationConnection getClient() {
        return connection;
    }

    @Override
    public String getPaintableId() {
        return paintableId;
    }
}