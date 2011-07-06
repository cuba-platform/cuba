/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopHBox extends DesktopAbstractBox implements AutoExpanding {

    public DesktopHBox() {
        layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.X);
    }

    @Override
    public boolean expandsWidth() {
        return false;
    }

    @Override
    public boolean expandsHeight() {
        return true;
    }
}
