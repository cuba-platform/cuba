/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.groupbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupBox;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VPanel;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(value = CubaGroupBox.class, loadStyle = Connect.LoadStyle.LAZY)
public class CubaGroupBoxConnector extends PanelConnector {

    @Override
    public CubaGroupBoxWidget getWidget() {
        return (CubaGroupBoxWidget) super.getWidget();
    }

    @Override
    protected CubaGroupBoxWidget createWidget() {
        CubaGroupBoxWidget groupBoxWidget = GWT.create(CubaGroupBoxWidget.class);
        groupBoxWidget.expandHandler = new CubaGroupBoxWidget.ExpandHandler() {
            @Override
            public void expand() {
                getRpcProxy(CubaGroupBoxServerRpc.class).expand();
            }

            @Override
            public void collapse() {
                getRpcProxy(CubaGroupBoxServerRpc.class).collapse();
            }
        };
        return groupBoxWidget;
    }

    @Override
    public CubaGroupBoxState getState() {
        return (CubaGroupBoxState) super.getState();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        // replace VPanel classnames
        Tools.replaceClassNames(getWidget().captionNode, VPanel.CLASSNAME, getWidget().getStylePrimaryName());
        Tools.replaceClassNames(getWidget().captionWrap, VPanel.CLASSNAME, getWidget().getStylePrimaryName());
        Tools.replaceClassNames(getWidget().contentNode, VPanel.CLASSNAME, getWidget().getStylePrimaryName());
        Tools.replaceClassNames(getWidget().bottomDecoration, VPanel.CLASSNAME, getWidget().getStylePrimaryName());
        Tools.replaceClassNames(getWidget().getElement(), VPanel.CLASSNAME, getWidget().getStylePrimaryName());
    }

    @Override
    public void layout() {
        super.layout();

        if (isUndefinedWidth()) {
            // do not set width: 100% for captionEndDeco in CSS
            // it brokes layout with width: AUTO
            getWidget().captionWrap.getStyle().setWidth(getWidget().contentNode.getOffsetWidth(), Style.Unit.PX);
        } else {
            getWidget().captionWrap.getStyle().setWidth(100, Style.Unit.PCT);
        }

        getWidget().captionEndDeco.getStyle().setWidth(100, Style.Unit.PCT);
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setCollapsable(getState().collapsable);
        getWidget().setExpanded(getState().expanded);
    }
}