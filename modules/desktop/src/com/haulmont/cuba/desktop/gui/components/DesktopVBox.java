/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
        setWidth("100%"); // compatibility with web
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
