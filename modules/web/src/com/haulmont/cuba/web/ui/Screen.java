/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 19:02:39
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.web.components.ComponentsHelper;
import com.itmill.toolkit.ui.ExpandLayout;

public class Screen extends ExpandLayout implements Window
{
    protected ScreenContext screenContext;

    public Screen() {
        super(ExpandLayout.ORIENTATION_VERTICAL);
        setMargin(true);
        setSpacing(true);
    }

    public void add(Component component) {
        addComponent(ComponentsHelper.unwrap(component));
    }

    public void remove(Component component) {
        removeComponent(ComponentsHelper.unwrap(component));
    }

    public void init(ScreenContext context) {
        screenContext = context;
    }

    public boolean onClose() {
        return true;
    }

    public int getVerticalAlIlignment() {
        return ALIGNMENT_VERTICAL_CENTER;
    }

    public void setVerticalAlIlignment(int verticalAlIlignment) {}

    public int getHorizontalAlIlignment() {
        return ALIGNMENT_HORIZONTAL_CENTER;
    }

    public void setHorizontalAlIlignment(int horizontalAlIlignment) {}
}
