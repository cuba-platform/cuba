/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgroup;

import com.vaadin.shared.ui.form.FormState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupState extends FormState {
    {
        primaryStyleName = "cuba-fieldgroup";
    }

    public boolean borderVisible = false;
}