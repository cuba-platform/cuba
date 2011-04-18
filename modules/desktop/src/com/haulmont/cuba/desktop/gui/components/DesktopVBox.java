/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopVBox extends DesktopAbstractContainer {

    public DesktopVBox() {
        layoutAdapter.setFlowDirection(LayoutAdapter.FlowDirection.Y);
    }
}
