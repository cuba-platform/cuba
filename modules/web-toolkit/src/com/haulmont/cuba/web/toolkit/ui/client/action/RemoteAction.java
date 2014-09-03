/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.action;

import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.ClientAction;
import com.vaadin.client.ui.Action;
import com.vaadin.client.ui.ActionOwner;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class RemoteAction extends Action {

    protected String actionId;

    protected RemoteAction(ClientAction clientAction, ActionOwner owner) {
        super(owner);

        // copy properties from client action
        this.caption = clientAction.getCaption();
        this.actionId = clientAction.getActionId();
    }
}