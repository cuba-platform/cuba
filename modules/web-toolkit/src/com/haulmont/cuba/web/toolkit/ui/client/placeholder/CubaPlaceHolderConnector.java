/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.placeholder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaPlaceHolder;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaPlaceHolder.class)
public class CubaPlaceHolderConnector extends AbstractComponentConnector {

    @Override
    public CubaPlaceHolderWidget getWidget() {
        return (CubaPlaceHolderWidget) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(CubaPlaceHolderWidget.class);
    }
}