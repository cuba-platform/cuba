/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.CssLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaCssLayout;

/**
 * @author petunin
 */
public class WebCssLayout extends WebAbstractOrderedLayout<CubaCssLayout> implements CssLayout {

    public WebCssLayout() {
        component = new CubaCssLayout();
    }

    @Override
    public boolean isResponsive(){
        return component.isResponsive();
    }

    @Override
    public void setResponsive(boolean responsive) {
        component.setResponsive(responsive);
    }
}
