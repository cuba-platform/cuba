/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.gui.components.HBoxLayout;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopHBox extends DesktopAbstractBox implements AutoExpanding, HBoxLayout {

    public DesktopHBox() {
        layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.X);
        setHeight("-1px"); // fix layout inside a scrollbox if the height is not set
    }

    @Override
    public boolean expandsWidth() {
        return false;
    }

    @Override
    public boolean expandsHeight() {
        return true;
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        layoutAdapter.setExpandLayout(!widthSize.isOwnSize()); // expand layout if width not -1
    }
}
