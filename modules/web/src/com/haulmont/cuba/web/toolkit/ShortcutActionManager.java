/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit;

import com.vaadin.event.ConnectorActionManager;
import com.vaadin.server.ClientConnector;

/**
 * Keeps track of the ShortcutListeners added to component, and manages the painting and handling as well. <br/>
 * Paints actions with ShortcutListener to separate 'shortcuts' json tag.
 *
 * @author artamonov
 * @version $Id$
 */
public class ShortcutActionManager extends ConnectorActionManager {

    public ShortcutActionManager(ClientConnector connector) {
        super(connector);
    }

    @Override
    protected String getActionsJsonTag() {
        return "shortcuts";
    }

    @Override
    protected boolean isNeedToAddActionVariable() {
        return false;
    }
}