/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.treetable;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.treetable.TreeTableState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTreeTableState extends TreeTableState {

    public boolean textSelectionEnabled = false;
    public boolean allowPopupMenu = true;

    public Connector presentations;
}