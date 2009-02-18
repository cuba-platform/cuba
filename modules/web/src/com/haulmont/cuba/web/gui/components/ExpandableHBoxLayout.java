/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 17:20:39
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.itmill.toolkit.ui.ExpandLayout;

public class ExpandableHBoxLayout extends AbstractExpandableContainer implements com.haulmont.cuba.gui.components.OrderedLayout {
    public ExpandableHBoxLayout() {
        super(ExpandLayout.ORIENTATION_HORIZONTAL);
    }
}