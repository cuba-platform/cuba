/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.toolkit.ui.TextField;
import com.vaadin.ui.TextArea;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebTextArea
        extends
            WebAbstractTextArea<TextField>
        implements
            com.haulmont.cuba.gui.components.TextArea, Component.Wrapper {

    @Override
    protected TextField createTextFieldImpl() {
        return new TextField();
    }
}