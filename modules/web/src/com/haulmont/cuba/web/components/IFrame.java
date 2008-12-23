/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:51:22
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.itmill.toolkit.ui.OrderedLayout;
import com.itmill.toolkit.ui.ExpandLayout;
import com.haulmont.cuba.gui.components.Component;

public class IFrame extends AbstractContainer implements com.haulmont.cuba.gui.components.IFrame {
    private boolean flexible;

    public IFrame() {
        super(ExpandLayout.ORIENTATION_VERTICAL);
    }

    public boolean isFlexible() {
        return flexible;
    }

    public void setFlexible(boolean flexible) {
        this.flexible = flexible;
        setWidth("100%");
    }
}
