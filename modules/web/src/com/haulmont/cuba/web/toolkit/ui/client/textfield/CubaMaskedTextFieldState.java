/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.vaadin.shared.ui.textfield.AbstractTextFieldState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMaskedTextFieldState extends AbstractTextFieldState {
    {
        primaryStyleName = "cuba-maskedfield";
    }

    public String mask = "";

    public boolean maskedMode = false;
}