/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import java.io.Serializable;

/**
 * @author artamonov
 * @version $Id$
 */
public class ClientAction implements Serializable {

    private static final long serialVersionUID = 188564361846926204L;

    private String caption;

    private String actionId;

    public ClientAction(String caption) {
        this.caption = caption;
    }

    public ClientAction() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}