/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.flowlayout;

import com.vaadin.shared.ui.csslayout.CssLayoutState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFlowLayoutState extends CssLayoutState {
    {
        primaryStyleName = "cuba-flowlayout";
    }

    public int marginsBitmask = 0;

    public boolean spacing = false;
}