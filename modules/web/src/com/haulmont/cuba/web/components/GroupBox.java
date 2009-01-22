/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 17:52:31
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.OrderedLayout;
import com.itmill.toolkit.ui.Panel;
import com.itmill.toolkit.ui.ExpandLayout;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class GroupBox extends AbstractPanel implements Component.HasCaption {

    public GroupBox() {
        setLayout(new ExpandLayout(OrderedLayout.ORIENTATION_VERTICAL));
    }
}
