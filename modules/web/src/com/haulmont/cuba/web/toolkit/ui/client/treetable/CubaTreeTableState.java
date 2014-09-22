/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.treetable;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.treetable.TreeTableState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTreeTableState extends TreeTableState {

    public boolean multiLineCells = false;
    public boolean textSelectionEnabled = false;
    public boolean contextMenuEnabled = true;

    public Connector presentations;
    public Connector contextMenu;
}