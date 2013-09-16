/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.groupbox;

import com.google.gwt.core.client.GWT;
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
    public void init() {
        super.init();

        getLayoutManager().registerDependency(this, getWidget().expander);
        getLayoutManager().registerDependency(this, getWidget().captionStartDeco);
        getLayoutManager().registerDependency(this, getWidget().captionEndDeco);
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        getLayoutManager().unregisterDependency(this, getWidget().expander);
        getLayoutManager().unregisterDependency(this, getWidget().captionStartDeco);
        getLayoutManager().unregisterDependency(this, getWidget().captionEndDeco);
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
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setCollapsable(getState().collapsable);
        getWidget().setExpanded(getState().expanded);
    }
}