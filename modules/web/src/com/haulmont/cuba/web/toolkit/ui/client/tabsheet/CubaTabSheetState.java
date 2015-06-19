/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.tabsheet.TabsheetState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTabSheetState extends TabsheetState {

    private static final long serialVersionUID = 4132538424243246049L;

    @NoLayout
    public boolean hasActionsHandlers = false;
}