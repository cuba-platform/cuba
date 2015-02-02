/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMaskedTextFieldState extends CubaTextFieldState {
    {
        primaryStyleName = "cuba-maskedfield";
    }

    public String mask = "";

    public boolean maskedMode = false;
}