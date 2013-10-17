/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 23.09.2010 18:29:26
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.vaadin.terminal.gwt.client.ui.VMenuBar;

/**
 * VerticalMenuBar
 * <br/>
 * [Compatible with Vaadin 6.6]
 */
public class VerticalMenuBar extends VMenuBar {
    public VerticalMenuBar() {
        super(true, null);
        setStyleName(CLASSNAME);
    }
}
