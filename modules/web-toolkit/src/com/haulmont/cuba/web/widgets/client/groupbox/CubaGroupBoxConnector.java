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

package com.haulmont.cuba.web.widgets.client.groupbox;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.haulmont.cuba.web.widgets.CubaGroupBox;
import com.haulmont.cuba.web.widgets.client.Tools;
import com.vaadin.client.*;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VPanel;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.MarginInfo;

@Connect(CubaGroupBox.class)
public class CubaGroupBoxConnector extends PanelConnector {
    protected boolean widgetInitialized = false;

    @Override
    public CubaGroupBoxWidget getWidget() {
        return (CubaGroupBoxWidget) super.getWidget();
    }

    @Override
    public void init() {
        super.init();

        getWidget().expandHandler = expanded ->
                getRpcProxy(CubaGroupBoxServerRpc.class).expandStateChanged(expanded);
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        if (!getState().showAsPanel && widgetInitialized) {
            LayoutManager layoutManager = getLayoutManager();
            CubaGroupBoxWidget widget = getWidget();

            layoutManager.unregisterDependency(this, widget.captionStartDeco);
            layoutManager.unregisterDependency(this, widget.captionEndDeco);
            layoutManager.unregisterDependency(this, widget.captionTextNode);
        }
    }

    @Override
    public CubaGroupBoxState getState() {
        return (CubaGroupBoxState) super.getState();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (!getState().showAsPanel) {
            // replace VPanel class names
            CubaGroupBoxWidget widget = getWidget();

            Tools.replaceClassNames(widget.captionNode, VPanel.CLASSNAME, widget.getStylePrimaryName());
            Tools.replaceClassNames(widget.captionWrap, VPanel.CLASSNAME, widget.getStylePrimaryName());
            Tools.replaceClassNames(widget.contentNode, VPanel.CLASSNAME, widget.getStylePrimaryName());
            Tools.replaceClassNames(widget.bottomDecoration, VPanel.CLASSNAME, widget.getStylePrimaryName());
            Tools.replaceClassNames(widget.getElement(), VPanel.CLASSNAME, widget.getStylePrimaryName());
        }
    }

    @Override
    public void layout() {
        if (!getState().showAsPanel) {
            layoutGroupBox();
        } else {
            super.layout();
        }
    }

    protected void layoutGroupBox() {
        CubaGroupBoxWidget panel = getWidget();
        LayoutManager layoutManager = getLayoutManager();

        if (isBordersVisible()) {
            int captionWidth = layoutManager.getOuterWidth(panel.captionNode);
            int captionStartWidth = layoutManager.getInnerWidth(panel.captionStartDeco);
            int totalMargin = captionWidth + captionStartWidth;

            panel.captionNode.getStyle().setWidth(captionWidth, Unit.PX);
            panel.captionWrap.getStyle().setPaddingLeft(totalMargin, Unit.PX);
            panel.captionStartDeco.getStyle().setMarginLeft(0 - totalMargin, Unit.PX);
        }

        Profiler.enter("CubaGroupBoxConnector.layout getHeights");
        // Haulmont API get max height of caption components
        int top = layoutManager.getOuterHeight(panel.captionNode);
        top = Math.max(layoutManager.getOuterHeight(panel.captionStartDeco), top);
        top = Math.max(layoutManager.getOuterHeight(panel.captionEndDeco), top);

        int bottom = layoutManager.getInnerHeight(panel.bottomDecoration);
        Profiler.leave("PanelConnector.layout getHeights");

        Style style = panel.getElement().getStyle();
        int paddingTop = 0;
        int paddingBottom = 0;
        if (panel.hasAnyOuterMargin()) {
            Profiler.enter("PanelConnector.layout get values from styles");
            // Clear previously set values

            style.clearPaddingTop();
            style.clearPaddingBottom();
            // Calculate padding from styles
            ComputedStyle computedStyle = new ComputedStyle(panel.getElement());
            paddingTop = computedStyle.getIntProperty("padding-top");
            paddingBottom = computedStyle.getIntProperty("padding-bottom");
            Profiler.leave("PanelConnector.layout get values from styles");
        }

        Profiler.enter("PanelConnector.layout modify style");
        panel.captionWrap.getStyle().setMarginTop(-top, Style.Unit.PX);
        panel.bottomDecoration.getStyle().setMarginBottom(-bottom, Style.Unit.PX);
        style.setPaddingTop(top + paddingTop, Style.Unit.PX);
        style.setPaddingBottom(bottom + paddingBottom, Style.Unit.PX);
        Profiler.leave("PanelConnector.layout modify style");

        // Update scroll positions
        Profiler.enter("PanelConnector.layout update scroll positions");
        panel.contentNode.setScrollTop(panel.scrollTop);
        panel.contentNode.setScrollLeft(panel.scrollLeft);
        Profiler.leave("PanelConnector.layout update scroll positions");

        // Read actual value back to ensure update logic is correct
        Profiler.enter("PanelConnector.layout read scroll positions");
        panel.scrollTop = panel.contentNode.getScrollTop();
        panel.scrollLeft = panel.contentNode.getScrollLeft();
        Profiler.leave("PanelConnector.layout read scroll positions");
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        CubaGroupBoxWidget widget = getWidget();

        if (!widgetInitialized) {
            widget.init();
            if (!getState().showAsPanel) {
                LayoutManager layoutManager = getLayoutManager();
                layoutManager.registerDependency(this, widget.captionStartDeco);
                layoutManager.registerDependency(this, widget.captionEndDeco);
                layoutManager.registerDependency(this, widget.captionTextNode);
            }

            widgetInitialized = true;
        }

        widget.setCollapsable(getState().collapsable);
        widget.setExpanded(getState().expanded);
        widget.setShowAsPanel(getState().showAsPanel);
        if (!getState().showAsPanel) {
            widget.setOuterMargin(new MarginInfo(getState().outerMarginsBitmask));
        }

        if (stateChangeEvent.hasPropertyChanged("caption")) {
            widget.captionNode.getStyle().clearWidth();
            getLayoutManager().setNeedsMeasure(this);
        }
    }

    protected boolean isBordersVisible() {
        CubaGroupBoxWidget panel = getWidget();
        return panel.captionStartDeco.getOffsetWidth() > 0 || panel.captionEndDeco.getOffsetWidth() > 0;
    }
}