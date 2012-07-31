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
public class DesktopVBox extends DesktopAbstractBox implements AutoExpanding {

    public DesktopVBox() {
        layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);
        setWidth("-1px"); // fix layout inside a scrollbox if the width is not set
    }

    @Override
    public boolean expandsWidth() {
        return true;
    }

    @Override
    public boolean expandsHeight() {
        return false;
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        layoutAdapter.setExpandLayout(!heightSize.isOwnSize());
    }
}
