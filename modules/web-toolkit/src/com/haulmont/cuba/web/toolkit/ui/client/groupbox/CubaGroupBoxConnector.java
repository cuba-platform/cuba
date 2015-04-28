/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.groupbox;

import com.google.gwt.dom.client.Style;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupBox;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.*;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VPanel;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaGroupBox.class)
public class CubaGroupBoxConnector extends PanelConnector {

    @Override
    public CubaGroupBoxWidget getWidget() {
        return (CubaGroupBoxWidget) super.getWidget();
    }

    @Override
    public void init() {
        super.init();
        CubaGroupBoxWidget widget = getWidget();

        widget.expandHandler = new CubaGroupBoxWidget.ExpandHandler() {
            @Override
            public void expand() {
                getRpcProxy(CubaGroupBoxServerRpc.class).expand();
            }

            @Override
            public void collapse() {
                getRpcProxy(CubaGroupBoxServerRpc.class).collapse();
            }
        };

        LayoutManager layoutManager = getLayoutManager();
        layoutManager.registerDependency(this, widget.captionStartDeco);
        layoutManager.registerDependency(this, widget.captionEndDeco);
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        LayoutManager layoutManager = getLayoutManager();
        CubaGroupBoxWidget widget = getWidget();

        layoutManager.unregisterDependency(this, widget.captionStartDeco);
        layoutManager.unregisterDependency(this, widget.captionEndDeco);
    }

    @Override
    public CubaGroupBoxState getState() {
        return (CubaGroupBoxState) super.getState();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        // replace VPanel class names
        CubaGroupBoxWidget widget = getWidget();

        Tools.replaceClassNames(widget.captionNode, VPanel.CLASSNAME, widget.getStylePrimaryName());
        Tools.replaceClassNames(widget.captionWrap, VPanel.CLASSNAME, widget.getStylePrimaryName());
        Tools.replaceClassNames(widget.contentNode, VPanel.CLASSNAME, widget.getStylePrimaryName());
        Tools.replaceClassNames(widget.bottomDecoration, VPanel.CLASSNAME, widget.getStylePrimaryName());
        Tools.replaceClassNames(widget.getElement(), VPanel.CLASSNAME, widget.getStylePrimaryName());
    }

    @Override
    public void layout() {
        CubaGroupBoxWidget panel = getWidget();

        boolean bordersVisible = panel.captionStartDeco.getOffsetWidth() > 0 || panel.captionEndDeco.getOffsetWidth() > 0;

        Style captionWrapStyle = panel.captionWrap.getStyle();
        if (bordersVisible) {
            if (isUndefinedWidth()) {
                // do not set width: 100% for captionEndDeco in CSS
                // it breaks layout with width: AUTO
                captionWrapStyle.setWidth(WidgetUtil.getRequiredWidth(panel.contentNode), Style.Unit.PX);
            } else {
                captionWrapStyle.setWidth(100, Style.Unit.PCT);
            }

            panel.captionEndDeco.getStyle().setWidth(100, Style.Unit.PCT);

            panel.captionNode.getStyle().clearWidth();

            int captionWidth = WidgetUtil.getRequiredWidth(panel.captionNode);
            int captionStartWidth = WidgetUtil.getRequiredWidth(panel.captionStartDeco);

            // Fix caption width to avoid problems with fractional width of caption text
            panel.captionNode.getStyle().setWidth(captionWidth, Style.Unit.PX);

            captionWrapStyle.setPaddingLeft(captionWidth + captionStartWidth, Style.Unit.PX);
            panel.captionStartDeco.getStyle().setMarginLeft(-captionStartWidth - captionWidth, Style.Unit.PX);
        }

        LayoutManager layoutManager = getLayoutManager();
        Profiler.enter("PanelConnector.layout getHeights");
        // Haulmont API get max height of caption components
        int top = layoutManager.getOuterHeight(panel.captionNode);
        top = Math.max(layoutManager.getOuterHeight(panel.captionStartDeco), top);
        top = Math.max(layoutManager.getOuterHeight(panel.captionEndDeco), top);

        int bottom = layoutManager.getInnerHeight(panel.bottomDecoration);
        Profiler.leave("PanelConnector.layout getHeights");

        Profiler.enter("PanelConnector.layout modify style");
        Style style = panel.getElement().getStyle();
        captionWrapStyle.setMarginTop(-top, Style.Unit.PX);
        panel.bottomDecoration.getStyle().setMarginBottom(-bottom, Style.Unit.PX);
        style.setPaddingTop(top, Style.Unit.PX);
        style.setPaddingBottom(bottom, Style.Unit.PX);
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

        widget.setCollapsable(getState().collapsable);
        widget.setExpanded(getState().expanded);

        if (stateChangeEvent.hasPropertyChanged("caption")) {
            getLayoutManager().setNeedsMeasure(this);
        }
    }
}