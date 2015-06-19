/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.datefield;

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.datefield.PopupDateFieldState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaDateFieldState extends PopupDateFieldState {
    {
        primaryStyleName = "cuba-datefield";
    }

    @NoLayout
    public String dateMask = "";
}