/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.table;

import com.google.gwt.dom.client.Element;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.table.TableConnector;

/**
 * @author gorelov
 * @version $Id$
 */
public class CubaTableShortcutActionHandler extends ShortcutActionHandler {

    protected TableConnector target;

    public CubaTableShortcutActionHandler(String pid, ApplicationConnection client, TableConnector target) {
        super(pid, client);
        this.target = target;
    }

    @Override
    protected ComponentConnector getTargetConnector(ComponentConnector target, Element et) {
        return this.target;
    }
}