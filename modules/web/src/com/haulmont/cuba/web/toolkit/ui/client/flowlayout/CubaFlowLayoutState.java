/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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