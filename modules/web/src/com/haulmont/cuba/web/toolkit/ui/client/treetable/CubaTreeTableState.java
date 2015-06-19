/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.treetable;

import com.vaadin.shared.Connector;
import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.treetable.TreeTableState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTreeTableState extends TreeTableState {

    @NoLayout
    public boolean multiLineCells = false;

    @NoLayout
    public boolean textSelectionEnabled = false;

    @NoLayout
    public boolean contextMenuEnabled = true;

    @NoLayout
    public Connector presentations;

    @NoLayout
    public Connector contextMenu;

    @NoLayout
    public Connector customPopup;

    public String[] clickableColumnKeys;
}