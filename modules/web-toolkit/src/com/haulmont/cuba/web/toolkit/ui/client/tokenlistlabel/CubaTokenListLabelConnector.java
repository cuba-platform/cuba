/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tokenlistlabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaTokenListLabel;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VPanel;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author devyatkin
 * @version $Id$
 */
@Connect(value = CubaTokenListLabel.class, loadStyle = Connect.LoadStyle.LAZY)
public class CubaTokenListLabelConnector extends PanelConnector {
    @Override
    public CubaTokenListLabelWidget getWidget() {
        return (CubaTokenListLabelWidget) super.getWidget();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        // replace VPanel class names
        Tools.replaceClassNames(getWidget().captionNode, VPanel.CLASSNAME, CubaTokenListLabelWidget.CLASSNAME);
        Tools.replaceClassNames(getWidget().contentNode, VPanel.CLASSNAME, CubaTokenListLabelWidget.CLASSNAME);
        Tools.replaceClassNames(getWidget().bottomDecoration, VPanel.CLASSNAME, CubaTokenListLabelWidget.CLASSNAME);
        Tools.replaceClassNames(getWidget().getElement(), VPanel.CLASSNAME, CubaTokenListLabelWidget.CLASSNAME);
    }

    @Override
    protected Widget createWidget() {
        CubaTokenListLabelWidget widget = GWT.create(CubaTokenListLabelWidget.class);
        widget.handler = new CubaTokenListLabelWidget.TokenListLabelHandler() {
            @Override
            public void remove() {
                getRpcProxy(CubaTokenListLabelServerRpc.class).removeToken();
            }

            @Override
            public void click() {
                getRpcProxy(CubaTokenListLabelServerRpc.class).itemClick();
            }
        };

        return widget;
    }

    @Override
    public CubaTokenListLabelState getState() {
        return (CubaTokenListLabelState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        getWidget().setEditable(getState().editable);
        getWidget().setCanOpen(getState().canOpen);
        getWidget().setText(getState().text);
    }
}
