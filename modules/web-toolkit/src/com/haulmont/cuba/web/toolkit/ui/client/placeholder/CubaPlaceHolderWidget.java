/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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