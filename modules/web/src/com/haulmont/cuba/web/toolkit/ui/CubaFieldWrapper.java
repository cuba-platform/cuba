/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * Simple wrapper for Cuba components which does not contain Vaadin Field
 *
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldWrapper extends CustomField {

    protected com.vaadin.ui.Component composition;

    public CubaFieldWrapper(Component composition) {
        this.composition = composition;
        this.setCaption(" "); // use space in caption for proper layout
    }

    @Override
    protected Component initContent() {
        return composition;
    }

    @Override
    public Class getType() {
        return Object.class;
    }
}