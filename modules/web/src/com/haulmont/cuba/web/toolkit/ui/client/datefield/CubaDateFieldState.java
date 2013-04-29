/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.datefield;

import com.vaadin.shared.ui.datefield.PopupDateFieldState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaDateFieldState extends PopupDateFieldState {
    {
        primaryStyleName = "cuba-datefield";
    }
    public String dateMask = "";
}