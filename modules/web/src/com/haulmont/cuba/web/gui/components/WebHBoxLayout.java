/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 17:20:39
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalActionsLayout;

public class WebHBoxLayout extends WebAbstractBox implements BoxLayout {

    public WebHBoxLayout() {
        component = new CubaHorizontalActionsLayout();
    }
}