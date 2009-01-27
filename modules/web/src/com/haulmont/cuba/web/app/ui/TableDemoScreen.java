/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 21.01.2009 18:54:56
 * $Id$
 */
package com.haulmont.cuba.web.app.ui;

import com.haulmont.cuba.web.ui.Window;

public class TableDemoScreen  extends Window {
    protected void init() {
        layout.addComponent(new TableExample());
    }
}
