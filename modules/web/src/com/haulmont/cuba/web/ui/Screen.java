/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 19:02:39
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.haulmont.cuba.web.config.ScreenAction;
import com.itmill.toolkit.ui.ExpandLayout;
import com.itmill.toolkit.ui.Label;

public class Screen extends ExpandLayout
{
    protected ScreenContext screenContext;

    public Screen() {
        super(ExpandLayout.ORIENTATION_VERTICAL);
        setMargin(true);
        setSpacing(true);
    }

    public void init(ScreenContext context) {
        screenContext = context;
    }

    public boolean onClose() {
        return true;
    }
}
