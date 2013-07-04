/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalActionsLayout;

/**
 * @author abramov
 * @version $Id$
 */
public class WebHBoxLayout extends WebAbstractBox {

    public WebHBoxLayout() {
        component = new CubaHorizontalActionsLayout();
    }
}