/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tree;

import com.vaadin.shared.Connector;
import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.tree.TreeState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTreeState extends TreeState {

    @NoLayout
    public boolean doubleClickMode = false;

    @NoLayout
    public boolean nodeCaptionsAsHtml = false;

    @NoLayout
    public Connector contextMenu;
}