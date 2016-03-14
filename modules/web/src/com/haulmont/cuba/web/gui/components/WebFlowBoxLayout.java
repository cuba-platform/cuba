/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.FlowBoxLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaFlowLayout;
import com.vaadin.shared.ui.MarginInfo;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebFlowBoxLayout extends WebAbstractOrderedLayout<CubaFlowLayout> implements FlowBoxLayout {

    public WebFlowBoxLayout() {
        component = new CubaFlowLayout();
    }

    @Override
    public void setMargin(boolean enable) {
        component.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        component.setMargin(new MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
    }

    @Override
    public void setSpacing(boolean enabled) {
        component.setSpacing(enabled);
    }
}