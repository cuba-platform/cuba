/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.table;

import com.vaadin.shared.Connector;
import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.table.TableState;

import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTableState extends TableState {

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

    @NoLayout
    public boolean customPopupAutoClose = false;

    public String[] clickableColumnKeys;

    public String[] sortDisallowedColumnKeys;

    @NoLayout
    public Map<String, String> columnDescriptions;
}