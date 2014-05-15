/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout;

import com.vaadin.shared.ui.gridlayout.GridLayoutState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupLayoutState extends GridLayoutState {
    {
        primaryStyleName = "cuba-fieldgrouplayout";
    }

    public boolean useInlineCaption = true;
    public int[] columnFieldCaptionWidth = null;
    public int fieldCaptionWidth = -1;
}