/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.vaadin.shared.annotations.NoLayout;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMaskedTextFieldState extends CubaTextFieldState {
    {
        primaryStyleName = "cuba-maskedfield";
    }

    @NoLayout
    public String mask = "";

    @NoLayout
    public boolean maskedMode = false;

    @NoLayout
    public boolean sendNullRepresentation = true;
}