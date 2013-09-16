/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.flowlayout;

import com.vaadin.client.ui.VCssLayout;
import com.vaadin.shared.ui.MarginInfo;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFlowLayoutWidget extends VCssLayout {

    public void setMargin(MarginInfo marginInfo) {
        if (marginInfo != null) {
            // Styles inherited from v-csslayout from base theme
            enableStyleDependentName("margin-top", marginInfo.hasTop());
            enableStyleDependentName("margin-right", marginInfo.hasRight());
            enableStyleDependentName("margin-bottom", marginInfo.hasBottom());
            enableStyleDependentName("margin-left", marginInfo.hasLeft());
        }
    }

    public void setSpacing(boolean spacing) {
        enableStyleDependentName("spacing", spacing);
    }

    public void enableStyleDependentName(String suffix, boolean enable) {
        if (enable)
            addStyleDependentName(suffix);
        else
            removeStyleDependentName(suffix);
    }
}