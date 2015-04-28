/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.gridlayout;

import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaGridLayout;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.client.ui.gridlayout.GridLayoutConnector;
import com.vaadin.client.ui.layout.VLayoutSlot;
import com.vaadin.shared.ui.Connect;

/**
 * @author devyatkin
 * @version $Id$
 */
@Connect(CubaGridLayout.class)
public class CubaGridLayoutConnector extends GridLayoutConnector {

    @Override
    public CubaGridLayoutWidget getWidget() {
        return (CubaGridLayoutWidget) super.getWidget();
    }

    protected void setDefaultCaptionParameters(CubaCaptionWidget widget) {
    }

    @Override
    public void updateCaption(ComponentConnector childConnector) {
        // CAUTION copied from GridLayoutConnector.updateCaption(ComponentConnector childConnector)
        VGridLayout layout = getWidget();
        VGridLayout.Cell cell = layout.widgetToCell.get(childConnector.getWidget());
        if (VCaption.isNeeded(childConnector.getState())) {
            VLayoutSlot layoutSlot = cell.slot;
            VCaption caption = layoutSlot.getCaption();
            if (caption == null) {
                // use our own caption widget
                caption = new CubaCaptionWidget(childConnector, getConnection());

                setDefaultCaptionParameters((CubaCaptionWidget)caption);

                Widget widget = childConnector.getWidget();

                layout.setCaption(widget, caption);
            }
            caption.updateCaption();
        } else {
            layout.setCaption(childConnector.getWidget(), null);
            getLayoutManager().setNeedsLayout(this);
        }
    }
}