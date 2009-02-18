/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 21.01.2009 18:54:56
 * $Id$
 */
package com.haulmont.cuba.web.app.ui;

import com.haulmont.cuba.web.gui.Window;
import com.itmill.toolkit.ui.ComponentContainer;

public class TableDemoScreen  extends Window {
    protected void init() {
        ((ComponentContainer) component).addComponent(new TableExample());
    }
}
