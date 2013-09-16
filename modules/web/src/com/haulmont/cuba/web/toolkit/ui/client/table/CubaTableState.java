/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.table;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.table.TableState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTableState extends TableState {

    public boolean textSelectionEnabled = false;
    public boolean allowPopupMenu = true;

    public Connector presentations;
}