/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.placeholder;

import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaPlaceHolderWidget extends SimplePanel {

    public static final String CLASSNAME = "cuba-placeholder";

    public CubaPlaceHolderWidget() {
        setStyleName(CLASSNAME);
    }
}