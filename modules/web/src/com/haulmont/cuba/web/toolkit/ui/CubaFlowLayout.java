/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.flowlayout.CubaFlowLayoutState;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Layout;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFlowLayout extends CssLayout implements Layout.MarginHandler, Layout.SpacingHandler {

    @Override
    protected CubaFlowLayoutState getState() {
        return (CubaFlowLayoutState) super.getState();
    }

    @Override
    protected CubaFlowLayoutState getState(boolean markAsDirty) {
        return (CubaFlowLayoutState) super.getState(markAsDirty);
    }

    @Override
    public void setMargin(boolean enabled) {
        setMargin(new MarginInfo(enabled));
    }

    @Override
    public MarginInfo getMargin() {
        return new MarginInfo(getState().marginsBitmask);
    }

    @Override
    public void setMargin(MarginInfo marginInfo) {
        getState().marginsBitmask = marginInfo.getBitMask();
    }

    @Override
    public void setSpacing(boolean spacing) {
        getState().spacing = spacing;
    }

    @Override
    public boolean isSpacing() {
        return getState().spacing;
    }
}