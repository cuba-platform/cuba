/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

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

        this.caption = clientAction.getCaption();
        this.actionId = clientAction.getActionId();
        // copy properties from client action
    }
}