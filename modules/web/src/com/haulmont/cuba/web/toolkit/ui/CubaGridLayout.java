/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.GridLayout;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaGridLayout extends GridLayout {

    public CubaGridLayout() {
        setHideEmptyRowsAndColumns(true);
    }
}