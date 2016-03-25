/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
        if (VCaption.isNeeded(childConnector.getState()) || !"".equals(childConnector.getState().description)) {
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